package com.lordcard.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinCmdDetail {

	@Expose @SerializedName("ac") private String account; //账号
	@Expose @SerializedName("up") private String userPwd; //密码
	@Expose @SerializedName("vi") private String version; //版本
	@Expose @SerializedName("nt") private String network; //网络
	@Expose @SerializedName("ce") private String channel; //渠道号
	@Expose @SerializedName("mi") private String macIp; //用户唯一标识
	@Expose @SerializedName("gt") private String gameType; //游戏类型
	@Expose @SerializedName("pf") private String pinfo; //手机硬件信息
	@Expose @SerializedName("op") private int oper; //操作[oper:"1" 注册流程,oper:"2" 登录流程]

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMacIp() {
		return macIp;
	}

	public void setMacIp(String macIp) {
		this.macIp = macIp;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getPinfo() {
		return pinfo;
	}

	public void setPinfo(String pinfo) {
		this.pinfo = pinfo;
	}

	public int getOper() {
		return oper;
	}

	public void setOper(int oper) {
		this.oper = oper;
	}
}
