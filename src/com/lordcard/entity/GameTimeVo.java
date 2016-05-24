package com.lordcard.entity;

/**比赛场倒计时(开赛倒计时，比赛结束倒计时)
 * @author Administrator
 */
public class GameTimeVo {
	private Long startTime;  //开始时间
	private Long endTime;   //结束时间
	private String roomName; //房间名称
	private String roomCode; //房间编号 
	
	public final Long getStartTime() {
		return startTime;
	}
	public final void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public final Long getEndTime() {
		return endTime;
	}
	public final void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public final String getRoomName() {
		return roomName;
	}
	public final void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public final String getRoomCode() {
		return roomCode;
	}
	public final void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	
}
