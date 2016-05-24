package com.lordcard.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastCards implements Serializable{
	
	private static final long serialVersionUID = -6889580455352907038L;
	@Expose @SerializedName("i") private String		id;			//玩家ID
	@Expose @SerializedName("m") private Integer	masterOrder;	// 地主位置
	@Expose @SerializedName("l") private String		last;			// 底牌
																	
	public LastCards(Integer masterOrder, String last,String id) {
		this.masterOrder = masterOrder;
		this.last = last;
		this.id=id;
	}
	
	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public Integer getMasterOrder() {
		return masterOrder;
	}
	
	public void setMasterOrder(Integer masterOrder) {
		this.masterOrder = masterOrder;
	}
	
	public String getLast() {
		return last;
	}
	
	public void setLast(String last) {
		this.last = last;
	}
	
	@Override
	public String toString() {
		return "LastCards [masterOrder=" + masterOrder + ", last=" + last + "]";
	}
	
}
