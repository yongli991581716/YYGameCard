package com.lordcard.common.task.base;

import com.lordcard.common.task.GenericTask;

/**
 * 任务事件监听接口 common.task.TaskListener
 * 
 * @author yinhb <br/>
 *         create at 2013 2013-2-5 下午4:10:50
 */
public interface TaskListener {

	String getName();

	/**
	 * 准备运行时操作
	 * 
	 * @param task
	 */
	void onPreExecute(GenericTask task);

	void onPostExecute(GenericTask task, TaskResult result);

	void onProgressUpdate(GenericTask task, Object param);

	void onCancelled(GenericTask task);
}
