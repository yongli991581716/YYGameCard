package com.lordcard.ui.view;

import com.ylly.playcard.R;

import java.util.ArrayList;
import java.util.List;

import com.lordcard.common.util.MultiScreenTool;
import com.lordcard.entity.Poker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation")
public class JiPaiQiTurnPlateView extends View {

	/** 记牌器放置位置 **/
	public enum Location {
		/** 右上角 **/
		Top_Right,
		/** 左上角 **/
		Top_Left,
		/** 右下角 **/
		// Bottom_Right,
		/** 左下角 **/
		// Bottom_Left,
	}

	/** 圆心位置 **/
	private PointF centerPosition = null;
	/** 记牌器放置位置 **/
	private Location location = Location.Top_Right;
	/** 已出牌列表 **/
	private List<List<Poker>> cardList = null;
	
	private int mCardCount = 0;
	/** 记牌器头像 **/
	private Bitmap avatar = null;

	/** 画笔 **/
	private Paint paint;
	private Paint mPaint;
	/** 当前扑克位置 **/
	private PointF currentCardPosition = null;
	private float centerGap = 20;
	/** 设置内圆半径 **/
	int innerCircle = 30;
	/** 设置圆环宽度 **/
	int ringWidth = 50;
	/** 转盘半径 **/
	private float radius = 55;
	/** 旋转角度 **/
	private Double angle = 90d;
	/** 扑克间隔角度 **/
	private float cardGapDegree = 15;
	/** 叠加角度 **/
	private Double plusDegree = 0d;
	/** 手指按下位置 **/
	private PointF touchPosition = null;
	/** 转盘转动速度 **/
	private float rate = 1.7f;
	private int cardWidth = 35;
	private int cardHeight = 45;
	private int avatarSize = 50;

	private Context context;
	MultiScreenTool mst = null;

	private void initializeDrawElement() {
		paint = new Paint();
		paint.setAntiAlias(true);
		mPaint = new Paint();
		mPaint.setAntiAlias(true); // 消除锯齿
		mPaint.setStyle(Paint.Style.STROKE); // 绘制空心圆
		mst =  MultiScreenTool.singleTonHolizontal();
		touchPosition = new PointF();
		currentCardPosition = new PointF();
		centerPosition = new PointF();
		centerGap = RecordPorkerView.dip2px(context, (int)centerGap,mst);
		innerCircle =  RecordPorkerView.dip2px(context, (int)innerCircle,mst);
		ringWidth =  RecordPorkerView.dip2px(context, (int)ringWidth,mst);
		radius =  RecordPorkerView.dip2px(context, (int)radius,mst);
		cardHeight =  RecordPorkerView.dip2px(context, (int)cardHeight,mst);
		cardWidth =  RecordPorkerView.dip2px(context, (int)cardWidth,mst);
		avatarSize =  RecordPorkerView.dip2px(context, (int)avatarSize,mst);
		
		avatar = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.nongmin);
		location = Location.Top_Right;
		cardList = new ArrayList<List<Poker>>();
		

	}

	public JiPaiQiTurnPlateView(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context;
		initializeDrawElement();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		canvas.drawColor(Color.GREEN);
		centerPosition = (location.equals(Location.Top_Left) ? (new PointF(centerGap, centerGap)) : (new PointF(
				getWidth() - centerGap, centerGap)));
		if (null != cardList && cardList.size() != 0) {
			/** 旋转角度 **/
			angle += plusDegree;
			Double tempRadians = 0d;
			Double tempAngle = 0d;
			Bitmap templeBitmap = null;
			int cardCount = 0;
			int count =0 ;
			for (List<Poker> list : cardList) {
				cardCount += list.size();
			}
			mCardCount = cardCount;
			tempAngle = (location.equals(Location.Top_Left)) ? angle : (angle + cardCount * cardGapDegree);
			for (int i = cardList.size() - 1; i >= 0; i--) {
				int j = (location.equals(Location.Top_Left))? i:cardList.size()-1-i;
				for (Poker mPoker : cardList.get(j)) {
					/** 如果是第一个则不旋转 **/
					if (location.equals(Location.Top_Left)) {
						if (count !=0)
							tempAngle -= cardGapDegree;
					} else {
						if (cardCount != count-1)
							tempAngle -= cardGapDegree;
					}
					count ++;
					templeBitmap = RecordPorkerView.scaleBitmap(
							BitmapFactory.decodeResource(getContext().getResources(), mPoker.getBitpamResID()),
							new Point(cardWidth, cardHeight));
					/** 将角度转换为弧度 **/
					tempRadians = Math.PI / 180 * tempAngle;
					currentCardPosition.x = (float) (Math.cos(tempRadians) * radius + centerPosition.x);
					currentCardPosition.y = (float) (Math.sin(tempRadians) * radius + centerPosition.y);

					/** 绘制扑克 **/
					canvas.save();
					float degree = Float.valueOf(tempAngle.toString()) - 90;
					/** 根据位置转换旋转角度 **/
					canvas.rotate(degree, currentCardPosition.x, currentCardPosition.y);

					canvas.drawBitmap(templeBitmap, currentCardPosition.x - templeBitmap.getWidth() / 2f,
							currentCardPosition.y - templeBitmap.getHeight() / 2, paint);
					canvas.restore();
				}
			}
			if (null != templeBitmap && !templeBitmap.isRecycled())
				templeBitmap.recycle();
		}

		canvas.save();

		if (null != avatar) {
			if (avatar.getHeight() != avatarSize)
				avatar = RecordPorkerView.scaleBitmap(avatar, new Point(avatarSize, avatarSize));
			canvas.drawBitmap(avatar, centerPosition.x - avatar.getWidth() / 2f, centerPosition.y - avatar.getHeight()
					/ 2f, paint);
		}

		// 绘制圆环
		mPaint.setARGB(60, 16, 16, 16);
		mPaint.setStrokeWidth(ringWidth);
		canvas.drawCircle(centerPosition.x, centerPosition.y, innerCircle + 1 + ringWidth / 2, mPaint);
		mPaint.setARGB(255, 0, 0, 0);
		plusDegree = 0d;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		onTouch(event);
		return true;
	}

	public void onTouch(MotionEvent event) {
		if (getVisibility() != View.VISIBLE)
			return;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchPosition.x = event.getRawX();
				touchPosition.y = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				float gap = 0f;
				if (Math.abs(event.getRawX() - touchPosition.x) > Math.abs(event.getRawY() - touchPosition.y)) {
					gap = touchPosition.x - event.getRawX();
				} else {
					if(location.equals(Location.Top_Right))
						gap = touchPosition.y - event.getRawY();
					else
						gap = event.getRawY() - touchPosition.y;
				}
				touchPosition.x = event.getRawX();
				touchPosition.y = event.getRawY();
				plusDegree = (double) gap / rate;
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				if (location.equals(Location.Top_Left)) {
					/** 判断上下分界线 **/
					if (angle + plusDegree < 90 || (mCardCount - 1) * cardGapDegree < 90) {
						plusDegree =90 -angle;// 使用angle==90;
					}else if ((angle + plusDegree > (mCardCount - 1) * cardGapDegree) ) {
						plusDegree = (double) ((mCardCount - 1) * cardGapDegree - angle);
					}
				} else if (location.equals(Location.Top_Right)) {
					/** 判断上下分界线 **/
					if (angle + plusDegree > 90 || (mCardCount - 1) * cardGapDegree < 90) {
						plusDegree = 90 - angle;// 使用angle==90;
					}else if ((angle + plusDegree + (mCardCount - 1) * cardGapDegree) <180 ) {
						plusDegree = (double) (180 - (mCardCount - 1) * cardGapDegree - angle);
					}
				}
				invalidate();
				break;
			default:
				break;
		}
	}

	public PointF getCenterPosition() {
		return centerPosition;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void addCardList(List<Poker> carList) {
		this.cardList.add(carList);
		angle = 90d;
		invalidate();
	}

	public List<List<Poker>> getCardList() {
		return cardList;
	}

	public Bitmap getAvatar() {
		return avatar;
	}

	public void setAvatar(Bitmap avatar) {
		this.avatar = avatar;
		invalidate();
	}

	public void clearCardList() {
		cardList.clear();
		angle = 90d;
		invalidate();
	}

	
}
