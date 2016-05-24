
package com.lordcard.constant;

/**
 * 系统常量类
 * 
 * @author yinhb
 */
public class Constant {

    public static final String GAME = "dizhu";
    public static final String CHAR = "UTF-8";
    /** 消息序列号 一直递增 */
    public static long seq = 1;
    public static final String LOG_TAG = "game_log";
    public static final String SUCCESS = "success";
    public static final int DEFAULT_WIDTH = 480;
    public static final int DEFAULT_HEIGHT = 800;
    public static final String MCID = "android";
    public static final String DB_NAME = "game_card.db";
    /** 广播播放时间 */
    public static final int AD_PLAY_TIME = 3000;
    /** 游戏等待时间 20秒 */
    public static final int WAIT_TIME = 20;
    /** 牌花色 黑桃 */
    public static final String CARD_TAO = "tao";
    /** 牌花色 红桃 */
    public static final String CARD_XIN = "xin";
    /** 牌花色 梅花 */
    public static final String CARD_MEI = "mei";
    /** 牌花色 方块 */
    public static final String CARD_FANG = "fang";
    /** 每个花色牌数据 */
    public static final int CART_COUNT = 13;
    /** 默认密码 */
    public static final String DEFAULT_PWD = "@@@@@@";
    public final static int MIN_HEAP_SIZE = 12 * 1024 * 1024;
    public final static float TARGET_HEAP_UTILIZATION = 0.75f;
    public static final int ANIM_FRAME_COUNT = 2;
    public static final String ANIM_IMAGE_START = "over000";
    public static final String BOMB_IMAGE_START = "bomb_";
    public static final String WANG_BOMB_IMAGE_START = "rocket_";
    public static final String SHUNZ_IMAGE_START = "anim_shunz_";
    public static final String FEIJI_IMAGE_START = "anim_feiji_";
    public static final String BAO_XIANG = "baoxiang_start_";
    public static final String LOT_MAIN = "lot_";
    public static final String POINT_ANIM = "point";
    public static final String IMAGE_NEW_ANIM = "new";
    public static final String IMAGE_NEWS_ANIM = "news";
    public static final String IMAGE_LING_ZHI_DOU_ANIM = "home_receive_been_button_";
    /** 快速加入游戏的类型 */
    public static final int FASTJOIN_TYPE = 2;
    /** 金豆 */
    public static final String UNIT_TYPE_BEAN = "2";
    public static int startCount;
    /** 游戏类型: 斗地主 */
    public static final int GAME_TYPE_DIZHU = 1;
    /** 房间类型：2:VIP包房 */
    public static int ROOM_VIP_PRIVATE = 2;
    /** 系统网络改变 */
    public static final String SYSTEM_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    /** 更新系统时间 **/
    public static final String SYSTEM_TIME_CHANGE_ACTION = "android.net.conn.SYSTEM_TIME_CHANGE";
    /** 浏览器 */
    public static final String SYSTEM_ACTION_INTENT = "android.intent.action.VIEW";
    /** 爱是商城 包名 */
    public static final String PACKAGE_NAME_SHOP = "til.mall.activity";
    /** 爱是商城 首页 */
    public static final String THREE_URI_SHOP_START = PACKAGE_NAME_SHOP + ".WelcomeActivity";
    /** 爱是商城商品详情 */
    public static final String THREE_URI_SHOP_DETAIL = PACKAGE_NAME_SHOP
            + ".bileMallItemDetailActivity";
    /** 爱是商城资讯 */
    public static final String THREE_URI_SHOP_NEW = PACKAGE_NAME_SHOP + ".wsItemDetailActivity";
    /** 短信发送广播 */
    public static final String ACTION_SMS_SEND = "com.lord.card.broadcast.sms.send";
    /** 短信发送广播 */
    public static final String ACTION_SMS_ORDER = "com.game.broadcast.sms.order";
    /** 短信发送广播(系统) */
    public static final String ACTION_SMS_SEND3 = "android.intent.action.SENDTO";
    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    /** 任务提示 */
    public static final String VIEW_ANIM_TASK_TIP = "view_anim_task_tip";
    /** 炸弹 */
    public static final String VIEW_ANIM_BOMB = "view_anim_bomb";
    /** 等待对方出牌沙漏 */
    public static final String VIEW_ANIM_BUSY = "view_anim_busy";
    public static final String APP_ID = "appId";
    public static final String APP_SECRECT = "appSecret";
    public final static String TOKEN_ID = "tokenID";
    /** token生成时间 */
    public final static String TOKEN_START_TIME = "tokenStartTime";
    /** token有效时间 */
    public final static String EXPIRE_TIME = "expireTime";
    /** 游戏激活存储参数 */
    public final static String GAME_ACTIVITE = "game_activite";
    /** 游戏获取话费 */
    public final static String GAME_GETFEE = "game_getfee";
    /** 本地账号列表 */
    public static final String LOCAL_ACCOUNTS = "local_accounts";
    /** 本地账号列表是否绑定 */
    public static final String LOCAL_ACCOUNTS_IS_BIND = "local_accounts_is_bind";
    public static final String ROOMTIME = "room_time";
    public static final String UPDATETIME = "updatetime";
    /** 游戏背景缓存Mmap的KEY */
    public static final String GAME_BACKGROUND = "gameback";
    /**
     * 记录点击按钮的时间， 下次再点击按钮的时候 与上次的差值<1000, 则不座响应（用于控制疯狂点击）
     **/
    public static long CLICK_TIME = 0;
    /** 两次点击的时间间隔 */
    public static final long SPACING_TIME = 1000;
    /** 单机本地赠送金豆时间 */
    public static final String GET_BEAN_COUNT = "getbeancount";
    /** 单机本地赠送金豆时间 */
    public static final String GET_BEAN_CACHE = "getbeancache";
    /** 单机缓存本地金豆 */
    public static final String GAME_BEAN_CACHE = "gamebeancache";
    /** 新手任务 */
    public static final String GAME_NEW_TASK = "new_user_task";
    /** 单机第一次使用 */
    public static final String IS_FIRST = "is_first";
    /** 单机缓存自己名字 */
    public static final String GAME_NAME_CACHE = "gameselfname";
    /** 单机缓存美女 */
    public static final String GAME_GIRL_CACHE = "gamegirlscache";
    /** 更新框弹出缓存参数 */
    public static final String UPDATECODE = "updatecode";
    /** 控制更新框是否自动弹出 */
    public static final String VERSIONCODE = "version";
    /** 后台下载包对应code参数 */
    public static final String SAVECODE = "savecode";
    /** 更新框弹出缓存参数 */
    public static final String SAVEAPK = "saveapk";
    public static final String APKNAME = "publicapp.apk";
    /** 0为聊天 */
    public final static int MESSAGE_TYPE_ZERO = 0;
    /** 1为思考 */
    public final static int MESSAGE_TYPE_ONE = 1;
    /** 2为表情 */
    public final static int MESSAGE_TYPE_TWO = 2;
    /** 3美女 */
    public final static int MESSAGE_TYPE_THREE = 3;
    /** 1游戏指南 2 合成帮助 */
    public final static String GUIDE_TYPE_ONE = "1";
    public final static String GUIDE_TYPE_TWO = "2";
    /** 打牌的局数 */
    public static final String QUANG_KEY = "quang";
    /** 打牌的总局数 */
    public static final String ALL_QUAN = "allquan";
    /** 消息通知 */
    public static final String NOTIFICATION_SERVICE = "notification.intent.action.Service";

    // key
    public static final String PRIZE_GOODS = "prizeGoods";
    public static final String RANK = "rank";
    /** 网络间隔时间 */
    public static long time_space = 0L;
    // public static long lastHbTime = 0; //socket建立连接时，记录最后的心跳时间
    /** 游戏状态：已发牌 */
    public static final int STATUS_START = 4;
    /** 游戏状态：正在叫地主 */
    public static final int STATUS_GRAB = 5;
    /** 游戏状态：正在游戏 */
    public static final int STATUS_PLAYING = 6;
    /** 游戏状态：游戏结束 */
    public static final int STATUS_END = 8;
    /** 游戏状态：正在踢拉 */
    public static final int STATUS_TL = 9;

    // 打牌界面：DoudizhuMainGameActivity or PersonnalDoudizhuActivity
    /** 发送聊天信息-文字 */
    public static final String GAME_VIEW_SEND_MESS_TEXT = "game_view_send_text";
    /** 发送聊天信息-GIF表情 */
    public static final String GAME_VIEW_SEND_MESS_GIF = "game_view_send_mess_gif";
    /** 发送聊天信息-GIF表情 */
    public static final String GAME_VIEW_SEND_MESS_CLICK_TYPE = "game_view_send_mess_click_type";

    // ================================= Handler what常量=================================//
    // ---打牌界面：DoudizhuMainGameActivity or PersonnalDoudizhuActivity
    /** 发送聊天信息-文字what */
    public static final int HANDLER_WHAT_GAME_VIEW_SEND_MESS_TEXT = 778;
    /** 发送聊天信息-GIF表情what */
    public static final int HANDLER_WHAT_GAME_VIEW_SEND_MESS_GIF = 780;

    /** sim缓存的KEY */
    public static final String SIM_KEY = "sim_cache";
    /** 移动sim卡 */
    public static final String SIM_MOBILE = "mobile";
    /** 联通sim卡 */
    public static final String SIM_UNICOM = "union";
    /** 电信sim卡 */
    public static final String SIM_TELE = "tele";
    /** 其他不可识别的sim卡 */
    public static final String SIM_OTHER = "nosim";

    //
    public static final String SMS_INITE_MSG = "sms_invite_msg";
    public static final String SMS_INITE_CODE = "sms_invite_code";
    public static final String SMS_INITE_INPUT = "sms_invite_input";
    public static final String FILL_IN_INFO = "fill_in_info";
    public static final String INVITE_FRIEND = "invite_friend";
    public static final String APP_DOWNLOAD = "app_download";
    public static final String FUN_NO_OPEN = "fun_no_open";
    // 用户游戏信息
    public static final String KEY_COUNT_PLAY_INNINGS = "key_count_play_innings";
    public static final String KEY_COUNT_LOSE_INNINGS = "key_count_lose_innings";
    public static final String KEY_COUNT_WIN_INNINGS = "key_count_win_innings";
    public static final String KEY_COUNT_IQ_RISE = "key_count_iq_rise";
    public static final String KEY_IQ = "key_iq";
    public static final String RECEIVE_FAIL = "receive_fail";
    public static final String HAS_RECEIVE = "has_received";
    public static final String KEY_ACCOUNT_BIND_DIALOG_SHOW_COUNT = "key_account_bind_dialog_show_count";
}
