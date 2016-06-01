
package com.lordcard.ui.personal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.reflect.TypeToken;
import com.lordcard.common.anim.AnimUtils;
import com.lordcard.common.anim.PlayCardEffect;
import com.lordcard.common.listener.HasTiShiListenner;
import com.lordcard.common.schedule.AutoTask;
import com.lordcard.common.schedule.ScheduledTask;
import com.lordcard.common.task.GenericTask;
import com.lordcard.common.util.ActivityPool;
import com.lordcard.common.util.ActivityUtils;
import com.lordcard.common.util.AudioPlayUtils;
import com.lordcard.common.util.DialogUtils;
import com.lordcard.common.util.ImageUtil;
import com.lordcard.common.util.ImageUtil.ImageCallback;
import com.lordcard.common.util.JsonHelper;
import com.lordcard.common.util.PatternUtils;
import com.lordcard.common.util.PreferenceHelper;
import com.lordcard.common.util.Vibrate;
import com.lordcard.constant.CacheKey;
import com.lordcard.constant.Constant;
import com.lordcard.constant.Database;
import com.lordcard.entity.GamePropsType;
import com.lordcard.entity.GameUser;
import com.lordcard.entity.GenLandowners;
import com.lordcard.entity.Grab;
import com.lordcard.entity.MarqueeText;
import com.lordcard.entity.Poker;
import com.lordcard.entity.Room;
import com.lordcard.entity.TiLa;
import com.lordcard.network.cmdmgr.CmdDetail;
import com.lordcard.network.cmdmgr.CmdUtils;
import com.lordcard.network.http.GameCache;
import com.lordcard.network.http.HttpURL;
import com.lordcard.rule.DouDiZhuData;
import com.lordcard.rule.DoudizhuRule;
import com.lordcard.rule.HintPokerUtil;
import com.lordcard.rule.PokerUtil;
import com.lordcard.ui.base.BaseActivity;
import com.lordcard.ui.base.IGameView;
import com.lordcard.ui.interfaces.InitMainGameInterface;
import com.lordcard.ui.personal.logic.CallUtil;
import com.lordcard.ui.personal.logic.ClientData;
import com.lordcard.ui.personal.logic.ClientName;
import com.lordcard.ui.personal.logic.ClientUser;
import com.lordcard.ui.personal.logic.DouDiZhuLogic;
import com.lordcard.ui.personal.logic.PlayAlone;
import com.lordcard.ui.personal.logic.Pritype;
import com.lordcard.ui.personal.logic.Rule;
import com.lordcard.ui.view.GameWaitView;
import com.lordcard.ui.view.JiPaiQiTurnPlateView;
import com.lordcard.ui.view.JiPaiQiTurnPlateView.Location;
import com.lordcard.ui.view.MainGameGuideView;
import com.lordcard.ui.view.RecordPorkerView;
import com.lordcard.ui.view.TouchRelativeLayout;
import com.lordcard.ui.view.dialog.GameDialog;
import com.lordcard.ui.view.dialog.SettingDialog;
import com.newqm.pointwall.QEarnNotifier;
import com.newqm.pointwall.QSdkManager;
import com.newqm.sdkoffer.AdView;
import com.ylly.playcard.R;
import com.ylly.playcard.R.color;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

@SuppressLint({
        "HandlerLeak", "UseSparseArrays"
})
public class PersonnalDoudizhuActivity extends BaseActivity implements IGameView, OnTouchListener,
        HasTiShiListenner, OnGestureListener, InitMainGameInterface, QEarnNotifier {

    /** 动画-无结束监听 */
    public static final int IS_NONE = 11100;
    /** 动画-头像移动 */
    public static final int IS_HEAD_ANIM = 11101;
    /** 动画-飞机 */
    public static final int IS_FEIJI_ANIM = 11102;
    /** 动画-王炸 */
    public static final int IS_WANGZHA_ANIM = 11103;
    /** 动画-宝箱移动 */
    public static final int IS_BAOXIANG_ANIM = 11104;
    /** 动画-炸弹 */
    public static final int IS_ZHADAN_ANIM = 11105;
    // 适应多屏幕的工具
    private List<ImageView> girlView;
    private boolean isWait5Second = false;
    private Handler handler = null;
    private Poker[] poker = null; // 扑克牌
    private TouchRelativeLayout myCardsTouchLayout;
    private RelativeLayout doudizhuLayout, doudizhuBackGround;
    private LinearLayout playBtnLayout, jiaofenLayout;
    private RelativeLayout tuoGuanLayout;
    // private RelativeLayout mySelfHeadRl;// 自己头像布局
    private TextView nullTv, nullTv2;// 做布局撑自己头像布局用的
    private int[] pai = null;
    private Button chupai, tishi, buchu = null;
    private AutoTask selfTask, leftTask, rightTask, pubTask, adTask, gameTask, task2, baoXiangTask,
            headTask;
    private MarqueeText marqueeText;
    private Button bujiao, fen1, fen2, fen3 = null;
    private TextView play1SurplusCount, play3SurplusCount, play2SurplusCount = null;
    private List<Poker> nowcard = null; // 现在手中的牌
    private List<Poker> chupaicard = null; // 准备出的牌
    private List<Poker> checkpai = null;
    private List<Poker> otherplay1 = null; // 准备出的牌
    private List<BitmapVO> cashList = null;
    private int[] paixu;
    private int[] bierenchupai = null;
    private int typeMe = 0; // 自己出牌的类型
    private int valueMe = -1;
    private int typeplay1 = 0;// 别人出牌的类型
    private int mySelfOrder;// 自己出牌的顺序为
    private Thread popThread;// popwindow线程
    private int card_jiange = 37;
    private boolean firstChupai = true, isTuoguan = false;
    private TextView play1Timer, play3Timer, play2Timer = null;
    private TextView zhidou = null;
    private ImageView play1Icon, play3Icon, play2Icon = null;// 头像
    private ImageView zhezhao1, zhezhao3, zhezhao2 = null;
    private int masterOrder = 0;
    private JSONObject advList = null;
    // private String waitTime = "20";
    private RelativeLayout.LayoutParams adWidgetLayoutParam = null;
    private SettingDialog settingDialog = null;
    private ImageButton gameRobot, gameSet;
    private Button tuoGuan;
    // 玩家名称
    private TextView playTextView1, playTextView3, playTextView2 = null;
    private TextView wolTv1, wolTv2, wolTv3;// 玩家输赢金豆动画Tv
    private RelativeLayout play1PassLayout, play2PassLayout, play3PassLayout,
            dizhuPaiLayout = null;
    private ImageView zhadanImageView, wangzhaImageView, shunzImageView, feijiImageView;
    private int PLAY2ICON_ID, PLAY3ICON_ID, ZHEZHAO2_ID, ZHEZHAO3_ID, JIABEI2_ID, JIABEI3_ID;
    // 玩家位置
    private TextView play1Order, play2Order, play3Order = null;
    private RelativeLayout userinfoshowView = null; // playBusyLayout
    private TextView userInfoText = null;
    private ImageView messbtnView;
    private Activity ctx = null;
    private LinearLayout myFrame, rightFrame, leftFrame, girlLeftFrame, girlRightFrame;
    private TextView girlLeftTv, girlRightTv;
    private boolean canFlipper = true;
    private GameWaitView gameWaitLayout = null;
    private Button rankTop;// 排名按钮
    private ImageView baoXiangStar;// 宝箱上的星星
    private RelativeLayout baoXiangLayout;// 宝箱布局
    // private TextView baoText;
    // private int quang, allQuan = 0;
    // private boolean baoFlag = false;
    private LinearLayout publicLayout;
    private boolean isTurnMySelf = true; // 是否轮到自己
    private TextView networkSlowtip; // 网络慢提示语
    private boolean areButtonsShowing;
    private MainGameGuideView mainGameGuideVI;// 手势提示引导View
    private TextView gpType, gpRound, gpScore, gpCount, gpRank = null;
    // private ImageView slipIv;
    private LinearLayout gpRl;
    private ImageView zhadanIv; // 炸弹
    private Map<Integer, Boolean> warn = null; // 警告记录(是否有警告过)
    // private boolean baoxiangIsShow = false;// 没有宝物的提示已提示过(避免用户疯狂点，导致重复提示)
    private int type = 0;
    private RelativeLayout nextPlayLayout;
    private LinearLayout tilaLayout; // 踢拉父布局
    private Button tiLaBtn, buTiLaBtn;// 加倍，不加倍
    private ImageView jiabei1Iv, jiabei2Iv, jiabei3Iv;
    private TextView countdownTv;// 倒计时Tv
    private long countDownTime = 0;// 剩余时间
    private GenericTask checkJoinTask;
    private boolean hasCallReady = false;// 后台是否请求准备状态
    private List<Map<String, String>> girls;
    private ImageView girlItems;// 美女图鉴按钮
    private PopupWindow popupWindow;// 美女图鉴弹出框
    private ImageView imageNewIv;// 新美女图鉴提示标签
    private View popupWindow_view;
    private List<Map<String, String>> girlList;
    private GoodsValuesAdapter valueAdapter;
    private GridView girlimgList;
    private LinearLayout gridlLayout;
    private List<GamePropsType> toolList, usetool;
    private ViewFlipper viewFlipper = null;
    private GestureDetector gestureDetector = null;
    private Button back;
    boolean isLongClickModule = false;
    float startX, startY;
    Timer timer;
    private TextView beishuNumView = null;
    private TextView dishu = null; // 底数
    private String beishuNumber = null;
    private int tiShiCount = -1;
    private int curPage;// 当前第几张，做标志用
    private int jiaofenNum;
    private int jiao;// 手势点击（叫分）的次数
    private boolean selfIsMove = false;// 自己的头像是否移动过
    private static boolean newImageIsShow = false;// 美女组件更新标志是否显示
    private boolean jiao1 = false, jiao2 = false, jiao3 = false;// （手势）可以叫分标示
    private ClientData backData = null;// 发牌信息
    private int turnsCallOrder;// 当前叫分的人的位置
    private List<Poker> preUserPoker, nextUserPoker;
    private DouDiZhuLogic logicPre, logicNext;
    private Pritype preType = new Pritype();
    private Pritype nextType = new Pritype();
    private AutoTask massageTask;
    private boolean isFirstPlay = false;
    // private boolean isFirstCall = false;
    private int firstPlaySize = 0;
    private int oneTurns = 0;
    private int calledPoint = 0;
    private boolean isAdjust;
    private int curPlayOrder;
    private int prePlayOrder;
    private TextView iqTv1, iqTv2, iqTv3;// 等级值
    private List<ClientUser> users;
    private LinearLayout beanLayout;
    private TextView beanNum;
    /** 记牌器 **/
    private JiPaiQiTurnPlateView leftJiPaiQiTurnPlateView;
    private JiPaiQiTurnPlateView rightJiPaiQiTurnPlateView;
    private RecordPorkerView recordPorkerView;
    private Dialog jiPaiQiChargeDialog = null;
    private View topLeftUserView;
    private View topRightUserView;
    private View topLeftShieldView;
    private View topRightShieldView;
    private View cardStatView;
    private View leftJiPaiQiLayout;
    private View rightJiPaiQiLayout;
    private boolean isJiPaiQiEnable = false;
    // private String jiPaiQiTipsMsg;
    private Button btn_jipaiqi;
    // private List<Bitmap> bitmapList = null;
    private TextView text_kingb;
    private TextView text_kings;
    private TextView text_2;
    private TextView text_A;
    private TextView text_K;
    private TextView text_Q;
    private TextView text_J;
    private TextView text_10;
    private TextView text_9;
    private TextView text_8;
    private TextView text_7;
    private TextView text_6;
    private TextView text_5;
    private TextView text_4;
    private TextView text_3;
    private AdView mAdView;
    private int mBaseScore;
    public static final String TRANS_NAME = "BASE_SCORE";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doudizhu_gameview);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt("type");
        }

        mBaseScore = this.getIntent().getIntExtra(TRANS_NAME, 200);
        Room room = new Room();
        room.setBasePoint(mBaseScore);
        room.setRatio(1);
        Database.JOIN_ROOM = room;
        // 检测金豆
        initGame(type);
    }

    private void initGame(int type) {
        this.type = type;
        selfIsMove = false;
        newImageIsShow = false;
        ctx = this;
        AudioPlayUtils.isPlay = true;
        card_jiange = mst.adjustXIgnoreDensity(37);// 每一张牌的间隔
        warn = new HashMap<Integer, Boolean>();
        // 设置常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 初始化界面元素
        gestureDetector = new GestureDetector(this);
        initView();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        initHandler();
        gameWaitLayout = new GameWaitView(this, handler);
        doudizhuLayout.addView(gameWaitLayout, layoutParams);
        gameWaitLayout.setOnClickListener(null);

        mAdView = new AdView(this);
        FrameLayout.LayoutParams lp_Left_Bottom = new FrameLayout.LayoutParams(
                300,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        // 设置广告出现的位置，例如，悬浮于右下角(可选)
        // lp_Left_Bottom.gravity = Gravity.TOP ;
        lp_Left_Bottom.gravity = Gravity.CENTER | Gravity.RIGHT;
        addContentView(mAdView, lp_Left_Bottom);
        mAdView.setVisibility(View.INVISIBLE);
        // 显示用户信息
        setUserInfo();
        // 为叫地主的按钮设置监听器
        bujiao.setOnClickListener(clickListener); // 不叫地主
        fen1.setOnClickListener(clickListener); // 叫1分
        fen2.setOnClickListener(clickListener); // 叫2分
        fen3.setOnClickListener(clickListener); // 叫3fen
        tishi.setOnClickListener(clickListener); // 提示监听
        buchu.setOnClickListener(clickListener); // 不出
        chupai.setOnClickListener(clickListener); // 出牌
        wolTv1.setVisibility(View.GONE);
        wolTv2.setVisibility(View.GONE);
        wolTv3.setVisibility(View.GONE);
        messbtnView.setOnClickListener(clickListener);
        GameUser cacheUser = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
        if (cacheUser != null) {
        }
        // mGestureDetector = new GestureDetector(this);
        // joinGame(true);
        if (!isAdjust) {
            mst.adjustView(doudizhuLayout, false);
            isAdjust = true;
        }
        checkBeans();
    }

    /**
     * 校验游戏
     */
    OnClickListener clickListener = new OnClickListener() {

        public void onClick(View v) {
            int id = v.getId();
            // if (id == R.id.playimageView1 || id == R.id.zhezhao1) { // 位置1玩家图像
            // String p1o = play1Order.getText().toString();
            // }
            switch (id) {
                case R.id.chupai_button:// 出牌
                    setTishiGone();
                    playCard(false);
                    break;
                case R.id.pass_button:// 不要
                    setTishiGone();
                    passCard();
                    break;
                case R.id.tishi_button:// 提示
                    setTishi();
                    break;
                case R.id.fen1Button:
                    callPoint(1);
                    break;
                case R.id.fen2Button:
                    callPoint(2);
                    break;
                case R.id.fen3Button:
                    callPoint(3);
                    break;
                case R.id.bujiaoButton:// 不叫
                    callPoint(0);
                    break;
                case R.id.game_back:
                    DialogUtils.exitGame(ctx);
                    break;
                case R.id.game_robot:
                    gameRobotClick();
                    break;
                case R.id.game_set:
                    settingDialog.show();
                    settingDialog.setPro();
                    break;
                case R.id.tuo_guan_btn:
                case R.id.tuo_guan_layout:
                    cancelTuoGuan();
                    break;
                // case R.id.girl_right_frame:// 点击右边美女图片
                // handler.sendEmptyMessage(406);
                // break;
                // case R.id.girl_left_frame:// 点击左边美女图片
                // handler.sendEmptyMessage(405);
                // break;
                case R.id.bao_xiang_layout:// 宝箱
                    // clickBaoxiang();
                    break;
                case R.id.gp_top_btn:// 排名(快速赛制)
                    break;
                case R.id.game_gilr_items:
                    girlItems.setVisibility(View.INVISIBLE);
                    showPopWindow(true);
                    if (null != toolList) {
                        setImageNewGone(toolList.size());
                    }
                    break;
                case R.id.tila_button_2:// 加倍
                    callJiabei();
                    break;
                case R.id.bu_tila_button:// 不加倍
                    callBuJiaBei();
                    break;
                case R.id.image_new_iv:
                    // setImageNewGone();
                    break;
                case R.id.btn_jipaiqi:// 记牌器
                    if (0 != masterOrder) {
                        if (leftJiPaiQiLayout.getVisibility() == View.VISIBLE)
                            setJiPaiQiVisibility(false);
                        else
                            setJiPaiQiVisibility(true);
                    } else {
                        DialogUtils.mesToastTip("亲，叫地主前不能使用记牌器哟~！");
                    }
                    break;
            }
        }
    };

    /**
     * 不加倍
     */
    private void callBuJiaBei() {
        stopTimer(0); // 暂停定时器
        startTiLaTimer(1);
        // 发送"不加倍"信息
        CmdDetail chat2 = new CmdDetail();
        chat2.setCmd(CmdUtils.CMD_TILA);
        chat2.setDetail("false");
        CmdUtils.sendMessageCmd(chat2);
        tilaLayout.setVisibility(View.GONE);
        moveMyHead();
        // 提示"不加倍"声音
        AudioPlayUtils apu = AudioPlayUtils.getInstance();
        String gd1 = Database.userMap.get(mySelfOrder).getGender();
        if ("1".equals(gd1)) {// 女
            // 女声
            apu.playSound(R.raw.nv_bujiabei);
        } else {
            // 男声
            apu.playSound(R.raw.nan_bujiabei);
        }
        // 显示"加倍"，"不加倍"
        ImageView info1 = new ImageView(ctx);
        info1.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.not_doubling, true));
        play1PassLayout.removeAllViews();
        // play2PassLayout.removeAllViews();
        // play3PassLayout.removeAllViews();
        info1.setPadding(0, 0, 0, 60);
        play1PassLayout.addView(info1, mst.getAdjustLayoutParamsForImageView(info1));
        // play1PassLayout.addView(info1, info1.getLayoutParams());
        ActivityUtils.startScaleAnim(play1PassLayout, ctx);// 播放缩放动画
    }

    /**
     * 加倍
     */
    private void callJiabei() {
        stopTimer(0); // 暂停定时器
        startTiLaTimer(1);
        // 发送"加倍"信息，
        CmdDetail chat = new CmdDetail();
        chat.setCmd(CmdUtils.CMD_TILA);
        chat.setDetail("true");
        CmdUtils.sendMessageCmd(chat);
        tilaLayout.setVisibility(View.GONE);
        // 给自己头像加上x2
        jiabei1Iv.setVisibility(View.VISIBLE);
        setTweenAnim(jiabei1Iv, R.anim.jump, IS_HEAD_ANIM);
        // 提示"加倍"声音
        AudioPlayUtils apu2 = AudioPlayUtils.getInstance();
        String gd = Database.userMap.get(mySelfOrder).getGender();
        if ("1".equals(gd)) {// 女
            // 女声
            apu2.playSound(R.raw.nv_jiabei);
        } else {
            // 男声
            apu2.playSound(R.raw.nan_jiabei);
        }
        // 显示"加倍"，"不加倍"
        ImageView info = new ImageView(ctx);
        info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.jiabei_x_2, true));
        play1PassLayout.removeAllViews();
        // play2PassLayout.removeAllViews();
        // play3PassLayout.removeAllViews();
        info.setPadding(0, 0, 0, 60);
        play1PassLayout.addView(info, mst.getAdjustLayoutParamsForImageView(info));
        ActivityUtils.startScaleAnim(play1PassLayout, ctx);// 播放缩放动画
    }

    private void showPopWindow(final boolean isShow) {
        popThread = new Thread() {

            public void run() {
                if (usetool == null) {
                    // Map<String, String> paramMap = new HashMap<String, String>();
                    if (!TextUtils.isEmpty(GameCache.getStr(Constant.GAME_GIRL_CACHE))) {
                        String result = GameCache.getStr(Constant.GAME_GIRL_CACHE);
                        try {
                            usetool = JsonHelper.fromJson(result,
                                    new TypeToken<List<GamePropsType>>() {
                                    });
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (toolList == null) {
                                if (usetool != null && usetool.size() > 0) {
                                    List<GamePropsType> alllList = usetool;
                                    for (int i = 0; i < alllList.size(); i++) {
                                        boolean hasAll = true;
                                        if (alllList.get(i).getType().equals("1")) {
                                            if (null != ImageUtil.getGirlBitmap(HttpURL.URL_PIC_ALL
                                                    + alllList.get(i).getPicPath(), false, false)) {
                                                girlList = JsonHelper.fromJson(alllList.get(i)
                                                        .getContent(),
                                                        new TypeToken<List<Map<String, String>>>() {
                                                        });
                                                for (int j = 0; j < girlList.size(); j++) {
                                                    if (null != ImageUtil.getGirlBitmap(
                                                            HttpURL.URL_PIC_ALL
                                                                    + girlList.get(j).get("path"),
                                                            false, false)) {
                                                    } else {
                                                        hasAll = false;
                                                    }
                                                }
                                            } else {
                                                hasAll = false;
                                            }
                                            if (hasAll) {
                                            } else {
                                                usetool.get(i).setType("-2");
                                            }
                                        }
                                    }
                                }
                                toolList = new ArrayList<GamePropsType>();
                                if (usetool != null && usetool.size() > 0) {
                                    for (int i = 0; i < usetool.size(); i++) {
                                        if (usetool.get(i).getType().equals("1")) {
                                            toolList.add(usetool.get(i));
                                        }
                                    }
                                }
                                // 新建一个道具复原，type为"-1"
                                GamePropsType reback = new GamePropsType();
                                reback.setType("-1");
                                toolList.add(reback);
                            }
                            if (isShow) {
                                getPopupWindow();
                                popupWindow.showAsDropDown(findViewById(R.id.pop_iv), 0, 0);
                            } else {
                                setImageNewVisible(toolList.size());
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            };
        };
        popThread.start();
    }

    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    protected void initPopuptWindow() {
        // TODO Auto-generated method stub
        girlItems.setVisibility(View.INVISIBLE);
        popupWindow_view = getLayoutInflater().inflate(R.layout.pop, null, false);
        RelativeLayout layout = (RelativeLayout) popupWindow_view
                .findViewById(R.id.login_sliding_content);
        popupWindow = new PopupWindow(popupWindow_view, mst.adjustXIgnoreDensity(450),
                mst.adjustYIgnoreDensity(105), true);
        popupWindow.setFocusable(true);
        girlimgList = (GridView) popupWindow_view.findViewById(R.id.valuesgrid);
        girlimgList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridlLayout = (LinearLayout) popupWindow_view.findViewById(R.id.grid_layout);
        gridlLayout.setGravity(Gravity.CENTER_VERTICAL);
        int space = 6;
        int numColumn = 95;
        int size = toolList.size();
        android.widget.LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) girlimgList
                .getLayoutParams(); // 取控件mGrid当前的布局参数
        linearParams.width = size * (mst.adjustXIgnoreDensity(numColumn + space)) + 20;
        girlimgList.setLayoutParams(linearParams);
        girlimgList.setGravity(Gravity.CENTER_VERTICAL);
        girlimgList.setNumColumns(size);
        girlimgList.setColumnWidth(mst.adjustXIgnoreDensity(numColumn));
        girlimgList.setHorizontalSpacing(mst.adjustXIgnoreDensity(space));
        girlimgList.setStretchMode(GridView.NO_STRETCH);
        valueAdapter = new GoodsValuesAdapter(toolList);
        girlimgList.setAdapter(valueAdapter);
        // 道具点击事件，目前只有美女和复原
        girlimgList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posision, long arg3) {
                try {
                    if (toolList.get(posision).getType().equals("1")) {
                        viewFlipper.setVisibility(View.VISIBLE);
                        GameCache.putStr(Constant.GAME_BACKGROUND, toolList.get(posision)
                                .getContent());
                        initViewFlipper(toolList.get(posision).getContent());
                        girls = JsonHelper.fromJson(toolList.get(posision).getContent(),
                                new TypeToken<List<Map<String, String>>>() {
                                });
                        mainGameGuideVI.setArrowLeftRightVisible();
                    } else if (toolList.get(posision).getType().equals("-1")) {// 复原
                        GameCache.putStr(Constant.GAME_BACKGROUND, "");
                        viewFlipper.removeAllViews();
                        girls = null;
                        valueAdapter = null;
                        viewFlipper.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                }
                popoDismiss();
                ImageUtil.clearGirlBitMapCache();
            }
        });
        layout.setFocusableInTouchMode(true);// 能够获得焦点
        layout.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            popoDismiss();
                            break;
                    }
                }
                return true;
            }
        });
        back = (Button) popupWindow_view.findViewById(R.id.back_btn);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popoDismiss();
            }
        });
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        popupWindow_view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                popoDismiss();
                return false;
            }
        });
    }

    /**
     * 记录玩家已出牌
     * 
     * @param order 当前打牌的玩家顺序
     * @param mPokers 要出的牌
     */
    public void addOutPokers(int order, List<Poker> mPokers) {
        if (mPokers == null || order > 3 || order < 1)
            return;
        List<Poker> pokers = new ArrayList<Poker>();
        for (Poker poker : mPokers) {
            pokers.add(poker);
        }
        /** flag可为 1,2,3 1：代表自己出牌；2代表右手边玩家出牌；3代表左手边玩家出牌 **/
        int flag = 1;
        if (order == getPerOrder(mySelfOrder)) {
            flag = 3;
        } else if (order == getNextOrder(mySelfOrder)) {
            flag = 2;
        }
        switch (flag) {
            case 1:
                recordPorkerView.addCardList(pokers);
                break;
            case 2:
                rightJiPaiQiTurnPlateView.addCardList(pokers);
                break;
            case 3:
                leftJiPaiQiTurnPlateView.addCardList(pokers);
                break;
        }
    }

    public void refreshJiPaiQiAvatar() {
        Bitmap templeBitmap = ImageUtil.Drawable2Bitmap(play3Icon.getDrawable());
        if (null != templeBitmap)
            leftJiPaiQiTurnPlateView.setAvatar(templeBitmap);
        templeBitmap = ImageUtil.Drawable2Bitmap(play2Icon.getDrawable());
        if (null != templeBitmap)
            rightJiPaiQiTurnPlateView.setAvatar(templeBitmap);
    }

    public boolean isFirstTimeUseJiPaiQiOneDay() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        String timeString = ActivityUtils.getSharedValue(RecordPorkerView.JIPAIQI_USE_TIME);
        if (null != timeString) {
            Date date = new Date(Long.parseLong(timeString));
            if (date.getDate() != calendar.get(Calendar.DATE)) {
                isJiPaiQiEnable = true;
                ActivityUtils.addSharedValue(RecordPorkerView.JIPAIQI_USE_TIME,
                        String.valueOf(calendar.getTimeInMillis()));
            } else {
                isJiPaiQiEnable = false;
            }
        } else {
            isJiPaiQiEnable = true;
            ActivityUtils.addSharedValue(RecordPorkerView.JIPAIQI_USE_TIME,
                    String.valueOf(calendar.getTimeInMillis()));
        }
        return isJiPaiQiEnable;
    }

    public void setJiPaiQiVisibility(boolean isVisible) {
        if (isVisible) {
            topLeftUserView.setVisibility(View.INVISIBLE);
            topRightUserView.setVisibility(View.INVISIBLE);
            leftJiPaiQiLayout.setVisibility(View.VISIBLE);
            leftJiPaiQiTurnPlateView.invalidate();
            rightJiPaiQiLayout.setVisibility(View.VISIBLE);
            rightJiPaiQiTurnPlateView.invalidate();
            recordPorkerView.setVisibility(View.VISIBLE);
            recordPorkerView.invalidate();
            cardStatView.setVisibility(View.VISIBLE);
            topLeftShieldView.setVisibility(View.INVISIBLE);
            topRightShieldView.setVisibility(View.INVISIBLE);
            girlItems.setVisibility(View.INVISIBLE);
            imageNewIv.setVisibility(View.INVISIBLE);
        } else {
            leftJiPaiQiLayout.setVisibility(View.INVISIBLE);
            rightJiPaiQiLayout.setVisibility(View.INVISIBLE);
            recordPorkerView.setVisibility(View.INVISIBLE);
            cardStatView.setVisibility(View.INVISIBLE);
            topLeftUserView.setVisibility(View.VISIBLE);
            topRightUserView.setVisibility(View.VISIBLE);
            topLeftShieldView.setVisibility(View.VISIBLE);
            topRightShieldView.setVisibility(View.VISIBLE);
            girlItems.setVisibility(View.VISIBLE);
            imageNewIv.setVisibility(View.INVISIBLE);
        }
    }

    public void refreshCardCountData() {
        int count = 4;
        for (int i = 3; i <= 17; i++) {
            if (i == 16 || i == 17)
                count = 1;
            else
                count = 4;
            /** 左边玩家已出牌 **/
            for (List<Poker> leftPokerList : leftJiPaiQiTurnPlateView.getCardList()) {
                for (Poker mPoker : leftPokerList)
                    if (mPoker.getValue() == i)
                        count--;
            }
            /** 右边玩家已出牌 **/
            for (List<Poker> rightPokerList : rightJiPaiQiTurnPlateView.getCardList()) {
                for (Poker mPoker : rightPokerList)
                    if (mPoker.getValue() == i)
                        count--;
            }
            /** 自己已出牌 **/
            for (List<Poker> myPokerList : recordPorkerView.getCardList()) {
                for (Poker mPoker : myPokerList)
                    if (mPoker.getValue() == i)
                        count--;
            }
            /** 自己当前拥有的出牌 **/
            for (Poker myOwnPoker : nowcard) {
                if (myOwnPoker.getValue() == i)
                    count--;
            }
            switch (i) {
                case 3:
                    text_3.setText(String.valueOf(count));
                    break;
                case 4:
                    text_4.setText(String.valueOf(count));
                    break;
                case 5:
                    text_5.setText(String.valueOf(count));
                    break;
                case 6:
                    text_6.setText(String.valueOf(count));
                    break;
                case 7:
                    text_7.setText(String.valueOf(count));
                    break;
                case 8:
                    text_8.setText(String.valueOf(count));
                    break;
                case 9:
                    text_9.setText(String.valueOf(count));
                    break;
                case 10:
                    text_10.setText(String.valueOf(count));
                    break;
                case 11:
                    text_J.setText(String.valueOf(count));
                    break;
                case 12:
                    text_Q.setText(String.valueOf(count));
                    break;
                case 13:
                    text_K.setText(String.valueOf(count));
                    break;
                case 14:
                    text_A.setText(String.valueOf(count));
                    break;
                case 15:
                    text_2.setText(String.valueOf(count));
                    break;
                case 16:
                    text_kings.setText(String.valueOf(count));
                    break;
                case 17:
                    text_kingb.setText(String.valueOf(count));
                    break;
            }
        }
    }

    public void reSetJiPaiQiDataForRelink(String relinkString) {
    }

    public void clearJiPaiQiData() {
        isJiPaiQiEnable = false;
        rightJiPaiQiTurnPlateView.clearCardList();
        leftJiPaiQiTurnPlateView.clearCardList();
        recordPorkerView.clearCardList();
    }

    private void initViewFlipper(String girlList) {
        canFlipper = true;
        viewFlipper.removeAllViews();
        curPage = 0;
        girlView = null;
        girlView = new ArrayList<ImageView>();
        viewFlipper.setVisibility(View.VISIBLE);
        girls = JsonHelper.fromJson(girlList, new TypeToken<List<Map<String, String>>>() {
        });
        for (int i = 0; i < 3; i++) {
            ImageView image = new ImageView(PersonnalDoudizhuActivity.this);
            girlView.add(image);
        }
        for (int i = 0; i < girlView.size(); i++) {
            int point = curPage - 1;
            if (point < 0) {
                point = girls.size() - 1;
            }
            Drawable draw = ImageUtil.getcutBitmap(HttpURL.URL_PIC_ALL + girls.get(i).get("path"),
                    false);
            if (null != draw) {
                girlView.get(i).setBackgroundDrawable(draw);
                girlView.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
                viewFlipper.addView(girlView.get(i), new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));
            }
        }
        readdView();
    }

    private void popoDismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            // girlItems.setVisibility(View.VISIBLE);
            // back.setVisibility(View.INVISIBLE);
            popupWindow.dismiss();
            popupWindow_view = null;
            popupWindow = null;
            ScheduledTask.addDelayTask(new AutoTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            girlItems.setVisibility(View.VISIBLE);
                            if (!newImageIsShow) {
                                if (null != toolList) {
                                    setImageNewVisible(toolList.size());
                                }
                                newImageIsShow = true;
                            }
                        }
                    });
                }
            }, 600);
        }
    }

    public void sendTextMessage(String talk, int clickType) {
        if (talk.equals("")) {
            Toast.makeText(PersonnalDoudizhuActivity.this, "发送消息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (talk.contains(";")) {
            talk = talk.replaceAll("\\;", " ");
        }
        myFrame.removeAllViews();
        startTask(myFrame, selfTask);
        messageFrame(myFrame, talk, clickType, null);
    }

    /**
     * 托管
     */
    public void gameRobotClick() {
        tuoGuan.setClickable(true);
        isTuoguan = true;
        // 托管时的牌不可按
        for (int i = 0; i < myCardsTouchLayout.getChildCount(); i++) {
            myCardsTouchLayout.getChildAt(i).setClickable(false);
        }
        gameRobot.setClickable(false);
        // if (areButtonsShowing) {
        tuoGuanLayout.setVisibility(View.VISIBLE);
        CmdUtils.sendIsRobot();
        // AnimUtils.startAnimationsIn(tuoGuanLayout, 800);
        AnimUtils.startScaleAnimationIn(tuoGuanLayout, ctx);
        areButtonsShowing = !areButtonsShowing;
        // }
        if (playBtnLayout.getVisibility() == View.VISIBLE) { // 如果打牌的时候托管
            setTuoGuan();
        }
        if (jiaofenLayout.getVisibility() == View.VISIBLE) {// 如果叫地主的时候托管
            callPoint(0);
        }
        if (tilaLayout.getVisibility() == View.VISIBLE) {// 如果叫地主的时候托管
            callBuJiaBei();
        }
    }

    /**
     * 取消托管
     */
    public void cancelTuoGuan() {
        tuoGuan.setClickable(false);
        isTuoguan = false;
        // CmdUtils.sendCancelRobot();
        // AnimUtils.startAnimationsOut(tuoGuanLayout, 300);
        AnimUtils.startScaleAnimationOut(tuoGuanLayout, ctx);
        // 取消托管时的牌可按
        for (int i = 0; i < myCardsTouchLayout.getChildCount(); i++) {
            myCardsTouchLayout.getChildAt(i).setClickable(false);
        }
        gameRobot.setClickable(true);
    }

    /**
     * 叫分
     * 
     * @param point 叫的分数
     */
    private void callPoint(int point) {
        for (int i = 0; i < nowcard.size(); i++) {
            nowcard.get(i).ischeck = false;
        }
        jiaofenLayout.setVisibility(View.GONE);
        stopTimer(0);
        String gender = backData.getUsers().get(0).getGender();
        ImageView info = new ImageView(ctx);
        if (point == 0) {
            if ("1".equals(gender)) {
                AudioPlayUtils.getInstance().playSound(R.raw.nv_bujiao);
            } else {
                AudioPlayUtils.getInstance().playSound(R.raw.nan_bujiao); // 叫0分
            }
            info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.call_bujiao, true));
        } else if (point == 1) {
            if ("1".equals(gender)) {
                AudioPlayUtils.getInstance().playSound(R.raw.nv_1fen);
            } else {
                AudioPlayUtils.getInstance().playSound(R.raw.nan_1fen); // 叫1分
            }
            info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.callone, true));
        } else if (point == 2) {
            if ("1".equals(gender)) {
                AudioPlayUtils.getInstance().playSound(R.raw.nv_2fen);
            } else {
                AudioPlayUtils.getInstance().playSound(R.raw.nan_2fen); // 叫2分
            }
            info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.calltwo, true));
        } else if (point == 3) {
            AudioPlayUtils.getInstance().playSound(R.raw.nan_3fen); // 叫3分 产生地主
            calledPoint = 3;
            Message message = new Message();
            Grab master = new Grab();
            master.setRatio(point);
            master.setMasterOrder(mySelfOrder);
            message.what = 2;
            Bundle bundle = new Bundle();
            bundle.putSerializable("master", master);
            message.setData(bundle);
            handler.sendMessage(message);
            return;
        }
        if (point != 3) {
            play1PassLayout.removeAllViews();
            play1PassLayout.addView(info, mst.getAdjustLayoutParamsForImageView(info));
            ActivityUtils.startScaleAnim(play1PassLayout, ctx);// 播放缩放动画
        }
        if (point > calledPoint) {
            calledPoint = point;
        } else {
            point = 0;
        }
        if (0 != point) {
            beishuNumber = String.valueOf(point);
            beishuNumView.setText(beishuNumber);
        }
        backData.getUsers().get(0).setCallPoint(point);
        if (point < 3) { // 继续等待下家叫地主
            startQiangTimer(1); // 开启抢地主定时器
        }
        turnsCallPoint(turnsCallOrder);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        playOrStopBgMusic();
        // baoxiangIsShow = false;
        leftJiPaiQiTurnPlateView.setLocation(Location.Top_Left);
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioPlayUtils.isPlay = false;
        AudioPlayUtils.getInstance().stopBgMusic();
        AudioPlayUtils.getInstance().stopMusic();
    }

    /**
     * 开启或关闭背景音乐
     */
    private void playOrStopBgMusic() {
        new Thread() {

            public void run() {
                AudioPlayUtils.isPlay = true;
                SharedPreferences sharedPreferences = PreferenceHelper.getMyPreference()
                        .getSetting();
                if (!sharedPreferences.getBoolean("bgmusic", true)) {
                    AudioPlayUtils.getInstance().stopBgMusic();
                } else {
                    AudioManager audiomanage = (AudioManager) ctx
                            .getSystemService(Context.AUDIO_SERVICE);
                    int currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (currentVolume > 70)
                        currentVolume = 70;
                    AudioPlayUtils.getInstance().SetVoice(
                            sharedPreferences.getInt("music", currentVolume));// 如果没有设置过音量，就获取系统的音量
                    AudioPlayUtils.getInstance().playBgMusic(R.raw.mg_bg);
                }
            };
        }.start();
    }

    // 捕获实体按钮事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int nowVol = PreferenceHelper.getMyPreference().getSetting().getInt("music", 0);
            if (nowVol != 15) {
                PreferenceHelper.getMyPreference().getEditor().putInt("music", nowVol + 1);
                PreferenceHelper.getMyPreference().getEditor().commit();
                AudioPlayUtils.getInstance().SetVoice(
                        PreferenceHelper.getMyPreference().getSetting().getInt("music", 0));
            }
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int nowVol = PreferenceHelper.getMyPreference().getSetting().getInt("music", 0);
            if (nowVol != 0) {
                PreferenceHelper.getMyPreference().getEditor().putInt("music", nowVol - 1);
                PreferenceHelper.getMyPreference().getEditor().commit();
                AudioPlayUtils.getInstance().SetVoice(
                        PreferenceHelper.getMyPreference().getSetting().getInt("music", 0));
            }
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (popupWindow != null && popupWindow.isShowing()) {
                girlItems.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
                ImageUtil.clearGirlBitMapCache();
                popupWindow = null;
            } else {
                DialogUtils.exitGame(PersonnalDoudizhuActivity.this);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            AudioPlayUtils.isPlay = false;
            AudioPlayUtils.getInstance().stopBgMusic();
            AudioPlayUtils.getInstance().stopMusic();
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.ctx = null;
        this.handler = null;
        gestureDetector = null;
        if (popThread != null) {
            popThread.interrupt();
            // popThread = null;
        }
        AudioPlayUtils.getInstance().stopMusic();
        AudioPlayUtils.getInstance().stopBgMusic();
        gameWaitLayout.closeTimer();
        if (gameWaitLayout != null) {
            gameWaitLayout.onDestory();
        }
        gameWaitLayout = null;
        if (checkJoinTask != null) {
            checkJoinTask.cancel(true);
            checkJoinTask.setFeedback(null);
            checkJoinTask.setListener(null);
        }
        checkJoinTask = null;
        if (selfTask != null) {
            selfTask.stop(true);
            selfTask = null;
        }
        if (leftTask != null) {
            leftTask.stop(true);
            leftTask = null;
        }
        if (rightTask != null) {
            rightTask.stop(true);
            rightTask = null;
        }
        if (pubTask != null) {
            pubTask.stop(true);
            pubTask = null;
        }
        if (task2 != null) {
            task2.stop(true);
            task2 = null;
        }
        if (adTask != null) {
            adTask.stop(true);
            adTask = null;
        }
        if (headTask != null) {
            headTask.stop(true);
            headTask = null;
        }
        if (baoXiangTask != null) {
            baoXiangTask.stop(true);
            baoXiangTask = null;
        }
        cancelTimer();
        ImageUtil.releaseDrawable(doudizhuBackGround.getBackground());// 释放背景图片占的内存
        ImageUtil.releaseDrawable(play1Icon.getBackground());
        ImageUtil.releaseDrawable(play3Icon.getBackground());
        ImageUtil.releaseDrawable(play2Icon.getBackground());
        // ImageUtil.releaseDrawable(zhezhao1.getBackground());
        // ImageUtil.releaseDrawable(zhezhao3.getBackground());
        // ImageUtil.releaseDrawable(zhezhao2.getBackground());
        marqueeText.onDestory();
        marqueeText = null;
        if (null != cashList && cashList.size() > 0) {
            for (int i = 0; i < cashList.size();) {
                if (null != cashList.get(i) && !cashList.get(i).getImage().isRecycled()) {
                    cashList.get(i).getImage().recycle();
                }
                cashList.remove(i);
                i = 0;
            }
            cashList.clear();
        }
        cashList = null;
        settingDialog = null;
        pai = null;
        paixu = null;
        bierenchupai = null;
        if (null != warn) {
            warn.clear();
            warn = null;
        }
        myFrame.removeAllViews();
        myFrame = null;
        rightFrame.removeAllViews();
        rightFrame = null;
        leftFrame.removeAllViews();
        leftFrame = null;
        girlLeftFrame.removeAllViews();
        girlLeftFrame = null;
        girlRightFrame.removeAllViews();
        girlRightFrame = null;
        clickListener = null;
        if (null != nowcard) {
            for (Poker card : nowcard) {
                if (card != null) {
                    card.onDestory();
                }
                card = null;
            }
            nowcard.clear();
            nowcard = null;
        }
        if (null != chupaicard) {
            for (Poker card : chupaicard) {
                if (card != null) {
                    card.onDestory();
                }
                card = null;
            }
            chupaicard.clear();
            chupaicard = null;
        }
        if (null != checkpai) {
            for (Poker card : checkpai) {
                if (card != null) {
                    card.onDestory();
                }
                card = null;
            }
            checkpai.clear();
            checkpai = null;
        }
        if (null != otherplay1) {
            for (Poker card : otherplay1) {
                if (card != null) {
                    card.onDestory();
                }
                card = null;
            }
            otherplay1.clear();
            otherplay1 = null;
        }
        // 释放所有扑克牌所占的资源
        if (poker != null) {
            for (Poker card : poker) {
                if (card != null) {
                    card.onDestory();
                }
                card = null;
            }
            poker = null;
        }
        cleanAllChuPaiInfo();
        play1PassLayout = null;
        play2PassLayout = null;
        play3PassLayout = null;
        dizhuPaiLayout.removeAllViews();
        dizhuPaiLayout = null;
        // mGestureDetector = null;
        if (myCardsTouchLayout != null) {
            myCardsTouchLayout.setListenner(null);
            myCardsTouchLayout.onDestory();
        }
        myCardsTouchLayout = null;
        if (nextPlayLayout != null) {
            nextPlayLayout.removeAllViews();
            nextPlayLayout = null;
        }
        userInfoText = null;
        networkSlowtip = null;
        bujiao.setOnClickListener(null); // 不叫地主
        fen1.setOnClickListener(null); // 叫1分
        fen2.setOnClickListener(null); // 叫2分
        fen3.setOnClickListener(null); // 叫3fen
        tishi.setOnClickListener(null); // 提示监听
        buchu.setOnClickListener(null); // 不出
        chupai.setOnClickListener(null); // 出牌
        messbtnView.setOnClickListener(null);
        play1Icon.setOnClickListener(null);
        play2Icon.setOnClickListener(null);
        play3Icon.setOnClickListener(null);
        zhezhao1.setOnClickListener(null);
        zhezhao2.setOnClickListener(null);
        zhezhao3.setOnClickListener(null);
        baoXiangLayout.setOnClickListener(null);
        // gameBack.setOnClickListener(null);
        gameRobot.setOnClickListener(null);
        gameSet.setOnClickListener(null);
        tuoGuan.setOnClickListener(null);
        jiaofenLayout.removeAllViews();
        jiaofenLayout = null;
        playBtnLayout.removeAllViews();
        playBtnLayout = null;
        tuoGuanLayout.removeAllViews();
        tuoGuanLayout = null;
        gpRank = null;
        gpType = null;
        gpRound = null;
        gpScore = null;
        gpCount = null;
        play1SurplusCount = null;
        play3SurplusCount = null;
        play2SurplusCount = null;
        ImageUtil.releaseDrawable(play1Timer.getBackground());
        ImageUtil.releaseDrawable(play2Timer.getBackground());
        ImageUtil.releaseDrawable(play3Timer.getBackground());
        if (gameTask != null) {
            gameTask.stop(true);
            gameTask = null;
        }
        play1Timer = null;
        play2Timer = null;
        play3Timer = null;
        ImageUtil.releaseDrawable(girlLeftTv.getBackground());
        ImageUtil.releaseDrawable(girlRightTv.getBackground());
        girlLeftTv = null;
        girlRightTv = null;
        playTextView1 = null;
        playTextView3 = null;
        playTextView2 = null;
        if (viewFlipper != null) {
            if (girls != null) {
                for (int i = 0; i < girls.size(); i++) {// 释放没有调用的bitmap
                    ImageUtil.clearsingleCache(HttpURL.URL_PIC_ALL + girls.get(i).get("path"));
                }
            }
            viewFlipper.removeAllViews();
            girls = null;
            valueAdapter = null;
        }
        ImageUtil.releaseDrawable(play1Order.getBackground());
        ImageUtil.releaseDrawable(play2Order.getBackground());
        ImageUtil.releaseDrawable(play3Order.getBackground());
        play1Order = null;
        play2Order = null;
        play3Order = null;
        beishuNumView = null;
        zhidou = null;
        ImageUtil.releaseDrawable(play1Icon.getBackground());
        ImageUtil.releaseDrawable(play2Icon.getBackground());
        ImageUtil.releaseDrawable(play3Icon.getBackground());
        play1Icon = null;
        play2Icon = null;
        play3Icon = null;
        // ImageUtil.releaseDrawable(zhezhao1.getBackground());
        // ImageUtil.releaseDrawable(zhezhao2.getBackground());
        // ImageUtil.releaseDrawable(zhezhao3.getBackground());
        zhezhao1 = null;
        zhezhao2 = null;
        zhezhao3 = null;
        // baoText = null;
        ImageUtil.releaseDrawable(zhadanImageView.getBackground());
        ImageUtil.releaseDrawable(wangzhaImageView.getBackground());
        ImageUtil.releaseDrawable(shunzImageView.getBackground());
        ImageUtil.releaseDrawable(feijiImageView.getBackground());
        zhadanImageView = null;
        wangzhaImageView = null;
        shunzImageView = null;
        feijiImageView = null;
        publicLayout.removeAllViews();
        publicLayout = null;
        doudizhuBackGround.setOnTouchListener(null);
        doudizhuBackGround.removeAllViews();
        doudizhuBackGround.removeAllViewsInLayout();
        doudizhuBackGround = null;
        doudizhuLayout.setOnClickListener(null);
        doudizhuLayout.removeAllViews();
        doudizhuLayout.removeAllViewsInLayout();
        doudizhuLayout = null;
        gpRl.removeAllViews();
        gpRl = null;
        if (userinfoshowView != null) {
            userinfoshowView.removeAllViews();
            userinfoshowView = null;
        }
        // ImageUtil.clearImageWeakMap();
        ActivityPool.remove(this);
        // GameUser cacheUser = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
        masterOrder = 0;
    }

    /**
     * 设置提示
     */
    public void setTishi() {
        for (int i = 0; i < nowcard.size(); i++) {
            poker[nowcard.get(i).getNumber()].params.topMargin = mst.adjustYIgnoreDensity(20);
            poker[nowcard.get(i).getNumber()]
                    .setLayoutParams(poker[nowcard.get(i).getNumber()].params);
            poker[nowcard.get(i).getNumber()].ischeck = false;
        }
        boolean arrowUp = false;// 向上引导布局
        if (bierenchupai == null) {
            DouDiZhuData data = new DouDiZhuData(nowcard);
            DouDiZhuData datas = new DouDiZhuData(nowcard);
            data.fillPokerList();
            List<List<Poker>> tishiList = data.getTiShi();
            // List<List<Poker>> tishiList = data.getTiShi(otherplay1);
            datas.fillAllPokerList();
            List<List<Poker>> tishiList2 = datas.getTiShi();
            // List<List<Poker>> tishiList = new ArrayList<List<Poker>>();
            HintPokerUtil aList = new HintPokerUtil();
            tishiList = aList.filterHintPoker(tishiList, tishiList2);
            if (tishiList == null || tishiList.size() == 0) {
                poker[nowcard.get(nowcard.size() - 1).getNumber()].params.topMargin = 0;
                poker[nowcard.get(nowcard.size() - 1).getNumber()].setLayoutParams(poker[nowcard
                        .get(nowcard.size() - 1).getNumber()].params);
                poker[nowcard.get(nowcard.size() - 1).getNumber()].ischeck = true;
            }
            setTiShiCount();
            if (getTiShiCount() > tishiList.size() - 1) {
                initTiShiCount();
                setTiShiCount();
            }
            List<Poker> tiShiPoker = tishiList.get(getTiShiCount());
            for (int i = 0; i < tiShiPoker.size(); i++) {
                poker[tiShiPoker.get(i).getNumber()].params.topMargin = 0;
                poker[tiShiPoker.get(i).getNumber()].setLayoutParams(poker[tiShiPoker.get(i)
                        .getNumber()].params);
                poker[tiShiPoker.get(i).getNumber()].ischeck = true;
                arrowUp = true;
            }
        } else {
            checkOtherChupai(bierenchupai);
            // int tishi[] = DoudizhuRule.GettiShi(otherplay1, nowcard);
            DouDiZhuData data = new DouDiZhuData(nowcard);
            DouDiZhuData datas = new DouDiZhuData(nowcard);
            data.fillPokerList();
            List<List<Poker>> tishiList = data.getTiShi(otherplay1);
            // List<List<Poker>> tishiList = data.getTiShi(otherplay1);
            checkOtherChupai(bierenchupai);
            datas.fillAllPokerList();
            List<List<Poker>> tishiList2 = datas.getTiShi(otherplay1);
            // List<List<Poker>> tishiList = new ArrayList<List<Poker>>();
            HintPokerUtil aList = new HintPokerUtil();
            if (tishiList != null && tishiList2 != null) {
                tishiList = aList.filterHintPoker(tishiList, tishiList2);
            }
            if (tishiList == null) {
                passCard();
                setTishiGone();
                initTiShiCount();
                return;
            }
            if (tishiList != null && tishiList.size() == 0) {
                passCard();
                setTishiGone();
                initTiShiCount();
                return;
            }
            // if(tiShiCount >= 0 ){
            // tishi.setBackgroundResource(R.drawable.reselect);
            // }else {
            // tishi.setBackgroundResource(R.drawable.tishi);
            // }
            setTiShiCount();
            if (getTiShiCount() > tishiList.size() - 1) {
                initTiShiCount();
                setTiShiCount();
            }
            List<Poker> tiShiPoker = tishiList.get(getTiShiCount());
            for (int i = 0; i < tiShiPoker.size(); i++) {
                poker[tiShiPoker.get(i).getNumber()].params.topMargin = 0;
                poker[tiShiPoker.get(i).getNumber()].setLayoutParams(poker[tiShiPoker.get(i)
                        .getNumber()].params);
                poker[tiShiPoker.get(i).getNumber()].ischeck = true;
                arrowUp = true;
            }
        }
        // 有牌弹出且没托管的情况下就弹出
        if (arrowUp && tuoGuanLayout.getVisibility() != View.VISIBLE) {
            mainGameGuideVI.setArrowUpVisible();
            mainGameGuideVI.setDoublePointVisible();
        }
    }

    /**
     * 设置是否5秒倒计时
     */
    public void isWaitFiveSecond() {
        if (bierenchupai != null && bierenchupai.length > 0) {
            checkOtherChupai(bierenchupai);
            DouDiZhuData data = new DouDiZhuData(nowcard);
            DouDiZhuData datas = new DouDiZhuData(nowcard);
            data.fillPokerList();
            List<List<Poker>> tishiList = data.getTiShi(otherplay1);
            // List<List<Poker>> tishiList = data.getTiShi(otherplay1);
            checkOtherChupai(bierenchupai);
            datas.fillAllPokerList();
            List<List<Poker>> tishiList2 = datas.getTiShi(otherplay1);
            // List<List<Poker>> tishiList = new ArrayList<List<Poker>>();
            HintPokerUtil aList = new HintPokerUtil();
            if (tishiList != null && tishiList2 != null) {
                tishiList = aList.filterHintPoker(tishiList, tishiList2);
            }
            if (tishiList == null) {
                isWait5Second = true;
            }
            if (tishiList != null && tishiList.size() == 0) {
                isWait5Second = true;
            }
        }
        if (isWait5Second) {
            startPlayTimer(R.id.play1Time);
            mainGameGuideVI.setArrowDownVisible();
            View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.setMargin(0f, 0.1f);
            toast.setView(toastRoot);
            TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
            tv.setText("");
            // toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            TextView now = (TextView) findViewById(R.id.play1Time);
            now.setText("5");
        }
    }

    /**
     * 把自己的牌显示出来
     * 
     * @param now
     */
    public void addCard(int[] now) {
        myCardsTouchLayout.removeAllViews();
        paixu = DoudizhuRule.sort(now, poker);
        for (int i = 0; i < paixu.length; i++) {
            Poker card = poker[paixu[i]];
            card.getPokeImage().setImageDrawable(
                    ImageUtil.getResDrawable(poker[paixu[i]].getBitpamResID(), true));
            card.setId(i + 100);
            card_jiange = (now != null && now.length > 1) ? ((int) (800 - 90) / (now.length - 1))
                    : card_jiange;
            if (card_jiange > 50) {
                card_jiange = 50;
            }
            myCardsTouchLayout.setDistance(mst.adjustXIgnoreDensity(card_jiange));
            card.params.leftMargin = mst.adjustXIgnoreDensity((card_jiange) * i);
            card.params.topMargin = mst.adjustYIgnoreDensity(20);
            card.getInnerLayout().setBackgroundDrawable(
                    ImageUtil.getResDrawable(R.drawable.poker_div, true));
            card.getInnerLayout().setVisibility(View.GONE);
            card.setClickable(false);
            myCardsTouchLayout.addView(card, card.params);
            nowcard.add(card);
            /** 刷新记牌器数据 **/
            refreshCardCountData();
        }
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        Typeface num = Typeface.createFromAsset(getAssets(), "fonts/NUM.ttf");
        poker = PokerUtil.getPoker(this);
        nowcard = new ArrayList<Poker>();
        chupaicard = new ArrayList<Poker>();
        otherplay1 = new ArrayList<Poker>();
        cashList = new ArrayList<BitmapVO>();
        iqTv1 = (TextView) findViewById(R.id.iq1_tv);
        iqTv2 = (TextView) findViewById(R.id.iq2_tv);
        iqTv3 = (TextView) findViewById(R.id.iq3_tv);
        iqTv1.setVisibility(View.INVISIBLE);
        iqTv2.setVisibility(View.INVISIBLE);
        iqTv3.setVisibility(View.INVISIBLE);// 隐藏等级值
        beanLayout = (LinearLayout) findViewById(R.id.presonal_bean_layout);// 单机金豆
        beanLayout.setVisibility(View.VISIBLE);
        beanNum = (TextView) findViewById(R.id.presonal_bean);
        girlItems = (ImageView) findViewById(R.id.game_gilr_items);// 美女图鉴
        girlItems.setOnClickListener(clickListener);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);// 美女背景滑动
        imageNewIv = (ImageView) findViewById(R.id.image_new_iv);
        imageNewIv.setVisibility(View.GONE);
        imageNewIv.setOnClickListener(clickListener);
        publicLayout = (LinearLayout) findViewById(R.id.dzm_public_mess_layout);
        publicLayout.getBackground().setAlpha(85);
        messbtnView = (ImageView) findViewById(R.id.game_set);
        chupai = (Button) findViewById(R.id.chupai_button);
        buchu = (Button) findViewById(R.id.pass_button);
        fen1 = (Button) findViewById(R.id.fen1Button);
        fen2 = (Button) findViewById(R.id.fen2Button);
        fen3 = (Button) findViewById(R.id.fen3Button);
        bujiao = (Button) findViewById(R.id.bujiaoButton);
        tishi = (Button) findViewById(R.id.tishi_button);
        tilaLayout = (LinearLayout) findViewById(R.id.tila_ll);
        tiLaBtn = (Button) findViewById(R.id.tila_button_2);
        tiLaBtn.setOnClickListener(clickListener);
        buTiLaBtn = (Button) findViewById(R.id.bu_tila_button);
        buTiLaBtn.setOnClickListener(clickListener);
        jiabei1Iv = (ImageView) findViewById(R.id.double1_iv);
        jiabei2Iv = (ImageView) findViewById(R.id.double2_iv);
        jiabei3Iv = (ImageView) findViewById(R.id.double3_iv);
        countdownTv = (TextView) findViewById(R.id.countdown);
        play1SurplusCount = (TextView) findViewById(R.id.play1_surplus_count);
        play2SurplusCount = (TextView) findViewById(R.id.play2_surplus_count);
        play3SurplusCount = (TextView) findViewById(R.id.play3_surplus_count);
        play1SurplusCount.setTypeface(num);
        play2SurplusCount.setTypeface(num);
        play3SurplusCount.setTypeface(num);
        play1Timer = (TextView) findViewById(R.id.play1Time);
        play2Timer = (TextView) findViewById(R.id.play2Time);
        play3Timer = (TextView) findViewById(R.id.play3Time);
        zhidou = (TextView) findViewById(R.id.zhidou_TextView);
        myCardsTouchLayout = (TouchRelativeLayout) findViewById(R.id.play_cards);
        myCardsTouchLayout.setListenner(this);
        myCardsTouchLayout.setDistance(mst.adjustXIgnoreDensity(card_jiange));
        play1PassLayout = (RelativeLayout) findViewById(R.id.play1_pass_card);
        play2PassLayout = (RelativeLayout) findViewById(R.id.play2pass_card);
        play3PassLayout = (RelativeLayout) findViewById(R.id.play3_pass_card);
        jiaofenLayout = (LinearLayout) findViewById(R.id.jiaofenRelative);
        // waitingLayout = (RelativeLayout) findViewById(R.id.waiting_layout);
        dizhuPaiLayout = (RelativeLayout) findViewById(R.id.dizhucard);
        playBtnLayout = (LinearLayout) findViewById(R.id.play_choice);
        doudizhuLayout = (RelativeLayout) findViewById(R.id.doudizhu_layout);
        doudizhuBackGround = (RelativeLayout) findViewById(R.id.doudizhugame_relative);
        // doudizhuBackGround.setOnTouchListener(this);
        play1Icon = (ImageView) findViewById(R.id.playimageView1);
        play1Icon.setOnClickListener(clickListener);
        zhezhao1 = (ImageView) findViewById(R.id.zhezhao1);
        zhezhao1.setOnClickListener(clickListener);
        zhezhao1.setVisibility(View.GONE);
        play2Icon = (ImageView) findViewById(R.id.playimageView2);
        play2Icon.setOnClickListener(clickListener);
        zhezhao2 = (ImageView) findViewById(R.id.zhezhao2);
        zhezhao2.setOnClickListener(clickListener);
        zhezhao2.setVisibility(View.GONE);
        play3Icon = (ImageView) findViewById(R.id.playimageView3);
        play3Icon.setOnClickListener(clickListener);
        zhezhao3 = (ImageView) findViewById(R.id.zhezhao3);
        zhezhao3.setOnClickListener(clickListener);
        zhezhao3.setVisibility(View.GONE);
        beishuNumView = (TextView) findViewById(R.id.beishunumber);
        dishu = (TextView) findViewById(R.id.dishunumber);
        baoXiangLayout = (RelativeLayout) findViewById(R.id.bao_xiang_layout);
        baoXiangLayout.setOnClickListener(clickListener);
        baoXiangLayout.setVisibility(View.GONE);
        baoXiangStar = (ImageView) findViewById(R.id.bao_xiang_star);
        baoXiangStar.setVisibility(View.INVISIBLE);
        // baoText = (TextView) findViewById(R.id.bao_xiang_text);
        // mySelfHeadRl = (RelativeLayout) findViewById(R.id.game_self_ll);
        nullTv = (TextView) findViewById(R.id.null_tv);
        nullTv.setVisibility(View.GONE);
        nullTv2 = (TextView) findViewById(R.id.null_tv2);
        nullTv2.setVisibility(View.VISIBLE);
        playTextView1 = (TextView) findViewById(R.id.playTextView1);
        playTextView2 = (TextView) findViewById(R.id.playTextView2);
        playTextView3 = (TextView) findViewById(R.id.playTextView3);
        wolTv1 = (TextView) findViewById(R.id.play1_winners_or_losers_tv);
        wolTv2 = (TextView) findViewById(R.id.play2_winners_or_losers_tv);
        wolTv3 = (TextView) findViewById(R.id.play3_winners_or_losers_tv);
        wolTv1.setVisibility(View.GONE);
        wolTv2.setVisibility(View.GONE);
        wolTv3.setVisibility(View.GONE);
        wolTv1.setTypeface(num);
        wolTv2.setTypeface(num);
        wolTv3.setTypeface(num);
        play1Order = (TextView) findViewById(R.id.play1Order);
        play2Order = (TextView) findViewById(R.id.play2Order);
        play3Order = (TextView) findViewById(R.id.play3Order);
        marqueeText = (MarqueeText) findViewById(R.id.public_textview);
        adWidgetLayoutParam = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        zhadanImageView = (ImageView) findViewById(R.id.play_anim_layout_zhadan);
        shunzImageView = (ImageView) findViewById(R.id.play_anim_layout_shunzi);
        feijiImageView = (ImageView) findViewById(R.id.play_anim_layout_feiji);
        wangzhaImageView = (ImageView) findViewById(R.id.play_anim_layout_wangzha);
        userinfoshowView = (RelativeLayout) findViewById(R.id.playinfoview);
        userInfoText = (TextView) findViewById(R.id.userinfotext);
        // 游戏界面返回键，暂时去掉
        // gameBack = (ImageButton) findViewById(R.id.game_back);
        // gameBack.setOnClickListener(clickListener);
        gameRobot = (ImageButton) findViewById(R.id.game_robot);
        gameRobot.setOnClickListener(clickListener);
        // 判断可否短信充值
        // if (ActivityUtils.getSimType() == Constant.SIM_OTHER || SdkDatabase.SMS_FAILURE == true
        // || SdkDatabase.SMS_LTDX_SET.equals("0")) {
        // gamePay.setBackgroundResource(R.drawable.mianpage_pay_no);
        // gamePay.setClickable(false);
        // } else if (ActivityUtils.getSimType() == Constant.SIM_YI &&
        // (SdkDatabase.SMS_MM_SET.equals("0") || SdkDatabase.SMS_MM_SET.equals("2"))) {
        // gamePay.setBackgroundResource(R.drawable.mianpage_pay_no);
        // gamePay.setClickable(false);
        // }
        gameSet = (ImageButton) findViewById(R.id.game_set);
        gameSet.setOnClickListener(clickListener);
        tuoGuanLayout = (RelativeLayout) findViewById(R.id.tuo_guan_layout);
        tuoGuanLayout.setOnClickListener(clickListener);
        tuoGuan = (Button) findViewById(R.id.tuo_guan_btn);
        tuoGuan.setOnClickListener(clickListener);
        myFrame = (LinearLayout) findViewById(R.id.my_frame);
        rightFrame = (LinearLayout) findViewById(R.id.right_frame);
        leftFrame = (LinearLayout) findViewById(R.id.left_frame);
        girlLeftFrame = (LinearLayout) findViewById(R.id.girl_left_frame);
        girlLeftFrame.setOnClickListener(clickListener);
        girlLeftTv = (TextView) findViewById(R.id.gir_left_tv);
        girlRightFrame = (LinearLayout) findViewById(R.id.girl_right_frame);
        girlRightFrame.setOnClickListener(clickListener);
        networkSlowtip = (TextView) findViewById(R.id.network_slow_tip);
        networkSlowtip.getBackground().setAlpha(85);
        girlRightTv = (TextView) findViewById(R.id.gir_right_tv);
        mainGameGuideVI = (MainGameGuideView) findViewById(R.id.main_game_guide_view);
        mainGameGuideVI.setVisibility(View.GONE);
        settingDialog = new SettingDialog(ctx) {

            public void setDismiss() {
                super.setDismiss();
                settingDialog.dismiss();
            }
        };
        // 设置游戏的背景
        doudizhuBackGround
                .setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.gamebg, false));
        // 设置选定的背景图
        if (!TextUtils.isEmpty(GameCache.getStr(Constant.GAME_BACKGROUND))) {
            initViewFlipper(GameCache.getStr(Constant.GAME_BACKGROUND));
        }
        beishuNumView.setText("1"); // 房间默认倍数
        dishu.setText(String.valueOf(mBaseScore)); // 房间默认底数
        gpRl = (LinearLayout) findViewById(R.id.doudizhu_gp_rl);
        gpCount = (TextView) findViewById(R.id.doudizhu_kuai_img);
        gpType = (TextView) findViewById(R.id.doudizhu_game_type_tv);
        gpRound = (TextView) findViewById(R.id.doudizhu_game_round_tv);
        gpScore = (TextView) findViewById(R.id.doudizhu_game_sorce_tv);
        gpRank = (TextView) findViewById(R.id.doudizhu_game_rank_tv);
        // slipIv = (ImageView) findViewById(R.id.doudizhu_slip_img);
        zhadanIv = (ImageView) findViewById(R.id.zhadan_iv);
        setViewInitData();
        rankTop = (Button) findViewById(R.id.gp_top_btn);
        rankTop.setOnClickListener(clickListener);
        rankTop.setVisibility(View.GONE);
        // 注册广播，用于接收广播刷新系统时间,计算比赛倒计时
        // if (0 != Database.JOIN_ROOM.getRoomType()) {// 除了大厅房，都显示倒计时
        // IntentFilter intentFilter = new IntentFilter();
        // intentFilter.addAction(Constant.SYSTEM_TIME_CHANGE_ACTION);
        // mReciver = new MyBroadcastReciver();
        // this.registerReceiver(mReciver, intentFilter);
        // }
        /** 记牌器 **/
        leftJiPaiQiLayout = findViewById(R.id.jipaiqi_layout_left);
        rightJiPaiQiLayout = findViewById(R.id.jipaiqi_layout_right);
        leftJiPaiQiTurnPlateView = (JiPaiQiTurnPlateView) findViewById(R.id.jipaiqi_left);
        leftJiPaiQiTurnPlateView.setLocation(Location.Top_Left);
        rightJiPaiQiTurnPlateView = (JiPaiQiTurnPlateView) findViewById(R.id.jipaiqi_right);
        leftJiPaiQiTurnPlateView.setLocation(Location.Top_Right);
        recordPorkerView = (RecordPorkerView) findViewById(R.id.jipaiqi_record_view);
        topRightShieldView = findViewById(R.id.dun_layout_right);
        cardStatView = findViewById(R.id.layout_jipaiqi);
        btn_jipaiqi = (Button) findViewById(R.id.btn_jipaiqi);
        setJipaiqiAvailableOrNotAvailable();
        btn_jipaiqi.setOnClickListener(clickListener);
        topLeftUserView = findViewById(R.id.top_left_head);
        topRightUserView = findViewById(R.id.top_right_head);
        topLeftShieldView = findViewById(R.id.dun_layout_left);
        topRightShieldView = findViewById(R.id.dun_layout_right);
        initCardCountLayout();
    }

    public void initCardCountLayout() {
        text_kingb = (TextView) findViewById(R.id.pork_kingb);
        text_kings = (TextView) findViewById(R.id.pork_kings);
        text_2 = (TextView) findViewById(R.id.pork_2);
        text_A = (TextView) findViewById(R.id.pork_A);
        text_K = (TextView) findViewById(R.id.pork_K);
        text_Q = (TextView) findViewById(R.id.pork_Q);
        text_J = (TextView) findViewById(R.id.pork_J);
        text_10 = (TextView) findViewById(R.id.pork_10);
        text_9 = (TextView) findViewById(R.id.pork_9);
        text_8 = (TextView) findViewById(R.id.pork_8);
        text_7 = (TextView) findViewById(R.id.pork_7);
        text_6 = (TextView) findViewById(R.id.pork_6);
        text_5 = (TextView) findViewById(R.id.pork_5);
        text_4 = (TextView) findViewById(R.id.pork_4);
        text_3 = (TextView) findViewById(R.id.pork_3);
    }

    /**
     * 给View设置初始值
     */
    private void setViewInitData() {
        play1Icon.setImageDrawable(ImageUtil.getResDrawable(R.drawable.nongmin, true));
        play2Icon.setImageDrawable(ImageUtil.getResDrawable(R.drawable.nongmin, true));
        play3Icon.setImageDrawable(ImageUtil.getResDrawable(R.drawable.nongmin, true));
        play1SurplusCount.setText("17");
        play2SurplusCount.setText("17");
        play3SurplusCount.setText("17");
        bierenchupai = null;
        fen1.setBackgroundResource(R.drawable.fen1_btn_bg);
        fen2.setBackgroundResource(R.drawable.fen2_btn_bg);
        fen3.setBackgroundResource(R.drawable.fen3_btn_bg);
        jiao1 = false;
        jiao2 = false;
        jiao3 = false;
    }

    /**
     * 收到信息更新游戏界面
     */
    private void initHandler() {
        handler = new Handler() {

            @SuppressWarnings("unchecked")
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0: // 发牌更新界面
                        cancelTimer(); // 取消定时
                        gameWaitLayout.closeTimer();
                        doudizhuLayout.removeView(gameWaitLayout);
                        mAdView.setVisibility(View.VISIBLE);
                        visibleOrGoneRankBtn();
                        setOrder(1);
                        nowcard.clear();// 清除自己手中的牌
                        addCard(pai);
                        mySelfOrder = 1;
                        ClientUser clientUser = backData.getUsers().get(0);
                        // 自己名字
                        playTextView1.setText(clientUser.getName()); // 自己
                        ActivityUtils.setHead(play1Icon, clientUser.getGender().trim(), false);// 设置头像
                        play1Order.setText(String.valueOf(mySelfOrder));
                        ClientUser clientUsernext = backData.getUsers().get(1);
                        playTextView2.setText(clientUsernext.getName()); // 下家
                        ActivityUtils.setHead(play2Icon, clientUsernext.getGender().trim(), false);// 设置头像
                        int p2o = getNextOrder(mySelfOrder);
                        play2Order.setText(String.valueOf(p2o));
                        int p3o = getPerOrder(mySelfOrder);
                        ClientUser clientUserpre = backData.getUsers().get(2);
                        playTextView3.setText(clientUserpre.getName()); // 下家
                        ActivityUtils.setHead(play3Icon, clientUserpre.getGender().trim(), false);// 设置头像
                        play3Order.setText(String.valueOf(p3o));
                        cleanAllChuPaiInfo();
                        // 托管时的牌可按
                        for (int i = 0; i < myCardsTouchLayout.getChildCount(); i++) {
                            myCardsTouchLayout.getChildAt(i).setClickable(false);
                        }
                        isTuoguan = false; // 取消托管之类的
                        callDizhu(backData.getMasterStart()); // 叫地主
                        if (null != warn) {
                            warn.clear();
                        }
                        warn.put(1101, false);
                        warn.put(1102, false);
                        warn.put(1103, false);
                        if (0 == PreferenceHelper.getMyPreference().getSetting()
                                .getInt("newImage", 0)) {
                            girlItems.setVisibility(View.INVISIBLE);
                            showPopWindow(true);
                        }
                        if (baoXiangTask != null) {
                            baoXiangTask.stop(true);
                            baoXiangTask = null;
                        }
                        baoXiangTask = new BaoXiangTask();
                        ScheduledTask.addDelayTask(baoXiangTask, 3000);
                        addDiZhuCardbg();
                        turnsCallPoint(backData.getMasterStart());
                        break;
                    case 1: // 叫地主更新界面
                        Grab grab = (Grab) msg.getData().get("grab");
                        if (grab.getNextOrder() == mySelfOrder) { // 上家叫的分
                            isTurnMySelf = true;
                            stopTimer(-1); // 暂停定时器
                        } else { // 下家叫的分
                            isTurnMySelf = false;
                            stopTimer(1); // 暂停定时器
                        }
                        truntoCallDizhu(grab);
                        break;
                    case 2: // 地主产生
                        hasDiZhu(msg);
                        if (headTask != null) {
                            headTask.stop(true);
                            headTask = null;
                        }
                        headTask = new HeadTask();
                        ScheduledTask.addDelayTask(headTask, 3000);
                        /** 更新记牌器头像 **/
                        refreshJiPaiQiAvatar();
                        break;
                    case 3: // 收到打牌消息
                        hiddenPlayBtn();
                        PlayAlone play = (PlayAlone) msg.getData().get("play");
                        playCard(play, false);
                        setShengxiaPai(play.getCount(), getPerOrder(play.getNextOrder()));
                        refreshCardCountData();
                        /** 更新记牌器头像 **/
                        refreshJiPaiQiAvatar();
                        break;
                    case 4: // 收到打完这盘的牌消息
                        hiddenPlayBtn();
                        LinkedList<PlayAlone> playResult = (LinkedList<PlayAlone>) msg.getData()
                                .get("playResult");
                        /** 清除记牌器数据 **/
                        clearJiPaiQiData();
                        /** 隐藏记牌器 **/
                        setJiPaiQiVisibility(false);
                        /** 记牌器按钮不能点 **/
                        btn_jipaiqi.setClickable(false);
                        if (null != jiPaiQiChargeDialog && jiPaiQiChargeDialog.isShowing())
                            jiPaiQiChargeDialog.dismiss();
                        setEndDonghua(playResult);
                        break;
                    case 5:// 收到叫地主定时器消息
                        int timeleast = 0;
                        // （-1 上家 0 自己 1 下载）
                        int callOrder = msg.arg1;
                        if (callOrder == 0) { // 自己叫地主
                            if (play1Timer != null) {
                                timeleast = Integer.parseInt(play1Timer.getText().toString()) - 1;
                                if (timeleast == 0) {
                                    callPoint(0);
                                    return;
                                } else {
                                    play1Timer.setText(String.valueOf(timeleast));
                                }
                            }
                        } else if (callOrder == 1) {
                            if (play2Timer != null) {
                                timeleast = Integer.parseInt(play2Timer.getText().toString()) - 1;
                                if (timeleast != 0) {
                                    play2Timer.setText(String.valueOf(timeleast));
                                }
                            }
                        } else if (callOrder == -1) {
                            if (play3Timer != null) {
                                timeleast = Integer.parseInt(play3Timer.getText().toString()) - 1;
                                if (timeleast != 0) {
                                    play3Timer.setText(String.valueOf(timeleast));
                                }
                            }
                        }
                        if (timeleast == 6) { // 播放警告声音
                            AudioPlayUtils.getInstance().playSound(R.raw.warn);
                        }
                        break;
                    case 6:// 收到打牌定时器时间更新消息
                        TextView now = (TextView) findViewById(msg.arg1);
                        if (null != now) {
                            int playtimeleast = Integer.parseInt(now.getText().toString()) - 1;
                            if (playtimeleast != -1) {// 时间没有到
                                now.setText(String.valueOf(playtimeleast));
                                // 如果打得起，并且倒计时小于5秒还没出牌，就震动提示
                                if (View.VISIBLE == playBtnLayout.getVisibility() && !isWait5Second
                                        && playtimeleast == 5) {
                                    // 实例化震动
                                    Vibrate vibrate = new Vibrate(ctx);
                                    if (PreferenceHelper.getMyPreference().getSetting()
                                            .getBoolean("zhendong", true)) {
                                        vibrate.playVibrate1(-1);
                                    }
                                    setTweenAnim(now, R.anim.shake, IS_NONE);
                                }
                            } else {
                                cancelTimer();
                                now.setText(String.valueOf(Constant.WAIT_TIME));
                                if (msg.arg1 == R.id.play1Time) {// 如果是自己出牌的话
                                    if (isWait5Second) {
                                        passCard();// 没有托管，也没打的起的牌
                                        isWait5Second = false;
                                    } else {
                                        gameRobotClick();
                                    }
                                    setTishiGone();
                                }
                            }
                        }
                        break;
                    case 7:// 收到打牌定时器消息
                        DialogUtils.mesTip(getString(R.string.game_playing), true);
                        break;
                    case 9: // 地主产生（带踢拉功能）
                        hasTiLaDiZhu(msg);
                        break;
                    case 10: // 接收显示地主的牌（带踢拉功能）
                        if (masterOrder == mySelfOrder) { // 停掉计时器
                            isTurnMySelf = true;
                            stopTimer(0);
                            moveMyHead();
                        } else if (getNextOrder(masterOrder) == mySelfOrder) { // 上家
                            stopTimer(-1);
                        } else {
                            stopTimer(1);
                        }
                        // LastCards lastCard = (LastCards) msg.getData().get("lastCard");
                        // 更新地主牌数量
                        TextView masterCountView2 = null;
                        if (!isTurnMySelf) {
                            masterCountView2 = (TextView) findViewById(1100 + masterOrder);
                        } else {
                            masterCountView2 = play1SurplusCount;
                        }
                        masterCountView2.setText("20");
                        // genxinMycard(lastCard.getId(), lastCard.getLast(),
                        // lastCard.getMasterOrder());
                        startOtherTimer();
                        break;
                    case 11:// 接收当前玩家的踢拉选择，提示下家是“踢”或"拉"
                        getTiLaMsg(msg);
                        break;
                    case 12:// 刷新倒计时
                        countDownTime -= 60;
                        if (countDownTime <= 0) {
                            countDownTime = 0;
                        }
                        if (0 < countDownTime) {
                            countdownTv.setVisibility(View.VISIBLE);
                            countdownTv.setText(ActivityUtils.getCountDown(countDownTime));
                        } else {
                            countdownTv.setVisibility(View.GONE);
                        }
                        break;
                    case 13:// 收到踢拉定时器消息
                        int timeleast1 = 0;
                        // （-1 上家 0 自己 1 下载）
                        int callOrder1 = msg.arg1;
                        if (callOrder1 == 0) { // 自己叫地主
                            timeleast1 = Integer.parseInt(play1Timer.getText().toString()) - 1;
                            if (timeleast1 == 0) {
                                callBuJiaBei();
                            } else {
                                play1Timer.setText(String.valueOf(timeleast1));
                            }
                        } else if (callOrder1 == 1) {
                            timeleast1 = Integer.parseInt(play2Timer.getText().toString()) - 1;
                            if (timeleast1 != 0) {
                                play2Timer.setText(String.valueOf(timeleast1));
                            }
                        } else if (callOrder1 == -1) {
                            timeleast1 = Integer.parseInt(play3Timer.getText().toString()) - 1;
                            if (timeleast1 != 0) {
                                play3Timer.setText(String.valueOf(timeleast1));
                            }
                        }
                        if (timeleast1 == 6) { // 播放警告声音
                            AudioPlayUtils.getInstance().playSound(R.raw.warn);
                        }
                        break;
                    case 18:// 收到聊天定时器消息
                        CmdDetail mess = (CmdDetail) msg.getData().get(CmdUtils.CMD_CHAT);
                        showMessage(mess);
                        break;
                    case 19:// 收到系统公告
                        String pubMess = (String) msg.getData().get("publicmess");
                        showPubMess(pubMess);
                        break;
                    case 20:// 退出游戏
                        DialogUtils.exitGame(PersonnalDoudizhuActivity.this);
                        break;
                    case 21:// 设置房间的倍数和底数
                        beishuNumView.setText("1"); // 房间默认倍数
                        dishu.setText(String.valueOf(mBaseScore)); // 房间默认底数
                        break;
                    case 23:// 加入心跳界面（普通赛制）
                        showWaitView();
                        break;
                    case 24:// 重新设置数据（普通赛制）
                        gameWaitLayout.setRoomName(Database.JOIN_ROOM);
                        setViewInitData();
                        setUserInfo();
                        playOrStopBgMusic();
                        if (hasCallReady) {// 若后台有请求过准备状态，则此时立刻回复
                            CmdUtils.ready();
                            hasCallReady = false;
                        }
                        if (isTuoguan) {// 如果处于托管状态，则取消托管
                            cancelTuoGuan();
                        }
                        break;
                    case 25:// 返回大厅
                        // 离开房间
                        finishSelf();
                        break;
                    case 26:// 再来一局
                        startAgain();
                        break;
                    case 200:// 重连
                        break;
                    case 301:// 隐藏自己的头像
                        // if (!selfIsMove) {
                        // selfIsMove = true;
                        // AnimUtils.startAnimationsOut1(mySelfHeadRl, 300, 150, nullTv, nullTv2);
                        // nullTv2.setVisibility(View.GONE);
                        // nullTv.setVisibility(View.VISIBLE);
                        // if (headTask != null) {
                        // headTask.stop(true);
                        // headTask = null;
                        // }
                        // }
                        break;
                    case 302:// 隐藏宝箱
                        // if (quang < 5) {
                        // AnimUtils.startAnimationsOut(baoXiangLayout, 300, 50);
                        // }
                        break;
                    case 303:// 隐藏美女图鉴
                        popoDismiss();
                        break;
                    case 400:// 隐藏网络缓存提示信息
                        networkSlowtip.setVisibility(View.GONE);
                        break;
                    case 401:// 展示网络缓存提示信息
                        networkSlowtip.setText("对方网络缓慢 ,请稍候 ...");
                        networkSlowtip.setVisibility(View.VISIBLE);
                        break;
                    case 402:// 设置游戏的背景
                        doudizhuBackGround.setBackgroundDrawable(ImageUtil.getResDrawable(
                                Database.GAME_BG_DRAWABLEID, false));
                        beishuNumView.setText(String.valueOf(Database.JOIN_ROOM_RATIO)); // 房间默认倍数
                        dishu.setText(String.valueOf(Database.JOIN_ROOM_BASEPOINT)); // 房间默认底数
                        break;
                    case 403:// 其他玩家断线通知
                        break;
                    case 404:// 显示比赛场积分\排名等信息
                        gpRl.setVisibility(View.VISIBLE);
                        gpCount.setVisibility(View.VISIBLE);
                        /**
                         * 复合赛制显示：积分/排名 普通赛制显示：积分/排名/类型/第几轮
                         **/
                        GameUser cacheUser = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
                        if (0 != cacheUser.getRound()) {// 第几轮不为空，则是快速赛
                            gpRound.setVisibility(View.VISIBLE);
                            gpType.setVisibility(View.VISIBLE);
                            Log.d("detail", "第" + cacheUser.getRound() + "轮");
                            gpRound.setText("第" + cacheUser.getRound() + "轮");
                            gpType.setText(ImageUtil.getGameType(cacheUser.getLevel()));
                            gpCount.setText(Database.JOIN_ROOM.getName()); // 赛场标题
                        } else {
                            gpRound.setVisibility(View.GONE);
                            gpType.setVisibility(View.GONE);
                            Log.d("detail", "复合赛roomName:" + cacheUser.getRoomName());
                            gpCount.setText(cacheUser.getRoomName()); // 赛场标题
                        }
                        gpScore.setText("积分" + PatternUtils.changeZhidou(cacheUser.getCred()));
                        gpRank.setText("第" + cacheUser.getRank() + "名");
                        break;
                    // case 405:// 隐藏左边美女图片
                    // girlLeftFrame.setVisibility(View.GONE);
                    // if (selfTask != null) {
                    // selfTask.stop(true);
                    // selfTask = null;
                    // }
                    // break;
                    // case 406:// 隐藏右边美女图片
                    // girlRightFrame.setVisibility(View.GONE);
                    // if (rightTask != null) {
                    // rightTask.stop(true);
                    // rightTask = null;
                    // }
                    // break;
                    case 666:// 收到金豆超过上限的命令
                        // String tipsMess = (String) msg.getData().get("tipsmess");
                        // TipsDialog tips = new TipsDialog(ctx) {
                        //
                        // public void okClick() {
                        // CmdUtils.sendFastJoinRoomCmd();
                        // }
                        //
                        // public void cancelClick() {
                        // CmdUtils.exitGame();
                        // // 记录逃跑日志
                        // GameUser cacheUser = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
                        // if (cacheUser != null) {
                        // cacheUser.setRound(0);
                        // GameCache.putObj(CacheKey.GAME_USER, cacheUser);
                        // }
                        // ClientCmdMgr.closeClient();
                        // ActivityUtils.finishAcitivity();
                        // }
                        // };
                        // tips.show();
                        // tips.setText(tipsMess);
                        break;
                    case 777:// 叫分选项显示
                        jiaofenNum = msg.arg1;
                        if (jiaofenNum == 1 && fen1.isClickable()) {
                            fen1.setBackgroundDrawable(ImageUtil.getResDrawable(
                                    R.drawable.fen1, true));
                        } else if (jiaofenNum == 2 && fen2.isClickable()) {
                            if (fen1.isClickable()) {
                                fen1.setBackgroundDrawable(ImageUtil.getResDrawable(
                                        R.drawable.fen1, true));
                            }
                            fen2.setBackgroundDrawable(ImageUtil.getResDrawable(
                                    R.drawable.fen2, true));
                        } else if (jiaofenNum == 3 && fen3.getVisibility() == View.VISIBLE) {
                            if (fen1.isClickable()) {
                                fen1.setBackgroundDrawable(ImageUtil.getResDrawable(
                                        R.drawable.fen1, true));
                            }
                            if (fen2.isClickable()) {
                                fen2.setBackgroundDrawable(ImageUtil.getResDrawable(
                                        R.drawable.fen2, true));
                            }
                            fen3.setBackgroundDrawable(ImageUtil.getResDrawable(
                                    R.drawable.fen3, true));
                        } else if (jiaofenNum > 3 && fen3.getVisibility() == View.VISIBLE) {
                            callPoint(3);
                        }
                        Log.i("lin", "a" + jiaofenNum);
                        break;
                    case Constant.HANDLER_WHAT_GAME_VIEW_SEND_MESS_TEXT:// 发送聊天信息
                        sendTextMessage(msg.getData().getString(Constant.GAME_VIEW_SEND_MESS_TEXT),
                                msg.getData().getInt(Constant.GAME_VIEW_SEND_MESS_CLICK_TYPE));
                        break;
                    case Constant.HANDLER_WHAT_GAME_VIEW_SEND_MESS_GIF:// GIF表情
                        String imageName = msg.getData()
                                .getString(Constant.GAME_VIEW_SEND_MESS_GIF);
                        int clickType = msg.getData().getInt(
                                Constant.GAME_VIEW_SEND_MESS_CLICK_TYPE);
                        myFrame.removeAllViews();
                        girlLeftFrame.setVisibility(View.GONE);
                        startTask(myFrame, selfTask);
                        messageFrame(myFrame, imageName, clickType, null);
                        break;
                }
            }

            /**
             * 再来一局
             */
            private void startAgain() {
                /** 重置记牌器数据 **/
                clearJiPaiQiData();
                /** 设置记牌器可获得焦点 **/
                btn_jipaiqi.setClickable(true);
                gameRobot.setClickable(true);
                isTuoguan = false;
                firstChupai = true;
                isTuoguan = false;
                beishuNumber = null;
                tiShiCount = -1;
                curPage = 0;// 当前第几张，做标志用
                jiaofenNum = 0;
                jiao = 0;// 手势点击（叫分）的次数
                selfIsMove = false;// 自己的头像是否移动过
                backData = null;// 发牌信息
                turnsCallOrder = 0;// 当前叫分的人的位置
                preUserPoker = null;
                nextUserPoker = null;
                // myUserPoker = null;
                logicPre = null;
                logicNext = null;
                preType = new Pritype();
                nextType = new Pritype();
                isFirstPlay = false;
                // isFirstCall = false;
                firstPlaySize = 0;
                oneTurns = 0;
                calledPoint = 0;
                curPlayOrder = 0;
                prePlayOrder = 0;
                myCardsTouchLayout.removeAllViews();
                play1PassLayout.removeAllViews();
                play2PassLayout.removeAllViews();
                play3PassLayout.removeAllViews();
                jiabei1Iv.setVisibility(View.GONE);
                jiabei2Iv.setVisibility(View.GONE);
                jiabei3Iv.setVisibility(View.GONE);
                wolTv1.setVisibility(View.GONE);
                wolTv2.setVisibility(View.GONE);
                wolTv3.setVisibility(View.GONE);
                if (selfIsMove) {
                    selfIsMove = false;
                    nullTv.setVisibility(View.GONE);
                    nullTv2.setVisibility(View.VISIBLE);
                }
                tuoGuan.setClickable(false);
                isTuoguan = false;
                AnimUtils.startScaleAnimationOut(tuoGuanLayout, ctx);
                if (null != nowcard) {
                    for (Poker card : nowcard) {
                        if (card != null) {
                            card.onDestory();
                        }
                        card = null;
                    }
                    nowcard.clear();
                    nowcard = null;
                }
                if (null != chupaicard) {
                    for (Poker card : chupaicard) {
                        if (card != null) {
                            card.onDestory();
                        }
                        card = null;
                    }
                    chupaicard.clear();
                    chupaicard = null;
                }
                if (null != checkpai) {
                    for (Poker card : checkpai) {
                        if (card != null) {
                            card.onDestory();
                        }
                        card = null;
                    }
                    checkpai.clear();
                    checkpai = null;
                }
                if (null != otherplay1) {
                    for (Poker card : otherplay1) {
                        if (card != null) {
                            card.onDestory();
                        }
                        card = null;
                    }
                    otherplay1.clear();
                    otherplay1 = null;
                }
                // 释放所有扑克牌所占的资源
                if (poker != null) {
                    for (Poker card : poker) {
                        if (card != null) {
                            card.onDestory();
                        }
                        card = null;
                    }
                    poker = null;
                }
                poker = PokerUtil.getPoker(ctx);
                nowcard = new ArrayList<Poker>();
                chupaicard = new ArrayList<Poker>();
                otherplay1 = new ArrayList<Poker>();
                // ImageUtil.clearPokerMap();
                setViewInitData();
                showWaitView();
                AudioPlayUtils.isPlay = true;
                playOrStopBgMusic();
                checkBeans();
                // washCard();
                masterOrder = 0;
                setJipaiqiAvailableOrNotAvailable();
            }

            /**
             * 显示心跳界面
             */
            private void showWaitView() {
                LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT);
                doudizhuLayout.removeView(gameWaitLayout);
                doudizhuLayout.addView(gameWaitLayout, layoutParams);
                mAdView.setVisibility(View.INVISIBLE);
                gameWaitLayout.setOnClickListener(null);
                gameWaitLayout.setjoinBottomllInVisible();
            }

            /**
             * 地主产生
             * 
             * @param msg
             */
            private void hasDiZhu(Message msg) {
                jiaofenLayout.setVisibility(View.GONE);
                Grab grabMaster = (Grab) msg.getData().get("master");
                setDizhuIcon(grabMaster.getMasterOrder());
                masterOrder = grabMaster.getMasterOrder();
                setJipaiqiAvailableOrNotAvailable();
                if (calledPoint == 3 || oneTurns == 3) {
                    setJiaofenXianshi(grabMaster.getRatio(), getPerOrder(turnsCallOrder));
                }
                isTurnMySelf = false;
                if (masterOrder == mySelfOrder) { // 停掉计时器
                    isTurnMySelf = true;
                    stopTimer(0);
                } else if (getNextOrder(masterOrder) == mySelfOrder) { // 上家
                    stopTimer(-1);
                } else {
                    stopTimer(1);
                }
                // 更新地主牌数量
                TextView masterCountView = null;
                if (!isTurnMySelf) {
                    masterCountView = (TextView) findViewById(1100 + masterOrder);
                } else {
                    masterCountView = play1SurplusCount;
                }
                masterCountView.setText("20");
                int tempRatio = grabMaster.getRatio();
                beishuNumber = String.valueOf(tempRatio);
                beishuNumView.setText(beishuNumber);
                genxinMycard(backData.getLastCards(), grabMaster.getMasterOrder());
                startOtherTimer();
            }

            /**
             * 地主产生（带踢拉功能）
             * 
             * @param msg
             */
            private void hasTiLaDiZhu(Message msg) {
                jiaofenLayout.setVisibility(View.GONE);
                GenLandowners gld = (GenLandowners) msg.getData().get("gld");
                setDizhuIcon(gld.getMasterOrder());
                masterOrder = gld.getMasterOrder();
                setJipaiqiAvailableOrNotAvailable();
                isTurnMySelf = false;
                int ratio = gld.getRatio();
                ImageView info = new ImageView(ctx);
                // play1PassLayout.removeAllViews();
                // play2PassLayout.removeAllViews();
                // play3PassLayout.removeAllViews();
                AudioPlayUtils apu = AudioPlayUtils.getInstance();
                String gender2 = Database.userMap.get(masterOrder).getGender();
                if (ratio == 1) {
                    if ("1".equals(gender2)) {
                        apu.playMusic(false, R.raw.nv_1fen); // 别人叫1分
                    } else {
                        apu.playMusic(false, R.raw.nan_1fen); // 别人叫1分
                    }
                    info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.callone, true));
                } else if (ratio == 2) {
                    if ("1".equals(gender2)) {
                        apu.playMusic(false, R.raw.nv_2fen); // 别人叫2分
                    } else {
                        apu.playMusic(false, R.raw.nan_2fen); // 别人叫2分
                    }
                    info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.calltwo, true));
                } else if (ratio == 3) {
                    if ("1".equals(gender2)) {
                        apu.playMusic(false, R.raw.nv_3fen); // 别人叫3分
                    } else {
                        apu.playMusic(false, R.raw.nan_3fen); // 别人叫3分
                    }
                    info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.callthree, true));
                }
                beishuNumber = String.valueOf(ratio);
                beishuNumView.setText(beishuNumber);
                // 关闭叫地主定时器
                if (mySelfOrder == masterOrder) {// 自己
                    stopTimer(0); // 暂停定时器
                } else if (JIABEI2_ID == (1600 + masterOrder)) {// 下家
                    stopTimer(1); // 暂停定时器
                } else if (JIABEI3_ID == (1600 + masterOrder)) {// 上家
                    stopTimer(-1); // 暂停定时器
                }
                // ActivityUtils.startScaleAnim(play1PassLayout, ctx);//播放缩放动画
                // 开启踢拉定时器
                if (mySelfOrder == masterOrder) {// 自己
                    isTurnMySelf = false;
                    startTiLaTimer(1);
                } else if (JIABEI2_ID == (1600 + masterOrder)) {// 下家
                    isTurnMySelf = false;
                    startTiLaTimer(-1);
                } else if (JIABEI3_ID == (1600 + masterOrder)) {// 上家
                    isTurnMySelf = true;
                    startTiLaTimer(0);
                }
                // 显示"加倍"，"不加倍"
                if (mySelfOrder == masterOrder) {
                    info.setPadding(0, 0, 0, 60);
                    play1PassLayout.addView(info, mst.getAdjustLayoutParamsForImageView(info));
                    ActivityUtils.startScaleAnim(play1PassLayout, ctx);// 播放缩放动画
                } else {
                    RelativeLayout re = (RelativeLayout) findViewById(masterOrder + 1000);
                    if (re != null) {
                        re.removeAllViews();
                        re.addView(info, mst.getAdjustLayoutParamsForImageView(info));
                        ActivityUtils.startScaleAnim(re, ctx);// 播放缩放动画
                    }
                }
            }

            /**
             * 接收当前玩家的踢拉选择，提示下家是“踢”或"拉"
             * 
             * @param msg
             */
            private void getTiLaMsg(Message msg) {
                TiLa tila = (TiLa) msg.getData().get("tila");
                ImageView info = new ImageView(ctx);
                // play1PassLayout.removeAllViews();
                // play2PassLayout.removeAllViews();
                // play3PassLayout.removeAllViews();
                String gd = Database.userMap.get(tila.getOrder()).getGender();
                AudioPlayUtils apu2 = AudioPlayUtils.getInstance();
                // (1:不加倍,2:加2倍,4:加4倍)
                if (1 != tila.getRatio()) {// 加倍
                    if (2 == tila.getRatio()) {
                        info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.jiabei_x_2,
                                true));
                        // 显示已加倍的状态
                        if (mySelfOrder == tila.getOrder()) {
                            jiabei1Iv.setVisibility(View.VISIBLE);
                            jiabei1Iv.setImageResource(R.drawable.jiabei_x_2);
                        }
                        if (JIABEI2_ID == (1600 + tila.getOrder())) {
                            jiabei2Iv.setVisibility(View.VISIBLE);
                            jiabei2Iv.setImageResource(R.drawable.jiabei_x_2);
                            setTweenAnim(jiabei2Iv, R.anim.jump, IS_NONE);
                        }
                        if (JIABEI3_ID == (1600 + tila.getOrder())) {
                            jiabei3Iv.setVisibility(View.VISIBLE);
                            jiabei3Iv.setImageResource(R.drawable.jiabei_x_2);
                            setTweenAnim(jiabei3Iv, R.anim.jump, IS_NONE);
                        }
                    } else {
                        info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.jiabei_x_4,
                                true));
                        // 显示已加倍的状态
                        if (mySelfOrder == tila.getOrder()) {
                            jiabei1Iv.setVisibility(View.VISIBLE);
                            jiabei1Iv.setImageResource(R.drawable.jiabei_x_4);
                        }
                        if (JIABEI2_ID == (1600 + tila.getOrder())) {
                            jiabei2Iv.setVisibility(View.VISIBLE);
                            jiabei2Iv.setImageResource(R.drawable.jiabei_x_4);
                            setTweenAnim(jiabei2Iv, R.anim.jump, IS_NONE);
                        }
                        if (JIABEI3_ID == (1600 + tila.getOrder())) {
                            setTweenAnim(jiabei3Iv, R.anim.jump, IS_NONE);
                            jiabei3Iv.setImageResource(R.drawable.jiabei_x_4);
                            jiabei3Iv.setVisibility(View.VISIBLE);
                        }
                    }
                    // 声音提示
                    if ("1".equals(gd)) {// 女
                        // 女声
                        apu2.playSound(R.raw.nv_jiabei);
                    } else {
                        // 男声
                        apu2.playSound(R.raw.nan_jiabei);
                    }
                    // 在对应的玩家头像显示加倍标识
                } else {// 不加倍
                    info.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.not_doubling,
                            true));
                    if ("1".equals(gd)) {// 女
                        // 女声
                        apu2.playSound(R.raw.nv_bujiabei);
                    } else {
                        // 男声
                        apu2.playSound(R.raw.nan_bujiabei);
                    }
                }
                // 显示"加倍"，"不加倍"
                if (mySelfOrder == tila.getOrder()) {
                    play1PassLayout.removeAllViews();
                    jiabei1Iv.setVisibility(View.VISIBLE);
                    info.setPadding(0, 0, 0, 60);
                    play1PassLayout.addView(info, mst.getAdjustLayoutParamsForImageView(info));
                    ActivityUtils.startScaleAnim(play1PassLayout, ctx);// 播放缩放动画
                } else {
                    RelativeLayout re = (RelativeLayout) findViewById(tila.getOrder() + 1000);
                    Log.i("Order", "tila.getOrder():" + tila.getOrder() + "      re:" + re);
                    if (re != null) {
                        re.removeAllViews();
                        re.addView(info, mst.getAdjustLayoutParamsForImageView(info));
                        ActivityUtils.startScaleAnim(re, ctx);// 播放缩放动画
                    }
                }
                // 关闭当前踢拉定时器
                if (null != tila.getOrder()) {
                    if (mySelfOrder == tila.getOrder()) {// 自己
                        stopTimer(0); // 暂停定时器
                    } else if (JIABEI2_ID == (1600 + tila.getOrder())) {// 下家
                        stopTimer(1); // 暂停定时器
                    } else if (JIABEI3_ID == (1600 + tila.getOrder())) {// 上家
                        stopTimer(-1); // 暂停定时器
                    }
                }
                // 开启下一家踢拉定时器
                if (tila.getNextCan() && null != tila.getNextOrder()) {
                    if (mySelfOrder == tila.getNextOrder()) {// 自己
                        isTurnMySelf = true;
                        startTiLaTimer(0);
                    } else if (JIABEI2_ID == (1600 + tila.getNextOrder())) {// 下家
                        isTurnMySelf = false;
                        startTiLaTimer(1);
                    } else if (JIABEI3_ID == (1600 + tila.getNextOrder())) {// 上家
                        isTurnMySelf = false;
                        startTiLaTimer(-1);
                    }
                }
                // 下一个可否踢or拉
                if (tila.getNextCan() && (null != tila.getNextOrder())
                        && (mySelfOrder == tila.getNextOrder())) {// 下一个是自己，并且可加倍
                    play1PassLayout.removeAllViews();
                    tilaLayout.setVisibility(View.VISIBLE);
                    if (isTuoguan) {
                        callBuJiaBei();
                    }
                }
            }
        };
    }

    /**
     * 隐藏对话框
     */
    private void dismissDialog() {
        if (null != popupWindow) {
            popupWindow.dismiss();
        }
        if (null != settingDialog && settingDialog.isShowing()) {
            settingDialog.dismiss();
        }
    }

    /**
     * 添加地主牌的背景
     */
    private void addDiZhuCardbg() {
        dizhuPaiLayout.removeAllViews();
        // 初始化，显示地主牌背景
        for (int i = 0; i < 3; i++) {
            Poker ca = new Poker(ctx);
            ca.getPokeImage().setImageDrawable(ImageUtil.getResDrawable(R.drawable.pukes, true));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    mst.adjustXIgnoreDensity(50), mst.adjustYIgnoreDensity(68));
            params.leftMargin = mst.adjustXIgnoreDensity(13 * i);
            dizhuPaiLayout.addView(ca, params);
            firstChupai = false;
        }
    }

    // /**
    // * 弹出淘汰或胜利对话框
    // *
    // * @param rank
    // * @param prizeGoods2
    // */
    // private void showGameOverDialog(String rank,List<PrizeGoods> prizeGoods2) {
    // GameOverDialog god = new GameOverDialog(PersonnalDoudizhuActivity.this, R.style.dialog,
    // prizeGoods2, taskManager, rank);
    // god.setInterface(PersonnalDoudizhuActivity.this);
    // god.show();
    // ClientCmdMgr.closeClient();// 断开Socket
    // }

    // 表情点击事件
    public void photoClick(String mess) {
        try {
            userinfoshowView.setVisibility(View.VISIBLE);
            LayoutParams layoutParams = new LayoutParams(mst.adjustXIgnoreDensity(300),
                    mst.adjustXIgnoreDensity(180));
            LayoutParams textParam = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            int tipWidth = 340;
            int tipHeight = 227;
            float sx = (float) Database.SCREEN_HEIGHT / (float) Constant.DEFAULT_HEIGHT;
            float sy = (float) Database.SCREEN_WIDTH / (float) Constant.DEFAULT_WIDTH;
            BitmapDrawable bitmapDrawable = (BitmapDrawable) ImageUtil.getResDrawable(
                    R.drawable.tip_msg, true);
            Bitmap bitmap = null;
            layoutParams.setMargins(mst.adjustXIgnoreDensity(100), mst.adjustXIgnoreDensity(30), 0,
                    0);
            bitmap = Bitmap.createBitmap(bitmapDrawable.getBitmap(), 0, 0, tipWidth, tipHeight);
            ImageUtil.releaseDrawable(bitmapDrawable);
            String bitMapKey = String.valueOf(R.drawable.tip_msg) + "_small";
            bitmap = ImageUtil.getBitmap(bitMapKey, false);
            if (bitmap == null) {
                bitmap = ImageUtil.resizeBitmap(bitmap, tipWidth * sx, tipHeight * sy);
                ImageUtil.addBitMap2Cache(bitMapKey, bitmap);
            }
            textParam.setMargins(0, mst.adjustXIgnoreDensity(30), 0, 0);
            textParam.setMargins(0, 30, 0, 0);
            userinfoshowView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            userinfoshowView.setLayoutParams(layoutParams);
            StringBuffer msgBuffer = new StringBuffer();
            msgBuffer.append(mess);
            // .append(u.getAccount()).append("</font><br/>");
            userInfoText.setLayoutParams(textParam);
            userInfoText.setText(mess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自己不出，pass
     */
    private void passCard() {
        setTishiGone();
        isWait5Second = false;
        for (int i = 0; i < nowcard.size(); i++) {
            poker[nowcard.get(i).getNumber()].params.topMargin = mst.adjustYIgnoreDensity(20);
            poker[nowcard.get(i).getNumber()]
                    .setLayoutParams(poker[nowcard.get(i).getNumber()].params);
            poker[nowcard.get(i).getNumber()].ischeck = false;
        }
        initTiShiCount();
        // CmdUtils.pass();
        curPlayOrder = 2;
        if (getNextCallOrder(prePlayOrder) == 1 && bierenchupai != null && bierenchupai.length > 0) {
            PlayAlone playAlone = new PlayAlone();
            playAlone.setPrecards(setFirstCard(bierenchupai));
            playAlone.setCount(nowcard.size());
            playCards(playAlone);
        } else {
            PlayAlone playAlone = new PlayAlone();
            playAlone.setCount(nowcard.size());
            playCards(playAlone);
        }
        ImageView im = new ImageView(ctx);
        im.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.play_buchu, true));
        play1PassLayout.removeAllViews();
        play1PassLayout.addView(im, mst.getAdjustLayoutParamsForImageView(im));
        ActivityUtils.startScaleAnim(play1PassLayout, ctx);// 播放缩放动画
        stopTimer(0);// 停止自己的定时器
        startPlayTimer(getNextOrder(mySelfOrder) + 1200);
        String gender = "0";
        if ("1".equals(gender)) {
            AudioPlayUtils.getInstance().playSound(R.raw.nv_pass);// 不叫
        } else {
            AudioPlayUtils.getInstance().playSound(R.raw.nan_pass);// 不叫
        }
        // 头像高亮切换
        zhezhao1.setVisibility(View.VISIBLE);
        ImageView img = (ImageView) findViewById(getNextOrder(mySelfOrder) + 1400);
        img.setVisibility(View.GONE);
        // 抹去上轮出的牌
        RelativeLayout res = (RelativeLayout) findViewById((getNextOrder(mySelfOrder)) + 1000);
        res.removeAllViews();
        hiddenPlayBtn();
    }

    /**
     * 隐藏手势提示
     */
    private void setTishiGone() {
        if (mainGameGuideVI.isArrowIsUp()) {
            mainGameGuideVI.setArrowUpGone(false);
        }
        if (mainGameGuideVI.isArrowIsDown()) {
            mainGameGuideVI.setArrowDownGone(false);
        }
        if (mainGameGuideVI.isPoint()) {
            mainGameGuideVI.setPointGone(false);
        }
        if (mainGameGuideVI.isArrowLeftRightVisible()) {
            mainGameGuideVI.setArrowLeftRightGone(false);
        }
        if (mainGameGuideVI.isDoublePoint()) {
            mainGameGuideVI.setDoublePointGone(false);
        }
    }

    /**
     * 自己打牌
     * 
     * @param comeling 是否为onFling调用
     */

    private void playCard(boolean comeOnFling) {
        valueMe = -1;
        /** 标识自己是否出牌 **/
        boolean isPlayCard = false;
        setTishiGone();
        chupaicard.clear();
        initTiShiCount();
        for (Poker card : nowcard) {
            if (card.ischeck) {
                chupaicard.add(card);
            }
        }
        // 检测别人出过牌没有
        if (bierenchupai == null) {
            firstChupai = true;
        }
        // 检测自己的出牌类型
        if (chupaicard.size() != 0) {
            checkpai = new ArrayList<Poker>();
            for (Poker card : chupaicard) {
                checkpai.add(card);
            }
            typeMe = DoudizhuRule.checkpai(checkpai);
            valueMe = DoudizhuRule.checkpaiValue(typeMe, checkpai);
        } else {
            playError(comeOnFling);
            return;
        }
        // 对自己的牌型进行检测
        if (typeMe == 0) {
            playError(comeOnFling);
            return;
        }
        // 检查别人出什么牌
        if (!firstChupai) {
            typeplay1 = checkOtherChupai(bierenchupai);
            // 检查谁大
            if (DoudizhuRule.compterpai(typeplay1, typeMe, DoudizhuRule.getMaxNumber(otherplay1),
                    DoudizhuRule.getMaxNumber(chupaicard), bierenchupai.length, chupaicard.size())) {
                cardAddview(chupaicard, false);
                initTiShiCount();
                // 发送出牌消失
                // CmdUtils.play(chupaicard);
                /** 标识自己是否出牌 **/
                isPlayCard = true;
                curPlayOrder = 2;
                if (chupaicard != null && chupaicard.size() > 0) {
                    prePlayOrder = 1;
                    PlayAlone playAlone = new PlayAlone();
                    playAlone.setCards(chupaicard);
                    logicNext.allPlayedPoker.addAll(chupaicard);
                    logicPre.allPlayedPoker.addAll(chupaicard);
                    playAlone.setCount(nowcard.size());
                    playCards(playAlone);
                }
                hiddenPlayBtn();
                firstChupai = true;
                bierenchupai = null;
                // 检测加倍
                checkJiaBei(typeMe, false);
                playDiZhuCardAudio(typeMe, valueMe, "0"); // 出牌的声音
                // 如果是炸弹显示特效
                PlayCardEffect.bomEffect(typeMe, doudizhuBackGround);
                if (myCardsTouchLayout.getChildCount() > 0) {
                    // 头像高亮切换
                    zhezhao1.setVisibility(View.VISIBLE);
                    ImageView img = (ImageView) findViewById(getNextOrder(mySelfOrder) + 1400);
                    img.setVisibility(View.GONE);
                    // 抹去上轮出的牌
                    RelativeLayout res = (RelativeLayout) findViewById((getNextOrder(mySelfOrder)) + 1000);
                    res.removeAllViews();
                }
            } else {
                playError(comeOnFling);
            }
        } else {
            // 检测加倍
            checkJiaBei(typeMe, false);
            cardAddview(chupaicard, false);
            initTiShiCount();
            // 发送出牌消息
            prePlayOrder = 1;
            curPlayOrder = 2;
            PlayAlone playAlone = new PlayAlone();
            playAlone.setCards(chupaicard);
            logicNext.allPlayedPoker.addAll(chupaicard);
            logicPre.allPlayedPoker.addAll(chupaicard);
            playAlone.setCount(nowcard.size());
            /** 标识自己是否出牌 **/
            isPlayCard = true;
            if (!isFirstPlay && mySelfOrder == masterOrder) {
                isFirstPlay = true;
                firstPlaySize = 20 - playAlone.getCards().size();
            }
            playCards(playAlone);
            // CmdUtils.play(chupaicard);
            hiddenPlayBtn();
            firstChupai = true;
            // 自己出完牌后将别人出牌清空
            bierenchupai = null;
            playDiZhuCardAudio(typeMe, valueMe, "0"); // 出牌的声音
            // 如果是炸弹显示特效
            PlayCardEffect.bomEffect(typeMe, doudizhuBackGround);
            if (myCardsTouchLayout.getChildCount() > 0) {
                // 头像高亮切换
                zhezhao1.setVisibility(View.VISIBLE);
                ImageView img = (ImageView) findViewById(getNextOrder(mySelfOrder) + 1400);
                img.setVisibility(View.GONE);
                // 抹去上轮出的牌
                RelativeLayout res = (RelativeLayout) findViewById((getNextOrder(mySelfOrder)) + 1000);
                res.removeAllViews();
            }
        }
        play1SurplusCount.setText("" + myCardsTouchLayout.getChildCount());
        if (comeOnFling) {
            myCardsTouchLayout.chekCard();
        }
        if (isPlayCard) {
            if (null != chupaicard)
                addOutPokers(mySelfOrder, chupaicard);
            refreshCardCountData();
        }
    }

    /**
     * 打的牌不符合规矩
     * 
     * @param comeOnFling
     */
    private void playError(boolean comeOnFling) {
        if (comeOnFling) {
            myCardsTouchLayout.chekCard();
        }
        Toast.makeText(ctx, "你出的牌不符合规矩", Toast.LENGTH_SHORT).show();
    }

    // 显示表情
    // private class MyAdapter extends BaseAdapter {
    //
    // private int[] gifInt;
    // private LayoutInflater mInflater;
    //
    // public MyAdapter(int[] gifs) {
    // this.gifInt = gifs;
    // this.mInflater = LayoutInflater.from(PersonnalDoudizhuActivity.this);
    // }
    //
    // public int getCount() {
    // return gifInt.length;
    // }
    //
    // public Object getItem(int position) {
    // return gifInt[position];
    // }
    //
    // public long getItemId(int position) {
    // return position;
    // }
    //
    // public View getView(int position, View convertView, ViewGroup parent) {
    // ViewHolder holder;
    // if (null == convertView) {
    // holder = new ViewHolder();
    // convertView = mInflater.inflate(R.layout.gif_item, null);
    // holder.iv = (ImageView) convertView.findViewById(R.id.gif1);
    // holder.iv.setBackgroundDrawable(ImageUtil.getDrawableResId(gifInt[position], true, true));
    // convertView.setTag(holder);
    // } else {
    // holder = (ViewHolder) convertView.getTag();
    // }
    // return convertView;
    // }
    //
    // private class ViewHolder {
    //
    // private ImageView iv;
    // }
    // }

    /**
     * 显示用户信息
     */
    public void setUserInfo() {
        GameUser cacheUser = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
        if (cacheUser != null) {
            zhidou.setText(String.valueOf(cacheUser.getBean()));
        } else {
            zhidou.setText(String.valueOf("20000"));
        }
    }

    /**
     * 检测是否需要加倍
     * 
     * @param type
     * @param reShow 是否重连时显示的牌
     */
    public void checkJiaBei(int type, boolean reShow) {
        if (reShow)
            return;
        // 如果是炸弹或者是火箭
        if (type == 6 || type == 13) {
            beishuNumber = String.valueOf(Integer.parseInt(beishuNumber) * 2);
            beishuNumView.setText(beishuNumber);
        }
    }

    private void setParams(android.view.WindowManager.LayoutParams lay) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
    }

    /**
     * 判断输赢并显示出來
     * 
     * @param users
     */
    public void setEndDonghua(final LinkedList<PlayAlone> users) {
        AudioPlayUtils.getInstance().stopMusic();
        AudioPlayUtils.getInstance().stopBgMusic();
        cancelTimer();
        dismissDialog();
        LinkedList<PlayAlone> userLink = new LinkedList<PlayAlone>(users);
        AudioPlayUtils.getInstance().playSound(R.raw.get_glod); // 数金豆声音
        for (PlayAlone end : userLink) {
            // 如果是自己的顺序
            List<Poker> last = end.getCards();
            int nextPlay = getNextOrder(mySelfOrder);
            if (end.getOrder() == mySelfOrder) {
                int now[] = new int[last.size()];
                for (int j = 0; j < last.size(); j++) {
                    now[j] = last.get(j).getNumber();
                }
                addCard(now);
                play1PassLayout.removeAllViews();
                setWolTvNum(wolTv1, end);
            } else if (end.getOrder() == nextPlay) {
                addCard(last, play2PassLayout, true);
                setWolTvNum(wolTv2, end);
            } else {
                addCard(last, play3PassLayout, false);
                setWolTvNum(wolTv3, end);
            }
        }
        AutoTask goOutTask = new AutoTask() {

            public void run() {
                ctx.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        dismissDialog();
                        AloneGameEndDialog mGameEndDialog = new AloneGameEndDialog(ctx, users,
                                mySelfOrder, handler, mBaseScore);
                        mGameEndDialog.setContentView(R.layout.doudizhu_end);
                        android.view.WindowManager.LayoutParams lay = mGameEndDialog.getWindow()
                                .getAttributes();
                        setParams(lay);
                        mGameEndDialog.show();
                    }
                });
            }
        };
        ScheduledTask.addDelayTask(goOutTask, (3000));
    }

    /**
     * 设置输赢金豆变动的数量
     * 
     * @param view 显示Tv
     * @param end 玩家
     */
    private void setWolTvNum(final TextView view, final PlayAlone end) {
        view.setVisibility(View.VISIBLE);
        if (0 > end.getPayment()) {
            view.setText("-" + Math.abs(end.getPayment()));
            view.setTextColor(ctx.getResources().getColor(R.color.chestnut_red));
        } else {
            view.setTextColor(ctx.getResources().getColor(R.color.gold));
            view.setText("+" + Math.abs(end.getPayment()));
        }
        new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    int num = (int) Math.abs(end.getPayment());
                    int step = 0;// 步长
                    int count = 0;// 循环次数
                    int yushu = 0;// 余数
                    if (num >= mBaseScore) {
                        step = num / mBaseScore;
                        count = mBaseScore;
                        yushu = num % mBaseScore;
                    } else {
                        step = 1;
                        count = num;
                    }
                    // 先显示余数
                    if (end.getPayment() > 0) {
                        view.setText("+" + yushu);
                    } else {
                        view.setText("-" + yushu);
                    }
                    for (int i = 1; i <= count; i++) {
                        // 每间隔10毫秒递增
                        sleep(10);
                        yushu += step;
                        final int yushu1 = yushu;
                        ctx.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (end.getPayment() > 0) {
                                    view.setText("+" + yushu1);
                                } else {
                                    view.setText("-" + yushu1);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }.start();
    }

    private int cardSpace = 0;

    /**
     * 把剩下的牌显示出来
     * 
     * @param last 最后手牌列表
     * @param rel 容器布局
     * @param isLeft 是否为屏幕左边的布局
     */
    public void addCard(List<Poker> last, RelativeLayout rel, boolean isLeft) {
        try {
            int now[] = new int[last.size()];
            for (int j = 0; j < last.size(); j++) {
                now[j] = last.get(j).getNumber();
            }
            rel.removeAllViews();
            int[] paixu = DoudizhuRule.sort(now, poker);
            cardSpace = mst.adjustXIgnoreDensity(cardSpace);
            for (int i = 0; i < paixu.length; i++) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        mst.adjustXIgnoreDensity(50), mst.adjustYIgnoreDensity(68));
                Poker card = poker[paixu[i]];
                card.getPokeImage().setImageDrawable(
                        ImageUtil.getResDrawable(card.getBitpamResID(), true));
                if (i < 9) {
                    params.topMargin = mst.adjustYIgnoreDensity(20);
                    params.leftMargin = mst.adjustXIgnoreDensity(20 * i);
                } else {
                    params.topMargin = mst.adjustYIgnoreDensity(54);
                    params.leftMargin = mst.adjustXIgnoreDensity(20 * (i - 9));
                }
                card.setLayoutParams(params);
                rel.addView(poker[paixu[i]]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 叫地主提示多少分
     * 
     * @param ratio
     * @param ResId
     * @param perCallOrder 上次叫分人
     */
    public void setJiaofenXianshi(int ratio, int perCallOrder) {
        ImageView info = new ImageView(this);
        info.setBackgroundDrawable(ImageUtil.getResDrawable(backId(ratio, perCallOrder), true));
        // play1PassLayout.removeAllViews();
        // play2PassLayout.removeAllViews();
        // play3PassLayout.removeAllViews();
        if (perCallOrder == mySelfOrder) {// 如果是自己叫地主
            // info.setPadding(0, 0, 0, 60);
            // // play1PassLayout.addView(info);
            // play1PassLayout.addView(info,
            // mst.getAdjustLayoutParamsForImageView(info));
            // ActivityUtils.startScaleAnim(play1PassLayout, ctx);//播放缩放动画
        } else {
            if (3 == ratio) { // 叫三分放在此处是防止，自己点三分的时候出现重复的叫3分声音
                String gender = "0";
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_3fen);// 叫3分
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_3fen);// 叫3分
                }
                RelativeLayout re = (RelativeLayout) findViewById(perCallOrder + 1000); // 叫分的人
                if (re != null) {
                    re.removeAllViews();
                    // re.addView(info);
                    re.addView(info, mst.getAdjustLayoutParamsForImageView(info));
                    ActivityUtils.startScaleAnim(re, ctx);// 播放缩放动画
                }
            } else {
                RelativeLayout re = (RelativeLayout) findViewById(perCallOrder + 1000); // 叫分的人
                if (re != null) {
                    re.removeAllViews();
                    // re.addView(info);
                    re.addView(info, mst.getAdjustLayoutParamsForImageView(info));
                    ActivityUtils.startScaleAnim(re, ctx);// 播放缩放动画
                }
            }
        }
    }

    /**
     * 清除出牌提示信息
     */
    public void cleanAllChuPaiInfo() {
        play1PassLayout.removeAllViews();
        play3PassLayout.removeAllViews();
        play2PassLayout.removeAllViews();
    }

    /**
     * 通过传入叫分的分值返回图片的id
     * 
     * @param ratio
     * @return
     */
    public int backId(int ratio, int perCallOrder) {
        int id = 0;
        String gender = "0";
        switch (ratio) {
            case 0:
                id = R.drawable.call_bujiao;
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_bujiao);// 不叫
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_bujiao);// 不叫
                }
                break;
            case 1:
                id = R.drawable.callone;
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_1fen);// 叫1分
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_1fen);// 叫1分
                }
                break;
            case 2:
                id = R.drawable.calltwo;
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_2fen);// 叫2分
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_2fen);// 叫2分
                }
                break;
            case 3:
                id = R.drawable.callthree;
                break;
        }
        Log.i("callPoint", "通过传入值返回" + ratio + "分");
        return id;
    }

    /**
     * 检查别人出什么类型的牌
     * 
     * @param chu
     * @return
     */
    public int checkOtherChupai(int[] chu) {
        // 首先将对方的牌拍好序
        int[] otherpaixu = DoudizhuRule.sort(chu, poker);
        otherplay1.clear();
        for (int i = 0; i < otherpaixu.length; i++) {
            otherplay1.add(poker[otherpaixu[i]]);
        }
        int type = DoudizhuRule.checkpai(otherplay1);
        return type;
    }

    /**
     * 显示打出去的牌,并减少自己的牌
     * 
     * @param passCardList
     */
    public void cardAddview(List<Poker> passCardList, boolean sigleTime) {
        play1PassLayout.removeAllViews();
        for (int i = 0; i < passCardList.size(); i++) {
            RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
                    mst.adjustXIgnoreDensity(50), mst.adjustXIgnoreDensity(68));
            Poker image = new Poker(this);
            image.getPokeImage().setImageDrawable(
                    ImageUtil.getResDrawable(passCardList.get(i).getBitpamResID(), true));
            ;
            // image.setImageResource(pass.get(i).getBitpamResID());
            params3.leftMargin = mst.adjustXIgnoreDensity(20 * i);
            play1PassLayout.addView(image, params3);// 显示出来
            // mst2.adjustView(image);
            // 清除图片
            ImageUtil.releaseDrawable(passCardList.get(i).getPokeImage().getDrawable());
            // 移除出的牌
            nowcard.remove(passCardList.get(i));
            myCardsTouchLayout.removeView(passCardList.get(i));
            // 让剩下的牌居中
            for (int j = 0; j < nowcard.size(); j++) {
                if (nowcard.size() > 1) {
                    card_jiange = (int) ((800 - 90) / (nowcard.size() - 1));
                    if (card_jiange > 50) {
                        card_jiange = 50;
                    }
                } else {
                    card_jiange = 50;
                }
                myCardsTouchLayout.setDistance(mst.adjustXIgnoreDensity(card_jiange));
                nowcard.get(j).params.leftMargin = super.mst.adjustXIgnoreDensity(card_jiange * j);
            }
            stopTimer(0);// 停止自己的定时器
            if (!sigleTime) {
                startPlayTimer(getNextOrder(mySelfOrder) + 1200);
            }
        }
        // 打完牌后取消定时器
        if (myCardsTouchLayout.getChildCount() == 0) {
            cancelTimer();
        }
    }

    /**
     * 将选中的牌添加到移动的View中
     * 
     * @param passCardList
     */
    public void cardAddMoveView(List<Poker> passCardList) {
        play1PassLayout.removeAllViews();
        for (int i = 0; i < passCardList.size(); i++) {
            RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
                    mst.adjustXIgnoreDensity(50), mst.adjustXIgnoreDensity(68));
            Poker image = new Poker(this);
            image.getPokeImage().setImageDrawable(
                    ImageUtil.getResDrawable(passCardList.get(i).getBitpamResID(), true));
            ;
            // image.setImageResource(pass.get(i).getBitpamResID());
            params3.leftMargin = mst.adjustXIgnoreDensity(20 * i);
            play1PassLayout.addView(image, params3);// 显示出来
            // mst2.adjustView(image);
            // 清除图片
            ImageUtil.releaseDrawable(passCardList.get(i).getPokeImage().getDrawable());
            // 移除出的牌
            nowcard.remove(passCardList.get(i));
            myCardsTouchLayout.removeView(passCardList.get(i));
            // 让剩下的牌居中
            for (int j = 0; j < nowcard.size(); j++) {
                if (nowcard.size() > 1) {
                    card_jiange = (int) ((800 - 90) / (nowcard.size() - 1));
                    if (card_jiange > 50) {
                        card_jiange = 50;
                    }
                } else {
                    card_jiange = 50;
                }
                myCardsTouchLayout.setDistance(mst.adjustXIgnoreDensity(card_jiange));
                nowcard.get(j).params.leftMargin = super.mst.adjustXIgnoreDensity(card_jiange * j);
            }
        }
    }

    /**
     * 托管
     */
    public void setTuoGuan() {
        for (int i = 0; i < nowcard.size(); i++) {
            poker[nowcard.get(i).getNumber()].params.topMargin = mst.adjustYIgnoreDensity(20);
            poker[nowcard.get(i).getNumber()]
                    .setLayoutParams(poker[nowcard.get(i).getNumber()].params);
            poker[nowcard.get(i).getNumber()].ischeck = false;
        }
        if (bierenchupai == null) {// 如果是自己先出牌
            if (nowcard.size() != 0) {
                stopTimer(0);// 停止自己的定时器
                List<Poker> tuoguanCards = new ArrayList<Poker>();
                tuoguanCards.add(nowcard.get(nowcard.size() - 1));
                cardAddview(tuoguanCards, true);
                // CmdUtils.play(tuoguanCards);
                prePlayOrder = 1;
                curPlayOrder = 2;
                PlayAlone playAlone = new PlayAlone();
                playAlone.setCards(tuoguanCards);
                logicNext.allPlayedPoker.addAll(tuoguanCards);
                logicPre.allPlayedPoker.addAll(tuoguanCards);
                playAlone.setCount(nowcard.size());
                // 标志用来计算春天
                if (!isFirstPlay && mySelfOrder == masterOrder) {
                    isFirstPlay = true;
                    firstPlaySize = 20 - playAlone.getCards().size();
                }
                playCards(playAlone);
                if (myCardsTouchLayout.getChildCount() > 0) {
                    startPlayTimer(getNextOrder(mySelfOrder) + 1200);
                    initTiShiCount();
                    zhezhao1.setVisibility(View.VISIBLE);
                    ImageView img = (ImageView) findViewById(getNextOrder(mySelfOrder) + 1400);
                    img.setVisibility(View.GONE);
                    // 抹去上轮出的牌
                    RelativeLayout res = (RelativeLayout) findViewById((getNextOrder(mySelfOrder)) + 1000);
                    res.removeAllViews();
                }
                play1SurplusCount.setText("" + myCardsTouchLayout.getChildCount());
            }
        } else {
            checkOtherChupai(bierenchupai);
            DouDiZhuData data = new DouDiZhuData(nowcard);
            DouDiZhuData datas = new DouDiZhuData(nowcard);
            data.fillPokerList();
            List<List<Poker>> tishiList = data.getTiShi(otherplay1);
            checkOtherChupai(bierenchupai);
            datas.fillAllPokerList();
            List<List<Poker>> tishiList2 = datas.getTiShi(otherplay1);
            HintPokerUtil aList = new HintPokerUtil();
            if (tishiList != null && tishiList2 != null) {
                tishiList = aList.filterHintPoker(tishiList, tishiList2);
            }
            if (tishiList == null) {
                passCard();
                return;
            }
            if (tishiList != null && tishiList.size() == 0) {
                passCard();
                return;
            }
            // tiShiCount=-1;
            setTiShiCount();
            if (getTiShiCount() > tishiList.size() - 1) {
                initTiShiCount();
                setTiShiCount();
            }
            List<Poker> tiShiPoker = tishiList.get(getTiShiCount());
            if (tiShiPoker == null) {
                passCard();
                return;
            }
            for (int i = 0; i < tiShiPoker.size(); i++) {
                poker[tiShiPoker.get(i).getNumber()].params.topMargin = 0;
                poker[tiShiPoker.get(i).getNumber()].setLayoutParams(poker[tiShiPoker.get(i)
                        .getNumber()].params);
                poker[tiShiPoker.get(i).getNumber()].ischeck = true;
            }
            initTiShiCount();
            playCard(false);
        }
        hiddenPlayBtn();
        if (tilaLayout.getVisibility() == View.VISIBLE
                && playBtnLayout.getVisibility() != View.VISIBLE
                && jiaofenLayout.getVisibility() != View.VISIBLE) {
            callBuJiaBei();
        }
    }

    /**
     * 显示剩下的牌数
     * 
     * @param Rid
     * @param paiCount
     * @param order
     */
    public void setShengxiaPai(int paiCount, final int order) {
        if (order != mySelfOrder) {
            final TextView nowView = (TextView) findViewById(1100 + order);
            nowView.setText(String.valueOf(paiCount));
            // 牌数少于三张,且之前没有警告过
            if (paiCount <= 3 && null != warn && warn.containsKey(1100 + order)
                    && !warn.get(1100 + order)) {
                AudioPlayUtils.getInstance().playSound(R.raw.audio_warn);// 警告
                Animation animationjg = AnimationUtils.loadAnimation(this, R.anim.my_scale_action);
                nowView.startAnimation(animationjg);
                animationjg.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        nowView.setTextColor(getResources().getColor(color.gold));
                        warn.put(1100 + order, true);
                    }
                });
                nowView.setTextColor(getResources().getColor(color.red));
            }
        } else { // 自己出的牌
            play1SurplusCount.setText(String.valueOf(paiCount));
        }
    }

    /**
     * 把牌的字符串转化成数组
     * 
     * @param cards
     * @return
     */
    public int[] setFirstCard(String cards) {
        String str = cards.substring(1, cards.length() - 1);
        String[] listCard = str.split(",");
        pai = new int[listCard.length];
        for (int i = 0; i < listCard.length; i++) {
            pai[i] = Integer.parseInt(listCard[i]);
        }
        return pai;
    }

    /**
     * 设置出牌的顺序
     * 
     * @param myOrder 自己的出牌顺序
     */
    public void setOrder(int myOrder) {
        if (myOrder == 1) {
            play2PassLayout.setId(1002);
            play3PassLayout.setId(1003);
            play2SurplusCount.setId(1102);
            play3SurplusCount.setId(1103);
            play2Timer.setId(1202);
            play3Timer.setId(1203);
            PLAY2ICON_ID = 1302;
            PLAY3ICON_ID = 1303;
            ZHEZHAO2_ID = 1402;
            ZHEZHAO3_ID = 1403;
            JIABEI2_ID = 1602;
            JIABEI3_ID = 1603;
        } else if (myOrder == 2) {
            play2PassLayout.setId(1003);
            play3PassLayout.setId(1001);
            play2SurplusCount.setId(1103);
            play3SurplusCount.setId(1101);
            play2Timer.setId(1203);
            play3Timer.setId(1201);
            PLAY2ICON_ID = 1303;
            PLAY3ICON_ID = 1301;
            ZHEZHAO2_ID = 1403;
            ZHEZHAO3_ID = 1401;
            JIABEI2_ID = 1603;
            JIABEI3_ID = 1601;
        } else if (myOrder == 3) {
            play2PassLayout.setId(1001);
            play3PassLayout.setId(1002);
            play2SurplusCount.setId(1101);
            play3SurplusCount.setId(1102);
            play2Timer.setId(1201);
            play3Timer.setId(1202);
            PLAY2ICON_ID = 1301;
            PLAY3ICON_ID = 1302;
            ZHEZHAO2_ID = 1401;
            ZHEZHAO3_ID = 1402;
            JIABEI2_ID = 1601;
            JIABEI3_ID = 1602;
        }
        play2Icon.setId(PLAY2ICON_ID);
        play3Icon.setId(PLAY3ICON_ID);
        zhezhao2.setId(ZHEZHAO2_ID);
        zhezhao3.setId(ZHEZHAO3_ID);
        jiabei2Iv.setId(JIABEI2_ID);
        jiabei3Iv.setId(JIABEI3_ID);
    }

    /**
     * 一开始对自己判断是否叫地主
     * 
     * @param fapai
     */
    public void callDizhu(int preOrder) {
        // 如果是轮到自己叫地主,显示叫地主的布局
        if (preOrder == mySelfOrder) {
            isTurnMySelf = true;
            // jiaofenLayout.setVisibility(View.VISIBLE);
        } else {
            isTurnMySelf = false;
            if (preOrder == getNextOrder(mySelfOrder)) { // 下家
                startQiangTimer(1);
            } else { // 上家
                startQiangTimer(-1);
            }
        }
    }

    /**
     * 根据服务器发送的消息来判断是否自己叫地主
     * 
     * @param grab
     */
    public void truntoCallDizhu(Grab grab) {
        int currentCallOrder = grab.getNextOrder(); // 当前叫分人的位置编号
        // 叫地主显示
        // setJiaofenXianshi(grab.getRatio(), getPerOrder(currentCallOrder));
        if (calledPoint < grab.getRatio()) {
            calledPoint = grab.getRatio();
        }
        if (grab.getRatio() != 0) { // 设置房间倍数
            beishuNumView.setText(String.valueOf(calledPoint));
        }
        if (currentCallOrder == mySelfOrder) { // 轮到自己叫分
            if (isTuoguan) {
                callPoint(0);
                return;
            } else {
                // 叫地主提示
                setJiaofenXianshi(grab.getRatio(), getPerOrder(currentCallOrder));
                jiaofenLayout.setVisibility(View.VISIBLE);
                // 根据别人叫分情况让某些叫分按钮不可按
                if (grab.getRatio() == 1) { // 上次叫1分
                    fen1.setClickable(false);
                    fen1.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.fen1_disable,
                            true));
                } else if (grab.getRatio() == 2) {// 上次叫2分
                    fen1.setClickable(false);
                    fen1.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.fen1_disable,
                            true));
                    fen2.setClickable(false);
                    fen2.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.fen2_disable,
                            true));
                }
            }
            if (oneTurns >= 3) {
                jiaofenLayout.setVisibility(View.INVISIBLE);
            }
        } else if (currentCallOrder != mySelfOrder) { // 下家叫的
            startQiangTimer(-1);
        }
        // 叫地主显示
        setJiaofenXianshi(grab.getRatio(), getPerOrder(currentCallOrder));
        turnsCallPoint(turnsCallOrder);
    }

    /**
     * 当前玩家的上一家位置编号
     * 
     * @param currentOrder
     * @return
     */
    public int getPerOrder(int currentOrder) {
        int now = 0;
        if (currentOrder == 1) {
            now = 3;
        } else {
            now = currentOrder - 1;
        }
        return now;
    }

    /**
     * 根据自己的order 得出下一个order是谁
     * 
     * @param nextOrder
     * @return
     */
    public int getNextOrder(int Order) {
        int next = 0;
        if (Order == 3) {
            next = 1;
        } else {
            next = Order + 1;
        }
        return next;
    }

    /**
     * 设置地主的头像
     * 
     * @param msterOrder
     */
    public void setDizhuIcon(int msterOrder) {
        if (msterOrder == mySelfOrder) { // 如果自己是地主的话
            // GameUser mGameUser = Database.userMap.get(mySelfOrder);
            ActivityUtils.setHead(play1Icon, "0", true);// 地主
            int po1 = getNextOrder(msterOrder);
            ImageView icon = (ImageView) findViewById(1300 + po1);
            if (null != icon) {
                ActivityUtils.setHead(icon, "0", false);// 农民
            }
            int po2 = getNextOrder(po1);
            ImageView icon1 = (ImageView) findViewById(1300 + po2);
            if (null != icon1) {
                ActivityUtils.setHead(icon1, "0", false);// 农民
            }
            zhezhao1.setVisibility(View.GONE);
        } else {
            ImageView icon2 = (ImageView) findViewById(1300 + msterOrder);
            // icon.setBackgroundResource(R.drawable.dizhu);
            if (null != icon2) {
                ActivityUtils.setHead(icon2, "0", true);// 地主
            }
            int po3 = getNextOrder(msterOrder);
            if (po3 == mySelfOrder) {
                ActivityUtils.setHead(play1Icon, "0", false);// 农民
            } else {
                ImageView icon3 = (ImageView) findViewById(1300 + po3);
                if (null != icon3) {
                    ActivityUtils.setHead(icon3, "0", false);// 农民
                }
            }
            int po4 = getNextOrder(po3);
            if (po3 == mySelfOrder) {
                ActivityUtils.setHead(play1Icon, "0", false);// 农民
            } else {
                ImageView icon4 = (ImageView) findViewById(1300 + po4);
                if (null != icon4) {
                    ActivityUtils.setHead(icon4, "0", false);// 农民
                }
            }
            ImageView zhezhao = (ImageView) findViewById(1400 + msterOrder);
            if (null != zhezhao) {
                zhezhao.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 地主产生后开始定时器
     */
    public void startOtherTimer() {
        if (mySelfOrder != masterOrder) {
            startPlayTimer(masterOrder + 1200);
        }
    }

    /**
     * 抢地主定时器开始 （-1 上家 0 自己 1 下家）
     */
    public void startQiangTimer(int callOrder) {
        if (gameTask != null) {
            gameTask.stop(true);
            gameTask = null;
        }
        gameTask = new DizhuTask(callOrder);
        ScheduledTask.addRateTask(gameTask, 1000);
    }

    /**
     * 踢拉定时器开始 （-1 上家 0 自己 1 下家）
     */
    public void startTiLaTimer(int callOrder) {
        if (gameTask != null) {
            gameTask.stop(true);
            gameTask = null;
        }
        gameTask = new TiLaTask(callOrder);
        ScheduledTask.addRateTask(gameTask, 1000);
    }

    /**
     * 打牌定时器开始
     */
    public void startPlayTimer(int id) {
        if (gameTask != null) {
            gameTask.stop(true);
            gameTask = null;
        }
        gameTask = new DapaiTask(id);
        ScheduledTask.addRateTask(gameTask, 1000);
    }

    /**
     * 取消并重置自己的定时器
     * 
     * @param timerOrder 当前定时的人 0自己 1下家 -1上家
     */
    public void stopTimer(int timerOrder) {
        cancelTimer();
        if (timerOrder == 0) {
            play1Timer.setVisibility(View.GONE);
            play1Timer.setText(String.valueOf(Constant.WAIT_TIME));
        } else if (timerOrder == 1) {
            play2Timer.setVisibility(View.GONE);
            play2Timer.setText(String.valueOf(Constant.WAIT_TIME));
        } else if (timerOrder == -1) {
            play3Timer.setVisibility(View.GONE);
            play3Timer.setText(String.valueOf(Constant.WAIT_TIME));
        }
    }

    /**
     * 取消并重置打牌的定时器
     */
    public void setTimer0(int id) {
        cancelTimer();
        TextView now = (TextView) findViewById(id);
        if (now != null) {
            now.setVisibility(View.GONE);
            now.setText(String.valueOf(Constant.WAIT_TIME));
        }
    }

    /**
     * 根据地主产生来决定是否需要更新自己的牌
     * 
     * @param master
     */
    public void genxinMycard(List<Integer> lastCards, int mastOder) {
        // 清除叫地主信息
        // cleanAllChuPaiInfo();
        for (int i = 0; i < nowcard.size(); i++) {
            poker[nowcard.get(i).getNumber()].params.topMargin = mst.adjustYIgnoreDensity(20);
            poker[nowcard.get(i).getNumber()]
                    .setLayoutParams(poker[nowcard.get(i).getNumber()].params);
            poker[nowcard.get(i).getNumber()].ischeck = false;
        }
        // 如果自己是地主的话
        if (mastOder == mySelfOrder) {
            int[] now = new int[20];
            for (int i = 0; i < pai.length; i++) {
                now[i] = pai[i];
            }
            for (int i = 0; i < lastCards.size(); i++) {
                now[i + 17] = lastCards.get(i).intValue();
            }
            // 清除再添加自己手中的牌
            for (int i = 0; i < paixu.length; i++) {
                myCardsTouchLayout.removeView(poker[paixu[i]]);
            }
            nowcard.clear();
            addCard(now);
            // 把地主的牌数加上3
            play1SurplusCount.setText("" + now.length);
            // 添加地主的牌在桌面上
            dizhuPaiLayout.removeAllViews();
            for (int i = 0; i < 3; i++) {
                Poker ca = new Poker(this);
                ca.getPokeImage().setImageDrawable(
                        ImageUtil.getResDrawable(
                                poker[lastCards.get(i).intValue()].getBitpamResID(), true));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        mst.adjustXIgnoreDensity(50), mst.adjustYIgnoreDensity(68));
                params.leftMargin = mst.adjustXIgnoreDensity(13 * i);
                dizhuPaiLayout.addView(ca, params);
                firstChupai = false;
            }
            play1PassLayout.removeAllViews();
            showPlayBtn(true);
            if (isTuoguan) { // 如果当前正在托管
                setTuoGuan();
            } else {
                jiaofenLayout.setVisibility(View.GONE);
                // 开启一个定时器
                // startPlayTimer(R.id.play1Time);
            }
            logicNext = new DouDiZhuLogic(nextUserPoker);
            logicPre = new DouDiZhuLogic(preUserPoker);
        } else { // 如果自己不是地主的话
            // 把地主的牌数加上3
            TextView cardCount = (TextView) findViewById(1100 + mastOder);
            cardCount.setText("20");
            // 添加地主的牌在桌面上
            dizhuPaiLayout.removeAllViews();
            for (int i = 0; i < 3; i++) {
                Poker ca = new Poker(this);
                // 有过空指针异常，，，需要捕捉parseInt(dizhuCard[i])] dizhuCard[] == ""
                ca.getPokeImage().setImageDrawable(
                        ImageUtil.getResDrawable(
                                poker[lastCards.get(i).intValue()].getBitpamResID(), true));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        mst.adjustXIgnoreDensity(50), mst.adjustYIgnoreDensity(68));
                params.leftMargin = mst.adjustXIgnoreDensity(13 * i);
                dizhuPaiLayout.addView(ca, params);
            }
            if (mastOder == 2) {
                nextUserPoker.addAll(getFirstCard(lastCards));
            } else if (mastOder == 3) {
                preUserPoker.addAll(getFirstCard(lastCards));
            }
            curPlayOrder = mastOder;
            logicNext = new DouDiZhuLogic(nextUserPoker);
            logicPre = new DouDiZhuLogic(preUserPoker);
            PlayAlone playAlone = new PlayAlone();
            playAlone.setCount(nowcard.size());
            playCards(playAlone);
        }
    }

    /**
     * 打牌更新界面之类的
     * 
     * @param play
     * @param reShow 是否是重连显示操作
     */
    public void playCard(PlayAlone play, boolean reShow) {
        // 取得别人出的牌并消除之前出过的牌
        int nextPlayId = getPerOrder(play.getNextOrder()) + 1000;
        nextPlayLayout = (RelativeLayout) findViewById(nextPlayId);
        // 设置上一轮的计时器清理 高亮头像切换
        if (play.getCount() > 0) {
            int playOrder = getPerOrder(play.getNextOrder());
            if (playOrder == mySelfOrder) {
                setTimer0(R.id.play1Time);
                zhezhao1.setVisibility(View.VISIBLE);
            } else {
                setTimer0(playOrder + 1200);
                ImageView img = (ImageView) findViewById(playOrder + 1400);
                img.setVisibility(View.VISIBLE);
            }
            if (nextPlayLayout != null && nextPlayLayout.getChildCount() != 0) {
                nextPlayLayout.removeAllViews();
            }
        } else {
            cancelTimer();
        }
        // 如果别人不要
        if (play.getCards() == null || play.getCards().size() == 0) {
            String gender = "0";
            if ("1".equals(gender)) {
                AudioPlayUtils.getInstance().playSound(R.raw.nv_pass);// 不出
            } else {
                AudioPlayUtils.getInstance().playSound(R.raw.nan_pass);// 不出
            }
            ImageView im = new ImageView(ctx);
            im.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.play_buchu, true));
            nextPlayLayout.addView(im, mst.getAdjustLayoutParamsForImageView(im));
            ActivityUtils.startScaleAnim(nextPlayLayout, ctx);// 播放缩放动画
        } else {
            // 别人只要出牌了到自己出牌的时候就要检测对手牌
            firstChupai = false;
            // 别人出牌显示出来哇
            List<Poker> carList = new ArrayList<Poker>();
            for (int i = 0; i < play.getCards().size(); i++) {
                Poker c = poker[play.getCards().get(i).getNumber()];
                c.getPokeImage().setImageDrawable(
                        ImageUtil.getResDrawable(
                                poker[play.getCards().get(i).getNumber()].getBitpamResID(), true));
                c.params.leftMargin = mst.adjustXIgnoreDensity(20) * i;
                c.params.topMargin = 0;
                c.params.height = mst.adjustXIgnoreDensity(68); // 91
                c.params.width = mst.adjustXIgnoreDensity(50); // 68
                if (c.getParent() != null) {
                    ((RelativeLayout) c.getParent()).removeView(c);
                }
                nextPlayLayout.addView(c, c.params);
                if (nextPlayLayout != null) {
                }
                carList.add(c);
            }
            /** 记录别人已经出的牌 **/
            addOutPokers(play.getOrder(), carList);
            int type = DoudizhuRule.checkpai(carList);
            int value = DoudizhuRule.checkpaiValue(type, carList);
            playDiZhuCardAudio(type, value, "0"); // 出牌的声音
            // 如果是炸弹显示特效
            PlayCardEffect.bomEffect(type, doudizhuBackGround);
            if (play.getCount() > 0) {
                // 如果下一个打牌的人是自己的话
                if (play.getNextOrder().intValue() == mySelfOrder) {
                    play1PassLayout.removeAllViews();
                }
            }
            int[] cad = new int[play.getCards().size()];
            for (int i = 0; i < play.getCards().size(); i++) {
                cad[i] = play.getCards().get(i).getNumber();
            }
            bierenchupai = cad;
            // 检测加倍
            checkJiaBei(checkOtherChupai(bierenchupai), reShow);
        }
        if (play.getCount() == 0) { // 已出完牌 此牌为最后一手
            // 发送结束命令
            sendEndDialog(play);
            return;
        }
        // 如果下一个是自己出牌
        if (play.getNextOrder() == mySelfOrder) {
            isTurnMySelf = true;
            zhezhao1.setVisibility(View.GONE);
            play1PassLayout.removeAllViews();
            if (play.getCards() == null || play.getCards().size() == 0) {
                if (play.getPrecards() != null && play.getPrecards().size() > 0) {
                    int[] cad = new int[play.getPrecards().size()];
                    for (int i = 0; i < play.getPrecards().size(); i++) {
                        cad[i] = play.getPrecards().get(i).getNumber();
                    }
                    bierenchupai = cad;
                }
            }
            if (bierenchupai == null) { // 如果别人都没有出牌，说明这轮你先出牌
                showPlayBtn(true);
            } else {
                showPlayBtn(false);
            }
            if (!isTuoguan) { // 如果没有托管
                isWaitFiveSecond();
                play1PassLayout.removeAllViews();
            } else {
                setTuoGuan();
            }
        } else {
            isTurnMySelf = false;
            // 最后开启别人一个打牌的定时器
            startPlayTimer(play.getNextOrder() + 1200);
            ImageView img = (ImageView) findViewById(play.getNextOrder() + 1400);
            img.setVisibility(View.GONE);
            // 抹去上轮出的牌
            RelativeLayout res = (RelativeLayout) findViewById(play.getNextOrder() + 1000);
            res.removeAllViews();
            playCards(play);
        }
    }

    /**
     * 叫地主定时器
     * 
     * @author Administrator
     */
    class DizhuTask extends AutoTask { // TimerTask { // 叫地主定时器

        private int callOrder; // 是否是自己叫地主 （-1 上家 0 自己 1 下载）

        public DizhuTask(int callOrder) {
            this.callOrder = callOrder;
            if (callOrder == 0) { // 自己
                play1Timer.setVisibility(View.VISIBLE);
                play1Timer.setText(String.valueOf(Constant.WAIT_TIME));
            } else if (callOrder == 1) { // 下家
                play2Timer.setVisibility(View.VISIBLE);
                play2Timer.setText(String.valueOf(Constant.WAIT_TIME));
            } else if (callOrder == -1) { // 上家
                play3Timer.setVisibility(View.VISIBLE);
                play3Timer.setText(String.valueOf(Constant.WAIT_TIME));
            }
        }

        public void run() {
            Message msg = new Message();
            msg.arg1 = callOrder;
            msg.what = 5;
            handler.sendMessage(msg);
        }
    }

    /**
     * 踢拉定时器
     * 
     * @author Administrator
     */
    class TiLaTask extends AutoTask { // TimerTask { //

        private int callOrder; // 是否是自己叫地主 （-1 上家 0 自己 1 下载）

        public TiLaTask(int callOrder) {
            this.callOrder = callOrder;
            if (callOrder == 0) { // 自己
                play1Timer.setVisibility(View.VISIBLE);
                play1Timer.setText(String.valueOf(Constant.WAIT_TIME));
            } else if (callOrder == 1) { // 下家
                play2Timer.setVisibility(View.VISIBLE);
                play2Timer.setText(String.valueOf(Constant.WAIT_TIME));
            } else if (callOrder == -1) { // 上家
                play3Timer.setVisibility(View.VISIBLE);
                play3Timer.setText(String.valueOf(Constant.WAIT_TIME));
            }
        }

        public void run() {
            Message msg = new Message();
            msg.arg1 = callOrder;
            msg.what = 13;
            handler.sendMessage(msg);
        }
    }

    /**
     * dapai定时器
     * 
     * @author Administrator
     */
    class DapaiTask extends AutoTask { // TimerTask { // dapai定时器

        private int id = 0;

        public DapaiTask(int id) {
            this.id = id;
            TextView time = (TextView) findViewById(id);
            time.setVisibility(View.VISIBLE);
            time.setText(String.valueOf(Constant.WAIT_TIME));
        }

        public void run() {
            Message msg = new Message();
            msg.what = 6;
            msg.arg1 = id;
            handler.sendMessage(msg);
        }
    }

    /**
     * 宝箱定时器
     * 
     * @author Administrator
     */
    class BaoXiangTask extends AutoTask {

        public void run() {
            handler.sendEmptyMessage(303);
        }
    }

    /**
     * 头像定时器
     * 
     * @author Administrator
     */
    class HeadTask extends AutoTask {

        public void run() {
            handler.sendEmptyMessage(301);
        }
    }

    /**
     * 取消播放
     */
    public void stopBusyLoadAnim(int busyResId) {
        ImageView busyImageView = (ImageView) findViewById(busyResId);
        busyImageView.setVisibility(View.GONE);
    }

    /**
     * 斗地主出牌声音
     */
    public void playDiZhuCardAudio(int audioType, int value, final String gender) {
        switch (audioType) {
            case DoudizhuRule.Danpai: // 如果是一张牌
                if (value > 0)
                {
                    value -= 3;
                    if (value >= 0 && value < sound_single[0].length)
                    {
                        AudioPlayUtils
                                .getInstance()
                                .playSound(
                                        sound_single["1".equals(gender) ? 1
                                                : 0][value]); // 出牌
                    }
                }
                // AudioPlayUtils.getInstance().playSound(R.raw.outcard); // 出牌
                break;
            case DoudizhuRule.Yidui:// 如果是两张牌
                if (value > 0)
                {
                    value -= 3;
                    if (value >= 0 && value < sound_pair[0].length)
                    {
                        AudioPlayUtils.getInstance()
                                .playSound(
                                        sound_pair["1".equals(gender) ? 1
                                                : 0][value]); // 出牌
                    }
                }
                // AudioPlayUtils.getInstance().playSound(R.raw.outcard); // 出牌
                break;
            case DoudizhuRule.wangzha:// 如果是两张牌 王炸
                wangzhaImageView.setVisibility(View.VISIBLE);
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playMultiMusic2(R.raw.nv_wangzha,
                            R.raw.boombeffect);
                } else {
                    AudioPlayUtils.getInstance().playMultiMusic2(R.raw.nan_wangzha,
                            R.raw.boombeffect);
                }
                AnimUtils.playAnim(wangzhaImageView, ImageUtil.getResAnimaSoft("wanBomb"), 2000);
                setTweenAnim(wangzhaImageView, R.anim.wangzha_out, IS_WANGZHA_ANIM);
                break;
            case DoudizhuRule.Santiao:// 如果是三张牌
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_3dai0);
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_3dai0);
                }
                break;
            case DoudizhuRule.zhadan: // 如果是四张牌 炸弹
                zhadanIv.setVisibility(View.VISIBLE);
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playMultiMusic2(R.raw.nv_bomb, R.raw.boombeffect);
                } else {
                    AudioPlayUtils.getInstance().playMultiMusic2(R.raw.nan_bomb, R.raw.boombeffect);
                }
                setTweenAnim(zhadanIv, R.anim.zhadang_play, IS_ZHADAN_ANIM);
                break;
            case DoudizhuRule.Sandaiyi:// 如果是四张牌 三带一
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_3dai1);
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_3dai1);
                }
                break;
            case DoudizhuRule.Sandaier:// 如果是五张牌 三待二
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_3dai2);
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_3dai2);
                }
                break;
            case DoudizhuRule.sidaiyi: // 如果是6张 "4带2
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_4dai2);
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_4dai2);
                }
                break;
            case DoudizhuRule.shunzi: // 顺牌
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_shunzi);
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_shunzi);
                }
                shunzImageView.setVisibility(View.VISIBLE);
                AnimUtils.playAnim(shunzImageView, ImageUtil.getResAnimaSoft("shunz"), 3000);
                break;
            case DoudizhuRule.liandui: // 如果是6张 连对
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_liandui);
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_liandui);
                }
                break;
            case DoudizhuRule.sidaier: // 检测4帶2對
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playSound(R.raw.nv_4dai22);
                } else {
                    AudioPlayUtils.getInstance().playSound(R.raw.nan_4dai22);
                }
                break;
            case DoudizhuRule.feiji: // 如果是6张 飞机
            case DoudizhuRule.feijidaisan: // 飞机带2
            case DoudizhuRule.feijidaidui: // 飞机带4
                if ("1".equals(gender)) {
                    AudioPlayUtils.getInstance().playMultiMusic2(R.raw.nv_feiji, R.raw.planeeffect);
                } else {
                    AudioPlayUtils.getInstance()
                            .playMultiMusic2(R.raw.nan_feiji, R.raw.planeeffect);
                }
                feijiImageView.setVisibility(View.VISIBLE);
                AnimUtils.playAnim(feijiImageView, ImageUtil.getResAnimaSoft("feiji"), 2000);
                setTweenAnim(feijiImageView, R.anim.feiji_out, IS_FEIJI_ANIM);
                break;
            default:
                AudioPlayUtils.getInstance().playSound(R.raw.outcard);
        }
    }

    // // 短语，思考
    // private List<String> getMessData() {
    // String[] items = getResources().getStringArray(R.array.mes_language);
    // List<String> list = Arrays.asList(items);
    // return list;
    // }
    //
    // private List<String> getThinkData() {
    // String[] items = getResources().getStringArray(R.array.think_language);
    // List<String> list = Arrays.asList(items);
    // return list;
    // }

    /**
     * 屏幕点击事件
     */
    public boolean onTouch(View view, MotionEvent event) {
        if (view.getId() == doudizhuBackGround.getId()) {
            if (userinfoshowView.getVisibility() == View.VISIBLE) {
                userinfoshowView.setVisibility(View.GONE);
                userInfoText.setText("");
                BitmapDrawable drawable = (BitmapDrawable) userinfoshowView.getBackground();
                drawable.getBitmap().recycle();
                drawable.setCallback(null);
            }
        }
        return this.gestureDetector.onTouchEvent(event);
    }

    public void recyleDrawable() {
        doudizhuLayout.removeAllViews();
    }

    public void cancelTimer() {
        if (gameTask != null) {
            gameTask.stop(true);
            gameTask = null;
            play1Timer.setVisibility(View.GONE);
            play2Timer.setVisibility(View.GONE);
            play3Timer.setVisibility(View.GONE);
        }
    }

    public void setTiShiCount() {
        tiShiCount++;
        tishi.setBackgroundResource(R.drawable.again_btn_bg);
    }

    public void initTiShiCount() {
        tiShiCount = -1;
        tishi.setBackgroundResource(R.drawable.tishi_btn_bg);
    }

    public int getTiShiCount() {
        return tiShiCount;
    }

    public void showMessage(CmdDetail mess) {
        if (null == Database.userMap) {
            return;
        }
        if (play2Order.getText().toString().equals("")
                || play3Order.getText().toString().equals("")) {
            return;
        }
        int play2O = Integer.parseInt(play2Order.getText().toString());
        int play3O = Integer.parseInt(play3Order.getText().toString());
        for (int i = 1; i <= 3; i++) {
            GameUser gu = Database.userMap.get(i);
            if (gu.getLoginToken().endsWith(mess.getFromUserId())
                    || gu.getAccount().equals(mess.getFromUserId())) {
                TextView textView = new TextView(this);
                textView.setTextColor(android.graphics.Color.RED);
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);
                if (i == play2O) {
                    rightFrame.removeAllViews();
                    if (mess.getType() == Constant.MESSAGE_TYPE_ZERO) {
                        textView.setBackgroundResource(R.drawable.you_mess_view);
                        textView.setGravity(Gravity.CENTER);
                        rightFrame.addView(textView);
                    } else if (mess.getType() == Constant.MESSAGE_TYPE_TWO) {
                    } else if (mess.getType() == Constant.MESSAGE_TYPE_ONE) {
                        textView.setBackgroundResource(R.drawable.you_mess_view);
                        textView.setGravity(Gravity.CENTER);
                        rightFrame.addView(textView);
                    }
                    if (mess.getType() == Constant.MESSAGE_TYPE_THREE) {
                        // startTask(girlRightFrame, rightTask);
                        // messageFrame(girlRightFrame, null, 0, mess, textView);
                    } else {
                        startTask(rightFrame, rightTask);
                        messageFrame(rightFrame, null, 0, mess, textView);
                    }
                } else if (i == play3O) {
                    leftFrame.removeAllViews();
                    if (mess.getType() == Constant.MESSAGE_TYPE_ZERO) {
                        textView.setBackgroundResource(R.drawable.zuo_mess_view);
                        textView.setGravity(Gravity.CENTER);
                        leftFrame.addView(textView);
                    } else if (mess.getType() == Constant.MESSAGE_TYPE_TWO) {
                    } else if (mess.getType() == Constant.MESSAGE_TYPE_ONE) {
                        textView.setBackgroundResource(R.drawable.zuo_mess_view);
                        textView.setGravity(Gravity.CENTER);
                        leftFrame.addView(textView);
                    }
                    if (mess.getType() == Constant.MESSAGE_TYPE_THREE) {
                        // startTask(girlLeftFrame, leftTask);
                        // messageFrame(girlLeftFrame, null, 0, mess, textView);
                    } else {
                        startTask(leftFrame, leftTask);
                        messageFrame(leftFrame, null, 0, mess, textView);
                    }
                }
            }
            gu = null;
        }
    }

    /**
     * @param layout
     * @param talk 不为空表示自己在说话
     * @param messType
     * @param mess
     * @param textView
     */
    public void messageFrame(final LinearLayout layout, String talk, int messType, CmdDetail mess,
            TextView... textView) {
        if (layout != null) {
            if (layout.getBackground() != null) {
                ImageUtil.releaseDrawable(layout.getBackground());
            }
            ImageUtil.clearGifCache();
        }
        if (null == talk) {
            int type = mess.getType();
            if (type == Constant.MESSAGE_TYPE_ZERO || type == Constant.MESSAGE_TYPE_ONE) {
                if (layout.getId() == leftFrame.getId()) {// 文字聊天
                    girlLeftFrame.setVisibility(View.GONE);
                }
                if (layout.getId() == rightFrame.getId()) {
                    girlRightFrame.setVisibility(View.GONE);
                }
                if (textView.length > 0) {
                    textView[0].setText(mess.getValue());
                }
            } else if (type == Constant.MESSAGE_TYPE_TWO) {// 表情动画
                if (layout.getId() == leftFrame.getId()) {
                    girlLeftFrame.setVisibility(View.GONE);
                }
                if (layout.getId() == rightFrame.getId()) {
                    girlRightFrame.setVisibility(View.GONE);
                }
                if (mess.getValue().contains("gif")) {
                    String imageName = mess.getValue().substring(0,
                            mess.getValue().lastIndexOf("."));
                    if (ImageUtil.getGifDrawable(imageName).getParent() != null) {
                        ((ViewGroup) ImageUtil.getGifDrawable(imageName).getParent())
                                .removeView(ImageUtil.getGifDrawable(imageName));
                    }
                    ImageUtil.getGifDrawable(imageName).showAnimation();
                    // ImageUtil.getGifDrawable(imageName).setShowDimension(mst.adjustXIgnoreDensity(150),
                    // mst.adjustYIgnoreDensity(150));
                    layout.addView(ImageUtil.getGifDrawable(imageName), new ViewGroup.LayoutParams(
                            mst.adjustXIgnoreDensity(150), mst.adjustYIgnoreDensity(150)));
                }
            }
            // else if (type == Constant.MESSAGE_TYPE_THREE) {// 美女
            // if (layout.getId() == girlLeftFrame.getId()) {
            // leftFrame.setVisibility(View.GONE);
            // String name = mess.getValue().substring(0, mess.getValue().lastIndexOf(".")) +
            // "_left";
            // girlLeftTv.setText(ImageUtil.getGirlSayText(name));
            // layout.setBackgroundDrawable(ImageUtil.getResDrawableByName(name, true, true));
            // }
            // if (layout.getId() == girlRightFrame.getId()) {
            // rightFrame.setVisibility(View.GONE);
            // String name = mess.getValue().substring(0, mess.getValue().lastIndexOf(".")) +
            // "_left";
            // girlRightTv.setText(ImageUtil.getGirlSayText(name));
            // int indext = -1;
            // if (null != cashList && cashList.size() > 3) {
            // for (int i = 0; i < cashList.size() - 3;) {
            // if (null != cashList.get(i) && !cashList.get(i).getImage().isRecycled()) {
            // cashList.get(i).getImage().recycle();
            // }
            // cashList.remove(i);
            // i = 0;
            // }
            // }
            // for (int i = 0, count = cashList.size(); i < count; i++) {
            // if (name.equals(cashList.get(i).getId())) {
            // indext = i;
            // break;
            // }
            // }
            // if (-1 == indext) {
            // Drawable drawable = ImageUtil.getResDrawableByName(name, true, true);
            // // Drawable转Bitmap
            // BitmapDrawable bd = (BitmapDrawable) drawable;
            // int height = Database.SCREEN_HEIGHT;
            // int width = Database.SCREEN_WIDTH;
            // String bitMapKey = name + "_small";
            // Bitmap bitmap = ImageUtil.getBitmap(bitMapKey, false);
            // if (bitmap == null) {
            // bitmap = ImageUtil.resizeBitmap(bd.getBitmap(), width, height - 1);
            // ImageUtil.addBitMap2Cache(bitMapKey, bitmap);
            // }
            // // Bitmap bitmap =
            // // ImageUtil.resizeBitmap(bd.getBitmap(), width, height
            // // - 1);
            // String canvasKey = name + "_canvas";
            // Drawable canvasDrawable = ImageUtil.getDrawableByKey(canvasKey);
            // if (canvasDrawable == null) {
            // Bitmap bitmap1 = Bitmap.createBitmap(width, height - 1, Bitmap.Config.ARGB_8888);
            // Canvas canvas = new Canvas(bitmap1);
            // canvas.save();
            // // 镜像翻转画布
            // canvas.scale(-1.0f, 1.0f, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            // canvas.drawBitmap(bitmap, 0, 0, null);
            // canvas.restore();
            // cashList.add(new BitmapVO(name, bitmap1));
            // cashList.add(new BitmapVO(name + "1", bitmap));
            // canvasDrawable = ImageUtil.addDrawable2Cache(canvasKey, new BitmapDrawable(bitmap1));
            // }
            // layout.setBackgroundDrawable(canvasDrawable);
            // } else {
            // layout.setBackgroundDrawable(new BitmapDrawable(cashList.get(indext).getImage()));
            // }
            // }
            // }
        } else {
            if (messType == Constant.MESSAGE_TYPE_ZERO || messType == Constant.MESSAGE_TYPE_ONE) {
                TextView myView = new TextView(this);
                myView.setText(talk);
                myView.setTextColor(android.graphics.Color.RED);
                myView.setBackgroundResource(R.drawable.mine_mess_view);
                myView.setGravity(Gravity.CENTER);
                girlLeftFrame.setVisibility(View.GONE);
                layout.addView(myView);
            }
            // else if (messType == Constant.MESSAGE_TYPE_ONE ) {
            // TextView myView = new TextView(this);
            // myView.setText(talk);
            // myView.setTextColor(android.graphics.Color.RED);
            // myView.setBackgroundResource(R.drawable.my_think);
            // myView.setGravity(Gravity.CENTER);
            // girlLeftFrame.setVisibility(View.GONE);
            // layout.addView(myView);
            // }
            else if (messType == Constant.MESSAGE_TYPE_TWO) {
                if (talk.contains("gif")) {
                    talk = talk.substring(0, talk.lastIndexOf("."));
                    if (ImageUtil.getGifDrawable(talk).getParent() != null) {
                        ((ViewGroup) ImageUtil.getGifDrawable(talk).getParent())
                                .removeView(ImageUtil.getGifDrawable(talk));
                    }
                    girlLeftFrame.setVisibility(View.GONE);
                    ImageUtil.getGifDrawable(talk).showAnimation();
                    // ImageUtil.getGifDrawable(talk).setShowDimension(mst.adjustXIgnoreDensity(150),
                    // mst.adjustYIgnoreDensity(150));
                    layout.addView(
                            ImageUtil.getGifDrawable(talk),
                            new ViewGroup.LayoutParams(mst.adjustXIgnoreDensity(150), mst
                                    .adjustYIgnoreDensity(150)));
                }
            }
            // else if (messType == Constant.MESSAGE_TYPE_THREE) {
            // talk = talk.substring(0, talk.lastIndexOf(".")) + "_left";
            // myFrame.setVisibility(View.GONE);
            // girlLeftTv.setText(ImageUtil.getGirlSayText(talk));
            // layout.setBackgroundDrawable(ImageUtil.getResDrawableByName(talk, true, true));
            // int height = Database.SCREEN_HEIGHT;
            // int width = Database.SCREEN_WIDTH;
            // Log.i("Background", "left-- height:" + height + "  width: " + width + " name: " +
            // talk);
            // }
        }
        layout.setVisibility(View.VISIBLE);
    }

    public void startTask(final LinearLayout layout, AutoTask autoTask) {
        if (autoTask != null) {
            autoTask.stop(true);
            autoTask = null;
        }
        autoTask = new AutoTask() {

            public void run() {
                Database.currentActivity.runOnUiThread(new Runnable() {

                    public void run() {
                        layout.setVisibility(View.GONE);
                    }
                });
            }
        };
        ScheduledTask.addDelayTask(autoTask, 3000);
    }

    /**
     * 游戏指令回调
     */
    /**
     * 设置玩家比赛场信息
     * 
     * @throws
     */
    /**
     * 断网重连处理
     * 
     * @throws
     */
    /**
     * 显示或隐藏”排名“按钮
     */
    private void visibleOrGoneRankBtn() {
        // 如果是比赛场并且不是第一场，则显示”排名“按钮
        // if (1 == Database.JOIN_ROOM.getRoomType()) {// 普通赛制
        // // if (0 == Database.USER.getRound()) {
        // // rankTop.setVisibility(View.GONE);
        // // } else {
        // // }
        // rankTop.setVisibility(View.VISIBLE);
        // }
    }

    /**
     * 显示重连前2手牌
     * 
     * @throws
     */
    /**
     * 加入房间前校验线程任务
     */
    public void showPubMess(String mess) {
        if (Database.userMap == null) {
            return;
        } else if (mess != null && !mess.equals("")) {
            // if (pubTimer != null) {
            // pubTimer.cancel();
            // pubTimer.purge();
            // pubTimer = null;
            // }
            publicLayout.setVisibility(View.VISIBLE);
            marqueeText.currentScrollX = 0;
            marqueeText.setText(mess);
            marqueeText.startScroll();
            marqueeText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            marqueeText.setSingleLine(true);
            marqueeText.setFocusable(true);
            // pubTimer = new Timer();
            int messLong = mess.length();
            int timeValue = messLong / 2;
            // pubTimer(publicLayout, pubTimer, timeValue);
            pubTimer(publicLayout, pubTask, timeValue);
        }
    }

    public void pubTimer(final LinearLayout layout, AutoTask autoTask, int value) {
        if (autoTask != null) {
            autoTask.stop(true);
            autoTask = null;
        }
        autoTask = new AutoTask() {

            public void run() {
                Database.currentActivity.runOnUiThread(new Runnable() {

                    public void run() {
                        // textView.stopScroll();
                        layout.setVisibility(View.GONE);
                    }
                });
            }
        };
        int timelong = value * 1000;
        ScheduledTask.addDelayTask(autoTask, timelong);
    }

    private class BitmapVO {

        // private String id;
        private Bitmap image;

        // public BitmapVO(String id, Bitmap image) {
        // this.id = id;
        // this.image = image;
        // }

        // public String getId() {
        // return id;
        // }

        public Bitmap getImage() {
            return image;
        }
    }

    @Override
    public boolean hasTiShi(Poker mPoker, int indexs) {
        boolean isTiShi = false; // 是否提示对应牌型
        boolean check = false; // 点击的牌是否弹出状态
        A2: for (int i = 0, count = nowcard.size(); i < count; i++) {
            if (nowcard.get(i).ischeck && (mPoker.getValue() == nowcard.get(i).getValue())) {
                check = true;
                break A2;
            }
        }
        // 如果商家有出过牌并且不是单牌，并且我点击的牌处于没弹出状态
        if (bierenchupai != null && 1 != bierenchupai.length && !check) {
            checkOtherChupai(bierenchupai);
            DouDiZhuData data = new DouDiZhuData(nowcard);
            DouDiZhuData datas = new DouDiZhuData(nowcard);
            data.fillPokerList();
            List<List<Poker>> tishiList = data.getTiShi(otherplay1);
            checkOtherChupai(bierenchupai);
            datas.fillAllPokerList();
            List<List<Poker>> tishiList2 = datas.getTiShi(otherplay1);
            HintPokerUtil aList = new HintPokerUtil();
            if (tishiList != null && tishiList2 != null) {
                tishiList = aList.filterHintPoker(tishiList, tishiList2);
            }
            if (tishiList != null && tishiList.size() > 0) {
                int index = -1;
                A1: for (int i = 0, count = tishiList.size(); i < count; i++) {
                    for (int j = 0, count2 = tishiList.get(i).size(); j < count2; j++) {
                        if (tishiList.get(i).get(j).getValue() == mPoker.getValue()) {
                            index = i;
                            break A1;
                        }
                    }
                }
                if (index != -1) {
                    for (int i = 0; i < nowcard.size(); i++) {
                        poker[nowcard.get(i).getNumber()].params.topMargin = mst
                                .adjustYIgnoreDensity(20);
                        poker[nowcard.get(i).getNumber()].setLayoutParams(poker[nowcard.get(i)
                                .getNumber()].params);
                        poker[nowcard.get(i).getNumber()].ischeck = false;
                    }
                    isTiShi = true;
                    List<Poker> tiShiPoker = tishiList.get(index);
                    for (int i = 0; i < tiShiPoker.size(); i++) {
                        poker[tiShiPoker.get(i).getNumber()].params.topMargin = 0;
                        poker[tiShiPoker.get(i).getNumber()].setLayoutParams(poker[tiShiPoker
                                .get(i).getNumber()].params);
                        poker[tiShiPoker.get(i).getNumber()].ischeck = true;
                    }
                }
            }
        }
        return isTiShi;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    private long startTap = System.currentTimeMillis();
    private long endTap = 0;

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (e.getRawY() > play3Icon.getHeight()) {
            if (PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true)) {
                if (0 == endTap) {
                    endTap = System.currentTimeMillis();
                    long tapTime = endTap - startTap;
                    if (tapTime > 0 && tapTime < 200) {
                        if (jiaofenLayout.getVisibility() != View.VISIBLE) {// 双击取消选牌
                            for (int i = 0; i < nowcard.size(); i++) {
                                poker[nowcard.get(i).getNumber()].params.topMargin = mst
                                        .adjustXIgnoreDensity(20);
                                poker[nowcard.get(i).getNumber()].setLayoutParams(poker[nowcard
                                        .get(i).getNumber()].params);
                                poker[nowcard.get(i).getNumber()].ischeck = false;
                            }
                            startTap = System.currentTimeMillis();
                            endTap = 0;
                            mainGameGuideVI.setDoublePointGone(true);
                            return true;
                        }
                    } else {
                        // 单击提示牌型
                        if (playBtnLayout.getVisibility() == View.VISIBLE
                                && jiaofenLayout.getVisibility() != View.VISIBLE
                                && tilaLayout.getVisibility() != View.VISIBLE) {
                            if (mainGameGuideVI.isPoint()) {
                                mainGameGuideVI.setPointGone(true);
                            }
                            setTishi();
                            startTap = System.currentTimeMillis();
                            endTap = 0;
                            return true;
                        }
                    }
                }
                startTap = System.currentTimeMillis();
                endTap = 0;
                if (jiaofenLayout.getVisibility() == View.VISIBLE
                        && tilaLayout.getVisibility() != View.VISIBLE
                        && playBtnLayout.getVisibility() != View.VISIBLE) {
                    /** 间距 */
                    int space = jiaofenLayout.getHeight() / 4;
                    int left = jiaofenLayout.getLeft() - space;
                    int right = jiaofenLayout.getRight() + space;
                    int top = jiaofenLayout.getTop() - space;
                    int bottom = jiaofenLayout.getBottom() + space;
                    if (e.getRawX() < left || e.getRawX() > right || e.getRawY() < top
                            || e.getRawY() > bottom) {
                        jiao++;
                        jiao1 = false;
                        jiao2 = false;
                        jiao3 = false;
                        if (fen1.isClickable()) {
                            if (jiao == 1) {
                                fen1.setBackgroundDrawable(ImageUtil.getResDrawable(
                                        R.drawable.fen1, true));
                                fen2.setBackgroundResource(R.drawable.fen2_btn_bg);
                                fen3.setBackgroundResource(R.drawable.fen3_btn_bg);
                                jiao1 = true;
                            } else if (jiao == 2) {
                                fen1.setBackgroundResource(R.drawable.fen1_btn_bg);
                                fen2.setBackgroundDrawable(ImageUtil.getResDrawable(
                                        R.drawable.fen2, true));
                                fen3.setBackgroundResource(R.drawable.fen3_btn_bg);
                                jiao2 = true;
                            } else {
                                fen1.setBackgroundResource(R.drawable.fen1_btn_bg);
                                fen2.setBackgroundResource(R.drawable.fen2_btn_bg);
                                fen3.setBackgroundDrawable(ImageUtil.getResDrawable(
                                        R.drawable.fen3, true));
                                jiao = 0;
                                jiao3 = true;
                            }
                        } else if (!fen1.isClickable() && fen2.isClickable()) {
                            if (jiao == 1) {
                                fen2.setBackgroundDrawable(ImageUtil.getResDrawable(
                                        R.drawable.fen2, true));
                                fen3.setBackgroundResource(R.drawable.fen3_btn_bg);
                                jiao2 = true;
                            } else {
                                fen2.setBackgroundResource(R.drawable.fen2_btn_bg);
                                fen3.setBackgroundDrawable(ImageUtil.getResDrawable(
                                        R.drawable.fen3, true));
                                jiao = 0;
                                jiao3 = true;
                            }
                        } else if (!fen1.isClickable() && !fen2.isClickable()) {
                            fen3.setBackgroundDrawable(ImageUtil.getResDrawable(
                                    R.drawable.fen3, true));
                            jiao = 0;
                            jiao3 = true;
                        }
                    }
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int length = mst.adjustYIgnoreDensity(80);
        // 手势引导是否打开,打开才可以出发手势
        if (PreferenceHelper.getMyPreference().getSetting().getBoolean("shoushi", true)) {
            if (null == e1 || null == e2) {
                return true;
            }
            if ((e1.getY() - e2.getY() < -length)) {// 向下滑动
                // 手势提示 出牌 不出牌
                if (playBtnLayout.getVisibility() == View.VISIBLE
                        && jiaofenLayout.getVisibility() != View.VISIBLE
                        && tilaLayout.getVisibility() != View.VISIBLE
                        && buchu.getVisibility() != View.GONE) {
                    if (mainGameGuideVI.isArrowIsDown()) {// 当前提示是向下滑动的提示
                        mainGameGuideVI.setArrowDownGone(true);
                    } else {// 若不是
                        if (mainGameGuideVI.isArrowIsUp()) {
                            mainGameGuideVI.setArrowUpGone(false);
                        }
                        if (mainGameGuideVI.isPoint()) {
                            mainGameGuideVI.setPointGone(false);
                        }
                        if (mainGameGuideVI.isDoublePoint()) {
                            mainGameGuideVI.setDoublePointGone(false);
                        }
                    }
                    passCard();
                    return true;
                }
                // 手势提示 加倍，不加倍
                if (tilaLayout.getVisibility() == View.VISIBLE
                        && playBtnLayout.getVisibility() != View.VISIBLE
                        && jiaofenLayout.getVisibility() != View.VISIBLE) {
                    callBuJiaBei();
                    return true;
                }
                // 手势不叫分
                if (jiaofenLayout.getVisibility() == View.VISIBLE
                        && tilaLayout.getVisibility() != View.VISIBLE
                        && playBtnLayout.getVisibility() != View.VISIBLE) {
                    callPoint(0);
                    return true;
                }
            } else if (e1.getY() - e2.getY() > mst.adjustYIgnoreDensity(40)) {// 向上滑动
                // 手势提示 出牌 不出牌
                if (playBtnLayout.getVisibility() == View.VISIBLE
                        && jiaofenLayout.getVisibility() != View.VISIBLE
                        && tilaLayout.getVisibility() != View.VISIBLE) {
                    if (mainGameGuideVI.isArrowIsUp()) {
                        mainGameGuideVI.setArrowUpGone(true);
                    } else {
                        if (mainGameGuideVI.isArrowIsDown()) {
                            mainGameGuideVI.setArrowDownGone(false);
                        }
                        if (mainGameGuideVI.isPoint()) {
                            mainGameGuideVI.setPointGone(false);
                        }
                        if (mainGameGuideVI.isDoublePoint()) {
                            mainGameGuideVI.setDoublePointGone(false);
                        }
                    }
                    playCard(false);
                    return true;
                }
                // 手势提示 加倍，不加倍
                if (tilaLayout.getVisibility() == View.VISIBLE
                        && playBtnLayout.getVisibility() != View.VISIBLE
                        && jiaofenLayout.getVisibility() != View.VISIBLE) {
                    callJiabei();
                    return true;
                }
                // 手势叫分
                if (jiaofenLayout.getVisibility() == View.VISIBLE
                        && tilaLayout.getVisibility() != View.VISIBLE
                        && playBtnLayout.getVisibility() != View.VISIBLE) {
                    if (jiao1) {
                        callPoint(1);
                    } else if (jiao2) {
                        callPoint(2);
                        Log.i("calls", "jiao2:" + jiao2);
                    } else if (jiao3) {
                        callPoint(3);
                        Log.i("calls", ":-" + jiao2);
                    }
                    return true;
                }
            }
        }
        // 对手指滑动的距离进行了计算，如果滑动距离大于120像素，就做切换动作，否则不做任何切换动作。
        // 从左向右滑动jiaofenLayout.getVisibility()==View.VISIBLE
        if (girls != null && girls.size() > 0) {
            if (e1.getX() - e2.getX() > length) {
                // 添加动画
                if (canFlipper) {
                    canFlipper = false;
                    this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_left_in));
                    this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_left_out));
                    this.viewFlipper.showNext();
                    curPage = curPage + 1;
                    if (curPage >= girls.size()) {
                        curPage = 0;
                    }
                    readdView();
                }
                mainGameGuideVI.setArrowLeftRightGone(true);
                return true;
            }// 从右向左滑动
            else if (e1.getX() - e2.getX() < -length) {
                if (canFlipper) {
                    canFlipper = false;
                    this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_right_in));
                    this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_right_out));
                    this.viewFlipper.showPrevious();
                    curPage = curPage - 1;
                    if (curPage < 0) {
                        curPage = girls.size() - 1;
                    }
                    readdView();
                }
                mainGameGuideVI.setArrowLeftRightGone(true);
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.gestureDetector.onTouchEvent(ev);
    }

    @Override
    public void InitMainGame() {
        AudioPlayUtils.isPlay = true;
        // Database.IS_OUT = false;
        setViewInitData();
        setUserInfo();
        playOrStopBgMusic();
        gameWaitLayout.setRoomName(Database.JOIN_ROOM);
        gameWaitLayout.joinPro();
        // 加入游戏前校验
        // TaskParams params = new TaskParams();
        // params.put("homeCode", Database.JOIN_ROOM_CODE);
        // params.put("passwd", "");
        // checkJoinTask.execute(params);
        // taskManager.addTask(checkJoinTask);
        // mst.adjustView(doudizhuLayout);
    }

    /**
     * 隐藏出牌按钮
     * 
     * @Title: hiddenPlayBtn
     * @param
     * @return void
     * @throws
     */
    private void hiddenPlayBtn() {
        chupai.setVisibility(View.GONE);
        tishi.setVisibility(View.GONE);
        buchu.setVisibility(View.GONE);
        if (null != playBtnLayout) {
            playBtnLayout.setVisibility(View.GONE); // 隐藏出牌按钮
        }
    }

    /**
     * 展示出牌提示按钮
     * 
     * @param startPlay 是否自己带头出牌
     */
    private void showPlayBtn(boolean startPlay) {
        playBtnLayout.setVisibility(View.VISIBLE); // 显示出牌按钮
        chupai.setVisibility(View.VISIBLE);
        tishi.setVisibility(View.VISIBLE);
        if (tuoGuanLayout.getVisibility() != View.VISIBLE) {
            mainGameGuideVI.setPointVisible();
        }
        if (startPlay) {
            buchu.setVisibility(View.GONE);
        } else {
            buchu.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 重新加载ViewFiper前面一个和后面一个imageview的背景
     */
    private void readdView() {
        ScheduledTask.addDelayTask(new AutoTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        canFlipper = true;
                        int curId = PersonnalDoudizhuActivity.this.viewFlipper.getDisplayedChild();
                        int point = curPage - 1;
                        int postion = curPage + 1;
                        int curpoint = curId - 1;
                        int curpostion = curId + 1;
                        if (point < 0) {
                            point = girls.size() - 1;
                        }
                        if (postion > girls.size() - 1) {
                            postion = 0;
                        }
                        if (curpoint < 0) {
                            curpoint = 2;
                        }
                        if (curpostion > 2) {
                            curpostion = 0;
                        }
                        Drawable draw = ImageUtil.getcutBitmap(
                                HttpURL.URL_PIC_ALL + girls.get(point).get("path"), false);
                        if (null != draw) {
                            PersonnalDoudizhuActivity.this.viewFlipper.getChildAt(curpoint)
                                    .setBackgroundDrawable(draw);
                        }
                        Drawable draw1 = ImageUtil.getcutBitmap(
                                HttpURL.URL_PIC_ALL + girls.get(postion).get("path"), false);
                        if (null != draw1) {
                            PersonnalDoudizhuActivity.this.viewFlipper.getChildAt(curpostion)
                                    .setBackgroundDrawable(draw1);
                        }
                        Log.d("forTag", " curId : " + curId);
                        for (int i = 0; i < girls.size(); i++) {// 释放没有调用的bitmap
                            if (i == point || i == curPage || i == postion) {
                            } else {
                                ImageUtil.clearsingleCache(HttpURL.URL_PIC_ALL
                                        + girls.get(i).get("path"));
                            }
                        }
                    }
                });
            }
        }, 700);
    };

    /**
     * 新美女图鉴提示标签显示
     */
    public void setImageNewVisible(int size) {
        int count = PreferenceHelper.getMyPreference().getSetting().getInt("newImage", 0);
        if (count < size) {
            imageNewIv.setVisibility(View.GONE);
            AnimUtils.playAnim(imageNewIv, ImageUtil.getResAnimaSoft("new"), 0);
        }
    }

    /**
     * 新美女图鉴提示标签隐藏
     */
    public void setImageNewGone(int count) {
        PreferenceHelper.getMyPreference().getEditor().putInt("newImage", count).commit();
        imageNewIv.setVisibility(View.GONE);
    }

    /**
     * 初始化美女道具栏
     */
    private class GoodsValuesAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<GamePropsType> list;

        public GoodsValuesAdapter(List<GamePropsType> toolList) {
            this.list = toolList;
            this.mInflater = LayoutInflater.from(ctx);
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.girl_gif_item, null);
            RelativeLayout la = (RelativeLayout) convertView.findViewById(R.id.mainview);
            mst.adjustView(la);
            final ImageView iv = (ImageView) convertView.findViewById(R.id.goodsview);
            // iv.setBackgroundDrawable(ImageUtil.getResDrawable(gifInt[position]));
            if (list.get(position).getType().equals("1")) {
                ImageUtil.setImg(HttpURL.URL_PIC_ALL + list.get(position).getPicPath(), iv,
                        new ImageCallback() {

                            public void imageLoaded(final Bitmap bitmap, final ImageView view) {
                                // view.setImageBitmap(bitmap);
                                BitmapDrawable bd = new BitmapDrawable(bitmap);
                                view.setBackgroundDrawable(bd);
                            }
                        });
            } else if (list.get(position).getType().equals("-1")) {

                // Resources res = getResources();
                // iv.setImageBitmap(BitmapFactory.decodeResource(res,
                // R.drawable.game_items_pic));
            }
            return convertView;
        }
    }

    /**
     * 移动自己的头像
     */
    private void moveMyHead() {
        // 定时下移我的头像
        if (headTask != null) {
            headTask.stop(true);
            headTask = null;
        }
        headTask = new HeadTask();
        ScheduledTask.addRateTask(headTask, 1500);
    }

    /**
     * 设置动画
     * 
     * @param view
     * @param animId
     */
    private void setTweenAnim(View view, int animId, final int type) {
        Animation anim = AnimationUtils.loadAnimation(ctx, animId);
        view.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (type) {
                    case IS_HEAD_ANIM:
                        moveMyHead();
                        break;
                    case IS_FEIJI_ANIM:
                        feijiImageView.setVisibility(View.INVISIBLE);
                        break;
                    case IS_WANGZHA_ANIM:
                        wangzhaImageView.setVisibility(View.INVISIBLE);
                        break;
                    case IS_BAOXIANG_ANIM:
                        break;
                    case IS_ZHADAN_ANIM:
                        zhadanIv.setVisibility(View.GONE);
                        zhadanImageView.setVisibility(View.VISIBLE);
                        AnimUtils.playAnim(zhadanImageView, ImageUtil.getResAnimaSoft("bomb"), 1500);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    boolean isAdd = false;
    boolean isChose = false;// 是否弹起

    @Override
    public void onScrollListenner(int e1x, int e1y, int e2x, int e2y, int startIndex) {
    }

    @Override
    public void onTouchUpListenner(float x, float y, int startIndex) {
    }

    @Override
    public void onFling() {
        if ((playBtnLayout.getVisibility() == View.VISIBLE)) {
            setTishiGone();
            playCard(true);
            int count = myCardsTouchLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                ((Poker) myCardsTouchLayout.getChildAt(i)).setDefaultParams2();
            }
        } else {
            myCardsTouchLayout.chekCard();
        }
    }

    /**
     * 洗牌发牌
     */
    private void washCard() {
        ClientData data = new ClientData();
        users = new ArrayList<ClientUser>();
        List<String> clientNames = ClientName.getallClientNames();
        ClientUser user1 = new ClientUser();
        user1.setOrder(1);
        user1.setName(TextUtils.isEmpty(GameCache.getStr(Constant.GAME_NAME_CACHE)) ? "独孤求败"
                : GameCache.getStr(Constant.GAME_NAME_CACHE)); // 自己
        ClientUser user2 = new ClientUser();
        user2.setOrder(2);
        user2.setName(clientNames.get(0));
        ClientUser user3 = new ClientUser();
        user3.setOrder(3);
        user3.setName(clientNames.get(1));
        users.add(user1);
        users.add(user2);
        users.add(user3);
        data.setUsers(users);
        backData = Rule.issueCards(data);
        // myUserPoker = getFirstCard(backData.getUsers().get(0).getCards());
        nextUserPoker = getFirstCard(backData.getUsers().get(1).getCards());
        preUserPoker = getFirstCard(backData.getUsers().get(2).getCards());
        doTurnsCall();
    }

    /**
     * 准备叫地主
     */
    private void doTurnsCall() {
        setFirstCard(backData.getUsers().get(0).getCards());
        turnsCallOrder = backData.getMasterStart();
        CallTask callTask = new CallTask();
        ScheduledTask.addDelayTask(callTask, 2000);
    }

    /**
     * 叫地主
     */
    private void turnsCallPoint(int curOrder) {
        oneTurns += 1;// 一轮叫分里面每次叫分都+1
        turnsCallOrder = getNextCallOrder(curOrder);// 下一个叫地主的标志+1
        int ratio = 0;// 默认不叫
        if (oneTurns > 3) {
            if (calledPoint > 0) {// 叫分大于0 ，并且三个都叫了，选出地主
                for (int i = 0; i < 3; i++) {
                    if (backData.getUsers().get(i).getCallPoint() == calledPoint) {
                        Message message = new Message();
                        Grab master = new Grab();
                        master.setRatio(calledPoint);
                        master.setMasterOrder(backData.getUsers().get(i).getOrder());
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("master", master);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }
            } else {
                oneTurns = 0;
                turnsCallOrder = 0;
                washCard();
            }
        } else {
            if (curOrder == 1) {// 轮到自己叫分
                isTurnMySelf = true;
                jiaofenLayout.setVisibility(View.VISIBLE);
            } else if (curOrder == 2) {// 轮到我的下一家叫分
                isTurnMySelf = false;
                ratio = CallUtil.callPoint(nextUserPoker);
                if (ratio == 0 && oneTurns >= 2) {
                    ratio = 1;
                }
                if (ratio > calledPoint) {
                    calledPoint = ratio;
                } else {
                    ratio = 0;
                }
                backData.getUsers().get(1).setCallPoint(ratio);
            } else if (curOrder == 3) {// 轮到我的上家叫分
                isTurnMySelf = false;
                ratio = CallUtil.callPoint(preUserPoker);
                if (ratio == 0 && oneTurns >= 2) {
                    ratio = 1;
                }
                if (ratio > calledPoint) {
                    calledPoint = ratio;
                } else {
                    ratio = 0;
                }
                backData.getUsers().get(2).setCallPoint(ratio);
            }
            if (!isTurnMySelf) {
                if (calledPoint != 3) {// 不是我叫分并且不是叫3分，播放叫分动画
                    TurnCallTask callTask = new TurnCallTask(ratio);
                    ScheduledTask.addDelayTask(callTask, 2000);
                } else {// 不是我叫分，叫了3分，产生地主
                    Message message = new Message();
                    Grab master = new Grab();
                    master.setRatio(calledPoint);
                    master.setMasterOrder(curOrder);
                    message.what = 2;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("master", master);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        }
    }

    private int getNextCallOrder(int callOrder) {
        callOrder += 1;
        if (callOrder > 3) {
            callOrder = 1;
        }
        return callOrder;
    }

    /**
     * 把数组转化成pai
     * 
     * @param cards
     * @return
     */
    public List<Poker> setFirstCard(int[] listCard) {
        List<Poker> pokers = new ArrayList<Poker>();
        int[] paixu = DoudizhuRule.sort(listCard, poker);
        for (int i = 0; i < paixu.length; i++) {
            Poker card = poker[paixu[i]];
            pokers.add(card);
        }
        return pokers;
    }

    /**
     * 把牌转化成数组
     * 
     * @param cards
     * @return
     */
    public int[] setFirstCard(List<Integer> listCard) {
        pai = new int[listCard.size()];
        for (int i = 0; i < listCard.size(); i++) {
            pai[i] = listCard.get(i).intValue();
        }
        return pai;
    }

    /**
     * 把integer转化成list<Poker>
     */
    public List<Poker> getFirstCard(List<Integer> listCard) {
        List<Poker> pokers = new ArrayList<Poker>();
        int[] pai = new int[listCard.size()];
        for (int i = 0; i < listCard.size(); i++) {
            pai[i] = listCard.get(i).intValue();
        }
        paixu = DoudizhuRule.sort(pai, poker);
        for (int i = 0; i < paixu.length; i++) {
            Poker card = poker[paixu[i]];
            pokers.add(card);
        }
        return pokers;
    }

    /**
     * 轮流打牌
     */
    public void playCards(PlayAlone playAlone) {
        if (playAlone.getCount() == 0) { // 已出完牌 此牌为最后一手
            // 发送结束命令
            sendEndDialog(playAlone);
            return;
        }
        List<Poker> otherClientPokers = null;// 上手牌
        if (playAlone.getCards() != null && playAlone.getCards().size() != 0) {
            otherClientPokers = playAlone.getCards();
        } else {
            otherClientPokers = playAlone.getPrecards();
        }
        List<Poker> tiShi = null;
        PlayAlone play = new PlayAlone();
        if (curPlayOrder == 2) {// 如果当前轮到我的下一家打牌了
            logicNext.setDzorder(masterOrder);// 设置地主的order
            logicNext.setMyorder(2);// 设置下一家自己的order
            logicNext.setPreorder(prePlayOrder);// 设置相对下一家的打上手牌的order
            if (prePlayOrder == logicNext.getDzorder().intValue()) {
                if (masterOrder == 1) {// 设置地主剩余牌
                    logicNext.setdzCount(nowcard.size());
                } else {
                    logicNext.setdzCount(preUserPoker.size());
                }
            }
            if (curPlayOrder == masterOrder) {// 自己是地主 设置农民的最少牌
                if (nowcard.size() < preUserPoker.size()) {
                    logicNext.setotherCount(nowcard.size());
                } else {
                    logicNext.setotherCount(preUserPoker.size());
                }
            }
            if (curPlayOrder != masterOrder && prePlayOrder != masterOrder) {
                if (masterOrder != 1) {
                    logicNext.setotherCount(nowcard.size());
                } else {
                    logicNext.setotherCount(preUserPoker.size());
                }
            }
            if (otherClientPokers == null || otherClientPokers.size() == 0) {
                tiShi = logicNext.getTiShi(null, -1);
            } else {
                tiShi = logicNext.getTiShi(otherClientPokers, nextType.getType());
            }
            List<Poker> tishiPokers = new ArrayList<Poker>();
            if (tiShi != null && tiShi.size() != 0) {
                for (int j = 17; j > 2; j--) {
                    for (int i = 0; i < tiShi.size(); ++i) {
                        if (tiShi.get(i).getValue() == j) {
                            tishiPokers.add(tiShi.get(i));
                        }
                    }
                }
                tiShi = tishiPokers;
                nextType.setType(-1);// 重置上手打不起的牌的类型为-1
                logicNext.Play(tiShi);// 移除要打掉的牌
            } else if (tiShi == null || tiShi.size() == 0) {
                nextType.setType(com.lordcard.ui.personal.logic.DoudizhuRule
                        .checkpai(otherClientPokers));
                if (getNextCallOrder(prePlayOrder) == 2) {
                    play.setPrecards(otherClientPokers);
                }
            }
            play.setNextOrder(getNextCallOrder(curPlayOrder));
            play.setOrder(curPlayOrder);
            if (tiShi != null && tiShi.size() > 0) {
                prePlayOrder = curPlayOrder;
                nextUserPoker.removeAll(tiShi);
                logicPre.allPlayedPoker.addAll(tiShi);
            }
            play.setCards(tiShi);
            play.setCount(logicNext.getPokerNum());
        }
        if (curPlayOrder == 3) {
            logicPre.setDzorder(masterOrder);
            logicPre.setMyorder(3);
            logicPre.setPreorder(prePlayOrder);
            if (prePlayOrder == logicPre.getDzorder().intValue()) {
                if (masterOrder == 1) {
                    logicPre.setdzCount(nowcard.size());
                } else {
                    logicPre.setdzCount(preUserPoker.size());
                }
            }
            if (curPlayOrder == masterOrder) {
                if (nowcard.size() < preUserPoker.size()) {
                    logicPre.setotherCount(nowcard.size());
                } else {
                    logicPre.setotherCount(preUserPoker.size());
                }
            }
            if (curPlayOrder != masterOrder && prePlayOrder != masterOrder) {
                if (masterOrder != 1) {
                    logicPre.setotherCount(nowcard.size());
                } else {
                    logicPre.setotherCount(preUserPoker.size());
                }
            }
            if (otherClientPokers == null || otherClientPokers.size() == 0) {
                tiShi = logicPre.getTiShi(null, -1);
            } else {
                tiShi = logicPre.getTiShi(otherClientPokers, preType.getType());
            }
            List<Poker> tishiPokers = new ArrayList<Poker>();
            if (tiShi != null && tiShi.size() != 0) {
                for (int j = 17; j > 2; j--) {
                    for (int i = 0; i < tiShi.size(); ++i) {
                        if (tiShi.get(i).getValue() == j) {
                            tishiPokers.add(tiShi.get(i));
                        }
                    }
                }
                tiShi = tishiPokers;
                preType.setType(-1);
                logicPre.Play(tiShi);
                // 打印我打出的牌
            } else if (tiShi == null || tiShi.size() == 0) {
                preType.setType(com.lordcard.ui.personal.logic.DoudizhuRule
                        .checkpai(otherClientPokers));
                if (getNextCallOrder(prePlayOrder) == 3) {
                    play.setPrecards(otherClientPokers);
                }
            }
            play.setNextOrder(getNextCallOrder(curPlayOrder));
            play.setOrder(curPlayOrder);
            if (tiShi != null && tiShi.size() > 0) {
                prePlayOrder = curPlayOrder;
                preUserPoker.removeAll(tiShi);
                logicNext.allPlayedPoker.addAll(tiShi);
            }
            play.setCards(tiShi);
            play.setCount(logicPre.getPokerNum());
        }
        if (curPlayOrder == 1) {// 自己打牌了；
        } else {
            if (massageTask != null) {
                massageTask.stop(true);
                massageTask = null;
            }
            if (tiShi == null || tiShi.size() == 0) {
            } else {
                prePlayOrder = curPlayOrder;
            }
            massageTask = new MessageTask(play);
            ScheduledTask.addDelayTask(massageTask, 2000);
        }
    }

    /**
     * 打牌定时器
     * 
     * @author Administrator
     */
    class MessageTask extends AutoTask {

        private PlayAlone played;

        public MessageTask(PlayAlone played) {
            // TODO Auto-generated constructor stub
            this.played = played;
        }

        public void run() {
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putSerializable("play", played);
            if (!isFirstPlay && played.getOrder() == masterOrder) {
                isFirstPlay = true;
                firstPlaySize = 20 - played.getCards().size();
            }
            message.setData(bundle);
            handler.sendMessage(message);
            curPlayOrder = getNextCallOrder(curPlayOrder);
        }
    }

    /**
     * 叫分定时器
     * 
     * @author Administrator
     */
    class CallTask extends AutoTask {

        public void run() {
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
        }
    }

    /**
     * 叫分间隔定时器
     * 
     * @author Administrator
     */
    class TurnCallTask extends AutoTask {

        private int ratio;

        public TurnCallTask(int ratio) {
            // TODO Auto-generated constructor stub
            this.ratio = ratio;
        }

        public void run() {
            Message message = new Message();
            Grab grab = new Grab();
            grab.setNextOrder(turnsCallOrder);
            grab.setRatio(ratio);
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putSerializable("grab", grab);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    /**
     * 结算
     */
    private void sendEndDialog(PlayAlone playAlone) {
        Map<Integer, String> nickMap = new HashMap<Integer, String>();
        nickMap.put(1, users.get(0).getName());
        nickMap.put(2, users.get(1).getName());
        nickMap.put(3, users.get(2).getName());
        int allMybeans = 0;
        if (!TextUtils.isEmpty(GameCache.getStr(Constant.GAME_BEAN_CACHE))) {
            String myBeans = GameCache.getStr(Constant.GAME_BEAN_CACHE);
            allMybeans = Integer.parseInt(myBeans);
        } else {
            allMybeans = 20000;
            GameCache.putStr(Constant.GAME_BEAN_CACHE, "20000");
        }
        if (playAlone.getCount() == 0) { // 已出完牌 此牌为最后一手
            // 发送结束命令
            int bombRatio = 0;
            int myPayBeans = 0;
            boolean isDizhuWin = false;
            if (calledPoint != 0) {
                bombRatio = (Integer.parseInt(beishuNumber)) / calledPoint;
            }
            if (masterOrder == prePlayOrder) {
                isDizhuWin = true;
            }
            int springRatio = 1;
            if (isDizhuWin) {
                if (masterOrder == 1) {
                    if (logicNext.getPokerNum() == 17 && logicPre.getPokerNum() == 17) {
                        springRatio = 2;
                    }
                } else if (masterOrder == 2) {
                    if (nowcard.size() == 17 && logicPre.getPokerNum() == 17) {
                        springRatio = 2;
                    }
                } else if (masterOrder == 3) {
                    if (nowcard.size() == 17 && logicNext.getPokerNum() == 17) {
                        springRatio = 2;
                    }
                }
            } else {
                if (masterOrder == 1 && nowcard.size() == firstPlaySize) {
                    springRatio = 2;
                } else if (masterOrder == 2 && logicNext.getPokerNum() == firstPlaySize) {
                    springRatio = 2;
                } else if (masterOrder == 3 && logicPre.getPokerNum() == firstPlaySize) {
                    springRatio = 2;
                }
            }
            // 我自己的结算
            PlayAlone playMine = new PlayAlone();
            playMine.setCards(nowcard);
            playMine.setOrder(mySelfOrder);
            playMine.setRatio(Integer.parseInt(beishuNumber) * springRatio);
            playMine.setSpringRatio(springRatio);
            if (isDizhuWin) {
                if (masterOrder != mySelfOrder) {
                    if (allMybeans <= Integer.parseInt(beishuNumber) * mBaseScore * springRatio) {
                        myPayBeans = -allMybeans;
                    } else {
                        myPayBeans = -Integer.parseInt(beishuNumber) * mBaseScore * springRatio;
                    }
                    playMine.setPayment(myPayBeans);
                } else {
                    myPayBeans = Integer.parseInt(beishuNumber) * mBaseScore * 2 * springRatio;
                    playMine.setPayment(myPayBeans);
                }
            } else {
                if (masterOrder != mySelfOrder) {
                    myPayBeans = Integer.parseInt(beishuNumber) * mBaseScore * springRatio;
                    playMine.setPayment(myPayBeans);
                } else {
                    if (allMybeans <= Integer.parseInt(beishuNumber) * mBaseScore * 2) {
                        myPayBeans = -allMybeans;
                    } else {
                        myPayBeans = -Integer.parseInt(beishuNumber) * mBaseScore * 2 * springRatio;
                    }
                    playMine.setPayment(myPayBeans);
                }
            }
            playMine.setBean(allMybeans + playMine.getPayment());
            int nowBeans = allMybeans + playMine.getPayment();
            GameCache.putStr(Constant.GAME_BEAN_CACHE, nowBeans + "");
            if (playMine.getBean() < 0) {
                playMine.setBean(0);
                GameCache.putStr(Constant.GAME_BEAN_CACHE, "0");
            }
            playMine.setBaseRatio(1);
            playMine.setBombRatio(bombRatio);
            if (masterOrder != mySelfOrder) {
                playMine.setCall(false);
            } else {
                playMine.setCall(true);
            }
            playMine.setCount(nowcard.size());
            playMine.setNickMap(nickMap);
            playMine.setCallRatio(calledPoint);
            // 下家的结算
            PlayAlone playNext = new PlayAlone();
            playNext.setCards(nextUserPoker);
            playNext.setOrder(2);
            if (isDizhuWin) {
                if (masterOrder != 2) {
                    playNext.setPayment(-Integer.parseInt(beishuNumber) * mBaseScore * springRatio);
                } else {
                    playNext.setPayment(Integer.parseInt(beishuNumber) * mBaseScore * springRatio
                            - myPayBeans);
                }
            } else {
                if (masterOrder != 2) {
                    if (masterOrder != mySelfOrder) {
                        playNext.setPayment(Integer.parseInt(beishuNumber) * mBaseScore
                                * springRatio);
                    } else {
                        playNext.setPayment(-myPayBeans / 2);
                    }
                } else {
                    playNext.setPayment(-Integer.parseInt(beishuNumber) * mBaseScore * 2
                            * springRatio);
                }
            }
            playNext.setBaseRatio(1);
            playNext.setBombRatio(bombRatio);
            playNext.setSpringRatio(springRatio);
            if (masterOrder != 2) {
                playNext.setCall(false);
            } else {
                playNext.setCall(true);
            }
            playNext.setCount(logicNext.getPokerNum());
            playNext.setNickMap(nickMap);
            playNext.setCallRatio(calledPoint);
            // 上家的结算
            PlayAlone playPre = new PlayAlone();
            playPre.setCards(preUserPoker);
            playPre.setOrder(3);
            if (isDizhuWin) {
                if (masterOrder != 3) {
                    playPre.setPayment(-Integer.parseInt(beishuNumber) * mBaseScore * springRatio);
                } else {
                    playPre.setPayment(Integer.parseInt(beishuNumber) * mBaseScore * springRatio
                            - myPayBeans);
                }
            } else {
                if (masterOrder != 3) {
                    if (masterOrder != mySelfOrder) {
                        playPre.setPayment(Integer.parseInt(beishuNumber) * mBaseScore
                                * springRatio);
                    } else {
                        playPre.setPayment(-myPayBeans / 2);
                    }
                } else {
                    playPre.setPayment(-Integer.parseInt(beishuNumber) * mBaseScore * 2
                            * springRatio);
                }
            }
            playPre.setBaseRatio(1);
            playPre.setBombRatio(bombRatio);
            playNext.setSpringRatio(springRatio);
            if (masterOrder != 3) {
                playPre.setCall(false);
            } else {
                playPre.setCall(true);
            }
            playPre.setCount(logicPre.getPokerNum());
            playPre.setNickMap(nickMap);
            playPre.setCallRatio(calledPoint);
            LinkedList<PlayAlone> usersLink = new LinkedList<PlayAlone>();
            usersLink.add(playMine);
            usersLink.add(playNext);
            usersLink.add(playPre);
            Message message = new Message();
            message.what = 4;
            Bundle bundle = new Bundle();
            bundle.putSerializable("playResult", usersLink);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    /**
     * 检查金豆然后发牌开始
     */
    private void checkBeans() {
        String localBean = GameCache.getStr(Constant.GAME_BEAN_CACHE);
        if (!TextUtils.isEmpty(localBean)) {
            int allMybeans = Integer.parseInt(localBean);
            if (allMybeans <= 0) {
                if (!TextUtils.isEmpty(GameCache.getStr(Constant.GET_BEAN_CACHE))
                        && GameCache.getStr(Constant.GET_BEAN_CACHE).trim()
                                .equals(ActivityUtils.getNowDate().trim())) {
                    int getBeanCount = Integer.parseInt(GameCache.getStr(Constant.GET_BEAN_COUNT)
                            .trim());
                    if (getBeanCount < 1) {
                        getBeanCount++;
                        DialogUtils.mesToastTip("您的金豆不足，系统赠送1000金豆，每天最多一次！");
                        GameCache.putStr(Constant.GAME_BEAN_CACHE, "1000");
                        localBean = "1000";
                        GameCache.putStr(Constant.GET_BEAN_COUNT, getBeanCount + "");
                    } else {
                        // 提示不能进去了
                        // DialogUtils.mesTip(getString(R.string.link_server_fail), true);
                        GameDialog dialog = DialogUtils.mesTipDialog("您的金豆不足,赚取金豆继续玩！", true, true);
                        dialog.getOk().setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 加载积分墙
                                QSdkManager.getsdkInstance().showOffers(
                                        PersonnalDoudizhuActivity.this);

                            }
                        });
                        return;
                    }
                } else {
                    GameCache.putStr(Constant.GET_BEAN_CACHE, ActivityUtils.getNowDate());
                    DialogUtils.mesToastTip("您的金豆不足，系统赠送1000金豆，每天最多一次！");
                    GameCache.putStr(Constant.GAME_BEAN_CACHE, "1000");
                    localBean = "1000";
                    GameCache.putStr(Constant.GET_BEAN_COUNT, "1");
                }
            }
        } else {
            ActivityUtils.getNowDate();
            // GameCache.putStr(Constant.GAME_BEAN_CACHE, "20000");
        }
        beanNum.setText(localBean);
        new Thread() {

            public void run() {
                washCard();
            };
        }.start();
    }

    /**
     * 斗地主出牌声音
     */
    public static int[][] sound_single = {
            {
                    R.raw.m_1_3, R.raw.m_1_4, R.raw.m_1_5, R.raw.m_1_6,
                    R.raw.m_1_7, R.raw.m_1_8, R.raw.m_1_9, R.raw.m_1_10,
                    R.raw.m_1_11, R.raw.m_1_12, R.raw.m_1_13, R.raw.m_1_14,
                    R.raw.m_1_15, R.raw.m_1_16, R.raw.m_1_17
            },
            {
                    R.raw.w_1_3, R.raw.w_1_4, R.raw.w_1_5, R.raw.w_1_6,
                    R.raw.w_1_7, R.raw.w_1_8, R.raw.w_1_9, R.raw.w_1_10,
                    R.raw.w_1_11, R.raw.w_1_12, R.raw.w_1_13, R.raw.w_1_14,
                    R.raw.w_1_15, R.raw.w_1_16, R.raw.w_1_17
            },
    };
    public static int[][] sound_pair = {
            {
                    R.raw.m_2_3, R.raw.m_2_4, R.raw.m_2_5, R.raw.m_2_6,
                    R.raw.m_2_7, R.raw.m_2_8, R.raw.m_2_9, R.raw.m_2_10,
                    R.raw.m_2_11, R.raw.m_2_12, R.raw.m_2_13, R.raw.m_2_14,
                    R.raw.m_2_15
            },
            {
                    R.raw.w_2_3, R.raw.w_2_4, R.raw.w_2_5, R.raw.w_2_6,
                    R.raw.w_2_7, R.raw.w_2_8, R.raw.w_2_9, R.raw.w_2_10,
                    R.raw.w_2_11, R.raw.w_2_12, R.raw.w_2_13, R.raw.w_2_14,
                    R.raw.w_2_15
            },
    };

    /**
     * 设置记牌器是否可用
     */
    private void setJipaiqiAvailableOrNotAvailable() {
        if (0 == masterOrder) {
            btn_jipaiqi.setBackgroundResource(R.drawable.button_card_record_down);
        } else {
            btn_jipaiqi.setBackgroundResource(R.drawable.btn_jipaiqi_bg);
        }
    }

    /*
     * 赚取积分通知接口 只在用户体验广告获得积分之后收通知 totalpoint:此参数已废除 earnedpoint:本次任务赚取的积分数
     */
    @Override
    public void earnedPoints(int totalpoint, int earnedpoint) {
        // TODO Auto-generated method stub
        if (earnedpoint != 0) {
            int goles = earnedpoint * 1000;
            // DialogUtils.toastTip("恭喜你成功获取" + goles + "金豆");
            String currentGolds = GameCache.getStr(Constant.GAME_BEAN_CACHE);
            if (TextUtils.isEmpty(currentGolds)) {
                currentGolds = "0";
            }
            goles += Integer.valueOf(currentGolds).intValue();

            GameCache.putStr(Constant.GAME_BEAN_CACHE, String.valueOf(goles));
            beanNum.setText(String.valueOf(goles));
        }

        Log.i("---",
                "---------------------这个通知接口里面，totalpoint 已经作废-----------------------------------------");
        Log.i("getUpdatePoints", "通知接口哦:赚取积分成功, params:" + "total=" + totalpoint
                + "  earned=" + earnedpoint);

        Log.i("---",
                "-----------------此方法中totalpoint:参数已废除---------------------------------------------");
    }

    /*
     * 查询，增加，减少积分之后，这个接口收到通知 pointTotal:当前积分数
     */
    @Override
    public void getPoints(int pointTotal) {
        // TODO Auto-generated method stub
        Log.i("getUpdatePoints", "通知接口：积分初始化&更新成功,刷新后的积分为:" + pointTotal);
    }

    @Override
    public void getPointsFailed(String arg0) {
        // TODO Auto-generated method stub
        Log.i("getUpdatePointsFailed", "通知接口：积分更新失败  == " + arg0);
    }
}
