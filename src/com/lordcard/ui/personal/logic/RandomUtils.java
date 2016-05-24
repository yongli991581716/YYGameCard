package com.lordcard.ui.personal.logic;

import java.util.Random;

public class RandomUtils {
	public static int getOrder( int playCount) {
		Random rd = new Random();
		int num = rd.nextInt(playCount);
		return num;
	}
}
