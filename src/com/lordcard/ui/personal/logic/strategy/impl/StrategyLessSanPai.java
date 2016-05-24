package com.lordcard.ui.personal.logic.strategy.impl;

import java.util.ArrayList;
import java.util.List;

import com.lordcard.entity.Poker;
import com.lordcard.ui.personal.logic.DouDiZhuLogic;
import com.lordcard.ui.personal.logic.PokerOfOnePlay;
import com.lordcard.ui.personal.logic.PokerOfOneValue;
import com.lordcard.ui.personal.logic.strategy.interfaces.Strategy;

public class StrategyLessSanPai implements Strategy {

	final private static int littleDuiValue = 11;
	DouDiZhuLogic ddzDataOriginal = null;
	DouDiZhuLogic ddzData = null;

	public StrategyLessSanPai(DouDiZhuLogic ddzData) {
		this.ddzDataOriginal = ddzData;
	}

	@Override
	public int check() {
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

		int sanPaiNum = 0;
		for (PokerOfOnePlay poop : ddzData.getSanPai().values()) {
			if (poop.getOnePlay().size() != 0 && poop.getMaxValue() < 13) {
				++sanPaiNum;
			}
		}

		int xiaoDuiNum = 0;

		for (PokerOfOnePlay poop : ddzData.getDuiZi().values()) {
			if (poop.getOnePlay().size() != 0 && poop.getMaxValue() < littleDuiValue) {
				++xiaoDuiNum;
			}
		}

		if (sanPaiNum + xiaoDuiNum == 0) {
			return 0;
		}
		int sanZhangNum = 0;
		for (PokerOfOnePlay poop : ddzData.getSanZhang().values()) {
			if (poop.getOnePlay().size() != 0) {
				++sanZhangNum;
			}
		}

		if (ddzData.getSanZhang().get(15) != null) {
			--sanZhangNum;
		}
//		int siZhangNum = 0;
//		for (PokerOfOnePlay poop : ddzData.getSiZhang().values()) {
//			if (poop.getOnePlay().size() != 0) {
//				++siZhangNum;
//			}
//		}

//		if (ddzData.getSiZhang().get(15) != null) {
//			--siZhangNum;
//		}

		int bigSanPaiNum = 0;
		if (ddzData.getPokers().containsKey(15) && ddzData.getPokers().get(15).getUnusedNum() > 0) {
//			bigSanPaiNum += ddzData.getPokers().get(15).getUnusedNum();
			bigSanPaiNum++;
		}
		if (ddzData.getPokers().containsKey(16)) {
			bigSanPaiNum += ddzData.getPokers().get(16).getUnusedNum();
		}
		if (ddzData.getPokers().containsKey(17)) {
			bigSanPaiNum += ddzData.getPokers().get(17).getUnusedNum();
		}
		int ret = 0;
		
		if (bigSanPaiNum >= sanPaiNum) {
			bigSanPaiNum -= sanPaiNum;
			sanPaiNum = 0;

		} else {
			sanPaiNum -= bigSanPaiNum;
			bigSanPaiNum = 0;
		}

		if (sanPaiNum > 0) {
			if (sanPaiNum >= sanZhangNum) {
				sanPaiNum -= sanZhangNum;
				sanZhangNum = 0;
			} else {
				xiaoDuiNum -= sanZhangNum;
				sanPaiNum = 0;
			}
		}

		if (xiaoDuiNum < 0) {
			xiaoDuiNum = 0;
		}

		if (sanPaiNum > 0 || xiaoDuiNum > 0) {
			ret = sanPaiNum * (-10) + xiaoDuiNum * (-5);
			//System.out.print(" LessSanPai策略得分 " + ret);
			return ret;
		}

		return 0;

	}

	@Override
	public List<Poker> handler() {
		return null;
	}

}
