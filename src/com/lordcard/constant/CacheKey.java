package com.lordcard.constant;



public class CacheKey {
	
	public static final String CHANNEL_MM_ID = "channel_mm_id";
	
	public static final String GAME_USER = "game_user_key";
	/**各界各种配置信息*/
	public static final String ALL_SETTING_KEY="all_setting_msg_key";
	/**各界面提示内容信息*/
	public static String KEY_TEXT_VIEW_MESSAGE_DATA = "text_view_message_data_key"; 
	/**玩游戏的信息(局数、输赢局数、金豆输赢数、经验增长数、当前金豆数)*/
	public static String KEY_PLAY_GAME_MSG = "key_play_game_msg"; 
	
	/** 支付初始数据缓存的键 */
	public static final String PAY_INIT_MAP = "pay_init_map";
	/** 计费位置支付配置*/
	public static final String PAY_SITE_MAP = "pay_site_list";
	/** 房间列表缓存键*/
	public static final String ROOM_HALL = "roomhall";
	/**记录最后一次领金豆时间*/
	public static final String GET_ZHI_DOU_DATE="get_zhi_dou_date";
	/**游戏公告*/
	public static final String GAME_NOTICE="game_notice_cache";
	
}
