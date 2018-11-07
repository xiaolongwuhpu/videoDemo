package com.video.longwu.activity.textureview;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.video.longwu.R;
import com.video.longwu.config.VideoUrl;
import com.video.longwu.constant.CommonConstants;
import com.video.longwu.util.DialogHelper;
import com.video.longwu.util.IMediaPlayer;
import com.video.longwu.util.ScreenUtil;
import com.video.longwu.util.TimeUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimpleTextureViewActivity extends AppCompatActivity implements IMediaPlayerInterface {
    @BindView(R.id.textureview)
    TextureView mTextureView;
    @BindView(R.id.surfacelayout)
    RelativeLayout surfacelayout;
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

    private int position;//记录位置
    DialogHelper dialogHelper;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean isLand;//是否是横屏

    private int videoLength = 0;
    private boolean isPlayering = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_texture_view);
        ButterKnife.bind(this);
        getOrientation();
        if (iMediaPlayer == null) {
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
        }

    }

    private void init() {
        mScreenWidth = ScreenUtil.getWidth(this);
        mScreenHeight = ScreenUtil.getHeight(this);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) surfacelayout.getLayoutParams();
        if (isLand) {//横屏
            lp.height = (int) (mScreenWidth * CommonConstants.SHOW_SCALE);
        } else {//竖屏
            lp.height = (int) (1.0 * mScreenWidth / CommonConstants.SHOW_SCALE);
        }
        surfacelayout.setLayoutParams(lp);
        mTextureView.setKeepScreenOn(true);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                if (mediaPlayer == null) {
                    mediaPlayer = iMediaPlayer.initMediaPlayer(surface, VideoUrl.url);
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
                    imageStartStop.setBackgroundResource(R.mipmap.start);
                    isPlayering = !isPlayering;
                    iMediaPlayer.pausePlay();
                } else {
                    imageStartStop.setBackgroundResource(R.mipmap.pause);
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
        ReSizeView.resetSize(this, mTextureView, mediaPlayer, isLand);
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }

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
            imageViewMM.setBackgroundResource(R.mipmap.min);
        } else {
            isLand = false;
            imageViewMM.setBackgroundResource(R.mipmap.max);
        }
    }

    private boolean isShowTimeText = true;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPrepared() {
        ReSizeView.resetSize(this, mTextureView, mediaPlayer, isLand);
        videoLength = mediaPlayer.getDuration();
        seekBar.setMax(videoLength);
        if (position != 0) {
            mediaPlayer.seekTo(position);
        }
        dialogHelper.dismissLoadingDialog();
        mediaPlayer.start();
        isPlayering = true;
        imageStartStop.setBackgroundResource(R.mipmap.pause);
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
}
