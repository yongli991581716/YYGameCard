
package com.lordcard.common.util;

import android.text.TextUtils;

import com.lordcard.constant.CacheKey;
import com.lordcard.constant.Constant;
import com.lordcard.entity.ChannelCfg;
import com.lordcard.network.http.GameCache;
import com.lordcard.ui.personal.logic.ConfigUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ChannelUtils {

    /**
     * 获取统计用的appkey
     * 
     * @Title: getUappKey
     * @param @return
     * @return String
     * @throws
     */
    public static String getUappKey() {
        String uappkey = ConfigUtil.getCfg("umeng_appkey");
        String channelId = GameCache.getStr(CacheKey.CHANNEL_MM_ID);
        // 对应的渠道配置ID存在
        if (!TextUtils.isEmpty(channelId)) {
            ChannelCfg cfg = (ChannelCfg) GameCache.getObj(channelId);
            if (cfg != null && !TextUtils.isEmpty(cfg.getUappkey())) {
                return cfg.getUappkey();
            }
        }
        return uappkey;
    }

    /**
     * 获取自定义的渠道名称
     * 
     * @Title: getUChannel
     * @param @return
     * @return String
     * @throws
     */
    public static String getUChannel() {
        String ucChannel = ConfigUtil.getCfg("umeng_channel");
        String channelId = GameCache.getStr(CacheKey.CHANNEL_MM_ID);
        // 对应的渠道配置ID存在
        if (!TextUtils.isEmpty(channelId)) {
            ChannelCfg cfg = (ChannelCfg) GameCache.getObj(channelId);
            if (cfg != null && !TextUtils.isEmpty(cfg.getUchannel())) {
                return cfg.getUchannel();
            }
        }
        return ucChannel;
    }

    /**
     * 服务器配置的目录名称
     * 
     * @Title: getSerCfgName
     * @param @return
     * @return String
     * @throws
     */
    public static String getSerCfgName() {
        String sercfgname = ConfigUtil.getCfg("channel_config");
        String channelId = GameCache.getStr(CacheKey.CHANNEL_MM_ID);
        // 对应的渠道配置ID存在
        if (!TextUtils.isEmpty(channelId)) {
            ChannelCfg cfg = (ChannelCfg) GameCache.getObj(channelId);
            if (cfg != null && !TextUtils.isEmpty(cfg.getSerCfgName())) {
                return cfg.getSerCfgName();
            }
        }
        return sercfgname;
    }

    /**
     * 更新包目录地址
     * 
     * @Title: getSerDir
     * @param @return
     * @return String
     * @throws
     */
    public static String getSerDir() {
        String serDir = ConfigUtil.getCfg("channel_update");
        String channelId = GameCache.getStr(CacheKey.CHANNEL_MM_ID);
        // 对应的渠道配置ID存在
        if (!TextUtils.isEmpty(channelId)) {
            ChannelCfg cfg = (ChannelCfg) GameCache.getObj(channelId);
            if (cfg != null && !TextUtils.isEmpty(cfg.getSerDir())) {
                return cfg.getSerDir();
            }
        }
        return serDir;
    }

    /**
     * 获取激活批次号
     * 
     * @Title: getBatchId
     * @param @return
     * @return String
     * @throws
     */
    public static String getBatchId() {
        String batchId = ConfigUtil.getCfg("game_batch_id");
        String channelId = GameCache.getStr(CacheKey.CHANNEL_MM_ID);
        // 对应的渠道配置ID存在
        if (!TextUtils.isEmpty(channelId)) {
            ChannelCfg cfg = (ChannelCfg) GameCache.getObj(channelId);
            if (cfg != null && !TextUtils.isEmpty(cfg.getBatchId())) {
                return cfg.getBatchId();
            }
        }
        return batchId;
    }

    /**
     * 获取MM的渠道商 ID
     * 
     * @Title: getMMChannel
     * @param @return
     * @return String
     * @throws
     */
    public static String getMMChannel() {
        // try {
        // InputStream inputStream = null;
        // Context ctx = CrashApplication.getInstance();
        // String packagePath = ctx.getPackageCodePath();
        // ZipInputStream zipInput = new ZipInputStream(new FileInputStream(packagePath));
        // ZipEntry currentZipEntry = null;
        // while ((currentZipEntry = zipInput.getNextEntry()) != null) {
        // String name = currentZipEntry.getName();
        // if (!currentZipEntry.isDirectory()) {
        // if (name.equalsIgnoreCase("mmiap.xml")) {
        // File file = new File(ctx.getFilesDir() + File.separator + name);
        // inputStream = new FileInputStream(file);
        // break;
        // }
        // }
        // }
        // zipInput.close();
        // if (inputStream == null) {
        // return null;
        // }
        // XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
        // XmlPullParser pullParser = pullParserFactory.newPullParser();
        // pullParser.setInput(inputStream, Constant.CHAR);
        // for (int j = pullParser.getEventType(); j != 1; j = pullParser.next())
        // switch (j) {
        // case 0:
        // break;
        // case 2:
        // String nodeName = pullParser.getName();
        // if ("channel".equals(nodeName)) {
        // String channel = pullParser.nextText();
        // DialogUtils.toastTip("========="+channel+"======");
        // return channel;
        // }
        // }
        // } catch (Exception exception) {}
        // return null;

        InputStream inputStream = null;
        InputStreamReader reader = null;
        BufferedReader bufReader = null;
        try {
            inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream("mmiap.xml");
            reader = new InputStreamReader(inputStream, Constant.CHAR);
            bufReader = new BufferedReader(reader);

            StringBuilder result = new StringBuilder();
            String readLink = null;
            while ((readLink = bufReader.readLine()) != null) {
                readLink = new String(readLink.getBytes("ISO-8859-1"), Constant.CHAR);
                result.append(readLink.toLowerCase());
            }
            int start = result.indexOf("<channel>");
            if (start <= 0)
                return null; // 不存在当前配置
            int end = result.indexOf("</channel>");
            if (end <= start)
                return null;

            String channel = result.substring(start + 9, end);
            return channel;
        } catch (Exception e) {
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (reader != null) {
                    reader.close();
                }

                if (bufReader != null) {
                    bufReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
