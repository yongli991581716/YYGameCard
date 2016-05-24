package com.lordcard.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Grab implements Serializable {

	private static final long serialVersionUID = 5260974564438078143L;
	/**
	 * id : 地主的ID ratio:叫的分数 lastcards: 地主底牌
	 */
	@Expose
	@SerializedName("i")
	private String id; // 玩家ID
	@Expose
	@SerializedName("r")
	private int ratio; // 叫的倍数
	@Expose
	@SerializedName("l")
	private String lastcards; // 最后的底牌
	@Expose
	@SerializedName("no")
	private Integer nextOrder; // 叫分人的位置
	@Expose
	@SerializedName("m")
	private Integer masterOrder; // 地主位置

	public Integer getMasterOrder() {
		return masterOrder;
	}

	public void setMasterOrder(Integer masterOrder) {
		this.masterOrder = masterOrder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public String getLastcards() {
		return lastcards;
	}

	public void setLastcards(String lastcards) {
		this.lastcards = lastcards;
	}

	public Integer getNextOrder() {
		return nextOrder;
	}

	public void setNextOrder(Integer nextOrder) {
		this.nextOrder = nextOrder;
	}

}
