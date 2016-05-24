package com.lordcard.entity;

public class GamePlaceVo {
	private int zhidou;//金豆
	private int zhizuang;// 钻石
	private String title;// 赛场标题
	private String content;// 赛场说明

	public GamePlaceVo(int zhidou, int zhizuang, String title, String content) {
		this.zhidou = zhidou;
		this.zhizuang = zhizuang;
		this.title = title;
		this.content = content;
	}

	public final int getZhidou() {
		return zhidou;
	}

	public final void setZhidou(int zhidou) {
		this.zhidou = zhidou;
	}

	public final int getZhizuang() {
		return zhizuang;
	}

	public final void setZhizuang(int zhizuang) {
		this.zhizuang = zhizuang;
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final String getContent() {
		return content;
	}

	public final void setContent(String content) {
		this.content = content;
	}

}
