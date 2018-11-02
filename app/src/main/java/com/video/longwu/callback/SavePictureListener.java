package com.video.longwu.callback;

public abstract class SavePictureListener {

    public void onSuccess() {
    }

    public abstract void onFailed(String reason);

}
