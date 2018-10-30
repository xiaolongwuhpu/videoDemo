package com.video.longwu.activity.surfaceview;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.video.longwu.R;
import com.video.longwu.callback.SavePictureListener;
import com.video.longwu.util.FileUtil;
import com.video.longwu.util.ICamera;
import com.video.longwu.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SurfaceCameraActivity2 extends AppCompatActivity implements SurfaceHolder.Callback {

    @BindView(R.id.btn_take_picture)
    Button btnTakePicture;
    @BindView(R.id.surface)
    SurfaceView surfaceView;
    @BindView(R.id.image_ligth)
    ImageView imageLigth;
    @BindView(R.id.image_preview)
    ImageView imagePreview;
    @BindView(R.id.btn_cancle)
    Button btnCancle;
    @BindView(R.id.btn_save)
    Button btnSave;
    SurfaceCameraActivity2 mContext;
    private SurfaceHolder surfaceHolder;
    private ICamera mICamera;
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
                        mICamera.autoFocus();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_camera);
        ButterKnife.bind(this);
        mICamera = new ICamera(true);
        mContext = this;
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
//        openCamera();
         camera = mICamera.openCamera(this);
    }

//    private void openCamera() {
//        try {
//            camera = Camera.open();
//            camera.setPreviewDisplay(surfaceHolder);
//        } catch (Exception e) {
//            if (null != camera) {
//                camera.release();
//                camera = null;
//            }
//            e.printStackTrace();
//            ToastUtil.showLong("启动摄像头失败,请开启摄像头权限");
//        }
//    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        initCamera();
        setCameraPreView();
        isFlashlightOn();
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

    @OnClick({R.id.surface, R.id.btn_take_picture, R.id.image_ligth, R.id.btn_cancle, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.surface:
                if (camera != null) {
                    if (camera != null)
                        camera.autoFocus(myAutoFocusCallback1);
                }
                break;
            case R.id.btn_take_picture:
                setUIvisiable(false);
                if(camera==null){
//                    openCamera();
                    camera = mICamera.openCamera(this);
                    mICamera.startSurfacePreview(surfaceHolder);
                }
                camera.takePicture(shutterCallback, null, postPictureCallBack);
//                camera.stopPreview();
//                camera.release();
//                camera = null;
                break;
            case R.id.image_ligth:
                flashlightUtils();
                break;
            case R.id.btn_cancle:
                setUIvisiable(true);
//                setCameraPreView();
                break;
            case R.id.btn_save:
                savePicture();
                setUIvisiable(true);
                setCameraPreView();
                break;
        }
    }

    private ToneGenerator tone;
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            //震动
            Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
            vibrator.vibrate(200);

            if (tone == null)
                //发出提示用户的声音
                tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP);//拍照声音
//            tone.startTone(ToneGenerator.TONE_PROP_ACK);//拍照声音


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


            Glide.with(mContext).load(data).into(imagePreview);

//            camera.cancelAutoFocus(); //这一句很关键
//            //恢复对焦模式
//            Camera.Parameters parameters = camera.getParameters();
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//            parameters.setFocusAreas(null);
//            camera.setParameters(parameters);
//            //
//            camera.startPreview();
            isPreviewing = true;
            mContext.data = data;
            //保存图片
//            savePicture(data);
//            File file = new File(FileUtil.IMAGE_PATH, fileName);
//            Uri uri = Uri.fromFile(file);
//            Intent it = new Intent();
//            it.setAction("android.intent.action.VIEW");
//            it.setDataAndType(uri, "image/jpeg");
//            startActivity(it);
        }
    };
    byte[] data;

    private void savePicture() {
        String fileName = SystemClock.currentThreadTimeMillis() + ".jpg";
        FileUtil.saveJPG(data, fileName, new SavePictureListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String reason) {

            }
        });
        ToastUtil.showShort("保存成功");
    }

    private boolean isPreviewing = false;

    private void setUIvisiable(boolean isPreviewing) {
        if (isPreviewing) {//正常界面
            btnCancle.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            imagePreview.setVisibility(View.GONE);

            imageLigth.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.VISIBLE);
            btnTakePicture.setVisibility(View.VISIBLE);
        } else { //保存界面
            btnCancle.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            imagePreview.setVisibility(View.VISIBLE);

            imageLigth.setVisibility(View.GONE);
            surfaceView.setVisibility(View.GONE);
            btnTakePicture.setVisibility(View.GONE);
        }
    }

//    //初始化相机
//    private void initCamera() {
//        Camera.Parameters parameters = camera.getParameters();
//        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
//        Camera.Size size = getOptimalPreviewSize(sizeList, surfaceView.getWidth(), surfaceView.getHeight());
//        parameters.setPreviewSize(size.width, size.height);
//        parameters.setRotation(90);//生成的图片转90°
//        camera.setParameters(parameters);
//        camera.setDisplayOrientation(90);
//        camera.startPreview();
//    }

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

    //----------------打开/关闭 闪光灯-------开始-------

    /**
     * 设置相机预览
     **/
    public void setCameraPreView() {
        if (camera != null) {
            camera.stopPreview();
        } else {
//            openCamera();
            camera = mICamera.openCamera(this);
        }
        mICamera.startSurfacePreview(surfaceHolder);
//        initCamera();
    }

    /**
     * 闪光灯开关
     */
    public void flashlightUtils() {
        if (isFlashlightOn()) {
            Closeshoudian();
        } else {
            Openshoudian();
        }
    }

    /**
     * 是否开启了闪光灯
     *
     * @return
     */
    public boolean isFlashlightOn() {
        try {
            Camera.Parameters parameters = camera.getParameters();
            String flashMode = parameters.getFlashMode();
            if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                Glide.with(this).load(R.mipmap.flash_close).into(imageLigth);
                return true;
            } else {
                Glide.with(this).load(R.mipmap.flash_open).into(imageLigth);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void Openshoudian() {
        if (camera != null) {
            //打开闪光灯
            camera.startPreview();
            Camera.Parameters parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);

        }
    }

    public void Closeshoudian() {
        if (camera != null) {
            //关闭闪光灯
            camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(camera.getParameters());
            camera.stopPreview();
            camera.release();
            camera = null;
            setCameraPreView();
        }
    }
    //----------------打开/关闭 闪光灯-------结束-------
}
