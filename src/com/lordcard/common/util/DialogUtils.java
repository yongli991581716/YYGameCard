
package com.lordcard.common.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lordcard.common.exception.CrashApplication;
import com.lordcard.constant.CacheKey;
import com.lordcard.constant.Database;
import com.lordcard.entity.GameUser;
import com.lordcard.network.cmdmgr.ClientCmdMgr;
import com.lordcard.network.cmdmgr.CmdUtils;
import com.lordcard.network.http.GameCache;
import com.lordcard.ui.GameHomeActivity;
import com.lordcard.ui.personal.PersonnalDoudizhuActivity;
import com.lordcard.ui.view.dialog.GameDialog;
import com.lordcard.ui.view.dialog.SingleDialog;
import com.ylly.playcard.R;

public class DialogUtils {

    public static GameDialog gameDialog = null;

    /** Toast提示消息 */
    public static void toastTip(String msg) {
        toastTip(msg, Toast.LENGTH_SHORT, 0);
    }

    // /** Toast提示消息 */
    // public static void toastTip(String msg,int showTime) {
    // toastTip(msg, showTime,0);
    // }
    /** 提示居中土司 */
    public static void mesToastTip(final String msg) {
        toastTip(msg, 0, Gravity.CENTER);
    }

    /**
     * Toast提示消息
     * 
     * @Title: toastTip
     * @param @param msg 提示的消息
     * @param @param showTime 显示的时间 小于0默认时间
     * @param @param align 垂直位置 0 默认位置
     * @return void
     * @throws
     */
    public static void toastTip(final String msg, final int showTime, final int align) {
        try {
            Database.currentActivity.runOnUiThread(new Runnable() {

                public void run() {
                    MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();
                    View toastRoot = Database.currentActivity.getLayoutInflater().inflate(
                            R.layout.my_text_toast, null);
                    toastRoot.getBackground().setAlpha(85);// 0~255透明度值
                    Toast toast = new Toast(CrashApplication.getInstance());
                    if (showTime > 0) {
                        toast.setDuration(showTime);
                    }
                    mst.adjustView(toastRoot);
                    toast.setView(toastRoot);
                    TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
                    mst.adjustView(tv);
                    if (!TextUtils.isEmpty(msg)) {
                        tv.setText(Html.fromHtml(msg));
                        if (align != 0) {
                            toast.setGravity(align, 0, 0);
                        }
                        toast.show();
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    /** 提示对话框 */
    public static void mesTip(final String msg, final boolean isFinish) {
        mesTip(msg, true, isFinish);
    }

    /** 提示对话框 */
    public static void mesTip(final String msg, final boolean showCancel, final boolean isFinish) {
        Database.currentActivity.runOnUiThread(new Runnable() {

            public void run() {
                try {
                    GameDialog gameDialog = new GameDialog(Database.currentActivity, showCancel) {

                        public void okClick() {
                            if (isFinish) {
                                ActivityUtils.finishAcitivity();
                            }
                        }
                    };
                    if (null != Database.currentActivity) {
                        gameDialog.show();
                        gameDialog.setText(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /** 提示对话框 */
    public static GameDialog mesTipDialog(final String msg, final boolean showCancel,final boolean isFinish) {
        Database.currentActivity.runOnUiThread(new Runnable() {

            public void run() {
                try {
                    gameDialog = new GameDialog(Database.currentActivity, showCancel) {

                        @Override
                        public void cancelClick() {
                            if (isFinish) {
                                ActivityUtils.finishAcitivity();
                            }
                        }
                    };
                    if (null != Database.currentActivity) {
                        gameDialog.show();
                        gameDialog.setText(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return gameDialog;
    }

    /** 单个确定提示 */
    public static void mesTipSingle(final String msg, final boolean isFinish) {
        Database.currentActivity.runOnUiThread(new Runnable() {

            public void run() {
                try {
                    SingleDialog gameDialog = new SingleDialog(Database.currentActivity, isFinish) {

                        public void okClick() {
                            dismiss();
                        }
                    };
                    gameDialog.show();
                    gameDialog.setText(msg);
                } catch (Exception e) {
                }
            }
        });
    }

    /** 登录失败 */
    public static void loginFail() {
        Database.currentActivity.runOnUiThread(new Runnable() {

            public void run() {
                try {
                    GameDialog gameDialog = new GameDialog(Database.currentActivity, false) {

                        public void okClick() {
                            ActivityUtils.finishAcitivity();
                        }
                    };
                    gameDialog.show();
                    gameDialog.setText("登录失败，请稍候再试！");
                } catch (Exception e) {
                }
            }
        });
    }

    /** 重新登录游戏 */
    public static void reLogin(final Activity act) {
        Database.currentActivity.runOnUiThread(new Runnable() {

            public void run() {
                try {
                    GameDialog gameDialog = new GameDialog(Database.currentActivity, false) {

                        public void okClick() {
                        }
                    };
                    gameDialog.show();
                    gameDialog.setText("用户信息过期，请重新登录！");
                } catch (Exception e) {
                }
            }
        });
    }

    public static void sentBeanTip() {
        toastTip("英雄也有落难时，我们为你准备了" + Database.SEND_BEAN + "金豆，助你东山再起。");
    }

 
    /**
     * 退出游戏
     * 
     * @param context
     * @param act
     */
    public static void exitGame(final Context context) {
        Database.currentActivity.runOnUiThread(new Runnable() {

            public void run() {
                try {
                    GameDialog gameDialog = new GameDialog(Database.currentActivity) {

                        public void okClick() {
                            CmdUtils.exitGame();
                            GameUser gu = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
                            // 记录逃跑日志
                            if (gu != null) {
                                gu.setRound(0);
                                GameCache.putObj(CacheKey.GAME_USER, gu);
                            }
                            ClientCmdMgr.closeClient();
                            ActivityUtils.finishAcitivity();
                        }

                        public void cancelClick() {
                            dismiss();
                        };
                    };
                    gameDialog.show();
                    // 超快赛
                    if (1 == Database.JOIN_ROOM.getRoomType()) {
                        gameDialog.setText("比赛还在进行，退出比赛将不退还报名费，确定退出？");
                    } else {
                        gameDialog.setText("是否返回游戏大厅？");
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 加载提示
     */
    public static ProgressDialog getWaitProgressDialog(Context context, String msg) {
        ProgressDialog dialog = new ProgressDialog(context, R.style.tsdialog);
        dialog.setMessage(msg);
        dialog.setProgress(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;// 透明度
        lp.dimAmount = 0.0f;// 黑暗度
        window.setAttributes(lp);
        return dialog;
    }

    /**
     * 充值提示
     * 
     * @param context
     * @param msg
     * @return
     */
    public static ProgressDialog getChargingProgressDialog(Context context, String msg) {
        ProgressDialog dialog = new ProgressDialog(context, R.style.tsdialog);
        dialog.setMessage(msg);
        dialog.setProgress(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;// 透明度
        lp.dimAmount = 0.0f;// 黑暗度
        window.setAttributes(lp);
        return dialog;
    }

    /**
     * 判断网络是否可用
     * 
     * @param context
     * @return
     */
    public static GameDialog getNetWorkDialog() {
        GameDialog gameDialog = new GameDialog(Database.currentActivity) {

            public void okClick() {
                // Settings.ACTION_WIRELESS_SETTINGS 移动网络(2G/3G)
                Database.currentActivity.startActivity(new Intent(Settings.ACTION_SETTINGS)); // 进入手机中的wifi网络设置界面
            }

            public void cancelClick() {
                if (!Database.currentActivity.getClass().equals(GameHomeActivity.class)) {
                    ActivityPool.exitApp();
                }
            }
        };
        gameDialog.show();
        gameDialog.setText("网络连接不上,请检查您的网络设置!");
        gameDialog.setOkText("网络设置");
        gameDialog.setOkButtonBg(R.drawable.select1_btn_bg);
        gameDialog.setCancelText("取	消");
        gameDialog.setCancelButtonBg(R.drawable.select_btn_bg);
        return gameDialog;
    }

    /** 网络缓慢提示 */
    public static void netSlowTip() {
        Database.currentActivity.runOnUiThread(new Runnable() {

            public void run() {
                try {
                    GameDialog gameDialog = new GameDialog(Database.currentActivity) {

                        public void okClick() {
                            dismiss();
                        }

                        public void cancelClick() {
                            dismiss();
                            Intent intent = new Intent();
                            intent.setClass(Database.currentActivity,
                                    PersonnalDoudizhuActivity.class);
                            Database.currentActivity.startActivity(intent);
                        }
                    };

                    String msg = Database.currentActivity.getString(R.string.link_server_fail);
                    gameDialog.show();
                    gameDialog.setText(msg);
                    gameDialog.setOkText("确   定");
                    gameDialog.setOkButtonBg(R.drawable.select_btn_bg);
                    gameDialog.setCancelText("单机游戏");
                    gameDialog.setCancelButtonBg(R.drawable.select1_btn_bg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
