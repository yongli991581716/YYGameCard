/**
 * GameHallView.java [v 1.0.0]
 * classes : com.frame.game.vo.GameHallView
 * auth : yinhongbiao
 * time : 2013 2013-1-17 下午04:40:10
 */
package com.lordcard.entity;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 房间大厅 com.frame.game.vo.GameHallView
 * 
 * @author yinhb <br/>
 *         create at 2013 2013-1-17 下午04:40:10
 */
public class GameHallView {

	@Expose @SerializedName("l") private String loginToken; // 游戏内部登录Token
	@Expose @SerializedName("g") private GameHall gameHall; // 大厅基本数据
	@Expose @SerializedName("gr") private List<Room> gameRoomList; // 大厅房间数据
	@Expose @SerializedName("fr") private List<Room> fastRoomList;// 普通赛制房间数据
	@Expose @SerializedName("sr") private List<Room> sortRoomList;// 符合赛制房间数据

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public List<Room> getGameRoomList() {
		return gameRoomList;
	}

	public void setGameRoomList(List<Room> gameRoomList) {
		this.gameRoomList = gameRoomList;
	}

	public GameHall getGameHall() {
		return gameHall;
	}

	public void setGameHall(GameHall gameHall) {
		this.gameHall = gameHall;
	}

	public List<Room> getFastRoomList() {
		return fastRoomList;
	}

	public void setFastRoomList(List<Room> fastRoomList) {
		this.fastRoomList = fastRoomList;
	}

	public List<Room> getSortRoomList() {
		return sortRoomList;
	}

	public void setSortRoomList(List<Room> sortRoomList) {
		this.sortRoomList = sortRoomList;
	}
}
