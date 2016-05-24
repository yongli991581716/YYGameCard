package com.lordcard.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.lordcard.common.util.MultiScreenTool;

public abstract class BaseDialog extends Dialog implements  OnClickListener {
	private static final  String TAG = BaseDialog.class.getSimpleName();
	protected MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();
	protected View mainLayout = null;
	protected Context mContext = null;
	
	protected Button leftButton = null;
	protected Button rightButton = null;
	protected Button cancelButton = null;
	
	public BaseDialog(Context context,int style) {
		super(context, style);
		mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		initContentView(mContext);
		adjustViewRectPerformance();
		
	}
	
	/**Set the dialog gravity of the window
	 * @param gravity 
	 */
	public void setGravity(int gravity){
		
		Window window = this.getWindow();
		window.setGravity(gravity);
	}
	
	
	/**Set an animation for dialog when dialog show() or dismiss()
	 * @param style the style of window @android:windowEnterAnimation and @android:windowExitAnimation attribute
	 */
	public void setDialogAnimation(int style){
		if(style ==-1)
			return;
		Window window = this.getWindow();
		window.setWindowAnimations(style);
	}
	
	/**To Initialize Buttons On the dialog
	 * @param leftBtn  left button ,if don't need pass the value @null
	 * @param rightBtn right button ,if don't need pass the value @null
	 * @param cancelBtn cancel button ,if don't need pass the value @null
	 */
	protected void initButtons(Button leftBtn,Button rightBtn,Button cancelBtn){
		if(null != leftBtn){
			leftButton = leftBtn;
			leftButton.setOnClickListener(this);
		}
		
		if(null != rightBtn){
			rightButton = rightBtn;
			rightButton.setOnClickListener(this);
		}
			
		if(null != cancelBtn){
			cancelButton = cancelBtn;
			cancelButton.setOnClickListener(this);
		}
		
	}
	
	/**
	 * Use MultiScreenTool tools to adjust view to adjust all screen resolutions
	 */
	private void adjustViewRectPerformance(){
		if(null != mainLayout)
			mst.adjustView(mainLayout);
		else 
			Log.w(TAG, "You may init mainLayout to make better performance for  user!");
	}
	
	/**Get String with params
	 * @param id the id of string 
	 * @param params params params to format string
	 * @return the result after format with params
	 */
	public String getStringWithParams(int id,Object ...params){
		String mString = mContext.getString(id);
		return getStringWithParams(mString,params);
	}
	
	/**Get String with params
	 * @param string String to format
	 * @param params params to format string
	 * @return the result after format with params
	 */
	public String getStringWithParams(String string,Object ...params){
		String mString = String.format(string, params);
		return mString;
	}
	@Override
	public void dismiss() {
		super.dismiss();
		if(null != mainLayout)
			mst.unRegisterView(mainLayout);
	}
	/**Initialize dialog content view,Implementation the method to create your dialog view when need
	 * @param context
	 */
	public abstract void initContentView(Context context);
}
