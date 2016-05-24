package com.lordcard.entity;

/**
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: ContentDetail
 * @Description: 内容
 * @author shaohu
 * @date 2013-4-11 下午05:01:52
 * 
 */
public class ContentDetail {

	@Expose @SerializedName("id") private String id;
	@Expose @SerializedName("ti") private String title;
	@Expose @SerializedName("dp") private String description; // 描述

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

	/**
	 * @return the display
	 */
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
