package com.video.longwu.activity.videolist.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;

import com.video.longwu.R;
import com.video.longwu.activity.videolist.MediaPlayerController;
import com.video.longwu.bean.TVListBean;
import com.video.longwu.util.MediaHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayer extends RelativeLayout {
    public MediaHelper mediaHelper;
    @BindView(R.id.textureview)
    TextureView textureview;
    @BindView(R.id.mediaplayercontroller)
    public MediaPlayerController mediaplayercontroller;
    private Context mContext;
    public MediaPlayer mMediaPlayer;
    public boolean hasPlay;//是否播放了
    Surface mSurface;
    public VideoPlayer(Context context) {
        this(context, null);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = View.inflate(getContext(), R.layout.video_player, this);
        ButterKnife.bind(this, view);
        initViewDisplay();
        mediaplayercontroller.setVideoPlayer(this);
    }


    //准备完成监听
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            //隐藏视频加载进度条
            mediaplayercontroller.setPbLoadingVisiable(View.GONE);
            //进行视频的播放
            MediaHelper.play(0);
            hasPlay = true;
            //隐藏标题
//            mediaplayercontroller.delayHideTitle();
            //设置视频的总时长
            mediaplayercontroller.setDuration(mMediaPlayer.getDuration());
            //更新播放的时间和进度
            mediaplayercontroller.updatePlayTimeAndProgress();
        }
    };

    //错误监听
    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return true;
        }
    };

    /**
     * 初始化显示状态
     */
    public void initViewDisplay() {
        textureview.setKeepScreenOn(true);
        textureview.setSurfaceTextureListener(surfaceTextureListener);
        textureview.setVisibility(View.GONE);
        mediaplayercontroller.initViewDisplay();
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mSurface = new Surface(surface);
            play(tvListBean.getTvUrl());
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    //视频播放（视频的初始化）
    private void play(String url){
        try {
            mMediaPlayer = MediaHelper.getInstance();
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            //让MediaPlayer和TextureView进行视频画面的结合
            mMediaPlayer.setSurface(mSurface);
            //设置监听
//            mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
//            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setOnErrorListener(onErrorListener);
            mMediaPlayer.setOnPreparedListener(onPreparedListener);
            mMediaPlayer.setScreenOnWhilePlaying(true);//在视频播放的时候保持屏幕的高亮
            //异步准备
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    TVListBean tvListBean;

    public void setDataSouresUrl(TVListBean tvListBean) {
        this.tvListBean = tvListBean;
    }

    public void setVideoViewVisiable(int isvisable) {
        textureview.setVisibility(isvisable);
    }
}
