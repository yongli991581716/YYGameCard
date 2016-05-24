package com.lordcard.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenLandowners implements Serializable{
	
	private static final long serialVersionUID = 4316793447383438819L;
	@Expose @SerializedName("m") private Integer 	masterOrder;	// 地主位置
	@Expose @SerializedName("r") private Integer	ratio;			// 倍数
																	
	public GenLandowners(Integer masterOrder, Integer ratio) {
		this.masterOrder = masterOrder;
		this.ratio = ratio;
	}
	
	public Integer getMasterOrder() {
		return masterOrder;
	}
	
	public void setMasterOrder(Integer masterOrder) {
		this.masterOrder = masterOrder;
	}
	
	public Integer getRatio() {
		return ratio;
	}
	
	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}
	
	@Override
	public String toString() {
		return "Master [masterOrder=" + masterOrder + ", ratio=" + ratio + "]";
	}
	
}
