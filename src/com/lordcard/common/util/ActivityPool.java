package com.lordcard.common.util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.util.Log;

public class ActivityPool {

	public static Map<String, Activity> activityPool = new HashMap<String, Activity>();

	/**
	 * 加入队列
	 * 
	 * @throws
	 */
	public static void push(Activity activity) {
		String key = activity.getClass().getName();
		remove(activity);
		activityPool.put(key, activity);
		//		Set set=activityPool.entrySet();
		//		Iterator<Map<String, Activity>> it=set.iterator();
		//		while(it.hasNext()){
		//			Map.Entry<String, Activity> entry=(Entry<String, Activity>) it.next();
		//			Log.i("ActivityPool", "activityPool:"+activityPool.size()+"     "+entry.getKey());
		//		}
	}

	/**
	 * 删除
	 * 
	 * @throws
	 */
	public static void remove(Activity activity) {
		String key = activity.getClass().getName();
		if (activityPool.containsKey(key)) {
			activityPool.remove(key);
			Log.d("ActivityPool", "activityPool:" + activityPool.size() + "    删除：" + key);
		}
		activity = null;
	}

	/**
	 * 退出应用
	 */
	public static void exitApp() {
		for (Activity activity : activityPool.values()) {
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
