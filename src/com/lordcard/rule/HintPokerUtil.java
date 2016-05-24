package com.lordcard.rule;

import java.util.List;

import com.lordcard.entity.Poker;

/**
 * 提示操作工具类 model.HintHelper
 * 
 * @author Administrator <br/>
 *         create at 2013 2013-3-19 下午2:25:41
 */
public class HintPokerUtil {

	/**
	 * 过滤提示的数据
	 * 
	 * @param tishiList
	 * @param tishiList2
	 * @return
	 */
	public List<List<Poker>> filterHintPoker(List<List<Poker>> tishiList, List<List<Poker>> tishiList2) {

		if (tishiList.size() == 0) {
			tishiList = tishiList2;
		} else {
			for (int i = 0; i < tishiList2.size(); i++) {
				if (!tishiList.contains(tishiList2.get(i))) {
					tishiList.add(tishiList2.get(i));
				}
			}
		}
		return tishiList;
	}
}
