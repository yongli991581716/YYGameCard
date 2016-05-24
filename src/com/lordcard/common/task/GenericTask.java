package com.lordcard.common.task;

import java.util.Observable;
import java.util.Observer;

import android.os.AsyncTask;

import com.lordcard.common.task.base.Feedback;
import com.lordcard.common.task.base.TaskListener;
import com.lordcard.common.task.base.TaskParams;
import com.lordcard.common.task.base.TaskResult;

public abstract class GenericTask extends AsyncTask<TaskParams, Object, TaskResult> implements Observer {

	private TaskListener mListener = null;
	private Feedback mFeedback = null;
	private boolean isCancelable = true;

	abstract protected TaskResult _doInBackground(TaskParams... params);

	/**
	 * 准备运行
	 */
	protected void onPreExecute() {
		super.onPreExecute();
		if (mListener != null) {
			mListener.onPreExecute(this);
		}
		if (mFeedback != null) {
			mFeedback.start("请稍候...");
		}
	}

	@Override
	protected TaskResult doInBackground(TaskParams... params) {
		TaskResult result = _doInBackground(params);
		if (mFeedback != null) {
			mFeedback.update(99);
		}
		return result;
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		super.onProgressUpdate(values);
		if (mListener != null) {
			if (values != null && values.length > 0) {
				mListener.onProgressUpdate(this, values[0]);
			}
		}
		if (mFeedback != null) {
			mFeedback.update(values[0]);
		}
	}

	@Override
	protected void onPostExecute(TaskResult result) {
		super.onPostExecute(result);
		if (mListener != null) {
			mListener.onPostExecute(this, result);
			mListener = null;
		}
		if (mFeedback != null) {
			mFeedback.success("");
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (mListener != null) {
			mListener.onCancelled(this);
		}
	}

	public void update(Observable o, Object arg) {
		if (TaskManager.CANCEL_ALL == (Integer) arg && isCancelable) {
			if (getStatus() == GenericTask.Status.RUNNING) {
				cancel(true);
			}
		}
	}

	public void doPublishProgress(Object... values) {
		super.publishProgress(values);
	}

	public void setFeedback(Feedback feedback) {
		mFeedback = feedback;
	}

	public void setListener(TaskListener taskListener) {
		mListener = taskListener;
	}

	public TaskListener getListener() {
		return mListener;
	}
}
