package com.lordcard.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinDetail implements Serializable{
	
	private static final long serialVersionUID = -2589537537887741946L;
	@Expose @SerializedName("rc") private String	roomCode;				// 加入房间编号
	@Expose @SerializedName("tl") private boolean	tiLaSwitch	= false;	// 前端是否有踢拉功能
																			
	public String getRoomCode() {
		return roomCode;
	}
	
	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	
	public boolean getTiLaSwitch() {
		return tiLaSwitch;
	}
	
	public void setTiLaSwitch(boolean tiLaSwitch) {
		this.tiLaSwitch = tiLaSwitch;
	}
	
}
