/**
 * ExceptionVO.java [v 1.0.0]
 * classes : common.exception.ExceptionVO
 * auth : yinhongbiao
 * time : 2012 2012-12-15 下午12:25:51
 */

package com.lordcard.common.exception;

import cn.bmob.v3.BmobObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * common.exception.ExceptionVO
 * 
 * @author Administrator <br/>
 *         create at 2012 2012-12-15 下午12:25:51
 */
public class ExceptionVO extends BmobObject {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String time; // 异常时间
    private String apkVName; // 异常apk版本名称
    private String apkVCode; // 异常apk版本号
    private String phoneInfo; // 异常的手机硬件信息
    private String cause; // 异常信息

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo(String phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getApkVName() {
        return apkVName;
    }

    public void setApkVName(String apkVName) {
        this.apkVName = apkVName;
    }

    public String getApkVCode() {
        return apkVCode;
    }

    public void setApkVCode(String apkVCode) {
        this.apkVCode = apkVCode;
    }

}
