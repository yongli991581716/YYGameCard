package com.lordcard.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.util.Log;

import com.lordcard.entity.Number;
import com.lordcard.entity.Poker;

/**
 * 斗地主规则
 * 
 */
public class DoudizhuRule {

	public static final int Danpai = 1;
	public static final int Yidui = 2;
	public static final int Santiao = 3;
	public static final int Sandaiyi = 4;
	public static final int Sandaier = 5;
	public static final int zhadan = 6;
	public static final int sidaiyi = 7;
	public static final int sidaier = 8;
	public static final int shunzi = 9;
	public static final int feiji = 10;
	public static final int feijidaisan = 11;
	public static final int feijidaidui = 12;
	public static final int wangzha = 13;
	public static final int liandui = 14;
	public static final int siZhang = 15;
	public static final int error = 0;

	/**
	 * 寻找一张牌中出现次数最多，牌最大的数字
	 * 
	 * @param cards
	 * @return
	 */
	public static int getMaxNumber(List<Poker> cards) {
		int max = 0;
		int count = getPokeCount(cards);
		// //System.out.println("c出现最多是"+count+"次");
		for (int i = 0; i < cards.size(); i++) {
			if (count == numberCount(cards.get(i).getValue(), cards)) {
				max = cards.get(i).getValue();
				// //System.out.println("最大的牌位"+max);
				return max;
			}
		}
		return max;
	}

	/**
	 * 检测牌的大小，如果大能出就true
	 * 
	 * @param typeOher
	 *            别人牌型
	 * @param typeMe
	 *            自己牌型
	 * @param maxOhther
	 *            別人最大的牌
	 * @param maxMe
	 *            自己最大的牌
	 * @param OtherCardsSize
	 *            別人出牌的長度
	 * @param meCardsSize
	 *            自己出牌的長度
	 * @return
	 */
	public static boolean compterpai(int typeOher, int typeMe, int maxOhther, int maxMe, int OtherCardsSize, int meCardsSize) {
		boolean compter = false;
		// 如果两个人出的牌类型不一样
		if (typeMe != typeOher) {
			// System.out.println("牌型不一样");
			if (typeOher == 13) {// 如果别人出王炸则一定自己打不过
				compter = false;
			}
			if (typeOher == 6 && typeMe != 13) {// 如果别人出炸而自己不是王炸一定打不过
				compter = false;
			}
			if (typeMe == 13) {// 自己如果是王炸则自己一定牌比较大
				compter = true;
			}
			if (typeMe == 6 && typeOher != 13) {// 如果自己出炸而别人不是王炸一定打得过
				compter = true;
			}
		} else {// 两个人出牌的类型一样
				// System.out.println("牌型一样");
			if (maxOhther >= maxMe) {
				compter = false;
			} else {
				compter = true;
			}
			if (meCardsSize != OtherCardsSize) {// 如果是顺子需要检测牌的长度，如果两个人的牌长度不一样，那自己打不过
				compter = false;
			}
		}
		return compter;

	}

	/**
	 * 检测自己的牌是否有火箭 由于牌的顺序已经排好，所以只要第一和第二张牌是大鬼和小鬼就可以了
	 * 
	 * @param list
	 * @return
	 */
	public static boolean IfWangzha(List<Poker> list) {
		if (list.size() < 2)
			return false;

		return list.get(0).getValue() + list.get(1).getValue() == 33 ? true : false;

	}

	/**
	 * 檢測自己的牌是否有炸彈
	 * 
	 * @param list
	 * @return
	 */
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

	/**
	 * 提示功能
	 * 
	 * @param cards
	 * @return
	 */
	public static int[] GettiShi(List<Poker> Othercards, List<Poker> Mycards) {
		int[] tishi = null;

		// 如果别人出王炸直接返回
		if (Othercards.size() != 1) {
			if (IfWangzha(Othercards)) {
				return null;
			}
		}

		// 自己剩下的牌少于对方出的牌
		// if(Othercards.size()>Mycards.size()){
		// return null;
		// }

		List<Poker> now = new ArrayList<Poker>();
		for (Poker card : Mycards) {
			now.add(card);
		}
		/*
		 * for(Card card :Othercards){ //System.out.println(card.getValue()); }
		 */
		// 首先检测别人是什么类型的牌
		int cardType = checkpai(Othercards);

		if (Mycards.size() >= Othercards.size()) {
			switch (cardType) {
			case Danpai: // 如果是单牌的话
				int value = Othercards.get(0).getValue();
				tishi = new int[1];
				for (int i = Mycards.size() - 1; i >= 0; i--) {
					// //System.out.println("最后一张的值为"+Mycards.get(i).getValue());
					if (value < Mycards.get(i).getValue()) {
						tishi[0] = Mycards.get(i).getNumber();
						return tishi;
					}
				}

				break;
			case Yidui:// 如果是一对的话
				tishi = new int[2];
				int yiduivalue = Othercards.get(0).getValue();

				for (int i = Mycards.size() - 1; i > 0; i--) {
					// //System.out.println("最后一张的值为"+Mycards.get(i).getValue());
					if (yiduivalue < Mycards.get(i).getValue()) {
						if (Mycards.get(i).getValue() == Mycards.get(i - 1).getValue()) {
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
					// //System.out.println("最后一张的值为"+Mycards.get(i).getValue());
					if (santiaovalue < Mycards.get(i).getValue()) {
						if (Mycards.get(i).getValue() == Mycards.get(i - 1).getValue()
								&& Mycards.get(i - 2).getValue() == Mycards.get(i - 1).getValue()) {
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
				// System.out.println("三带一最大牌是 "+Sandaiyivalue);
				int taget = 0;
				int j = 1;
				for (int i = Mycards.size() - 1; i > 1; i--) {
					// //System.out.println("最后一张的值为"+Mycards.get(i).getValue());
					if (Sandaiyivalue < Mycards.get(i).getValue()) {
						if (Mycards.get(i).getValue() == Mycards.get(i - 1).getValue()
								&& Mycards.get(i - 2).getValue() == Mycards.get(i - 1).getValue()) {
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
				// System.out.println("三带2最大牌是 "+Sandaier);
				// int taget1 = 0;
				// int j1 = 1;
				for (int i = Mycards.size() - 1; i > 1; i--) {
					// //System.out.println("最后一张的值为"+Mycards.get(i).getValue());
					if (Sandaier < Mycards.get(i).getValue()) {
						if (Mycards.get(i).getValue() == Mycards.get(i - 1).getValue()
								&& Mycards.get(i - 2).getValue() == Mycards.get(i - 1).getValue()) {
							tishi[0] = Mycards.get(i).getNumber();
							tishi[1] = Mycards.get(i - 1).getNumber();
							tishi[2] = Mycards.get(i - 2).getNumber();
							now.remove(Mycards.get(i));
							now.remove(Mycards.get(i - 1));
							now.remove(Mycards.get(i - 2));
							for (int x = now.size() - 1; x > 0; x--) {
								if (now.get(x).getValue() == now.get(x - 1).getValue()) {
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
					// //System.out.println("最后一张的值为"+Mycards.get(i).getValue());

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
					// //System.out.println("最后一张的值为"+Mycards.get(i).getValue());

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
				// hjr 重写
				for (int i = Mycards.size() - 1; i > 0 && tishiPos < tishi.length; --i) {
					// 牌的数小于等于我对手最小的牌
					if (Mycards.get(i).getValue() <= minPai) {
						continue;
					}
					// 碰到2的时候，直接退出
					if (Mycards.get(i).getValue() == 15) {
						break;
					}
					// 只有和下一张牌构成一对时，才向下走，
					if (Mycards.get(i - 1).getValue() != Mycards.get(i).getValue()) {
						// lastPaiValue = -1;
						continue;
					}
					// 找到第一个符合条件的对
					if (lastPaiValue == -1) {
						tishi[0] = Mycards.get(i).getNumber();
						--i;
						tishi[1] = Mycards.get(i).getNumber();
						tishiPos = 2;
						lastPaiValue = Mycards.get(i).getValue();
						continue;
					}
					// 存在3张或者4张，跳过第三张和第四张
					if (Mycards.get(i).getValue() == lastPaiValue) {
						continue;
					}
					// 当前牌如果成对，但不和上一对是连着的，则清除已有的标注，重新将其做为连对的最小一对
					if (Mycards.get(i).getValue() != lastPaiValue + 1) {
						tishi[0] = Mycards.get(i).getNumber();
						--i;
						tishi[1] = Mycards.get(i).getNumber();
						tishiPos = 2;

					} else {
						// 找到符合条件的下一对，存入tishi队列
						tishi[tishiPos++] = Mycards.get(i).getNumber();
						--i;
						// ++tishiPos;
						tishi[tishiPos++] = Mycards.get(i).getNumber();

					}
					lastPaiValue = Mycards.get(i).getValue();
				}
				// 如果tishi队列是满的，则找到需要的提示牌
				if (tishiPos == tishi.length) {
					return tishi;
				}
				break;
			case shunzi:
				tishi = new int[Othercards.size()];
				int shunzi = Othercards.get(Othercards.size() - 1).getValue();
				// //System.out.println("别人顺子最小牌为"+shunzi);
				for (int i = Mycards.size() - 1; i > Othercards.size() - 2; i--) {
					if (shunzi < Mycards.get(i).getValue()) {
						int shunCount = 1;
						tishi[0] = Mycards.get(i).getNumber();
						for (int z = 1; z < Othercards.size(); z++) {
							for (int s = i; s >= 1; s--) {
								if (Mycards.get(i).getValue() + z == Mycards.get(s - 1).getValue() && Mycards.get(s - 1).getValue() < 15) {
									tishi[shunCount] = Mycards.get(s - 1).getNumber();
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
		// 如果打不过同类型的则检测自己是否有炸弹
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
					// System.out.println("你有王炸");
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

	/**
	 * 检查自己的牌中是否有飞机
	 * 
	 * @param othercards
	 *            对手牌
	 * @param myCards
	 *            自己现在的牌
	 * @return 提示数组：item中的牌的number
	 */
	private static int[] getFeiJiTiShi(List<Poker> othercards, List<Poker> myCards) {
		if (myCards.size() < othercards.size()) {
			return null;
		}
		// 查出对手最大的一张牌
		int maxValue = othercards.get(0).getValue();
		if (maxValue == 0) {
			return null;
		}

		// 飞机总数
		int feijiCount = othercards.size() / 3;

		List<Poker> del = new ArrayList<Poker>();
		// int count4 = -1;
		for (int i = myCards.size() - 1; i >= 0; --i) {
			// if (mycards.get(i).getValue() < )
			int sameCardCount = numberCount(myCards.get(i).getValue(), myCards);
			// 如果只出現一次
			if (sameCardCount < 3) {
				// cards.remove(i);
				del.add(myCards.get(i));
				// //System.out.println("shanchu");
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
			// 最后一个循环，而且tiShiIndex不等于0，说明接下来的三张牌也是飞机的一部分
			// if (i - 3 < 0 && tiShiIndex != 0){
			// if (tiShiIndex <= tiShi.length - 3){
			// tiShi[tiShiIndex++] = myCards.get(i).getNumber();
			// tiShi[tiShiIndex++] = myCards.get(i-1).getNumber();
			// tiShi[tiShiIndex++] = myCards.get(i-2).getNumber();
			// }
			// break;
			// }
			// 最后一个循环，
			if (i - 3 < 0) {
				if (tiShiIndex == 0) {
					// 最后一个循环，但是还没有其他三个，没有飞机，退出
					break;
				}
				// 而且tiShiIndex不等于0，说明接下来的三张牌也是飞机的一部分
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

	/**
	 * 检查自己的牌中是否有四带二的牌
	 * 
	 * @param othercards
	 *            对手牌
	 * @param myCards
	 *            自己现在的牌
	 * @return 提示数组：item中的牌的number
	 */
	private static int[] getSiDaiErTiShi(List<Poker> othercards, List<Poker> myCards) {
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

		// 寻找两对
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

	/**
	 * 检查飞机带对
	 * 
	 * @param othercards
	 *            对手牌
	 * @param mycards
	 *            我的牌
	 * @return 提示的牌的数组
	 */
	private static int[] getFeiJiDaiDuiTiShi(List<Poker> othercards, List<Poker> myCards) {

		if (myCards.size() < othercards.size()) {
			return null;
		}

		int feiJiCount = othercards.size() / 5;

		// 查出对手最大的一张牌
		int maxValue = DoudizhuRule.checkFeijiDaidui(othercards);
		if (maxValue == 0) {
			return null;
		}

		List<Poker> del = new ArrayList<Poker>();
		// int duiCount = 0; //对子的数量

		for (int i = 0; i < myCards.size(); i++) {
			// 如果只出現一次
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

		// 只留下三张
		for (int i = 0; i < del.size(); i++) {

			myCards.remove(del.get(i));
		}

		if (myCards.size() < feiJiCount * 3) {
			return null;
		}

		// 查出所有的数据，并将其
		Poker tiShiPoker[] = new Poker[feiJiCount * 3];// 暂时缓存某个牌在队列中的位置
		int tiShiIndex = 0;
		int i;
		for (i = myCards.size() - 1; i >= 2; i = i - 3) {
			if (myCards.get(i).getValue() < maxValue) {
				continue;
			}
			// 最后一个循环，而且tiShiIndex不等于0，说明接下来的三张牌也是飞机的一部分
			// if (i - 3 < 0 && tiShiIndex != 0){
			// if (tiShiIndex <= tiShiPoker.length - 3){
			// tiShiPoker[tiShiIndex++] = myCards.get(i);
			// tiShiPoker[tiShiIndex++] = myCards.get(i - 1);
			// tiShiPoker[tiShiIndex++] = myCards.get(i - 2);
			// }
			// break;
			// }
			// 最后一个循环，
			if (i - 3 < 0) {
				if (tiShiIndex == 0) {
					// 最后一个循环，但是还没有其他三个，没有飞机，退出
					break;
				}
				// 而且tiShiIndex不等于0，说明接下来的三张牌也是飞机的一部分
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
				// 将不需要的牌添加到del数组中
				for (int j = 0; j < tiShiIndex; ++j) {
					del.add(tiShiPoker[j]);
				}
				tiShiIndex = 0;
			}
		}
		if (tiShiIndex != tiShiPoker.length) {
			return null;
		}
		// 将其他牌和在一起
		for (i = i - 3; i >= 0; --i) {
			del.add(myCards.get(i));
		}
		for (int j = 0; j < del.size(); ++j) {
			myCards.remove(del.get(j));
		}
		// 判断是否有足够的对子
		int duiCount = 0;
		boolean hasCheck[] = new boolean[del.size()];
		// 计算对的数量
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
						// 返回相应的提示数组
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

	/**
	 * 检查出飞机带散牌的提示
	 * 
	 * @param othercards
	 *            对手牌
	 * @param mycards
	 *            我的牌
	 * @return 提示数组
	 */
	private static int[] getFeiJiDaiSanTiShi(List<Poker> othercards, List<Poker> mycards) {
		if (mycards.size() < othercards.size()) {
			return null;
		}
		// 查出对手最大的一张牌
		int maxValue = DoudizhuRule.checkFeijiDaisan(othercards);
		if (maxValue == 0) {
			return null;
		}

		// 飞机总数
		int feijiCount = othercards.size() / 4;

		List<Poker> del = new ArrayList<Poker>();
		// int count4 = -1;
		for (int i = mycards.size() - 1; i >= 0; --i) {
			// if (mycards.get(i).getValue() < )
			int sameCardCount = numberCount(mycards.get(i).getValue(), mycards);
			// 如果只出現一次
			if (sameCardCount < 3) {
				// cards.remove(i);
				del.add(mycards.get(i));
				// //System.out.println("shanchu");
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
			// 最后一个循环，
			if (i - 3 < 0) {
				if (tiShiIndex == 0) {
					// 最后一个循环，但是还没有其他三个，没有飞机，退出
					break;
				}
				// 而且tiShiIndex不等于0，说明接下来的三张牌也是飞机的一部分
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
   /**
    * 检测牌型相关的牌面值
    * @param type
    * @param cards
    * @return
    */
   public static int getValueForpoker(List<Poker> cards,int count)
   {
	   int value = -1;
	   int[][]tmV = new int[cards.size()][2];
	   for(int i=0;i<cards.size();i++)
	   {
		   Poker p = cards.get(i);
		   boolean isExists = false;
		   for(int j =0;j<tmV.length;j++)
		   {
			   if(tmV[j][0] == p.getValue())
			   {
				   isExists = true;
				   tmV[j][1]++;
				   if(tmV[j][1] == count)
				   {
					   value = p.getValue();
					   break;
				   }
			   }
		   }
		   if(!isExists)
		   {
			   tmV[i][0] = p.getValue();
		   }
		   if(value != -1)
		   {
			   break;
		   }
	   }
	   return value;
   }
   public static int checkpaiValue(int cardStyle,List<Poker> cards)
   {
	   int value = -1;
	   switch (cardStyle) {
	    case Danpai:
	    case Yidui:
	    case Santiao:
	    	value = cards.get(0).getValue();
			break;
		}
	   return value;
   }
	/**
	 * 检测是什么牌
	 * 
	 * @param cards
	 * @return
	 */
	public static int checkpai(List<Poker> cards) {
		int cardStyle = 0;
		if (cards.size() == 0) {// 如果0张牌
			cardStyle = error; // error
		}
		int pokerCount = getPokeCount(cards);
		switch (cards.size()) {
		case 1: // 如果是一张牌
			cardStyle = Danpai; // 单牌
			break;
		case 2:// 如果是两张牌
			if (cards.get(0).getValue() == cards.get(1).getValue()) {
				cardStyle = Yidui;// 一对
			} else if (cards.get(0).getValue() + cards.get(1).getValue() == 33) {
				cardStyle = wangzha;// 王炸
			}
			break;
		case 3:// 如果是三张牌
			if (pokerCount == 3) {
				cardStyle = Santiao;
				// System.out.println("三张");
			}
			break;
		case 4:// 如果是四张牌
			if (pokerCount == 4) {
				cardStyle = zhadan; // 炸弹
			} else if (pokerCount == 3) {// 三带一
				cardStyle = Sandaiyi;
			}
			break;
		case 5:// 如果是五张牌
			if (pokerCount == 3) {// 三待二
				if (cards.get(3).getValue() == cards.get(4).getValue() && cards.get(0).getValue() == cards.get(1).getValue()) {
					cardStyle = Sandaier;
				}
			} else if (checkShunpai(cards)) {// 是顺牌
				cardStyle = shunzi;
			}
			break;
		case 6:// 如果是6张
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
			// 检测4帶2對
			if (pokerCount == 4 && cards.size() == 8 && cards.get(0).getValue() == cards.get(1).getValue()
					&& cards.get(2).getValue() == cards.get(3).getValue() && cards.get(4).getValue() == cards.get(5).getValue()
					&& cards.get(6).getValue() == cards.get(7).getValue()) {
				return sidaier;
			}
			// 检测顺牌
			if (checkShunpai(cards)) {
				return shunzi;
			}
			// 检测连对
			if (checkLiandui(cards)) {
				return liandui;
			}
			// 检测连飞机
			if (checkFeiji(cards)) {
				return feiji;
			}
			if (cards.size() % 4 == 0) {
				if (0 != checkFeijiDaisan(cards)) {
					// System.out.println("飞机带散牌");
					return feijidaisan;
				}
			}
			if (cards.size() % 5 == 0) {
				if (0 != checkFeijiDaidui(cards)) {
					// System.out.println("飞机带队");
					return feijidaidui;
				}
			}
		}
		return cardStyle;

	}

	/**
	 * 统计是不是顺牌，顺牌不包括2
	 * 
	 * @param cards
	 * @return
	 */
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

	/**
	 * 检查去除重复牌后的手牌，并找出最大的顺子
	 * 
	 * @Title: check
	 * @Description: TODO
	 * @param @param set
	 * @param @return
	 * @return String[]
	 * @throws
	 */

	public static String[] checkShunZi(TreeSet<Integer> set) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuilder temp = new StringBuilder();
		int count = 0;
		Integer[] nums = new Integer[1];
		nums = set.toArray(nums);
		int begin = 0;
		// ================选出所有的顺子===================
		for (Integer i : set) {
			if (count == 0) {
				begin = i;
				count++;
				temp.append("" + begin);
			} else if (i != 15 && i != 16 && i != 17 && (i == begin + count)) {
				temp.append("," + i);
				count++;
			} else {
				if (count > 4) {
					result.add(temp.toString().split(","));
				}
				begin = i;
				temp.replace(0, temp.length(), i + "");
				count = 1;
			}
		}

		if (count > 4)
			result.add(temp.toString().split(","));
		// ====================================================

		// ---------------选出最大的顺子--------------------
		String[] max;
		if (result.size() == 0) {
			max = null;
		} else {
			max = result.get(0);
			System.out.println("共有" + result.size() + "个顺子:");
			for (int i = 1; i < result.size(); i++) {
				if (max.length < result.get(i).length) {
					max = result.get(i);
				}
			}
		}
		return max;
	}

	/**
	 * 滑动选牌时，5对及5对以上的连对检测
	 * @param pokers
	 * @return
	 */
	public static List<Integer> checkLianDui2(List<Poker> pokers) {
		List<Integer> card = new ArrayList<Integer>();
		List<Integer> cardId1 = new ArrayList<Integer>();
		List<Integer> card2 = new ArrayList<Integer>();
		int[] cardss = new int[pokers.size()];
		for (int i = 0; i < pokers.size(); i++) {
			//			cards.add(pokers.get(i).getValue());
			cardss[i] = pokers.get(i).getValue();
			System.out.println("扑克：" + pokers.get(i).getValue());
		}
		int[] cards = Sorts(cardss);
		Log.i("checkLianDui2","==========原始牌==============");
		for (int i = 0; i < cards.length; i++) {
			Log.i("checkLianDui2","" + cards[i]);
		}
		Log.i("checkLianDui2","===============================");
		if (cards.length > 0) {
			int current = cards[0];
			cardId1.add(cards[0]);
			for (int i = 1; i < cards.length; i++) {//找连续的牌
				int cardsValue = cards[i];
				if (cardsValue != 15 && cardsValue != 16 && cardsValue != 17 && cardsValue - current == 1) {
					current = cardsValue;
					cardId1.add(cardsValue);
				} else if (cardsValue - current > 1) {
					current = cardsValue;
					if (cardId1.size() > 4) {//如果有4个以上的连牌，就退出循环
						break;
					} else {
						cardId1.clear();
						cardId1.add(cardsValue);
					}
				}
			}
			Log.i("checkLianDui2","==========连续的牌==============");
			for (int i = 0; i < cardId1.size(); i++) {
				Log.i("checkLianDui2",cardId1.get(i) + " ,");
			}
			Log.i("checkLianDui2","==============================");
			if (cardId1.size() >= 5) {
				for (int i = 0; i < cardId1.size(); i++) {
					int cardId = cardId1.get(i);
					int count = 0;
					for (int j = 0; j < cards.length; j++) {
						if (cardId == cards[j]) {//如果相同
							++count;
							if (count < 3) {
								card.add(cards[j]);
							} else {
								continue;
							}
						}
					}
					if (count < 2) {
						if (card.size() >= 1) {
							card.remove(card.size() - 1);
						}
					}
				}
			} else {
				return card2;
			}
			Log.i("checkLianDui2","==========散对子==============");
			for (int i = 0; i < card.size(); i++) {
				Log.i("checkLianDui2",card.get(i) + " ,");
			}
			Log.i("checkLianDui2","==============================");

			cardId1.clear();
			current = cards[0];
			cardId1.add(cards[0]);
			for (int i = 1; i < card.size(); i++) {//找连续的牌
				if (card.get(i) - current == 1) {
					current = card.get(i);
					cardId1.add(card.get(i));
				} else if (card.get(i) - current > 1) {
					current = card.get(i);
					if (cardId1.size() > 4) {//如果有4个以上的连牌，就退出循环
						break;
					} else {
						cardId1.clear();
						cardId1.add(card.get(i));
					}
				}
			}
			Log.i("checkLianDui2","==========散对子====连续的牌==========");
			for (int i = 0; i < cardId1.size(); i++) {
				Log.i("checkLianDui2",cardId1.get(i) + " ,");
			}
			Log.i("checkLianDui2","==================================");
			if (cardId1.size() >= 5) {
				for (int i = 0; i < cardId1.size(); i++) {
					int cardId = cardId1.get(i);
					int count = 0;
					for (int j = 0; j < card.size(); j++) {
						if (cardId == card.get(j)) {//如果相同
							++count;
							if (count < 3) {
								card2.add(card.get(j));
							} else {
								continue;
							}
						}
					}
					if (count < 2) {
						if (card2.size() >= 1) {
							card2.remove(card2.size() - 1);
						}
					}
				}
			}
			Log.i("checkLianDui2","==========连对==============");
			for (int i = 0; i < card2.size(); i++) {
				Log.i("checkLianDui2",card2.get(i) + " ,");
			}
			Log.i("checkLianDui2","============================");
		}
		return card2;
	}

	
	/**冒泡排序
	 * @param score
	 * @return
	 */
	public static int[] Sorts(int[] score) {
		for (int i = 0; i < score.length - 1; i++) { //最多做n-1趟排序
			for (int j = 0; j < score.length - i - 1; j++) { //对当前无序区间score[0......length-i-1]进行排序(j的范围很关键，这个范围是在逐步缩小的)
				if (score[j] > score[j + 1]) { //把小的值交换到后面
					int temp = score[j];
					score[j] = score[j + 1];
					score[j + 1] = temp;
				}
			}
		}
		return score;
	}
	/**
	 * 判断连对
	 * 
	 * @param cards
	 * @return
	 */
	public static boolean checkLiandui(List<Poker> cards) {
		int count = 0;
		if (cards.size() >= 6 && cards.size() % 2 == 0) {
			for (int i = 0; i < cards.size() - 1; i += 2) {
				// 排除2和王
				if (cards.get(i).getValue() == 16 || cards.get(i).getValue() == 17 || cards.get(i).getValue() == 15) {
					return false;
				}

				if (cards.get(i).getValue() == cards.get(i + 1).getValue()) {
					count++;
				} else {
					return false;
				}
				// //System.out.println("执行到了这里"+count);
				if (i + 2 < cards.size() - 1) {
					if (cards.get(i + 2).getValue() + 1 != cards.get(i).getValue()) {
						return false;
					}
				}
			}
		}
		// //System.out.println(count);
		if (count == cards.size() / 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * //檢測一張牌在一手牌中出現的次數
	 * 
	 * @param number
	 * @param cards
	 * @return
	 */
	public static int numberCount(int number, List<Poker> cards) {
		int count = 0;
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getValue() == number) {
				// //System.out.println("value"+cards.get(i).getValue()+"number"+number);
				count++;
			}
		}
		// //System.out.println("检测count长度"+count);
		return count;
	}

	/**
	 * 检测是不是飞机带散牌
	 * 
	 * @param cards
	 * @return
	 */
	public static int checkFeijiDaisan(List<Poker> myCards) {

		if (myCards.size() % 4 != 0) {
			return 0;
		}

		// 复制一手牌进行检查
		List<Poker> cards = new ArrayList<Poker>();
		for (int i = 0; i < myCards.size(); ++i) {
			cards.add(myCards.get(i));
		}

		// 飞机总数
		int feijiCount = cards.size() / 4;

		List<Poker> del = new ArrayList<Poker>();
		// int count4 = -1;
		for (int i = 0; i < cards.size(); i++) {
			// 如果只出現一次
			if (numberCount(cards.get(i).getValue(), cards) < 3) {
				// cards.remove(i);
				del.add(cards.get(i));
				// //System.out.println("shanchu");
			} else if (numberCount(cards.get(i).getValue(), cards) == 4) {
				del.add(cards.get(i));
				i = i + 3;
			}
		}

		// 判断除去飞机的牌的数目是否为飞机数
		// if(del.size()!=cards.size()/4){
		// ////System.out.println("del.size()"+del.size()+"cards.size()/4"+cards.size()/4);
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
			if (cards.get(i).getValue() - 1 == cards.get(i + 3).getValue() && cards.get(i).getValue() < 15) {
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

	/**
	 * 检测是不是飞机带对
	 * 
	 * @param cards
	 * @return
	 */
	public static int checkFeijiDaidui(List<Poker> myCards) {
		if (myCards.size() % 5 != 0 || myCards.size() / 5 < 2) {
			return 0;
		}
		// 复制一手牌进行检查
		List<Poker> cards = new ArrayList<Poker>();
		for (int i = 0; i < myCards.size(); ++i) {
			cards.add(myCards.get(i));
		}

		List<Poker> del = new ArrayList<Poker>();

		for (int i = 0; i < cards.size(); i++) {
			// 如果只出現一次
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

		if (cards.size() % 3 != 0 || cards.size() / 3 < 2 || cards.size() / 3 != del.size() / 2) {
			return 0;
		}

		for (int i = 0; i < cards.size() - 3; i = i + 3) {
			if (cards.get(i).getValue() - 1 != cards.get(i + 3).getValue()) {
				return 0;
			}
			if (cards.get(i).getValue() > 14) {
				return 0;
			}
		}

		return cards.get(0).getValue();

	}

	/**
	 * 检测是不是飞机
	 * 
	 * @param cards
	 * @return
	 */
	public static boolean checkFeiji(List<Poker> cards) {

		int count = 1;
		int size = cards.size();
		// 如果是飞机不带
		if (size % 3 == 0) {
			for (int i = 0; i < cards.size() - 3; i += 3) {
				// 排除2和王
				if (cards.get(i).getValue() == 16 || cards.get(i).getValue() == 17 || cards.get(i).getValue() == 15) {
					return false;
				} else {
					if (cards.get(i).getValue() - 1 == cards.get(i + 3).getValue() && cards.get(i).getValue() == cards.get(i + 1).getValue()
							&& cards.get(i).getValue() == cards.get(i + 2).getValue()) {
						count++;
					} else {
						return false;
					}
				}
			}
			// 检查最后的几张牌是否一致
			if (cards.get(cards.size() - 1).getValue() == cards.get(cards.size() - 2).getValue()
					&& cards.get(cards.size() - 1).getValue() == cards.get(cards.size() - 3).getValue() && count == size / 3) {
				return true;
			}

		}
		return false;

	}

	/**
	 * 检测是不是四带两对
	 * 
	 * @param cards
	 * @return
	 */
	public static boolean checkSidai2dui(List<Poker> cards) {

		if (getPokeCount(cards) == 4 && cards.get(4).getValue() == cards.get(5).getValue() && cards.get(6).getValue() == cards.get(7).getValue()) {
			return true;
		}
		return false;
	}

	//
	/**
	 * 统计一手牌中同值的牌出现的次数来判断是对牌,三顺,三带一,炸弹,四代二等
	 * 
	 * @param cards
	 * @return
	 */
	public static int getPokeCount(List<Poker> cards) {
		int count = 1;
		int maxcount = 1;
		int nowvalue = cards.get(0).getValue(); // 先取得第一个牌的值
		for (int i = 0; i < cards.size() - 1; i++) {
			if (nowvalue == cards.get(i + 1).getValue()) {// 如果相同count++，不同改变nowvalue值
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

	//
	/**
	 * 将牌排序从小到大
	 * 
	 * @param cards
	 * @param poker
	 * @return
	 */
	public static int[] sort(int[] cards, Poker[] poker) {
		if (null == poker || null == cards) {
			return null;
		}
		int zhi[] = new int[cards.length];
		Number[] num = new Number[cards.length];
		// * 冒泡排序
		for (int i = 0; i < cards.length; i++) {
			num[i] = new Number();
			num[i].setValue(poker[cards[i]].getValue() * 100 + poker[cards[i]].getStyle());
			num[i].setPokerNumber(cards[i]);
			zhi[i] = poker[cards[i]].getValue();
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
			zhi[i] = num[i].getPokerNumber();
		}
		return zhi;
	}
}
