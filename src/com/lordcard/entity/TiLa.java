package com.lordcard.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 踢拉
 */
public class TiLa implements Serializable {

	private static final long serialVersionUID = -6421585395387451465L;

	@Expose
	@SerializedName("i")
	private String id; // 玩家ID
	@Expose
	@SerializedName("r")
	private int ratio = 1; // 加倍的倍数 (1:不加倍,2:加2倍,4:加4倍)
	@Expose
	@SerializedName("no")
	private Integer nextOrder; // 下一个踢or拉玩家的位置
	@Expose
	@SerializedName("nc")
	private Boolean nextCan; // 下一个可否踢or拉
	@Expose
	@SerializedName("o")
	private Integer order; // 加倍玩家的位置

	public TiLa() {
	}

	public TiLa(String id, int ratio, Integer nextOrder, Boolean nextCan,
			Integer order) {
		this.id = id;
		this.ratio = ratio;
		this.nextOrder = nextOrder;
		this.nextCan = nextCan;
		this.order = order;
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

	public Integer getNextOrder() {
		return nextOrder;
	}

	public void setNextOrder(Integer nextOrder) {
		this.nextOrder = nextOrder;
	}

	public Boolean getNextCan() {
		return nextCan;
	}

	public void setNextCan(Boolean nextCan) {
		this.nextCan = nextCan;
	}

	public final Integer getOrder() {
		return order;
	}

	public final void setOrder(Integer order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "TiLa [id=" + id + ", ratio=" + ratio + ", nextOrder="
				+ nextOrder + ", nextCan=" + nextCan + "]";
	}

}
