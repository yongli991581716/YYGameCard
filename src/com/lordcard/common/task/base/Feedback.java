package com.lordcard.common.task.base;

/**
 * 任务状态反馈 common.task.Feedback
 * 
 * @author yinhb <br/>
 *         create at 2013 2013-2-5 下午3:29:16
 */
public interface Feedback {

	public void isCancel(boolean isCancel);

	public boolean hasCancel();

	public void start(CharSequence text);

	public void cancel(CharSequence text);

	public void success(CharSequence text);

	public void failed(CharSequence text);

	public void update(Object arg0);

	public void setIndeterminate(boolean indeterminate);
}
