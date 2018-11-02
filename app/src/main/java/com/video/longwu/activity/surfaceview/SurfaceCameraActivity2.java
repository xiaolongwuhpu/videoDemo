package com.video.longwu.activity.surfaceview;

import android.hardware.Camera;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.video.longwu.R;
import com.video.longwu.callback.SavePictureListener;
import com.video.longwu.util.FileUtil;
import com.video.longwu.util.ICamera;
import com.video.longwu.util.ToastUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 使用封装好的工具类
 */
public class SurfaceCameraActivity2 extends AppCompatActivity implements SurfaceHolder.Callback {

    @BindView(R.id.btn_take_picture)
    Button btnTakePicture;
    @BindView(R.id.surface)
    SurfaceView surfaceView;
    @BindView(R.id.image_ligth)
    ImageView imageLigth;

    SurfaceCameraActivity2 mContext;
    private SurfaceHolder surfaceHolder;
    private ICamera mICamera;
    private Camera camera;
    private static final int save_pic = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_camera_2_3);
        ButterKnife.bind(this);
        mICamera = new ICamera(true);
        mContext = this;
        initData();
    }

    private void initData() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = mICamera.openCamera(this);
        mICamera.startSurfacePreview(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mICamera.closeCamera();
    }

    @OnClick({R.id.surface, R.id.btn_take_picture, R.id.image_ligth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.surface:
                mICamera.autoFocus();
                break;
            case R.id.btn_take_picture:
                if (camera == null) {
                    camera = mICamera.openCamera(this);
                }
                camera.takePicture(shutterCallback, null, postPictureCallBack);
                break;
            case R.id.image_ligth:
                switchFlashMode();
                break;
        }
    }

    private ToneGenerator tone;
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {//默认就有声音
        @Override
        public void onShutter() {//只要实现这个方法,就有默认的声音
//            //震动
//            Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
//            vibrator.vibrate(200);

//            if (tone == null)
//                //发出提示用户的声音
//            tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
//            tone.startTone(ToneGenerator.TONE_PROP_BEEP);//拍照声音
        }
    };

    Camera.PictureCallback postPictureCallBack = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera mCamera) {
            isPreviewing = true;
            myThread = new MyThread(mContext, data);
            myThread.start();
            camera = mICamera.openCamera(mContext);
            mICamera.startSurfacePreview(surfaceHolder);
        }
    };

    private void savePicture(byte[] data) {
        String fileName = SystemClock.currentThreadTimeMillis() + ".jpg";
        FileUtil.saveJPG(data, fileName, new SavePictureListener() {
            @Override
            public void onFailed(String reason) {
                Message msg = Message.obtain();
                msg.what = save_pic;
                msg.obj = reason;
                mHandler.sendMessage(msg);
            }
        });
    }

    private boolean isPreviewing = false;

    //----------------打开/关闭 闪光灯-------开始-------
    String FlashMode = Camera.Parameters.FLASH_MODE_OFF;
    private int lightType = 3;//1 不开启闪光灯 2自动 3长亮

    /**
     * 闪光灯开关
     */
    public void switchFlashMode() {
        switch (lightType) {
            case 1:
                imageLigth.setBackgroundResource(R.mipmap.flash_auto);
                setIsOpenFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                lightType = 2;
                break;
            case 2:
                imageLigth.setBackgroundResource(R.mipmap.flash_on);
                setIsOpenFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                lightType = 3;
                break;
            case 3:
                imageLigth.setBackgroundResource(R.mipmap.flash_close);
                setIsOpenFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                lightType = 1;
                break;
        }
    }

    //设置开启闪光灯(重新预览)
    public void setIsOpenFlashMode(String mIsOpenFlashMode) {
        FlashMode = mIsOpenFlashMode;
        Camera.Parameters mParameters = camera.getParameters();
        //设置闪光灯模式
        mParameters.setFlashMode(mIsOpenFlashMode);
        camera.setParameters(mParameters);
    }

    //----------------打开/关闭 闪光灯-------结束-------
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case save_pic:
                    ToastUtil.showShort((String) msg.obj);
                    break;
            }
        }
    };

    MyThread myThread;

    private class MyThread extends Thread {
        WeakReference<SurfaceCameraActivity2> mThreadActivityRef;
        byte[] data;

        public MyThread(SurfaceCameraActivity2 activity, byte[] data) {
            mThreadActivityRef = new WeakReference<SurfaceCameraActivity2>(
                    activity);
            this.data = data;
        }

        @Override
        public void run() {
            super.run();
            if (mThreadActivityRef == null)
                return;
            if (mThreadActivityRef.get() != null)
                savePicture(data);
        }
    }

}
