package com.lordcard.network.socket;

import java.util.concurrent.Executors;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class SocketConfig {

	public static ChannelFactory CHANNEL_FACTORY = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
	public static int TIME_OUT = 4000;		//socket超时时间4秒
	public static int SOLINGER = 1;

	public static boolean isOpenHB = true;		//是否开启心跳检测
	public static int WAIT_TIME_OUT = 18000;	//心跳超时40秒
	public static int HB_TIME = 10000;			//前端向后端发送心跳间隔时间
	public static int SOCKET_RE_TIME = 10000;	//socket消息重发时间
	public static int SOCKET_RE_COUNT = 2;		//最多重发的次数
}
