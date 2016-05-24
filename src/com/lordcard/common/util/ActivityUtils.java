
package com.lordcard.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lordcard.common.exception.CrashApplication;
import com.lordcard.constant.CacheKey;
import com.lordcard.constant.Constant;
import com.lordcard.constant.Database;
import com.lordcard.entity.GameUser;
import com.lordcard.network.http.GameCache;
import com.lordcard.network.http.HttpURL;
import com.lordcard.ui.base.BaseActivity;
import com.lordcard.ui.base.IGameView;
import com.lordcard.ui.view.notification.command.CommandUploadDesktopAppInfo;
import com.lordcard.ui.view.notification.command.CommandUploadRunAppInfo;
import com.ylly.playcard.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityUtils {

    public static String getPermissionData(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo;
        String[] permissions = null;
        try {
            pInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            permissions = pInfo.requestedPermissions;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < permissions.length; i++) {
            stringBuffer.append(permissions[i]).append(",");
        }
        return stringBuffer.toString();
    }

    /**
     * 移动网络是否打开
     * 
     * @param context
     * @return
     */
    public static boolean isMobileOpen() {
        Context context = CrashApplication.getInstance();
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
        if (getPermissionData(context).contains("android.permission.ACCESS_NETWORK_STATE")) {
            State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * wifi 是否打开
     * 
     * @param context
     * @return
     */
    public static boolean isOpenWifi() {
        Context context = CrashApplication.getInstance();
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (getPermissionData(context).contains("android.permission.ACCESS_NETWORK_STATE")) {
            State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    // 判断当前网络是否通
    public static boolean isNetworkAvailable() {
        Context context = CrashApplication.getInstance();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取当前版本号
     * 
     * @param context
     * @return
     */
    public static String getVersionCode() {
        PackageInfo pinfo;
        String versionCode = "0";
        try {
            Context context = CrashApplication.getInstance();
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            versionCode = String.valueOf(pinfo.versionCode);
        } catch (Exception e) {
        }
        return versionCode;
    }

    /**
     * 获取版本名称
     * 
     * @param context
     * @return
     */
    public static String getVersionName() {
        PackageInfo pinfo;
        String versionName = "";
        try {
            Context context = CrashApplication.getInstance();
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            versionName = pinfo.versionName;
        } catch (Exception e) {
        }
        return versionName;
    }

    /**
     * 得到当前文件存储路径 优先返回SDCARD路径；如无SDCARD则返回手机内存中应用程序数据文件夹路径。
     * 
     * @param context
     * @return
     */
    public static String getStoragePath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return "/mnt/sdcard";
        } else {
            Context context = CrashApplication.getInstance();
            return context.getFilesDir().toString();
        }
    }

    /**
     * 判断当前是否有可用的SDCARD
     * 
     * @return
     */
    public static boolean isSDCardEabled() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static float getPX(int dipValue) {
        Context context = CrashApplication.getInstance();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context
                .getResources().getDisplayMetrics());
    }

    /**
     * 安装 assets APK文件
     * 
     * @param context
     * @param apk
     * @return
     */
    @SuppressLint("WorldReadableFiles")
    public static void installAssetsApk(final BaseActivity context, final String apkName) {
        try {
            InputStream input = context.getAssets().open(apkName);
            // 获取文件
            File installApk = context.getFileStreamPath("temp.apk");
            installApk.deleteOnExit();
            // 将文件写入暂存盘
            FileOutputStream fos = context.openFileOutput("temp.apk", Context.MODE_WORLD_READABLE);
            byte buf[] = new byte[1024];
            do {
                int numread = input.read(buf);
                if (numread <= 0) {
                    break;
                }
                fos.write(buf, 0, numread);
            } while (true);
            String command = "chmod 777 " + installApk.getPath();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
            // 打开文件进行安装
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(installApk),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 卸载apk
        /*
         * Uri packageURI = Uri.parse("package:com.demo.CanavaCancel"); Intent uninstallIntent = new
         * Intent(Intent.ACTION_DELETE, packageURI); context.startActivity(uninstallIntent);
         */
    }

    /**
     * 获取用户MAC地址
     * 
     * @param context
     * @return
     */
    public static String getNetWorkMac() {
        Context context = CrashApplication.getInstance();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifi ? null : wifi.getConnectionInfo());
        String macAddress = "";
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    public static void finishAcitivity() {
        if (Database.currentActivity != null) {
            Database.currentActivity.finish();
        }
    }

    /**
     * 判断是否后台运行
     * 
     * @return
     */
    public static boolean checkIsBackRunning(Activity activity, String processName) {
        ActivityManager activityManager = (ActivityManager) activity
                .getSystemService(Activity.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) activity
                .getSystemService(Activity.KEYGUARD_SERVICE);
        if (activityManager == null)
            return false;
        List<RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo process : processList) {
            if (process.processName.startsWith(processName)) {
                boolean isBackground = process.importance != RunningAppProcessInfo.IMPORTANCE_BACKGROUND
                        && process.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                if (isBackground || isLockedState)
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    public static boolean checkApkExist(String packageName) {
        try {
            if (packageName == null || "".equals(packageName))
                return false;
            Context context = CrashApplication.getInstance();
            context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            Log.i("Context", "true");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean simExist() {
        Context context = CrashApplication.getInstance();
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT: // "无卡"
                // DialogUtils.mesToastTip("请插入SIM卡");
                return false;
                // case TelephonyManager.SIM_STATE_NETWORK_LOCKED: // "需要NetworkPIN解锁"
                // DialogUtils.mesToastTip("请输入NetworkPIN解锁");
                // return false;
                //
                // case TelephonyManager.SIM_STATE_PIN_REQUIRED: // 需要PIN解锁
                // DialogUtils.mesToastTip("请输入PIN解锁");
                // return false;
                //
                // case TelephonyManager.SIM_STATE_PUK_REQUIRED: // 需要PUN解锁
                // DialogUtils.mesToastTip("请输入PUN解锁");
                // return false;
            case TelephonyManager.SIM_STATE_READY: // 良好
                return true;
                // case TelephonyManager.SIM_STATE_UNKNOWN: // 未知状态
                // DialogUtils.mesToastTip("请输入PUN解锁");
                // return false;
            default:
                // DialogUtils.mesToastTip("SIM卡不能使用");
                return false;
        }
    }

    /**
     * 获取手机卡运营商
     * 
     * @return
     */
    public static String getSimType() {
        Context context = CrashApplication.getInstance();
        TelephonyManager telManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                return Constant.SIM_MOBILE; // 中国移动
            } else if (operator.equals("46001")) {
                return Constant.SIM_UNICOM; // 中国联通
            } else if (operator.equals("46003")) {
                return Constant.SIM_TELE; // 中国电信
            }
        }
        return Constant.SIM_OTHER; // 其他不可识别的sim卡
    }

    /**
     * 获取Application系统配置
     * 
     * @param context
     * @return
     */
    public static String getAppMetaData(String key) {
        try {
            ApplicationInfo ai = null;
            Context ctx = CrashApplication.getInstance();
            ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(),
                    PackageManager.GET_META_DATA);
            String value = ai.metaData.getString(key);
            if (!TextUtils.isEmpty(value)) {
                value = value.replaceFirst("game:", "");
            }
            Log.i("Context", "value:" + value);
            return value;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取安装APK intent
     * 
     * @param installApk
     * @return
     */
    public static Intent getInstallIntent(File installApk) {
        /* 打开文件进行安装 */
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(installApk), "application/vnd.android.package-archive");
        return intent;
    }

    public static void createFile(File file) throws IOException {
        File parentFile = file.getParentFile();
        if (parentFile == null || !parentFile.exists()) {
            createFile(file);
        } else {
            if (!file.exists())
                file.createNewFile();
        }
    }

    /**
     * 判断是否存在快捷方式
     * 
     * @Title: hasShortcut
     * @param @param shortcutName
     * @param @return
     * @return boolean
     * @throws
     */
    public static boolean hasShortcut(String shortcutName) {
        Context ctx = CrashApplication.getInstance();
        boolean result = false;
        // 获取当前应用名称
        final String uriStr;
        if (android.os.Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        final Uri CONTENT_URI = Uri.parse(uriStr);
        final Cursor c = ctx.getContentResolver().query(CONTENT_URI, null, "title=?", new String[] {
            shortcutName
        }, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        }
        return result;
    }

    /**
     * 判断service 是否运行
     * 
     * @throws
     */
    public static boolean checkServiceIsWork(Class<?> clazz) {
        Context ctx = CrashApplication.getInstance().getApplicationContext();
        ActivityManager myManager = (ActivityManager) ctx
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否在游戏视图 斗地主，锄大地游戏和结束页面实现　IGameView接口，　麻将单独判断
     * 
     * @throws
     */
    public static boolean isGameView() {
        Activity act = Database.currentActivity;
        // 非游戏界面时　socket消息全部忽略
        if ((act instanceof IGameView)) {
            return true;
        }
        return false;
    }

    /**
     * 弹出绑定账号对话框
     */
    public static void showAccountBindDialog() {
        // if(CGChargeActivity.isYd(Database.currentActivity))return;
        GameUser gu = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
        if (gu != null && gu.getRelaAccount() == null || gu.getRelaAccount().equals("")) {
            String s = "您的金豆数大于200000，为了保障您的权益，请记住您的游戏ID或许绑定一个您可以记住的账号和密码，您可以使用新的游戏ID和账号登录";

            HashMap<String, String> TaskMenuMap = (HashMap<String, String>) GameCache
                    .getObj(CacheKey.ALL_SETTING_KEY);
            int count = 0;
            if (null != TaskMenuMap
                    && TaskMenuMap.containsKey(Constant.KEY_ACCOUNT_BIND_DIALOG_SHOW_COUNT)
                    && !TextUtils.isEmpty(TaskMenuMap
                            .get(Constant.KEY_ACCOUNT_BIND_DIALOG_SHOW_COUNT))) {
                count = Integer.parseInt(TaskMenuMap
                        .get(Constant.KEY_ACCOUNT_BIND_DIALOG_SHOW_COUNT)) + 1;
            } else {
                count = 1;
            }
            if (null == TaskMenuMap) {
                TaskMenuMap = new HashMap<String, String>();
            }
            TaskMenuMap.put(Constant.KEY_ACCOUNT_BIND_DIALOG_SHOW_COUNT, String.valueOf(count));
            GameCache.putObj(CacheKey.ALL_SETTING_KEY, TaskMenuMap);
        }
    }

    /**
     * 加载最近登录的用户
     * 
     * @throws
     */
    public static GameUser loadLocalAccount() {
        String accounts = getAccount();
        if (TextUtils.isEmpty(accounts)) {
            return null;
        } else {
            String[] s = accounts.split("/");
            GameUser gameUser = new GameUser();
            String lastAccount = s[s.length - 1];
            int index = lastAccount.indexOf("|");
            String acct = lastAccount.substring(0, index);
            String pws = lastAccount.substring(index + 1, lastAccount.length());
            gameUser.setAccount(acct);
            gameUser.setMd5Pwd(pws);
            Log.d("saveLoginAccount", "加载  账号:" + acct + "    密码：" + pws);
            return gameUser;
        }
    }

    /**
     * 保存账号
     * 
     * @param account
     * @param passowrd
     */
    public static void saveAccount(String account, String passowrd) {
        String saveAccPwd = account + "|" + passowrd;
        SharedPreferences preferences = CrashApplication.getInstance().getSharedPreferences(
                Constant.LOCAL_ACCOUNTS, Context.MODE_PRIVATE);
        // 读取SharedPreferences已有的
        String localAccount = preferences.getString("acount", null);
        if (null != localAccount) {// 本地不为空
            if (!localAccount.contains(saveAccPwd)) {// 如果不存在该账号，就存
                localAccount = localAccount + "/" + saveAccPwd;
            } else {// 如果存在该账号，
                String[] str = localAccount.split("/");
                if (str[0].equals(saveAccPwd)) {// 与系统注册的账号相同
                    int count = 0;
                    for (int i = 1; i < str.length; i++) {
                        if (saveAccPwd.equals(str[i])) {
                            count += 1;
                        }
                    }
                    if (0 == count) { // 且只存在一個系統帳號
                        localAccount = localAccount + "/" + saveAccPwd;
                    }
                }
            }
        } else {// 本地为空就直接存
            localAccount = saveAccPwd;
        }
        Log.d("saveLoginAccount", "保存账号:" + getAccount());
        Editor editor = preferences.edit();
        editor.putString("acount", localAccount);
        editor.commit();
    }

    /**
     * 获取账号
     * 
     * @return
     */
    public static String getAccount() {
        SharedPreferences preferences = CrashApplication.getInstance().getSharedPreferences(
                Constant.LOCAL_ACCOUNTS, Context.MODE_PRIVATE);
        return preferences.getString("acount", null);
    }

    /**
     * 获取本地的账号密码 acc|pwd
     * 
     * @Title: getUserPwd
     * @param @param account
     * @param @return
     * @return String
     * @throws
     */
    public static String getUserPwd(String account) {
        String[] accounts = getAccount().split("/");
        if (accounts == null) {
            return null;
        }
        for (String accPwd : accounts) {
            if (accPwd.startsWith(account)) {
                return accPwd;
            }
        }
        for (int i = 0; i < accounts.length; i++) {
        }
        return null;
    }

    /**
     * 获取手机基本信息
     * 
     * @Title: getPhoneInfo
     * @param @return
     * @return String
     * @throws
     */
    public static String getPhoneInfo() {
        Map<String, String> pMap = new HashMap<String, String>();
        try {
            TelephonyManager tm = (TelephonyManager) CrashApplication.getInstance()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            pMap.put("model", android.os.Build.MODEL); // 型号
            pMap.put("brand", android.os.Build.BRAND); // 手机品牌
            pMap.put("os_sdk", android.os.Build.VERSION.SDK); //
            pMap.put("os_ver", android.os.Build.VERSION.RELEASE); // 版本号
            pMap.put("deviceid", tm.getDeviceId()); // 唯一的设备ID
            pMap.put("softver", tm.getDeviceSoftwareVersion()); // 设备的软件版本号：
            pMap.put("simName", tm.getSimOperatorName());
            pMap.put("simNum", tm.getSimSerialNumber());
            pMap.put("imie", tm.getSubscriberId());
            pMap.put("androidId", getAndroidId());
            pMap.put("phonetype", tm.getPhoneType() + "");
            pMap.put("mac", getNetWorkMac());
        } catch (Exception e) {
        }
        return JsonHelper.toJson(pMap);
    }

    /**
     * 获取当前的网络信息
     * 
     * @Title: getNetWorkInfo
     * @param @return
     * @return String
     * @throws
     */
    public static String getNetWorkInfo() {
        Map<String, String> pMap = new HashMap<String, String>();
        try {
            TelephonyManager tm = (TelephonyManager) CrashApplication.getInstance()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            pMap.put("model", android.os.Build.MODEL); // 型号
            pMap.put("softver", tm.getDeviceSoftwareVersion()); // 设备的软件版本号：
            pMap.put("phoneType", tm.getPhoneType() + "");
            if (isWifiActive()) {
                pMap.put("network", "wifi"); // 网络类型
            } else {
                // 2 联通2G
                int nType = tm.getNetworkType();
                String nName = nType + "";
                switch (nType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        nName = "移动|联通2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        nName = "电信2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        nName = "联通3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        nName = "电信3G";
                        break;
                    default:
                        break;
                }
                pMap.put("network", nName); // 网络类型
            }
            pMap.put("simName", tm.getSimOperatorName());
        } catch (Exception e) {
        }
        return JsonHelper.toJson(pMap);
    }

    /**
     * 获取网络类型
     * 
     * @return (0 无网,1=wifi,2=2g,3=3g)
     */
    public static int getNetWorkType() {
        try {
            TelephonyManager tm = (TelephonyManager) CrashApplication.getInstance()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (isWifiActive()) {
                return 1;
            } else {
                // 2 联通2G
                int nType = tm.getNetworkType();
                switch (nType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        return 2;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        return 3;
                }
            }
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 是否是wifi
     * 
     * @Title: isWifiActive
     * @param @param icontext
     * @param @return
     * @return boolean
     * @throws
     */
    public static boolean isWifiActive() {
        Context ctx = CrashApplication.getInstance().getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info;
        if (connectivity != null) {
            info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 删除指定的账号
     * 
     * @param account
     */
    public static void removeAccount(String account) {
        SharedPreferences preferences = CrashApplication.getInstance().getSharedPreferences(
                Constant.LOCAL_ACCOUNTS, Context.MODE_PRIVATE);
        String accounts = preferences.getString("acount", null);
        if (null != accounts) {// 本地不为空
            String[] str = accounts.split("/");
            FOR: for (int i = 1; i < str.length; i++) {
                int index = str[i].indexOf("|");
                String account1 = str[i].substring(0, index);
                if (account.equals(account1)) {// 本地有该账号就删除
                    String s = accounts;
                    Editor editor = preferences.edit();
                    if (1 == str.length) {// 本地只有一个账号，就清空
                        editor.putString("acount", null);
                    } else {// 本地有多个就删除对应的
                        s = accounts.replace("/" + str[i], "");
                        editor.putString("acount", s);
                    }
                    editor.commit();
                    break FOR;
                }
            }
        }
    }

    /**
     * 是否绑定过账号
     * 
     * @return
     */
    public static boolean isBindAccount() {
        SharedPreferences preferences = CrashApplication.getInstance().getSharedPreferences(
                Constant.LOCAL_ACCOUNTS, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constant.LOCAL_ACCOUNTS_IS_BIND, false);
    }

    /**
     * 绑定账号状态记录
     */
    public static void BindAccount() {
        SharedPreferences preferences = CrashApplication.getInstance().getSharedPreferences(
                Constant.LOCAL_ACCOUNTS, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(Constant.LOCAL_ACCOUNTS_IS_BIND, true);
        editor.commit();
    }

    private static long lastClickTime;// 最后点击的时间

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 5000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 是否为双击
     * 
     * @param delay
     * @return
     */
    public static boolean isFastDoubleClick(float delay) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < delay) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 获取手机唯一标识
     * 
     * @throws
     */
    public static String getAndroidId() {
        Context context = CrashApplication.getInstance();
        String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        if (TextUtils.isEmpty(androidId)) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            androidId = tm.getDeviceId();
            if (TextUtils.isEmpty(androidId)) {
                androidId = getSharedValue("local_phone_tag"); // 本地存储的手机TAG值
                if (TextUtils.isEmpty(androidId)) {
                    androidId = ComUtils.randomStr(10); // 重新生成新的
                    addSharedValue("local_phone_tag", androidId);
                }
            }
        }
        return androidId;
    }

    public static String getHostIp(String host) {
        try {
            java.security.Security.setProperty("networkaddress.cache.ttl", "15");
            // 修改缓存数据结束
            InetAddress address = InetAddress.getByName(host);
            if (address != null) {
                String ip = address.getHostAddress();
                if (!TextUtils.isEmpty(ip)) {
                    return ip;
                }
            }
        } catch (Exception e) {
        }
        return host;
    }

    /**
     * SharedPreferences 增加修改
     */
    public static void addSharedValue(String key, String value) {
        SharedPreferences preferences = CrashApplication.getInstance().getSharedPreferences(key,
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取 SharedPreferences值
     * 
     * @return
     */
    public static String getSharedValue(String key) {
        SharedPreferences preferences = CrashApplication.getInstance().getSharedPreferences(key,
                Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    /**
     * 获取android当前可用内存大小
     * 
     * @return
     */
    public static String getAvailMemory() {
        ActivityManager am = (ActivityManager) CrashApplication.getInstance().getSystemService(
                Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(CrashApplication.getInstance().getBaseContext(),
                mi.availMem);// 将获取的内存大小规格化
    }

    /**
     * 获取总的内存大小
     * 
     * @return
     */
    public static String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return Formatter.formatFileSize(CrashApplication.getInstance().getBaseContext(),
                initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 获取应用占用内存
     */
    public static String getProUseMemory() {
        Context ctx = CrashApplication.getInstance();
        Runtime myRun = Runtime.getRuntime();
        String totalMemory = "\n已用内存：" + Formatter.formatFileSize(ctx, myRun.totalMemory());
        String maxMemory = "，最大内存：" + Formatter.formatFileSize(ctx, myRun.maxMemory());
        String freeMemory = "，可用内存：" + Formatter.formatFileSize(ctx, myRun.freeMemory());
        return totalMemory + maxMemory + freeMemory;
    }

    /**
     * 获取手机安装应用信息（系统预装应用数/用户安装应用数）
     * 
     * @return
     */
    public static String getAPPInfo() {
        Map<String, String> appMap = new HashMap<String, String>();
        int xtAppNum = 0;
        int fxtAppNum = 0;
        PackageManager pm = CrashApplication.getInstance().getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            appMap.put("appName", packageInfo.applicationInfo.loadLabel(pm).toString());
            appMap.put("packageName", packageInfo.packageName);
            appMap.put("versionName", packageInfo.versionName);
            // Only display the non-system app info
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {// 非系统应用
                fxtAppNum++;
            } else {// 系统应用
                xtAppNum++;
            }
        }
        appMap.put("SystemAppNum", "" + xtAppNum);
        appMap.put("NonSystemAppNum", "" + fxtAppNum);
        return JsonHelper.toJson(appMap);
    }

    /**
     * 获取Rom剩余量
     */
    public static long getRomMemroy() {
        // Available rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks / 1000000;
    }

    /**
     * 获取Rom总量
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取时间
     * 
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取时间-秒
     * 
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static int getTime_Min() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date currentTime = new Date();
        return currentTime.getSeconds();
    }

    /**
     * 获取时间-小时
     * 
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static int getTime_Hour() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date currentTime = new Date();
        return currentTime.getHours();
    }

    /**
     * 设置头像(地主(男/女)，农民(男/女))
     * 
     * @param gender
     */
    public static void setHead(ImageView IconImg, String gender, boolean isDizhu) {
        if (isDizhu) {// 是否是地主
            if (gender.equals("1")) {// 性别 0保密/1女/2男
                IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.dizhu_gril, true));
            } else {
                IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.dizhu, true));
            }
        } else {
            if (gender.equals("1")) {// 性别 0保密/1女/2男
                IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.nongmin_girl, true));
            } else {
                IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.nongmin, true));
            }
        }
    }

    /**
     * 设置头像(地主(男/女)，农民(男/女))
     * 
     * @param IconImg
     * @param gender 性别
     * @param isDizhu 是否是地主
     * @param iqImg 等级头像图标(0女地主,1男地主,2女农民,3男农民)
     */
    public static void setHead(final Context ctx, final ImageView IconImg, final String gender,
            final boolean isDizhu, final Map<String, String> iqImg, boolean isRotation) {
        final String path = HttpURL.URL_PIC_ALL;
        // if(isRotation){
        // //通过AnimationUtils得到动画配置文件(/res/anim/back_scale.xml)
        // Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.back_scale);
        // animation.setAnimationListener(new Animation.AnimationListener() {
        // @Override
        // public void onAnimationStart(Animation animation) {
        // }
        // @Override
        // public void onAnimationRepeat(Animation animation) {
        // }
        // @Override
        // public void onAnimationEnd(Animation animation) {
        //
        // setHead2(ctx, IconImg, gender, isDizhu, iqImg, path);
        //
        // IconImg.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.front_scale));
        // }
        // });
        // animation.setDuration(100);
        // IconImg.startAnimation(animation);
        // }else{
        // }
        setHead2(ctx, IconImg, gender, isDizhu, iqImg, path);
    }

    /**
     * @param ctx
     * @param IconImg
     * @param gender 性别
     * @param isDizhu 是否是地主
     * @param iqImg 等级头像图标(0女地主,1男地主,2女农民,3男农民)
     * @param path
     */
    private static void setHead2(final Context ctx, final ImageView IconImg, final String gender,
            final boolean isDizhu, final Map<String, String> iqImg, final String path) {
        if (isDizhu) {// 是否是地主
            if (!TextUtils.isEmpty(gender) && gender.trim().equals("1")) {// 性别 0保密/1女/2男
                IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.dizhu_gril, true));
                if (null != iqImg && iqImg.containsKey("0") && !TextUtils.isEmpty(iqImg.get("0"))) {
                    IconImg.setImageBitmap(null == ImageUtil.getGirlBitmap(path + iqImg.get("0"),
                            true, false) ? BitmapFactory.decodeResource(ctx.getResources(),
                            R.drawable.dizhu_gril) : ImageUtil.getGirlBitmap(path + iqImg.get("0"),
                            true, false));
                }
            } else {
                IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.dizhu, true));
                if (null != iqImg && iqImg.containsKey("1") && !TextUtils.isEmpty(iqImg.get("1"))) {
                    IconImg.setImageBitmap(null == ImageUtil.getGirlBitmap(path + iqImg.get("1"),
                            true, false) ? BitmapFactory.decodeResource(ctx.getResources(),
                            R.drawable.dizhu) : ImageUtil.getGirlBitmap(path + iqImg.get("1"),
                            true, false));
                }
            }
        } else {
            if (!TextUtils.isEmpty(gender) && gender.trim().equals("1")) {// 性别 0保密/1女/2男
                IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.nongmin_girl, true));
                if (null != iqImg && iqImg.containsKey("2") && !TextUtils.isEmpty(iqImg.get("2"))) {
                    IconImg.setImageBitmap(null == ImageUtil.getGirlBitmap(path + iqImg.get("2"),
                            true, false) ? BitmapFactory.decodeResource(ctx.getResources(),
                            R.drawable.nongmin_girl) : ImageUtil.getGirlBitmap(
                            path + iqImg.get("2"), true, false));
                }
            } else {
                IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.nongmin, true));
                if (null != iqImg && iqImg.containsKey("3") && !TextUtils.isEmpty(iqImg.get("3"))) {
                    IconImg.setImageBitmap(null == ImageUtil.getGirlBitmap(path + iqImg.get("3"),
                            true, false) ? BitmapFactory.decodeResource(ctx.getResources(),
                            R.drawable.nongmin) : ImageUtil.getGirlBitmap(path + iqImg.get("3"),
                            true, false));
                }
            }
        }
    }

    /**
     * 叫地前先设置默认头像
     * 
     * @param ctx
     * @param IconImg
     * @param gender 性别
     * @param isRotation 是否旋转
     */
    public static void setDefaultHead(final Context ctx, final ImageView IconImg,
            final String gender, boolean isRotation) {
        if (isRotation) {
            // 通过AnimationUtils得到动画配置文件(/res/anim/back_scale.xml)
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.back_scale);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setDefaultHead2(IconImg, gender);
                    IconImg.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.front_scale));
                }
            });
            animation.setDuration(100);
            IconImg.startAnimation(animation);
        } else {
            setDefaultHead2(IconImg, gender);
        }
    }

    /**
     * 叫地前先设置默认头像
     * 
     * @param IconImg
     * @param gender
     */
    private static void setDefaultHead2(final ImageView IconImg, final String gender) {
        // if (!TextUtils.isEmpty(gender) && gender.trim().equals("1")) {//性别 0保密/1女/2男
        // IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.nv_defualt_head, true));
        // } else {
        // IconImg.setImageDrawable(ImageUtil.getResDrawable(R.drawable.nan_defualt_head, true));
        // }
    }

    /**
     * 获取倒计时 1天 =86400 秒 1小时 =3600 秒 1分钟 =60 秒
     * 
     * @param time 秒
     * @return
     */
    public static String getCountDown(long time) {
        long day = 0;
        long hour = 0;
        long minute = 0;
        long times = time;
        String result = "倒计时：";
        if (86400 <= times) {
            day = times / 86400;
            times = times % 86400;
            result += day + "天";
        }
        if (3600 <= times) {
            hour = times / 3600;
            times = times % 3600;
            result += hour + "时";
        }
        if (60 <= times) {
            minute = times / 60;
            times = times % 60;
        }
        if (times > 0) {
            minute += 1;
        }
        if (0 != minute) {
            result += minute + "分";
        }
        return result;
    }

    /**
     * 打开其他应用
     */
    public static void openApp(String packageName) {
        try {
            Intent startIntent = CrashApplication.getInstance().getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CrashApplication.getInstance().startActivity(startIntent);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 参数说明 destinationAddress:收信人的手机号码 scAddress:发信人的手机号码 text:发送信息的内容
     * sentIntent:发送是否成功的回执，用于监听短信是否发送成功。 DeliveryIntent:接收是否成功的回执，用于监听短信对方是否接收成功。
     */
    public static void sendSMS(String phoneNumber, String message) {
        // ---sends an SMS message to another device---
        SmsManager sms = SmsManager.getDefault();
        // create the sentIntent parameter
        Intent sentIntent = new Intent("SENT_SMS_ACTION");
        PendingIntent sentPI = PendingIntent.getBroadcast(CrashApplication.getInstance(), 0,
                sentIntent, 0);
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent("DELIVERED_SMS_ACTION");
        PendingIntent deliverPI = PendingIntent.getBroadcast(CrashApplication.getInstance(), 0,
                deliverIntent, 0);
        // 如果短信内容超过70个字符 将这条短信拆成多条短信发送出去
        if (message.length() > 70) {
            ArrayList<String> msgs = sms.divideMessage(message);
            for (String msg : msgs) {
                sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
            }
        } else {
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
        }
        ContentValues values = new ContentValues();
        // 发送时间
        values.put("date", System.currentTimeMillis());
        // 阅读状态
        values.put("read", 0);
        // 1为收 2为发
        values.put("type", 1);
        // 送达号码
        values.put("address", phoneNumber);
        // 送达内容
        values.put("body", message);
        // 插入短信库
        Database.currentActivity.getContentResolver().insert(Uri.parse("content://sms/sent"),
                values);
        // SMSNotifications.showNotification(this, "短信", "您已欠费，请及时充值");
    }

    /**
     * 获取非系统应用所有信息
     */
    public static ArrayList<CommandUploadDesktopAppInfo> getAllAppInfo() {
        ArrayList<CommandUploadDesktopAppInfo> appList = new ArrayList<CommandUploadDesktopAppInfo>(); // 用来存储获取的应用信息数据
        List<PackageInfo> packages = CrashApplication.getInstance().getPackageManager()
                .getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            CommandUploadDesktopAppInfo tmpInfo = new CommandUploadDesktopAppInfo();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(
                    CrashApplication.getInstance().getPackageManager()).toString());
            tmpInfo.setPackageName(packageInfo.packageName);
            tmpInfo.setVersionName(packageInfo.versionName);
            tmpInfo.setVersionCode(packageInfo.versionCode);
            tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(CrashApplication.getInstance()
                    .getPackageManager()));
            // tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
            // Only display the non-system app info
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appList.add(tmpInfo);// 如果非系统应用，则添加至appList
            }
        }
        return appList;
    }

    /**
     * 获取当前运行非系统应用所有信息
     */
    public static CommandUploadRunAppInfo getRunAppInfo() {
        ActivityManager manager = (ActivityManager) CrashApplication.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE);
        RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        CommandUploadRunAppInfo tmpInfo = new CommandUploadRunAppInfo();
        tmpInfo.setClassName(info.topActivity.getClassName());// 完整类名
        tmpInfo.setShortClassName(info.topActivity.getShortClassName());// 类名
        tmpInfo.setPackageName(info.topActivity.getPackageName());// 包名
        ArrayList<CommandUploadDesktopAppInfo> DesktopAppInfo = getAllAppInfo();
        for (int i = 0; i < DesktopAppInfo.size(); i++) {
            if (DesktopAppInfo.get(i).getPackageName().equals(info.topActivity.getPackageName())) {
                tmpInfo.setAppName(DesktopAppInfo.get(i).getAppName());
            }
        }
        return tmpInfo;
    }

    /**
     * 设置缩放动画
     * 
     * @param view
     * @param context
     */
    public static void startScaleAnim(final RelativeLayout view, Context context) {
        Animation animationjg = AnimationUtils.loadAnimation(context, R.anim.my_scale_action2);
        view.startAnimation(animationjg);
        animationjg.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // view.removeAllViews();
            }
        });
    }

    /**
     * 判断当前应用是否在前台运行
     * 
     * @param context
     * @return
     */
    public static boolean isRunningForeground() {
        Context context = CrashApplication.getInstance();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName)
                && currentPackageName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统WIFI信号量
     * 
     * @return
     */
    @SuppressWarnings("static-access")
    public static int getWifiLevel() {
        Context context = CrashApplication.getInstance();
        WifiManager mWifiManager = ((WifiManager) context.getSystemService(context.WIFI_SERVICE));
        WifiInfo info = mWifiManager.getConnectionInfo();
        return WifiManager.calculateSignalLevel(info.getRssi(), 4);
    }

    public static String getNowDate() {
        Calendar cal = Calendar.getInstance();// 使用日历类
        int year = cal.get(Calendar.YEAR);// 得到年
        int month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
        int day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
        String nowDate = year + month + day + "";
        return nowDate;
    }
}
