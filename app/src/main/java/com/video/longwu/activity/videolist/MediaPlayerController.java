package com.video.longwu.activity.videolist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.video.longwu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MediaPlayerController extends RelativeLayout {
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.iv_replay)
    ImageView ivReplay;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.rl_play_finish)
    RelativeLayout rlPlayFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_all_time)
    TextView tvAllTime;
    @BindView(R.id.tv_use_time)
    TextView tvUseTime;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_fullscreen)
    ImageView ivFullscreen;
    @BindView(R.id.ll_play_control)
    LinearLayout llPlayControl;
    private Context mContext;

    public MediaPlayerController(Context context) {
        this(context, null);
    }

    public MediaPlayerController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaPlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = View.inflate(getContext(), R.layout.mediaplayer_controller, this);
        ButterKnife.bind(this,view);
        initViewDisplay();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                isTouching = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                iMediaPlayer.startPlay(seekBar.getProgress());
//                isTouching = false;

            }
        });
    }


    //初始化控件的显示状态
    public void initViewDisplay() {
        tvTitle.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.VISIBLE);
        ivPlay.setImageResource(R.mipmap.new_play_video);
        tvAllTime.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
        llPlayControl.setVisibility(View.GONE);
        rlPlayFinish.setVisibility(View.GONE);
        tvUseTime.setText("00:00");
        seekBar.setProgress(0);
        seekBar.setSecondaryProgress(0);
    }


    @OnClick({R.id.iv_replay, R.id.iv_share, R.id.iv_play, R.id.iv_fullscreen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_replay:
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_play:
                break;
            case R.id.iv_fullscreen:
                break;
        }
    }
}
