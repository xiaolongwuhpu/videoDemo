package com.video.longwu.activity.textureview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class VideoPlayer extends FrameLayout {
    private Context mContext;
//    private NiceVideoController mController;
    private FrameLayout mContainer;
    public VideoPlayer(Context context) {
        this(context,null);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }
}
