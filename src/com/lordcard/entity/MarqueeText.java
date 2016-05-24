package com.lordcard.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeText extends TextView implements Runnable {
	public int currentScrollX;// 当前滚动的位置 
	private boolean isStop = false;
	private int textWidth;
	private boolean isMeasure = false;
	private Paint paint = null;

	public MarqueeText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub 
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub 
		super.onDraw(canvas);
		if (!isMeasure) {// 文字宽度只需获取一次就可以了 
			getTextWidth();
			isMeasure = true;
		}
	}

	/** 
	* 获取文字宽度 
	*/
	private void getTextWidth() {
		paint = this.getPaint();
		String str = this.getText().toString();
		textWidth = (int) paint.measureText(str);
	}

	@Override
	public void run() {
		currentScrollX += 1;// 滚动速度 
		scrollTo(currentScrollX, 0);
		if (isStop) {
			return;
		}
		if (getScrollX() >= textWidth) {
			currentScrollX = 0;
			scrollTo(currentScrollX, 0);
			//			startFor0();

			// return; 
		}
		postDelayed(this, 5);
	}

	// 开始滚动 
	public void startScroll() {
		isStop = false;
		this.removeCallbacks(this);
		post(this);
	}

	// 停止滚动 
	public void stopScroll() {
		isStop = true;
		this.removeCallbacks(this);
	}

	// 从头开始滚动 
	public void startFor0() {
		currentScrollX = 0;
		startScroll();
	}

	public void onDestory() {
		stopScroll();
		paint = null;
	}
}