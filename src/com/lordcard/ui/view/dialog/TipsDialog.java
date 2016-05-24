/**
 * BaseDialog.java [v 1.0.0]
 * classes : com.lordcard.ui.view.dialog.BaseDialog
 * auth : yinhongbiao
 * time : 2013 2013-3-25 下午5:32:42
 */
package com.lordcard.ui.view.dialog;

import com.ylly.playcard.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lordcard.common.task.GenericTask;
import com.lordcard.common.task.TaskFeedback;
import com.lordcard.common.task.TaskManager;
import com.lordcard.common.util.MultiScreenTool;

/**
 * com.lordcard.ui.view.dialog.AlertDialog
 * 
 * @author Administrator <br/>
 *         create at 2013 2013-3-25 下午5:32:42
 */
public class TipsDialog extends Dialog implements OnClickListener {

	private TaskFeedback feedback = TaskFeedback.getInstance(TaskFeedback.DIALOG_MODE);
	protected TaskManager taskManager = new TaskManager();
	private Context context;
	private TextView showText;
	private MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();
	private RelativeLayout layout;
	private GenericTask rjoinTask;

	protected TipsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}

	public TipsDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tips_dialog);
		layout(context);
		layout = (RelativeLayout) findViewById(R.id.mm_layout);
		mst.adjustView(layout);
	}

	/**
	 * 布局
	 */
	private void layout(final Context context) {
		Button cancel = (Button) findViewById(R.id.common_cancel);
		Button ok = (Button) findViewById(R.id.common_ok);
		showText = (TextView) findViewById(R.id.common_text);
		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.common_cancel:
				mst.unRegisterView(layout);
				dismiss();
				cancelClick();
				break;
			case R.id.common_ok:
				mst.unRegisterView(layout);
				dismiss();
				okClick();
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 重写返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			cancelClick();
		}
		return true;
	}

	public void setText(String content) {
		showText.setText(content);
	}

	/** 确定 */
	public void okClick() {};

	@Override
	public void dismiss() {
		super.dismiss();
	}

	/** 取消 */
	public void cancelClick() {
		dismiss();
	};
}
