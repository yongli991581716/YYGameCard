package com.lordcard.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChannelCfg implements Serializable{
	
	private static final long serialVersionUID = -8081966695452233567L;
	
	@Expose @SerializedName("ukey") private String uappkey;		//统计用的appkey  现在用友盟
	@Expose @SerializedName("uchl") private String uchannel;	//自定义的渠道名称
	@Expose @SerializedName("sfgn") private String serCfgName;	//服务器配置的目录名称
	@Expose @SerializedName("sdir") private String serDir;		//更新包目录地址
	@Expose @SerializedName("btid") private String batchId;		//激活批次号
	
	public String getUappkey() {
		return uappkey;
	}
	
	public void setUappkey(String uappkey) {
		this.uappkey = uappkey;
	}
	
	public String getUchannel() {
		return uchannel;
	}
	
	public void setUchannel(String uchannel) {
		this.uchannel = uchannel;
	}
	
	public String getSerDir() {
		return serDir;
	}

	public void setSerDir(String serDir) {
		this.serDir = serDir;
	}

	public String getSerCfgName() {
		return serCfgName;
	}

	public void setSerCfgName(String serCfgName) {
		this.serCfgName = serCfgName;
	}

	public String getBatchId() {
		return batchId;
	}
	
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

}
