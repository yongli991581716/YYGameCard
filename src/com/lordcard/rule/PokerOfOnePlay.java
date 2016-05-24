package com.lordcard.rule;

import java.util.ArrayList;
import java.util.List;

import com.lordcard.entity.Poker;

public class PokerOfOnePlay {

	int type;
	private int maxValue;

	private List<PokerOfOneValue> pokers = new ArrayList<PokerOfOneValue>();

	public PokerOfOnePlay(int maxValue, int paiType) {
		this.maxValue = maxValue;
		this.type = paiType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<PokerOfOneValue> getPokers() {
		return pokers;
	}

	public void setPokers(List<PokerOfOneValue> pokers) {
		this.pokers = pokers;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public void add(PokerOfOneValue poov) {
		pokers.add(poov);
	}

	public List<Poker> getOnePlay() {
		List<Poker> ret = new ArrayList<Poker>();
		switch (type) {
		case DoudizhuRule.Danpai:
			if (pokers.get(0).getUnusedNum() < 1) {
				return new ArrayList<Poker>();
			} else {
				return pokers.get(0).getUnusedPoker(1);
			}
		case DoudizhuRule.Yidui:
			if (pokers.get(0).getUnusedNum() < 2) {
				return new ArrayList<Poker>();
			} else {
				return pokers.get(0).getUnusedPoker(2);
			}
		case DoudizhuRule.Santiao:
			if (pokers.get(0).getUnusedNum() < 3) {
				return new ArrayList<Poker>();
			} else {
				return pokers.get(0).getUnusedPoker(3);
			}
		case DoudizhuRule.zhadan:
			if (pokers.size() == 1 && pokers.get(0).getUnusedNum() == 4) {
				return pokers.get(0).getUnusedPoker(4);
			} else if (pokers.size() == 2 && pokers.get(0).getUnusedNum() == 1 && pokers.get(1).getUnusedNum() == 1) {
				ret.add(pokers.get(0).getOneUnusedPoker());
				ret.add(pokers.get(1).getOneUnusedPoker());
				return ret;
			}
			return new ArrayList<Poker>();
		case DoudizhuRule.siZhang:
			if (pokers.size() == 1 && pokers.get(0).getUnusedNum() == 4) {
				return pokers.get(0).getUnusedPoker(4);
			}
			return new ArrayList<Poker>();
		case DoudizhuRule.shunzi:
			for (PokerOfOneValue poov : pokers) {
				Poker p = poov.getOneUnusedPoker();
				if (p == null) {
					return new ArrayList<Poker>();
				}
				ret.add(p);
			}
			return ret;
		case DoudizhuRule.feiji:
			for (PokerOfOneValue poov : pokers) {
				List<Poker> p = poov.getUnusedPoker(3);
				if (p == null) {
					return new ArrayList<Poker>();
				}
				ret.addAll(p);
			}
			return ret;

		case DoudizhuRule.liandui:
			for (PokerOfOneValue poov : pokers) {
				List<Poker> p = poov.getUnusedPoker(2);
				if (p == null) {
					return new ArrayList<Poker>();
				}
				ret.addAll(p);
			}
			return ret;
		}
		return new ArrayList<Poker>();
	}

	public List<Poker> getOnePlayIgnoreUsedState() {
		List<Poker> ret = new ArrayList<Poker>();
		switch (type) {
		case DoudizhuRule.Danpai:
			if (pokers.get(0).PokerNum() < 1) {
				return new ArrayList<Poker>();
			} else {
				return pokers.get(0).getPokerIgnoreUsedState(1);
			}
		case DoudizhuRule.Yidui:
			if (pokers.get(0).PokerNum() < 2) {
				return new ArrayList<Poker>();
			} else {
				return pokers.get(0).getPokerIgnoreUsedState(2);
			}
		case DoudizhuRule.Santiao:
			if (pokers.get(0).PokerNum() < 3) {
				return new ArrayList<Poker>();
			} else {
				return pokers.get(0).getPokerIgnoreUsedState(3);
			}
		case DoudizhuRule.zhadan:
			if (pokers.size() == 1 && pokers.get(0).PokerNum() == 4) {
				return pokers.get(0).getPokerIgnoreUsedState(4);
			} else if (pokers.size() == 2 && pokers.get(0).PokerNum() == 1 && pokers.get(1).PokerNum() == 1) {
				ret.add(pokers.get(0).getPokers().get(0));
				ret.add(pokers.get(1).getPokers().get(0));
				return ret;
			}
			return new ArrayList<Poker>();
		case DoudizhuRule.siZhang:
			if (pokers.size() == 1 && pokers.get(0).PokerNum() == 4) {
				return pokers.get(0).getPokerIgnoreUsedState(4);
			}
			return new ArrayList<Poker>();
		case DoudizhuRule.shunzi:
			for (PokerOfOneValue poov : pokers) {
				List<Poker> p = poov.getPokerIgnoreUsedState(1);
				if (p == null) {
					return new ArrayList<Poker>();
				}
				ret.addAll(p);
			}
			return ret;
		case DoudizhuRule.feiji:
			for (PokerOfOneValue poov : pokers) {
				List<Poker> p = poov.getPokerIgnoreUsedState(3);
				if (p == null) {
					return new ArrayList<Poker>();
				}
				ret.addAll(p);
			}
			return ret;

		case DoudizhuRule.liandui:
			for (PokerOfOneValue poov : pokers) {
				List<Poker> p = poov.getPokerIgnoreUsedState(2);
				if (p == null) {
					return new ArrayList<Poker>();
				}
				ret.addAll(p);
			}
			return ret;
		}
		return new ArrayList<Poker>();
	}

}
