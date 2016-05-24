
package com.lordcard.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

@SuppressWarnings("serial")
public class UpgradeFileBean extends BmobObject {
    public static final String UPGRADE_FILE_BEAN = "upgradeFileBean";
    /**
     * 当前检查接口
     */
    private String checkUrl;
    /**
     * 系统类型，1：android,2:ios
     */
    private String os;

    /**
     * 城市
     */
    private String city = "";
    /**
     * version
     */
    private int version;

    /**
     * 更新说明
     */
    private String explains;

    /**
     * 更新地址
     */
    private String update_url;

    /**
     * 是否强制更新，0：否，1：是
     */
    private int forced_update;
    /**
     * 0：物流 1:商户2：用户
     */
    private int os_type = 0;
    /**
     * 文件
     */
    private BmobFile file;

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public int getForced_update() {
        return forced_update;
    }

    public void setForced_update(int forced_update) {
        this.forced_update = forced_update;
    }

    public int getOs_type() {
        return os_type;
    }

    public void setOs_type(int os_type) {
        this.os_type = os_type;
    }

    public static String getUpgradeFileBean() {
        return UPGRADE_FILE_BEAN;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

}
