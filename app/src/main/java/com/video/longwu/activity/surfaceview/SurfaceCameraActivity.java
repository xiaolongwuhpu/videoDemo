package com.video.longwu.activity.surfaceview;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.video.longwu.R;
import com.video.longwu.util.FileUtil;
import com.video.longwu.util.ToastUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SurfaceCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    @BindView(R.id.btn_take_picture)
    Button btnTakePicture;
    @BindView(R.id.surface)
    SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private AutoFocusCallback myAutoFocusCallback1;
    public static final int only_auto_focus = 110;
    int issuccessfocus = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case only_auto_focus:
                    if (camera != null)
                        camera.autoFocus(myAutoFocusCallback1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_camera);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        myAutoFocusCallback1 = new AutoFocusCallback() {

            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success)//success表示对焦成功
                {
                    issuccessfocus++;
                    if (issuccessfocus <= 1)
                        mHandler.sendEmptyMessage(only_auto_focus);
                } else {
                    //if (issuccessfocus == 0) {
                    mHandler.sendEmptyMessage(only_auto_focus);
                }
            }

        };
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {
            if (null != camera) {
                camera.release();
                camera = null;
            }
            e.printStackTrace();
            ToastUtil.showLong("启动摄像头失败,请开启摄像头权限");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != camera) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @OnClick({R.id.surface, R.id.btn_take_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.surface:
                if (camera != null) {
                    if (camera != null)
                        camera.autoFocus(myAutoFocusCallback1);
                }
                break;
            case R.id.btn_take_picture:
                ToastUtil.showLong("模拟拍照动作");
                camera.takePicture(null, null, postPictureCallBack);
                break;
        }
    }

    private ToneGenerator tone;
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            if (tone == null)
                //发出提示用户的声音
                tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP2);
        }
    };
    Camera.PictureCallback RAWpictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };
    Camera.PictureCallback postPictureCallBack = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.cancelAutoFocus(); //这一句很关键
            //恢复对焦模式
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setFocusAreas(null);
            camera.setParameters(parameters);
            //
            camera.startPreview();
            isPreviewing = true;
            //保存图片
            String fileName = SystemClock.currentThreadTimeMillis() + ".jpg";
            FileUtil.saveJPG(data, fileName);
            File file = new File(FileUtil.IMAGE_PATH, fileName);
            Uri uri = Uri.fromFile(file);
            Intent it = new Intent();
            it.setAction("android.intent.action.VIEW");
            it.setDataAndType(uri, "image/jpeg");
            startActivity(it);
        }
    };
    private boolean isPreviewing = false;


    //初始化相机
    private void initCamera() {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        Camera.Size size = getOptimalPreviewSize(sizeList, surfaceView.getWidth(), surfaceView.getHeight());
        parameters.setPreviewSize(size.width, size.height);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        camera.startPreview();
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;

    }
}
