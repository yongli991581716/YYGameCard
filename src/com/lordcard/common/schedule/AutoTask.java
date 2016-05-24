package com.lordcard.common.schedule;

import java.util.concurrent.ScheduledFuture;

public abstract class AutoTask implements Runnable {

	private ScheduledFuture<?> scheduledFuture;

	public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		this.scheduledFuture = scheduledFuture;
	}

	/**
	 * Returns true if this task completed.
	 * Completion may be due to normal termination, an exception, or cancellation -- in all of these cases, 
	 * this method will return true.
	 * @Title: isDone  
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean isDone() {
		if (scheduledFuture != null) {
			return scheduledFuture.isDone();
		}
		return true;
	}

	public boolean isCancelled() {
		if (scheduledFuture != null) {
			scheduledFuture.isCancelled();
		}
		return true;
	}

	/**
	 * 关闭任务
	 * @Title: stop  
	 * @param @param mayInterruptIfRunning true 正在执行的中断,false等待完成
	 * @return void
	 * @throws
	 */
	public void stop(boolean mayInterruptIfRunning) {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(mayInterruptIfRunning);
		}
		scheduledFuture = null;
	}

}
