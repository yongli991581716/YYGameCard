package com.lordcard.network.cmdmgr;

import java.util.List;

import android.text.TextUtils;

import com.lordcard.common.util.ActivityUtils;
import com.lordcard.common.util.ChannelUtils;
import com.lordcard.common.util.JsonHelper;
import com.lordcard.constant.CacheKey;
import com.lordcard.constant.Database;
import com.lordcard.entity.GameUser;
import com.lordcard.entity.JoinCmdDetail;
import com.lordcard.entity.JoinDetail;
import com.lordcard.entity.Poker;
import com.lordcard.network.http.GameCache;

public class CmdUtils {

	public static final String SUCCESS_CODE = "1"; // 成功代码
	public static final String FAIL_CODE = "0"; // 失败代码

	public static final String MY = "myself"; //自己的标识

	//socket
	public static final String CMD_HB = "hb"; //心跳
	public static final String CMD_JOIN = "j"; //加入房间join
	public static final String CMD_FJ = "fj"; // 快速游戏
	public static final String CMD_STARTREADY = "srd"; //准备startReady
	public static final String CMD_READY = "rd"; //准备Ready
	public static final String CMD_USER = "ui"; //玩家资料
	public static final String CMD_START = "s"; //客户端开始游戏指令start
	public static final String CMD_GRAB = "g"; //玩家抢地主grab
	public static final String CMD_MASTER = "m"; //地主产生master
	public static final String CMD_GEN_LANDOWNERS = "gl"; // 产生地主
	public static final String CMD_SENDLASTCARDS = "l"; // 发剩余的牌给地主
	public static final String CMD_TILA = "t"; //玩家踢拉
	public static final String CMD_PLAYING = "p"; //游戏进行，出牌指令playing
	public static final String CMD_END = "e"; //本局游戏结束end
	public static final String CMD_CHAT = "c"; //聊天chat
	public static final String CMD_QUIT = "q"; //quit指令，客户端在游戏中任何时间收到这个指令，都必须退出到房间列表
	public static final String CMD_OUT = "o"; //离开游戏out
	public static final String CMD_DOLK = "dolk"; //向服务器发送重连命令
	public static final String CMD_RLINK = "rl"; //服务器通知重连 rlink

	public static final String CMD_LINK = "lk"; //下行： 开始链接   上行：连接成功
	public static final String CMD_BREAK = "bk"; //玩家断线beak
	public static final String CMD_SLOW = "sl"; //网络缓慢
	public static final String CMD_PPC = "ppc"; //生成预充值订单
	//http
	public static final String CMD_HDETAIL = "hdl"; //homeDetail 查看房间资料 
	public static final String CMD_RJOIN = "rj"; //rjoin准备加入
	public static final String CMD_CREATE = "ce"; //创建房间create
	public static final String CMD_ERR_CREATE = "ect"; //创建房间错误err_create
	public static final String CMD_ERR_RJOIN = "ej"; //准备加入失败err_rjoin
	public static final String CMD_SER_STOP = "ser_stop"; //服务器暂停

	public static final String CMD_ROUTING = "routing"; // 系统公告
	public static final String CMD_INLINE = "inline"; // 获取在线人数
	public static final String CMD_LOGIN = "login"; // 登录
	public static final String CMD_RANK = "rk"; // 排名（快速赛场）
	public static final String CMD_WAIT_GROUP = "wg"; // 桌等待( 还有多少桌在比赛)
	public static final String CMD_WAIT_USER = "wu"; // 用户等待( 还差多少人开赛)
	public static final String CMD_DIE_OUT = "dt"; // 淘汰用户
	public static final String CMD_PLAY_OVER = "pv"; // 比赛结束

	public static final String CMD_MES = "mes"; //系统公告
	public static final String CMD_SYSMSG = "sysmsg"; //
	public static final String LOGIN_SUCCESS = "login_success";
	public static final String CMD_TASK = "task"; // 游戏活动任务

	public static final String CMD_COMPLAINTS = "co"; //举报

	public static final int REGEST_CODE = 1; // 快速游戏未
	public static final int LOGIN_CODE = 2; // 成功代码
	public static final String NO_SCOPE_BEAN = "ncb"; //金豆不在上下限制范围内
	/**
	 * 发送加入房间指令
	 * 
	 * @param gameToken
	 * @param roomCode
	 * @return
	 */
	public static void sendJoinRoomCmd(String gameToken, String roomCode) {
		CmdDetail detail = new CmdDetail();
		detail.setCmd(CMD_JOIN);
		detail.setToken(gameToken);
		detail.setAndroId(ActivityUtils.getAndroidId());
		detail.setType(CmdDetail.PLAY);
		detail.setVersion(ActivityUtils.getVersionName());
		//		detail.setDetail(roomCode);

		JoinDetail join = new JoinDetail();
		join.setRoomCode(roomCode);
		join.setTiLaSwitch(true);
		String ji = JsonHelper.toJson(join);
		detail.setDetail(ji);

		ClientCmdMgr.sendCmd(detail);
	}

	/**
	 * 发送加入房间指令
	 * 
	 * @param gameToken
	 * @param roomCode
	 * @return
	 */
	public static void sendFastJoinRoomCmd() {
		CmdDetail joinroom = new CmdDetail();
		joinroom.setCmd(CMD_FJ);
		joinroom.setAndroId(ActivityUtils.getAndroidId());

		JoinCmdDetail detail = new JoinCmdDetail();
		GameUser gu = (GameUser)GameCache.getObj(CacheKey.GAME_USER);
//		GameUser gu = ActivityUtils.loadLocalAccount();
		if (gu != null) {
			detail.setAccount(gu.getAccount());
			detail.setGameType(String.valueOf(Database.GAME_TYPE));
			detail.setMacIp(ActivityUtils.getAndroidId());
			detail.setOper(LOGIN_CODE);
			String pwd = gu.getMd5Pwd();
			pwd = TextUtils.isEmpty(pwd) ? gu.getUserPwd() : pwd;
//			detail.setUserPwd(gu.getMd5Pwd());
			detail.setUserPwd(pwd);
			detail.setVersion(ActivityUtils.getVersionName());
		} else {
			detail.setGameType(String.valueOf(Database.GAME_TYPE));
			detail.setMacIp(ActivityUtils.getAndroidId());
			detail.setVersion(ActivityUtils.getVersionName());
			detail.setChannel(ChannelUtils.getSerCfgName());
			detail.setOper(REGEST_CODE);
		}
		String dj = JsonHelper.toJson(detail);
		joinroom.setDetail(dj);
		joinroom.setType(CmdDetail.PLAY);

		ClientCmdMgr.sendCmd(joinroom);
	}

	public static void sendGetRankCmd(String gameToken) {
		CmdDetail detail = new CmdDetail();
		detail.setCmd(CMD_RANK);
		ClientCmdMgr.sendCmd(detail);
	}

	/**
	 * 设置托管
	 */
	public static void sendIsRobot() {
		CmdDetail detail = new CmdDetail();
		detail.setCmd(CMD_BREAK);
		ClientCmdMgr.sendCmd(detail);
	}

	/**
	 * 设置取消托管
	 */
	public static void sendCancelRobot() {
		CmdDetail detail = new CmdDetail();
		detail.setCmd(CMD_RLINK);
		ClientCmdMgr.sendCmd(detail);
	}

	/**
	 * 发送聊天信息
	 */
	public static void sendMessageCmd(CmdDetail message) {
		ClientCmdMgr.sendCmd(message);
	}

	/**
	 * 准备游戏
	 */
	public static void ready() {
		CmdDetail detail = new CmdDetail();
		detail.setCmd(CmdUtils.CMD_READY);
		detail.setType(CmdDetail.PLAY);
		ClientCmdMgr.sendCmd(detail);
	}

	/**
	 * 叫地主
	 */
	public static void callDizhu(String fen) {
		CmdDetail detail = new CmdDetail();
		detail.setCmd(CmdUtils.CMD_GRAB);
		detail.setDetail(fen);
		detail.setType(CmdDetail.PLAY);
		ClientCmdMgr.sendCmd(detail);
	}

	/**
	 * 退出房间
	 */
	public static void exitGame() {
		CmdDetail detail = new CmdDetail();
		detail.setCmd(CmdUtils.CMD_OUT);
		ClientCmdMgr.sendCmd(detail);
	}

	/**
	 * 自己不出
	 */
	public static void pass() {
		CmdDetail detail = new CmdDetail();
		detail.setCmd(CmdUtils.CMD_PLAYING);
		detail.setDetail("[]");
		detail.setType(CmdDetail.PLAY);
		ClientCmdMgr.sendCmd(detail);
	}

	/**
	 * 游戏开始出牌
	 * 
	 * @param cards
	 * @return
	 */
	public static void play(List<Poker> cards) {
		StringBuilder playCard = new StringBuilder();
		playCard.append("[");
		for (int i = 0; i < cards.size(); i++) {
			playCard.append(cards.get(i).getNumber());
			playCard.append(",");
		}
		playCard.deleteCharAt(playCard.length() - 1);
		playCard.append("]");

		CmdDetail detail = new CmdDetail();
		detail.setCmd(CmdUtils.CMD_PLAYING);
		detail.setDetail(playCard.toString());
		detail.setType(CmdDetail.PLAY);
		ClientCmdMgr.sendCmd(detail);
	}
}
