package com.lordcard.entity;

import java.io.Serializable;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 登录游戏的用户信息
 */
public class GameUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose @SerializedName("a") private String account; // 游戏账号
	@Expose @SerializedName("ra") private String relaAccount; // 关联账号
	@Expose @SerializedName("n") private String nickname; // 昵称
	@Expose @SerializedName("t") private int type; // 玩家类型 0：一般玩家 1：机器人
	@Expose @SerializedName("b") private Long bean=0l; //金豆
	@Expose @SerializedName("gs") private String gameServer; // 所在游戏服务器
	@Expose @SerializedName("cn") private String cusName; // 姓名
	@Expose @SerializedName("p") private String userPwd; // 账户密码
	@Expose @SerializedName("mp") private String md5Pwd; // 加密密码
	private String confirmPwd;
	@Expose @SerializedName("gd") private String gender; // 性别 0保密1女2男
	@Expose @SerializedName("pn") private String phoneNum; // 联系号码
	@Expose @SerializedName("e") private String email; // email
	@Expose @SerializedName("iup") private String isUpdatedPwd; // 是否修改过密码0未修改过1已经修改过
	@Expose @SerializedName("cd") private String createDate; // 创建时间
	@Expose @SerializedName("ud") private String updateDate; // 更新时间
	@Expose @SerializedName("hi") private String headImage; // 头像
	@Expose @SerializedName("so") private Integer sortOrder; // 用户排名
	@Expose @SerializedName("ak") private String authKey; // 安全验证签名密钥
	@Expose @SerializedName("rt") private String roomTime; // 房间更新时间
	// 已经赢[钻石挖矿场使用]
	@Expose @SerializedName("hw") private Integer hasWin = 0;
	// 记牌器剩余有效时间(s)
	@Expose @SerializedName("jt") private int jiPaiQiTime;
	// 需要赢
	@Expose @SerializedName("nw") private Integer needWin = 0;
	/**
	 * 比赛剩余秒数
	 */
	@Expose @SerializedName("ot") private Long overTime;
	@Expose @SerializedName("lt") private String loginToken; // 游戏内部登录Token
	@Expose @SerializedName("gt") private Integer gameType; // 当前玩的游戏类型 1：斗地主 2：麻将 3：锄大地
	@Expose @SerializedName("ir") private boolean isPlay = false; // 是否参加比赛 true:已参加 false:未参加
	@Expose @SerializedName("rn") private String roomName; // 房间名称
	@Expose private int round; // 轮次
	@Expose private int level; // 比赛阶段 1:预赛 2:决赛
	@Expose private Integer rank; // 用户排名
	@Expose private Long cred = (long) 0; // 用户积分
	@Expose @SerializedName("iq") private Integer iq = 0; // 等级等级
	@Expose @SerializedName("ii") private Map<String, String> iqImg; // 等级头像图标(0女地主,1男地主,2女农民,3男农民)
	@Expose @SerializedName("tl") private String title; // 称号
	@Expose @SerializedName("in") private Integer intellect; // 当前经验
	@Expose @SerializedName("ni") private Integer nextIntellect; // 达到下一级等级所需经验
	@Expose @SerializedName("li") private Map<String, String> levelImg; // 等级图标(key:count,value:imagePath)
	// 钻石的数量
	@Expose @SerializedName("dis") private Long diamSum = (long) 0;
	// 无记牌器时的提示文本
	@Expose @SerializedName("tpm") private String tipMes;

	public String getTipMes() {
		return tipMes;
	}

	public void setTipMes(String tipMes) {
		this.tipMes = tipMes;
	}

	public GameUser() {}

	public String getGameServer() {
		return gameServer;
	}

	public void setGameServer(String gameServer) {
		this.gameServer = gameServer;
	}

	public void setBean(Long bean) {
		this.bean = bean;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getBean() {
		return bean;
	}

	public void setBean(long bean) {
		this.bean = bean;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getIsUpdatedPwd() {
		return isUpdatedPwd;
	}

	public void setIsUpdatedPwd(String isUpdatedPwd) {
		this.isUpdatedPwd = isUpdatedPwd;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getRelaAccount() {
		return relaAccount;
	}

	public void setRelaAccount(String relaAccount) {
		this.relaAccount = relaAccount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRoomTime() {
		return roomTime;
	}

	public void setRoomTime(String roomTime) {
		this.roomTime = roomTime;
	}

	public final Long getOverTime() {
		return overTime;
	}

	public final void setOverTime(Long overTime) {
		this.overTime = overTime;
	}

	public String getMd5Pwd() {
		return md5Pwd;
	}

	public void setMd5Pwd(String md5Pwd) {
		this.md5Pwd = md5Pwd;
	}

	public final int getRound() {
		return round;
	}

	public final void setRound(int round) {
		this.round = round;
	}

	public final int getLevel() {
		return level;
	}

	public final void setLevel(int level) {
		this.level = level;
	}

	public final Integer getRank() {
		return rank;
	}

	public final void setRank(Integer rank) {
		this.rank = rank;
	}

	public final Long getCred() {
		return cred;
	}

	public final void setCred(Long cred) {
		this.cred = cred;
	}

	public final boolean isPlay() {
		return isPlay;
	}

	public final void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	public final String getRoomName() {
		return roomName;
	}

	public final void setRoomName(String roomName) {
		
		this.roomName = roomName;
	}

	public final Integer getIq() {
		return iq;
	}

	public final void setIq(Integer iq) {
		this.iq = iq;
	}

	public final Integer getIntellect() {
		return intellect;
	}

	public final void setIntellect(Integer intellect) {
		this.intellect = intellect;
	}

	public final Integer getNextIntellect() {
		return nextIntellect;
	}

	public final void setNextIntellect(Integer nextIntellect) {
		this.nextIntellect = nextIntellect;
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final Map<String, String> getIqImg() {
		return iqImg;
	}

	public final void setIqImg(Map<String, String> iqImg) {
		this.iqImg = iqImg;
	}

	public final Map<String, String> getLevelImg() {
		return levelImg;
	}

	public final void setLevelImg(Map<String, String> levelImg) {
		this.levelImg = levelImg;
	}

	public Integer getHasWin() {
		return hasWin;
	}

	public void setHasWin(Integer hasWin) {
		this.hasWin = hasWin;
	}

	public int getJiPaiQiTime() {
		return jiPaiQiTime;
	}

	public void setJiPaiQiTime(int jiPaiQiTime) {
		this.jiPaiQiTime = jiPaiQiTime;
	}

	public Integer getNeedWin() {
		return needWin;
	}

	public void setNeedWin(Integer needWin) {
		this.needWin = needWin;
	}

	public Long getDiamSum() {
		return diamSum;
	}

	public void setDiamSum(Long diamSum) {
		this.diamSum = diamSum;
	}
}
