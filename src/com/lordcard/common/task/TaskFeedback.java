package com.lordcard.common.task;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lordcard.common.task.base.Feedback;
import com.lordcard.constant.Database;

public abstract class TaskFeedback implements Feedback {

	private static TaskFeedback _instance = null;
	public static final int DIALOG_MODE = 0x01;

	public static TaskFeedback getInstance(int type) {
		switch (type) {
			case DIALOG_MODE:
				_instance = DialogFeedback.getInstance();
				break;
		}
		return _instance;
	}

	public void failed(CharSequence text) {}

	public void showProgress(int progress) {}

	public void setIndeterminate(boolean indeterminate) {}
}

/**
 *
 */
class DialogFeedback extends TaskFeedback {

	private boolean isCancel = false;//加载对话框是否消失
	private static DialogFeedback _instance = null;

	public static DialogFeedback getInstance() {
		if (_instance == null) {
			_instance = new DialogFeedback();
		}
		return _instance;
	}

	private ProgressDialog _dialog = null;

	public void start(CharSequence text) {
		_dialog = ProgressDialog.show(Database.currentActivity, "", text, true);
		_dialog.setCancelable(true);
		isCancel(false);
		_dialog.setProgress(ProgressDialog.STYLE_SPINNER);
		_dialog.setIndeterminate(true);
		_dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				Log.i("_dialog", "进度对话框被取消");
				isCancel(true);
			}
		});
		Window window = _dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.9f;// 透明度
		lp.dimAmount = 0.1f;// 黑暗度
		window.setAttributes(lp);
	}

	public void cancel(CharSequence text) {
		if (_dialog != null) {
			isCancel(true);
			_dialog.dismiss();
		}
	}

	public void failed(String prompt) {
		if (_dialog != null) {
			_dialog.dismiss();
		}
		Toast toast = Toast.makeText(Database.currentActivity, prompt, Toast.LENGTH_LONG);
		toast.show();
	}

	public void success(CharSequence text) {
		if (_dialog != null) {
			_dialog.dismiss();
		}
	}

	public void update(Object arg0) {}

	@Override
	public void isCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	@Override
	public boolean hasCancel() {
		return isCancel;
	}
}
