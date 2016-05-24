package com.lordcard.ui.view.dialog;

import com.ylly.playcard.R;

import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lordcard.common.util.ActivityUtils;
import com.lordcard.common.util.MultiScreenTool;

/**
 * IQ升级提示对话框
 * @author Administrator
 *
 */
public class GameIqUpgradeDialog extends Dialog implements OnClickListener {

	private Context context;
	private TextView  contentTv;//内容Tv
	private ImageView headIv;//头像Iv
	private Button helpBtn, acceptBtn;
	private Handler handler;
	private Map<String,String> headPath;
	private boolean isDiZhu;
	private String gender;
	private String content;
	private int what;//用于发送消息
	// 适应多屏幕的工具
	private MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();


	public GameIqUpgradeDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
	}

	public GameIqUpgradeDialog(Context context, Handler handler, String content,String gender,boolean isDiZhu,Map<String,String> headPath,int what) {
		super(context, R.style.dialog);
		this.context = context;
		this.handler = handler;
		this.headPath=headPath;
		this.isDiZhu=isDiZhu;
		this.gender=gender;
		this.content=content;
		this.what=what;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_iq_upgrade_dialog);
		layout(context);
	}

	/**
	 * 布局
	 * 
	 * @param context
	 */
	private void layout(final Context context) {
		mst.adjustView(findViewById(R.id.giud_layout));
		contentTv = (TextView) findViewById(R.id.giud_content_text);
		contentTv.setText(""+content);
		headIv = (ImageView) findViewById(R.id.giud_head_iv);
		if(!TextUtils.isEmpty(gender) && null != headPath){
			ActivityUtils.setHead(context,headIv, gender, isDiZhu, headPath,false);//设置头像
		}
		helpBtn = (Button) findViewById(R.id.giud_help);
		helpBtn.setOnClickListener(this);
		acceptBtn = (Button) findViewById(R.id.giud_accept);
		acceptBtn.setOnClickListener(this);
	}

	public void setContentTv(String content) {
		contentTv.setText(content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.giud_help://帮助
			handler.sendEmptyMessage(what);
			break;
		case R.id.giud_accept://接受
			dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void dismiss() {
		mst.unRegisterView(findViewById(R.id.giud_layout));
		super.dismiss();
	}
}
