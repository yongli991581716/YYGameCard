package com.lordcard.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lordcard.common.util.JsonHelper;

public class GameIQ implements Serializable {

	private static final long serialVersionUID = 1L;
	@Expose @SerializedName("iq") private Integer iq = 0; // 等级等级
	@Expose @SerializedName("tl") private String title; // 称号
	@Expose @SerializedName("ti") private String titleImg; // 称号图标
	@Expose @SerializedName("td") private String titleDesc; // 称号描述
	@Expose @SerializedName("tp") private String titlePerson; // 称号关联人物
	@Expose @SerializedName("li") private String levelImg; // 等级图标

	public final Integer getIq() {
		return iq;
	}

	public final void setIq(Integer iq) {
		this.iq = iq;
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final String getTitleImg() {
		return titleImg;
	}

	public final List<String> getTitleImgList() {
		try {
			return JsonHelper.fromJson(titleImg, new TypeToken<List<String>>() {});
		} catch (Exception e) {
			return null;
		}
	}

	public final void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}

	public final String getTitleDesc() {
		return titleDesc;
	}

	public final void setTitleDesc(String titleDesc) {
		this.titleDesc = titleDesc;
	}

	public final String getTitlePerson() {
		return titlePerson;
	}

	public final void setTitlePerson(String titlePerson) {
		this.titlePerson = titlePerson;
	}

	public String getLevelImg() {
		return levelImg;
	}

	public void setLevelImg(String levelImg) {
		this.levelImg = levelImg;
	}
}
