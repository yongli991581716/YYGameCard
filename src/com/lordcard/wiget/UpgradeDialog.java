
package com.lordcard.wiget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ylly.playcard.R;

/**
 * 升级对话框
 * 
 * @author liyong
 */
public class UpgradeDialog extends Dialog {
    public UpgradeDialog(Context context) {
        super(context);
    }

    public UpgradeDialog(Context context, int theme) {
        super(context, theme);
    }

    public static UpgradeDialog show(Context context, CharSequence message) {

        return show(context, message, true);
    }

    public static UpgradeDialog show(Context context, CharSequence message, boolean cancelable) {

        return show(context, message, cancelable, null);
    }

    public static UpgradeDialog show(Context context, CharSequence message, boolean cancelable,
            OnCancelListener cancelListener) {

        return setAttr(context, message, cancelable, cancelListener);
    }

    private static UpgradeDialog setAttr(Context context, CharSequence message, boolean cancelable,
            OnCancelListener cancelListener) {
        final UpgradeDialog dialog = new UpgradeDialog(context, R.style.dialog_basic_styles);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_update_apk);

        // 初始化

        if (!TextUtils.isEmpty(message))
        {
            TextView mTextView = (TextView) dialog.findViewById(R.id.updateTips);
            mTextView.setText(message);
        }

        Button mGiveUPBtn = (Button) dialog.findViewById(R.id.btn_dialog_cancle);
        mGiveUPBtn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        if (cancelListener != null)
            dialog.setOnCancelListener(cancelListener);

        dialog.setCancelable(cancelable);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }

    /**
     * 获取Title
     * 
     * @return
     */
    public TextView getTitleTextView()
    {
        return (TextView) this.findViewById(R.id.dialogTile);
    }

    /**
     * 获取进度条
     * 
     * @return
     */
    public View getProgressView()
    {
        return (View) this.findViewById(R.id.llv_upgrade_apk_progress);
    }

    /**
     * 获取进度条
     * 
     * @return
     */
    public ProgressBar getProgressBar()
    {
        return (ProgressBar) this.findViewById(R.id.update_progress);
    }

    /**
     * 获取进度
     * 
     * @return
     */
    public TextView getProgressBarTextView()
    {
        return (TextView) this.findViewById(R.id.update_progress_text);
    }

    /**
     * 获取
     * 
     * @return
     */
    public TextView getTextView()
    {
        return (TextView) this.findViewById(R.id.updateTips);
    }

    /**
     * 获取放弃按钮控件
     * 
     * @return
     */
    public Button getGiveUpButton()
    {
        return (Button) this.findViewById(R.id.btn_dialog_cancle);
    }

    /**
     * 获取确定按钮控件
     * 
     * @return
     */
    public Button getFirmButton()
    {
        return (Button) this.findViewById(R.id.btn_dialog_confirm);
    }
}
