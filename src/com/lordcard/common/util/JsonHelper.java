package com.lordcard.common.util;

import java.lang.reflect.Type;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonHelper {

	private static Gson getGson() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			if (!TextUtils.isEmpty(json)) {
				if (json.endsWith(";")) {
					json = json.substring(0, json.length() - 1);
				}
				Gson gson = getGson();
				T t = gson.fromJson(json, clazz);
				clazz = null;
				gson = null;
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T fromJson(String json, TypeToken<T> typeToken) {
		try {
			if (!TextUtils.isEmpty(json)) {
				if (json.endsWith(";")) {
					json = json.substring(0, json.length() - 1);
				}
				Gson gson = getGson();
				Type type = typeToken.getType();
				T t = gson.fromJson(json, type);
				typeToken = null;
				type = null;
				gson = null;
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toJson(Object obj) {
		Gson gson = getGson();
		String result = gson.toJson(obj);
		gson = null;
		return result;
	}
}
