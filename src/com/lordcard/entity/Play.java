package com.lordcard.entity;

import java.io.Serializable;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Play implements Serializable {

	private static final long serialVersionUID = 186874763769645267L;

	@Expose @SerializedName("i") 	private String id; // 出牌人编号
	@Expose @SerializedName("ct") 	private int count; // 剩余的牌数
	@Expose @SerializedName("cd") 	private String cards; // 出的牌的json格式字符串
	@Expose @SerializedName("o") 	private int order; // 游戏出牌顺序
	@Expose @SerializedName("cl") 	private boolean call; // 叫分
	@Expose @SerializedName("co") 	private int callOrder; // 开始叫分的人位置
	@Expose @SerializedName("e") 	private boolean escape; // 是否逃跑
	@Expose @SerializedName("no") 	private Integer nextOrder; // 下一个出牌人位置
	@Expose @SerializedName("nm") 	private Map<Integer, String> nickMap; // 玩家名称

	@Expose @SerializedName("r") 	private int ratio; //总倍数
	@Expose @SerializedName("pm") 	private double payment = 0; // 每局支付多少 负数为输 正数为赢
	@Expose @SerializedName("u") 	private int unitType = 0; // 类型 积分还是豆 0 积分、1 豆
	@Expose @SerializedName("et") 	private double extract = 0; // 抽成
	@Expose @SerializedName("gc") 	private int goodsCount = 0; // 物品赠送数量
	@Expose @SerializedName("m") 	private Integer masterOrder; // 庄家位置编号
	
	@Expose @SerializedName("bo") private int bombRatio = 1;				//炸弹倍数(默认1)
	@Expose @SerializedName("sr") private int springRatio = 1;				//春天倍数(默认1)
	@Expose @SerializedName("dr") private int doubleRatio = 1;				//加倍倍数(默认1)
	@Expose @SerializedName("b") private double bean = 0;					//最新所有金豆
	// 炸弹、春天、加倍、总倍数、底数、金豆、经验、当前金豆、等级
	@Expose @SerializedName("iq") private Integer iq=0;						// 最新等级
	@Expose @SerializedName("in") private Integer intellect=0 ;				// 最新经验
	@Expose @SerializedName("ai") private Integer addIntellect=0;				// 输赢经验
	
	@Expose @SerializedName("up") private boolean	upgrade;				//是否升级了
	@Expose @SerializedName("ce") private String	celebratedText;			//庆祝文字
	@Expose @SerializedName("ee") private String	celebratedEffect;		//庆祝效果
	
	@Expose @SerializedName("br") private int baseRatio = 1;				//基础倍数(默认1)
	@Expose @SerializedName("cr") private int callRatio = 1;				//叫分倍数(默认1)
	@Expose@SerializedName("it")	private Integer isTitle; // 是否为称号:0=不是,1=是
	
	
	public Play() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isCall() {
		return call;
	}

	public void setCall(boolean call) {
		this.call = call;
	}

	public int getCallOrder() {
		return callOrder;
	}

	public void setCallOrder(int callOrder) {
		this.callOrder = callOrder;
	}

	public boolean isEscape() {
		return escape;
	}

	public void setEscape(boolean escape) {
		this.escape = escape;
	}

	public Integer getNextOrder() {
		return nextOrder;
	}

	public void setNextOrder(Integer nextOrder) {
		this.nextOrder = nextOrder;
	}

	public Map<Integer, String> getNickMap() {
		return nickMap;
	}

	public void setNickMap(Map<Integer, String> nickMap) {
		this.nickMap = nickMap;
	}

	public Integer getMasterOrder() {
		return masterOrder;
	}

	public void setMasterOrder(Integer masterOrder) {
		this.masterOrder = masterOrder;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public int getUnitType() {
		return unitType;
	}

	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}

	public double getExtract() {
		return extract;
	}

	public void setExtract(double extract) {
		this.extract = extract;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public final int getBombRatio() {
		return bombRatio;
	}

	public final void setBombRatio(int bombRatio) {
		this.bombRatio = bombRatio;
	}

	public final int getSpringRatio() {
		return springRatio;
	}

	public final void setSpringRatio(int springRatio) {
		this.springRatio = springRatio;
	}

	public final int getDoubleRatio() {
		return doubleRatio;
	}

	public final void setDoubleRatio(int doubleRatio) {
		this.doubleRatio = doubleRatio;
	}

	public final double getBean() {
		return bean;
	}

	public final void setBean(double bean) {
		this.bean = bean;
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

	public final Integer getAddIntellect() {
		return addIntellect;
	}

	public final void setAddIntellect(Integer addIntellect) {
		this.addIntellect = addIntellect;
	}

	public final boolean isUpgrade() {
		return upgrade;
	}

	public final void setUpgrade(boolean upgrade) {
		this.upgrade = upgrade;
	}

	public final String getCelebratedText() {
		return celebratedText;
	}

	public final void setCelebratedText(String celebratedText) {
		this.celebratedText = celebratedText;
	}

	public final String getCelebratedEffect() {
		return celebratedEffect;
	}

	public final void setCelebratedEffect(String celebratedEffect) {
		this.celebratedEffect = celebratedEffect;
	}

	public final int getBaseRatio() {
		return baseRatio;
	}

	public final void setBaseRatio(int baseRatio) {
		this.baseRatio = baseRatio;
	}

	public final int getCallRatio() {
		return callRatio;
	}

	public final void setCallRatio(int callRatio) {
		this.callRatio = callRatio;
	}

	public final Integer getIsTitle() {
		return isTitle;
	}

	public final void setIsTitle(Integer isTitle) {
		this.isTitle = isTitle;
	}
}
