/**
 * JsonResult.java [v 1.0.0]
 * classes : com.game.module.thirdapi.ac.JsonResult
 * auth : yinhongbiao
 * time : 2013 2013-1-25 上午10:53:04
 */
package com.lordcard.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonResult {

	/** 成功 */
	public static final String SUCCESS = "0";
	/** 数据非法 */
	public static final String ILLEGAL = "1";
	/** tokenId 非法 */
	public static final String FAIL_TOKENID = "2";
	@Expose @SerializedName("mc") private String methodCode;
	@Expose @SerializedName("mm") private String methodMessage;

	public String getMethodCode() {
		return methodCode;
	}

	public void setMethodCode(String methodCode) {
		this.methodCode = methodCode;
	}

	public String getMethodMessage() {
		return methodMessage;
	}

	public void setMethodMessage(String methodMessage) {
		this.methodMessage = methodMessage;
	}
}
