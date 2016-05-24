package com.lordcard.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 增加经验后，返回给用户的数据
 * 包括升级时赠送的物品显示
 */
public class GameIQUpgrade {

	@Expose
	@SerializedName("iq")
	private Integer iq; // 等级等级

	@Expose
	@SerializedName("in")
	private Integer intellect; // 当前经验

	@Expose
	@SerializedName("ni")
	private Integer nextIntellect; // 达到下一级等级所需经验

	@Expose
	@SerializedName("up")
	private boolean upgrade; // 是否升级了

	@Expose
	@SerializedName("it")
	private Integer isTitle; // 是否为称号:0=不是,1=是

	@Expose
	@SerializedName("ce")
	private String celebratedText; // 庆祝文字

	@Expose
	@SerializedName("ee")
	private String celebratedEffect; // 庆祝效果

	public Integer getIq() {
		return iq;
	}

	public void setIq(Integer iq) {
		this.iq = iq;
	}

	public Integer getIntellect() {
		return intellect;
	}

	public void setIntellect(Integer intellect) {
		this.intellect = intellect;
	}

	public Integer getNextIntellect() {
		return nextIntellect;
	}

	public void setNextIntellect(Integer nextIntellect) {
		this.nextIntellect = nextIntellect;
	}

	public boolean isUpgrade() {
		return upgrade;
	}

	public void setUpgrade(boolean upgrade) {
		this.upgrade = upgrade;
	}

	public Integer getIsTitle() {
		return isTitle;
	}

	public void setIsTitle(Integer isTitle) {
		this.isTitle = isTitle;
	}

	public String getCelebratedText() {
		return celebratedText;
	}

	public void setCelebratedText(String celebratedText) {
		this.celebratedText = celebratedText;
	}

	public String getCelebratedEffect() {
		return celebratedEffect;
	}

	public void setCelebratedEffect(String celebratedEffect) {
		this.celebratedEffect = celebratedEffect;
	}

}
