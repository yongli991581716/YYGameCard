package com.lordcard.network.cmdmgr;


public class ClientCmdMgr {

	private static long seq = 1; //消息序列号  一直递增

	private static Client client = null;

	/**
	 * Client 结束(退出游戏界面)
	 * @Title: destory  
	 * @param 
	 * @return void
	 * @throws
	 */
	public static void closeClient() {
		if (client != null) {
			client.destory();
		}
		client = null;
	}

	/**
	 * 设置Client状态
	 * @Title: setClientStatus  
	 * @param 
	 * @return void
	 * @throws
	 */
	public static void setClientStatus(int status) {
		if (client != null) {
			client.setStatus(status);
		}
	}

	/**
	 * 重连次数归零
	 * @Title: resetRelinkCount  
	 * @param 
	 * @return void
	 * @throws
	 */
	public static void resetRelinkCount() {
		client.setRelinkCount(0);
	}

	/**
	 * 发送消息
	 * @Title: sendCmd  
	 * @param @param detail
	 * @return void
	 * @throws
	 */
	public static void sendCmd(CmdDetail detail) {
		if (client != null) {
			client.sendCmd(detail);
		}
	}

	/**
	 * 一局结束
	 * @Title: gameOver  
	 * @param 
	 * @return void
	 * @throws
	 */
	public static void gameOver() {
		if (client != null) {
			client.gameOver();
		}
	}

	public synchronized static long getCmdSeq() {
		return seq++;
	}
}
