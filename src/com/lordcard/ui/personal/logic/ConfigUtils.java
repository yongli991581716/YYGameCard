package com.lordcard.ui.personal.logic;

import java.util.Properties;

public class ConfigUtils {

	protected static Properties properties = new Properties();

	static {
		try {
			properties.load(ConfigUtils.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回配置值
	 * @param key
	 * @return
	 * 2012  2012-6-11
	 */
	public static String get(String key){
		return properties.getProperty(key); 
	}

}
