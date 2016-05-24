package com.lordcard.common.util;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

/**震动类
 * @author Administrator
 *
 */
public class Vibrate {
	final static String TAG = "GameEngine";
	public  int num=0;
	Vibrator vibrator;
	Timer timer ;
	long[] pattern = { 1000, 1000 }; // 震动周期 可以自己设置
	long[] pattern1 = { 80, 100,100, 100}; 
	long[] patternjiaofen = { 500, 500,500,500,500,500}; 

	@SuppressWarnings("static-access")
	public Vibrate(Context context) {
		vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
	}

	public void playVibrate(int type) {
		vibrator.vibrate(pattern, type);
		// -1不重复，非-1为从pattern的指定下标开始重复
	}
	public void playVibrate1(int type) {
		vibrator.vibrate(pattern1, type);
		// -1不重复，非-1为从pattern的指定下标开始重复
	}
	public void playVibratejiaofen(int type,final Handler handler) {
		vibrator.vibrate(patternjiaofen, type);
		// -1不重复，非-1为从pattern的指定下标开始重复
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				num++;
				num=num+500;//毫秒
				Message msg = new Message();
				msg.what = 777;
				msg.arg1=num/1000;
				handler.sendMessage(msg);
				Log.i("lin2", ""+num/1000);
			}
		}, 0, 550); //550毫秒是为了防止出现没震动效果但是已经算了震动的次数
	}

	public void Stop() {
		vibrator.cancel();
		if(timer!=null){
			timer.cancel();
			num=0;
		}
	}
}
