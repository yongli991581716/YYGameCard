package com.lordcard.ui.personal.logic.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lordcard.entity.Poker;
import com.lordcard.ui.personal.logic.DouDiZhuLogic;
import com.lordcard.ui.personal.logic.DoudizhuRule;
import com.lordcard.ui.personal.logic.FillDiZhuData;
import com.lordcard.ui.personal.logic.PokerOfOnePlay;
import com.lordcard.ui.personal.logic.PokerUtil;
import com.lordcard.ui.personal.logic.strategy.interfaces.Strategy;



/**
 * 此策略计算某手牌出后，自己手中的牌是否能够再吃回来，目前算法是相同牌型，如果有比当前牌大5的，就加分，加分策略为，比当前牌（最大的牌）的value大多少，就加多少个点
 * @author lenovo-win7
 *
 */
public class StrategyBigPoker implements Strategy {

	DouDiZhuLogic ddzData = null;
	private Poker[] allPokers = PokerUtil.getPoker(); //
	private List<Poker> allofPokers = new ArrayList<Poker>();; //没出过的所有牌

	public StrategyBigPoker(DouDiZhuLogic ddzData) {
		this.ddzData = ddzData;
	}

	@Override
	public int check() {
		return 1;
	}

	@Override
	public int getPoint() {
		if (ddzData.getNowPlaying() == null) {
			return 0;
		}
		List<Poker> playedPokers = ddzData.getAllPlayedPoker();
		int[] pos = new int[playedPokers.size()];
		;
		for (int i = 0; i < playedPokers.size(); i++) {
			pos[i] = playedPokers.get(i).getNumber();
		}

		int paixu[] = DoudizhuRule.sort(pos, allPokers);
		for (int i = 0; i < paixu.length; i++) {
			Poker card = allPokers[paixu[i]];
			allofPokers.add(card);
		}
		for (int j = 0; j < playedPokers.size(); j++) {
			if (allofPokers.contains(playedPokers.get(j))) {
				allofPokers.remove(playedPokers.get(j));
			}
		}

		List<Poker> ps = sort(ddzData.getNowPlaying(), ddzData.getNowPlayingAttachment());
		int type = DoudizhuRule.checkpai(ps);

		if (type == DoudizhuRule.zhadan || type == DoudizhuRule.wangzha) {
			return 0;
		}
		int maxValue = max(ddzData.getNowPlaying());
		Map<Integer, PokerOfOnePlay> play = PokerUtil.whichArray(ddzData, ps.size(), type);
		// 
		for (int i = 17; i >= maxValue; --i) {
			if (play.containsKey(i)) {
				FillDiZhuData dizhuData = new FillDiZhuData(allofPokers);

				List<Poker> pok = play.get(i).getOnePlay();
				if (pok != null && pok.size() > 0) {
					if (dizhuData.maxPoker(pok) == null) {
						return 15;
					}
				}
			}
		}

		return 0;

	}

	private int max(List<Poker> nowPlaying) {
		int ret = 0;
		for (Poker p : nowPlaying) {
			if (ret < p.getValue()) {
				ret = p.getValue();
			}
		}
		return ret;
	}

	/**
	 * 将牌从大到小排序
	 * @param ps
	 */
	private List<Poker> sort(List<Poker> playing, List<Poker> attachment) {
		List<Poker> ret = new ArrayList<Poker>();
		List<Poker> ps = new ArrayList<Poker>();

		ps.addAll(playing);
		if (attachment != null) {
			ps.addAll(attachment);
		}
		Poker[] pokers = new Poker[ps.size()];

		for (int i = 0; i < pokers.length; ++i) {
			pokers[i] = ps.get(i);
		}

		for (int i = 0; i < pokers.length; ++i) {
			for (int j = i + 1; j < pokers.length; ++j) {
				if (pokers[i].getValue() < pokers[j].getValue()) {
					//交换
					Poker tmp = pokers[i];
					pokers[i] = pokers[j];
					pokers[j] = tmp;
				}
			}
		}
		for (int i = 0; i < pokers.length; ++i) {
			ret.add(pokers[i]);
		}
		return ret;
	}

	@Override
	public List<Poker> handler() {
		// TODO Auto-generated method stub
		return null;
	}

}
