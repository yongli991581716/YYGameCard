package com.lordcard.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class NoticesVo implements Serializable {

	private static final long serialVersionUID = -435134959596449351L;
	
	@Expose private String title;
	@Expose private String content;
	@Expose private String ctime;
	@Expose private String type;//类型   0:公告        1:推送通知   2:游戏等待页面提示消息  3:专场推送通知,4:房间通知消息,5:充值推送消息,6:命令推送

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public NoticesVo() {}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
}
