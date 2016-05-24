package com.lordcard.network.http;

import java.io.Serializable;

import com.lordcard.common.exception.CrashApplication;
import com.lordcard.network.base.SimpleCache;

/**
 * 游戏的数据缓存管理
 * @author Administrator
 *
 */
public class GameCache {
	
	private static SimpleCache cache = SimpleCache.get(CrashApplication.getInstance());		//登录user数据缓存

	/**
	 * 放入缓存
	 * @param key
	 * @param value
	 */
	public static void putStr(String key,String value){
		if(value == null){
			value = "";
		}
		cache.put(key, value);
	}
	
	/**
	 * 获取缓存记录
	 * @param key
	 * @return
	 */
	public static String getStr(String key){
		return cache.getAsString(key);
	}
	
	/**
	 * 放入缓存
	 * @param key
	 * @param value
	 */
	public static void putObj(String key,Serializable obj){
		cache.put(key, obj);
	}
	
	/**
	 * 获取缓存记录
	 * @param key
	 * @return
	 */
	public static Object getObj(String objKey){
		return cache.getAsObject(objKey);
	}
	
	/**
	 * 删除指定的缓存
	 * @param key
	 */
	public static void remove(String key){
		cache.remove(key);
	}
}
