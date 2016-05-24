package com.lordcard.ui;
interface MatchHander{  
    boolean compare(int a,int b);  
} 
public class Match {

	/**
	 * 09. * 百分之多少之内匹配错误可以接受 10. * a与ab匹配为百分之50的错误率。 11. * @param percent
	 * 设置匹配百分比 12. * @param src 字符串1 13. * @param dest 字符串2 14. * @param hander
	 * 匹配规则 15. * @return 16.
	 */
	public static boolean match(double percent, String src, String dest,
			MatchHander hander) {
		char[] csrc = src.toCharArray();
		char[] cdest = dest.toCharArray();
		double score = 0;
		score = cal(csrc, 0, cdest, 0, hander);
		int max = csrc.length > cdest.length ? csrc.length : cdest.length;
		System.out.println("最小匹配百分比：" + percent + "，成功匹配百分比：" + score / max);
		return score / max > percent;
	}

	/**
	 * 28. * 几个错误的字符可以接受 29. * a与ab为1个字符错误可以接受 30. * @param percent 设置匹配百分比 31.
	 * * @param src 字符串1 32. * @param dest 字符串2 33. * @param hander 匹配规则 34. * @return
	 * 35.
	 */
	public static boolean match(int errorNum, String src, String dest,
			MatchHander hander) {
		char[] csrc = src.toCharArray();
		char[] cdest = dest.toCharArray();
		int score = 0;
		score = cal(csrc, 0, cdest, 0, hander);
		int max = csrc.length > cdest.length ? csrc.length : cdest.length;
		System.out.println("可以接受错误数：" + errorNum + "，发现错误数：" + (max - score));
		return max - score <= errorNum;
	}

	/**
	 * 46. * 2个字符串75%匹配成功返回true 47. * @param src 48. * @param dest 49. * @return
	 * 50.
	 */
	public static boolean match(double percent, String src, String dest) {
		return match(percent, src, dest, new MatchHander() {

			@Override
			public boolean compare(int a, int b) {
				return a == b;
			}
		});
	}

	/**
	 * 60. * 2个字符串错几个字符可以接受 61. * @param errorNum 62. * @param src 63. * @param
	 * dest 64. * @return 65.
	 */
	public static boolean match(int errorNum, String src, String dest) {
		return match(errorNum, src, dest, new MatchHander() {

			@Override
			public boolean compare(int a, int b) {
				return a == b;
			}
		});
	}

	/**
	 * 76. * 使用递归方法匹配字符串 77. * @param csrc 78. * @param i 79. * @param cdest 80.
	 * * @param j 81. * @param hander 82. * @return 83.
	 */
	private static int cal(char[] csrc, int i, char[] cdest, int j,
			MatchHander hander) {
		int score = 0;
		if (i >= csrc.length || j >= cdest.length)
			return 0;
		boolean ismatch = hander.compare(csrc[i], cdest[j]);
		if (ismatch) {
			score++;
			if (i + 1 < csrc.length && j + 1 < cdest.length)
				score += cal(csrc, i + 1, cdest, j + 1, hander);
		} else {
			int temp1 = 0;
			int temp2 = 0;
			int temp3 = 0;
			temp1 += cal(csrc, i, cdest, j + 1, hander);
			temp2 += cal(csrc, i + 1, cdest, j, hander);
			temp3 += cal(csrc, i + 1, cdest, j + 1, hander);
			int temp4 = Math.max(temp1, temp2);
			score += Math.max(temp3, temp4);
		}
		return score;
	}

}
