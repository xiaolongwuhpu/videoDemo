package com.video.longwu.activity.textureview;

public interface IMediaPlayerInterface {
    void onPrepared();
    void pausePlayer(int percent);
    void onError();

    void onComplete();

    /**
     * 当前播放进度
     * @param position
     */
    void currentPosition(int position);

    /**
     * 缓冲进度
     * @param percent
     */
    void onBufferingUpdate(int percent);


}
