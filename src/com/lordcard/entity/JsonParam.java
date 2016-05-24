/**
 * JsonResult.java [v 1.0.0]
 * classes : com.game.module.thirdapi.ac.JsonResult
 * auth : yinhongbiao
 * time : 2013 2013-1-25 上午10:53:04
 */
package com.lordcard.entity;

import com.google.gson.annotations.Expose;

/**
 * 通用Json请求参数 com.game.module.thirdapi.ac.JsonParam
 * 
 * @author yinhb <br/>
 *         create at 2013 2013-1-25 上午10:53:04
 */
public class JsonParam {

	// //////////rjoin 使用//////////////////
	@Expose
	private String loginToken; // 游戏登录Token
	@Expose
	private String homeCode; // 加入的房间号
	@Expose
	private String passwd; // 房间密码

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getHomeCode() {
		return homeCode;
	}

	public void setHomeCode(String homeCode) {
		this.homeCode = homeCode;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
