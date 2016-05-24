package com.lordcard.ui.personal.logic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.lordcard.entity.Poker;

public class PlayAlone implements Serializable {

	private static final long serialVersionUID = 186874763769645267L;

	private String id; // 出牌人编号
	private int count; // 剩余的牌数
	private List<Poker> precards; // 上手牌；
	private List<Poker> cards; // 出的牌的json格式字符串
	private int order; // 游戏出牌顺序
	private boolean call; // 叫分
	private int callOrder; // 开始叫分的人位置
	private boolean escape; // 是否逃跑
	private Integer nextOrder; // 下一个出牌人位置
	private Map<Integer, String> nickMap; // 玩家名称

	private int ratio; //总倍数
	private int payment = 0; // 每局支付多少 负数为输 正数为赢
	private int unitType = 0; // 类型 积分还是豆 0 积分、1 豆
	private double extract = 0; // 抽成
	private int goodsCount = 0; // 物品赠送数量
	private Integer masterOrder; // 庄家位置编号

	private int bombRatio = 1; //炸弹倍数(默认1)
	private int springRatio = 1; //春天倍数(默认1)
	private int doubleRatio = 1; //加倍倍数(默认1)
	private int bean = 0; //最新所有金豆
	// 炸弹、春天、加倍、总倍数、底数、金豆、经验、当前金豆、等级
	private Integer iq; // 最新等级
	private Integer intellect; // 最新经验
	private Integer addIntellect; // 输赢经验

	private boolean upgrade; //是否升级了
	private String celebratedText; //庆祝文字
	private String celebratedEffect; //庆祝效果

	private int baseRatio = 1; //基础倍数(默认1)
	private int callRatio = 1; //叫分倍数(默认1)
	private Integer isTitle; // 是否为称号:0=不是,1=是

	public PlayAlone() {}

	public List<Poker> getPrecards() {
		return precards;
	}

	public void setPrecards(List<Poker> precards) {
		this.precards = precards;
	}

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

	public List<Poker> getCards() {
		return cards;
	}

	public void setCards(List<Poker> cards) {
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

	public int getPayment() {
		return payment;
	}

	public void setPayment(int payment) {
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

	public final void setBean(int bean) {
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
