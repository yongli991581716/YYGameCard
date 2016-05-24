package com.lordcard.ui.personal.logic.strategy.impl;

import java.util.ArrayList;
import java.util.List;

import com.lordcard.entity.Poker;
import com.lordcard.ui.personal.logic.DouDiZhuLogic;
import com.lordcard.ui.personal.logic.PokerOfOnePlay;
import com.lordcard.ui.personal.logic.PokerOfOneValue;
import com.lordcard.ui.personal.logic.strategy.interfaces.Strategy;

public class StrategySanPai implements Strategy {

	final private static int paiInteval = 6;
	DouDiZhuLogic ddzDataOriginal = null;
	DouDiZhuLogic ddzData = null;
	final static private int minValue = 12;

	public StrategySanPai(DouDiZhuLogic ddzData) {
		this.ddzDataOriginal = ddzData;
	}

	@Override
	public int check() {
		if (ddzDataOriginal.getNowPlaying() == null || ddzDataOriginal.isInitiative() == false) {
			return 0;
		}

		if (ddzDataOriginal.getNowPlaying().size() != 1) {
			return 0;
		}
		return 1;
	}

	@Override
	public int getPoint() {
		List<Poker> ps = new ArrayList<Poker>();

		for (PokerOfOneValue poov : ddzDataOriginal.getPokers().values()) {
			for (Poker p : poov.getPokers()) {
				if (!p.isUsed()) {
					ps.add(p);
				}
			}
		}

		ddzData = new DouDiZhuLogic(ps);
		ddzData.setPokerNum(ddzDataOriginal.getPokerNum());
		ddzData.setNowPlaying(this.ddzDataOriginal.getNowPlaying());
		ddzData.setNowPlayingAttachment(this.ddzDataOriginal.getNowPlayingAttachment());

		int xiaosannum = 0;
		for (PokerOfOnePlay poop : ddzData.getSanPai().values()) {
			if (poop.getOnePlay().size() != 0 && poop.getMaxValue() < minValue) {
				++xiaosannum;
			}
		}
		int v = ddzData.getNowPlaying().get(0).getValue();
		if (v >= minValue && xiaosannum == 0) {
			return 0;
		}

		for (int i = v + paiInteval; i < 18; ++i) {
			if (!ddzData.getSanPai().containsKey(i)) {
				continue;
			}
			if (ddzData.getSanPai().get(i).getOnePlay().size() > 0) {
				//System.out.print("SanPai策略得分 " + 0);
				return 0;
			}
		}
		//System.out.print("SanPai策略得分 " + (-12));
		return -8;
	}

	@Override
	public List<Poker> handler() {
		return null;
	}

}
