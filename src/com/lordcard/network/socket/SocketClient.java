package com.lordcard.network.socket;

import android.text.TextUtils;
import android.util.Log;

import com.lordcard.constant.Constant;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import java.net.InetSocketAddress;

public class SocketClient {

	private StringBuffer RECEIVE_MSG = new StringBuffer();
	private Channel channel;
	private ClientBootstrap bootstrap;

	private ClientAdapter clientAdapter;

	public SocketClient(ClientAdapter clientAdapter) {
		this.clientAdapter = clientAdapter;
	}

	public void startSocket(String host, int port) {
		try {
			bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory());
			bootstrap.setPipelineFactory(new ServerPipelineFactory(this, 16));

			bootstrap.setOption("tcpNoDelay", true);
			bootstrap.setOption("keepAlive", true);
			bootstrap.setOption("reuseAddress", true);
			bootstrap.setOption("soLinger", SocketConfig.SOLINGER);
			bootstrap.setOption("connectTimeoutMillis", SocketConfig.TIME_OUT); // 超时时间
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
			future.awaitUninterruptibly(); // 等待返回的ChannelFuture
			if (future.isSuccess()) { // 以确定建立连接的尝试是否成功。
				HBMgr.startHb(clientAdapter.getGameClient());
				channel = future.getChannel();
			} else {
				clientAdapter.connectFail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			clientAdapter.connectFail();
		}
	}

	private static class ServerPipelineFactory implements ChannelPipelineFactory {
		private SocketClient socketClient;
		private OrderedMemoryAwareThreadPoolExecutor executor;

		public ServerPipelineFactory(SocketClient socketClient, int poolSize) {
			this.socketClient = socketClient;
			executor = new OrderedMemoryAwareThreadPoolExecutor(poolSize, 0, 0);
		}

		public ChannelPipeline getPipeline() throws Exception {
			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast("encode", new StringEncoder()); //字符串编码
			pipeline.addLast("decode", new StringDecoder()); //字条串解码
			pipeline.addLast("execution", new ExecutionHandler(executor));
			pipeline.addLast("handler", new SocketHandler(socketClient)); //指令处理
			return pipeline;
		}

	}

	public synchronized void doWithMsg(String msg) {
		if (RECEIVE_MSG.length() > 0) {
			msg = RECEIVE_MSG.toString() + msg;
			RECEIVE_MSG = new StringBuffer();
		}	
		String msgList[] = splitReceiveMsg(msg);
		for (int i = 0; i < msgList.length; i++) {
			messageReceived(msgList[i]);
		}
	}

	/**
	 * 解析服务器返回的指令列表
	 * 
	 * @param str
	 * @return
	 */
	public String[] splitReceiveMsg(String str) {
		str = str.replaceAll(" ", "");
		if (!str.endsWith(";")) { // 不是}; 号结尾 则表示收到的信息不完整
			if (str.lastIndexOf(";") == -1) { // 单条指令不完整
				RECEIVE_MSG.append(str);
				str = "";
			} else { // 多条指令 最后一条不完整
				String tempStr = str.substring(0, str.lastIndexOf(";"));
				RECEIVE_MSG.append(str.substring(str.lastIndexOf(";") + 1));
				str = tempStr;
			}
		}
		String list[] = str.split(";");
		return list;
	}

	/**
	 * 是否在连接判断
	 * @Title: isConnected  
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean isConnected() {
		if (channel != null && channel.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * 接收消息处理
	 * @Title: messageReceived  
	 * @param @param msg	收到的消息
	 * @return void
	 * @throws
	 */
	public void messageReceived(String msg) {
		/*try {
			msg = HURLEncoder.encode(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Log.e(Constant.LOG_TAG, "<=== ("+channel.getId()+"): " + msg);
		clientAdapter.messageReceived(msg);
	}

	/**
	 * Socket异常
	 * @Title: socketException  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void socketException(Throwable e) {
		clientAdapter.socketException(e);
	}

	/**
	 * socket关闭时的处理
	 * @Title: socketClosed  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void socketClosed() {
		clientAdapter.socketClosed();
	}

	/**
	 * 发送消息
	 * @Title: sendMsg  
	 * @param @param msg
	 * @return void
	 * @throws
	 */
	public void sendMsg(String msg) {
		try {
			if (!TextUtils.isEmpty(msg) && isConnected()) {
				Log.d(Constant.LOG_TAG, "===> : "+ channel.getId() + " | " + msg);
				channel.write(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 主动关闭的操作
	 * @Title: close  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void close() {
		try {
			RECEIVE_MSG = new StringBuffer();
			HBMgr.stopHB();
			if (channel != null) {
				if (channel.isConnected()) {
					channel.unbind();
					channel.close();
					channel.getCloseFuture().awaitUninterruptibly();
				}
				bootstrap.releaseExternalResources();
				channel = null;
				bootstrap = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ClientAdapter getClientAdapter() {
		return clientAdapter;
	}

	public void setClientAdapter(ClientAdapter clientAdapter) {
		this.clientAdapter = clientAdapter;
	}

}
