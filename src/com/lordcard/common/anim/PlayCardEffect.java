package com.lordcard.common.anim;


import com.ylly.playcard.R;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.lordcard.common.exception.CrashApplication;
import com.lordcard.common.util.PreferenceHelper;
import com.lordcard.common.util.Vibrate;

public class PlayCardEffect {

	/***************************************************************
	 * 炸弹特效
	 * 
	 * @param context
	 *            上下文
	 * @param type
	 *            牌的类型
	 * @param view
	 *            需要播放动画的视图
	 ******************************************************************/
	public static void bomEffect(int type, View view) {
		Context context = CrashApplication.getInstance();
		// 加载动画
		Animation huiojian = AnimationUtils.loadAnimation(context, R.anim.shake);

		// 实例化震动
		Vibrate vibrate = new Vibrate(context);

		// 如果是王炸或者是炸弹的话
		if (type == 13 || type == 6) {
			if (PreferenceHelper.getMyPreference().getSetting().getBoolean("zhendong", true)) {
				vibrate.playVibrate(-1);
			}
			view.startAnimation(huiojian);
		}
	}

}
