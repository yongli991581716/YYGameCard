package com.lordcard.network.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 业务处理线程池
 */
public class ThreadPool {

	private static ExecutorService workerpool = Executors.newCachedThreadPool(); //业务处理线程池

	/**
	 * 开启线程任务
	 * @Title: startWork  
	 * @param @param runnable
	 * @return void
	 * @throws
	 */
	public static void startWork(Runnable runnable){
		workerpool.execute(runnable);
	}
}
