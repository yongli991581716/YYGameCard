/**
 * BaseDialog.java [v 1.0.0]
 * classes : com.lordcard.ui.view.dialog.BaseDialog
 * auth : yinhongbiao
 * time : 2013 2013-3-25 下午5:32:42
 */

package com.lordcard.ui.view.dialog;

import com.ylly.playcard.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lordcard.common.util.MultiScreenTool;

/**
 * com.lordcard.ui.view.dialog.AlertDialog
 * 
 * @author Administrator <br/>
 *         create at 2013 2013-3-25 下午5:32:42
 */
public class GameDialog extends Dialog implements OnClickListener {

    private Context context;
    private TextView showText;
    private boolean canCancel = true; // 是否允许取消
    private MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();
    private RelativeLayout layout;
    // private boolean InterceptKeyBack=false;//是否拦截返回按钮事件
    private Button cancel, ok;

    protected GameDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public GameDialog(Context context, boolean canCancel) {
        super(context, R.style.dialog);
        this.context = context;
        this.canCancel = canCancel;
        // this.InterceptKeyBack= InterceptKeyBack;
    }

    public GameDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 重写返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_dialog);
        layout(context);
        layout = (RelativeLayout) findViewById(R.id.mm_layout);
        mst.adjustView(layout);
    }

    /**
     * 布局
     */
    private void layout(final Context context) {
        cancel = (Button) findViewById(R.id.common_cancel);
        if (!canCancel) {
            cancel.setVisibility(View.GONE);
        }
        ok = (Button) findViewById(R.id.common_ok);
        showText = (TextView) findViewById(R.id.common_text);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.common_cancel:
                    mst.unRegisterView(layout);
                    dismiss();
                    cancelClick();
                    break;
                case R.id.common_ok:
                    mst.unRegisterView(layout);
                    dismiss();
                    okClick();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void setText(String content) {
        showText.setText(content);
    }

    public void setCancelText(String content) {
        cancel.setText(content);
    }

    public void setOkText(String content) {
        ok.setText(content);
    }

    public Button getOk() {
        return ok;
    }

    public Button getCancel() {
        return cancel;
    }

    public void setOkButtonBg(int bgId) {
        ok.setBackgroundResource(bgId);
    }

    public void setCancelButtonBg(int bgId) {
        cancel.setBackgroundResource(bgId);
    }

    /** 确定 */
    public void okClick() {
    };

    /** 取消 */
    public void cancelClick() {
    };
}
