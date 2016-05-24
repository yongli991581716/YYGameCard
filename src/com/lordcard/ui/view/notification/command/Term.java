package com.lordcard.ui.view.notification.command;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 指令执行条件
 */
public class Term {
	
	@Expose private String	runApp;		// 运行某些程序时
	@Expose private String	notRunApp;		// 未运行某些程序时
	@Expose private String	hasApp;		// 系统中有某些程序时
	@Expose private String  notHasApp;		// 系统中没有某些程序时
	@Expose private List< String>	networkTypes;	// 符合相应网络类型时(1=wifi,2=2g,3=3g)
	public String getRunApp() {
		return runApp;
	}
	public void setRunApp(String runApp) {
		this.runApp = runApp;
	}
	public String getNotRunApp() {
		return notRunApp;
	}
	public void setNotRunApp(String notRunApp) {
		this.notRunApp = notRunApp;
	}
	public String getHasApp() {
		return hasApp;
	}
	public void setHasApp(String hasApp) {
		this.hasApp = hasApp;
	}
	public String getNotHasApp() {
		return notHasApp;
	}
	public void setNotHasApp(String notHasApp) {
		this.notHasApp = notHasApp;
	}
	public List<String> getNetworkTypes() {
		return networkTypes;
	}
	public void setNetworkTypes(List<String> networkTypes) {
		this.networkTypes = networkTypes;
	}
	
}
