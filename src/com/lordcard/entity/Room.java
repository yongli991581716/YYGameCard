package com.lordcard.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 斗地主房间
 * 
 * @author admin
 * 
 */
public class Room {

	/** 普通房间  */
	public static final Integer GENERAL_ROOM = 0;
	/** 超快赛房间 */
	public static final Integer ULTRAFAST_ROOM = 1;
	/** 排位赛房间  */
	public static final Integer RANK_ROOM = 2;
	@Expose @SerializedName("c") private String code; // 房间编号
	@Expose @SerializedName("hn") private String name; // 房间名称
	@Expose @SerializedName("r") private int ratio; // 房间倍数
	@Expose @SerializedName("bp") private int basePoint; // 房间每局底数
	@Expose @SerializedName("u") private int unitType; // 交易单位 0 积分、1 豆
	@Expose @SerializedName("l") private long limit = 0; // 允许进入的最低限制
	@Expose @SerializedName("pw") private String password; // 房间密码
	@Expose @SerializedName("ln") private int limitGroupNum = 0; // 每次参与游戏的人数
	@Expose @SerializedName("cn") private long commissionNum; // 抽取的拥金数
	@Expose @SerializedName("ht") private int homeType; // 0:普通房间/1:VIP/2:VIP包房/3:专场/4:钻石专场/5:合成挤专场/6:
	@Expose @SerializedName("hc") private String hallCode; // 大厅编号
	@Expose @SerializedName("up") private long upper; // 上限
	private int registFee; // 报名费
	private String explanation;// 环境说明
	@Expose @SerializedName("s") private int sort; // 房间在大厅排序
	@Expose @SerializedName("mc") private int maxCount; // 房间最大支持人数 0不限制
	@Expose @SerializedName("v") private int version; // 房间版本号 版本号 有变化代表房间数据有更改，前台需要更新
	@Expose @SerializedName("rh") private String resHall; // 大厅展示资源下载目录
	@Expose @SerializedName("rhu") private String resHallUrl; // 大厅展示资源下载地址
	@Expose @SerializedName("si") private String serverIp; // 游戏服务器IP
	@Expose @SerializedName("gs") private String gameServer; // 游戏服务器地址（临时变量，不存储） 格式 192.168.0.1:8080
	@Expose @SerializedName("rl") private String rule; // 专场游戏物品赠送规则
	@Expose @SerializedName("sr") private String startRace; // 专场开始时间
	@Expose @SerializedName("pt") private Integer playType; // 游戏比赛类型
	@Expose @SerializedName("de") private String detail; // 游戏房间说明
	@Expose @SerializedName("ma") private String maxAward; // 最大奖励
	@Expose @SerializedName("pp") private Long prizePool; //奖金池金豆
	@Expose @SerializedName("ro") private GameRoomRuleDetail roomDetail; // 游戏房间详细说明
	@Expose @SerializedName("ra") private Integer raceType; // 比赛类型(1即时赛，2排位赛)
	@Expose @SerializedName("rt") private int roomType; // 房间类型：0大厅房，1超快赛房，2排位赛房/6:挖矿场
	private Integer count = 0; // 当前在线的人数
	private String msg; // 帮助提示信息
	private String enter;

	public Room() {}

	public Room(String code, String name, int homeType, String msg, int registFee) {
		super();
		this.code = code;
		this.name = name;
		this.msg = msg;
		this.homeType = homeType;
		this.registFee = registFee;
	}

	/**
	 * @param code
	 *            房间编号
	 * @param limit
	 *            最低进入限制
	 * @param basePoint
	 *            低数
	 * @param ratio
	 *            最低倍数
	 * @param homeType
	 *            房间类型
	 * @param registFee
	 *            报名费
	 * @param explanation
	 *            环境说明
	 */
	public Room(String code, long limit, String enter, int basePoint, int ratio, int homeType, String name, int registFee, String explanation) {
		super();
		this.code = code;
		this.limit = limit;
		this.basePoint = basePoint;
		this.ratio = ratio;
		this.homeType = homeType;
		this.enter = enter;
		this.name = name;
		this.registFee = registFee;
		this.explanation = explanation;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public int getBasePoint() {
		return basePoint;
	}

	public void setBasePoint(int basePoint) {
		this.basePoint = basePoint;
	}

	public int getUnitType() {
		return unitType;
	}

	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLimitGroupNum() {
		return limitGroupNum;
	}

	public void setLimitGroupNum(int limitGroupNum) {
		this.limitGroupNum = limitGroupNum;
	}

	public int getHomeType() {
		return homeType;
	}

	public void setHomeType(int homeType) {
		this.homeType = homeType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getEnter() {
		return enter;
	}

	public void setEnter(String enter) {
		this.enter = enter;
	}

	public String getGameServer() {
		return gameServer;
	}

	public void setGameServer(String gameServer) {
		this.gameServer = gameServer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public int getRegistFee() {
		return registFee;
	}

	public void setRegistFee(int registFee) {
		this.registFee = registFee;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getResHall() {
		return resHall;
	}

	public void setResHall(String resHall) {
		this.resHall = resHall;
	}

	public String getResHallUrl() {
		return resHallUrl;
	}

	public void setResHallUrl(String resHallUrl) {
		this.resHallUrl = resHallUrl;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getStartRace() {
		return startRace;
	}

	public void setStartRace(String startRace) {
		this.startRace = startRace;
	}

	public long getCommissionNum() {
		return commissionNum;
	}

	public void setCommissionNum(long commissionNum) {
		this.commissionNum = commissionNum;
	}

	public final Integer getPlayType() {
		return playType;
	}

	public final void setPlayType(Integer playType) {
		this.playType = playType;
	}

	public final String getDetail() {
		return detail;
	}

	public final void setDetail(String detail) {
		this.detail = detail;
	}

	public final String getMaxAward() {
		return maxAward;
	}

	public final void setMaxAward(String maxAward) {
		this.maxAward = maxAward;
	}

	public final GameRoomRuleDetail getRoomDetail() {
		return roomDetail;
	}

	public final void setRoomDetail(GameRoomRuleDetail roomDetail) {
		this.roomDetail = roomDetail;
	}

	public final Integer getRaceType() {
		return raceType;
	}

	public final void setRaceType(Integer raceType) {
		this.raceType = raceType;
	}

	public final int getRoomType() {
		return roomType;
	}

	public final void setRoomType(int roomType) {
		this.roomType = roomType;
	}

	public long getUpper() {
		return upper;
	}

	public void setUpper(long upper) {
		this.upper = upper;
	}

	public final Long getPrizePool() {
		return prizePool;
	}

	public final void setPrizePool(Long prizePool) {
		this.prizePool = prizePool;
	}
}
