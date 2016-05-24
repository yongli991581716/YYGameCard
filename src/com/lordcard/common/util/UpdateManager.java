
package com.lordcard.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

import com.lordcard.entity.UpgradeFileBean;
import com.lordcard.wiget.UpgradeDialog;
import com.ylly.playcard.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * 应用程序更新工具包
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.1
 * @created 2012-6-29
 */
@SuppressLint("InflateParams")
public class UpdateManager
{

    private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int CLOSE_ACTIVITY = 3;

    private static final int DIALOG_TYPE_LATEST = 0;
    private static final int DIALOG_TYPE_FAIL = 1;
    private static final int DIALOG_TYPE_MESSAGE = 2;

    /** SharedPreference的名字 */
    public static final String SP_NAME = "yy_config";
    /** 保存提示时间 */
    public static final String APK_UPDATE_PROMPT_DATE = "apk_update_prompt_date";
    private static UpdateManager mUpdateManager;

    private Activity mActivity;
    private UpgradeDialog upgradeDialog;
    // 下载对话框
    private UpgradeDialog mDownloadDialog;
    // '已经是最新' 或者 '无法获取最新版本' 的对话框
    private UpgradeDialog mLatestOrFailDialog;
    // 进度条
    private ProgressBar mProgress;
    // 显示下载数值
    private TextView mProgressText;
    // 查询动画
    private ProgressDialog mProDialog;
    // 进度值
    private int progress;
    // 下载线程
    private Thread mDownLoadThread;
    // 终止标记
    private boolean mInterceptFlag;
    private String mUpdateMsg = "";
    // 返回的安装包url
    private String mApkUrl = "";
    // 下载包保存路径
    private String mSavePath = "";
    // apk保存完整路径
    private String mApkFilePath = "";
    // 临时下载文件路径
    private String mTmpFilePath = "";
    // 下载文件大小
    private String mApkFileSize;
    // 已下载文件大小
    private String mTmpFileSize;

    private String mCurVersionName = "";
    private int mCurVersionCode;
    private UpgradeFileBean mUpdate;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {
        @SuppressLint("ShowToast")
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    mProgressText.setText(mTmpFileSize + "/" + mApkFileSize);
                    break;
                case DOWN_OVER:
                    mDownloadDialog.dismiss();
                    installApk();
                    // 关闭
                    closeActivity();
                    break;
                case DOWN_NOSDCARD:
                    mDownloadDialog.dismiss();
                    Toast.makeText(mActivity, "无法下载安装文件，请检查SD卡是否挂载", 3000)
                            .show();
                    // 关闭
                    closeActivity();
                    break;
                case CLOSE_ACTIVITY:
                    // 关闭
                    closeActivity();
                    break;
            }
        };
    };

    public static UpdateManager getUpdateManager()
    {
        if (mUpdateManager == null)
        {
            mUpdateManager = new UpdateManager();
        }
        mUpdateManager.mInterceptFlag = false;
        return mUpdateManager;
    }

    /**
     * 关闭
     */
    protected void closeActivity() {
        if (mActivity != null) {
            // 暂不关闭
            // mActivity.finish();
        }
    }

    /**
     * 检查App更新
     * 
     * @param context
     * @param isShowMsg 是否显示提示消息
     */
    public void checkAppUpdate(final Activity context, final boolean isAutoUpdate,
            final UpgradeFileBean uFileBean,
            final boolean isShowMsg)
    {
        mActivity = context;
        getCurrentVersion();
        if (isShowMsg)
        {
            if (mProDialog == null)
                mProDialog = ProgressDialog.show(mActivity, null, "正在检测，请稍后...",
                        true, true);
            else if (mProDialog.isShowing()
                    || (mLatestOrFailDialog != null && mLatestOrFailDialog
                            .isShowing()))
                return;
        }

        BmobQuery<UpgradeFileBean> query = new BmobQuery<UpgradeFileBean>();
        query.addWhereEqualTo("os", uFileBean.getOs());
        query.findObjects(mActivity, new FindListener<UpgradeFileBean>() {

            @Override
            public void onError(int arg0, String arg1) {
                if (isShowMsg)
                {
                    showLatestOrFailDialog(DIALOG_TYPE_FAIL, null);
                } else {
                    closeActivity();
                }
            }

            @Override
            public void onSuccess(List<UpgradeFileBean> arg0) {
                if (arg0 == null || arg0.isEmpty()) {

                    // 保存配置时间
                    savaConfig();
                    if (isShowMsg)
                    {
                        showLatestOrFailDialog(DIALOG_TYPE_FAIL, null);
                    } else {
                        closeActivity();
                    }
                } else {
                    // 保存配置时间
                    // savaConfig();
                    UpgradeFileBean upgradeFileBean = arg0.get(0);
                    // uFileBean.setOs(upgradeFileBean.getOs());
                    // uFileBean.setExplains(upgradeFileBean.getExplains());
                    // uFileBean.setVersion(upgradeFileBean.getVersion());
                    // uFileBean.setUpdate_url(upgradeFileBean.getUpdate_url());
                    isShowUpdate(upgradeFileBean, isAutoUpdate, isShowMsg, false, null);
                }

            }
        });
    }

    @SuppressWarnings("unused")
    private void showInfo(JSONObject dataObj) {
        String msg = dataObj.optString("msg");
        if (TextUtils.isEmpty(msg)) {
            msg = "已经是最新版本";
        }
        showLatestOrFailDialog(DIALOG_TYPE_MESSAGE, msg);
    }

    /**
     * @Title: isShowUpdate
     * @Description: 是否更新
     * @author:liyong
     * @param: @param result
     * @return: void
     * @throws
     */
    public void isShowUpdate(UpgradeFileBean result, boolean isAutoUpdate, boolean isShowMsg,
            boolean isUpdate,
            Activity context)
    {
        if (context != null) {
            mActivity = context;
        }
        // 进度条对话框不显示 - 检测结果也不显示
        if (mProDialog != null && !mProDialog.isShowing())
        {
            mProDialog = null;
            return;
        }
        // 关闭并释放释放进度条对话框
        if (isShowMsg && mProDialog != null)
        {
            mProDialog.dismiss();
            mProDialog = null;
        }
        // 显示检测结果

        mUpdate = result;
        if (mUpdate != null)
        {
            if (isUpdate || mCurVersionCode < mUpdate.getVersion())
            {
                mApkUrl = mUpdate.getUpdate_url();
                mUpdateMsg = mUpdate.getExplains();
                showNoticeDialog(context, isAutoUpdate);
            }
            else if (isShowMsg)
            {
                // 保存配置时间
                savaConfig();
                showLatestOrFailDialog(DIALOG_TYPE_LATEST, null);
            } else {
                // 保存配置时间
                savaConfig();
                closeActivity();
            }
        } else {
            // 保存配置时间
            savaConfig();
            closeActivity();
        }
    }

    /**
     * 显示'已经是最新'或者'无法获取版本信息'对话框
     */
    private void showLatestOrFailDialog(int dialogType, String msg)
    {
        if (mLatestOrFailDialog != null)
        {
            // 关闭并释放之前的对话框
            mLatestOrFailDialog.dismiss();
            mLatestOrFailDialog = null;
        }

        if (mProDialog != null)
        {
            if (mProDialog.isShowing())
            {
                mProDialog.dismiss();
            }
            mProDialog = null;
        }

        String message = "";
        if (dialogType == DIALOG_TYPE_LATEST)
        {
            message = "已经是最新版本";
        }
        else if (dialogType == DIALOG_TYPE_FAIL)
        {
            message = "无法获取版本更新信息";
        } else if (dialogType == DIALOG_TYPE_MESSAGE) {
            message = msg;
        }
        mLatestOrFailDialog = UpgradeDialog.show(mActivity, message);
        mLatestOrFailDialog.getTitleTextView().setText("系统提示");

        mLatestOrFailDialog.getGiveUpButton().setVisibility(View.GONE);
        mLatestOrFailDialog.getFirmButton().setText(R.string.firm);
        mLatestOrFailDialog.getFirmButton().setOnClickListener(
                new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLatestOrFailDialog.dismiss();
                        closeActivity();
                    }

                });
        mLatestOrFailDialog.getGiveUpButton().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mLatestOrFailDialog.dismiss();
                closeActivity();

            }
        });
        mLatestOrFailDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                closeActivity();

            }
        });

    }

    /**
     * 获取当前客户端版本信息
     */
    private void getCurrentVersion()
    {
        try
        {
            PackageInfo info = mActivity.getPackageManager().getPackageInfo(
                    mActivity.getPackageName(), 0);
            setCurVersionName(info.versionName);
            mCurVersionCode = info.versionCode;
        } catch (NameNotFoundException e)
        {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 显示版本更新通知对话框
     */
    private void showNoticeDialog(Activity context, boolean isAutoUpdate)
    {
        if (mUpdate.getForced_update() == 1)
        {

            // 保存配置时间
            savaConfig();
            upgradeDialog = UpgradeDialog.show(mActivity, mUpdateMsg, false);
            upgradeDialog.getGiveUpButton().setVisibility(View.GONE);
        } else {
            // 是否是今天第一自动更新
            boolean isShow = isTodayFirst();
            // 保存配置时间
            savaConfig();
            if (!isAutoUpdate || isShow) {
                upgradeDialog = UpgradeDialog.show(mActivity, mUpdateMsg);
            } else {
                closeActivity();
                return;
            }

        }
        upgradeDialog.getFirmButton().setOnClickListener(
                new android.view.View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        upgradeDialog.dismiss();
                        showDownloadDialog(mUpdate.getForced_update());
                    }

                });
        upgradeDialog.getGiveUpButton().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                upgradeDialog.dismiss();
                closeActivity();

            }
        });
        upgradeDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                closeActivity();

            }
        });
    }

    /**
     * 显示下载对话框
     */
    private void showDownloadDialog(int forceUpdateValue)
    {

        if (forceUpdateValue == 1) {
            // 强制更新
            mDownloadDialog = UpgradeDialog.show(mActivity, null, false);
            mDownloadDialog.getGiveUpButton().setVisibility(View.GONE);
        } else {
            mDownloadDialog = UpgradeDialog.show(mActivity, null);
            mDownloadDialog.getGiveUpButton().setOnClickListener(
                    new android.view.View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mDownloadDialog.dismiss();
                            mInterceptFlag = true;
                            closeActivity();
                        }

                    });

            mDownloadDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    closeActivity();
                }
            });
        }

        mDownloadDialog.getProgressView().setVisibility(View.VISIBLE);
        mProgress = mDownloadDialog.getProgressBar();
        mProgressText = mDownloadDialog.getProgressBarTextView();

        mDownloadDialog.getFirmButton().setVisibility(View.GONE);
        // 下载apk
        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                String apkName = getProjectName() + mUpdate.getVersion()
                        + ".apk";
                String tmpApk = getProjectName() + mUpdate.getVersion()
                        + ".tmp";
                // 判断是否挂载了SD卡
                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED))
                {
                    mSavePath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/" + getProjectName() + "/Update/";
                    File file = new File(mSavePath);
                    if (!file.exists())
                    {
                        file.mkdirs();
                    }
                    mApkFilePath = mSavePath + apkName;
                    mTmpFilePath = mSavePath + tmpApk;
                }

                // 没有挂载SD卡，无法下载文件
                if (mApkFilePath == null || mApkFilePath == "")
                {
                    mHandler.sendEmptyMessage(DOWN_NOSDCARD);
                    return;
                }

                File ApkFile = new File(mApkFilePath);

                // 是否已下载更新文件
                if (ApkFile.exists())
                {
                    mDownloadDialog.dismiss();
                    installApk();
                    return;
                }

                // 输出临时下载文件
                File tmpFile = new File(mTmpFilePath);
                FileOutputStream fos = new FileOutputStream(tmpFile);

                URL url = new URL(mApkUrl);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                // 显示文件大小格式：2个小数点显示
                DecimalFormat df = new DecimalFormat("0.00");
                // 进度条下面显示的总文件大小
                mApkFileSize = df.format((float) length / 1024 / 1024) + "MB";

                int count = 0;
                byte buf[] = new byte[1024];

                do
                {
                    int numread = is.read(buf);
                    count += numread;
                    // 进度条下面显示的当前下载文件大小
                    mTmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
                    // 当前进度值
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0)
                    {
                        // 下载完成 - 将临时下载文件转成APK文件
                        if (tmpFile.renameTo(ApkFile))
                        {
                            // 通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                        }
                        break;
                    }
                    fos.write(buf, 0, numread);
                }
                while (!mInterceptFlag);// 点击取消就停止下载

                fos.close();
                is.close();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            } finally {
                // 通知安装
                mHandler.sendEmptyMessage(CLOSE_ACTIVITY);
            }

        }
    };

    /**
     * @Title: downloadApk
     * @Description: 下载apk
     * @author:liyong
     * @param:
     * @return: void
     * @throws
     */
    private void downloadApk()
    {
        // mDownLoadThread = new Thread(mdownApkRunnable);
        // mDownLoadThread.start();

        String apkName = getProjectName() + mUpdate.getVersion()
                + ".apk";
        // 判断是否挂载了SD卡
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED))
        {
            mSavePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + getProjectName() + "/Update/";
            File file = new File(mSavePath);
            if (!file.exists())
            {
                file.mkdirs();
            }
            mApkFilePath = mSavePath + apkName;
        }

        // 没有挂载SD卡，无法下载文件
        if (mApkFilePath == null || mApkFilePath == "")
        {
            mHandler.sendEmptyMessage(DOWN_NOSDCARD);
            return;
        }

        File ApkFile = new File(mApkFilePath);

        // 是否已下载更新文件
        if (ApkFile.exists())
        {
            mDownloadDialog.dismiss();
            installApk();
            // 关闭
            closeActivity();
            return;
        }

        mUpdate.getFile().download(mActivity, ApkFile, new DownloadFileListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(String savePath) {
                // 通知安装
                mHandler.sendEmptyMessage(DOWN_OVER);
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {

                // 当前进度值
                progress = value;
                // 更新进度
                mHandler.sendEmptyMessage(DOWN_UPDATE);
            }

            @Override
            public void onFailure(int code, String msg) {
                // 通知失败
                mHandler.sendEmptyMessage(CLOSE_ACTIVITY);
            }
        });
    }

    protected String getProjectName() {
        String packAgeName = mActivity.getPackageName();
        String[] names = packAgeName.split("\\.");
        int length = names.length;
        return names[length - 1];
    }

    /**
     * 安装apk
     * 
     * @param url
     */
    private void installApk()
    {
        File apkfile = new File(mApkFilePath);
        if (!apkfile.exists())
        {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mActivity.startActivity(i);
    }

    public String getCurVersionName()
    {
        return mCurVersionName;
    }

    public void setCurVersionName(String curVersionName)
    {
        this.mCurVersionName = curVersionName;
    }

    /**
     * 是否是今天第一自动更新
     * 
     * @param context
     * @return
     */
    private boolean isTodayFirst() {
        String updateTime = getSharedPreferences().getString(APK_UPDATE_PROMPT_DATE, null);
        if (TextUtils.isEmpty(updateTime))
        {
            return true;
        } else {
            // 一天只自动检测一次
            if (!StringUtils.isCurrentDay(mActivity, updateTime))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @param context
     */
    protected void savaConfig() {
        getSharedPreferences().edit()
                .putString(APK_UPDATE_PROMPT_DATE, DateFormat
                        .getDateFormat(mActivity).format(new Date())).commit();

    }

    private SharedPreferences getSharedPreferences() {
        return mActivity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

}
