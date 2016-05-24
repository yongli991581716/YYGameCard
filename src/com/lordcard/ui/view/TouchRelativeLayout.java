package com.lordcard.ui.view;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.lordcard.common.listener.HasTiShiListenner;
import com.lordcard.entity.Poker;
import com.lordcard.rule.DoudizhuRule;

public class TouchRelativeLayout extends RelativeLayout implements OnGestureListener, OnTouchListener, android.view.GestureDetector.OnGestureListener {

	private int startIndex, endIndex;

	private GestureDetector mGestureDetector;
	private int distance;
	private HasTiShiListenner mHasTiShiListenner;
	private OnTouchListener onTouchListener = null;
	
	public TouchRelativeLayout(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(context, this);

		setLongClickable(true);
		onTouchListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return mGestureDetector.onTouchEvent(event);
			}
		};

		this.setOnTouchListener(onTouchListener);
	}

	public void onDestory() {

		setOnTouchListener(null);
		removeAllViews();
		removeAllViewsInLayout();

		mGestureDetector = null;
		mHasTiShiListenner = null;
		onTouchListener = null;
	}

	public void setListenner(HasTiShiListenner mHasTiShiListenner) {
		this.mHasTiShiListenner = mHasTiShiListenner;
	}

	public TouchRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, this);
		setOnTouchListener(this);
		setLongClickable(true);
	}

	private long startTime = 0;
	private long stopTime = 0;

	@Override
	public boolean onDown(MotionEvent e) {
		if (null == e) {
			return false;
		}
		int[] location = new int[2];
		this.getLocationOnScreen(location);
		int x = (int) e.getRawX();
		xlength = 0;
		int cardDistance = (x - location[0]);
		int index = cardDistance / distance;
		if (cardDistance % distance != 0) {
			index++;
		}
		startIndex = endIndex = index - 1;
		adjustIndex();
		Log.i("OnGestureListener", "onDown");
		startTime = System.currentTimeMillis();
		return false;
	}
 
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.i("OnGestureListener", "onLongPress");
	}

	private float xlength = 0;//滑动式X轴位移长度

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if (null == e1 || null == e2) {
			return false;
		}
		int e1X = (int) e1.getRawX();
		int e2X = (int) e2.getRawX();
		int e1Y = (int) e1.getRawY();
		int e2Y = (int) e2.getRawY();
		xlength = Math.abs(e1X - e2X);
		Log.i("OnGestureListener", "location==Top:" + this.getTop() + "   Left:" + this.getLeft() + "  Right:" + this.getRight() + "distanceX:"
				+ xlength);
		if (e2Y >= this.getTop()) {
			if (e1X - e2X > 0) {
				final int[] location = new int[2];
				getLocationOnScreen(location);
				// 获取在整个屏幕内的绝对坐标，
				// 注意这个值是要从屏幕顶端算起，也就是包括了通知栏的高度。
				endIndex = (e1X - location[0]) / distance;
				if ((e1X - location[0]) % distance != 0) {
					endIndex++;
				}
				startIndex = (e2X - location[0]) / distance;
				if ((e2X - location[0]) % distance != 0) {
					startIndex++;
				}
				startIndex = startIndex - 1;
				endIndex = endIndex - 1;
				adjustIndex();
				for (int i = startIndex; i <= endIndex; i++) {
					((Poker) getChildAt(i)).getInnerLayout().setVisibility(View.VISIBLE);
				}
				// 当向左侧滑动的时候
			} else if (e1X - e2X < 0) {
				final int[] location = new int[2];
				getLocationOnScreen(location);

				startIndex = (e1X - location[0]) / distance;
				if ((e1X - location[0]) % distance != 0) {
					startIndex++;
				}
				endIndex = (e2X - location[0]) / distance;
				if ((e2X - location[0]) % distance != 0) {
					endIndex++;
				}
				startIndex = startIndex - 1;
				endIndex = endIndex - 1;
				adjustIndex();
				for (int i = startIndex; i <= endIndex; i++) {
					((Poker) getChildAt(i)).getInnerLayout().setVisibility(View.VISIBLE);
				}
			}
		}
		//		mHasTiShiListenner.onScrollListenner(e1X, e1Y, e2X, e2Y,startIndex);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.i("OnGestureListener", "onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.i("OnGestureListener", "onSingleTapUp");
		return false;
	}

	public void adjustIndex() {
		if (startIndex < 0) {
			startIndex = 0;
		} else if (startIndex >= getChildCount()) {
			startIndex = getChildCount() - 1;
		}

		if (endIndex >= getChildCount()) {
			endIndex = getChildCount() - 1;
		} else if (endIndex < 0) {
			endIndex = 0;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
	if(null ==event){
			return mGestureDetector.onTouchEvent(event);
	   }
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.i("OnGestureListener", "TouchUp");
			stopTime = System.currentTimeMillis();
			long times = stopTime - startTime;
			if (event.getRawY() < this.getTop() && xlength<150 && (times < 800)) {
				mHasTiShiListenner.onFling();
			} else {
				chekCard();
			}
			//			mHasTiShiListenner.onTouchUpListenner(event.getRawX(),event.getRawY(),startIndex);
		}
		return mGestureDetector.onTouchEvent(event);
	}

	/**
	 * 选牌
	 */
	public void chekCard() {
		for (int i = 0; i <= getChildCount() - 1; i++) {
			((Poker) getChildAt(i)).getInnerLayout().setVisibility(View.GONE);
		}
		adjustIndex();
		boolean hasCheck = false; // 是否已有选择的牌
		for (int i = 0; i <= getChildCount() - 1; i++) {
			((Poker) getChildAt(i)).getInnerLayout().setVisibility(View.GONE);
			if (((Poker) getChildAt(i)).ischeck) {
				hasCheck = true;
			}
		}
		if ((endIndex - startIndex) >= 5) {
			List<Poker> cards = new ArrayList<Poker>();
			TreeSet<Integer> set = new TreeSet<Integer>();
			for (int i = startIndex; i <= endIndex; i++) {
				cards.add(((Poker) getChildAt(i)));
				set.add(((Poker) getChildAt(i)).getValue());
			}
			//看是否存在连对
			if ((endIndex - startIndex) >= 10) {
				List<Integer> liandui = DoudizhuRule.checkLianDui2(cards);
				if (liandui.size() > 0) {
					for (int i = 0; i < cards.size(); i++) {
						boolean has = false;
						for (int j = 0; j < liandui.size(); j++) {
							if (liandui.get(j) == cards.get(i).getValue()) {
								has = true;
								liandui.remove(j);
								break;
							}
						}
						if (has) {
							((Poker) getChildAt(startIndex + i)).ischeck = false;
							((Poker) getChildAt(startIndex + i)).setRiseParams();
						} else {
							((Poker) getChildAt(startIndex + i)).ischeck = true;
							((Poker) getChildAt(startIndex + i)).setDefaultParams();
						}
					}
					return;
				}
			}
			String[] shunPai = DoudizhuRule.checkShunZi(set);
			if (null != shunPai && !hasCheck) {// 存在顺子,当前手上没有牌选择
				for (int i = 0; i < cards.size(); i++) {
					boolean has = false;
					for (int j = 0; j < shunPai.length; j++) {
						if (Integer.valueOf(shunPai[j]) == cards.get(i).getValue()) {
							shunPai[j] = "0";
							has = true;
						}
					}
					if (has) {
						((Poker) getChildAt(startIndex + i)).ischeck = false;
						((Poker) getChildAt(startIndex + i)).setRiseParams();
					} else {
						((Poker) getChildAt(startIndex + i)).ischeck = true;
						((Poker) getChildAt(startIndex + i)).setDefaultParams();
					}
				}
			} else {// 不存在顺子
				makeAChoiceCard();
			}
			cards.clear();
			cards = null;
		} else if ((startIndex == endIndex) && !hasCheck) {//选择一张牌，并且没有牌弹出
			//是否提示对应牌型：true 提示 ；false 不提示
			boolean tishi = mHasTiShiListenner.hasTiShi(((Poker) getChildAt(startIndex)), startIndex);
			if (!tishi) {
				makeAChoiceCard();
			}
		} else {
			makeAChoiceCard();
		}
	}

	/**
	 * 设置牌：弹出/压下
	 */
	public void makeAChoiceCard() {
		for (int i = startIndex; i <= endIndex; i++) {
			if (null != ((Poker) getChildAt(i))) {
				if (((Poker) getChildAt(i)).ischeck) {// 如果牌是弹出的
					((Poker) getChildAt(i)).setDefaultParams();
				} else {
					((Poker) getChildAt(i)).setRiseParams();
				}
			}
		}
	}

	@Override
	public void onGesture(GestureOverlayView overlay, MotionEvent event) {}

	@Override
	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {}

	@Override
	public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {}

	@Override
	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
