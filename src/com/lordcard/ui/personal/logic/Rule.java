package com.lordcard.ui.personal.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.lordcard.common.exception.LogUtil;

/**
 * 斗地主游戏规则
 * 
 *		[0--------12]:     	黑桃A---------黑桃K
 *		[13-------25]:    	红心A---------红心K
 * 		[26-------38]：		梅花A---------梅花K
 * 		[39-------51]:		方块A---------方块K
 *		[52] :	小王 
 *		[53] :	大王
 * 
 * @author yinhongbiao 2012 2012-6-6
 * 
 */
public class Rule {

	private static int everycardnum = 17; //每个人牌的数量
	private static int bombCount = 13; //炸数量
	// 斗地主炸集合
	private static List<Integer[]> bombList = new ArrayList<Integer[]>();

	//散牌中的大牌
	private static List<Integer> sanpai = new ArrayList<Integer>();
	//王
	private static List<Integer> king = new ArrayList<Integer>();
	//2
	private static List<Integer> pokertwo = new ArrayList<Integer>();
	//a
	private static List<Integer> pokera = new ArrayList<Integer>();

	static {
		//录入A-K的炸[从大到小的路人牌]
		bombList.add(new Integer[] { 52, 53 });
		king.add(53);
		king.add(52);

		//2
		pokertwo.add(1);
		pokertwo.add(14);
		pokertwo.add(27);
		pokertwo.add(40);

		//A
		pokera.add(0);
		pokera.add(13);
		pokera.add(26);
		pokera.add(39);

		for (int i = 1; i >= 0; i--) {
			bombList.add(new Integer[] { i, i + 13, i + 26, i + 39 });

			sanpai.add(i);
			sanpai.add(i + 13);
			sanpai.add(i + 26);
			sanpai.add(i + 39);
		}
		for (int i = 12; i >= 2; i--) {
			bombList.add(new Integer[] { i, i + 13, i + 26, i + 39 });
		}
	}

	/**
	 * 给玩家发炸弹派
	 * @param bombRecored
	 * @return
	 * 2012  2012-6-12
	 */
	public static Integer[] issueBomb(List<Integer> bombRecored) {
		Random rand = new Random();
		int i = rand.nextInt(bombCount + 1); //获取炸的索引

		while (bombRecored.contains(i)) { //判断当前炸在此局是否已经发给完家。保证不发重复牌
			i = rand.nextInt(bombCount + 1);
		}
		bombRecored.add(i); //记录发出去的炸
		return bombList.get(i);
	}

	/**
	 * 洗牌
	 * @return
	 * 2012  2012-6-12
	 */
	private static LinkedList<Integer> washCards() {

		//录入54张牌
		LinkedList<Integer> cards = new LinkedList<Integer>();
		for (int i = 0; i < 54; i++) {
			cards.add(i);
		}

		Random rand = new Random();
		//通过随机数打乱cards内牌的顺序
		for (int i = 0; i < 54; i++) {
			int r = rand.nextInt(54);
			Collections.swap(cards, i, r);
		}
		return cards;
	}

	/**
	 * 获取炸弹数
	 * @param count	炸弹数
	 * @return
	 */
	private static LinkedList<Integer> getBombs(int count) {
		LinkedList<Integer> bombRecored = null;
		if (count > 0) {
			bombRecored = new LinkedList<Integer>();
			Random rand = new Random();
			for (int i = 0; i < count; i++) {
				int index = rand.nextInt(bombCount + 1); //获取炸的索引
				while (bombRecored.contains(index)) { //判断当前炸在此局是否已经发给完家。保证不发重复牌
					index = rand.nextInt(bombCount + 1);
				}
				bombRecored.add(index);
			}
		}
		return bombRecored;
	}

	/**
	 * 发牌
	 * @param group
	 * 2012 2012-6-8
	 */
	public static ClientData issueCards(ClientData data) {
		try {
			List<ClientUser> users = data.getUsers();
			int playCount = 3; //本局玩家数
			//洗牌
			LinkedList<Integer> washCardList = washCards();
			/////////////////////////// 发炸弹牌  没有机器人，且炸弹数大于0////////////////////////////////
			Random rd = new Random();
			int bombCount = rd.nextInt(4);//随即0-3个炸弹
			LinkedList<Integer> bombs = getBombs(bombCount); //当前炸弹

			//分配炸弹
			for (int i = 0; i < bombCount; i++) {
				int playIndex = RandomUtils.getOrder(playCount); //随机找一个玩家
				for (int j = 0; j < users.size(); j++) {
					if (users.get(j).getOrder() == playIndex) {
						for (Integer b : bombList.get(bombs.poll())) {
							List<Integer> tempCard = users.get(j).getCards();
							tempCard.add(b);
							washCardList.remove(b); //已发的牌从之后的随机发牌队列中删除
						}
					}
				}
			}
			//随机发剩余的牌
			for (ClientUser client : users) {
				List<Integer> mycards = client.getCards(); //我的牌
				int mycardscount = mycards.size(); //已有牌的数量
				//随机获取剩余的牌
				for (int i = 0; i < (everycardnum - mycardscount); i++) {
					mycards.add(washCardList.removeFirst());
				}
			}

			//随机决定由哪个位置开始叫分
			int count = users.size();
			int masterStart = RandomUtils.getOrder(count) + 1; //随机产生叫地主的位置
			data.setMasterStart(masterStart);
			// 底牌(没有发完的牌成为底牌)
			data.getLastCards().addAll(washCardList);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.err(" === 发牌时报错 ====", e);
		}
		return data;

	}
}
