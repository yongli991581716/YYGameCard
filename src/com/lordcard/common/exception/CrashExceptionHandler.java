/**
 * CrashExceptionHandler.java [v 1.0.0]
 * classes : common.exception.CrashExceptionHandler
 * auth : yinhongbiao
 * time : 2012 2012-12-15 下午12:00:47
 */

package com.lordcard.common.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import cn.bmob.v3.listener.SaveListener;

import com.lordcard.common.util.ActivityPool;
import com.lordcard.common.util.DateUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * common.exception.CrashExceptionHandler
 * 
 * @author Administrator <br/>
 *         create at 2012 2012-12-15 下午12:00:47
 */
public class CrashExceptionHandler implements UncaughtExceptionHandler {

    // 排除的异常信息
    private static List<String> errorList = new ArrayList<String>();
    private ExceptionVO ep;
    static {
        errorList.add("viewnotattachedtowindowmanager");
        errorList.add("atandroid.widget.gridview.onmeasure");
    }

    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler defaultHandler;
    // CrashExceptionHandler实例
    private static CrashExceptionHandler INSTANCE = new CrashExceptionHandler();
    // 程序的Context对象
    private Context ctx;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    private CrashExceptionHandler() {
    }

    /** 获取CrashExceptionHandler实例 ,单例模式 */
    public static CrashExceptionHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     * 
     * @param context
     */
    public void init(Context context) {
        ctx = context;
        // 获取系统默认的UncaughtException处理器
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashExceptionHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && defaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
                // 退出程序
                ActivityPool.exitApp();
            } catch (Exception e) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * 
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {

        if (ex == null) {
            return false;
        }
        LogUtil.err("很抱歉,程序出现异常,即将退出.", ex);
        // 收集设备参数信息
        collectDeviceInfo(ctx);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     * 
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (Exception e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
    }

    /**
     * 保存错误信息到文件中
     * 
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     */
    private void saveCrashInfo2File(Throwable ex) {
        String time = DateUtil.formatTimesTampDate(new Date());

        ep = new ExceptionVO();

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        ep.setApkVCode(infos.get("versionCode"));
        ep.setApkVName(infos.get("versionName"));
        ep.setPhoneInfo(sb.toString());
        ep.setTime(time);

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        ep.setCause(result);

        // 上传错误信息
        ep.save(ctx, new SaveListener() {

            @Override
            public void onSuccess() {
                // 成功
            }

            @Override
            public void onFailure(int code, String msg) {
                // 失败
            }
        });

    }
}
