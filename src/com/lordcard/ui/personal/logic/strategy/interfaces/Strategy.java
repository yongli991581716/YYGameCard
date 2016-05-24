package com.lordcard.ui.personal.logic.strategy.interfaces;

import java.util.List;

import com.lordcard.entity.Poker;

public interface Strategy {
	public int check();
	public int getPoint();
	public List<Poker> handler(); 
	
}
