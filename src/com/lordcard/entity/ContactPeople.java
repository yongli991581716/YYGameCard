package com.lordcard.entity;

import android.graphics.drawable.Drawable;

/**
 * 联系人信息 model.ContactPeople
 * 
 * @author Administrator <br/>
 *         create at 2013 2013-3-19 下午12:10:01
 */
public class ContactPeople {

	private int index;
	private String name = null;
	private String number = null;
	private String md5Number = null; // 手机号加密结果
	private String sort_key = null;
	private String sortName = null;
	private Drawable photo = null;
	
	private boolean checkdownload = false;

	public boolean isCheckdownload() {
		return checkdownload;
	}

	public void setCheckdownload(boolean checkdownload) {
		this.checkdownload = checkdownload;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSort_key() {
		return sort_key;
	}

	public void setSort_key(String sort_key) {
		this.sort_key = sort_key;
	}
	public String getSort_Name() {
		return sortName;
	}

	public void setSort_Name(String sortName) {
		this.sortName = sortName;
	}
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Drawable getPhoto() {
		return photo;
	}

	public void setPhoto(Drawable photo) {
		this.photo = photo;
	}

	public String getMd5Number() {
		return md5Number;
	}

	public void setMd5Number(String md5Number) {
		this.md5Number = md5Number;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
