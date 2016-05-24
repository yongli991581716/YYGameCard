/**
 */
package com.lordcard.entity;

/**
 * @ClassName: GameContent
 * @Description: 内容
 * @author shaohu
 * @date 2013-4-10 上午11:43:32
 * 
 */

public class GameContent {
	private String id;
	private String title;
	private Integer type; // 标示 文本类型 1: 赌坛指南
	private Integer display; // 显示方式 1:文本显示 2:图片显示
	private Integer ordernum; // 排序
	private String description; // 描述

	public GameContent() {
		super();
	}

	/**
	 * @param id
	 * @param title
	 * @param type
	 * @param display
	 * @param ordernum
	 * @param description
	 */
	public GameContent(String id, String title, Integer type, Integer display, Integer ordernum, String description) {
		super();
		this.id = id;
		this.title = title;
		this.type = type;
		this.display = display;
		this.ordernum = ordernum;
		this.description = description;
	}

	/**
	 * @return the id
	 */

	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */

	public Integer getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the display
	 */

	public Integer getDisplay() {
		return display;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(Integer display) {
		this.display = display;
	}

	/**
	 * @return the ordernum
	 */
	public Integer getOrdernum() {
		return ordernum;
	}

	/**
	 * @param ordernum
	 *            the ordernum to set
	 */
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}

	/**
	 * @return the description
	 */

	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
