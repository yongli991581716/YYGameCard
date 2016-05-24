package com.lordcard.common.util;

import java.util.Properties;

import com.lordcard.common.exception.CrashApplication;

public class ConfigUtil {
	
	private static final String fileName = "config.cfg";
	
	protected static Properties properties = new Properties();

	static {
		try {
			properties.load(CrashApplication.getInstance().getResources().getAssets().open(fileName));
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
	public static String getCfg(String key){
		return properties.getProperty(key); 
	}
	
//	private static final Map<String,String> configMap = new HashMap<String,String>();
//	
//	static{
//		loadConfig();
//	}
//	
//	public static void loadConfig(){
//		try {
//			 InputStreamReader inputReader = new InputStreamReader(CrashApplication.getInstance().getResources().getAssets().open(fileName)); 
//             BufferedReader bufReader = new BufferedReader(inputReader);
//             String readLink= null;
//             while((readLink = bufReader.readLine()) != null){
//            	 readLink = new String(readLink.getBytes("ISO-8859-1"), Constant.CHAR);
//            	 if(TextUtils.isEmpty(readLink) || readLink.startsWith("#")){		//空的或者注释的去掉
//            		 continue;
//            	 }
//            	 readLink = readLink.replaceAll(" ","");
//            	 
//            	 String [] cfg = readLink.split("=");
//            	 if(cfg == null || cfg.length != 2) continue;	//无效的配置
//            	 
//            	 configMap.put(cfg[0],cfg[1]);
//             }
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * 获取配置
//	 * @Title: getCfg  
//	 * @param @param key
//	 * @param @return
//	 * @return String
//	 * @throws
//	 */
//	public static String getCfg(String key){
//		return configMap.get(key);
//	}
}
