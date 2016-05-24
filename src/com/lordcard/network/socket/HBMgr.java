package com.lordcard.network.socket;

import android.util.Log;

import com.lordcard.common.schedule.AutoTask;
import com.lordcard.common.schedule.ScheduledTask;
import com.lordcard.constant.Constant;

/**
 * 心跳管理
 * @ClassName: HBMgr   
 * @date 2013-9-16 下午3:23:22
 */
public class HBMgr {

	private static long lastTime; //最后一次心跳时间
	private static AutoTask hbTask;
	private static GameClient gameClient;

	public static void startHb(GameClient client) {
		if (!SocketConfig.isOpenHB)
			return; //不开启心跳直接退出

		stopHB();
		Log.d(Constant.LOG_TAG, "开启心跳  startTask");
		lastTime = System.currentTimeMillis();
		gameClient = client;

		hbTask = new AutoTask() {
			public void run() {
				try {
					//游戏时退出当前界面
					if (lastTime == 0)
						return;

					long now = System.currentTimeMillis();
					long btime = now - lastTime;

					if (btime > SocketConfig.HB_TIME) {
						Log.d(Constant.LOG_TAG, lastTime + " | " + btime);
						sendHb();
					}

					//					//当前socket已关闭了。则自动重连
					//					if(gameClient != null && !gameClient.isConnected()){
					//						gameClient.waitTimeOut();
					//					}

					if (btime > SocketConfig.WAIT_TIME_OUT) {
						if (gameClient != null) {
							gameClient.waitTimeOut();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		ScheduledTask.addRateTask(hbTask, 4000);
	}

	/**
	 * 更新心跳时间
	 * @Title: refreshHB  
	 * @param 
	 * @return void
	 * @throws
	 */
	public static void refreshHB() {
		lastTime = System.currentTimeMillis();
	}

	public static void stopHB() {
		Log.d(Constant.LOG_TAG, "停止心跳 stopHB");
		lastTime = 0;
		if (hbTask != null) {
			hbTask.stop(true);
			hbTask = null;
		}
	}

	private static void sendHb() {
		if (gameClient != null) {
			String hb = "h;";
			gameClient.sendMsg(hb);
		}
	}
}
