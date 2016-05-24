package com.lordcard.common.util;

import java.util.HashMap;

import com.lordcard.constant.CacheKey;
import com.lordcard.network.http.GameCache;


public class SettingUtils {
	
	//游戏审核中
	public static final String GAME_CHECK = "game_check";
	
	@SuppressWarnings("unchecked")
	private static HashMap<String,String> settingMap = (HashMap<String, String>)GameCache.getObj(CacheKey.ALL_SETTING_KEY);;
	
	public static boolean getBoolean(String key){
		try {
			String value = settingMap.get(key);
			return Boolean.parseBoolean(value);
		} catch (Exception e) {
		}
		return false;
	}
}
