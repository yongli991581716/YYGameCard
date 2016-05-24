package com.lordcard.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GamePropsType {
	@Expose
	@SerializedName("ty")
	private String type; // 类型1=图集content=List<Map<String,String>>
	@Expose
	@SerializedName("gc")
	private String goodsCode; // 物品编号
	@Expose
	@SerializedName("na")
	private String name; // 名称
	@Expose
	@SerializedName("pp")
	private String picPath; // 封面图
	@Expose
	@SerializedName("pr")
	private Integer price; // 价格
	@Expose
	@SerializedName("ti")
	private String title; // 标题
	@Expose
	@SerializedName("ct")
	private String content; // 道具内容
	@Expose
	@SerializedName("on")
	private Integer orderNum; // 排序

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

}
