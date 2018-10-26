package com.video.longwu;

import android.app.Application;
import android.content.Context;
import android.text.StaticLayout;
import android.widget.SimpleCursorTreeAdapter;

public class BaseApplication extends Application {

    private static BaseApplication mBaseApplication=null;
    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
    }


    public static BaseApplication getContext() {
        return mBaseApplication;
    }

}
