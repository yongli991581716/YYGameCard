
package com.lordcard.ui;

import android.app.Activity;
import android.os.Bundle;

import com.lordcard.common.util.UpdateManager;
import com.lordcard.entity.UpgradeFileBean;
import com.ylly.playcard.R;

public class NotifyAppUpgradeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_version_upgrade);

        // 初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        initAndOpenData();
    }

    /**
     * 初始化数据
     */
    private void initAndOpenData() {
        if (getIntent()
                .getSerializableExtra("upgradeFileBean") != null) {
            UpgradeFileBean upgradeFileBean = (UpgradeFileBean) getIntent()
                    .getSerializableExtra("upgradeFileBean");
            if (upgradeFileBean != null) {
                int mark = getIntent().getIntExtra("mark", -1);
                // 0:标识通知 1：标识自动更新 2：标识手动更新
                if (mark == 0) {
                    UpdateManager.getUpdateManager().isShowUpdate(upgradeFileBean, false, false,
                            true,
                            this);
                } else if (mark == 1) {
                    UpdateManager.getUpdateManager().checkAppUpdate(this, true, upgradeFileBean,
                            false);
                } else if (mark == 2) {
                    UpdateManager.getUpdateManager().checkAppUpdate(this, false, upgradeFileBean,
                            true);
                } else {
                    finish();
                }
                return;
            }
        }
        finish();
    }
}
