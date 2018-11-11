package com.video.longwu.activity.videolist;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.video.longwu.R;
import com.video.longwu.activity.videolist.view.VideoPlayer;
import com.video.longwu.util.IMediaPlayer;
import com.video.longwu.util.TimeUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MediaPlayerController extends RelativeLayout {
    private String TAG = "MediaPlayerController";
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
    private int currentPosition;

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
        ButterKnife.bind(this, view);
        initViewDisplay();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouching = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myVideoPlayer.iMediaPlayer.startPlay(seekBar.getProgress());
                isTouching = false;

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

    private boolean hasPause;
    @OnClick({R.id.iv_replay, R.id.iv_share, R.id.iv_play, R.id.iv_fullscreen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_replay:

                break;
            case R.id.iv_share:
                break;
            case R.id.iv_play:
//                //点击一个新的条目进行播放
//                //点击的条目下标是否是之前播放的条目下标
//                if (ItemPosition != adapter.currentPosition && adapter.currentPosition != -1) {
//                    Log.i(TAG, "点击了其他的条目");
//
//                    //让其他的条目停止播放(还原条目开始的状态)
//                    iMediaPlayer.releaseMP();
//                    //把播放条目的下标设置给适配器
//                    adapter.setPlayPosition(position);
//                    //刷新显示
//                    adapter.notifyDataSetChanged();
//                    //播放
//                    ivPlay.setVisibility(View.GONE);
//                    tvAllTime.setVisibility(View.GONE);
//                    pbLoading.setVisibility(View.VISIBLE);
//                    //视频播放界面也需要显示
//                    myVideoPlayer.setVideoViewVisiable(View.VISIBLE);
//                    ivPlay.setImageResource(R.mipmap.player_pause);
//                    return;
//                }
//
//                if (myVideoPlayer!=null && myVideoPlayer.mMediaPlayer.isPlaying()) {
//                    //暂停
//                    iMediaPlayer.pausePlay();
//                    //移除隐藏Controller布局的消息
////                    mHandler.removeMessages(MSG_HIDE_CONTROLLER);
//                    //移除更新播放时间和进度的消息
////                    mHandler.removeMessages(MSG_UPDATE_TIME_PROGRESS);
//                    ivPlay.setImageResource(R.mipmap.player_start);
//                    hasPause = true;
//                } else {
//                    if (hasPause) {
//                        //继续播放
//                        iMediaPlayer.startPlay(0);
////                        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, 2000);
////                        updatePlayTimeAndProgress();
//                        hasPause = false;
//                    } else {
//                        //播放
//                        ivPlay.setVisibility(View.GONE);
//                        tvAllTime.setVisibility(View.GONE);
//                        pbLoading.setVisibility(View.VISIBLE);
//                        //视频播放界面也需要显示
//                        myVideoPlayer.setVideoViewVisiable(View.VISIBLE);
//                        //把播放条目的下标设置给适配器
//                        adapter.setPlayPosition(position);
//                    }
//                    ivPlay.setImageResource(R.mipmap.player_pause);
//                }
                break;
            case R.id.iv_fullscreen:
                break;
        }
    }

    private int ItemPosition;

    public void setPosition(int position) {
        this.ItemPosition = position;
    }

//    private VideoPlayListAdatper adapter;
//
//    public void setAdapter(VideoPlayListAdatper videoPlayListAdatper) {
//        this.adapter = videoPlayListAdatper;
//    }

    private VideoPlayer myVideoPlayer;
    private IMediaPlayer iMediaPlayer;

    public void setVideoPlayer(VideoPlayer myVideoPlayer) {
        this.myVideoPlayer = myVideoPlayer;
        iMediaPlayer = myVideoPlayer.iMediaPlayer;
    }

    //设置播放视频的总时长
    public void setDuration(int duration) {
        String time = TimeUtils.formatTime(duration);
        tvTime.setText(time);
        tvUseTime.setText("00:00");
        seekBar.setMax(duration);
        setseekBar();
    }

    //显示视频播放完成的界面
    public void showPlayFinishView() {
        tvTitle.setVisibility(View.VISIBLE);
        rlPlayFinish.setVisibility(View.VISIBLE);
        tvAllTime.setVisibility(View.VISIBLE);
    }

    //----------记录播放进度----start-----//
    Timer mTimer;
    TimerTask mTimerTask;
    private boolean isTouching;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    };

    public void setseekBar() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (myVideoPlayer == null || myVideoPlayer.mMediaPlayer == null || isTouching == true || seekBar == null)
                    return;//当用户正在拖动进度进度条时不处理进度条的的进度
                currentPosition = myVideoPlayer.mMediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
                mHandler.post(new MyRunnable());
            }

        };
        mTimer.schedule(mTimerTask, 0, 500);
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            tvUseTime.setText(TimeUtils.formatTime(currentPosition));
        }
    }

    public void releaseTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    //----------记录播放进度---end------//
    //移除所有的消息
    public void removeAllMessage() {
        mHandler.removeCallbacksAndMessages(null);
    }
}
