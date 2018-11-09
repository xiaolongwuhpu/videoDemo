package com.video.longwu.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

public class RollTextView extends AppCompatTextView {
    public RollTextView(Context context) {
        super(context);
    }

    public RollTextView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public RollTextView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
