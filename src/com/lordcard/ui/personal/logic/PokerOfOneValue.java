package com.lordcard.ui.personal.logic;

import java.util.ArrayList;
import java.util.List;

import com.lordcard.entity.Poker;

public class PokerOfOneValue {
	private int value;
	private List<Poker> pokers = new ArrayList<Poker>();
	public boolean addCard(Poker p){
		if (p.getValue() != value){
			return false;
		}
		pokers.add(p);
		return true;
	}
	
	public PokerOfOneValue(int value){
		this.value = value;
	}
	
	/**
	 * 
	 */
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public List<Poker> getPokers() {
		return pokers;
	}
	public void setPokers(List<Poker> pokers) {
		this.pokers = pokers;
	}
	public int PokerNum(){
		return pokers.size();
	}
	public void clearUsedState(){
		for(Poker p : pokers){
			p.setUsed(false);
		}
	}
	public int getUnusedNum() {
		int i = 0;
		for(Poker p : pokers){
			if (!p.isUsed()){
				++i;
			}
		}
		return i;
	}
	public Poker getOneUnusedPoker(){
		for(Poker p : pokers){
			if (!p.isUsed()){
				return p;
			}
		}
		return null;
	}
	public List<Poker> getUnusedPoker(int num){
		List<Poker> ret = new ArrayList<Poker>();
		for(Poker p : pokers){
			if (!p.isUsed()){
				ret.add(p);
				//p.setUsed(true);
			}
			if (ret.size() == num){
				return ret;
			}
		}
		return ret;
	}
	public void setUsedState(boolean isUsed) {
		for(Poker p : pokers){
			p.setUsed(true);
		}
		
	}
	public List<Poker> getPokerIgnoreUsedState(int num) {
		List<Poker> ret = new ArrayList<Poker>();
		for(Poker p : pokers){
				ret.add(p);
			if (ret.size() == num){
				return ret;
			}
		}
		return ret;
	}
}
