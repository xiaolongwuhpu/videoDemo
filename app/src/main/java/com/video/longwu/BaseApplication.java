package com.video.longwu;

import android.app.Application;
import android.content.Context;
import android.text.StaticLayout;
import android.widget.SimpleCursorTreeAdapter;

import com.video.longwu.util.SimpleOkhttpClientUtil;

public class BaseApplication extends Application {

    private static BaseApplication mBaseApplication=null;
    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
        SimpleOkhttpClientUtil.initOkHttpClient();
    }


    public static BaseApplication getContext() {
        return mBaseApplication;
    }

}
