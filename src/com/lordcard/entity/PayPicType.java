/**
 */
package com.lordcard.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayPicType {

	@Expose
	@SerializedName("pic")
	private String picUrl; // 出牌人编号
	@Expose
	@SerializedName("v")
	private int verson; // 剩余的牌数

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getVerson() {
		return verson;
	}

	public void setVerson(int verson) {
		this.verson = verson;
	}

}
