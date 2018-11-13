package com.video.longwu.util;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.video.longwu.activity.textureview.IMediaPlayerInterface;

import java.io.IOException;

public class IMediaPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    MediaPlayer mediaPlayer;
    Context mContext;
    int position;
    IMediaPlayerInterface iMediaPlayerInterface;

    public IMediaPlayer() {

    }

    public IMediaPlayer(Context context) {
        mContext = context;
        this.iMediaPlayerInterface = (IMediaPlayerInterface) context;
    }

    public IMediaPlayer(Context context, IMediaPlayerInterface iMediaPlayerInterface) {
        mContext = context;
        this.iMediaPlayerInterface = iMediaPlayerInterface;
    }

    public MediaPlayer initMediaPlayer(SurfaceTexture surface, String url) {
        try {
            if (surface == null) {
                return null;
            }
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setSurface(new Surface(surface));

            initPreperListener(url);
            return mediaPlayer;
        } catch (IOException e) {
            ToastUtil.showShort("路径错误");
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setSurface(new Surface(surface));
            return mediaPlayer;

        }
    }

    public MediaPlayer initMediaPlayer(SurfaceHolder holder, String url) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDisplay(holder);
            initPreperListener(url);
            return mediaPlayer;
        } catch (IOException e) {
            ToastUtil.showShort("路径错误");
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDisplay(holder);
            return mediaPlayer;

        }
    }

    private void initPreperListener(String url) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.prepareAsync();
        mediaPlayer.setScreenOnWhilePlaying(true);
    }

    public void startPlay(int position) {
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

    public void pausePlay() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                position = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                iMediaPlayerInterface.pausePlayer(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort("暂停失败");
        }
    }

    public void stopPlay() {
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

    /**
     * 销毁掉MediaPlayer对象
     */
    public void releaseMP() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            System.gc();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        iMediaPlayerInterface.onBufferingUpdate(percent);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        iMediaPlayerInterface.onPrepared();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        iMediaPlayerInterface.onComplete();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        iMediaPlayerInterface.onError();
        return true;
    }
}
