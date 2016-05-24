package com.lordcard.ui.view.notification.command;

import com.google.gson.annotations.Expose;

/**
 * 桌面APP信息上传
 */
public class CommandUploadRunAppInfo {
	
	@Expose private String	appName;		// APP名称
	@Expose private String	packageName;	// APP创建时间
	@Expose private String	className; // APP上次使用时间
	@Expose private String shortClassName;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getShortClassName() {
		return shortClassName;
	}
	public void setShortClassName(String shortClassName) {
		this.shortClassName = shortClassName;
	}	
	
}
