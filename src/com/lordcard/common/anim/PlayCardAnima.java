package com.lordcard.common.anim;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.lordcard.common.util.ImageUtil;
import com.lordcard.constant.Constant;

public class PlayCardAnima {

	private static AnimationDrawable animationDrawable = null;

	public static AnimationDrawable createAnimDrawAble() {
		animationDrawable = new AnimationDrawable();
		for (int i = 2; i < (Constant.ANIM_FRAME_COUNT + 2); i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.ANIM_IMAGE_START + i, true, true);
			animationDrawable.addFrame(drawable, 200);
		}
		animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}

	public static AnimationDrawable createBomb() {
		animationDrawable = new AnimationDrawable();
		for (int i = 0; i < 5; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.BOMB_IMAGE_START + i, true, true);
			animationDrawable.addFrame(drawable, 150);
		}
		animationDrawable.setOneShot(true); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}

	public static AnimationDrawable createShunz() {
		animationDrawable = new AnimationDrawable();
		for (int i = 1; i <= 10; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.SHUNZ_IMAGE_START + i, true, true);
			animationDrawable.addFrame(drawable, 300);
		}
		animationDrawable.setOneShot(true); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}
 
	public static AnimationDrawable createWangBomb() {
		animationDrawable = new AnimationDrawable();
		for (int i = 0; i < 6; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.WANG_BOMB_IMAGE_START + i, true, true);
			animationDrawable.addFrame(drawable, 50);
		}
		animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}

	public static AnimationDrawable createFeiji() {
		animationDrawable = new AnimationDrawable();
		for (int i = 1; i < 6; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.FEIJI_IMAGE_START + i, true, true);
			animationDrawable.addFrame(drawable, 50);
		}
		animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}
	
	public static AnimationDrawable createPoint() {
		animationDrawable = new AnimationDrawable();
		for (int i = 2; i < 4; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.POINT_ANIM + i, true, true);
			animationDrawable.addFrame(drawable, 200);
		}
		animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}
	
	public static AnimationDrawable createImageNew() {
		animationDrawable = new AnimationDrawable();
		for (int i = 1; i < 3; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.IMAGE_NEW_ANIM + i, true, true);
			animationDrawable.addFrame(drawable, 200);
		}
		animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}
	
	public static AnimationDrawable createImageNews() {
		animationDrawable = new AnimationDrawable();
		for (int i = 0; i < 4; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.IMAGE_NEWS_ANIM + i, true, true);
			animationDrawable.addFrame(drawable, 200);
		}
		animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}
	public static AnimationDrawable createImageLingZhiDou() {
		animationDrawable = new AnimationDrawable();
		for (int i = 1; i < 3; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.IMAGE_LING_ZHI_DOU_ANIM + i, true, true);
			animationDrawable.addFrame(drawable, 200);
		}
		animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}

	public static AnimationDrawable createBaoXiang() {
		animationDrawable = new AnimationDrawable();
		for (int i = 1; i < 4; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.BAO_XIANG + i, true, true);
			animationDrawable.addFrame(drawable, 500);
		}
		animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}

	public static AnimationDrawable createLotMain() {
		animationDrawable = new AnimationDrawable();
		for (int i = 1; i < 3; i++) {
			Drawable drawable = ImageUtil.getResDrawableByName(Constant.LOT_MAIN + i, true, true);
			animationDrawable.addFrame(drawable, 500);
		}
		animationDrawable.setOneShot(false); // 设置是否循环播放 false:循环播放
		return animationDrawable;
	}

	/**
	 * 加载牌到缓存
	 * 
	 * @param context
	 */
	public static void loadPlayCard() {
		String[] cartType = new String[] { Constant.CARD_TAO, Constant.CARD_XIN, Constant.CARD_MEI, Constant.CARD_FANG };

		for (String ct : cartType) {
			for (int j = 1; j < Constant.CART_COUNT + 1; j++) {
				ImageUtil.getResDrawableByName(ct + j, true, true);
			}
		}
	}

}
