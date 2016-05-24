package com.lordcard.ui.view.dialog;

import com.ylly.playcard.R;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.lordcard.common.util.AudioPlayUtils;
import com.lordcard.common.util.ImageUtil;
import com.lordcard.common.util.MultiScreenTool;
import com.lordcard.common.util.PreferenceHelper;

public class SettingDialog extends Dialog implements OnClickListener {

	private Context context;
	private RelativeLayout mainLayout;
	private AudioManager audiomanage;
	private SeekBar music;
	private MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();
	private Button zhendong,jingyin;//震动，静音
	private Button sure;//确定
	private Button arrowBtn;//手势引导
	private Button cancel;//取消
	private Button bgMusic;//背景音乐

	protected SettingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
		// layout(context);
	}

	public SettingDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;

	}

	/**
	 * 设置声音进度条进度
	 */
	public void setPro() {
		int currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
		PreferenceHelper.getMyPreference().getEditor().putInt("music", currentVolume).commit();
		music.setProgress(currentVolume);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_tip);
		mainLayout = (RelativeLayout) findViewById(R.id.setting_tip_layout);
		// mainLayout.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.alert_bg));
		mst.adjustView(mainLayout);
		layout(context);
	}

	public void setDismiss() {

	}

	/**
	 * 布局
	 * 
	 * @param context
	 */
	private void layout(final Context context) {
		zhendong = (Button) findViewById(R.id.checkzhendong);
		jingyin= (Button) findViewById(R.id.jingyin);
		bgMusic = (Button) findViewById(R.id.bgmusic);
		music = (SeekBar) findViewById(R.id.musicControl);
		sure = (Button) findViewById(R.id.sure);
		cancel = (Button) findViewById(R.id.cancel);
		arrowBtn=(Button) findViewById(R.id.arrow);

		// 设置初始化
		if (PreferenceHelper.getMyPreference().getSetting().getBoolean("zhendong", true)) {
			zhendong.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.open, true));
		} else {
			zhendong.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.close, true));
		}
		if (PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
			jingyin.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.open, true));
		} else {
			jingyin.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.close, true));
		}

		if (PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true)) {
			bgMusic.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.open, true));
		} else {
			bgMusic.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.close, true));
		}
		
		if (PreferenceHelper.getMyPreference().getSetting().getBoolean("bgmusic", true)) {
			arrowBtn.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.open, true));
		} else {
			arrowBtn.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.close, true));
		}

		audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int max = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		music.setMax(max);
		music.setProgress(PreferenceHelper.getMyPreference().getSetting().getInt("music", 0));
		music.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				PreferenceHelper.getMyPreference().getEditor().putInt("music", progress).commit();
				AudioPlayUtils.getInstance().SetVoice(progress);
			}
		});
		// 设置监听器
		zhendong.setOnClickListener(this);
		jingyin.setOnClickListener(this);
		bgMusic.setOnClickListener(this);
		sure.setOnClickListener(this);
		cancel.setOnClickListener(this);
		arrowBtn.setOnClickListener(this);
	}

	@Override
	public void dismiss() {
		mst.unRegisterView(mainLayout);
		super.dismiss();
		ImageUtil.releaseDrawable(mainLayout.getBackground());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkzhendong://震动
			boolean zhengDong=PreferenceHelper.getMyPreference().getSetting().getBoolean("zhendong", true);
			PreferenceHelper.getMyPreference().getEditor().putBoolean("zhendong", !zhengDong).commit();
			
			if (PreferenceHelper.getMyPreference().getSetting().getBoolean("zhendong", true)) {
				zhendong.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.open,true));
			} else {
				zhendong.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.close,true));
			}
			break;
		case R.id.jingyin://静音
			boolean jingYin=PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false);
			PreferenceHelper.getMyPreference().getEditor().putBoolean("jingyin", !jingYin).commit();
			
			if (PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
				jingyin.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.open,true));
			} else {
				jingyin.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.close,true));
			}
			if (!PreferenceHelper.getMyPreference().getSetting().getBoolean("bgmusic", true) || PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
				AudioPlayUtils.getInstance().stopBgMusic();
			} else {
				if (!AudioPlayUtils.getInstance().isBgisPlaying()) {
					AudioPlayUtils.getInstance().playBgMusic(R.raw.mg_bg);
				}
			}
			break;
		case R.id.bgmusic://背景音乐
			boolean beijing=PreferenceHelper.getMyPreference().getSetting().getBoolean("bgmusic", true);
			PreferenceHelper.getMyPreference().getEditor().putBoolean("bgmusic", !beijing).commit();
			
			if (PreferenceHelper.getMyPreference().getSetting().getBoolean("bgmusic", true)) {
				bgMusic.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.open,true));
			} else {
				bgMusic.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.close,true));
			}
			if (!PreferenceHelper.getMyPreference().getSetting().getBoolean("bgmusic", true) || PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
				AudioPlayUtils.getInstance().stopBgMusic();
			} else {
				if (!AudioPlayUtils.getInstance().isBgisPlaying()) {
					AudioPlayUtils.getInstance().playBgMusic(R.raw.mg_bg);
				}
			}
			break;
		case R.id.cancel://取消
			dismiss();
			break;
		case R.id.sure://保存
			PreferenceHelper.getMyPreference().getEditor().commit();
			AudioPlayUtils.getInstance().SetVoice(PreferenceHelper.getMyPreference().getSetting().getInt("music", 0));
			dismiss();
			break;
		case R.id.arrow://手势
			boolean shoushitishi=PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true);
			PreferenceHelper.getMyPreference().getEditor().putBoolean("shoushi", !shoushitishi).commit();//手势总开关
			if (PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true)) {
				PreferenceHelper.getMyPreference().getEditor().putInt("newImage", 3).commit();//新图鉴
				PreferenceHelper.getMyPreference().getEditor().putInt("pointTable", 2).commit();//轻敲桌面
				PreferenceHelper.getMyPreference().getEditor().putInt("slideLeftRight", 1).commit();//左右滑动
				PreferenceHelper.getMyPreference().getEditor().putInt("slideDown", 2).commit();//向下滑动
				PreferenceHelper.getMyPreference().getEditor().putInt("slideUp", 2).commit();//向上滑动
				PreferenceHelper.getMyPreference().getEditor().putInt("slideLeftRight", 1).commit();//向左右滑动
				arrowBtn.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.open,true)); 
			} else {
				PreferenceHelper.getMyPreference().getEditor().putInt("newImage", 0).commit();//新图鉴
				PreferenceHelper.getMyPreference().getEditor().putInt("pointTable", 0).commit();//轻敲桌面
				PreferenceHelper.getMyPreference().getEditor().putInt("slideLeftRight", 0).commit();//左右滑动
				PreferenceHelper.getMyPreference().getEditor().putInt("slideDown", 0).commit();//向下滑动
				PreferenceHelper.getMyPreference().getEditor().putInt("slideUp", 0).commit();//向上滑动
				PreferenceHelper.getMyPreference().getEditor().putInt("slideLeftRight", 0).commit();//向左右滑动
				arrowBtn.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.close,true));
			}
			break;
		default:
			break;
		}
	}

}
