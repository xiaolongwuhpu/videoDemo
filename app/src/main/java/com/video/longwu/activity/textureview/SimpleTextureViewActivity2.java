package com.video.longwu.activity.textureview;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.video.longwu.R;
import com.video.longwu.config.VideoUrl;
import com.video.longwu.util.DialogHelper;
import com.video.longwu.util.ScreenUtil;
import com.video.longwu.util.ToastUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimpleTextureViewActivity2 extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {
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

    private SurfaceTexture mSurfaceTexture;
    MediaPlayer mediaPlayer;
    private int position;//记录位置
    DialogHelper dialogHelper;
    public static final float SHOW_SCALE = 16 * 1.0f / 9;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean isLand;//是否是横屏

    private int videoLength = 0;
    private boolean isPlayering = false;
    private SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_texture_view);
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
//        mSurfaceTexture = mTextureView.getSurfaceTexture();
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
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                initMediaPlayer(surface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                stopPlay();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    private void initMediaPlayer(SurfaceTexture surface) {
        try {
            if (mediaPlayer == null) {
                init();
            }

            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(VideoUrl.url);
            mediaPlayer.setLooping(true);
            mediaPlayer.setSurface(new Surface(surface));
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.prepareAsync();
            mediaPlayer.setScreenOnWhilePlaying(true);

            dialogHelper.showLoadingDialog();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showShort("路径错误");
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

    @OnClick({R.id.textureview,/* R.id.start, R.id.pause,R.id.continued, R.id.stop,  */ R.id.image_zoom, R.id.image_start_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textureview:
                break;
//            case R.id.start:
//                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//                    mediaPlayer.start();
//                } else {
//                    initMediaPlayer();
//                }
//                break;
//            case R.id.pause:
//                pausePlay();
//                break;
//            case R.id.continued:
//                continuePlay(position);
//                break;
//            case R.id.stop:
//                releaseMP();
//                finish();
//                break;
            case R.id.image_zoom:
                changeOrientation();
                break;
            case R.id.image_start_stop:
                if (isPlayering) {
                    imageStartStop.setBackgroundResource(R.mipmap.start);
                    isPlayering = !isPlayering;
                    pausePlay();
                } else {
                    imageStartStop.setBackgroundResource(R.mipmap.pause);
                    isPlayering = !isPlayering;
                    continuePlay(position);
                }
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
            mTextureView.setLayoutParams(layoutParamsSV);
        }
        if (areaWH < mediaWH) {
            //直接放会瘦高。
            int svHeight = (int) (mScreenWidth / mediaWH);
            layoutParamsSV = new RelativeLayout.LayoutParams(mScreenWidth, svHeight);
            layoutParamsSV.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextureView.setLayoutParams(layoutParamsSV);
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

    private boolean isShowTimeText = true;

    @Override
    public void onPrepared(MediaPlayer mp) {
        resetSize();
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
            textTotleTime.setText(formatTime(videoLength));
        } else {
            textTotleTime.setVisibility(View.GONE);
            textPlayTime.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
            isShowTimeText = false;
        }
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
            isPlayering = false;
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
                if (mediaPlayer == null || isTouching == true || seekBar == null)
                    return;//当用户正在拖动进度进度条时不处理进度条的的进度
                final int time = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(time);
                if (isShowTimeText) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textPlayTime.setText(formatTime(time));
                        }
                    });
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 500);
    }

    /**
     * 缓冲
     *
     * @param mp
     * @param percent
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        int currentProgress=mediaPlayer.getCurrentPosition();//mediaplayer 实时进度
        seekBar.setSecondaryProgress((int) (videoLength * percent * 1.0 / 100.0));
    }

    public String formatTime(long ms) {
        if (ms == 0) return "";
        if (formatter == null) {
            formatter = new SimpleDateFormat("HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        }
        String time = formatter.format(ms);
        return time.startsWith("00:") ? time.substring(3) : time;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
