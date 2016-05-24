package com.lordcard.ui.view;

import com.ylly.playcard.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lordcard.common.anim.AnimUtils;
import com.lordcard.common.util.ImageUtil;
import com.lordcard.common.util.MultiScreenTool;
import com.lordcard.common.util.PreferenceHelper;

/**
 * 主界面手势引导View
 * 提示流程:
 * 1.提示"轻敲桌面，智能选牌"
 * 2.提示"向上滑动，选择出牌"
 * 3."向下滑动，本轮不出"
 * 4."双击屏幕，取消选牌"
 * @author Administrator
 */
public class MainGameGuideView extends RelativeLayout{
	private RelativeLayout arrowRl;//上下滑动手势提示区域
	private ImageView arrowIv,arrowFingerIv,arrowTextIv;//箭头IV,手指Iv,滑动手势提示文字IV
	private RelativeLayout pointRl,doublePointRl;//单击，双击手势提示区域
	private ImageView pointAimIv,fingerIv,pointTextIv;//点Iv,点动画Iv,手指Iv,点击手势提示文字Iv
	private Context context;
	private Animation upAnim,downAnim;//手指向上动画，向下动画
	private boolean arrowIsUp=false; //向上滑动提示
	private boolean arrowIsDown=false;  //向下滑动提示
	
	private LinearLayout arrowLeftRightLl;//左右滑动手势提示区域
	private ImageView arrowLeftIv,arrowRightIv;//左箭头，右箭头
	private Animation leftAnim,rightAnim;//箭头向左动画，向右动画
	private MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();

	public MainGameGuideView(Context context) {
		super(context);
		this.context = context;
	}

	public MainGameGuideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.game_tishi_view, this);
		arrowRl =(RelativeLayout) findViewById(R.id.arrow_up_down_rl);
		arrowIv=(ImageView) findViewById(R.id.arrow_up_down_iv);
		arrowFingerIv=(ImageView) findViewById(R.id.arrow_finger_iv);
		arrowTextIv=(ImageView) findViewById(R.id.arrow_text_iv);
		
		pointRl =(RelativeLayout) findViewById(R.id.point_rl);
		pointAimIv=(ImageView) findViewById(R.id.point_iv);
		fingerIv=(ImageView) findViewById(R.id.point_finger_iv);
		pointTextIv=(ImageView) findViewById(R.id.point_text_iv);
		upAnim = AnimationUtils.loadAnimation(context, R.anim.guide_up);
		downAnim = AnimationUtils.loadAnimation(context, R.anim.guide_down);
		
		doublePointRl =(RelativeLayout) findViewById(R.id.double_point_rl);
		
		arrowLeftRightLl=(LinearLayout) findViewById(R.id.arrow_left_right_ll);
		arrowLeftIv=(ImageView) findViewById(R.id.arrow_left_iv);
		arrowRightIv=(ImageView) findViewById(R.id.arrow_right_iv);
		leftAnim = AnimationUtils.loadAnimation(context, R.anim.guide_left);
		rightAnim = AnimationUtils.loadAnimation(context, R.anim.guide_right);
		mst.adjustView(findViewById(R.id.gtv_rl));
	}
	
	/**
	 * 显示向下手势引导
	 * 提示向上滑动完后再提示向下滑动
	 */
	public void setArrowDownVisible(){
		boolean isOpen=PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true);
		int pointCount=PreferenceHelper.getMyPreference().getSetting().getInt("pointTable", 0);
		int downCount=PreferenceHelper.getMyPreference().getSetting().getInt("slideDown", 0);
		if(isOpen && pointCount>=1 && downCount<1){
			this.setVisibility(View.VISIBLE);
			arrowRl.setVisibility(View.VISIBLE);
			arrowIv.setBackgroundResource(R.drawable.arrow_down);
			arrowTextIv.setBackgroundResource(R.drawable.xiangxiahuadong);
			//手指向下滑动动画
			cancelUpAnim();
			cancelDownAnim();
			arrowFingerIv.startAnimation(downAnim);
			setArrowIsDown(true);
		}
	}
	/**
	 * 隐藏向下手势引导
	 */
	public void setArrowDownGone(boolean isCommit){
		if(isCommit){
			int downCount=PreferenceHelper.getMyPreference().getSetting().getInt("slideDown", 0);
			PreferenceHelper.getMyPreference().getEditor().putInt("slideDown", downCount+1).commit();
		}
		cancelDownAnim();
		arrowRl.setVisibility(View.GONE);
		setArrowIsDown(false);
		this.setVisibility(View.GONE);
	}
	
	
	
	/**
	 * 是否提示向上滑动
	 * @return
	 */
	public final boolean isArrowIsUp() {
		return arrowIsUp;
	}

	public final void setArrowIsUp(boolean arrowIsUp) {
		this.arrowIsUp = arrowIsUp;
	}

	/**是否提示向下滑动
	 * @return
	 */
	public final boolean isArrowIsDown() {
		return arrowIsDown;
	}

	public final void setArrowIsDown(boolean arrowIsDown) {
		this.arrowIsDown = arrowIsDown;
	}

	/**
	 * 取消手指向下的动画
	 */
	public void cancelDownAnim(){
		if(downAnim.hasStarted()){
			downAnim.cancel();
		}
	}
	
	/**
	 * 显示向上手势引导
	 *轻敲提示完后再提示向上滑动
	 */
	public void setArrowUpVisible(){
		boolean isOpen=PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true);
		int upCount=PreferenceHelper.getMyPreference().getSetting().getInt("slideUp", 0);
		int pointCount=PreferenceHelper.getMyPreference().getSetting().getInt("pointTable", 0);
		if(isOpen && pointCount>=1 &&upCount<1){
			this.setVisibility(View.VISIBLE);
			arrowRl.setVisibility(View.VISIBLE);
			arrowIv.setBackgroundResource(R.drawable.arrow_up);
			arrowTextIv.setBackgroundResource(R.drawable.xiangshanghuadong);
			//手指向上滑动动画
			cancelUpAnim();
			cancelDownAnim();
			arrowFingerIv.startAnimation(upAnim);
			setArrowIsUp(true);
		}
	}
	/**
	 * 隐藏向上手势引导
	 */
	public void setArrowUpGone(boolean isCommit){
		if(isCommit){
			int upCount=PreferenceHelper.getMyPreference().getSetting().getInt("slideUp", 0);
			PreferenceHelper.getMyPreference().getEditor().putInt("slideUp", upCount+1).commit();
		}
		cancelUpAnim();
		arrowRl.setVisibility(View.GONE);
		setArrowIsUp(false);
		this.setVisibility(View.GONE);
	}
	
	/**
	 * 取消手指向上的动画
	 */
	public void cancelUpAnim(){
		if(upAnim.hasStarted()){
			upAnim.cancel();
		}
	}
	
	/**
	 * 点击手势提示布局显示
	 */
	public void setPointVisible(){
		boolean isOpen=PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true);
		int count=PreferenceHelper.getMyPreference().getSetting().getInt("pointTable", 0);
		if(isOpen && count<1){
			this.setVisibility(View.VISIBLE);
			pointRl.setVisibility(View.VISIBLE);
			//点的动画
			AnimUtils.playAnim(pointAimIv, ImageUtil.getResAnimaSoft("point"), 0);
		}
	}
	/**
	 * 点击手势提示布局隐藏
	 */
	public void setPointGone(boolean isCommit){
		if(isCommit){
			int count=PreferenceHelper.getMyPreference().getSetting().getInt("pointTable", 0);
			PreferenceHelper.getMyPreference().getEditor().putInt("pointTable", count+1).commit();
		}
		pointRl.setVisibility(View.GONE);
		this.setVisibility(View.GONE);
	}
	
	/**
	 * 双击手势提示布局显示
	 */
	public void setDoublePointVisible(){
		boolean isOpen=PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true);
		int count=PreferenceHelper.getMyPreference().getSetting().getInt("doublePointTable", 0);
		int upCount=PreferenceHelper.getMyPreference().getSetting().getInt("slideUp", 0);
		if(isOpen && count<1 && upCount>=1){
			this.setVisibility(View.VISIBLE);
			doublePointRl.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 双击手势提示布局隐藏
	 */
	public void setDoublePointGone(boolean isCommit){
		if(isCommit){
			int count=PreferenceHelper.getMyPreference().getSetting().getInt("doublePointTable", 0);
			PreferenceHelper.getMyPreference().getEditor().putInt("doublePointTable", count+1).commit();
		}
		doublePointRl.setVisibility(View.GONE);
		this.setVisibility(View.GONE);
	}
	
	/**
	 * 双击手势引导是否显示
	 * @return
	 */
	public boolean isDoublePoint(){
		return View.VISIBLE==doublePointRl.getVisibility();
	}
	
	/**
	 * 点击手势提示布局是否显示
	 * @return
	 */
	public boolean  isPoint(){
		return (View.VISIBLE==pointRl.getVisibility());
	}
	
	/**
	 * 显示左右滑动引导提示
	 */
	public void setArrowLeftRightVisible(){
		boolean isOpen=PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true);
		int lrCount=PreferenceHelper.getMyPreference().getSetting().getInt("slideLeftRight", 0);
		if(isOpen &&lrCount<1){
			this.setVisibility(View.VISIBLE);
			arrowLeftRightLl.setVisibility(View.VISIBLE);
			//左右箭头动画
			cancelLeftRightAnim();
			arrowLeftIv.startAnimation(leftAnim);
			arrowRightIv.startAnimation(rightAnim);
		}
	}
	/**
	 * 隐藏左右滑动手势引导提示
	 * @param isCommit 是否提交显示次数
	 */
	public void setArrowLeftRightGone(boolean isCommit){
		if(isCommit){
			int upCount=PreferenceHelper.getMyPreference().getSetting().getInt("slideLeftRight", 0);
			PreferenceHelper.getMyPreference().getEditor().putInt("slideLeftRight", upCount+1).commit();
		}
		cancelLeftRightAnim();
		arrowLeftRightLl.setVisibility(View.GONE);
		this.setVisibility(View.GONE);
	}
	
	/**
	 * 左右滑动手势引导是否显示
	 * @return
	 */
	public boolean isArrowLeftRightVisible(){
		return View.VISIBLE==arrowLeftRightLl.getVisibility();
	}
	/**
	 * 取消箭头向左右的动画
	 */
	public void cancelLeftRightAnim(){
		if(leftAnim.hasStarted()){
			leftAnim.cancel();
		}
		if(rightAnim.hasStarted()){
			rightAnim.cancel();
		}
	}
}
