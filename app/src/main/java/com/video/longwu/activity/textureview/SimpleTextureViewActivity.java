package com.video.longwu.activity.textureview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.video.longwu.R;
import com.video.longwu.bean.LayoutSize;
import com.video.longwu.util.DialogHelper;
import com.video.longwu.util.IMediaPlayer;
import com.video.longwu.util.TimeUtils;
import com.video.longwu.util.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimpleTextureViewActivity extends AppCompatActivity implements IMediaPlayerInterface, GestureDetector.OnGestureListener, View.OnTouchListener {
    @BindView(R.id.textureview)
    TextureView mTextureView;
    @BindView(R.id.surfacelayout)
    RelativeLayout surfacelayout;

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    @BindView(R.id.image_zoom)
    ImageView imageViewMM;

    @BindView(R.id.image_start_stop)
    ImageView imageStartStop;
    @BindView(R.id.seekbar)
    SeekBar seekBar;
    @BindView(R.id.text_playtime)
    TextView textPlayTime;
    @BindView(R.id.text_totletime)
    TextView textTotleTime;

    IMediaPlayer iMediaPlayer;
    MediaPlayer mediaPlayer;

    @BindView(R.id.gesture_iv_player_volume)
    ImageView gestureIvPlayerVolume;
    @BindView(R.id.geture_tv_volume_percentage)
    TextView getureTvVolumePercentage;
    @BindView(R.id.gesture_volume_layout)
    RelativeLayout gestureVolumeLayout;
    @BindView(R.id.gesture_iv_player_bright)
    ImageView gestureIvPlayerBright;
    @BindView(R.id.geture_tv_bright_percentage)
    TextView getureTvBrightPercentage;
    @BindView(R.id.gesture_bright_layout)
    RelativeLayout gestureBrightLayout;

    private int position;//记录位置
    DialogHelper dialogHelper;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean isLand;//是否是横屏

    private int videoLength = 0;
    private boolean isPlayering = false;
    private GestureDetector gestureDetector;
    private AudioManager audiomanager;
    private int maxVolume;
    private int currentVolume;
    private int playerWidth;
    private int playerHeight;
    private boolean firstScroll;
    private int gesture_flag;

    private String urlPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_texture_view);
        ButterKnife.bind(this);
        getOrientation();
        gestureDetector = new GestureDetector(this, this);
        if (getTVUrl()) return;
        iMediaPlayer = new IMediaPlayer(this);
        init();
        dialogHelper = new DialogHelper(this);
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
                iMediaPlayer.startPlay(seekBar.getProgress());
                isTouching = false;

            }
        });
        gestureDetector.setIsLongpressEnabled(true);
        mTextureView.setLongClickable(true);
        mTextureView.setEnabled(true);
        mTextureView.setOnTouchListener(this);
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 获取系统最大音量
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取当前值
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);

//        /** 获取视频播放窗口的尺寸 */
//        ViewTreeObserver viewObserver = mTextureView.getViewTreeObserver();
//        viewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mTextureView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                playerWidth = mTextureView.getWidth();
//                playerHeight = mTextureView.getHeight();
//            }
//        });
    }

    private boolean getTVUrl() {
        Intent intent = getIntent();
        if (intent == null) {
            ToastUtil.showShort("连接错误请返回重进");
            finish();
            return true;
        }
        String url = intent.getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            ToastUtil.showShort("连接错误请返回重进");
            finish();
            return true;
        } else {
            urlPath = url;
        }
        return false;
    }

    private void init() {
        reSizeSurface();
        mTextureView.setKeepScreenOn(true);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                if (mediaPlayer == null) {
                    mediaPlayer = iMediaPlayer.initMediaPlayer(surface, urlPath);
                    dialogHelper.showLoadingDialog();
                } else {
                    mediaPlayer.setSurface(new Surface(surface));
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//                iMediaPlayer.stopPlay();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    // 重新 计算包裹surface的父布局的窗口大小
    private void reSizeSurface() {
        LayoutSize layoutSize = ReSizeView.ResetRootLayoutSize(this, isLand);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) surfacelayout.getLayoutParams();
        lp.height = playerHeight = layoutSize.getHeight();
        lp.width = playerWidth = layoutSize.getWidth();
        surfacelayout.setLayoutParams(lp);
    }


    @Override
    protected void onPause() {
        super.onPause();
        iMediaPlayer.pausePlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iMediaPlayer.startPlay(position);
    }

    @OnClick({R.id.textureview, R.id.image_zoom, R.id.image_start_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textureview:
                break;
            case R.id.image_zoom:
                changeOrientation();
                break;
            case R.id.image_start_stop:
                if (isPlayering) {
                    imageStartStop.setBackgroundResource(R.mipmap.player_start);
                    isPlayering = !isPlayering;
                    iMediaPlayer.pausePlay();
                } else {
                    imageStartStop.setBackgroundResource(R.mipmap.player_pause);
                    isPlayering = !isPlayering;
                    iMediaPlayer.startPlay(position);
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLand = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        reSizeSurface();
        ReSizeView.resetSurfaceSize(this, mTextureView, mediaPlayer, isLand);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19 ) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void changeOrientation() {
        if (Configuration.ORIENTATION_LANDSCAPE == this.getResources().getConfiguration().orientation) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    public void getOrientation() {
        if (Configuration.ORIENTATION_LANDSCAPE == this.getResources().getConfiguration().orientation) {
            isLand = true;
            imageViewMM.setBackgroundResource(R.mipmap.screen_min);
        } else {
            isLand = false;
            imageViewMM.setBackgroundResource(R.mipmap.screen_max);
        }
    }

    private boolean isShowTimeText = true;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPrepared() {
        ReSizeView.resetSurfaceSize(this, mTextureView, mediaPlayer, isLand);
        videoLength = mediaPlayer.getDuration();
        seekBar.setMax(videoLength);
        if (position != 0) {
            mediaPlayer.seekTo(position);
        }
        dialogHelper.dismissLoadingDialog();
        mediaPlayer.start();
        isPlayering = true;
        imageStartStop.setBackgroundResource(R.mipmap.player_pause);
        if (videoLength != 0) {
            isShowTimeText = true;
            textTotleTime.setText(TimeUtils.formatTime(videoLength));
            seekBar.setVisibility(View.VISIBLE);
            setseekBar();
        } else {
            textTotleTime.setVisibility(View.GONE);
            textPlayTime.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
            isShowTimeText = false;
        }

    }

    @Override
    public void pausePlayer(int percent) {
        position = percent;
    }

    @Override
    public void onError() {
        dialogHelper.dismissLoadingDialog();
        ToastUtil.showLong("播放失败,请重试");
        finish();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void currentPosition(int position) {

    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (isShowTimeText)
            seekBar.setSecondaryProgress((int) (videoLength * percent * 1.0 / 100.0));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseTimer();
        iMediaPlayer.releaseMP();
    }

    //----------记录播放进度----start-----//
    Timer mTimer;
    TimerTask mTimerTask;
    private boolean isTouching;

    private void setseekBar() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer == null || isTouching == true || seekBar == null)
                    return;//当用户正在拖动进度进度条时不处理进度条的的进度
                final int time = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(time);
                if (isShowTimeText) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textPlayTime.setText(TimeUtils.formatTime(time));
                        }
                    });
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 500);
    }

    private void releaseTimer() {
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

    //------------------------手势滑动-----------start------------------------
    int GESTURE_FLAG = 0;
    int GESTURE_MODIFY_VOLUME = 2;//音量
    int GESTURE_MODIFY_BRIGHT = 3;//亮度
    private float mBrightness = -1f; // 亮度
    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快

    // 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
    @Override
    public boolean onDown(MotionEvent e) {
        // 设定是触摸屏幕后第一次scroll的标志
        firstScroll = true;
        return false;
    }

    // 用户轻触触摸屏，尚未松开或拖动，由1个MotionEvent ACTION_DOWN触发, 注意和onDown()的区别，强调的是没有松开或者拖动的状态
    @Override
    public void onShowPress(MotionEvent e) {

    }

    // 用户（轻触触摸屏后）松开，由1个MotionEvent ACTION_UP触发
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    // 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float mOldX = e1.getX(), mOldY = e1.getY();
        int y = (int) e2.getRawY();
        if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
            // 横向的距离变化大则调整进度，纵向的变化大则调整音量
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {
//                gesture_progress_layout.setVisibility(View.VISIBLE);
                gestureVolumeLayout.setVisibility(View.GONE);
                gestureBrightLayout.setVisibility(View.GONE);
//                GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
            } else {
                if (mOldX > playerWidth * 3.0 / 5) {// 音量
                    gestureVolumeLayout.setVisibility(View.VISIBLE);
                    gestureBrightLayout.setVisibility(View.GONE);
//                    gesture_progress_layout.setVisibility(View.GONE);
                    GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                } else if (mOldX < playerWidth * 2.0 / 5) {// 亮度
                    gestureBrightLayout.setVisibility(View.VISIBLE);
                    gestureVolumeLayout.setVisibility(View.GONE);
//                    gesture_progress_layout.setVisibility(View.GONE);
                    GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
                }
            }
        }

        // 如果每次触摸屏幕后第一次scroll是调节进度，那之后的scroll事件都处理音量进度，直到离开屏幕执行下一次操作
//        if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
//            // distanceX=lastScrollPositionX-currentScrollPositionX，因此为正时是快进
//            if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
//                if (distanceX >= DensityUtil.dip2px(this, STEP_PROGRESS)) {// 快退，用步长控制改变速度，可微调
//                    gesture_iv_progress.setImageResource(R.drawable.souhu_player_backward);
//                    if (playingTime > 3) {// 避免为负
//                        playingTime -= 3;// scroll方法执行一次快退3秒
//                    } else {
//                        playingTime = 0;
//                    }
//                } else if (distanceX <= -DensityUtil.dip2px(this, STEP_PROGRESS)) {// 快进
//                    gesture_iv_progress.setImageResource(R.drawable.souhu_player_forward);
//                    if (playingTime < videoTotalTime - 16) {// 避免超过总时长
//                        playingTime += 3;// scroll执行一次快进3秒
//                    } else {
//                        playingTime = videoTotalTime - 10;
//                    }
//                }
//                if (playingTime < 0) {
//                    playingTime = 0;
//                }
//                tv_pro_play.seekTo(playingTime);
//                geture_tv_progress_time.setText(DateTools.getTimeStr(playingTime) + "/" + DateTools.getTimeStr(videoTotalTime));
//            }
//        }

        // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
        else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
            currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
            if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                if (distanceY >= DensityUtil.dp2px(STEP_VOLUME)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                    if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                        currentVolume++;
                    }
                    gestureIvPlayerVolume.setImageResource(R.mipmap.player_volume);
                } else if (distanceY <= -DensityUtil.dp2px(STEP_VOLUME)) {// 音量调小
                    if (currentVolume > 0) {
                        currentVolume--;
                        if (currentVolume == 0) {// 静音，设定静音独有的图片
                            gestureIvPlayerVolume.setImageResource(R.mipmap.player_volume_0);
                        }
                    }
                }
                int percentage = (currentVolume * 100) / maxVolume;
                getureTvVolumePercentage.setText(percentage + "%");
                audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            }
        }

        // 如果每次触摸屏幕后第一次scroll是调节亮度，那之后的scroll事件都处理亮度调节，直到离开屏幕执行下一次操作
        else if (GESTURE_FLAG == GESTURE_MODIFY_BRIGHT) {
            gestureIvPlayerBright.setImageResource(R.mipmap.player_bright);
            if (mBrightness < 0) {
                mBrightness = getWindow().getAttributes().screenBrightness;
                if (mBrightness <= 0.00f)
                    mBrightness = 0.50f;
                if (mBrightness < 0.01f)
                    mBrightness = 0.01f;
            }
            WindowManager.LayoutParams lpa = getWindow().getAttributes();
            lpa.screenBrightness = mBrightness + (mOldY - y) / playerHeight;
            if (lpa.screenBrightness > 1.0f)
                lpa.screenBrightness = 1.0f;
            else if (lpa.screenBrightness < 0.01f)
                lpa.screenBrightness = 0.01f;
            getWindow().setAttributes(lpa);
            getureTvBrightPercentage.setText((int) (lpa.screenBrightness * 100) + "%");
        }

        firstScroll = false;// 第一次scroll执行完成，修改标志
        return false;
    }

    // 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
    @Override
    public void onLongPress(MotionEvent e) {

    }

    // 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 手势里除了singleTapUp，没有其他检测up的方法
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 手指离开屏幕后，重置调节音量或进度的标志
            gesture_flag = 0;
            gestureVolumeLayout.setVisibility(View.GONE);
            gestureBrightLayout.setVisibility(View.GONE);
        }
        return gestureDetector.onTouchEvent(event);//如果想要监听到双击、滑动、长按等复杂的手势操作，这个时候就必须得用到OnGestureListener了

    }
    //------------------------手势滑动-----------end------------------------
}
