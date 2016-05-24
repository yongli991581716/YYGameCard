package com.lordcard.common.anim;


import com.ylly.playcard.R;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.lordcard.common.schedule.AutoTask;
import com.lordcard.common.schedule.ScheduledTask;
import com.lordcard.common.util.ImageUtil;
import com.lordcard.constant.Database;

public class AnimUtils {

	public static Map<String, AnimBaseView> animViewMap = new HashMap<String, AnimBaseView>();

	public static final String ANIM_BUSY = "anim_busy"; // 忙

	// private static int xOffset = 15;
	// private static int yOffset = 13;

	/**
	 * 播放动画
	 * 
	 * @param viewGroup
	 * @param animView
	 * @param playTime
	 *            动画时长
	 */
	public static void playAnim(final ViewGroup viewGroup, final AnimBaseView animView, long playTime) {
		if (playTime < 0)
			return;

		viewGroup.addView(animView);

		ScheduledTask.addDelayTask(new AutoTask() {
			public void run() {
				Database.currentActivity.runOnUiThread(new Runnable() {
					public void run() {
						animView.stopAnimation();
						viewGroup.removeView(animView);
					}
				});
			}
		}, playTime);
		//		TimerTask animTask = new TimerTask() {
		//			public void run() {
		//				Database.currentActivity.runOnUiThread(new Runnable() {
		//					public void run() {
		//						animView.stopAnimation();
		//						viewGroup.removeView(animView);
		//					}
		//				});
		//			}
		//		};
		//		Timer timer = new Timer();
		//		timer.schedule(animTask, playTime);
	}

	public static void playAnim(final ImageView viewGroup, final AnimationDrawable animView, long playTime) {
		viewGroup.clearAnimation();
		viewGroup.setBackgroundDrawable(animView);

		if (!animView.isRunning()) {
			animView.stop();
		}
		animView.start();

		if (playTime > 0) {
			ScheduledTask.addDelayTask(new AutoTask() {
				public void run() {
					Database.currentActivity.runOnUiThread(new Runnable() {
						public void run() {
							if (animView != null) {
								animView.stop();
								AnimUtils.releaseLoadAnimDrawable(animView);
							}
							viewGroup.setBackgroundDrawable(null);
						}
					});
				}
			}, playTime);
		}

	}
	
	public static void playButtonAnim(final Button viewGroup, final AnimationDrawable animView, long playTime) {
		viewGroup.clearAnimation();
		viewGroup.setBackgroundDrawable(animView);

		if (!animView.isRunning()) {
			animView.stop();
		}
		animView.start();

		if (playTime > 0) {
			ScheduledTask.addDelayTask(new AutoTask() {
				public void run() {
					Database.currentActivity.runOnUiThread(new Runnable() {
						public void run() {
							if (animView != null) {
								animView.stop();
								AnimUtils.releaseLoadAnimDrawable(animView);
							}
							viewGroup.setBackgroundDrawable(null);
						}
					});
				}
			}, playTime);
		}

	}

	public static void playFrame(Animation animation, final ImageView iv) {

		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				Database.currentActivity.runOnUiThread(new Runnable() {
					public void run() {
						iv.setVisibility(View.INVISIBLE);
					}
				});

			}
		});

	}

	public static void playAnim(final ViewGroup viewGroup, final AnimationDrawable animView, long playTime) {
		viewGroup.setBackgroundDrawable(animView);
		if (animView.isRunning()) {
			animView.stop();
		} else {
			animView.start();
		}
		if (playTime > 0) {
			//			TimerTask animTask = new TimerTask() {
			//				public void run() {
			//					Database.currentActivity.runOnUiThread(new Runnable() {
			//						public void run() {
			//							animView.stop();
			//							viewGroup.setBackgroundDrawable(null);
			//						}
			//					});
			//				}
			//			};
			//			Timer timer = new Timer();
			//			timer.schedule(animTask, playTime);

			ScheduledTask.addDelayTask(new AutoTask() {
				public void run() {
					Database.currentActivity.runOnUiThread(new Runnable() {
						public void run() {
							animView.stop();
							viewGroup.setBackgroundDrawable(null);
						}
					});
				}
			}, playTime);
		} else {

		}

	}

	// public static AnimationDrawable getTaskTipView(Context context) {
	// AnimationDrawable animationDrawable = new AnimationDrawable();
	//
	// Drawable drawable = ImageUtil
	// .getResDrawable( R.drawable.money0);
	// animationDrawable.addFrame(drawable, 1000);
	//
	// Drawable drawable1 = ImageUtil.getResDrawable(
	// R.drawable.money1);
	// animationDrawable.addFrame(drawable1, 1000);
	//
	// animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
	// return animationDrawable;
	// }

	/**
	 * 释放加载动画所占的内存
	 */
	public static void releaseLoadAnimDrawable(AnimationDrawable animationDrawable) {
		for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
			ImageUtil.releaseDrawable(animationDrawable.getFrame(i));
		}
	}

	public static Animation getMiniAnimation(int durationMillis) {
		Animation miniAnimation = new ScaleAnimation(2.0f, 1f, 2.0f, 1f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
		miniAnimation.setDuration(durationMillis);
		miniAnimation.setFillAfter(true);
		return miniAnimation;
	}
	
	/**缩放动画入
	 * @param viewgroup
	 */
	public static void startScaleAnimationIn(ViewGroup viewgroup,Context context){
		viewgroup.setVisibility(View.VISIBLE);
		Animation animationjg = AnimationUtils.loadAnimation(context, R.anim.tuoguan_scale_action_in);
		viewgroup.startAnimation(animationjg);
	}
	
	/**缩放动画出
	 * @param viewgroup
	 */
	public static void startScaleAnimationOut(final View viewgroup,Context context){
		Animation animationjg = AnimationUtils.loadAnimation(context, R.anim.tuoguan_scale_action_out);
		viewgroup.startAnimation(animationjg);
		animationjg.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				viewgroup.setVisibility(View.GONE);
			}
		});
	}

	/** 图标的动画(右入动画)
	 * @param viewgroup
	 * @param durationMillis 时间
	 * @param moveLength 唯一长度
	 */
	public static void startAnimationsIn(ViewGroup viewgroup, int durationMillis,int moveLength) {
		viewgroup.setVisibility(0);
		viewgroup.setClickable(true);
		viewgroup.setFocusable(true);
		MarginLayoutParams mlp = (MarginLayoutParams) viewgroup.getLayoutParams();
		Animation animation = new TranslateAnimation(mlp.rightMargin + 300, 0F, 0F, 0F);

		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		animation.setInterpolator(new OvershootInterpolator(2F));// 动画的效果
		// 弹出再回来的效果
		viewgroup.startAnimation(animation);
	}

	/**图标的动画(右出动画)
	 * @param viewgroup 
	 * @param durationMillis 时间
	 * @param moveLength 唯一长度
	 */
	public static void startAnimationsOut(final ViewGroup viewgroup, int durationMillis,int moveLength) {
		MarginLayoutParams mlp = (MarginLayoutParams) viewgroup.getLayoutParams();
		Animation animation = new TranslateAnimation(0F, mlp.rightMargin + moveLength, 0F, 0F);

		animation.setFillAfter(false);
		animation.setDuration(durationMillis);
		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation arg0) {}

			public void onAnimationRepeat(Animation arg0) {}

			public void onAnimationEnd(Animation arg0) {
				viewgroup.setVisibility(View.GONE);
			}
		});
		viewgroup.startAnimation(animation);
	}
	
	/**
	 * @param view 执行动画的VIEW
	 * @param durationMillis 时间
	 * @param moveLength 移动长度
	 * @param view2 显示控件，用于把头像布局顶下去
	 */
	public static void startAnimationsOut1(final View view, int durationMillis,final int moveLength,final View view1,final View view2) {
		MarginLayoutParams mlp = (MarginLayoutParams) view.getLayoutParams();
		Animation animation = new TranslateAnimation(0F, 0F, 0F,mlp.bottomMargin+moveLength);
		animation.setDuration(durationMillis);
		
		 animation.setInterpolator(new OvershootInterpolator());
		 animation.setDuration(durationMillis);
		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation arg0) {}
			public void onAnimationRepeat(Animation arg0) {}
			public void onAnimationEnd(Animation arg0) {
				view2.setVisibility(View.GONE);
				view1.setVisibility(View.VISIBLE);
				view.clearAnimation();
			}
		});
		view.startAnimation(animation);
	}
		
	// 图标的动画(入动画左入)
	public static void startAnimationsInLeft(ViewGroup viewgroup, int durationMillis) {
		viewgroup.setVisibility(0);
		viewgroup.setClickable(true);
		viewgroup.setFocusable(true);
		MarginLayoutParams mlp = (MarginLayoutParams) viewgroup.getLayoutParams();
		Animation animation = new TranslateAnimation(mlp.leftMargin - 500, 0F, 0F, 0F);

		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		//			animation.setInterpolator(new OvershootInterpolator(2F));// 动画的效果
		// 弹出再回来的效果
		viewgroup.startAnimation(animation);
	}

	// 图标的动画(入动画下入)
	public static void startAnimationsInBottom(ViewGroup viewgroup, int durationMillis, int startOffset) {
		viewgroup.setVisibility(0);
		viewgroup.setClickable(true);
		viewgroup.setFocusable(true);
		MarginLayoutParams mlp = (MarginLayoutParams) viewgroup.getLayoutParams();
		Animation animation = new TranslateAnimation(0f, 0F, mlp.bottomMargin + 800, 0F);

		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		animation.setStartOffset(startOffset);
//		animation.setInterpolator(new OvershootInterpolator(2F));// 动画的效果
		// 弹出再回来的效果
		viewgroup.startAnimation(animation);
	}

	// 图标的动画(出动画左出)
	public static void startAnimationsOutLeft(final ViewGroup viewgroup, int durationMillis) {
		MarginLayoutParams mlp = (MarginLayoutParams) viewgroup.getLayoutParams();
		Animation animation = new TranslateAnimation(0F, mlp.leftMargin - 500, 0F, 0F);

		animation.setFillAfter(false);
		animation.setDuration(durationMillis);
		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation arg0) {}

			public void onAnimationRepeat(Animation arg0) {}

			public void onAnimationEnd(Animation arg0) {
				viewgroup.setVisibility(View.GONE);
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					if (viewgroup.getChildAt(i) instanceof ImageView) {
						viewgroup.getChildAt(i).setClickable(true);
					}
				}
			}
		});
		viewgroup.startAnimation(animation);
	}


	/**	/** 图标的动画(出动画下出)
	 * @param viewgroup
	 * @param durationMillis  时间
	 * @param startOffset 
	 * @param moveLength  位移长度
	 * @param hasGone 是否隐藏
	 */
	public static void startAnimationsOutBttom(final ViewGroup viewgroup, int durationMillis, int startOffset,final int moveLength,final boolean hasGone) {
		MarginLayoutParams mlp = (MarginLayoutParams) viewgroup.getLayoutParams();
		Animation animation = new TranslateAnimation(0F, 0f, 0F, mlp.bottomMargin + moveLength);
		Log.i("moveLength", "mlp.bottomMargin:"+mlp.bottomMargin+"    moveLength:"+moveLength);
		final int left=mlp.leftMargin;
		final int right=mlp.rightMargin;
		final int bottom=mlp.bottomMargin;
		final int top=mlp.topMargin;
		animation.setFillAfter(false);
		animation.setDuration(durationMillis);
		animation.setStartOffset(startOffset);
		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation arg0) {}

			public void onAnimationRepeat(Animation arg0) {}

			public void onAnimationEnd(Animation arg0) {
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					if (viewgroup.getChildAt(i) instanceof Button) {
						viewgroup.getChildAt(i).setClickable(true);
					}
				}
				if(hasGone){
					viewgroup.setVisibility(View.GONE);
				}else{
					viewgroup.layout(left, top+ moveLength, right, bottom+ moveLength);
				}
			}
		});
		viewgroup.startAnimation(animation);
	}
	
	
	public static void startAnimationsOut(final View view, int durationMillis,final int moveLength) {
		MarginLayoutParams mlp = (MarginLayoutParams) view.getLayoutParams();
		Animation animation = new TranslateAnimation(0F, 0F, 0F,mlp.bottomMargin+moveLength);
		final int right=view.getRight();
		final int left=view.getLeft();
		final int top=view.getTop();
		final int bottom=view.getBottom();
		animation.setFillAfter(false);
		animation.setDuration(durationMillis);
		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation arg0) {}
			public void onAnimationRepeat(Animation arg0) {}
			public void onAnimationEnd(Animation arg0) {
				view.layout(left, top+ moveLength, right, bottom+ moveLength);
			}
		});
		view.startAnimation(animation);
	}

	//按钮点击效果
	public static void btnShake(final ViewGroup btn, int durationMillis, final ProgressDialog dialog) {
		Animation animation = new ScaleAnimation(1F, 0.8F, 1F, 0.8F, 1, 0.5F, 1, 0.5F);
		animation.setInterpolator(new OvershootInterpolator(2F));
		animation.setDuration(durationMillis);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				dialog.show();
				//					btn.setClickable(true);
			}
		});
		btn.setAnimation(animation);
	}

}
