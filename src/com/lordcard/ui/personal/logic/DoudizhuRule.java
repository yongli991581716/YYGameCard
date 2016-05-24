package com.lordcard.ui.personal.logic;

import java.util.ArrayList;
import java.util.List;

import com.lordcard.entity.Poker;

public class DoudizhuRule {

	public static final int Danpai = 1;
	public static final int Yidui = 2;
	public static final int Santiao = 3;
	public static final int Sandaiyi = 4;
	public static final int Sandaier = 5;
	public static final int zhadan = 6;
	public static final int siZhang = 15;
	public static final int sidaiyi = 7;
	public static final int sidaier = 8;
	public static final int shunzi = 9;
	public static final int feiji = 10;
	public static final int feijidaisan = 11;
	public static final int feijidaidui = 12;
	public static final int wangzha = 13;
	public static final int liandui = 14;
	public static final int error = 0;

	public static int getMaxNumber(List<Poker> cards) {
		int max = 0;
		int count = getPokeCount(cards);
		for (int i = 0; i < cards.size(); i++) {
			if (count == numberCount(cards.get(i).getValue(), cards)) {
				max = cards.get(i).getValue();
				return max;
			}
		}
		return max;
	}

	public static boolean compterpai(int typeOher, int typeMe, int maxOhther,
			int maxMe, int OtherCardsSize, int meCardsSize) {
		boolean compter = false;
		if (typeMe != typeOher) {
			if (typeOher == 13) {
				compter = false;
			}
			if (typeOher == 6 && typeMe != 13) {
				compter = false;
			}
			if (typeMe == 13) {
				compter = true;
			}
			if (typeMe == 6 && typeOher != 13) {
				compter = true;
			}
		} else {
			if (maxOhther >= maxMe) {
				compter = false;
			} else {
				compter = true;
			}
			if (meCardsSize != OtherCardsSize) {
				compter = false;
			}
		}
		return compter;

	}

	public static boolean IfWangzha(List<Poker> list) {
		if (list.size() < 2)
			return false;

		return list.get(0).getValue() + list.get(1).getValue() == 33 ? true
				: false;

	}

	public static int ifZhadan(List<Poker> list) {
		int number = -1;
		for (int i = list.size() - 1; i > 1; i--) {
			if (numberCount(list.get(i).getValue(), list) == 4) {
				number = i;
				return number;
			}
		}
		return -1;

	}

	public static int[] GettiShi(List<Poker> Othercards, List<Poker> Mycards) {
		int[] tishi = null;

		if (Othercards.size() != 1) {
			if (IfWangzha(Othercards)) {
				return null;
			}
		}

		// if(Othercards.size()>Mycards.size()){
		// return null;
		// }

		List<Poker> now = new ArrayList<Poker>();
		for (Poker card : Mycards) {
			now.add(card);
		}
		/*
		 * for(Card card :Othercards){ System.out.println(card.getValue()); }
		 */

		int cardType = checkpai(Othercards);

		if (Mycards.size() >= Othercards.size()) {
			switch (cardType) {
			case Danpai:
				int value = Othercards.get(0).getValue();
				tishi = new int[1];
				for (int i = Mycards.size() - 1; i >= 0; i--) {
					if (value < Mycards.get(i).getValue()) {
						tishi[0] = Mycards.get(i).getNumber();
						return tishi;
					}
				}

				break;
			case Yidui:
				tishi = new int[2];
				int yiduivalue = Othercards.get(0).getValue();

				for (int i = Mycards.size() - 1; i > 0; i--) {
					if (yiduivalue < Mycards.get(i).getValue()) {
						if (Mycards.get(i).getValue() == Mycards.get(i - 1)
								.getValue()) {
							tishi[0] = Mycards.get(i).getNumber();
							tishi[1] = Mycards.get(i - 1).getNumber();
							return tishi;
						}
					}
				}
				break;
			case Santiao:
				tishi = new int[3];
				int santiaovalue = Othercards.get(0).getValue();

				for (int i = Mycards.size() - 1; i > 1; i--) {
					if (santiaovalue < Mycards.get(i).getValue()) {
						if (Mycards.get(i).getValue() == Mycards.get(i - 1)
								.getValue()
								&& Mycards.get(i - 2).getValue() == Mycards
										.get(i - 1).getValue()) {
							tishi[0] = Mycards.get(i).getNumber();
							tishi[1] = Mycards.get(i - 1).getNumber();
							tishi[2] = Mycards.get(i - 2).getNumber();
							return tishi;
						}
					}
				}
				break;
			case Sandaiyi:
				tishi = new int[4];
				int Sandaiyivalue = getMaxNumber(Othercards);
				int taget = 0;
				int j = 1;
				for (int i = Mycards.size() - 1; i > 1; i--) {
					if (Sandaiyivalue < Mycards.get(i).getValue()) {
						if (Mycards.get(i).getValue() == Mycards.get(i - 1)
								.getValue()
								&& Mycards.get(i - 2).getValue() == Mycards
										.get(i - 1).getValue()) {
							tishi[0] = Mycards.get(i).getNumber();
							tishi[1] = Mycards.get(i - 1).getNumber();
							tishi[2] = Mycards.get(i - 2).getNumber();
							tishi[3] = 100;
							taget = Mycards.get(i).getValue();
							break;
						}
					}
				}
				while (tishi[3] == 100) {
					if (Mycards.get(Mycards.size() - j).getValue() != taget) {
						tishi[3] = Mycards.get(Mycards.size() - j).getNumber();
						return tishi;
					} else {
						j++;
					}
				}
				break;
			case Sandaier:
				tishi = new int[5];
				int Sandaier = getMaxNumber(Othercards);
				// int taget1 = 0;
				// int j1 = 1;
				for (int i = Mycards.size() - 1; i > 1; i--) {
					if (Sandaier < Mycards.get(i).getValue()) {
						if (Mycards.get(i).getValue() == Mycards.get(i - 1)
								.getValue()
								&& Mycards.get(i - 2).getValue() == Mycards
										.get(i - 1).getValue()) {
							tishi[0] = Mycards.get(i).getNumber();
							tishi[1] = Mycards.get(i - 1).getNumber();
							tishi[2] = Mycards.get(i - 2).getNumber();
							now.remove(Mycards.get(i));
							now.remove(Mycards.get(i - 1));
							now.remove(Mycards.get(i - 2));
							for (int x = now.size() - 1; x > 0; x--) {
								if (now.get(x).getValue() == now.get(x - 1)
										.getValue()) {
									tishi[3] = now.get(x).getNumber();
									tishi[4] = now.get(x - 1).getNumber();
									return tishi;
								}
							}

						}
					}
				}
				break;
			case zhadan:
				tishi = new int[4];
				int zhadan = Othercards.get(0).getValue();
				for (int i = Mycards.size() - 1; i > 2; i--) {

					if (zhadan < Mycards.get(i).getValue()) {
						if (numberCount(Mycards.get(i).getValue(), Mycards) == 4) {
							tishi[0] = Mycards.get(i).getNumber();
							tishi[1] = Mycards.get(i - 1).getNumber();
							tishi[2] = Mycards.get(i - 2).getNumber();
							tishi[3] = Mycards.get(i - 3).getNumber();
							return tishi;
						}
					}

				}
				// if (Mycards.get)

				break;
			case sidaiyi:
				tishi = new int[6];
				int max = getMaxNumber(Othercards);
				for (int i = Mycards.size() - 1; i > 2; i--) {

					if (max < Mycards.get(i).getValue()) {
						if (numberCount(Mycards.get(i).getValue(), Mycards) == 4) {
							tishi[0] = Mycards.get(i).getNumber();
							tishi[1] = Mycards.get(i - 1).getNumber();
							tishi[2] = Mycards.get(i - 2).getNumber();
							tishi[3] = Mycards.get(i - 3).getNumber();

							now.remove(Mycards.get(i));
							now.remove(Mycards.get(i - 1));
							now.remove(Mycards.get(i - 2));
							tishi[4] = now.get(now.size() - 1).getNumber();
							tishi[5] = now.get(now.size() - 2).getNumber();

							return tishi;
						}
					}

				}
				break;
			case sidaier:

				tishi = getSiDaiErTiShi(Othercards, now);
				if (tishi != null) {
					return tishi;
				}
				break;
			case feiji:
				tishi = getFeiJiTiShi(Othercards, now);
				if (tishi != null) {
					return tishi;
				}
				break;
			case feijidaisan:

				tishi = getFeiJiDaiSanTiShi(Othercards, now);
				if (tishi != null) {
					return tishi;
				}
				break;
			case feijidaidui:
				// tishi = new int[Othercards.size()];
				tishi = getFeiJiDaiDuiTiShi(Othercards, now);
				if (tishi != null) {
					return tishi;
				}
				break;
			case liandui:
				tishi = new int[Othercards.size()];
				int minPai = Othercards.get(Othercards.size() - 1).getValue();

				int tishiPos = 0;
				int lastPaiValue = -1;
				for (int i = Mycards.size() - 1; i > 0
						&& tishiPos < tishi.length; --i) {
					if (Mycards.get(i).getValue() <= minPai) {
						continue;
					}
					if (Mycards.get(i).getValue() == 15) {
						break;
					}
					if (Mycards.get(i - 1).getValue() != Mycards.get(i)
							.getValue()) {
						// lastPaiValue = -1;
						continue;
					}
					if (lastPaiValue == -1) {
						tishi[0] = Mycards.get(i).getNumber();
						--i;
						tishi[1] = Mycards.get(i).getNumber();
						tishiPos = 2;
						lastPaiValue = Mycards.get(i).getValue();
						continue;
					}
					if (Mycards.get(i).getValue() == lastPaiValue) {
						continue;
					}
					if (Mycards.get(i).getValue() != lastPaiValue + 1) {
						tishi[0] = Mycards.get(i).getNumber();
						--i;
						tishi[1] = Mycards.get(i).getNumber();
						tishiPos = 2;

					} else {
						tishi[tishiPos++] = Mycards.get(i).getNumber();
						--i;
						// ++tishiPos;
						tishi[tishiPos++] = Mycards.get(i).getNumber();

					}
					lastPaiValue = Mycards.get(i).getValue();
				}
				if (tishiPos == tishi.length) {
					return tishi;
				}
				break;
			case shunzi:
				tishi = new int[Othercards.size()];
				int shunzi = Othercards.get(Othercards.size() - 1).getValue();
				for (int i = Mycards.size() - 1; i > Othercards.size() - 2; i--) {
					if (shunzi < Mycards.get(i).getValue()) {
						int shunCount = 1;
						tishi[0] = Mycards.get(i).getNumber();
						for (int z = 1; z < Othercards.size(); z++) {
							for (int s = i; s >= 1; s--) {
								if (Mycards.get(i).getValue() + z == Mycards
										.get(s - 1).getValue()
										&& Mycards.get(s - 1).getValue() < 15) {
									tishi[shunCount] = Mycards.get(s - 1)
											.getNumber();
									shunCount++;
									break;
								}
							}
						}
						if (shunCount == Othercards.size()) {
							return tishi;
							/*
							 * }else{ return null;
							 */
						}
					}
				}
				break;

			}
		}
		int number = ifZhadan(Mycards);
		if (number != -1 && cardType != zhadan) {
			tishi = new int[4];
			for (int i = 0; i < 4; i++) {
				tishi[i] = Mycards.get(number - i).getNumber();
			}
			return tishi;
		} else {
			if (Othercards.size() != 1) {
				if (IfWangzha(Mycards)) {
					tishi = new int[2];
					tishi[0] = Mycards.get(0).getNumber();
					tishi[1] = Mycards.get(1).getNumber();
					;
					return tishi;
				}
			}
		}

		return null;
	}

	private static int[] getFeiJiTiShi(List<Poker> othercards,
			List<Poker> myCards) {
		if (myCards.size() < othercards.size()) {
			return null;
		}
		int maxValue = othercards.get(0).getValue();
		if (maxValue == 0) {
			return null;
		}

		int feijiCount = othercards.size() / 3;

		List<Poker> del = new ArrayList<Poker>();
		// int count4 = -1;
		for (int i = myCards.size() - 1; i >= 0; --i) {
			// if (mycards.get(i).getValue() < )
			int sameCardCount = numberCount(myCards.get(i).getValue(), myCards);

			if (sameCardCount < 3) {
				// cards.remove(i);
				del.add(myCards.get(i));
				// System.out.println("shanchu");
			} else if (sameCardCount == 4) {
				del.add(myCards.get(i));
				i = i - 3;
			}
		}

		for (int i = 0; i < del.size(); i++) {
			myCards.remove(del.get(i));

		}

		if (myCards.size() < feijiCount * 3) {
			return null;
		}

		int tiShi[] = new int[feijiCount * 3];
		int tiShiIndex = 0;
		for (int i = myCards.size() - 1; i >= 2; i = i - 3) {
			if (myCards.get(i).getValue() < maxValue) {
				continue;
			}

			if (i - 3 < 0 && tiShiIndex != 0) {
				if (tiShiIndex <= tiShi.length - 3) {
					tiShi[tiShiIndex++] = myCards.get(i).getNumber();
					tiShi[tiShiIndex++] = myCards.get(i - 1).getNumber();
					tiShi[tiShiIndex++] = myCards.get(i - 2).getNumber();
				}
				break;
			}
			if (myCards.get(i).getValue() + 1 == myCards.get(i - 3).getValue()) {
				if (tiShiIndex <= tiShi.length - 3) {
					tiShi[tiShiIndex++] = myCards.get(i).getNumber();
					tiShi[tiShiIndex++] = myCards.get(i - 1).getNumber();
					tiShi[tiShiIndex++] = myCards.get(i - 2).getNumber();
				}
				if (tiShiIndex == tiShi.length) {
					return tiShi;
				}
			} else {
				tiShiIndex = 0;
			}
		}

		if (tiShiIndex == tiShi.length) {
			return tiShi;
		}
		return null;

		// for(int i = 0;i<cards.size();i++){
		//
		// }
		// if(checkFeiji(cards)){
		// return true;
		// }else{
		// return false;
		// }
	}

	private static int[] getSiDaiErTiShi(List<Poker> othercards,
			List<Poker> myCards) {
		if (myCards.size() < othercards.size()) {
			return null;
		}
		int maxValue = getMaxNumber(othercards);
		int tishi[] = new int[othercards.size()];
		int tiShiIndex = 0;
		int i = 0;
		for (i = myCards.size() - 1; i >= 3; --i) {
			if (myCards.get(i).getValue() < maxValue) {
				continue;
			}
			int count = numberCount(myCards.get(i).getValue(), myCards);
			if (count == 4) {
				tishi[tiShiIndex++] = myCards.get(i).getNumber();
				tishi[tiShiIndex++] = myCards.get(i - 1).getNumber();
				tishi[tiShiIndex++] = myCards.get(i - 2).getNumber();
				tishi[tiShiIndex++] = myCards.get(i - 3).getNumber();
				break;
			}
		}

		if (i == 2) {
			return null;
		}

		for (int j = myCards.size() - 1; j > 0; --j) {
			if (j > i - 4 && j <= i) {
				continue;
			}
			if (myCards.get(j).getValue() == myCards.get(j - 1).getValue()) {
				tishi[tiShiIndex++] = myCards.get(j).getNumber();
				tishi[tiShiIndex++] = myCards.get(j - 1).getNumber();
				--j;
			}
			if (tiShiIndex == tishi.length) {
				return tishi;
			}
		}

		return null;
	}

	private static int[] getFeiJiDaiDuiTiShi(List<Poker> othercards,
			List<Poker> myCards) {

		if (myCards.size() < othercards.size()) {
			return null;
		}

		int feiJiCount = othercards.size() / 5;

		int maxValue = DoudizhuRule.checkFeijiDaidui(othercards);
		if (maxValue == 0) {
			return null;
		}

		List<Poker> del = new ArrayList<Poker>();

		for (int i = 0; i < myCards.size(); i++) {
			int count = numberCount(myCards.get(i).getValue(), myCards);
			if (count == 1) {
				del.add(myCards.get(i));
			} else if (count == 2) {
				del.add(myCards.get(i));
				del.add(myCards.get(i + 1));
				++i;
			} else if (count == 4) {
				del.add(myCards.get(i));
				i = i + 3;
			}
		}

		for (int i = 0; i < del.size(); i++) {

			myCards.remove(del.get(i));
		}

		if (myCards.size() < feiJiCount * 3) {
			return null;
		}

		Poker tiShiPoker[] = new Poker[feiJiCount * 3];
		int tiShiIndex = 0;
		int i;
		for (i = myCards.size() - 1; i >= 2; i = i - 3) {
			if (myCards.get(i).getValue() < maxValue) {
				continue;
			}

			if (i - 3 < 0 && tiShiIndex != 0) {
				if (tiShiIndex <= tiShiPoker.length - 3) {
					tiShiPoker[tiShiIndex++] = myCards.get(i);
					tiShiPoker[tiShiIndex++] = myCards.get(i - 1);
					tiShiPoker[tiShiIndex++] = myCards.get(i - 2);
				}
				break;
			}
			if (myCards.get(i).getValue() + 1 == myCards.get(i - 3).getValue()) {
				if (tiShiIndex <= tiShiPoker.length - 3) {
					tiShiPoker[tiShiIndex++] = myCards.get(i);
					tiShiPoker[tiShiIndex++] = myCards.get(i - 1);
					tiShiPoker[tiShiIndex++] = myCards.get(i - 2);
				}
				if (tiShiIndex == tiShiPoker.length) {
					break;
				}
			} else {
				for (int j = 0; j < tiShiIndex; ++j) {
					del.add(tiShiPoker[j]);
				}
				tiShiIndex = 0;
			}
		}
		if (tiShiIndex != tiShiPoker.length) {
			return null;
		}

		for (i = i - 3; i >= 0; --i) {
			del.add(myCards.get(i));
		}
		for (int j = 0; j < del.size(); ++j) {
			myCards.remove(del.get(j));
		}

		int duiCount = 0;
		boolean hasCheck[] = new boolean[del.size()];

		for (i = 0; i < del.size(); ++i) {
			if (hasCheck[i]) {
				continue;
			}
			for (int j = i + 1; j < del.size(); ++j) {
				if (hasCheck[j]) {
					continue;
				}
				if (del.get(i).getValue() == del.get(j).getValue()) {
					if (++duiCount >= feiJiCount) {

						int tiShi[] = new int[myCards.size()];
						for (int k = 0; k < myCards.size(); ++k) {
							tiShi[k] = myCards.get(k).getNumber();
						}
						return tiShi;
					} else {
						hasCheck[j] = true;
					}
					break;
				}
			}

		}

		return null;

	}

	private static int[] getFeiJiDaiSanTiShi(List<Poker> othercards,
			List<Poker> mycards) {
		if (mycards.size() < othercards.size()) {
			return null;
		}
		int maxValue = DoudizhuRule.checkFeijiDaisan(othercards);
		if (maxValue == 0) {
			return null;
		}

		int feijiCount = othercards.size() / 4;

		List<Poker> del = new ArrayList<Poker>();
		// int count4 = -1;
		for (int i = mycards.size() - 1; i >= 0; --i) {
			// if (mycards.get(i).getValue() < )
			int sameCardCount = numberCount(mycards.get(i).getValue(), mycards);
			if (sameCardCount < 3) {
				// cards.remove(i);
				del.add(mycards.get(i));
				// System.out.println("shanchu");
			} else if (sameCardCount == 4) {
				del.add(mycards.get(i));
				i = i - 3;
			}
		}

		for (int i = 0; i < del.size(); i++) {
			mycards.remove(del.get(i));

		}

		if (mycards.size() < feijiCount * 3) {
			return null;
		}

		int tiShi[] = new int[feijiCount * 3];
		int tiShiIndex = 0;
		for (int i = mycards.size() - 1; i >= 2; i = i - 3) {
			if (mycards.get(i).getValue() < maxValue) {
				continue;
			}
			if (i - 3 < 0) {
				if (tiShiIndex == 0) {
					break;
				}
				if (tiShiIndex <= tiShi.length - 3) {
					tiShi[tiShiIndex++] = mycards.get(i).getNumber();
					tiShi[tiShiIndex++] = mycards.get(i - 1).getNumber();
					tiShi[tiShiIndex++] = mycards.get(i - 2).getNumber();
				}
				break;
			}
			if (mycards.get(i).getValue() + 1 == mycards.get(i - 3).getValue()) {
				if (tiShiIndex <= tiShi.length - 3) {
					tiShi[tiShiIndex++] = mycards.get(i).getNumber();
					tiShi[tiShiIndex++] = mycards.get(i - 1).getNumber();
					tiShi[tiShiIndex++] = mycards.get(i - 2).getNumber();
				}
				if (tiShiIndex == tiShi.length) {
					return tiShi;
				}
			} else {
				tiShiIndex = 0;
			}
		}

		if (tiShiIndex == tiShi.length) {
			return tiShi;
		}
		return null;

		// for(int i = 0;i<cards.size();i++){
		//
		// }
		// if(checkFeiji(cards)){
		// return true;
		// }else{
		// return false;
		// }
	}

	public static int checkpai(List<Poker> cards) {
		int cardStyle = 0;
		if (cards.size() == 0) {
			cardStyle = error; // error
			return error;
		}
		int pokerCount = getPokeCount(cards);
		switch (cards.size()) {
		case 1:
			cardStyle = Danpai;
			break;
		case 2:
			if (cards.get(0).getValue() == cards.get(1).getValue()) {
				cardStyle = Yidui;
			} else if (cards.get(0).getValue() + cards.get(1).getValue() == 33) {
				cardStyle = wangzha;
			}
			break;
		case 3:
			if (pokerCount == 3) {
				cardStyle = Santiao;
			}
			break;
		case 4:
			if (pokerCount == 4) {
				cardStyle = zhadan;
			} else if (pokerCount == 3) {
				cardStyle = Sandaiyi;
			}
			break;
		case 5:
			if (pokerCount == 3) {
				if (cards.get(3).getValue() == cards.get(4).getValue()
						&& cards.get(0).getValue() == cards.get(1).getValue()) {
					cardStyle = Sandaier;
				}
			} else if (checkShunpai(cards)) {
				cardStyle = shunzi;
			}
			break;
		case 6:
			if (pokerCount == 4) {
				cardStyle = sidaiyi;
				return sidaiyi;
			} else if (checkShunpai(cards)) {
				cardStyle = shunzi;
				return shunzi;
			} else if (checkLiandui(cards)) {
				cardStyle = liandui;
				return liandui;
			} else if (pokerCount == 3) {
				if (checkFeiji(cards)) {
					cardStyle = feiji;
				}
			}
			break;
		}

		if (cards.size() >= 7) {
			if (pokerCount == 4 && cards.size() == 8
					&& cards.get(0).getValue() == cards.get(1).getValue()
					&& cards.get(2).getValue() == cards.get(3).getValue()
					&& cards.get(4).getValue() == cards.get(5).getValue()
					&& cards.get(6).getValue() == cards.get(7).getValue()) {
				// System.out.println("sidai2");
				return sidaier;
			}
			if (checkShunpai(cards)) {
				return shunzi;
			}
			if (checkLiandui(cards)) {
				return liandui;
			}
			if (checkFeiji(cards)) {
				return feiji;
			}
			if (cards.size() % 4 == 0) {
				if (0 != checkFeijiDaisan(cards)) {
					return feijidaisan;
				}
			}
			if (cards.size() % 5 == 0) {
				if (0 != checkFeijiDaidui(cards)) {
					return feijidaidui;
				}
			}
		}
		return cardStyle;

	}

	public static boolean checkShunpai(List<Poker> cards) {
		int first = cards.get(0).getValue();
		if (first == 15 || first == 16 || first == 17) {
			return false;
		}
		for (int i = 0; i < cards.size() - 1; i++) {
			if (cards.get(i + 1).getValue() + 1 == first) {
				first = cards.get(i + 1).getValue();
			} else {
				return false;
			}
		}
		return true;
	}

	public static boolean checkLiandui(List<Poker> cards) {
		int count = 1;
		if (cards.size() >= 6 && cards.size() % 2 == 0) {
			for (int i = 0; i < cards.size() - 1; i += 2) {
				if (cards.get(i).getValue() == 16
						|| cards.get(i).getValue() == 17
						|| cards.get(i).getValue() == 15) {
					return false;
				}

				if (cards.get(i).getValue() == cards.get(i + 1).getValue())
					count++;
				if (i + 2 < cards.size() - 1) {
					if (cards.get(i + 2).getValue() + 1 != cards.get(i)
							.getValue()) {
						return false;
					}
				}
			}
		}
		// System.out.println(count);
		if (count >= 3) {
			return true;
		} else {
			return false;
		}
	}

	public static int numberCount(int number, List<Poker> cards) {
		int count = 0;
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getValue() == number) {
				// System.out.println("value"+cards.get(i).getValue()+"number"+number);
				count++;
			}
		}
		return count;
	}

	public static int checkFeijiDaisan(List<Poker> myCards) {

		if (myCards.size() % 4 != 0) {
			return 0;
		}

		List<Poker> cards = new ArrayList<Poker>();
		for (int i = 0; i < myCards.size(); ++i) {
			cards.add(myCards.get(i));
		}

		int feijiCount = cards.size() / 4;

		List<Poker> del = new ArrayList<Poker>();
		// int count4 = -1;
		for (int i = 0; i < cards.size(); i++) {
			if (numberCount(cards.get(i).getValue(), cards) < 3) {
				// cards.remove(i);
				del.add(cards.get(i));
				// System.out.println("shanchu");
			} else if (numberCount(cards.get(i).getValue(), cards) == 4) {
				del.add(cards.get(i));
				i = i + 3;
			}
		}

		// if(del.size()!=cards.size()/4){
		// System.out.println("del.size()"+del.size()+"cards.size()/4"+cards.size()/4);
		// return false;
		// }

		for (int i = 0; i < del.size(); i++) {
			// System.out.println(del.get(i));

			cards.remove(del.get(i));

		}

		if (cards.size() % 3 != 0 || cards.size() == 0) {
			return 0;
		}

		int lianCount = 1;
		int maxLianCount = 1;
		for (int i = 0; i < cards.size() - 3; i = i + 3) {
			if (cards.get(i).getValue() - 1 == cards.get(i + 3).getValue()) {
				++lianCount;
			} else {
				if (lianCount > maxLianCount) {
					maxLianCount = lianCount;
				}
				lianCount = 1;
			}
		}
		if (lianCount > maxLianCount) {
			maxLianCount = lianCount;
		}
		if (maxLianCount == feijiCount) {
			return cards.get(0).getValue();
		}
		return 0;

		// for(int i = 0;i<cards.size();i++){
		//
		// }
		// if(checkFeiji(cards)){
		// return true;
		// }else{
		// return false;
		// }

	}

	public static int checkFeijiDaidui(List<Poker> myCards) {
		if (myCards.size() % 5 != 0 || myCards.size() / 5 < 2) {
			return 0;
		}
		List<Poker> cards = new ArrayList<Poker>();
		for (int i = 0; i < myCards.size(); ++i) {
			cards.add(myCards.get(i));
		}

		List<Poker> del = new ArrayList<Poker>();

		for (int i = 0; i < cards.size(); i++) {
			int count = numberCount(cards.get(i).getValue(), cards);
			if (count == 1) {
				return 0;
				// wangCount = wangCount +cards.get(i).getValue();
			} else if (count == 2) {
				del.add(myCards.get(i));
				del.add(myCards.get(i + 1));
				++i;
			} else if (count == 4) {
				del.add(myCards.get(i));
				del.add(myCards.get(i + 1));
				del.add(myCards.get(i + 2));
				del.add(myCards.get(i + 3));
				i = i + 3;
			}
		}

		for (int i = 0; i < del.size(); i++) {
			// System.out.println(del.get(i));

			cards.remove(del.get(i));
		}

		if (cards.size() % 3 != 0 || cards.size() / 3 < 2
				|| cards.size() / 3 != del.size() / 2) {
			return 0;
		}

		for (int i = 0; i < cards.size() - 3; i = i + 3) {
			if (cards.get(i).getValue() - 1 != cards.get(i + 3).getValue()) {
				return 0;
			}
		}

		return cards.get(0).getValue();

	}

	public static boolean checkFeiji(List<Poker> cards) {

		int count = 1;
		int size = cards.size();
		if (size % 3 == 0) {
			for (int i = 0; i < cards.size() - 3; i += 3) {
				if (cards.get(i).getValue() == 16
						|| cards.get(i).getValue() == 17
						|| cards.get(i).getValue() == 15) {
					return false;
				} else {
					if (cards.get(i).getValue() - 1 == cards.get(i + 3)
							.getValue()
							&& cards.get(i).getValue() == cards.get(i + 1)
									.getValue()
							&& cards.get(i).getValue() == cards.get(i + 2)
									.getValue()) {
						count++;
					} else {
						return false;
					}
				}
			}
			if (count >= 2) {
				return true;
			}

		}
		return false;

	}

	public static boolean checkSidai2dui(List<Poker> cards) {

		if (getPokeCount(cards) == 4
				&& cards.get(4).getValue() == cards.get(5).getValue()
				&& cards.get(6).getValue() == cards.get(7).getValue()) {
			return true;
		}
		return false;
	}

	public static int getPokeCount(List<Poker> cards) {
		int count = 1;
		int maxcount = 1;
		int nowvalue = cards.get(0).getValue();
		for (int i = 0; i < cards.size() - 1; i++) {
			if (nowvalue == cards.get(i + 1).getValue()) {
				count++;
			} else {
				nowvalue = cards.get(i + 1).getValue();
				if (maxcount < count)
					maxcount = count;
				count = 1;
			}
		}
		if (maxcount > count) {
			return maxcount;
		} else {
			return count;
		}
	}

	public static int[] sort(int[] cards, Poker[] poker) {
		if (cards == null)
			return null;
		int zhi[] = new int[cards.length];
		Number[] num = new Number[cards.length];
		for (int i = 0; i < cards.length; i++) {
			num[i] = new Number();
			num[i].setValue(poker[cards[i]].getValue() * 100
					+ poker[cards[i]].getStyle());
			num[i].setPokerNumber(cards[i]);
			zhi[i] = poker[cards[i]].getValue();
			// System.out.println();
		}

		for (int i = 0; i < num.length; i++) {
			for (int j = i + 1; j < num.length; j++) {
				if (num[i].getValue() < num[j].getValue()) {
					Number a = num[i];
					num[i] = num[j];
					num[j] = a;
				}
			}
		}
		for (int i = 0; i < num.length; i++) {
			// System.out.println(num[i].getValue());
			zhi[i] = num[i].getPokerNumber();
		}
		return zhi;
	}
}
