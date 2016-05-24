package com.lordcard.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.lordcard.entity.Poker;

@SuppressLint("UseSparseArrays")
public class DouDiZhuData {
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
	 * 策略集合
	 */
	// List<Strategy> strategys = new ArrayList<Strategy>();

	/**
	 * 地主,我顺序和otherCard牌主顺序
	 */
	private Integer dzorder;
	@SuppressWarnings("unused")
	private int preorder;
	private int mineorder;
	private int otherCount;
	private int dzCount;
	boolean isdzBaodan = false; // 是否地主报单
	boolean isdzBaodui = false; // 是否地主报双
	boolean ispmBaodan = false; // 是否平民报单
	boolean ispmBaodui = false; // 是否平民报双
	boolean isZhaDan = false; // 是否 要打炸弹

	public void setPlayZD(Boolean playZhaDan) {
		this.isZhaDan = playZhaDan;
	}

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
		int pmcardNum = 17;
		if (otherCount < pmcardNum) {
			pmcardNum = otherCount;
		}
		return pmcardNum;
	}

	/**
	 * 扑克牌数量
	 */
	int pokerNum = 0;

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

	public DouDiZhuData(List<Poker> ps) {
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
		// strategys.add(new StrategyLessSanPai(this));
		// strategys.add(new StrategySanPai(this));

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
		// fillPokerList();
		this.pokerNum = 17;
	}

	/**
	 * 把poker中的牌添加到各个牌型的数组中
	 */
	public void fillPokerList() {

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
		addLianDui();
		// printUsedState();

		// 添加飞机
		addFeiJi();
		// 添加顺子并且设置了那些对子被使用了
		addShunZi();
		addSanZhang();
		// 将没有被使用的对子添加到duiZi列表,清除被使用状态
		addDuiZi();
		// 将没有被使用的单牌添加到散牌列表，并清楚使用状态
		addSanPai();

		clearUsedState();

		printAllPokerList();
	}

	/**
	 * 把poker中的牌添加到各个牌型的数组中
	 */
	public void fillAllPokerList() {
		this.setPlayZD(true);
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

		clearUsedState();
		// 添加连对
		addLianDui();
		// printUsedState();

		clearUsedState();
		// 添加飞机
		addFeiJi();

		clearUsedState();
		// 添加顺子并且设置了那些对子被使用了
		addAllShunZi();

		clearUsedState();
		addAllSanZhang();

		// 将没有被使用的对子添加到duiZi列表,清除被使用状态
		clearUsedState();
		addAllDuiZi();

		clearUsedState();
		// 将没有被使用的单牌添加到散牌列表，并清楚使用状态
		addAllSanPai();

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
		System.out.print("all pokers: ");
		for (PokerOfOneValue poov : this.pokers.values()) {
			for (Poker p : poov.getPokers()) {
				System.out.print(" " + p.getValue());
			}
		}
		System.out.println(" ");
		Iterator<String> iter = allPokerList.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Map<Integer, PokerOfOnePlay> poops = allPokerList.get(name);
			if (poops.size() == 0) {
				continue;
			}
			System.out.print(name);
			for (PokerOfOnePlay play : poops.values()) {
				System.out.print(" " + play.getMaxValue());
			}
			System.out.println("");
		}

	}

	/**
	 * 添加散牌
	 */
	private void addSanPai() {
		// for (PokerOfOneValue v : pokers.values()){
		// if (v.PokerNum() != 1 || v.getValue() >= 15 || v.getUnusedNum() ==
		// 0){
		// continue;
		// }
		//
		// PokerOfOnePlay poop = new
		// PokerOfOnePlay(v.getValue(),DoudizhuRule.Danpai);
		// poop.add(v);
		// sanPai.put(v.getValue(), poop);
		//
		// }
		int j = 18;
		if (pokers.containsKey(16) && pokers.containsKey(17)) {
			j = 16;
		}
		for (int i = 3; i < j; ++i) {
			// 所有的2都当成散牌，但在取附带牌时不取2
			if (!pokers.containsKey(i)) {
				continue;
			}
			if (pokers.get(i).getUnusedNum() != 1) {
				continue;
			}

			PokerOfOneValue v = pokers.get(i);
			PokerOfOnePlay poop = new PokerOfOnePlay(v.getValue(), DoudizhuRule.Danpai);
			poop.add(v);
			sanPai.put(v.getValue(), poop);
		}

	}

	/**
	 * 添加散牌
	 */
	private void addAllSanPai() {
		// for (PokerOfOneValue v : pokers.values()){
		// if (v.PokerNum() != 1 || v.getValue() >= 15 || v.getUnusedNum() ==
		// 0){
		// continue;
		// }
		//
		// PokerOfOnePlay poop = new
		// PokerOfOnePlay(v.getValue(),DoudizhuRule.Danpai);
		// poop.add(v);
		// sanPai.put(v.getValue(), poop);
		//
		// }

		for (int i = 3; i < 18; i++) {

			if (!pokers.containsKey(i) || pokers.get(i).PokerNum() == 4) {
				continue;
			}

			PokerOfOneValue v = pokers.get(i);
			PokerOfOnePlay poop = new PokerOfOnePlay(v.getValue(), DoudizhuRule.Danpai);
			poop.add(v);
			sanPai.put(v.getValue(), poop);
		}
	}

	// for (int i = 3; i < 18; ++i) {
	// // 所有的2都当成散牌，但在取附带牌时不取2
	// if (!pokers.containsKey(i)) {
	// continue;
	// }
	//
	// PokerOfOneValue v = pokers.get(i);
	// PokerOfOnePlay poop = new PokerOfOnePlay(v.getValue(),
	// DoudizhuRule.Danpai);
	// poop.add(v);
	// sanPai.put(v.getValue(), poop);
	// }

	/**
	 * 添加小队
	 */
	private void addDuiZi() {
		for (PokerOfOneValue v : pokers.values()) {
			// if (v.PokerNum() != 2){
			// continue;
			// }
			if (v.getUnusedNum() == 2) {
				PokerOfOnePlay poop = new PokerOfOnePlay(v.getValue(), DoudizhuRule.Yidui);
				poop.add(v);
				duiZi.put(v.getValue(), poop);
			}
		}

	}

	/**
	 * 添加所有小队
	 */
	private void addAllDuiZi() {
		for (PokerOfOneValue v : pokers.values()) {
			for (int i = 3; i < 16; i++) {
				if (v.getUnusedNum() < 2 || v.getUnusedNum() == 4) {
					continue;
				}
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
				int shunzhongdui = 0;
				int shunzhongsan = 0;
				if (maxValue > 14) {
					break;
				}

				for (int painum = i; painum <= maxValue; ++painum) {
					if (pokers.containsKey(painum) && pokers.get(painum).PokerNum() >= 3) {
						shunzhongsan++;
					}
				}
				if (shunzhongsan >= (lianNum + 1) / 3) {
					break;
				}
				for (int painum = i; painum <= maxValue; ++painum) {
					if (pokers.containsKey(painum) && pokers.get(painum).PokerNum() >= 2) {
						shunzhongdui++;
					}
				}
				if (shunzhongdui > (lianNum / 2)) {
					break;
				}

				if (pokers.containsKey(maxValue)) {
					// 多对
					PokerOfOnePlay poop = new PokerOfOnePlay(maxValue, DoudizhuRule.shunzi);
					for (int j = i; j <= maxValue; ++j) {
						poop.add(pokers.get(j));
						// pokers.get(j).setUsedState(true);//设置已经使用状态，主要是给监测单牌使用
					}
					this.setUsedState(poop.getOnePlayIgnoreUsedState(), true);
					shunZi.get(lianNum).put(maxValue, poop);
				} else {
					break;
				}
			}
		}
	}

	/**
	 * 检查pokers中的顺子，并添加到shunzi数组
	 */

	private void addAllShunZi() {
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
					this.setUsedState(poop.getOnePlayIgnoreUsedState(), true);
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
					// 多对
					PokerOfOnePlay poop = new PokerOfOnePlay(maxValue, DoudizhuRule.liandui);
					for (int j = i; j <= maxValue; ++j) {
						poop.add(pokers.get(j));
						// pokers.get(j).setUsedState(true);
					}
					this.setUsedState(poop.getOnePlayIgnoreUsedState(), true);
					lianDui.get(lianNum).put(maxValue, poop);

				} else {
					break;
				}
			}

		}

	}

	/**
	 * 添加三张
	 */
	private void addSanZhang() {
		for (PokerOfOneValue poov : this.getPokers().values()) {
			// 添加三张
			if (poov.PokerNum() == 3 && poov.getUnusedNum() == 3) {
				PokerOfOnePlay poop = new PokerOfOnePlay(poov.getValue(), DoudizhuRule.Santiao);
				poop.add(poov);
				sanZhang.put(poov.getValue(), poop);
				// 设置已经使用标志（为了后面判断散牌和对子）
				for (Poker p : poov.getPokers()) {
					p.setUsed(true);
				}
				continue;
			}
		}
	}

	/**
	 * 添加三张
	 */
	private void addAllSanZhang() {
		for (PokerOfOneValue poov : this.getPokers().values()) {
			// 添加三张
			if (poov.getUnusedNum() != 3) {
				continue;
			}

			PokerOfOnePlay poop = new PokerOfOnePlay(poov.getValue(), DoudizhuRule.Santiao);
			poop.add(poov);
			sanZhang.put(poov.getValue(), poop);
		}
	}

	/**
	 * 添加三张，四张，炸弹
	 */
	private void addSanZhangAndSiZhangAndZhadan() {
		for (PokerOfOneValue poov : this.getPokers().values()) {
			// 添加三张
			// if(poov.PokerNum() == 3){
			// PokerOfOnePlay poop = new
			// PokerOfOnePlay(poov.getValue(),DoudizhuRule.Santiao);
			// poop.add(poov);
			// sanZhang.put(poov.getValue(), poop);
			// //设置已经使用标志（为了后面判断散牌和对子）
			// for (Poker p : poov.getPokers()){
			// p.setUsed(true);
			// }
			// continue;
			// }
			// 添加四张和炸弹
			if (poov.PokerNum() == 4) {
				PokerOfOnePlay poop = new PokerOfOnePlay(poov.getValue(), DoudizhuRule.zhadan);
				poop.add(poov);
				zhaDan.put(poov.getValue(), poop);

				PokerOfOnePlay poop2 = new PokerOfOnePlay(poov.getValue(), DoudizhuRule.siZhang);
				poop2.add(poov);
				siZhang.put(poov.getValue(), poop2);
				// 设置已经使用标志（为了后面判断散牌和对子）
				for (Poker p : poov.getPokers()) {
					p.setUsed(true);
				}
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
				if (pokers.containsKey(maxValue) && pokers.get(maxValue).PokerNum() >= 3) {
					// 多对
					PokerOfOnePlay poop = new PokerOfOnePlay(maxValue, DoudizhuRule.feiji);
					for (int j = i; j <= maxValue; ++j) {
						poop.add(pokers.get(j));

					}
					this.setUsedState(poop.getOnePlayIgnoreUsedState(), true);
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

	@SuppressWarnings("unused")
	private void printUsedState() {

		System.out.println("UsedState:");
		for (PokerOfOneValue poov : this.getPokers().values()) {
			System.out.println("    poov.getValue() == " + poov.getValue() + ":::");
			for (Poker p : poov.getPokers()) {

				System.out.println("         p.getValue() == " + p.getValue() + "; p.getStyle()==" + p.getStyle() + ";p.UsedState==" + p.isUsed());
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
	public List<List<Poker>> getTiShi(List<Poker> otherPokers) {
		List<List<Poker>> myPlay = new ArrayList<List<Poker>>();
		// List<List<Poker>> myPlay = new ArrayList<List<Poker>>();
		if (otherPokers == null) {

			this.isInitiative = true;
			myPlay.add(getInitiativeTiShi());
			return myPlay;
		}

		this.isInitiative = false;

		// printUsedState();
		clearUsedState();
		// printUsedState();
		// 指向某个牌型数组的指针
		Map<Integer, PokerOfOnePlay> play = null;
		// 附带牌（带单或者带对）的数量，
		int attachmentCount = 0;
		// 带单牌还是带对子
		boolean takeSanPai = true;
		// 牌的类型
		int type = DoudizhuRule.checkpai(otherPokers);
		// 对手牌最大
		int otherMaxValue = DoudizhuRule.getMaxNumber(otherPokers);

		if (this.pokerNum >= otherPokers.size()) {
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

				play = feiJi.get(otherPokers.size() / 3);
				break;
			case DoudizhuRule.feijidaisan:
				play = feiJi.get(otherPokers.size() / 4);
				attachmentCount = otherPokers.size() / 4;
				break;
			case DoudizhuRule.feijidaidui:
				play = feiJi.get(otherPokers.size() / 5);
				attachmentCount = otherPokers.size() / 5;
				takeSanPai = false;
				break;
			case DoudizhuRule.liandui:
				play = lianDui.get(otherPokers.size() / 2);

				break;
			case DoudizhuRule.shunzi:
				play = shunZi.get(otherPokers.size());
				break;
			}
		}
		if (play == null) {
			System.out.println("检查对手牌，该牌类型为" + type);
			return null;
		}

		List<Poker> play1 = null;
		List<Poker> play2 = null;
		if (play != null) {
			for (int i = otherMaxValue + 1; i < 18; ++i) {
				if (!play.containsKey(i)) {
					continue;
				}
				List<Poker> ret = new ArrayList<Poker>();
				// printUsedState();
				PokerOfOnePlay poop = play.get(i);

				if (poop.getMaxValue() > otherMaxValue) {
					play1 = poop.getOnePlay();
					if (play1.size() == 0) {
						continue;
					}
					setUsedState(play1, true);
					if (attachmentCount > 0) {
						play2 = getAttachment(attachmentCount, takeSanPai);

						if (play2 == null) {
							setUsedState(play1, false);
							continue;
						}
						setUsedState(play2, true);
					}
					printPokers(play1, play2);

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
					myPlay.add(ret);
				}
			}

			clearUsedState();
			if (play != zhaDan && isZhaDan) {
				for (int i = 3; i < 18; ++i) {
					// 如果有炸弹，
					if (zhaDan.containsKey(i)) {
						List<Poker> ret = new ArrayList<Poker>();
						ret = zhaDan.get(i).getOnePlay();

						if (ret.size() > 0) {
							myPlay.add(ret);
						}
					}
				}

			}

		}
		return myPlay;
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
		System.out.print("一手牌:");
		if (play1 != null) {
			for (int i = 0; i < play1.size(); ++i) {
				System.out.print(play1.get(i).getValue() + " ");
			}
		}

		if (play2 != null) {
			for (int i = 0; i < play2.size(); ++i) {
				System.out.print(play2.get(i).getValue() + " ");
			}
		}
	}

	/**
	 * 取主动出牌的提示
	 * 
	 * @param otherPokers
	 *            对手牌
	 * @return
	 */
	private List<Poker> getInitiativeTiShi() {
		this.clearUsedState();
		List<Poker> oldPlay = null;
		List<Poker> play = null;
		List<Poker> oldAttachment = null;
		List<Poker> attachment = null;
		int oldPoint = 0;
		int point = 0;
		boolean firstLoop = true;

		if (dzCount == 1) {
			isdzBaodan = true;
		}
		if (dzCount == 2) {
			isdzBaodui = true;
		}
		if (pmCount(this.otherCount) == 1) {
			ispmBaodan = true;
		}
		if (pmCount(this.otherCount) == 2) {
			ispmBaodui = true;
		}
		if (isdzBaodan) {
			isdzBaodui = false;
		}
		if (ispmBaodan) {
			ispmBaodui = false;
		}

		// 如果只有炸弹
		if (pokerNum == 2) {
			for (int i = 16; i < 18; ++i) {
				if (zhaDan.containsKey(i)) {
					oldPlay = zhaDan.get(i).getOnePlay();
					break;
				}
			}
			if (oldPlay != null && oldPlay.size() != 0)
				return oldPlay;
		}
		if (pokerNum == 4) {
			for (int i = 3; i < 16; ++i) {
				if (zhaDan.containsKey(i)) {
					oldPlay = zhaDan.get(i).getOnePlay();
					break;
				}
			}

			if (oldPlay != null && oldPlay.size() != 0)
				return oldPlay;

		}

		// 遍历手中所有手牌，计算出得分最高的牌

		// 飞机
		for (Map<Integer, PokerOfOnePlay> nFeiJi : feiJi.values()) {
			for (int j = 4; j < 15; ++j) {
				setNowPlayingAttachment(null);
				if (!nFeiJi.containsKey(j)) {
					continue;
				}
				PokerOfOnePlay poop = nFeiJi.get(j);

				play = poop.getOnePlay();
				if (play.size() == 0) {
					continue;
				}
				this.setUsedState(play, true);
				nowPlaying = play;

				// 带散、带对的情况
				for (int i = 1; i < 3; ++i) {
					attachment = this.getAttachment(play.size() / 3, i % 2 == 1 ? false : true);
					if (attachment == null) {
						continue;
					}
					this.setUsedState(attachment, true);
					setNowPlayingAttachment(attachment);
					printPokers(nowPlaying, nowPlayingAttachment);
					// 计算得分
					point = 0;
					// for (Strategy s : strategys) {
					// switch (s.check()) {
					// case 0:
					// // 此策略器不适合使用
					// break;
					// case 1:
					// // 计算得分并累加
					// point += s.getPoint();
					// break;
					// case 2:
					// // 直接处理，返回结果
					// oldPlay = s.handler();
					// return oldPlay;
					// }
					// }
					System.out.println("总得分：" + point);
					if (firstLoop) {
						firstLoop = false;
						oldPlay = play;
						oldPoint = point;
						oldAttachment = attachment;
					}
					if (point > oldPoint) {
						oldPlay = play;
						oldPoint = point;
						oldAttachment = attachment;
					}
					this.setUsedState(attachment, false);
				}

				// 不带散、不带对的情况
				// 计算得分
				setNowPlayingAttachment(null);
				printPokers(nowPlaying, nowPlayingAttachment);
				point = 0;
				// for (Strategy s : strategys) {
				// switch (s.check()) {
				// case 0:
				// // 此策略器不适合使用
				// break;
				// case 1:
				// // 计算得分并累加
				// point += s.getPoint();
				// break;
				// case 2:
				// // 直接处理，返回结果
				// oldPlay = s.handler();
				// return oldPlay;
				// }
				// }
				System.out.println("总得分：" + point);
				if (firstLoop) {
					firstLoop = false;
					oldPlay = play;
					oldPoint = point;
					oldAttachment = null;
				}
				if (point > oldPoint) {
					oldPlay = play;
					oldPoint = point;
					oldAttachment = null;
				}

				this.setUsedState(play, false);
			}
		}

		setNowPlayingAttachment(null);
		// 连对
		for (Map<Integer, PokerOfOnePlay> nLianDui : lianDui.values()) {
			for (int j = 5; j < 15; ++j) {
				if (!nLianDui.containsKey(j)) {
					continue;
				}
				PokerOfOnePlay poop = nLianDui.get(j);
				play = poop.getOnePlay();
				if (play.size() == 0) {
					continue;
				}
				this.setUsedState(play, true);
				nowPlaying = play;
				printPokers(nowPlaying, nowPlayingAttachment);
				// 不带散、不带对的情况
				// 计算得分
				point = 0;
				// for (Strategy s : strategys) {
				// switch (s.check()) {
				// case 0:
				// // 此策略器不适合使用
				// break;
				// case 1:
				// // 计算得分并累加
				// point += s.getPoint();
				// break;
				// case 2:
				// // 直接处理，返回结果
				// oldPlay = s.handler();
				// return oldPlay;
				// }
				// }
				System.out.println("总得分：" + point);
				if (firstLoop) {
					firstLoop = false;
					oldPlay = play;
					oldPoint = point;
					oldAttachment = null;
				}
				if (point > oldPoint) {
					oldPlay = play;
					oldPoint = point;
					oldAttachment = null;
				}
				this.setUsedState(play, false);
			}
		}

		setNowPlayingAttachment(null);
		// 顺子
		// for(Map<Integer,PokerOfOnePlay> nShunZi : shunZi.values()){
		for (int k = 12; k >= 5; --k) {
			Map<Integer, PokerOfOnePlay> nShunZi = shunZi.get(k);
			for (int j = 7; j < 15; ++j) {
				if (!nShunZi.containsKey(j)) {
					continue;
				}
				PokerOfOnePlay poop = nShunZi.get(j);
				play = poop.getOnePlay();
				if (play.size() == 0) {
					continue;
				}
				this.setUsedState(play, true);
				nowPlaying = play;
				printPokers(nowPlaying, nowPlayingAttachment);
				// 不带散、不带对的情况
				// 计算得分
				point = 0;
				// for (Strategy s : strategys) {
				// switch (s.check()) {
				// case 0:
				// // 此策略器不适合使用
				// break;
				// case 1:
				// // 计算得分并累加
				// point += s.getPoint();
				// break;
				// case 2:
				// // 直接处理，返回结果
				// oldPlay = s.handler();
				// return oldPlay;
				// }
				// }
				System.out.println("总得分：" + point);
				if (firstLoop) {
					firstLoop = false;
					oldPlay = play;
					oldPoint = point;
					oldAttachment = null;
				}
				if (point > oldPoint) {
					oldPlay = play;
					oldPoint = point;
					oldAttachment = null;
				}
				this.setUsedState(play, false);
			}
		}

		// setNowPlayingAttachment(null);
		// 三张
		for (int j = 3; j < 16; ++j) {
			if (!sanZhang.containsKey(j)) {
				continue;
			}
			setNowPlayingAttachment(null);
			PokerOfOnePlay poop = sanZhang.get(j);
			play = poop.getOnePlay();
			if (play.size() == 0) {
				continue;
			}
			this.setUsedState(play, true);
			nowPlaying = play;

			// 带散、带对的情况
			for (int i = 1; i < 3; ++i) {
				attachment = this.getAttachment(1, i % 2 == 1 ? false : true);
				if (attachment == null) {
					continue;
				}
				this.setUsedState(attachment, true);
				setNowPlayingAttachment(attachment);
				printPokers(nowPlaying, nowPlayingAttachment);
				// 计算得分
				point = 0;
				// for (Strategy s : strategys) {
				// switch (s.check()) {
				// case 0:
				// // 此策略器不适合使用
				// break;
				// case 1:
				// // 计算得分并累加
				// point += s.getPoint();
				// break;
				// case 2:
				// // 直接处理，返回结果
				// oldPlay = s.handler();
				// return oldPlay;
				// }
				// }
				System.out.println("总得分：" + point);
				if (firstLoop) {
					firstLoop = false;
					oldPlay = play;
					oldPoint = point;
					oldAttachment = attachment;
				}
				if (point > oldPoint) {
					oldPlay = play;
					oldPoint = point;
					oldAttachment = attachment;
				}
				this.setUsedState(attachment, false);
			}
			setNowPlayingAttachment(null);
			printPokers(nowPlaying, nowPlayingAttachment);
			// 计算得分
			point = 0;
			// for (Strategy s : strategys) {
			// switch (s.check()) {
			// case 0:
			// // 此策略器不适合使用
			// break;
			// case 1:
			// // 计算得分并累加
			// point += s.getPoint();
			// break;
			// case 2:
			// // 直接处理，返回结果
			// oldPlay = s.handler();
			// return oldPlay;
			// }
			// }
			System.out.println("总得分：" + point);
			if (firstLoop) {
				firstLoop = false;
				oldPlay = play;
				oldPoint = point;
				oldAttachment = null;
			}
			if (point > oldPoint) {
				oldPlay = play;
				oldPoint = point;
				oldAttachment = null;
			}
			this.setUsedState(play, false);
		}

		setNowPlayingAttachment(null);
		// 对子
		for (int i = 3; i < 16; ++i) {
			if (!duiZi.containsKey(i)) {
				continue;
			}
			PokerOfOnePlay poop = duiZi.get(i);
			play = poop.getOnePlay();
			if (play.size() == 0) {
				continue;
			}
			this.setUsedState(play, true);
			nowPlaying = play;

			printPokers(nowPlaying, nowPlayingAttachment);
			// 计算得分
			// 如果地主只有2个牌，打对先扣50分;
			if (mineorder != dzorder.intValue() && isdzBaodui) {
				point = -50;
			}
			// 如果自己是地主，平民报双了，出对扣50分
			else if (mineorder == dzorder.intValue() && ispmBaodui) {
				point = -50;
			} else {
				point = 0;
			}
			// for (Strategy s : strategys) {
			// switch (s.check()) {
			// case 0:
			// // 此策略器不适合使用
			// break;
			// case 1:
			// // 计算得分并累加
			// point += s.getPoint();
			// break;
			// case 2:
			// // 直接处理，返回结果
			// oldPlay = s.handler();
			// return oldPlay;
			// }
			// }
			System.out.println("总得分：" + point);
			if (firstLoop) {
				firstLoop = false;
				oldPlay = play;
				oldPoint = point;
			}
			if (point > oldPoint) {
				oldPlay = play;
				oldPoint = point;
				oldAttachment = null;
			}
			this.setUsedState(play, false);
		}

		setNowPlayingAttachment(null);
		// 散牌
		for (int i = 3; i < 18; ++i) {
			if (!sanPai.containsKey(i)) {
				continue;
			}
			PokerOfOnePlay poop = sanPai.get(i);
			play = poop.getOnePlay();
			if (play.size() == 0) {
				continue;
			}
			this.setUsedState(play, true);
			nowPlaying = play;
			printPokers(nowPlaying, nowPlayingAttachment);
			// 如果地主报单，出单牌先扣50分，

			if (mineorder != dzorder.intValue() && isdzBaodan) {
				point = -50;
			}
			// 如果自己是地主，有平民报单了，出单牌扣50分
			else if (mineorder == dzorder.intValue() && ispmBaodan) {
				point = -50;
			} else if (mineorder != dzorder.intValue() && ispmBaodan && !isdzBaodan) {
				// 打牌伙伴只剩下一张牌，同时地主不止一张牌的情况下，我要打一张散牌，看看伙伴能不能过，如果不能过，下一把我就只管打自己的牌，不再打散牌，
				if (!hasPlayOneSanPai) {
					for (int p = 3; p < 13; ++p) {
						if (this.pokers.containsKey(p)) {
							PokerOfOneValue pv = this.pokers.get(p);
							if (pv.getPokers().size() > 0) {
								hasPlayOneSanPai = true;
								return pv.getPokerIgnoreUsedState(1);
							}
						}
					}
				}

			} else {
				point = 0;
			}
			// for (Strategy s : strategys) {
			// switch (s.check()) {
			// case 0:
			// // 此策略器不适合使用
			// break;
			// case 1:
			// // 计算得分并累加
			// point += s.getPoint();
			// break;
			// case 2:
			// // 直接处理，返回结果
			// oldPlay = s.handler();
			// return oldPlay;
			// }
			// }
			System.out.println("总得分：" + point);
			if (firstLoop) {
				firstLoop = false;
				oldPlay = play;
				oldPoint = point;
			}
			if (point > oldPoint) {
				oldPlay = play;
				oldPoint = point;
				oldAttachment = null;
			}
			this.setUsedState(play, false);
		}

		setNowPlayingAttachment(null);
		// 炸弹
		for (int j = 3; j < 18; ++j) {
			if (!zhaDan.containsKey(j)) {
				continue;
			}
			PokerOfOnePlay poop = zhaDan.get(j);
			play = poop.getOnePlay();
			if (play.size() == 0) {
				continue;
			}
			this.setUsedState(play, true);
			nowPlaying = play;
			printPokers(nowPlaying, nowPlayingAttachment);
			if (this.pokerNum > 10) {
				point = -30;
			} else {
				point = 0;
			}
			// for (Strategy s : strategys) {
			// switch (s.check()) {
			// case 0:
			// // 此策略器不适合使用
			// break;
			// case 1:
			// // 计算得分并累加
			// point += s.getPoint();
			// break;
			// case 2:
			// // 直接处理，返回结果
			// oldPlay = s.handler();
			// return oldPlay;
			// }
			// }
			System.out.println("总得分：" + point);
			if (firstLoop) {
				firstLoop = false;
				oldPlay = play;
				oldPoint = point;
			}
			if (point > oldPoint) {
				oldPlay = play;
				oldPoint = point;
				oldAttachment = null;
			}
			this.setUsedState(play, false);
		}

		// setNowPlayingAttachment(null);
		// 四张
		/*
		 * for(int j = 3; j<16; ++j){ if (!siZhang.containsKey(j)){ continue; }
		 * PokerOfOnePlay poop = siZhang.get(j); play = poop.getOnePlay(); if
		 * (play.size() == 0){ continue; } this.setUsedState(play, true);
		 * nowPlaying = play; //带散、带对的情况 for (int i = 1; i < 3; ++i){ attachment
		 * = this.getAttachment(2, i % 2 == 1 ? true : false); if (attachment ==
		 * null){ continue; } this.setUsedState(attachment, true);
		 * setNowPlayingAttachment(attachment); printPokers(nowPlaying,
		 * nowPlayingAttachment); //计算得分 point = 0; for (Strategy s :
		 * strategys){ switch(s.check()){ case 0: //此策略器不适合使用 break; case 1:
		 * //计算得分并累加 point += s.getPoint(); break; case 2: //直接处理，返回结果 oldPlay =
		 * s.handler(); return oldPlay; } } System.out.println("总得分：" + point);
		 * if (firstLoop){ firstLoop = false; oldPlay = play; oldPoint = point;
		 * oldAttachment = null; } if (point > oldPoint){ oldPlay = play;
		 * oldPoint = point; oldAttachment = attachment; }
		 * this.setUsedState(attachment, false); } this.setUsedState(play,
		 * false); }
		 */
		if (oldPlay != null && oldAttachment != null) {
			oldPlay.addAll(oldAttachment);

		}
		// 如果地主报单但是手中只有单牌
		if (oldPlay != null && mineorder != dzorder.intValue() && isdzBaodan && DoudizhuRule.checkpai(oldPlay) == 1) {
			for (int i = 17; i > 2; --i) {
				if (!sanPai.containsKey(i)) {
					continue;
				}
				PokerOfOnePlay poop = sanPai.get(i);
				oldPlay = poop.getOnePlay();
				break;
			}

		}
		// 如果自己是地主，平民报单且自己只有单牌
		if (oldPlay != null && mineorder == dzorder.intValue() && ispmBaodan && DoudizhuRule.checkpai(oldPlay) == 1) {
			for (int i = 17; i > 2; --i) {
				if (!sanPai.containsKey(i)) {
					continue;
				}
				PokerOfOnePlay poop = sanPai.get(i);
				oldPlay = poop.getOnePlay();
				break;
			}

		}

		return oldPlay;
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
		List<PokerOfOnePlay> poops = new ArrayList<PokerOfOnePlay>();
		if (takeSanPai) {

			for (int i = 3; i < 18; i++) {
				if (sanPai.containsKey(i)) {
					poops.add(sanPai.get(i));
				}

			}
			// Object[] poops = sanPai.values().toArray();
			// 带散牌
			// Arrays.sort(poops);
			for (int i = 0; i < poops.size(); ++i) {

				PokerOfOnePlay poop = (PokerOfOnePlay) poops.get(i);
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

			for (int i = 3; i < 18; i++) {
				if (duiZi.containsKey(i)) {
					poops.add(duiZi.get(i));
				}
			}
			Poker dui = null;
			// 在对子中取单牌
			for (int i = 0; i < poops.size(); ++i) {
				PokerOfOnePlay poop = (PokerOfOnePlay) poops.get(i);
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
			for (int i = 3; i < 18; ++i) {
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
				// int n = pokers.get(i).getUnusedNum();
				// if (n == 4) {
				// continue;
				// }
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

	// public List<Strategy> getStrategys() {
	// return strategys;
	// }

	// public void setStrategys(List<Strategy> strategys) {
	// this.strategys = strategys;
	// }

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
	 * 获取提示，otherPokers是已经从大到小排好序的牌
	 * 
	 * @param otherPokers
	 *            其地主的一手牌,如果是null，则主动出牌
	 * @return 要打出去的数组
	 */
	public List<List<Poker>> getTiShi() {
		List<List<Poker>> myPlay = new ArrayList<List<Poker>>();
		for (int i = 3; i < 18; ++i) {
			//散牌
			if (sanPai.containsKey(i)) {
				PokerOfOnePlay danpai = sanPai.get(i);
				if (danpai != null) {
					List<Poker> danplay = danpai.getOnePlay();
					if (danplay.size() != 0) {
						myPlay.add(danplay);
					}
				}

			}
			if (i >= 16) {
				if (sanPai.containsKey(16) && sanPai.containsKey(17)) {
					//炸弹
					for (int j = 16; j < 18; ++j) {
						if (zhaDan.containsKey(j)) {
							PokerOfOnePlay poop = zhaDan.get(j);
							List<Poker> play = poop.getOnePlay();
							if (play != null && play.size() != 0) {
								if (!myPlay.contains(play)) {
									myPlay.add(play);

								}
							}
						}
					}

				}
				if (sanPai.containsKey(i)) {
					PokerOfOnePlay danpai = sanPai.get(i);
					if (danpai != null) {
						List<Poker> danplay = danpai.getOnePlay();
						if (danplay.size() != 0) {
							myPlay.add(danplay);
						}
					}
				}
			}

			//对子
			if (duiZi.containsKey(i)) {
				PokerOfOnePlay yidui = duiZi.get(i);
				if (yidui != null) {
					List<Poker> duiplay = yidui.getOnePlay();
					if (duiplay.size() != 0) {
						myPlay.add(duiplay);
					}
				}

			}

			//三带单
			clearUsedState();
			if (sanZhang.containsKey(i)) {
				PokerOfOnePlay sanzhang = sanZhang.get(i);
				if (sanzhang != null) {
					List<Poker> sanplay = sanzhang.getOnePlay();
					if (sanplay != null && sanplay.size() != 0) {

						for (int j = 3; j < 18; j++) {
							List<Poker> ret = new ArrayList<Poker>();
							ret.addAll(sanplay);
							if (sanPai.containsKey(j)) {
								PokerOfOnePlay atachment = sanPai.get(j);
								if (atachment != null) {
									List<Poker> daiplay = atachment.getOnePlay();
									ret.addAll(daiplay);
									myPlay.add(ret);
								}

							}

						}

					}

				}

			}
			//三带对
			clearUsedState();
			if (sanZhang.containsKey(i)) {
				PokerOfOnePlay sanzhang = sanZhang.get(i);
				if (sanzhang != null) {
					List<Poker> sanplay = sanzhang.getOnePlay();
					if (sanplay != null && sanplay.size() != 0) {

						for (int j = 3; j < 18; j++) {
							if (duiZi.containsKey(j)) {
								List<Poker> ret = new ArrayList<Poker>();
								ret.addAll(sanplay);
								PokerOfOnePlay atachment = duiZi.get(j);
								if (atachment != null) {
									List<Poker> daiplay = atachment.getOnePlay();
									ret.addAll(daiplay);
									myPlay.add(ret);
								}
							}
						}
					}
				}

			}
			clearUsedState();
			//三张
			if (sanZhang.containsKey(i)) {
				PokerOfOnePlay sanzhang = sanZhang.get(i);
				if (sanzhang != null) {
					List<Poker> sanplay = sanzhang.getOnePlay();
					if (sanplay != null && sanplay.size() != 0) {
						myPlay.add(sanplay);
					}
				}

			}

			//			this.clearUsedState();
			//			List<Poker> oldPlay = null;
			//			List<Poker> play = null;
			//			List<Poker> oldAttachment = null;
			//			List<Poker> attachment = null;
			//			int oldPoint = 0;
			//			int point = 0;
			//			boolean firstLoop = true;

			// 飞机带单
			clearUsedState();
			for (int k = 6; k >= 2; --k) {
				Map<Integer, PokerOfOnePlay> nFeiJi = feiJi.get(k);
				for (int j = 4; j < 15; ++j) {
					if (nFeiJi.containsKey(j)) {
						PokerOfOnePlay poop = nFeiJi.get(j);
						List<Poker> play = poop.getOnePlay();
						if (play.size() != 0) {
							this.setUsedState(play, true);
							nowPlaying = play;

							// 带散、带对的情况
							List<Poker> attachment = this.getAttachment(play.size() / 3, true);
							if (attachment == null) {
								continue;
							}
							this.setUsedState(attachment, true);
							setNowPlayingAttachment(attachment);
							printPokers(nowPlaying, nowPlayingAttachment);
							List<Poker> oldPlay = play;
							List<Poker> oldAttachment = attachment;

							if (oldPlay != null && oldAttachment != null) {
								oldPlay.addAll(oldAttachment);
							}

							if (oldPlay != null && oldPlay.size() != 0) {
								boolean contans = false;
								for (int a = 0; a < play.size(); a++) {
									if (play.get(a).getValue() == i) {
										contans = true;
									}
								}
								if (contans) {
									if (!myPlay.contains(oldPlay)) {
										myPlay.add(oldPlay);
									}
								}
							}
							this.setUsedState(attachment, false);
						}

					}
				}

			}

			// 飞机带对
			clearUsedState();
			for (int k = 6; k >= 2; --k) {
				Map<Integer, PokerOfOnePlay> nFeiJi = feiJi.get(k);
				for (int j = 4; j < 15; ++j) {
					if (nFeiJi.containsKey(j)) {
						PokerOfOnePlay poop = nFeiJi.get(j);
						List<Poker> play = poop.getOnePlay();
						if (play.size() != 0) {
							this.setUsedState(play, true);
							nowPlaying = play;

							// 带散、带对的情况
							List<Poker> attachment = this.getAttachment(play.size() / 3, false);
							if (attachment == null) {
								continue;
							}
							this.setUsedState(attachment, true);
							setNowPlayingAttachment(attachment);
							printPokers(nowPlaying, nowPlayingAttachment);
							List<Poker> oldPlay = play;
							List<Poker> oldAttachment = attachment;

							if (oldPlay != null && oldAttachment != null) {
								oldPlay.addAll(oldAttachment);
							}

							if (oldPlay != null && oldPlay.size() != 0) {
								boolean contans = false;
								for (int a = 0; a < play.size(); a++) {
									if (play.get(a).getValue() == i) {
										contans = true;
									}
								}
								if (contans) {
									if (!myPlay.contains(oldPlay)) {
										myPlay.add(oldPlay);
									}
								}
							}
							this.setUsedState(attachment, false);
						}
					}

				}
			}
			// 飞机不带
			clearUsedState();
			for (int k = 6; k >= 2; --k) {
				Map<Integer, PokerOfOnePlay> nFeiJi = feiJi.get(k);
				for (int j = 4; j < 15; ++j) {
					if (nFeiJi.containsKey(j)) {
						PokerOfOnePlay poop = nFeiJi.get(j);
						List<Poker> play = poop.getOnePlay();
						if (play.size() != 0) {
							this.setUsedState(play, true);
							List<Poker> oldPlay = play;
							nowPlaying = play;
							if (oldPlay != null && oldPlay.size() != 0) {
								boolean contans = false;
								for (int a = 0; a < play.size(); a++) {
									if (play.get(a).getValue() == i) {
										contans = true;
									}
								}
								if (contans) {
									if (!myPlay.contains(oldPlay)) {
										myPlay.add(oldPlay);
									}
								}
							}
						}

					}
				}
			}

			setNowPlayingAttachment(null);
			clearUsedState();
			// 顺子
			for (int k = 12; k >= 5; --k) {
				Map<Integer, PokerOfOnePlay> nShunZi = shunZi.get(k);
				for (int j = 7; j < 15; ++j) {
					if (nShunZi.containsKey(j)) {
						PokerOfOnePlay poop = nShunZi.get(j);
						List<Poker> shunz = poop.getOnePlay();
						if (shunz.size() != 0) {
							boolean contans = false;
							for (int a = 0; a < shunz.size(); a++) {
								if (shunz.get(a).getValue() == i) {
									contans = true;
								}
							}
							if (contans) {
								if (!myPlay.contains(shunz)) {
									myPlay.add(shunz);
								}
							}
							//							this.setUsedState(shunz, false);
						}
						;
					}

				}
			}
			//连队
			clearUsedState();
			for (int k = 10; k >= 3; --k) {
				Map<Integer, PokerOfOnePlay> nLianDui = lianDui.get(k);
				for (int j = 5; j < 15; ++j) {
					if (nLianDui.containsKey(j)) {
						PokerOfOnePlay poop = nLianDui.get(j);
						List<Poker> liandui = poop.getOnePlay();
						if (liandui.size() != 0) {
							boolean contans = false;
							for (int a = 0; a < liandui.size(); a++) {
								if (liandui.get(a).getValue() == i) {
									contans = true;
								}
							}
							if (contans) {
								if (!myPlay.contains(liandui)) {
									myPlay.add(liandui);
								}
							}
							//							this.setUsedState(shunz, false);
						}
						;
					}

				}
			}
		}

		//炸弹
		for (int j = 3; j < 16; ++j) {
			if (zhaDan.containsKey(j)) {
				PokerOfOnePlay poop = zhaDan.get(j);

				List<Poker> play = poop.getOnePlay();
				if (play != null && play.size() != 0) {
					if (!myPlay.contains(play)) {
						myPlay.add(play);
					}
				}
			}
		}

		//		clearUsedState();
		// 指向某个牌型数组的指针
		return myPlay;
	}

}
