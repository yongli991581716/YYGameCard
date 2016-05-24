package com.lordcard.network.socket;

public class ClientAdapter {

	private GameClient gameClient;
	private SocketClient socketClient;

	public ClientAdapter(GameClient client, String host, int port) {
		this.gameClient = client;
		socketClient = new SocketClient(this);
		socketClient.startSocket(host, port);
	}

	public void connectFail() {
		if (gameClient != null) {
			gameClient.connectFail();
		}
	}

	public boolean isConnected() {
		if (socketClient != null) {
			return socketClient.isConnected();
		}
		return false;
	}

	public void waitTimeOut() {
		if (gameClient != null) {
			gameClient.waitTimeOut();
		} else {
			closeSocket();
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
		if (gameClient != null) {
			gameClient.socketException(e);
		} else {
			closeSocket();
		}
	}

	/**
	 * socket关闭时的处理
	 * @Title: socketClosed  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void socketClosed() {
		if (gameClient != null) {
			gameClient.socketClosed();
		}
	}

	/**
	 * 接收消息处理
	 * @Title: messageReceived  
	 * @param @param msg	收到的消息
	 * @return void
	 * @throws
	 */
	public void messageReceived(String msg) {
		if (gameClient != null) {
			gameClient.messageReceived(msg);
		} else {
			closeSocket();
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
		if (socketClient != null) {
			socketClient.sendMsg(msg);
		}
	}

	/**
	 * 主动关闭的操作
	 * @Title: close  
	 * @param 
	 * @return void
	 * @throws
	 */
	public void closeSocket() {
		if (socketClient != null) {
			socketClient.close();
		}
		unRegisterAdapter();
	}

	public void unRegisterAdapter() {
//		socketClient.setClientAdapter(null);
		socketClient = null;
//		client.setClientAdapter(null);
		gameClient = null;
	}

	public GameClient getGameClient() {
		return gameClient;
	}
}
