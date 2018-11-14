package com.video.longwu.activity.videolist;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.video.longwu.R;
import com.video.longwu.activity.videolist.view.VideoPlayer;
import com.video.longwu.adapter.VideoListAdapter;
import com.video.longwu.util.MediaHelper;
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
    public  TextView tvTitle;
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

    private static final int MSG_HIDE_TITLE = 0;
    private static final int MSG_UPDATE_TIME_PROGRESS = 1;
    private static final int MSG_HIDE_CONTROLLER = 2;

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
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //按下+已经播放了
                if (event.getAction() == MotionEvent.ACTION_DOWN && myVideoPlayer.hasPlay) {
                    //显示或者隐藏视频控制界面
                    showOrHideVideoController();
                }
                return true;//去处理事件
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MediaHelper.play(seekBar.getProgress());
            }
        });
    }

    //显示或者隐藏视频控制界面
    private void showOrHideVideoController() {
        if (llPlayControl.getVisibility() == View.GONE) {
            //显示（标题、播放按钮、视频进度控制）
            tvTitle.setVisibility(View.VISIBLE);
            ivPlay.setVisibility(View.VISIBLE);
            //加载动画
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_enter);
            animation.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    llPlayControl.setVisibility(View.VISIBLE);
                    //过2秒后自动隐藏
                    mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, 2000);
                }
            });
            //执行动画
            llPlayControl.startAnimation(animation);
        } else {
            //隐藏（标题、播放按钮、视频进度控制）
            tvTitle.setVisibility(View.GONE);
            ivPlay.setVisibility(View.GONE);
            //加载动画
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_exit);
            animation.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    llPlayControl.setVisibility(View.GONE);
                }
            });
            //执行动画
            llPlayControl.startAnimation(animation);
        }
    }


    //简单的动画监听器（不需要其他的监听器去实现多余的方法）
    private class SimpleAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
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
                String time = TimeUtils.formatTime(MediaHelper.getInstance().getDuration());
                tvTime.setText(time);
                tvAllTime.setText(time);
                rlPlayFinish.setVisibility(View.GONE);
                //继续播放
                ItemPosition = 0;
                MediaHelper.play(ItemPosition);
                mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, 2000);
                updatePlayTimeAndProgress();
                //延时隐藏标题
                delayHideTitle();
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_play:
                //点击一个新的条目进行播放
                //点击的条目下标是否是之前播放的条目下标
                if (ItemPosition != adapter.currentPosition && adapter.currentPosition != -1) {
                    Log.i(TAG, "点击了其他的条目");

                    //让其他的条目停止播放(还原条目开始的状态)
                    MediaHelper.release();
                    //把播放条目的下标设置给适配器
                    adapter.setPlayPosition(ItemPosition);
                    //刷新显示
                    adapter.notifyDataSetChanged();
                    //播放
                    ivPlay.setVisibility(View.GONE);
                    tvAllTime.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.VISIBLE);
                    //视频播放界面也需要显示
                    myVideoPlayer.setVideoViewVisiable(View.VISIBLE);
                    ivPlay.setImageResource(R.mipmap.player_pause);
                    return;
                }

                if(MediaHelper.getInstance().isPlaying()){
                    //暂停
                    MediaHelper.pause();
                    //移除隐藏Controller布局的消息
                    mHandler.removeMessages(MSG_HIDE_CONTROLLER);
                    //移除更新播放时间和进度的消息
                    mHandler.removeMessages(MSG_UPDATE_TIME_PROGRESS);
                    ivPlay.setImageResource(R.mipmap.player_start);
                    hasPause = true;
                } else {
                    if (hasPause) {
                        //继续播放
                        MediaHelper.play(ItemPosition);
                        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, 2000);
                        updatePlayTimeAndProgress();
                        hasPause = false;
                    } else {
                        //播放
                        ivPlay.setVisibility(View.GONE);
                        tvAllTime.setVisibility(View.GONE);
                        pbLoading.setVisibility(View.VISIBLE);
                        //视频播放界面也需要显示
                        myVideoPlayer.setVideoViewVisiable(View.VISIBLE);
                        //把播放条目的下标设置给适配器
                        adapter.setPlayPosition(ItemPosition);
                    }
                    ivPlay.setImageResource(R.mipmap.player_pause);
                }
                break;
            case R.id.iv_fullscreen:
                break;
        }
    }

    private int ItemPosition;

    public void setPosition(int position) {
        this.ItemPosition = position;
    }

    private VideoListAdapter adapter;

    public void setAdapter(VideoListAdapter videoPlayListAdatper) {
        this.adapter = videoPlayListAdatper;
    }

    private VideoPlayer myVideoPlayer;

    public void setVideoPlayer(VideoPlayer myVideoPlayer) {
        this.myVideoPlayer = myVideoPlayer;
    }

    //设置播放视频的总时长
    public void setDuration(int duration) {
        String time = TimeUtils.formatTime(duration);
        tvTime.setText(time);
        tvAllTime.setText(time);
        tvUseTime.setText("00:00");
        seekBar.setMax(duration);
    }

    //显示视频播放完成的界面
    public void showPlayFinishView() {
        tvTitle.setVisibility(View.VISIBLE);
        rlPlayFinish.setVisibility(View.VISIBLE);
        tvAllTime.setVisibility(View.VISIBLE);
    }

    //设置视频加载进度条的显示状态
    public void setPbLoadingVisiable(int visiable) {
        pbLoading.setVisibility(visiable);
    }

    //----------记录播放进度----start-----//
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_HIDE_TITLE:
                    tvTitle.setVisibility(View.GONE);
                    break;
                case MSG_UPDATE_TIME_PROGRESS:
                    updatePlayTimeAndProgress();
                    break;
                case MSG_HIDE_CONTROLLER:
                    showOrHideVideoController();
                    break;
            }
        }
    };

    //更新播放的时间和进度
    public void updatePlayTimeAndProgress() {
        //获取目前播放的进度
        int currentPosition = MediaHelper.getInstance().getCurrentPosition();
        //格式化
        String useTime = TimeUtils.formatTime(currentPosition);
        tvUseTime.setText(useTime);
        //更新进度
        int duration = MediaHelper.getInstance().getDuration();
        if(duration == 0){
            return;
        }
        seekBar.setProgress(currentPosition);
        //发送一个更新的延时消息
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME_PROGRESS,500);
//        setseekBar();
    }
    //----------记录播放进度---end------//
    public void delayHideTitle(){
        //移除消息
        mHandler.removeMessages(MSG_HIDE_TITLE);
        //发送一个空的延时2秒消息
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_TITLE,2000);
    }
    //移除所有的消息
    public void removeAllMessage() {
        mHandler.removeCallbacksAndMessages(null);
    }
}
