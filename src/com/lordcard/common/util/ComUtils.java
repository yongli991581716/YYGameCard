package com.lordcard.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import android.annotation.SuppressLint;

/**    
 * @Title:	CommonUtil.java 
 * @Package:	com.game.common.util 
 * @Description:	公用方法工具类
 * @author:		yinhongbiao 
 * @date:	2011-11-25
 * @version V1.0 
 */
public class ComUtils {

	/**
	 * 随机获取UUID字符串(无中划线)
	 * 
	 * @return UUID字符串
	 */
	public synchronized static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
	}

	/**
	 * 产生单个随机数
	 */
	public static int randomNum(int total) {
		Random random = new Random();
		return random.nextInt(total);
	}

	/**
	 * 产生多个不重复的随机数
	 * @throws
	 */
	public static int[] randomNums(int total) {
		int[] sequence = new int[total];
		int[] output = new int[total];
		for (int i = 0; i < total; i++) {
			sequence[i] = i;
		}
		Random random = new Random();
		int end = total - 1;
		for (int i = 0; i < total; i++) {
			int num = random.nextInt(end + 1);
			output[i] = sequence[num];
			sequence[num] = sequence[end];
			end--;
		}
		return output;
	}

	/**
	 * 产生随机长度的字符串
	 */
	public static String randomStr(int length) {
		length = (length > 32) ? 32 : length;
		String value = getUUID().substring(0, length);
		return value;
	}

	/**
	 * 获取随机编号
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getNo() {
		String strKey = null;
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = calendar.getTime();
			strKey = format.format(date);
			java.util.Random r = new java.util.Random();
			strKey = strKey + Math.abs(r.nextInt());
			strKey = strKey.substring(0, 16);
		} catch (Exception e) {
			strKey = getNo();
		}
		return strKey;
	}
}
