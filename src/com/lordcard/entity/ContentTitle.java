/**
 */
package com.lordcard.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: ContentTitle
 * @Description: 指南标题
 * @author shaohu
 * @date 2013-4-11 下午05:00:16
 * 
 */
public class ContentTitle {
	@Expose
	@SerializedName("id")
	private String id;
	@Expose
	@SerializedName("di")
	private Integer display; // 显示方式 1:文本显示 2:图片显示
	@Expose
	@SerializedName("ti")
	private String title; // 标题
	@Expose
	@SerializedName("dp")
	private String description; // 描述

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getDisplay() {
		return display;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(Integer display) {
		this.display = display;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
