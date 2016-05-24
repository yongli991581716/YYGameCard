package com.lordcard.ui.personal.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lordcard.entity.Poker;
import com.lordcard.ui.personal.logic.strategy.interfaces.Strategy;

@SuppressWarnings("unused")
public class FillDiZhuData {
	/**
	 * 手中的所有牌的数组
	 */
	Map<Integer, PokerOfOneValue> pokers = new HashMap<Integer, PokerOfOneValue>();

	/**
	 * 散牌，是指在其他类型中没出现的牌，2和王不会出现在此队列
	 */
	Map<Integer, PokerOfOnePlay> sanPai = new HashMap<Integer, PokerOfOnePlay>();
	/**
	 * 对子，是指没有在其他连对里面出现过的对子，2不会出现在此队中
	 */
	Map<Integer, PokerOfOnePlay> duiZi = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> sanZhang = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> siZhang = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> zhaDan = new HashMap<Integer, PokerOfOnePlay>();

	Map<Integer, Map<Integer, PokerOfOnePlay>> feiJi = new HashMap<Integer, Map<Integer, PokerOfOnePlay>>();

	Map<Integer, PokerOfOnePlay> feiJi2 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> feiJi3 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> feiJi4 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> feiJi5 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> feiJi6 = new HashMap<Integer, PokerOfOnePlay>();

	Map<Integer, Map<Integer, PokerOfOnePlay>> lianDui = new HashMap<Integer, Map<Integer, PokerOfOnePlay>>();

	Map<Integer, PokerOfOnePlay> lianDui3 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> lianDui4 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> lianDui5 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> lianDui6 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> lianDui7 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> lianDui8 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> lianDui9 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> lianDui10 = new HashMap<Integer, PokerOfOnePlay>();

	Map<Integer, Map<Integer, PokerOfOnePlay>> shunZi = new HashMap<Integer, Map<Integer, PokerOfOnePlay>>();

	Map<Integer, PokerOfOnePlay> shunZi5 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> shunZi6 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> shunZi7 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> shunZi8 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> shunZi9 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> shunZi10 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> shunZi11 = new HashMap<Integer, PokerOfOnePlay>();
	Map<Integer, PokerOfOnePlay> shunZi12 = new HashMap<Integer, PokerOfOnePlay>();
	/**
	 * 所有牌
	 */
	public List<Poker> allPlayedPoker = new ArrayList<Poker>();

	public List<Poker> getAllPlayedPoker() {
		return allPlayedPoker;
	}

	public void setAllPlayedPoker(List<Poker> allPlayedPoker) {
		this.allPlayedPoker = allPlayedPoker;
	}

	/**
	 * 策略集合
	 */
	List<Strategy> strategys = new ArrayList<Strategy>();

	/**
	 * 地主,我顺序和otherCard牌主顺序
	 */
	private Integer dzorder;
	private int preorder;
	private int mineorder;
	private int otherCount;
	private int dzCount;
	boolean isdzBaodan = false; // 是否地主报单
	boolean isdzBaodui = false; // 是否地主报双
	boolean ispmBaodan = false; // 是否平民报单
	boolean ispmBaodui = false; // 是否平民报双
	private int pmcardNum = 17;

	public void setDzorder(Integer dzorder) {
		this.dzorder = dzorder;
	}

	public void setPreorder(int preorder) {
		this.preorder = preorder;
	}

	public void setMyorder(int myorder) {
		this.mineorder = myorder;
	}

	public Integer getDzorder() {
		return dzorder;
	}

	public void setotherCount(int count) {
		this.otherCount = count;
	}

	public void setdzCount(int count) {
		this.dzCount = count;
	}

	private int pmCount(int otherCount) {
		if (otherCount != 0 && otherCount < pmcardNum) {
			this.pmcardNum = otherCount;
		}
		return this.pmcardNum;
	}

	/**
	 * 扑克牌数量
	 */
	int pokerNum = 0;

	public int getPokerNum() {
		return pokerNum;
	}

	public void setPokerNum(int pokerNum) {
		this.pokerNum = pokerNum;
	}

	private List<Poker> nowPlaying = null;

	public List<Poker> getNowPlaying() {
		return nowPlaying;
	}

	public void setNowPlaying(List<Poker> nowPlaying) {
		this.nowPlaying = nowPlaying;
	}

	private List<Poker> nowPlayingAttachment = null;

	private boolean isInitiative;

	private boolean hasPlayOneSanPai = false;// 打牌伙伴只剩下一张牌，同时地主不止一张牌的情况下，我要打一张散牌，看看伙伴能不能过，如果不能过，下一把我就只管打自己的牌，不再打散牌，此开关检查是否是第一次打散牌

	public boolean isInitiative() {
		return isInitiative;
	}

	public void setInitiative(boolean isInitiative) {
		this.isInitiative = isInitiative;
	}

	public Map<Integer, PokerOfOneValue> getPokers() {
		return pokers;
	}

	public void setPokers(Map<Integer, PokerOfOneValue> pokers) {
		this.pokers = pokers;
	}

	/**
	 * 初始化一手扑克
	 * 
	 * @param ps
	 *            一手扑克
	 */
	public FillDiZhuData(List<Poker> ps) {
		// 初始化程序
		feiJi.put(2, feiJi2);
		feiJi.put(3, feiJi3);
		feiJi.put(4, feiJi4);
		feiJi.put(5, feiJi5);
		feiJi.put(6, feiJi6);
		lianDui.put(3, lianDui3);
		lianDui.put(4, lianDui4);
		lianDui.put(5, lianDui5);
		lianDui.put(6, lianDui6);
		lianDui.put(7, lianDui7);
		lianDui.put(8, lianDui8);
		lianDui.put(9, lianDui9);
		lianDui.put(10, lianDui10);
		shunZi.put(5, shunZi5);
		shunZi.put(6, shunZi6);
		shunZi.put(7, shunZi7);
		shunZi.put(8, shunZi8);
		shunZi.put(9, shunZi9);
		shunZi.put(10, shunZi10);
		shunZi.put(11, shunZi11);
		shunZi.put(12, shunZi12);
		// 添加策略
		// 所有牌加入到pokers里面
		for (int i = ps.size() - 1; i >= 0; --i) {
			int value = ps.get(i).getValue();
			PokerOfOneValue poov = null;
			if (this.getPokers().containsKey(value)) {
				poov = this.getPokers().get(value);
			} else {
				poov = new PokerOfOneValue(value);
				this.getPokers().put(value, poov);
			}
			poov.addCard(ps.get(i));
		}
		fillPokerList();
		this.pokerNum = 17;
	}

	/**
	 * 把poker中的牌添加到各个牌型的数组中
	 */
	private void fillPokerList() {

		sanPai.clear();

		duiZi.clear();
		sanZhang.clear();
		siZhang.clear();
		zhaDan.clear();

		feiJi2.clear();
		feiJi3.clear();
		feiJi4.clear();
		feiJi5.clear();
		feiJi6.clear();

		lianDui3.clear();
		lianDui4.clear();
		lianDui5.clear();
		lianDui6.clear();
		lianDui7.clear();
		lianDui8.clear();
		lianDui9.clear();
		lianDui10.clear();

		shunZi5.clear();
		shunZi6.clear();
		shunZi7.clear();
		shunZi8.clear();
		shunZi9.clear();
		shunZi10.clear();
		shunZi11.clear();
		shunZi12.clear();

		clearUsedState();
		// 添加三张，四张和炸弹，并且设置了那些对子被使用了
		addSanZhangAndSiZhangAndZhadan();

		// 添加连对
		// printUsedState();

		// 添加飞机
		addFeiJi();
		// 添加顺子并且设置了那些对子被使用了
		addShunZi();
		addSanZhang();
		// 将没有被使用的对子添加到duiZi列表,清除被使用状态
		addLianDui();
		addDuiZi();
		// 将没有被使用的单牌添加到散牌列表，并清楚使用状态
		addSanPai();

		clearUsedState();

		printAllPokerList();
	}

	/**
	 * 打印所欲的牌型数组
	 */
	private void printAllPokerList() {

		Map<String, Map<Integer, PokerOfOnePlay>> allPokerList = new HashMap<String, Map<Integer, PokerOfOnePlay>>();
		// 所有的牌型都加入一个数组，主要是为了打印方便
		allPokerList.put("散牌", sanPai);
		allPokerList.put("对子", duiZi);
		allPokerList.put("三张", sanZhang);
		allPokerList.put("四张", siZhang);
		allPokerList.put("炸弹", zhaDan);
		allPokerList.put("飞机2", feiJi2);
		allPokerList.put("飞机3", feiJi3);
		allPokerList.put("飞机4", feiJi4);
		allPokerList.put("飞机5", feiJi5);
		allPokerList.put("飞机6", feiJi6);
		allPokerList.put("连对3", lianDui3);
		allPokerList.put("连对4", lianDui4);
		allPokerList.put("连对5", lianDui5);
		allPokerList.put("连对6", lianDui6);
		allPokerList.put("连对7", lianDui7);
		allPokerList.put("连对8", lianDui8);
		allPokerList.put("连对9", lianDui9);
		allPokerList.put("连对10", lianDui10);
		allPokerList.put("顺子5", shunZi5);
		allPokerList.put("顺子6", shunZi6);
		allPokerList.put("顺子7", shunZi7);
		allPokerList.put("顺子8", shunZi8);
		allPokerList.put("顺子9", shunZi9);
		allPokerList.put("顺子10", shunZi10);
		allPokerList.put("顺子11", shunZi11);
		allPokerList.put("顺子12", shunZi12);
		//System.out.print("all pokers: ");
		for (PokerOfOneValue poov : this.pokers.values()) {
			for (Poker p : poov.getPokers()) {
				//System.out.print(" " + p.getValue());
			}
		}
		Iterator<String> iter = allPokerList.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Map<Integer, PokerOfOnePlay> poops = allPokerList.get(name);
			if (poops.size() == 0) {
				continue;
			}

		}

	}

	/**
	 * 添加散牌
	 */
	private void addSanPai() {
		for (int i = 3; i < 18; ++i) {
			// 所有的2都当成散牌，但在取附带牌时不取2
			if (!pokers.containsKey(i)) {
				continue;
			}

			PokerOfOneValue v = pokers.get(i);
			PokerOfOnePlay poop = new PokerOfOnePlay(v.getValue(), DoudizhuRule.Danpai);
			poop.add(v);
			sanPai.put(v.getValue(), poop);
		}

	}

	/**
	 * 添加小队
	 */
	private void addDuiZi() {
		for (PokerOfOneValue v : pokers.values()) {
			if (v.getUnusedNum() >= 2) {
				PokerOfOnePlay poop = new PokerOfOnePlay(v.getValue(), DoudizhuRule.Yidui);
				poop.add(v);
				duiZi.put(v.getValue(), poop);
			}
		}

	}
	/**
	 * 检查pokers中的顺子，并添加到shunzi数组
	 */

	private void addShunZi() {
		for (int i = 3; i <= 10; ++i) {
			if (!(pokers.containsKey(i) && pokers.containsKey(i + 1) && pokers.containsKey(i + 2) && pokers.containsKey(i + 3))) {
				continue;
			}

			for (int lianNum = 5; lianNum <= 12; ++lianNum) {
				int maxValue = i + lianNum - 1;
				if (maxValue > 14) {
					break;
				}

				if (pokers.containsKey(maxValue)) {
					// 多对
					PokerOfOnePlay poop = new PokerOfOnePlay(maxValue, DoudizhuRule.shunzi);
					for (int j = i; j <= maxValue; ++j) {
						poop.add(pokers.get(j));
						// pokers.get(j).setUsedState(true);//设置已经使用状态，主要是给监测单牌使用
					}
					shunZi.get(lianNum).put(maxValue, poop);
				} else {
					break;
				}
			}
		}
	}

	/**
	 * 检查出pokers中的连对，并添加到连对数组
	 */
	private void addLianDui() {
		for (int i = 3; i < 14; ++i) {
			if (!(pokers.containsKey(i) && pokers.get(i).PokerNum() >= 2 && pokers.containsKey(i + 1) && pokers.get(i + 1).PokerNum() >= 2)) {
				continue;
			}
			for (int lianNum = 3; lianNum <= 10; ++lianNum) {
				int maxValue = i + lianNum - 1;
				if (maxValue > 14) {
					break;
				}
				if (pokers.containsKey(maxValue) && pokers.get(maxValue).PokerNum() >= 2) {
					PokerOfOnePlay poop = new PokerOfOnePlay(maxValue, DoudizhuRule.liandui);
					for (int j = i; j <= maxValue; ++j) {
						poop.add(pokers.get(j));
					}
					lianDui.get(lianNum).put(maxValue, poop);

				} else {
					break;
				}
			}

		}

	}

	private void addSanZhang() {
		for (PokerOfOneValue poov : this.getPokers().values()) {
			// 添加三张

			if (poov.PokerNum() >= 3) {
				PokerOfOnePlay poop = new PokerOfOnePlay(poov.getValue(), DoudizhuRule.Santiao);
				poop.add(poov);
				sanZhang.put(poov.getValue(), poop);
				// 设置已经使用标志（为了后面判断散牌和对子）
				continue;
			}
		}
	}

	/**
	 * 添加四张，炸弹
	 */
	private void addSanZhangAndSiZhangAndZhadan() {
		for (PokerOfOneValue poov : this.getPokers().values()) {
			if (poov.PokerNum() == 4) {
				PokerOfOnePlay poop = new PokerOfOnePlay(poov.getValue(), DoudizhuRule.zhadan);
				poop.add(poov);
				zhaDan.put(poov.getValue(), poop);
				PokerOfOnePlay poop2 = new PokerOfOnePlay(poov.getValue(), DoudizhuRule.siZhang);
				poop2.add(poov);
				siZhang.put(poov.getValue(), poop2);
				// 设置已经使用标志（为了后面判断散牌和对子）
				continue;
			}

		}
		// 添加王炸
		if (pokers.containsKey(16) && pokers.containsKey(17)) {
			PokerOfOnePlay poop = new PokerOfOnePlay(17, DoudizhuRule.zhadan);
			poop.add(pokers.get(16));
			poop.add(pokers.get(17));
			zhaDan.put(17, poop);
		}
	}

	private void addFeiJi() {

		for (int i = 3; i < 14; ++i) {
			if (!(pokers.containsKey(i)) || pokers.get(i).PokerNum() < 3) {
				continue;
			}

			for (int lianNum = 2; lianNum <= 6; ++lianNum) {
				int maxValue = i + lianNum - 1;
				if (maxValue > 14) {
					break;
				}
				if (pokers.containsKey(maxValue) && pokers.get(maxValue).PokerNum() == 3) {
					PokerOfOnePlay poop = new PokerOfOnePlay(maxValue, DoudizhuRule.feiji);
					for (int j = i; j <= maxValue; ++j) {
						poop.add(pokers.get(j));

					}
					feiJi.get(lianNum).put(maxValue, poop);
				} else {
					break;
				}
			}

		}
	}

	/**
	 * 清楚是否被用标志，此标志位只是在一次出牌决策过程中作为临时状态位使用，每次决策后必须清空。
	 */
	private void clearUsedState() {
		for (PokerOfOneValue poov : this.getPokers().values()) {
			for (Poker p : poov.getPokers()) {
				p.setUsed(false);
			}
		}
	}

	private void printUsedState() {

		for (PokerOfOneValue poov : this.getPokers().values()) {
			for (Poker p : poov.getPokers()) {

			}
			System.out.println();
		}
	}

	/**
	 * 获取提示，otherPokers是已经从大到小排好序的牌
	 * 
	 * @param otherPokers
	 *            其地主的一手牌,如果是null，则主动出牌
	 * @return 要打出去的数组
	 */
	public List<Poker> maxPoker(List<Poker> checkPoker) {
		clearUsedState();
		// printUsedState();
		// 指向某个牌型数组的指针
		Map<Integer, PokerOfOnePlay> play = null;
		// 附带牌（带单或者带对）的数量，
		int attachmentCount = 0;
		// 带单牌还是带对子
		boolean takeSanPai = true;
		// 牌的类型
		int type = DoudizhuRule.checkpai(checkPoker);
		// 对手牌最大
		int otherMaxValue = DoudizhuRule.getMaxNumber(checkPoker);

		switch (type) {
		case DoudizhuRule.Danpai:
			play = sanPai;
			break;
		case DoudizhuRule.Yidui:
			play = duiZi;
			break;
		case DoudizhuRule.Santiao:
			play = sanZhang;
			break;
		case DoudizhuRule.Sandaiyi:
			play = sanZhang;
			attachmentCount = 1;
			break;
		case DoudizhuRule.Sandaier:
			play = sanZhang;
			attachmentCount = 1;
			takeSanPai = false;
			break;
		case DoudizhuRule.zhadan:
		case DoudizhuRule.wangzha:
			play = zhaDan;
			break;
		case DoudizhuRule.sidaiyi:
			play = siZhang;
			attachmentCount = 2;
			break;
		case DoudizhuRule.sidaier:
			play = siZhang;
			attachmentCount = 2;
			takeSanPai = false;

			break;
		case DoudizhuRule.feiji:

			play = feiJi.get(checkPoker.size() / 3);
			break;
		case DoudizhuRule.feijidaisan:
			play = feiJi.get(checkPoker.size() / 4);
			attachmentCount = checkPoker.size() / 4;
			break;
		case DoudizhuRule.feijidaidui:
			play = feiJi.get(checkPoker.size() / 5);
			attachmentCount = checkPoker.size() / 5;
			takeSanPai = false;
			break;
		case DoudizhuRule.liandui:
			play = lianDui.get(checkPoker.size() / 2);

			break;
		case DoudizhuRule.shunzi:
			play = shunZi.get(checkPoker.size());
			break;
		}
		if (play == null) {
			return null;
		}
		if (play != null) {
			List<Poker> play2 = new ArrayList<Poker>();
			List<Poker> play1 = new ArrayList<Poker>();
			List<Poker> ret = new ArrayList<Poker>();
			for (int i = otherMaxValue + 1; i < 18; ++i) {
				if (!play.containsKey(i)) {
					continue;
				}
				PokerOfOnePlay poop = play.get(i);
				if (poop.getMaxValue() > otherMaxValue) {
					play1 = poop.getOnePlay();
					if (play1 == null || play1.size() == 0) {
						continue;
					}

					// 设置该手牌为已使用

					setUsedState(play1, true);
					nowPlaying = play1;
					if (attachmentCount > 0) {
						play2 = getAttachment(attachmentCount, takeSanPai);

						if (play2 == null) {
							setUsedState(play1, false);
							continue;
						}
						setUsedState(play2, true);
					}
					printPokers(play1, play2);
					// 开始使用策略器进行处理

					//System.out.println("总得分：" + point);

					if (play1 != null) {
						setUsedState(play1, false);
					}
					if (play2 != null) {
						setUsedState(play2, false);
					}
					if (play1 != null) {
						ret.addAll(play1);

					}
					if (play2 != null) {
						ret.addAll(play2);
					}

				}
				if (ret != null && ret.size() > 0) {
					return ret;
				}
			}
			if (ret != null && ret.size() > 0) {
				return ret;
			}
		}
		return null;

	}

	/**
	 * 打印手牌
	 * 
	 * @param play1
	 *            第一手牌
	 * @param play2
	 *            第二手牌 一般是带的散牌或者对牌
	 */
	private void printPokers(List<Poker> play1, List<Poker> play2) {
		//System.out.print("一手牌:");
		if (play1 != null) {
			for (int i = 0; i < play1.size(); ++i) {
				//System.out.print(play1.get(i).getValue() + " ");
			}
		}

		if (play2 != null) {
			for (int i = 0; i < play2.size(); ++i) {
				//System.out.print(play2.get(i).getValue() + " ");
			}
		}
	}



	/**
	 * 取三张或者四张或者飞机带的牌
	 * 
	 * @param attachment
	 * @return
	 */
	private List<Poker> getAttachment(int attachmentCount, boolean takeSanPai) {
		List<Poker> usedList = new ArrayList<Poker>();
		List<Poker> ret = new ArrayList<Poker>();
		if (takeSanPai) {
			Object[] poops = sanPai.values().toArray();
			// 带散牌
			int i = 0;
			for (; i < poops.length; ++i) {

				PokerOfOnePlay poop = (PokerOfOnePlay) poops[i];
				if (poop.getMaxValue() > 14) {
					continue;
				}
				List<Poker> ps = poop.getOnePlay();
				if (ps.size() == 0) {
					continue;
				}
				ret.add(ps.get(0));
				ps.get(0).setUsed(true);
				usedList.add(ps.get(0));

				if (--attachmentCount <= 0) {
					this.setUsedState(usedList, false);
					return ret;
				}
			}

			poops = duiZi.values().toArray();
			Poker dui = null;
			// 在对子中取单牌
			for (; i < poops.length; ++i) {
				PokerOfOnePlay poop = (PokerOfOnePlay) poops[i];
				dui = poop.getPokers().get(0).getOneUnusedPoker();
				if (dui == null) {
					continue;
				}
				ret.add(dui);
				dui.setUsed(true);
				usedList.add(dui);
				if (--attachmentCount == 0) {
					this.setUsedState(usedList, false);
					return ret;
				}
				dui = poop.getPokers().get(0).getOneUnusedPoker();
				if (dui == null) {
					continue;
				}
				ret.add(dui);
				dui.setUsed(true);
				usedList.add(dui);

				if (--attachmentCount == 0) {
					this.setUsedState(usedList, false);
					return ret;
				}

			}

			// this.setUsedState(ret, true);
			// 从pokers队列中取数据
			for (i = 3; i < 18; ++i) {
				if (!pokers.containsKey(i)) {
					continue;
				}
				PokerOfOneValue poov = pokers.get(i);
				if (poov.PokerNum() == 4) {
					continue;
				}
				Poker p = poov.getOneUnusedPoker();
				if (p != null) {
					ret.add(p);
					p.setUsed(true);
				} else {
					continue;
				}
				// 在取一个单牌
				if (--attachmentCount == 0) {
					this.setUsedState(usedList, false);
					return ret;
				}

				p = poov.getOneUnusedPoker();
				if (p != null) {
					ret.add(p);
					p.setUsed(true);
				} else {
					continue;
				}

				if (--attachmentCount == 0) {
					this.setUsedState(usedList, false);
					return ret;
				}

			}
		} else {
			// 带对
			for (int i = 3; i < 16; ++i) {
				if (!duiZi.containsKey(i)) {
					continue;
				}
				List<Poker> dui = duiZi.get(i).getOnePlay();
				if (dui.size() == 0) {
					continue;
				}
				ret.addAll(dui);
				dui.get(0).setUsed(true);
				dui.get(1).setUsed(true);
				if (--attachmentCount == 0) {
					return ret;
				}
			}
			// 在所有牌中找对子
			for (int i = 3; i < 16; ++i) {
				if (!pokers.containsKey(i)) {
					continue;
				}
				int n = pokers.get(i).getUnusedNum();
				if (n == 4) {
					continue;
				}
				List<Poker> dui = pokers.get(i).getUnusedPoker(2);
				if (dui.size() != 2) {
					continue;
				}
				ret.addAll(dui);
				if (--attachmentCount == 0) {
					return ret;
				}
			}
		}
		return null;
	}

	/**
	 * 设置一手牌的状态
	 * 
	 * @param play1
	 * @param isUsed
	 */
	private void setUsedState(List<Poker> play1, boolean isUsed) {
		for (Poker p : play1) {
			p.setUsed(isUsed);
		}

	}

	/**
	 * 打出一手牌，此处不检查此手牌的合法性
	 * 
	 * @param onePlay
	 *            一手牌
	 */
	public void Play(List<Poker> onePlay) {
		// 主要工作是将打出去的牌在数据结构中删除
		for (Poker p : onePlay) {
			PokerOfOneValue poov = pokers.get(p.getValue());
			if (poov == null) {
				continue;
			}
			poov.getPokers().remove(p);
			if (poov.getPokers().size() == 0) {
				pokers.remove(p.getValue());
			}
		}
		// 打出一手牌后，重新填充所有牌的列表，这个地方性能有些低，需要重新组织结构
		this.fillPokerList();
		this.pokerNum -= onePlay.size();
		return;
	}

	public Map<Integer, PokerOfOnePlay> getSanPai() {
		return sanPai;
	}

	public void setSanPai(Map<Integer, PokerOfOnePlay> sanPai) {
		this.sanPai = sanPai;
	}

	public Map<Integer, PokerOfOnePlay> getDuiZi() {
		return duiZi;
	}

	public void setDuiZi(Map<Integer, PokerOfOnePlay> duiZi) {
		this.duiZi = duiZi;
	}

	public Map<Integer, PokerOfOnePlay> getSanZhang() {
		return sanZhang;
	}

	public void setSanZhang(Map<Integer, PokerOfOnePlay> sanZhang) {
		this.sanZhang = sanZhang;
	}

	public Map<Integer, PokerOfOnePlay> getSiZhang() {
		return siZhang;
	}

	public void setSiZhang(Map<Integer, PokerOfOnePlay> siZhang) {
		this.siZhang = siZhang;
	}

	public Map<Integer, PokerOfOnePlay> getZhaDan() {
		return zhaDan;
	}

	public void setZhaDan(Map<Integer, PokerOfOnePlay> zhaDan) {
		this.zhaDan = zhaDan;
	}

	public Map<Integer, Map<Integer, PokerOfOnePlay>> getFeiJi() {
		return feiJi;
	}

	public void setFeiJi(Map<Integer, Map<Integer, PokerOfOnePlay>> feiJi) {
		this.feiJi = feiJi;
	}

	public Map<Integer, PokerOfOnePlay> getFeiJi2() {
		return feiJi2;
	}

	public void setFeiJi2(Map<Integer, PokerOfOnePlay> feiJi2) {
		this.feiJi2 = feiJi2;
	}

	public Map<Integer, PokerOfOnePlay> getFeiJi3() {
		return feiJi3;
	}

	public void setFeiJi3(Map<Integer, PokerOfOnePlay> feiJi3) {
		this.feiJi3 = feiJi3;
	}

	public Map<Integer, PokerOfOnePlay> getFeiJi4() {
		return feiJi4;
	}

	public void setFeiJi4(Map<Integer, PokerOfOnePlay> feiJi4) {
		this.feiJi4 = feiJi4;
	}

	public Map<Integer, PokerOfOnePlay> getFeiJi5() {
		return feiJi5;
	}

	public void setFeiJi5(Map<Integer, PokerOfOnePlay> feiJi5) {
		this.feiJi5 = feiJi5;
	}

	public Map<Integer, PokerOfOnePlay> getFeiJi6() {
		return feiJi6;
	}

	public void setFeiJi6(Map<Integer, PokerOfOnePlay> feiJi6) {
		this.feiJi6 = feiJi6;
	}

	public Map<Integer, Map<Integer, PokerOfOnePlay>> getLianDui() {
		return lianDui;
	}

	public void setLianDui(Map<Integer, Map<Integer, PokerOfOnePlay>> lianDui) {
		this.lianDui = lianDui;
	}

	public Map<Integer, PokerOfOnePlay> getLianDui3() {
		return lianDui3;
	}

	public void setLianDui3(Map<Integer, PokerOfOnePlay> lianDui3) {
		this.lianDui3 = lianDui3;
	}

	public Map<Integer, PokerOfOnePlay> getLianDui4() {
		return lianDui4;
	}

	public void setLianDui4(Map<Integer, PokerOfOnePlay> lianDui4) {
		this.lianDui4 = lianDui4;
	}

	public Map<Integer, PokerOfOnePlay> getLianDui5() {
		return lianDui5;
	}

	public void setLianDui5(Map<Integer, PokerOfOnePlay> lianDui5) {
		this.lianDui5 = lianDui5;
	}

	public Map<Integer, PokerOfOnePlay> getLianDui6() {
		return lianDui6;
	}

	public void setLianDui6(Map<Integer, PokerOfOnePlay> lianDui6) {
		this.lianDui6 = lianDui6;
	}

	public Map<Integer, PokerOfOnePlay> getLianDui7() {
		return lianDui7;
	}

	public void setLianDui7(Map<Integer, PokerOfOnePlay> lianDui7) {
		this.lianDui7 = lianDui7;
	}

	public Map<Integer, PokerOfOnePlay> getLianDui8() {
		return lianDui8;
	}

	public void setLianDui8(Map<Integer, PokerOfOnePlay> lianDui8) {
		this.lianDui8 = lianDui8;
	}

	public Map<Integer, PokerOfOnePlay> getLianDui9() {
		return lianDui9;
	}

	public void setLianDui9(Map<Integer, PokerOfOnePlay> lianDui9) {
		this.lianDui9 = lianDui9;
	}

	public Map<Integer, PokerOfOnePlay> getLianDui10() {
		return lianDui10;
	}

	public void setLianDui10(Map<Integer, PokerOfOnePlay> lianDui10) {
		this.lianDui10 = lianDui10;
	}

	public Map<Integer, Map<Integer, PokerOfOnePlay>> getShunZi() {
		return shunZi;
	}

	public void setShunZi(Map<Integer, Map<Integer, PokerOfOnePlay>> shunZi) {
		this.shunZi = shunZi;
	}

	public Map<Integer, PokerOfOnePlay> getShunZi5() {
		return shunZi5;
	}

	public void setShunZi5(Map<Integer, PokerOfOnePlay> shunZi5) {
		this.shunZi5 = shunZi5;
	}

	public Map<Integer, PokerOfOnePlay> getShunZi6() {
		return shunZi6;
	}

	public void setShunZi6(Map<Integer, PokerOfOnePlay> shunZi6) {
		this.shunZi6 = shunZi6;
	}

	public Map<Integer, PokerOfOnePlay> getShunZi7() {
		return shunZi7;
	}

	public void setShunZi7(Map<Integer, PokerOfOnePlay> shunZi7) {
		this.shunZi7 = shunZi7;
	}

	public Map<Integer, PokerOfOnePlay> getShunZi8() {
		return shunZi8;
	}

	public void setShunZi8(Map<Integer, PokerOfOnePlay> shunZi8) {
		this.shunZi8 = shunZi8;
	}

	public Map<Integer, PokerOfOnePlay> getShunZi9() {
		return shunZi9;
	}

	public void setShunZi9(Map<Integer, PokerOfOnePlay> shunZi9) {
		this.shunZi9 = shunZi9;
	}

	public Map<Integer, PokerOfOnePlay> getShunZi10() {
		return shunZi10;
	}

	public void setShunZi10(Map<Integer, PokerOfOnePlay> shunZi10) {
		this.shunZi10 = shunZi10;
	}

	public Map<Integer, PokerOfOnePlay> getShunZi11() {
		return shunZi11;
	}

	public void setShunZi11(Map<Integer, PokerOfOnePlay> shunZi11) {
		this.shunZi11 = shunZi11;
	}

	public Map<Integer, PokerOfOnePlay> getShunZi12() {
		return shunZi12;
	}

	public void setShunZi12(Map<Integer, PokerOfOnePlay> shunZi12) {
		this.shunZi12 = shunZi12;
	}

	public List<Strategy> getStrategys() {
		return strategys;
	}

	public void setStrategys(List<Strategy> strategys) {
		this.strategys = strategys;
	}

	public List<Poker> getNowPlayingAttachment() {
		return nowPlayingAttachment;
	}

	public void setNowPlayingAttachment(List<Poker> nowPlayingAttachment) {
		this.nowPlayingAttachment = nowPlayingAttachment;
	}

	/**
	 * 调整pokers
	 * 
	 * @param myPokers
	 */
	public void adjustPokers(List<Poker> ps) {
		this.pokers.clear();
		// 所有牌加入到pokers里面
		for (int i = ps.size() - 1; i >= 0; --i) {
			int value = ps.get(i).getValue();
			PokerOfOneValue poov = null;
			if (this.getPokers().containsKey(value)) {
				poov = this.getPokers().get(value);
			} else {
				poov = new PokerOfOneValue(value);
				this.getPokers().put(value, poov);
			}
			poov.addCard(ps.get(i));
		}
		fillPokerList();
		this.pokerNum = ps.size();

	}

	/**
	 * 对当前的牌取一个分值，算出当前牌的好坏程度
	 * 
	 * @return
	 */
	public int getScore() {
		int zhaDan = Integer.parseInt(ConfigUtils.get("rule.robot.zhaDan"));
		int sanPai = Integer.parseInt(ConfigUtils.get("rule.robot.sanPai"));
		int wang = Integer.parseInt(ConfigUtils.get("rule.robot.bigPai.wang"));
		int two = Integer.parseInt(ConfigUtils.get("rule.robot.bigPai.two"));
		int a = Integer.parseInt(ConfigUtils.get("rule.robot.bigPai.a"));
		int k = Integer.parseInt(ConfigUtils.get("rule.robot.bigPai.k"));
		int lianDui = Integer.parseInt(ConfigUtils.get("rule.robot.bigPai.lianDui"));
		int feiJi = Integer.parseInt(ConfigUtils.get("rule.robot.bigPai.feiJi"));
		int bigSanZhang = Integer.parseInt(ConfigUtils.get("rule.robot.bigPai.bigSanZhang"));

		int score = 0;
		// 炸弹计分
		score += zhaDan * this.zhaDan.size();
		// 王计分
		if (this.pokers.containsKey(17)) {
			score += wang;
		}
		if (this.pokers.containsKey(16)) {
			score += wang;
		}
		// 2计分
		if (this.pokers.containsKey(15)) {
			score += two * this.pokers.get(15).PokerNum();
		}
		// A计分
		if (this.pokers.containsKey(14)) {
			score += a * this.pokers.get(14).PokerNum();
		}
		// K计分
		if (this.pokers.containsKey(13)) {
			score += k * this.pokers.get(13).PokerNum();
		}
		// 散牌 扣分
		for (int i = 3; i < 14; ++i) {
			if (this.sanPai.containsKey(i)) {
				score += sanPai;
			}
		}
		// 大三张计分
		for (int i = 11; i < 16; ++i) {
			if (this.sanZhang.containsKey(i)) {
				score += bigSanZhang;
			}
		}
		// 有飞机 计分
		score += feiJi * this.feiJi2.size();

		// 连对计分
		score += lianDui * this.lianDui3.size();

		//System.out.println("--------poker scoreing-----------");

		this.printAllPokerList();

		//System.out.println("--------score " + score + "------------");

		return score;
	}

	/**
	 * 取出当前未打出的所有牌，按照value值从大到小排列
	 * @return 所有牌的队列
	 */
	private List<Poker> getAllPokers() {
		List<Poker> restPokers = new ArrayList<Poker>();
		for (int i = 17; i >= 3; --i) {
			if (!pokers.containsKey(i)) {
				continue;
			}
			for (Poker p : pokers.get(i).getPokers()) {
				if (p.isUsed()) {
					continue;
				}
				restPokers.add(p);
			}
		}
		return restPokers;
	}
}
