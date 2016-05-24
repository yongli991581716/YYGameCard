package com.lordcard.network.socket;

import android.text.TextUtils;
import android.util.Log;

import com.lordcard.common.exception.LogUtil;
import com.lordcard.common.util.ComUtils;
import com.lordcard.constant.Constant;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * 消息接收处理
 * @ClassName: SocketHandler   
 * @Description: TODO 
 * @author yinhongbiao   
 * @date 2013-9-16 下午5:05:28
 */
public class SocketHandler extends SimpleChannelUpstreamHandler {

	private SocketClient socketClient;

	public SocketHandler(SocketClient socketClient) {
		this.socketClient = socketClient;
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelConnected(ctx, e);
		ctx.getChannel().write("h"+ComUtils.randomNum(10)+";");
	}

	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelClosed(ctx, e);
		Log.d(Constant.LOG_TAG, "channel关闭了 : "+ ctx.getChannel().getId() );
		if (socketClient != null) {
			socketClient.close();
			socketClient.socketClosed();
		}
	}

	/**
	 * 网络异常
	 */
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		e.getCause().printStackTrace();
		Log.d(Constant.LOG_TAG, "client exceptionCaught : " + e.getChannel().getId());
		Log.d(Constant.LOG_TAG, e.getCause().getMessage());
		LogUtil.err("socket 网络中断异常", e.getCause());
		if (socketClient != null) {
			socketClient.socketException(e.getCause());
		}
	}

	/**
	 * 接收消息
	 */
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (socketClient == null || !socketClient.isConnected()) {
			return;
		}

		HBMgr.refreshHB(); //有消息过来则刷新时间点
		String msg = String.valueOf(e.getMessage());
		//byte[] mes = (byte[]) e.getMessage();
		//String msg = new String(mes,"UTF-8");
		//msg = EncodeUtils.strDecode(msg);
		if (!TextUtils.isEmpty(msg)) {
			socketClient.doWithMsg(msg);
		}
	}

}
