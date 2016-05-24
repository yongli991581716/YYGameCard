package com.lordcard.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 游戏大厅 com.frame.game.entity.GameHall
 * 
 * @author yinhb <br/>
 *         create at 2013 2013-1-17 下午03:19:00
 */
public class GameHall {

	@Expose @SerializedName("n") private String name; // 大厅名称
	@Expose @SerializedName("r") private String res; // 相关资源(json格式存储)
	@Expose @SerializedName("u") private String resUrl; // 相关资源下载目录
	@Expose @SerializedName("v") private int version; // 大厅版本号 版本号 有变化代表大厅数据有更改，前台需要更新

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
