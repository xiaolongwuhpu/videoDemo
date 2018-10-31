package com.video.longwu.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CameraType {
    public static final int FONT = 1;
    public static final int BACK = 0;

    public int cameraType;



    //Retention 是元注解，简单地讲就是系统提供的，用于定义注解的“注解”
    @Retention(RetentionPolicy.SOURCE)
    //这里指定int的取值只能是以下范围
    @IntDef({FONT, BACK})
   public  @interface CameraTypes {}

//    public CameraType(@CameraTypes int fileType) {
//        this.cameraType = fileType;
//    }

    @CameraTypes
    public int getCameraType() {
        return cameraType;
    }

    public void setCameraType(@CameraTypes int cameraType) {
        this.cameraType = cameraType;
    }
}
