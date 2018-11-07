package com.video.longwu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.video.longwu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayer extends RelativeLayout {
    @BindView(R.id.textureview)
    TextureView textureview;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.image_start_stop)
    ImageView imageStartStop;
    @BindView(R.id.image_zoom)
    ImageView imageZoom;
    @BindView(R.id.text_playtime)
    TextView textPlaytime;
    @BindView(R.id.text_totletime)
    TextView textTotletime;
    @BindView(R.id.surfacelayout)
    RelativeLayout surfacelayout;

    public VideoPlayer(Context context) {
        this(context, null);

    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.activity_texture_videoplayer, this);
        ButterKnife.bind(this,view);
    }

    private void Play(String url){


    }
}
