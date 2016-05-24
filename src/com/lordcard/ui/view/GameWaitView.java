
package com.lordcard.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lordcard.common.schedule.AutoTask;
import com.lordcard.common.schedule.ScheduledTask;
import com.lordcard.common.util.MultiScreenTool;
import com.lordcard.constant.CacheKey;
import com.lordcard.constant.Constant;
import com.lordcard.constant.Database;
import com.lordcard.entity.GameUser;
import com.lordcard.entity.NoticesVo;
import com.lordcard.entity.Room;
import com.lordcard.network.http.GameCache;
import com.lordcard.network.task.GetRankTask;
import com.lordcard.ui.personal.PersonnalDoudizhuActivity;
import com.ylly.playcard.R;

import java.util.List;

/**
 * widget.GameWaitView
 * 
 * @author yinhb <br/>
 *         create at 2012 2012-12-31 下午12:19:26
 */
@SuppressLint("ViewConstructor")
public class GameWaitView extends LinearLayout {

    private LayoutInflater layoutInflater = null;
    private View mainView = null;
    private TextView vipRoomId, joinText = null;
    // private Timer timer;
    private AutoTask waitTask = null;
    private int ShowIndex;
    private int FRAME_TIME = 800;
    private Handler mHandler;
    private RelativeLayout joinBottomll;
    private TextView joinProgrees;// 加入房间进度(比赛场)
    private Button exitGame, gameRank, returnGpBtn;// 退出比赛，排行,退赛
    private Context context;
    private Handler handler;
    private static int maxPeople = 0; // 普通赛制开赛人数
    private MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();
    private RelativeLayout layout;
    private int tableNum = 0; // 当前正在比赛的桌数(普通赛制比赛场提示数据)
    private int rank = 0;// 当前排名(普通赛制比赛场提示数据)
    private int rankCount = 0; // 当前排名列表总人数(普通赛制比赛场提示数据)

    private boolean isFast; // 是否为超快赛
    private List<NoticesVo> noticeList = null;

    /**
     * @param activity
     * @param handler
     */
    @SuppressLint("HandlerLeak")
    public GameWaitView(final Activity activity, final Handler handler) {
        super(activity);
        noticeList = Database.JOIN_NOTICE_LIST;
        this.context = activity;
        this.handler = handler;
        this.layoutInflater = LayoutInflater.from(activity);
        mainView = this.layoutInflater.inflate(R.layout.game_wait, null);
        mainView.setOnClickListener(null);
        mst.adjustView(mainView);
        joinText = (TextView) findViewById(R.id.join_ad_text);
     
        joinProgrees = (TextView) mainView.findViewById(R.id.join_home_text);
        exitGame = (Button) mainView.findViewById(R.id.join_gp_exit_btn);
        exitGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(20);
            }
        });
        joinBottomll = (RelativeLayout) mainView.findViewById(R.id.join_bottom_ll);
        setjoinBottomllInVisible();
        gameRank = (Button) mainView.findViewById(R.id.join_gp_top_btn);
        gameRank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((System.currentTimeMillis() - Constant.CLICK_TIME) >= Constant.SPACING_TIME) {
                    Constant.CLICK_TIME = System.currentTimeMillis();
                    GetRankTask getRankTask = new GetRankTask();
                    getRankTask.execute();
                }
            }
        });
        returnGpBtn = (Button) mainView.findViewById(R.id.join_gp_return_place_btn);
        returnGpBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Math.abs(System.currentTimeMillis() - Constant.CLICK_TIME) >= Constant.SPACING_TIME) {// 防止重复刷新
                    Constant.CLICK_TIME = System.currentTimeMillis();
                }
            }
        });
        int[] location = new int[2];
        TranslateAnimation translate = new TranslateAnimation(location[0], location[0],
                location[1] - 20, location[1] + 20);
        translate.setDuration(FRAME_TIME);
        translate.setRepeatCount(TranslateAnimation.INFINITE);
        translate.setRepeatMode(TranslateAnimation.REVERSE);
        vipRoomId = (TextView) mainView.findViewById(R.id.vip_room_id);
        Room room = Database.JOIN_ROOM;
        if (activity.getClass().equals(PersonnalDoudizhuActivity.class)) {
            joinProgrees.setVisibility(View.INVISIBLE);
        } 
        LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        addView(mainView, layoutParams);
        joinPro();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 10001:
                        if (!isFast) {
                            joinProgrees.setText("智能拼桌中..");
                        }
                        break;
                    case 10002:
                        if (!isFast) {
                            joinProgrees.setText("智能拼桌中....");
                        }
                        break;
                    case 10003:
                        if (!isFast) {
                            joinProgrees.setText("智能拼桌中......");
                        }
                        break;
                    case 10004:
                        if (!isFast) {
                            joinProgrees.setText("智能拼桌中........");
                        }
                        break;
                    case 10005:
                        if (!isFast) {
                            joinProgrees.setText("智能拼桌中");
                        }
                        break;
                    case 10006:// 设置提示(普通赛制比赛场)
                        setTishi();
                        break;
                    case 10007:// 设置等待进度(普通赛制比赛场)
                        int n = msg.getData().getInt("n");
                        setNum(n);
                        break;
                    case 10008:
                        break;
                    case 10009:
                        break;
                    case 10010:
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
        ShowIndex = 1;
        waitTask = new AutoTask() {

            public void run() {
                switch (ShowIndex) {
                    case 1:
                        mHandler.sendEmptyMessage(10001);
                        break;
                    case 2:
                        mHandler.sendEmptyMessage(10002);
                        break;
                    case 3:
                        mHandler.sendEmptyMessage(10003);
                        break;
                    case 4:
                        mHandler.sendEmptyMessage(10004);
                        break;
                    case 5:
                        mHandler.sendEmptyMessage(10005);
                        break;
                    default:
                        break;
                }
                ShowIndex += 1;
                if (ShowIndex == 6) {
                    ShowIndex = 1;
                }
            }
        };
        ScheduledTask.addRateTask(waitTask, 20, 1500);
    }

    /**
     * 设置等待界面"退出"、"排名"、"退赛"按钮的显示
     */
    public void setjoinBottomllVisible() {
        joinBottomll.setVisibility(View.VISIBLE);
        GameUser gameUser = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
        if (0 == gameUser.getRound()) {
            exitGame.setVisibility(View.INVISIBLE);
            gameRank.setVisibility(View.INVISIBLE);
            returnGpBtn.setVisibility(View.VISIBLE);
        } else {
            exitGame.setVisibility(View.VISIBLE);
            gameRank.setVisibility(View.VISIBLE);
            returnGpBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置等待界面"退出"、"排名"、"退赛"按钮的隐藏
     */
    public void setjoinBottomllInVisible() {
        joinBottomll.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置房间名称
     * 
     * @param room
     */
    public void setRoomName(Room room) {
        switch (room.getRoomType()) {
            case 0:// 0大厅房
            case 2:// 2排位赛房
                if (room != null && !TextUtils.isEmpty(room.getName()) && room.getHomeType() != 2) {
                    vipRoomId.setText(room.getName());
                } else {
                    vipRoomId.setText(context.getResources().getString(R.string.vip_room_lable)
                            + Database.JOIN_ROOM_CODE);
                }
                break;
            case 1:// 1超快赛房
                if (null != room && null != room.getRoomDetail()) {
                    maxPeople = room.getRoomDetail().getLimitNum();
                } else {
                    Log.i("room", "room.getRoomDetail() is null");
                }
                setNum(0);
                setjoinBottomllVisible();
                if (room != null && !TextUtils.isEmpty(room.getName())) {
                    vipRoomId.setText(room.getName());
                }
                break;
        }
    }

    /**
     * 加载进度
     */
    public void joinPro() {
        if (noticeList != null) {
            int i = (int) (Math.random() * noticeList.size());
            joinText = (TextView) findViewById(R.id.join_ad_text);
            if (null != noticeList && i < noticeList.size()) {
                joinText.setText(noticeList.get(i).getContent());
            }
        } else {
           
        }
    }

    /**
     * 关闭定时器
     */
    public void closeTimer() {
        if (waitTask != null) {
            waitTask.stop(true);
        }
    }

    /**
     * 开启定时器
     */
    public void startTimer() {
        ShowIndex = 1;
        ScheduledTask.addRateTask(waitTask, 20, 1500);
    }

    /**
     * 设置加入人数进度
     * 
     * @param n
     */
    public void setNum(int n) {
        GameUser gameUser = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
        if (0 == gameUser.getRound()) {
            if (-1 == n) {
                joinProgrees.setText("人数已到齐，马上开赛");
            } else {
                if ((maxPeople - n) >= 0) {
                    joinProgrees.setText("还差" + (maxPeople - n) + "人开赛");
                } else {
                    joinProgrees.setText("还差0人开赛");
                }
            }
        } else {
            if (-1 == n) {
                joinProgrees.setText("此轮结束，开始下轮");
                tableNum = 0;
            } else {
                joinProgrees.setText("此轮还有" + n + "桌在比赛");
                tableNum = n;
            }
            setTishi();
            // clickPm = false;
            // GetRankTask getRankTask = new GetRankTask();
            // getRankTask.execute();
        }
    }

    /**
     * 设置等待进度，及排名名次信息
     */
    private void setTishi() {
        GameUser gameUser = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
        if (0 != gameUser.getRound()) {
            String table = "";
            String rankNum = "";
            if (0 == tableNum) {
                table = "其他各桌比赛完成，马上进入下一轮比赛。";
            } else {
                table = "还有" + tableNum + "桌未完成比赛，请耐心等候。";
            }
            if (rank > 0 && rank <= 3) {
                rankNum = "您目前的排名是第" + rank + "名，成绩不错哦，继续加油吧！";
            } else if (rank > 0) {
                rankNum = "您目前的排名是第" + rank + "名，还有机会晋级，继续加油吧！";
            }
            if (null == joinText) {
                joinText = (TextView) findViewById(R.id.join_ad_text);
            }
            joinText.setText(table + rankNum);
        }
    }

    public void onDestory() {
        noticeList = null;
        joinText = null;
        closeTimer();
    }
}
