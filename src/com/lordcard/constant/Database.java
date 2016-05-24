
package com.lordcard.constant;

import android.app.Activity;
import android.app.ProgressDialog;

import com.lordcard.entity.ContactPeople;
import com.lordcard.entity.ContentDetail;
import com.lordcard.entity.ContentTitle;
import com.lordcard.entity.GameCommandCheck;
import com.lordcard.entity.GameHallView;
import com.lordcard.entity.GameIQ;
import com.lordcard.entity.GamePropsType;
import com.lordcard.entity.GameRoomRuleDetail;
import com.lordcard.entity.GameUser;
import com.lordcard.entity.NoticesVo;
import com.lordcard.entity.Room;
import com.ylly.playcard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    /** 机器人账号 */
    public static final String ROBOT_ACCOUNT = "96714293";
    /** 推送数据 */
    public static Map<String, String> PUSH_DATA = null;
    // /**所有开关（后台控制前台的信息，包括充值部分）*/
    // public static Map<String,String> CONFIG_MAP = null;
    /** 是否后台下载的状态 */
    public static boolean UPDATED_STYLE = false;
    /** 当前的充值类型 */
    public static String PAYTYPE = null;
    /** 应用ID */
    public static final String GAME_SYSBID = "03";
    /** 美女图鉴数据 */
    public static GamePropsType TOOL = null;
    /** 当前页面 */
    public static Activity currentActivity = null;
    /** 更新中 */
    public static boolean UPDATEING = false;
    /** 游戏服务器地址 格式 192.168.0.1:8080 */
    public static String GAME_SERVER = null;
    // /**安全验证签名密钥*/
    // public static String SIGN_KEY;
    /** 进入的游戏类型(1:斗地主 2：麻将 3：锄大地) */
    public static int GAME_TYPE;
    /** 加入的房间 */
    public static Room JOIN_ROOM = null;
    /** 加入的房间编号 */
    public static String JOIN_ROOM_CODE;
    /** 加入的房间倍数 */
    public static int JOIN_ROOM_RATIO = 0;
    // /**安全验证签名密钥*/
    // public static String GAME_USER_AUTH_KEY="";
    /** 房间的更新时间 */
    public static String ROOM_UPDATE = null;
    /** 游戏房间背景图ID */
    public static int GAME_BG_DRAWABLEID = R.drawable.gamebg;
    /** 底数 */
    public static int JOIN_ROOM_BASEPOINT = 0;
    /** 游戏每局人数 */
    public static int GAME_GROUP_NUM = 0;
    // /**登录用户信息*/
    // public static GameUser USER = null;
    /** 赠判断的金豆 */
    public static long SEND_BEAN;
    /** 游戏玩家基本信息key:玩家的order */
    public static Map<Integer, GameUser> userMap = new HashMap<Integer, GameUser>();
    /** 手机验证码 */
    public static String PHONE_VALIDATOR_CODE = null;
    /** 手机号码 */
    public static String PHONE_VALIDATOR_VALUE = null;
    /** 手机屏幕宽 */
    public static int SCREEN_WIDTH = 0;
    /** 手机屏幕高 */
    public static int SCREEN_HEIGHT = 0;
    /** 是否为钻石专场 */
    public static boolean ZHIZHUANSIGN = false;
    /** 网络心跳检测时间 */
    public static int HEARTBEAT_TIME = 18000;
    /** 联系人列表 */
    public static List<ContactPeople> ContactPeopleList = null;
    /** 当前手机中安装的APK名称列表 */
    public static List<String> packageNames = new ArrayList<String>();
    /** 加入游戏等待页面 等待消息 */
    public static List<NoticesVo> JOIN_NOTICE_LIST;
    /** 游戏指南 */
    public static List<ContentTitle> GAME_GUIDE_LIST;
    /** 游戏指南 */
    public static List<ContentTitle> STOVE_GUIDE_LIST;
    /** 游戏指南详情 */
    public static ContentDetail GUIDE_DETAIL_LIST;
    /** 没有校验过，则校验新版本 */
    public static boolean CHECK_VERSION = true;
    /** 是否有新版本 */
    public static boolean HAS_NEW_VERSION = false;
    /** 是否有新版本 */
    public static boolean UPDATED = false;
    /** 房间信息缓存 **/
    public static GameHallView HALL_CACHE = null;
    /** 快速游戏房间缓存 */
    public static GameHallView QUICK_HALL = null;
    /** 普通赛制说明 */
    public static HashMap<String, GameRoomRuleDetail> FAST_EXPLAIN;
    /** 符合赛制说明 */
    public static HashMap<String, GameRoomRuleDetail> SORT_EXPLAIN;
    /** 房间列表更新时间 */
    public static String ROOM_FRESH_TIME = "";
    /** 游戏助理下载应用标示 */
    public static boolean ASSISTANT_DW = false;
    /** 判断可否加入消息中心（第一次不可加入） */
    public static boolean ADD_DATA_CENTRE = false;
    /** 判断是否已经关闭游戏助理 */
    public static boolean ASSCLOSE = true;
    /** 登录时间：格式(yyyy-MM-dd HH:mm:ss) */
    public static String LOGIN_TIME;
    /** 命令详情 */
    public static GameCommandCheck GCC = null;
    /** 下载美女图鉴的最后一个图片 */
    public static String LASTPIC = "";
    /** 等级列表数据 */
    public static List<GameIQ> IQ_DATA = null;

    /** 已发送的命令列表 */
    public static List<String> hasSendCmdList = new ArrayList<String>();
    /** 记牌器可免费使用的次数 */
    public static int JI_PAI_QI_FREE_COUNT = 0;

    public static ProgressDialog chargingProcessDia;// 充值进度框
}
