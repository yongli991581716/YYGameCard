package com.lordcard.common.anim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.lordcard.common.util.ImageUtil;

@SuppressLint("ViewConstructor")
public class AnimBaseView extends View {

	private int duration;
	protected AnimationDrawable animationDrawable;

	/**
	 * @param context
	 * @param frameBitmap
	 *            动画的帧集合
	 * @param duration
	 *            每帧播放间隔
	 * @param oneshot
	 *            是单次播放
	 */
	public AnimBaseView(Context context, int duration, boolean oneshot) {
		super(context);
		animationDrawable = new AnimationDrawable();
		animationDrawable.setOneShot(oneshot);
		this.duration = duration;
	}

	/**
	 * 加载动画的帧
	 * 
	 * @param frameBitmap
	 */
	public void loadAnimFrame(Bitmap[] frameBitmap) {
		for (int frame = 0; frame < frameBitmap.length; frame++) {
			animationDrawable.addFrame(new BitmapDrawable(frameBitmap[frame]), duration);
		}
		setBackgroundDrawable(animationDrawable);
	}

	// public void loadAnimFrame(List<WeakReference<Bitmap>> bitmaps) {
	// for (int frame = 0; frame < bitmaps.size(); frame++) {
	// animationDrawable.addFrame(new BitmapDrawable(bitmaps.get(frame)
	// .get()), duration);
	// }
	// setBackgroundDrawable(animationDrawable);
	// }

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		animationDrawable.start();
	}

	protected void onAnimationEnd() {
		super.onAnimationEnd();
	}

	public void stopAnimation() {
		animationDrawable.stop();
		for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
			ImageUtil.releaseDrawable(animationDrawable.getFrame(i));
		}
	}

}
