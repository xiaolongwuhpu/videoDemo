package com.video.longwu.widget;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.video.longwu.R;
import com.video.longwu.util.DensityUtils;

public class BackgroundLayout extends LinearLayout {

        private float mCornerRadius;
        private int mBackgroundColor;

    public BackgroundLayout(Context context) {
            super(context);
            init();
        }

    public BackgroundLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BackgroundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        @SuppressWarnings("deprecation")
        private void init() {
            int color = getContext().getResources().getColor(R.color.loadingbg);
            initBackground(color, mCornerRadius);
        }

        private void initBackground(int color, float cornerRadius) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setColor(color);
            drawable.setCornerRadius(cornerRadius);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(drawable);
            } else {
                setBackgroundDrawable(drawable);
            }
        }

        public void setCornerRadius(float radius) {
            mCornerRadius = DensityUtils.dp2px(radius);
            initBackground(mBackgroundColor, mCornerRadius);
        }

        public void setBaseColor(int color) {
            mBackgroundColor = color;
            initBackground(mBackgroundColor, mCornerRadius);
        }

        //正方形显示
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int size = Math.max(width, height);
            setMeasuredDimension(size, size);
        }
    }
