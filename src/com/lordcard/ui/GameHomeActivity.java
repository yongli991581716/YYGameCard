
package com.lordcard.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.lordcard.common.schedule.AutoTask;
import com.lordcard.common.schedule.ScheduledTask;
import com.lordcard.common.task.GenericTask;
import com.lordcard.common.task.base.TaskParams;
import com.lordcard.common.task.base.TaskResult;
import com.lordcard.common.util.ActivityUtils;
import com.lordcard.common.util.DialogUtils;
import com.lordcard.common.util.FileUtils;
import com.lordcard.common.util.ImageUtil;
import com.lordcard.common.util.PatternUtils;
import com.lordcard.common.util.PreferenceHelper;
import com.lordcard.common.util.UpdateManager;
import com.lordcard.constant.CacheKey;
import com.lordcard.constant.Constant;
import com.lordcard.constant.Database;
import com.lordcard.entity.GameUser;
import com.lordcard.entity.NoticesVo;
import com.lordcard.entity.UpgradeFileBean;
import com.lordcard.network.http.GameCache;
import com.lordcard.ui.base.BaseActivity;
import com.lordcard.ui.personal.PersonnalDoudizhuActivity;
import com.lordcard.ui.view.dialog.GameDialog;
import com.newqm.pointwall.QEarnNotifier;
import com.newqm.pointwall.QSdkManager;
import com.newqm.sdkoffer.AdView;
import com.newqm.sdkoffer.QuMiConnect;
import com.sc.kjfs.Shortcut;
import com.sc.sdk.Connect;
import com.ylly.playcard.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint({
        "HandlerLeak", "DefaultLocale", "SimpleDateFormat", "WorldReadableFiles"
})
public class GameHomeActivity extends BaseActivity implements OnTouchListener,
        OnGestureListener, QEarnNotifier {

    private static final int WHAT_BACK_MARK = 101;
    private TextView accountTv, goldTv; // 账号，金豆
    private ImageView headIv;// 头像
    private Button loginBtn, bindAccountBtn, quickMatch, updateBtn;
    private SharedPreferences sharedPrefrences;
    // private SharedPreferences sharedViewfiper;
    private Editor editor;
    private GameDialog netWorkDialog = null;
    public static final String LOGIN_VIEW_FLIPPER = "login_view_flipper";
    // private boolean isBackState = false;//退出状态，标记当前是否准备退出的状态（点击两次退出，两次间隔时间超过10秒表示重新开始标记）
    public static final String KEY_USER = "user_key";
    /** 更新界面账号 */
    public static final int HANDLER_WHAT_LOGIN_UPDATE_USER = 1000;
    /** 注册账号 */
    public static final int HANDLER_WHAT_LOGIN_RESIGSTER_USER = 1001;
    /** 公告打开 */
    public static final int HANDLER_WHAT_LOGIN_ANNOUNCEMENT_OPEN = 1003;
    /** 公告关闭 */
    public static final int HANDLER_WHAT_LOGIN_ANNOUNCEMENT_CLOSE = 1004;
    /** 公告显示 */
    public static final int HANDLER_WHAT_LOGIN_ANNOUNCEMENT_VISIBLE = 1005;
    /** 公告隐藏 */
    public static final int HANDLER_WHAT_LOGIN_ANNOUNCEMENT_GONE = 1006;
    // 登录的时候需要的几个常量
    public final static String ACCOUNT = "account";
    public final static String PASSWORD = "userPwd";
    private RelativeLayout gameBg = null;
    private RelativeLayout katong, loginBg = null;
    // private TextView zhezhao;
    // 是否更新支付数据
    private RelativeLayout ggdetaiLayout;
    private TextView titleView, contentView, timeView, textName, textTeam;
    private static Boolean boolean1;
    private static int i;
    /** 像素增加 */
    private static int PXZ;
    /** 像素最大值 */
    private static int PX_MST;
    /** 像素最大值 */
    private static int PX_LAST_MST;
    /** 是否有公告内容 */
    private static boolean HAS_GONGGAO = false;
    private TextView t1;
    private ScrollView scrollView;
    private Button gonggao;
    private GenericTask rjoinTask;
    private GestureDetector mGestureDetector = null;
    private ViewFlipper mViewFlipper;
    private boolean isShown;
    private int[] imageId = new int[] {/*
                                        * R.drawable.login_guide1, R.drawable.login_guide2,
                                        * R.drawable.login_guide3 xs_del
                                        */};
    private AutoTask autoTask; // 定时任务
    private boolean mIsWaitPressBack;
    private AdView mAdView;
    // private ProgressDialog loginProgress;
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_BACK_MARK:
                    // 是否需要退出应用
                    mIsWaitPressBack = false;
                    break;
                case HANDLER_WHAT_LOGIN_UPDATE_USER:
                    setUserInfo();
                    break;
                case HANDLER_WHAT_LOGIN_RESIGSTER_USER:// 注册账号

                    break;
                case HANDLER_WHAT_LOGIN_ANNOUNCEMENT_OPEN:// 公告展开
                    PXZ = PXZ + PX_MST;
                    LayoutParams lp = (LayoutParams) t1.getLayoutParams();
                    lp.height = PXZ;
                    t1.setLayoutParams(lp);
                    i = i + 1;
                    break;
                case HANDLER_WHAT_LOGIN_ANNOUNCEMENT_CLOSE:// 公告收拢
                    PXZ = PXZ - PX_MST;
                    LayoutParams lp2 = (LayoutParams) t1.getLayoutParams();
                    lp2.height = PXZ;
                    t1.setLayoutParams(lp2);
                    i = i - 1;
                    break;
                case HANDLER_WHAT_LOGIN_ANNOUNCEMENT_VISIBLE:// 公告显示
                    PXZ = PX_LAST_MST;
                    i = 20;
                    if (autoTask != null) {
                        autoTask.stop(true);
                        autoTask = null;
                    }
                    scrollView.setVisibility(View.VISIBLE);
                    gonggao.setClickable(true);
                    break;
                case HANDLER_WHAT_LOGIN_ANNOUNCEMENT_GONE:// 公告隐藏
                    PXZ = 0;
                    i = 1;
                    if (autoTask != null) {
                        autoTask.stop(true);
                        autoTask = null;
                    }
                    ggdetaiLayout.setVisibility(View.GONE);
                    gonggao.setClickable(true);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置标题栏不显示
        overridePendingTransition(R.anim.fade, R.anim.hold);
        setContentView(R.layout.game_login);
        // 初始化 appid，秘钥 为 积分墙示例程序用key，请替换为你自己的应用的id秘钥
        QSdkManager.init(this, "2519762d22cd4ada", "2724787a3113ed53");
        QSdkManager.getsdkInstance(this).initOfferAd(this); // 缓存积分墙数据
        QuMiConnect.ConnectQuMi(this, "2519762d22cd4ada", "2724787a3113ed53"); // 初始化数据统计，不需要重复调用,调用一次即可
        mGestureDetector = new GestureDetector(this);
        initView();
        i = 1;
        PXZ = 0;
        PX_MST = mst.adjustYIgnoreDensity(13);// 公告每段高度
        PX_LAST_MST = PX_MST * 20;// 高度总高度
        t1 = (TextView) findViewById(R.id.t1);
        boolean1 = true; // 公告标志
        mst.adjustView(gameBg);

        // PreferenceHelper.getMyPreference().getEditor().putBoolean("jingyin", true).commit();
        if (PreferenceHelper.getMyPreference().getSetting().getBoolean("is_first", true)) {
            PreferenceHelper.getMyPreference().getEditor().putBoolean("jingyin", false).commit();
            PreferenceHelper.getMyPreference().getSetting().getBoolean("is_first", false);
        }
        // 删除更新目录下的apk文件
        deleteApkFiles();

        // 如果有网络，则检查更新
        if (ActivityUtils.isNetworkAvailable()) {

            checkUpdate();
        }

        String isFirst = GameCache.getStr(Constant.IS_FIRST);
        if (!TextUtils.isEmpty(isFirst)) {
            // 添加广告条
            if (mAdView == null) {
                mAdView = new AdView(this);
                FrameLayout.LayoutParams lp_Left_Bottom = new FrameLayout.LayoutParams(
                        1000,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                // 设置广告出现的位置，例如，悬浮于右下角(可选)
                // lp_Left_Bottom.gravity = Gravity.TOP ;
                lp_Left_Bottom.gravity = Gravity.BOTTOM;
                addContentView(mAdView, lp_Left_Bottom);
            }
        }
    }

    /**
     * 自动检查更新（每天检查一次）
     */
    private void checkUpdate() {

        // String updateTime = SharedPreferenceUtils.getString(AppConfig.APK_UPDATE_PROMPT_DATE);
        UpgradeFileBean uFileBean = new UpgradeFileBean();
        uFileBean.setOs("1");

        // 检查更新
        UpdateManager.getUpdateManager().checkAppUpdate(this, true, uFileBean,
                false);

    }

    /**
     * 删除更新目录下的apk文件
     */
    private void deleteApkFiles() {
        String apkFile = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + getProjectName() + "/Update/";
        File file = new File(apkFile);
        if (file != null && file.isDirectory()) {
            FileUtils.deleteDirectory(file);
        }

    }

    private String getProjectName() {
        String packAgeName = this.getPackageName();
        String[] names = packAgeName.split("\\.");
        int length = names.length;
        return names[length - 1];
    }

    /**
     * 启动通知界面
     * 
     * @param uFileBean
     */
    private void startNotifyActivity(UpgradeFileBean uFileBean) {
        Intent intent = new Intent(this, NotifyAppUpgradeActivity.class);
        // 0:标识通知 1：标识自动更新 2：标识手动更新
        intent.putExtra("mark", 1);
        intent.putExtra("upgradeFileBean", uFileBean);
        startActivity(intent);
    }

    // @Override
    // public void onBackPressed() {
    // NotificationManager notificationManager = (NotificationManager)
    // getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    // // 关闭升级下载通知
    // notificationManager.cancel(UpdateService.NOTIFICATION_ID);// 多次下载的
    // UpdateUtils.stopDownLoadNewVesionSev(this);
    // Intent intent = new Intent(Intent.ACTION_MAIN);
    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    // intent.addCategory(Intent.CATEGORY_HOME);
    // startActivity(intent);
    // // ActivityPool.exitApp();
    // }

    @Override
    public void onBackPressed() {
        if (mIsWaitPressBack) {
            super.onBackPressed();
        } else {
            mIsWaitPressBack = true;
            handler.sendEmptyMessageDelayed(WHAT_BACK_MARK, 2000);
            Toast.makeText(GameHomeActivity.this, "再按一次退出", 1000).show();
        }
    }

    /**
     * 初始化view控件
     */
    private void initView() {
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        mViewFlipper.setOnTouchListener(new android.view.View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        mViewFlipper.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                return;
            }
        });

        quickMatch = (Button) findViewById(R.id.game_quick_match);
        quickMatch.setOnClickListener(mOnClickListener);

        bindAccountBtn = (Button) findViewById(R.id.game_login_bind_account);
        // 移动不允许账号绑定
        bindAccountBtn.setEnabled(false);
        bindAccountBtn.setVisibility(View.INVISIBLE);
        bindAccountBtn.setText("");

        bindAccountBtn.setOnClickListener(mOnClickListener);
        accountTv = (TextView) findViewById(R.id.game_login_id);
        goldTv = (TextView) findViewById(R.id.game_login_gold);

        findViewById(R.id.do_task).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 加载积分墙
                QSdkManager.getsdkInstance().showOffers(GameHomeActivity.this);

            }
        });

        accountTv.setText("玩家"); // 账号
        // goldTv.setText("20000"); // 金豆
        headIv = (ImageView) findViewById(R.id.game_login_img);
        gameBg = (RelativeLayout) findViewById(R.id.layout);
        katong = (RelativeLayout) findViewById(R.id.katong);
        katong.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.katong, true));
        titleView = (TextView) findViewById(R.id.gg_title);
        contentView = (TextView) findViewById(R.id.gg_content);
        timeView = (TextView) findViewById(R.id.gg_time);
        textName = (TextView) findViewById(R.id.gg_name);
        textTeam = (TextView) findViewById(R.id.gg_team);
        ggdetaiLayout = (RelativeLayout) findViewById(R.id.gg_detail);
        scrollView = (ScrollView) findViewById(R.id.room_list_scrollView);
        gonggao = (Button) findViewById(R.id.gonggao);
        if (TextUtils.isEmpty(GameCache.getStr(Constant.GAME_NEW_TASK))) {
            gonggao.setVisibility(View.VISIBLE);
            Connect.ConnectShortcut(this, "2519762d22cd4ada", "2724787a3113ed53");
            gonggao.setOnClickListener(mOnClickListener);
        } else {
            gonggao.setVisibility(View.GONE);
        }

        loginBg = (RelativeLayout) findViewById(R.id.login_bg);
        // loginBg.setBackgroundDrawable(ImageUtil.getResDrawable(R.drawable.loginbj, true));
    }

    @Override
    public void onResume() {
        super.onResume();
        String localBean = "";

        String isFirst = GameCache.getStr(Constant.IS_FIRST);
        if (TextUtils.isEmpty(isFirst)) {
            GameCache.putStr(Constant.IS_FIRST, Constant.IS_FIRST);
            localBean = "3000";
            GameCache.putStr(Constant.GAME_BEAN_CACHE, localBean);

            final GameDialog dialog = DialogUtils.mesTipDialog(
                    "首次登录系统，奖励3000金豆，加油哦!", false, true);
            dialog.getOk().setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } else {
            localBean = GameCache.getStr(Constant.GAME_BEAN_CACHE);
        }

        goldTv.setText(localBean); // 金豆
    }

    /**
     * 设置用户信息
     * 
     * @param gameUser
     */
    private void setUserInfo() {
        GameUser gameUser = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
        if (gameUser != null) {
            String account = gameUser.getAccount();
            if (!TextUtils.isEmpty(gameUser.getRelaAccount())) {
                account = gameUser.getRelaAccount();
            }
            accountTv.setText(account); // 账号
            goldTv.setText(PatternUtils.changeZhidou(0 > gameUser.getBean() ? 0 : gameUser
                    .getBean())); // 金豆
        }
    }

    /**
     * 登录跳转
     * 
     * @param gameUser
     */
    private void userLogin(GameUser gameUser) {
        // Database.USER = gameUser;
        Database.ROOM_FRESH_TIME = gameUser.getRoomTime();
        Database.GAME_SERVER = gameUser.getGameServer();
        gameUser.setRound(0);
        // Database.SIGN_KEY = gameUser.getAuthKey();
        GameCache.putObj(CacheKey.GAME_USER, gameUser);
        // 保存登录过的账号到本地
        SharedPreferences sharedData = getApplication().getSharedPreferences(
                Constant.GAME_ACTIVITE, Context.MODE_PRIVATE);
        Editor editor = sharedData.edit();
        editor.putString(ACCOUNT, gameUser.getAccount());
        editor.commit();
        runOnUiThread(new Runnable() {

            public void run() {
                setUserInfo();
            }
        });
    }

    OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mViewFlipper.getVisibility() != View.VISIBLE) {
                switch (v.getId()) {
                    case R.id.gonggao:

                        final GameDialog dialog = DialogUtils.mesTipDialog(
                                "完成新手任务将获得1000金豆，即点击确定！（桌面会创建推荐应用快捷方式哦）", true, true);
                        dialog.getOk().setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                DialogUtils.mesToastTip("恭喜您获取1000金豆！");
                                dialog.dismiss();
                                // 生成快捷方式
                                gonggao.setVisibility(View.GONE);

                                // 添加金豆
                                String currentGolds = GameCache.getStr(Constant.GAME_BEAN_CACHE);
                                if (TextUtils.isEmpty(currentGolds)) {
                                    currentGolds = "0";
                                }
                                int goles = Integer.valueOf(currentGolds).intValue() + 1000;
                                GameCache.putStr(Constant.GAME_BEAN_CACHE, String.valueOf(goles));
                                goldTv.setText(String.valueOf(goles));
                                // 标记已添加快捷方式
                                GameCache.putStr(Constant.GAME_NEW_TASK, Constant.GAME_NEW_TASK);
                                Shortcut.getShortcutInstance(GameHomeActivity.this, "热门推荐",
                                        "icon_shortcut_2");
                            }
                        });

                        dialog.getCancel().setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 生成快捷方式
                                dialog.dismiss();
                            }
                        });
                        // gonggao.setClickable(false);
                        // if (boolean1 == false) {
                        // scrollView.setVisibility(View.GONE);
                        // if (autoTask != null) {
                        // autoTask.stop(true);
                        // autoTask = null;
                        // }
                        // /** 画卷收拢 */
                        // autoTask = new AutoTask() {
                        //
                        // public void run() {
                        // if (i >= 1) {
                        // handler.sendEmptyMessage(HANDLER_WHAT_LOGIN_ANNOUNCEMENT_CLOSE);
                        // } else {
                        // PXZ = 0;
                        // i = 1;
                        // stop(true);
                        // handler.sendEmptyMessage(HANDLER_WHAT_LOGIN_ANNOUNCEMENT_GONE);
                        // }
                        // }
                        // };
                        // ScheduledTask.addRateTask(autoTask, 30);
                        // boolean1 = true;
                        // } else {
                        // if (HAS_GONGGAO) {
                        // gonggao.setClickable(false);
                        // ggdetaiLayout.setVisibility(View.VISIBLE);
                        // if (autoTask != null) {
                        // autoTask.stop(true);
                        // autoTask = null;
                        // }
                        // /** 画卷展开 */
                        // autoTask = new AutoTask() {
                        //
                        // public void run() {
                        // if (i >= 0 && i <= 20) {
                        // handler.sendEmptyMessage(HANDLER_WHAT_LOGIN_ANNOUNCEMENT_OPEN);
                        // } else {
                        // PXZ = PX_LAST_MST;
                        // i = 20;
                        // handler.sendEmptyMessage(HANDLER_WHAT_LOGIN_ANNOUNCEMENT_VISIBLE);
                        // stop(true);
                        // }
                        // }
                        // };
                        // ScheduledTask.addRateTask(autoTask, 30);
                        // boolean1 = false;
                        // } else {
                        // runOnUiThread(new Runnable() {
                        //
                        // public void run() {
                        // DialogUtils.mesToastTip("亲，暂时没有公告哟~");
                        // gonggao.setClickable(true);
                        // }
                        // });
                        // }
                        // }
                        break;
                    case R.id.game_quick_match:// 单机
                        Intent intent = new Intent();
                        intent.setClass(GameHomeActivity.this, SelectBaseScoreActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 游戏公告信息
     */
    private class GameNoticeTask extends GenericTask {

        protected TaskResult _doInBackground(TaskParams... params) {
            try {
                final NoticesVo notices = (NoticesVo) GameCache.getObj(CacheKey.GAME_NOTICE);
                if (notices == null)
                    return TaskResult.FAILED;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                Date dt = format.parse(notices.getCtime());
                format = new SimpleDateFormat("yyyy-MM-dd");
                final String time1 = format.format(dt);
                final String content1 = notices.getContent();// 获取的公告内容
                sharedPrefrences = getSharedPreferences("account_ban", MODE_WORLD_READABLE);
                final String time2 = sharedPrefrences.getString("time", null);
                // final String content2 = sharedPrefrences.getString("content",
                // null);// 保存的公告内容
                runOnUiThread(new Runnable() {

                    public void run() {
                        ggdetaiLayout.setOnClickListener(null);
                        ggdetaiLayout.setVisibility(View.VISIBLE);
                        if (time1.equals(time2)) {
                            ggdetaiLayout.setVisibility(View.GONE);
                            titleView.setText(notices.getTitle());
                            contentView.setText("    " + notices.getContent());
                            timeView.setText(time1);
                            textName.setText("尊敬的玩家：");
                            textTeam.setText("YY斗地主运营团队");
                            boolean1 = true;
                        } else {
                            boolean1 = false;
                            ggdetaiLayout.setVisibility(View.VISIBLE);
                            titleView.setText(notices.getTitle());
                            contentView.setText("    " + notices.getContent());
                            timeView.setText(time1);
                            textName.setText("尊敬的玩家：");
                            textTeam.setText("YY斗地主运营团队");
                            editor = sharedPrefrences.edit();
                            editor.putString("content", content1);
                            editor.putString("time", time1);
                            editor.commit();
                            if (autoTask != null) {
                                autoTask.stop(true);
                                autoTask = null;
                            }
                            /** 画卷展开 */
                            autoTask = new AutoTask() {

                                public void run() {
                                    if (i <= 20 && i >= 0) {
                                        handler.sendEmptyMessage(HANDLER_WHAT_LOGIN_ANNOUNCEMENT_OPEN);
                                    } else {
                                        i = 20;
                                        PXZ = PX_LAST_MST;
                                        handler.sendEmptyMessage(HANDLER_WHAT_LOGIN_ANNOUNCEMENT_VISIBLE);
                                        cancel(true);
                                    }
                                }
                            };
                            ScheduledTask.addRateTask(autoTask, 2000, 30);
                        }
                        if (!TextUtils.isEmpty(notices.getTitle().trim())) {
                            HAS_GONGGAO = true;
                        }
                    }
                });
            } catch (Exception e) {
                return TaskResult.FAILED;
            }
            return TaskResult.OK;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyleDrawable();
        if (autoTask != null) {
            autoTask.stop(true);
        }
        if (rjoinTask != null) {
            rjoinTask.cancel(true);
            rjoinTask = null;
        }
    }

    public void recyleDrawable() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        layout.removeAllViews();
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("forTag", " onFling : ");
        if (!isShown) {
            if (e1.getX() - e2.getX() > 30) {// 向右滑动
                if (mViewFlipper.getDisplayedChild() == imageId.length) {
                    mViewFlipper.setVisibility(View.GONE);
                    GameCache.putStr(LOGIN_VIEW_FLIPPER, "1");
                    // Editor editor = sharedViewfiper.edit();
                    // editor.putBoolean("flipper", true);
                    // editor.commit();
                } else {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_left_out));
                    mViewFlipper.showNext();
                    if (mViewFlipper.getDisplayedChild() == imageId.length) {
                        new Thread() {

                            public void run() {
                                try {
                                    sleep(700);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                GameHomeActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        mViewFlipper.setVisibility(View.GONE);
                                    }
                                });
                            };
                        }.start();
                        GameCache.putStr(LOGIN_VIEW_FLIPPER, "1");
                        // Editor editor = sharedViewfiper.edit();
                        // editor.putBoolean("flipper", true);
                        // editor.commit();
                        handler.sendEmptyMessage(HANDLER_WHAT_LOGIN_RESIGSTER_USER);
                    }
                    return true;
                }
            } else if (e2.getX() - e1.getX() > 30) {// 向左滑动
                if (null != mViewFlipper && View.VISIBLE == mViewFlipper.getVisibility()
                        && mViewFlipper.getDisplayedChild() == 0) {
                    // if (mViewFlipper.getDisplayedChild() == 0) {
                    DialogUtils.toastTip("亲，已经是第一张了");
                } else {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_right_out));
                    mViewFlipper.showPrevious();
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        return this.mGestureDetector.onTouchEvent(event);
    }

    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // if (keyCode == KeyEvent.KEYCODE_BACK) {
    // // if (Math.abs(System.currentTimeMillis() - Constant.CLICK_TIME) >= 5000) {
    // // isBackState = false;
    // // Constant.CLICK_TIME = System.currentTimeMillis();
    // // Toast.makeText(LoginActivity.this, "再按一次退出", 1000).show();
    // // } else {
    // // isBackState = !isBackState;
    // // Constant.CLICK_TIME = System.currentTimeMillis();
    // // if (isBackState) {
    // // return super.onKeyDown(keyCode, event);
    // // } else {
    // // Toast.makeText(LoginActivity.this, "再按一次退出", 1000).show();
    // // }
    // // }
    // /*
    // * String appName = getResources().getString(R.string.app_name); AlertDialog.Builder
    // * builder = new Builder(LoginActivity.this); builder.setMessage("是否退出   <<" + appName +
    // * ">> ?"); builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
    // * @Override public void onClick(DialogInterface dialog, int which) {
    // * ActivityPool.exitApp(); } }); builder.setNegativeButton("再玩一会", new
    // * DialogInterface.OnClickListener() {
    // * @Override public void onClick(DialogInterface dialog, int which) { dialog.dismiss();
    // * } }); builder.create().show();
    // */
    // if (CGChargeActivity.isYd(this))
    // {
    // GameInterface.exit(this);
    // } else
    // {
    // this.runOnUiThread(new Runnable() {
    // @Override
    // public void run() {
    // CheckTool.exit(LoginActivity.this, new ExitCallBack() {
    // @Override
    // public void exit() {
    // Intent intent = new Intent(Intent.ACTION_MAIN);
    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    // intent.addCategory(Intent.CATEGORY_HOME);
    // startActivity(intent);
    // }
    //
    // @Override
    // public void cancel() {
    //
    // }
    // });
    // }
    // });
    // }
    //
    // }
    // return false;
    // }

    /**
     * 提示没有网络
     * 
     * @Title: showNetWorkDialog
     * @param
     * @return void
     * @throws
     */
    public void showNetWorkDialog() {
        if (netWorkDialog == null) {
            netWorkDialog = DialogUtils.getNetWorkDialog();
        } else if (!netWorkDialog.isShowing()) {
            netWorkDialog.show();
        }
    }

    /*
     * 赚取积分通知接口 只在用户体验广告获得积分之后收通知 totalpoint:此参数已废除 earnedpoint:本次任务赚取的积分数
     */
    @Override
    public void earnedPoints(int totalpoint, int earnedpoint) {
        // TODO Auto-generated method stub
        if (earnedpoint != 0) {
            int goles = earnedpoint;
            // DialogUtils.toastTip("恭喜你成功获取" + goles + "金豆");
            String currentGolds = GameCache.getStr(Constant.GAME_BEAN_CACHE);
            if (TextUtils.isEmpty(currentGolds)) {
                currentGolds = "0";
            }
            goles += Integer.valueOf(currentGolds).intValue();

            GameCache.putStr(Constant.GAME_BEAN_CACHE, String.valueOf(goles));
            goldTv.setText(String.valueOf(goles));
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
