package com.video.longwu.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.video.longwu.R;

/**
 * 通用加载中dialog
 */
public class CustomDialog extends Dialog {
    static CustomDialog dialog;

    public CustomDialog(Context context) {
        this(context, 0);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static void showDialog(Activity activity) {
        showDialog(activity, null);
    }

    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void showDialog(Activity activity, String msg) {
        RelativeLayout layout = (RelativeLayout) View.inflate(activity, R.layout
                .common_progress_view, null);
        TextView msgText = (TextView) layout.findViewById(R.id.tv_Msg);
        ImageView v = (ImageView) layout.findViewById(R.id.loadingView);

        if (TextUtils.isEmpty(msg)) {
            msgText.setVisibility(View.GONE);
        } else {
            msgText.setText(msg);
        }
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.loading_anim);
        v.startAnimation(animation);//开始动画

        dialog = new CustomDialog(activity, R.style.loading_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(layout,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );
        dialog.show();
    }
}
