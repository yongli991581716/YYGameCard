package com.lordcard.ui.personal.logic;

import java.util.ArrayList;
import java.util.List;

public class ClientUser {
	private int order;
	private String name;
	private List<Integer> cards = new ArrayList<Integer>();
	private String gender = "0";
	private int callPoint;
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getCallPoint() {
		return callPoint;
	}

	public void setCallPoint(int callPoint) {
		this.callPoint = callPoint;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getCards() {
		return cards;
	}

	public void setCards(List<Integer> cards) {
		this.cards = cards;
	}



}
