package com.lordcard.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.lordcard.common.util.MultiScreenTool;
import com.lordcard.entity.Poker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RecordPorkerView extends View {

	private Paint paint = null;
	private PointF centerPointF = null;
	private RectF frontColor = null;
	private Bitmap bitmap = null;
	private float width = -1;
	private float height = -1;
	/**自己打过的牌与牌之间的间隙*/
	private float cardGapDistance = 20;
	private List<List<Poker>> cardList;
	private MultiScreenTool mst = null;
	private int cardWidth = 35;
	private int cardHeight = 45;
	public static String MSG_TIPS = "msg_tips";
	public static String JIPAIQI_USE_TIME = "jipaiqi_use_time";
	/**记牌器可使用次数*/
	public static String JIPAIQI_USE_COUNT="jipaiqi_use_count";
	public RecordPorkerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeDrawElement();

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		centerPointF.x = getWidth() / 2.0f;
		centerPointF.y = getHeight() / 2.0f;
		if (null == cardList || cardList.size() == 0)
			return;
		if (null == bitmap) {
			bitmap = scaleBitmap(BitmapFactory.decodeResource(getContext().getResources(), cardList.get(0).get(0).getBitpamResID()), new Point(cardWidth, cardHeight));
			width = bitmap.getWidth();
			height = bitmap.getHeight();

		}
		int pokersSize  = 0;
		for(List<Poker> list:cardList){
			pokersSize += list.size();
		}
		float widthX = (pokersSize - 1) * cardGapDistance + width;
		float tempX = -1;
		Bitmap templeBitmap = null;
		int count = 0;
		for (int i = cardList.size() - 1; i >= 0 ; i--) {
			
			for(Poker poker:cardList.get(i)){
				tempX = centerPointF.x - widthX / 2 + count * cardGapDistance;
				templeBitmap = scaleBitmap(BitmapFactory.decodeResource(getContext().getResources(), poker.getBitpamResID()), new Point(cardWidth, cardHeight));
				canvas.drawBitmap(templeBitmap, tempX, centerPointF.y - height / 2.0f, paint);
				count++;
			}
			
		}
		if (null == frontColor)
			frontColor = new RectF(centerPointF.x - widthX / 2f, centerPointF.y - height / 2.f, centerPointF.x + widthX
					/ 2f + 3, centerPointF.y + height / 2.f);

		frontColor.set(centerPointF.x - widthX / 2f, centerPointF.y - height / 2.f, centerPointF.x + widthX / 2f + 3,
				centerPointF.y + height / 2.f);
		paint.setARGB(60, 0, 0, 0);
		canvas.drawRect(frontColor, paint);
		paint.setARGB(255, 0, 0, 0);
	}

	private void initializeDrawElement() {
		paint = new Paint();
		paint.setAntiAlias(true);
		centerPointF = new PointF();
		cardList = new ArrayList<List<Poker>>();
		mst = MultiScreenTool.singleTonHolizontal();
		cardWidth = dip2px(getContext(),cardWidth,mst);
		cardHeight =  dip2px(getContext(),cardHeight,mst);
		cardGapDistance=mst.adjustXIgnoreDensity(20);
	}

	public List<List<Poker>> getCardList() {
		return cardList;
	}
	
	public void addCardList(List<Poker> list) {
		this.cardList.add(list);
		invalidate();
	}
	public void clearCardList(){
		cardList.clear();
		invalidate();
	}
	public static int dip2px(Context context, float dpValue,MultiScreenTool mst) {
		final float scale = context.getResources().getDisplayMetrics().density ;
		
		return (scale >1) ? (int) (dpValue * scale + 0.5f) : mst.adjustX((int)dpValue);
	}
	public static Bitmap scaleBitmap(Bitmap bitmap, Point dimenSize) {
		// 获取这个图片的宽和高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// 定义预转换成的图片的宽度和高度
		int newWidth = dimenSize.x;
		int newHeight = dimenSize.y;

		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return resizedBitmap;
	}


}
