package com.lordcard.network.cmdmgr;

import com.ylly.playcard.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.text.TextUtils;
import android.util.Log;

import com.lordcard.common.exception.CrashApplication;
import com.lordcard.common.exception.LogUtil;
import com.lordcard.common.schedule.AutoTask;
import com.lordcard.common.schedule.ScheduledTask;
import com.lordcard.common.util.ActivityUtils;
import com.lordcard.common.util.Base64Util;
import com.lordcard.common.util.DateUtil;
import com.lordcard.common.util.DialogUtils;
import com.lordcard.common.util.JsonHelper;
import com.lordcard.constant.CacheKey;
import com.lordcard.constant.Constant;
import com.lordcard.entity.GameUser;
import com.lordcard.network.http.GameCache;
import com.lordcard.network.socket.ClientAdapter;
import com.lordcard.network.socket.GameClient;
import com.lordcard.network.socket.HBMgr;
import com.lordcard.network.socket.HURLEncoder;
import com.lordcard.network.socket.ICallback;
import com.lordcard.network.socket.SocketConfig;

public class Client implements GameClient {

	public static final int DEFAULT = 0;
	public static final int STARTING = 5;
	public static final int JOINING = 10;
	public static final int PLAYING = 20;
	private int relinkCount = 0; //当前重连的次数
	private long lastRelinkTime = 0;
	private static final int relinkMaxCount = 5; //重连的次数
	private static final int relinkTime = 3000; //每次重连间隔时间5秒
	private String host; //socket连接ip
	private int port; //socket连接端口
	private int status; //状态   0：默认	10：加入过程 	20：打牌中   
	private AutoTask sendTask; //消息处理任务
	private ClientAdapter clientAdapter;
	private ICallback callback;
	/** 记录已处理的消息*/
	private List<String> hasWithCmdList = new ArrayList<String>();
	/**消息序列对应列表：自己发送的消息key: out_seq ,收到的消息:in_uid_seq */
	private ConcurrentHashMap<String, CmdDetail> seqCmdMap = new ConcurrentHashMap<String, CmdDetail>();
	/**待发送的打牌的指令 */
	private ConcurrentLinkedQueue<CmdDetail> cmdqueue = new ConcurrentLinkedQueue<CmdDetail>();

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		clientAdapter = new ClientAdapter(this, host, port);
	}

	/**
	 * 开始游戏
	 * @Title: startGame  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void startGame() {
		sendTask = new AutoTask() {

			public void run() {
				CmdDetail cmd = null;
				while (!cmdqueue.isEmpty()) {
					/////////////////////////////////////直接发送，不用校验返回////////////////////////
					CmdDetail tcmd = cmdqueue.poll(); //先取出来
					//判断是否可处理的命令
					if (tcmd == null || TextUtils.isEmpty(tcmd.getCmd()) || tcmd.isHasDo()) {
						continue;
					}
					/////////////////////////////////////加入序列发送，需要校验消息返回////////////////////////
					//					CmdDetail tcmd = cmdqueue.peek(); //先取出来
					//					
					//					//判断是否可处理的命令
					//					if (tcmd == null || TextUtils.isEmpty(tcmd.getCmd()) || tcmd.isHasDo()) {
					//						cmdqueue.poll();	//移除
					//						continue;
					//					}
					//发送的时间差
					long sendSpace = System.currentTimeMillis() - tcmd.getTime();
					//没有处理的消息 或者需要重发的消息
					if (tcmd.getTime() == 0 || sendSpace > SocketConfig.SOCKET_RE_TIME) {
						cmd = tcmd;
						break;
					}
				}
				if (cmd != null) {
					//消息重发次数超过最大次数  消息处理超时
					if (cmd.getCount() >= SocketConfig.SOCKET_RE_COUNT) {
						doWithCmdTimeOut();
						return;
					}
					//允许发送的消息
					if (!cmd.isHasDo()) {
						cmd.setTime(System.currentTimeMillis()); //更新发送时间
						cmd.setCount(cmd.getCount() + 1); //记录发送次数
						hasWithCmdList.add(DateUtil.getTimesDate() + ":out_" + cmd.getSeq() + "_" + cmd.getCount() + "_" + cmd.getCmd());
						sendMsg(cmd.toJson());
					}
				}
			}
		};
		ScheduledTask.addRateTask(sendTask, 1000);
	}

	/**
	 * 接收消息处理
	 * @Title: messageReceived  
	 * @param @param msg	收到的消息
	 * @return void
	 * @throws
	 */
	public void messageReceived(String msg) {
		if (TextUtils.isEmpty(msg))
			return;
		try {
			CmdDetail detail = JsonHelper.fromJson(msg, CmdDetail.class);
			if (detail == null)
				return;	
			detail.urlDecode();//解码字符串
			String cmd = detail.getCmd();
			hasWithCmdList.add(DateUtil.getTimesDate() + ":in." + cmd);
			if (CmdUtils.CMD_LINK.equals(cmd)) { // 连接成功
				return;
			}
			if (CmdUtils.CMD_HB.equals(cmd)) { // 心跳纪录时间点
				return;
			}
			if (CmdUtils.CMD_ERR_RJOIN.equals(cmd)) { //玩家加入时，服务器暂停维护提示
				if (CmdUtils.CMD_SER_STOP.equals(detail.getDetail())) {
					String tipMsg = CrashApplication.getInstance().getResources().getString(R.string.gs_update);
					DialogUtils.mesToastTip(tipMsg);
					ActivityUtils.finishAcitivity();
					return;
				}
			}
			//系统公告和聊天
			/*if(cmd.equals(CmdUtils.CMD_MES)||cmd.equals(CmdUtils.CMD_CHAT))
			{
				String mes = detail.getDetail();
				//String mess =new String(Base64Util.decode(mes),"UTF-8");
				String mess =HURLEncoder.readUTF(mes);
				detail.setDetail(mess);
				return;
			}*/
			//消息客户端ID
			//			String androidId = detail.getAndroId();
			//			if(!TextUtils.isEmpty(androidId)){
			//				if(ActivityUtils.getAndroidId().equals(androidId)){		//我自己返回的消息
			//					CmdDetail outCmd = seqCmdMap.get("out_"+detail.getSeq());
			//					if(outCmd.isHasDo()){	//当前消息已处理 直接返回
			//						return;
			//					}
			//					outCmd.setHasDo(true);
			//					cmdqueue.remove(outCmd);
			//				}else{						//收到的是别人发送过来的消息
			//					String inKey = "in_"+detail.getAndroId()+"_"+detail.getSeq();
			//					CmdDetail inCmd = seqCmdMap.get(inKey);
			//					if(inCmd != null){	//其他人当前发送的消息已接收处理
			//						return;
			//					}else{
			//						
			//					}
			//					seqCmdMap.put(inKey,detail);		//记录当前处理的消息
			//				}
			//			}
			if (callback != null) {
				callback.messageHandler(detail);
			}
//			String mess_test =new String(Base64Util.decode(detail.getMes()),"UTF-8");
//			System.out.println(mess_test);
		} catch (Exception e) {
			LogUtil.err("socket消息处理出错 :  msg :" + msg, e);
		}
	}

	/**
	 * 重连游戏
	 * @Title: relinkGame  
	 * @param 
	 * @return void
	 * @throws
	 */
	private synchronized void relinkGame() {
		boolean canRelink = false;
		try {
			if (this.relinkCount < relinkMaxCount) {
				canRelink = true;
			}
			if (!canRelink) { //不用重连时
				destory();
				DialogUtils.mesToastTip("您的网络太不给力，无法连接到网络，请稍候再试!");
				ActivityUtils.finishAcitivity();
				return;
			}
			//			if (relinkCount != 0) {
			try {
				Thread.sleep(relinkTime);
			} catch (Exception e) {
				long bt = System.currentTimeMillis() - this.lastRelinkTime;
				if (bt < relinkTime) {
					return;
				}
			}
			//			}
			this.relinkCount++;
			if (callback != null) {
				CmdDetail cmd = new CmdDetail();
				cmd.setCmd(CmdUtils.CMD_SLOW); //网络缓慢
				cmd.setDetail(CmdUtils.MY); //我自己
				callback.messageHandler(cmd);
			} else {
				destory();
				ActivityUtils.finishAcitivity();
				return;
			}
			Log.d(Constant.LOG_TAG, "重连  " + relinkCount);
			//			close();
			//			startSocket(host, port);
			clientAdapter.closeSocket();
			clientAdapter = null;
			clientAdapter = new ClientAdapter(this, host, port);
			if (isConnected()) { //发重连的命令
				GameUser gu = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
				CmdDetail detail = new CmdDetail();
				detail.setCmd(CmdUtils.CMD_DOLK);
				detail.setToken(gu.getLoginToken());
				detail.setVersion(ActivityUtils.getVersionName());
				detail.setAndroId(ActivityUtils.getAndroidId());
				sendCmd(detail);
				this.lastRelinkTime = System.currentTimeMillis();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canRelink) { //不用重连时
				HBMgr.startHb(Client.this);
			}
		}
	}

	/**
	 * 一局结束
	 * @Title: gameOver  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void gameOver() {
		status = DEFAULT;
		seqCmdMap.clear();
		cmdqueue.clear();
		if (sendTask != null) {
			sendTask.stop(true);
			sendTask = null;
		}
		this.setCallback(null);
	}

	/**
	 * Client销毁
	 * @Title: destory  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void destory() {
		status = DEFAULT;
		lastRelinkTime = 0L;
		relinkCount = 0;
		gameOver(); //清除数据
		if (null != clientAdapter) {
			clientAdapter.closeSocket(); //关闭Socket
		}
	}

	/**
	 * 发送消息
	 * @Title: sendMsg  
	 * @param @param detail
	 * @return void
	 * @throws
	 */
	public void sendCmd(CmdDetail detail) {
		if (detail == null)
			return;
		detail.setTag("out");
		if (detail.getType() == CmdDetail.PLAY) { //如果是打牌的消息
			detail.setSeq(ClientCmdMgr.getCmdSeq());
			cmdqueue.add(detail);
			seqCmdMap.put("out_" + detail.getSeq(), detail);
		} else {
			hasWithCmdList.add(DateUtil.getTimesDate() + ":out_" + detail.getCmd());
			sendMsg(detail.toJson());
		}
	}

	/**
	 * 发送消息
	 * @Title: sendMsg  
	 * @param @param msg
	 * @return void
	 * @throws
	 */
	public void sendMsg(String msg) {
		if (clientAdapter != null)
			clientAdapter.sendMsg(msg);
	}

	/**
	 * 消息处理超时
	 * @Title: doWithCmdTimeOut  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void doWithCmdTimeOut() {
		clientAdapter.closeSocket();
		socketClosed();
	}

	/**
	 * 连接失败
	 * @Title: connectFail  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void connectFail() {}

	/**
	 * 是否在连接判断
	 * @Title: isConnected  
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean isConnected() {
		return clientAdapter.isConnected();
	}

	/**
	 * socket关闭时的处理
	 * @Title: socketClosed  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void socketClosed() {
		Log.d(Constant.LOG_TAG, "===== socket 关闭了 =====" + this.status);
		//正在游戏时断线 || this.status == STARTING
		if ((this.status == PLAYING || this.status == STARTING) && ActivityUtils.isGameView()) { //正在打牌时自动重连
			relinkGame();
		} else {
			if (this.status == STARTING) {
				DialogUtils.mesToastTip("请在良好网络环境下游戏!");
			}
			
			//在游戏界面时直接退出
			if(ActivityUtils.isGameView()){
				ActivityUtils.finishAcitivity();
			}
			
			destory();
		}
	}

	/**
	 * Socket异常
	 * @Title: socketException  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void socketException(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public int status() {
		return this.status;
	}

	/**
	 * 心跳检测超时
	 * @Title: waitTimeOut  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void waitTimeOut() {
		doWithCmdTimeOut();
	}

	public void setCallback(ICallback callback) {
		this.callback = callback;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRelinkCount() {
		return relinkCount;
	}

	public void setRelinkCount(int relinkCount) {
		if (relinkCount <= 0) {
			lastRelinkTime = 0;
		}
		this.relinkCount = relinkCount;
	}

	public ClientAdapter getClientAdapter() {
		return clientAdapter;
	}

	public void setClientAdapter(ClientAdapter clientAdapter) {
		this.clientAdapter = clientAdapter;
	}
}
