package com.lordcard.ui.personal.logic;

import java.util.List;

import com.lordcard.entity.Poker;

public class CallUtil {
	public static int callPoint(List<Poker> myPokers) {
		int grab3 = Integer.parseInt(ConfigUtils.get("DiZhuClient.robot.grab.3"));
		int grab2 = Integer.parseInt(ConfigUtils.get("DiZhuClient.robot.grab.2"));
		int grab1 = Integer.parseInt(ConfigUtils.get("DiZhuClient.robot.grab.1"));

		//决定是否叫地主
		DouDiZhuLogic ddzData = new DouDiZhuLogic(myPokers);
		int score = ddzData.getScore();
		int fen = 0;

		if (score > grab3) {
			fen = 3;
		} else if (score > grab2) {
			fen = 2;
		} else if (score > grab1) {
			fen = 1;
		} else {
			fen = 0;

		}
		return fen;
	}
}
