package com.lordcard.rule;

import android.content.Context;

import com.lordcard.entity.CardType;
import com.lordcard.entity.Poker;

/**
 * @author yuanjielong
 * 
 */
public class PokerUtil {

	public static Poker[] getPoker(Context context) {
		Poker[] poker = new Poker[54];
		try {
			for (int i = 0; i < 13; i++) {
				Poker card = new Poker(context);
				card.setStyle(CardType.HEITAO);
				card.setNumber(i);
				if (i == 0) {
					card.setValue(i + 14);
				} else if (i == 1) {
					card.setValue(i + 14);
				} else {
					card.setValue(i + 1);
				}
				card.setBitpamResID(CardType.ID[i]);
				poker[i] = card;
			}
			for (int i = 13; i < 26; i++) {
				Poker card = new Poker(context);
				card.setStyle(CardType.HONGXIN);
				card.setNumber(i);
				if (i == 13) {
					card.setValue(i - 13 + 14);
				} else if (i == 14) {
					card.setValue(15);
				} else {
					card.setValue(i - 13 + 1);
				}
				card.setBitpamResID(CardType.ID[i]);
				poker[i] = card;
			}
			for (int i = 26; i < 39; i++) {
				Poker card = new Poker(context);
				card.setStyle(CardType.MEIHUA);
				card.setNumber(i);
				if (i == 26) {
					card.setValue(i - 26 + 14);
				} else if (i == 27) {
					card.setValue(15);
				} else {
					card.setValue(i - 26 + 1);
				}
				card.setBitpamResID(CardType.ID[i]);
				poker[i] = card;
			}
			for (int i = 39; i < 52; i++) {
				Poker card = new Poker(context);
				card.setStyle(CardType.FANGKUAI);
				card.setNumber(i);
				if (i == 39) {
					card.setValue(i - 39 + 14);
				} else if (i == 40) {
					card.setValue(15);
				} else {
					card.setValue(i - 39 + 1);
				}
				card.setBitpamResID(CardType.ID[i]);
				poker[i] = card;
			}
			for (int i = 52; i < 54; i++) {
				Poker card = new Poker(context);
				card.setStyle(CardType.GUI);
				card.setNumber(i);
				card.setValue(i - 52 + 16);
				card.setBitpamResID(CardType.ID[i]);
				poker[i] = card;
			}
		} catch (Exception e) {
		}

		return poker;

	}
	
	public static Poker getPokerFromNumber(int number,Context context){
		Poker poker = null;
		if(number >0 && number <13){
			/** 黑桃 **/
			poker = new Poker(context);
			poker.setStyle(CardType.HEITAO);
			poker.setNumber(number);
			if (number == 0) {
				poker.setValue(number + 14);
			} else if (number == 1) {
				poker.setValue(number + 14);
			} else {
				poker.setValue(number + 1);
			}
			poker.setBitpamResID(CardType.ID[number]);
			
		}else if(number >=13 && number <26){
			/** 红心 **/
			poker = new Poker(context);
			poker.setStyle(CardType.HONGXIN);
			poker.setNumber(number);
			if (number == 13) {
				poker.setValue(number - 13 + 14);
			} else if (number == 14) {
				poker.setValue(15);
			} else {
				poker.setValue(number - 13 + 1);
			}
			poker.setBitpamResID(CardType.ID[number]);
			
		}else if(number >=26 && number < 39){
			/** 梅花 **/
			poker = new Poker(context);
			poker.setStyle(CardType.MEIHUA);
			poker.setNumber(number);
			if (number == 26) {
				poker.setValue(number - 26 + 14);
			} else if (number == 27) {
				poker.setValue(15);
			} else {
				poker.setValue(number - 26 + 1);
			}
			poker.setBitpamResID(CardType.ID[number]);
			
		}else if(number >= 39 && number < 52){
			/** 方块 **/
			poker = new Poker(context);
			poker.setStyle(CardType.FANGKUAI);
			poker.setNumber(number);
			if (number == 39) {
				poker.setValue(number - 39 + 14);
			} else if (number == 40) {
				poker.setValue(15);
			} else {
				poker.setValue(number - 39 + 1);
			}
			poker.setBitpamResID(CardType.ID[number]);
			
		}else if(number >= 52 && number < 54){
			/**大小王**/
			poker = new Poker(context);
			poker.setStyle(CardType.GUI);
			poker.setNumber(number);
			poker.setValue(number - 52 + 16);
			poker.setBitpamResID(CardType.ID[number]);
		}
		return poker;
	}
	//	public static Poker[] getChoudiPoker(Context context) {
	//		Poker[] poker = new Poker[54];
	//		for (int i = 0; i < 13; i++) {
	//			Poker card = new Poker(context);
	//			card.setStyle(CardType.HEITAO);
	//			card.setNumber(i);
	//			if (i == 0) {
	//				card.setValue(i + 14);
	//			} else if (i == 1) {
	//				card.setValue(i + 14);
	//			} else {
	//				card.setValue(i + 1);
	//			}
	//			card.setBitpamResID(CardType.ID[i]);
	//			poker[i] = card;
	//		}
	//		for (int i = 13; i < 26; i++) {
	//			Poker card = new Poker(context);
	//			card.setStyle(CardType.HONGXIN);
	//			card.setNumber(i);
	//			if (i == 13) {
	//				card.setValue(i - 13 + 14);
	//			} else if (i == 14) {
	//				card.setValue(15);
	//			} else {
	//				card.setValue(i - 13 + 1);
	//			}
	//			card.setBitpamResID(CardType.ID[i]);
	//			poker[i] = card;
	//		}
	//		for (int i = 26; i < 39; i++) {
	//			Poker card = new Poker(context);
	//			card.setStyle(CardType.MEIHUA);
	//			card.setNumber(i);
	//			if (i == 26) {
	//				card.setValue(i - 26 + 14);
	//			} else if (i == 27) {
	//				card.setValue(15);
	//			} else {
	//				card.setValue(i - 26 + 1);
	//			}
	//			card.setBitpamResID(CardType.ID[i]);
	//			poker[i] = card;
	//		}
	//		for (int i = 39; i < 52; i++) {
	//			Poker card = new Poker(context);
	//			card.setStyle(CardType.FANGKUAI);
	//			card.setNumber(i);
	//			if (i == 39) {
	//				card.setValue(i - 39 + 14);
	//			} else if (i == 40) {
	//				card.setValue(15);
	//			} else {
	//				card.setValue(i - 39 + 1);
	//			}
	//			card.setBitpamResID(CardType.ID[i]);
	//			poker[i] = card;
	//		}
	//
	//		return poker;
	//
	//	}

}
