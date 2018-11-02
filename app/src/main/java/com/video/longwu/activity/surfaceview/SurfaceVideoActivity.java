package com.video.longwu.activity.surfaceview;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.video.longwu.R;
import com.video.longwu.config.VideoUrl;
import com.video.longwu.util.DialogHelper;
import com.video.longwu.util.ScreenUtil;
import com.video.longwu.util.ToastUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SurfaceVideoActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {
    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceView;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.pause)
    Button pause;
    @BindView(R.id.continued)
    Button continued;
    @BindView(R.id.stop)
    Button stop;
    @BindView(R.id.surfacelayout)
    RelativeLayout surfacelayout;
    @BindView(R.id.image)
    ImageView imageViewMM;
    @BindView(R.id.seekbar)
    SeekBar seekBar;

    private SurfaceHolder mSurfaceHolder;
    MediaPlayer mediaPlayer;
    private int position;//记录位置
    DialogHelper dialogHelper;
    public static final float SHOW_SCALE = 16 * 1.0f / 9;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean isLand;//是否是横屏

    private int videoLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_video);
        ButterKnife.bind(this);
        getOrientation();
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
                continuePlay(seekBar.getProgress());
                isTouching = false;

            }
        });
    }

    private void init() {
        mediaPlayer = new MediaPlayer();
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
        mScreenWidth = ScreenUtil.getWidth(this);
        mScreenHeight = ScreenUtil.getHeight(this);
        RelativeLayout.LayoutParams lp =
                (RelativeLayout.LayoutParams) surfacelayout.getLayoutParams();
        lp.height = (int) (mScreenWidth * SHOW_SCALE);
        if (lp.height == 0 && !isLand) {
            lp.height = DensityUtil.dp2px(300);
        }
        if (lp.height > DensityUtil.dp2px(300) && !isLand) {
            lp.height = DensityUtil.dp2px(300);
        } else if (isLand) {
            lp.height = ScreenUtil.getHeight(this);
        }
        surfacelayout.setLayoutParams(lp);
        mSurfaceHolder.addCallback(this);
    }

    private void initMediaPlayer() {
        try {
            if (mediaPlayer == null) {
                init();
            }
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(VideoUrl.url);
            mediaPlayer.setLooping(true);
            mediaPlayer.setDisplay(mSurfaceHolder);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
            mediaPlayer.setScreenOnWhilePlaying(true);

            dialogHelper.showLoadingDialog();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showShort("路径错误");
        }

    }

    private void pausePlay() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                position = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort("暂停失败");
        }
    }

    private void continuePlay(int position) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(position);
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort("继续失败");
        }
    }

    private void stopPlay() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                position = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort("继续失败");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlay();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initMediaPlayer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPlay();
    }

    @OnClick({R.id.surfaceview, R.id.start, R.id.pause, R.id.continued, R.id.stop, R.id.image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.surfaceview:

                break;
            case R.id.start:
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                pausePlay();
                break;
            case R.id.continued:
                continuePlay(position);
                break;
            case R.id.stop:
                stopPlay();
                break;
            case R.id.image:
                changeOrientation();
                break;
        }
    }

    private void resetSize() {
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
//        RelativeLayout.LayoutParams layoutParams =
//                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
//        mSurfaceLayout.setLayoutParams(layoutParams);

        int mediaWidth = mediaPlayer.getVideoWidth();
        int mediaHeight = mediaPlayer.getVideoHeight();
        float mediaWH = mediaWidth * 1.0f / mediaHeight;

        RelativeLayout.LayoutParams layoutParamsSV = null;
        if (areaWH > mediaWH) {
            //直接放会矮胖
            int svWidth = (int) (height * mediaWH);
            layoutParamsSV = new RelativeLayout.LayoutParams(svWidth, height);
            layoutParamsSV.addRule(RelativeLayout.CENTER_IN_PARENT);
            mSurfaceView.setLayoutParams(layoutParamsSV);
        }
        if (areaWH < mediaWH) {
            //直接放会瘦高。
            int svHeight = (int) (mScreenWidth / mediaWH);
            layoutParamsSV = new RelativeLayout.LayoutParams(mScreenWidth, svHeight);
            layoutParamsSV.addRule(RelativeLayout.CENTER_IN_PARENT);
            mSurfaceView.setLayoutParams(layoutParamsSV);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        isLand = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        mScreenWidth = ScreenUtil.getWidth(this);
        mScreenHeight = ScreenUtil.getHeight(this);
        resetSize();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
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
            imageViewMM.setBackgroundResource(R.mipmap.min);
        } else {
            isLand = false;
            imageViewMM.setBackgroundResource(R.mipmap.max);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        resetSize();
        videoLength = (int) (mediaPlayer.getDuration());
        seekBar.setMax(videoLength);
        if (position != 0) {
            mediaPlayer.seekTo(position);
        }
        dialogHelper.dismissLoadingDialog();
        mediaPlayer.start();
        setseekBar();
    }

    /**
     * 销毁掉MediaPlayer对象
     */
    private void releaseMP() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            System.gc();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseTimer();
        releaseMP();
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

    //----------记录播放进度---------//
    Timer mTimer;
    TimerTask mTimerTask;
    private boolean isTouching;

    private void setseekBar() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (isTouching == true)//当用户正在拖动进度进度条时不处理进度条的的进度
                    return;
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        };
        mTimer.schedule(mTimerTask, 0, 500);
    }

}
