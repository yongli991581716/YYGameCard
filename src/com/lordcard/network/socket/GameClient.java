package com.lordcard.network.socket;

public interface GameClient {

	public abstract void connectFail();

	public abstract boolean isConnected();

	public abstract void waitTimeOut();

	/**
	 * Socket异常
	 * @Title: socketException  
	 * @param 
	 * @return void
	 * @throws
	 */
	public abstract void socketException(Throwable e);

	/**
	 * socket关闭时的处理
	 * @Title: socketClosed  
	 * @param 
	 * @return void
	 * @throws
	 */
	public abstract void socketClosed();

	/**
	 * 发送消息
	 * @Title: sendMsg  
	 * @param @param msg
	 * @return void
	 * @throws
	 */
	public abstract void sendMsg(String msg);

	/**
	 * 接收消息处理
	 * @Title: messageReceived  
	 * @param @param msg	收到的消息
	 * @return void
	 * @throws
	 */
	public abstract void messageReceived(String msg);
	
	public abstract int status();
}
