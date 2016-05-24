package com.lordcard.entity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lordcard.common.util.ImageUtil;
import com.lordcard.common.util.MultiScreenTool;

public class Poker extends RelativeLayout {

	private int style = 0; // 牌的款式
	private int value = 0; // 牌的值
	private int number = 0; // 牌的数组
	private int BitpamResID = 0; // 图片资源的
	public boolean ischeck;
	public boolean isTouch = false;
	private boolean isUsed = false;
	private ImageView pokeImage;
	private RelativeLayout innerLayout;
	public RelativeLayout.LayoutParams params = null;
	public RelativeLayout.LayoutParams imageParams = null;
	public RelativeLayout.LayoutParams innerParams = null;
	

	private static MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();
	public static final int width=mst.adjustXIgnoreDensity(90);
	public static final int height=mst.adjustYIgnoreDensity(125);
	public Poker(Context context) {
		super(context);
//		params = new RelativeLayout.LayoutParams(mst.adjustXIgnoreDensity(90), mst.adjustYIgnoreDensity(125));
		params = new RelativeLayout.LayoutParams(width,height);
		pokeImage = new ImageView(context);
		innerLayout = new RelativeLayout(context);
		imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		innerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		addView(pokeImage, imageParams);
		addView(innerLayout, innerParams);
	}

	public void onDestory() {
		try {
			if (pokeImage != null) {
				pokeImage.setLayoutParams(null);
				Drawable drawable = pokeImage.getDrawable();
				if (drawable != null) {
					BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
					if (!bitmapDrawable.getBitmap().isRecycled()) {//先判断图片是否已释放了
						bitmapDrawable.getBitmap().recycle();
					}
					bitmapDrawable.setCallback(null);
					bitmapDrawable = null;
				}
				drawable.setCallback(null);
				drawable = null;
				//				ImageUtil.releaseDrawable(pokeImage.getDrawable());// 释放扑克图片占用内存
			}
			pokeImage = null;

			if (innerLayout != null) {
				innerLayout.setLayoutParams(null);
				innerLayout.removeAllViews();
				ImageUtil.releaseDrawable(innerLayout.getBackground());
			}
			innerLayout = null;

			params = null;
			imageParams = null;
			innerParams = null;

			setLayoutParams(null);
			removeAllViews();
			removeAllViewsInLayout();
			mst = null;
		} catch (Exception e) {
		}
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getBitpamResID() {
		return BitpamResID;
	}

	public void setBitpamResID(int bitpamResID) {
		BitpamResID = bitpamResID;
	}

	/**
	 * 设置牌回到默认位置
	 */
	public void setDefaultParams() {
		params.topMargin = mst.adjustYIgnoreDensity(20);
		setLayoutParams(params);
		ischeck = !ischeck;
	}
	
	public void setDefaultParams2() {
		params.topMargin = mst.adjustYIgnoreDensity(20);
		setLayoutParams(params);
		ischeck=false;
	}

	/**
	 *  设置牌上升位置
	 */
	public void setRiseParams() {
		params.topMargin = 0;
		setLayoutParams(params);
		ischeck = !ischeck;
	}

	public ImageView getPokeImage() {
		return pokeImage;
	}

	public void setPokeImage(ImageView pokeImage) {
		this.pokeImage = pokeImage;
	}

	public RelativeLayout getInnerLayout() {
		return innerLayout;
	}

	public void setInnerLayout(RelativeLayout innerLayout) {
		this.innerLayout = innerLayout;
	}

	public boolean isTouch() {
		return isTouch;
	}

	public void setTouch(boolean isTouch) {
		this.isTouch = isTouch;
	}

}
