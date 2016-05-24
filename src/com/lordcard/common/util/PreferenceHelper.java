package com.lordcard.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.lordcard.common.exception.CrashApplication;

public class PreferenceHelper {

	private static PreferenceHelper my = null;
	private static SharedPreferences setting = null;
	private static Editor editor = null;

	public PreferenceHelper() {}

	public static void create(Context context2) {
		setting = context2.getSharedPreferences("Setting", Context.MODE_PRIVATE);
		editor = setting.edit();
	}

	public static PreferenceHelper getMyPreference() {
		Context context2 = CrashApplication.getInstance();
		if (my == null) {
			my = new PreferenceHelper();
			create(context2);
		}
		return my;
	}

	public Editor getEditor() {
		return editor;
	}

	public SharedPreferences getSetting() {
		return setting;
	}
}
