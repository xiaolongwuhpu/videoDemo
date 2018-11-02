package com.video.longwu.util;

import android.app.Activity;

import com.video.longwu.widget.CustomDialogView;

public class DialogHelper {
    private CustomDialogView dialog;//进度条
    private Activity mContext;

    public DialogHelper(Activity context) {
        this.mContext = context;
    }

    /**
     * 显示进度条
     */
    public void showLoadingDialog() {
        try {
            CustomDialogView temp = getDialog(null);
            if (!mContext.isFinishing() && !temp.isShowing()) {
                temp.show();
            }
        } catch (Exception e) {
//            LogUtil.e("showLoadingDialog", e.toString());
        }
    }

    public void showLoadingDialog(String msg) {
        try {
            CustomDialogView temp = getDialog(msg);
            if (!mContext.isFinishing() && !temp.isShowing()) {
                temp.show();
            }
        } catch (Exception e) {
//            LogUtil.e("showLoadingDialog", e.toString());
        }
    }
    /**
     * 移除进度条
     */
    public void dismissLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    // dialog
    public CustomDialogView getDialog(String msg) {
        if (dialog == null) {
            dialog = CustomDialogView.showDialog(mContext, msg);
            dialog.setCancelable(true);
        }
        return dialog;
    }

}
