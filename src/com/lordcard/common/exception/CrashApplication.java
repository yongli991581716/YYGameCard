
package com.lordcard.common.exception;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * common.exception.CrashApplication
 * 
 * @author Administrator <br/>
 *         create at 2012 2012-12-15 下午12:07:05
 */
public class CrashApplication extends Application {

    private static CrashApplication Instance;

    public static CrashApplication getInstance() {
        return Instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashExceptionHandler crashExceptionHandler = CrashExceptionHandler.getInstance();
        crashExceptionHandler.init(getApplicationContext());
        // 初始化 Bmob SDK，第一个参数为上下文，第二个参数为Application ID
        Bmob.initialize(this, "d7505902eba1ebec9124aec9f135e587");
        Instance = this;
    }

}
