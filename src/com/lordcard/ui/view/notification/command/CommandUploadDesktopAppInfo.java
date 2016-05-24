package com.lordcard.ui.view.notification.command;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;

/**
 * 桌面APP信息上传
 */
public class CommandUploadDesktopAppInfo {
	@Expose private String appName;//应用名
	@Expose private String packageName;//包名
	@Expose private String versionName;//版本
	@Expose private int versionCode;//升级号	
	@Expose private Drawable appIcon;
	
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
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
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	
}



