package com.video.longwu.activity.videolist.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;

import com.video.longwu.R;
import com.video.longwu.activity.textureview.IMediaPlayerInterface;
import com.video.longwu.activity.videolist.MediaPlayerController;
import com.video.longwu.bean.TVListBean;
import com.video.longwu.util.IMediaPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayer extends RelativeLayout implements IMediaPlayerInterface {
    IMediaPlayer iMediaPlayer;
    @BindView(R.id.textureview)
    TextureView textureview;
    @BindView(R.id.mediaplayercontroller)
    MediaPlayerController mediaplayercontroller;
    private Context mContext;
    MediaPlayer mMediaPlayer;

    public VideoPlayer(Context context) {
        this(context, null);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iMediaPlayer = new IMediaPlayer(context);
        mContext = context;
        View view = View.inflate(getContext(), R.layout.video_player, this);
        ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        textureview.setKeepScreenOn(true);
        textureview.setSurfaceTextureListener(surfaceTextureListener);


    }
    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mMediaPlayer =  iMediaPlayer.initMediaPlayer(surface,tvListBean.getTvUrl());
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
    @Override
    public void onPrepared() {

    }

    @Override
    public void pausePlayer(int percent) {

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

    }

    TVListBean tvListBean;

    public void setDataSouresUrl(TVListBean tvListBean) {
        this.tvListBean = tvListBean;
    }

}
