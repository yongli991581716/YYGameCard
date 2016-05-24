package com.lordcard.common.listener;

import com.lordcard.entity.Poker;

/**
 * 检测是否提示对应牌型 (当上家有出牌，到我出牌时，我选择的单张牌： 1.包含在可以吃上家的牌的牌型中，则提示出该牌型 2.不包含，则只提示该单张牌)
 * 
 * @ClassName: hasTiShiListenner
 * @Description: TODO
 * @author zhenggang
 * @date 2013-6-4 下午5:09:32
 */
public interface HasTiShiListenner {

	/**
	 * * 检测是否提示对应牌型 (当上家有出牌，到我出牌时，我选择的单张牌： 1.包含在可以吃上家的牌的牌型中，则提示出该牌型
	 * 2.不包含，则只提示该单张牌)
	 */
	public boolean hasTiShi(Poker mPoker, int index);
	
	/**
	 * 监听手牌区onScroll变化
	 * @param e1X
	 * @param e1Y
	 * @param e2X
	 * @param e2Y
	 * @param distanceX
	 * @param distanceY
	 */
	public void onScrollListenner(int e1X,int e1Y,int e2X,int e2Y,int startIndex);
	
	/**监听滑动放开地位置信息
	 * @param x
	 * @param y
	 */
	public void onTouchUpListenner(float x,float y,int startIndex);
	
	/**
	 * 监听onFling
	 * @param e1
	 * @param e2
	 * @param velocityX
	 * @param velocityY
	 */
	public void onFling() ;
}
