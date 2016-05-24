package com.lordcard.common.exception;




public class LogUtil {
	
//	static{
//		configure();
//	} 
//	private static final Logger errLog = Logger.getLogger(LogUtil.class);
//	
//	public static void configure() {
//		final LogConfigurator logConfigurator = new LogConfigurator();
//		logConfigurator.setFileName(Environment.getExternalStorageDirectory()+File.separator+"error.log");
//		logConfigurator.setRootLevel(Level.DEBUG);
//		logConfigurator.setLevel("org.apache", Level.ERROR);
//		logConfigurator.configure();
//	}
//	
	public static void err(String msg,Throwable t){
//		errLog.debug(msg, t);
	}
	
}
