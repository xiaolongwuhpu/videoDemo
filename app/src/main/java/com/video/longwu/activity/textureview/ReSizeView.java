package com.video.longwu.activity.textureview;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.RelativeLayout;

import com.video.longwu.bean.LayoutSize;
import com.video.longwu.constant.CommonConstants;
import com.video.longwu.util.ScreenUtil;

import static com.video.longwu.constant.CommonConstants.SHOW_SCALE;

public class ReSizeView {
    /**
     * 计算包裹surface的父布局的窗口大小
     *
     * @param context
     * @param rootlayout
     * @param isLand
     */
    public static LayoutSize ResetRootLayoutSize(Activity context, boolean isLand) {
        int mScreenWidth = ScreenUtil.getWidth(context);
        int mScreenHeight = ScreenUtil.getHeight(context);
        LayoutSize layoutSize = new LayoutSize();
        if (isLand) {//横屏
            layoutSize.setHeight((int) (mScreenWidth * CommonConstants.SHOW_SCALE));
        } else {//竖屏
            layoutSize.setHeight((int) (1.0 * mScreenWidth / CommonConstants.SHOW_SCALE));
        }
        layoutSize.setWidth(mScreenWidth);
        return layoutSize;
    }

    /**
     * 计算实际播放surface窗口大小
     *
     * @param context
     * @param isLand
     */
    public static void resetSurfaceSize(Activity context, View mTextureView, MediaPlayer mediaPlayer, boolean isLand) {
        int mScreenWidth = ScreenUtil.getWidth(context);
        int mScreenHeight = ScreenUtil.getHeight(context);
        float areaWH = 0.0f;
        int height;
        if (!isLand) {
            // 竖屏16:9
            height = (int) (mScreenWidth / SHOW_SCALE);
            areaWH = SHOW_SCALE;
        } else {
            //横屏按照手机屏幕宽高计算比例
            height = mScreenHeight;
            areaWH = mScreenWidth / mScreenHeight;
        }
        int mediaWidth = mediaPlayer.getVideoWidth();
        int mediaHeight = mediaPlayer.getVideoHeight();
        float mediaWH = mediaWidth * 1.0f / mediaHeight;

        RelativeLayout.LayoutParams layoutParamsSV = null;
        if (areaWH > mediaWH) {
            //直接放会矮胖
            int svWidth = (int) (height * mediaWH);
            layoutParamsSV = new RelativeLayout.LayoutParams(svWidth, height);
            layoutParamsSV.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextureView.setLayoutParams(layoutParamsSV);
        }
        if (areaWH < mediaWH) {
            //直接放会瘦高。
            int svHeight = (int) (mScreenWidth / mediaWH);
            layoutParamsSV = new RelativeLayout.LayoutParams(mScreenWidth, svHeight);
            layoutParamsSV.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextureView.setLayoutParams(layoutParamsSV);
        }

    }


}
