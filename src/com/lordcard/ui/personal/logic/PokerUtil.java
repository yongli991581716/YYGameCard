package com.lordcard.ui.personal.logic;

import java.util.Map;

import com.lordcard.constant.Database;
import com.lordcard.entity.Poker;

/**
 * @author yuanjielong
 * 
 */
public class PokerUtil {

	public static Poker[] getPoker() {
		Poker[] poker = new Poker[54];
		try {
			for (int i = 0; i < 13; i++) {
				Poker card = new Poker(Database.currentActivity);
				card.setStyle(Card.HEITAO);
				card.setNumber(i);
				if (i == 0) {
					card.setValue(i + 14);
				} else if (i == 1) {
					card.setValue(i + 14);
				} else {
					card.setValue(i + 1);
				}
				poker[i] = card;
			}
			for (int i = 13; i < 26; i++) {
				Poker card = new Poker(Database.currentActivity);
				card.setStyle(Card.HONGXIN);
				card.setNumber(i);
				if (i == 13) {
					card.setValue(i - 13 + 14);
				} else if (i == 14) {
					card.setValue(15);
				} else {
					card.setValue(i - 13 + 1);
				}
				poker[i] = card;
			}
			for (int i = 26; i < 39; i++) {
				Poker card = new Poker(Database.currentActivity);
				card.setStyle(Card.MEIHUA);
				card.setNumber(i);
				if (i == 26) {
					card.setValue(i - 26 + 14);
				} else if (i == 27) {
					card.setValue(15);
				} else {
					card.setValue(i - 26 + 1);
				}
				poker[i] = card;
			}
			for (int i = 39; i < 52; i++) {
				Poker card = new Poker(Database.currentActivity);
				card.setStyle(Card.FANGKUAI);
				card.setNumber(i);
				if (i == 39) {
					card.setValue(i - 39 + 14);
				} else if (i == 40) {
					card.setValue(15);
				} else {
					card.setValue(i - 39 + 1);
				}
				poker[i] = card;
			}
			for (int i = 52; i < 54; i++) {
				Poker card = new Poker(Database.currentActivity);
				card.setStyle(Card.GUI);
				card.setNumber(i);
				card.setValue(i - 52 + 16);
				poker[i] = card;
			}
		} catch (Exception e) {
			// Log.i("eden", "--------Poker getPoker NullPointerException");
		}

		return poker;

	}

	public static Poker[] getChoudiPoker() {
		Poker[] poker = new Poker[54];
		for (int i = 0; i < 13; i++) {
			Poker card = new Poker(Database.currentActivity);
			card.setStyle(Card.HEITAO);
			card.setNumber(i);
			if (i == 0) {
				card.setValue(i + 14);
			} else if (i == 1) {
				card.setValue(i + 14);
			} else {
				card.setValue(i + 1);
			}
			poker[i] = card;
		}
		for (int i = 13; i < 26; i++) {
			Poker card = new Poker(Database.currentActivity);
			card.setStyle(Card.HONGXIN);
			card.setNumber(i);
			if (i == 13) {
				card.setValue(i - 13 + 14);
			} else if (i == 14) {
				card.setValue(15);
			} else {
				card.setValue(i - 13 + 1);
			}
			poker[i] = card;
		}
		for (int i = 26; i < 39; i++) {
			Poker card = new Poker(Database.currentActivity);
			card.setStyle(Card.MEIHUA);
			card.setNumber(i);
			if (i == 26) {
				card.setValue(i - 26 + 14);
			} else if (i == 27) {
				card.setValue(15);
			} else {
				card.setValue(i - 26 + 1);
			}
			poker[i] = card;
		}
		for (int i = 39; i < 52; i++) {
			Poker card = new Poker(Database.currentActivity);
			card.setStyle(Card.FANGKUAI);
			card.setNumber(i);
			if (i == 39) {
				card.setValue(i - 39 + 14);
			} else if (i == 40) {
				card.setValue(15);
			} else {
				card.setValue(i - 39 + 1);
			}
			poker[i] = card;
		}

		return poker;

	}

	/**
	 * 给出对应类型的牌所在的map
	 * @param ddzData 斗地主数据结构
	 * @param cardNum 牌数量
	 * @param cardType 牌类型
	 * @return
	 */
	public static Map<Integer, PokerOfOnePlay> whichArray(DouDiZhuLogic ddzData, int cardNum, int cardType) {
		Map<Integer, PokerOfOnePlay> play = null;
		switch (cardType) {
		case DoudizhuRule.Danpai:
			play = ddzData.getSanPai();
			break;
		case DoudizhuRule.Yidui:
			play = ddzData.getDuiZi();
			break;
		case DoudizhuRule.Santiao:
		case DoudizhuRule.Sandaiyi:
		case DoudizhuRule.Sandaier:
			play = ddzData.getSanZhang();
			break;
		case DoudizhuRule.zhadan:
		case DoudizhuRule.wangzha:
			play = ddzData.getZhaDan();
			break;
		case DoudizhuRule.sidaiyi:
		case DoudizhuRule.sidaier:
			play = ddzData.getSiZhang();
			break;
		case DoudizhuRule.feiji:
			play = ddzData.getFeiJi().get(cardNum / 3);
			break;
		case DoudizhuRule.feijidaisan:
			play = ddzData.getFeiJi().get(cardNum / 4);
			break;
		case DoudizhuRule.feijidaidui:
			play = ddzData.getFeiJi().get(cardNum / 5);
			break;
		case DoudizhuRule.liandui:
			play = ddzData.getLianDui().get(cardNum / 2);
			break;
		case DoudizhuRule.shunzi:
			play = ddzData.getShunZi().get(cardNum);
			break;
		}
		return play;
	}

}
