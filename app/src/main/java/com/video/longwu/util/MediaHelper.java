package com.video.longwu.util;

import android.media.MediaPlayer;

public class MediaHelper {
    private MediaHelper() {
    }

    private static MediaPlayer mPlayer;

    //获取多媒体对象
    public static MediaPlayer getInstance() {
        if (mPlayer == null) {
            synchronized (MediaHelper.class) {
                if (mPlayer == null) {
                    mPlayer = new MediaPlayer();
                    mPlayer.setScreenOnWhilePlaying(true);
                }
            }
        }
        return mPlayer;
    }

    //播放
    public static void play(int position) {
        if (mPlayer != null) {
            mPlayer.seekTo(position);
            mPlayer.start();
        }
    }

    static int currentPosition = 0;
    //暂停
    public static void pause() {
        if (mPlayer != null) {
            currentPosition = mPlayer.getCurrentPosition();
            mPlayer.pause();
        }
    }

    //释放
    public static void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
