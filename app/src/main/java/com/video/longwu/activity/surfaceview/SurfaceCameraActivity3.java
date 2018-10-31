package com.video.longwu.activity.surfaceview;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.video.longwu.util.ScreenUtil;
import com.video.longwu.util.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 每拍摄一张,都给用户选择是否保存
 */
public class SurfaceCameraActivity3 extends AppCompatActivity implements SurfaceHolder.Callback {

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
    SurfaceCameraActivity3 mContext;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private AutoFocusCallback myAutoFocusCallback1;
    public static final int only_auto_focus = 110;
    public static final int save_pic = 111;
    int issuccessfocus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_camera_2_3);
        ButterKnife.bind(this);
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
                    mHandler.sendEmptyMessage(only_auto_focus);
                }
            }

        };
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera();
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
                if (camera == null) {
                    openCamera();
                    initCamera();
                }
                camera.takePicture(shutterCallback, null, postPictureCallBack);
                break;
            case R.id.image_ligth:
                flashlightUtils();
                break;
            case R.id.btn_cancle:
                setUIvisiable(true);
                break;
            case R.id.btn_save:
                setUIvisiable(true);
                myThread = new MyThread(this);
                myThread.start();
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
                tone = new ToneGenerator(AudioManager.STREAM_MUSIC, 50);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP);//拍照声音

        }
    };
    Camera.PictureCallback postPictureCallBack = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Glide.with(mContext).load(data).thumbnail(0.2f).into(imagePreview);
            mContext.data = data;
//            File file = new File(FileUtil.IMAGE_PATH, fileName);
//            Uri uri = Uri.fromFile(file);
//            Intent it = new Intent();
//            it.setAction("android.intent.action.VIEW");
//            it.setDataAndType(uri, "image/jpeg");
//            startActivity(it);
        }
    };

    private void openCamera() {
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {
            if (null != camera) {
                camera.release();
                camera = null;
            }
            ToastUtil.showLong("启动摄像头失败,请开启摄像头权限");
        }
    }

    String FlashMode = Camera.Parameters.FLASH_MODE_OFF;
    Camera.Parameters parameters;

    //初始化相机
    private void initCamera() {
        if (parameters == null) {
            parameters = camera.getParameters();
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setJpegQuality(100);
            parameters.setJpegThumbnailQuality(100);
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
            Camera.Size size = getOptimalPreviewSize(sizeList, surfaceView.getWidth(), ScreenUtil.getScreenHeight(this));

            List<Camera.Size> picsizeList = parameters.getSupportedPictureSizes();
            Camera.Size picsize = getPicsize(picsizeList, 0.5625);
            parameters.setPreviewSize(size.width, size.height);
            parameters.setRotation(90);//生成的图片转90°
            parameters.setFlashMode(FlashMode);
            parameters.setPictureSize(picsize.width, picsize.height);
        }

        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        camera.startPreview();
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.2;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

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

    private Camera.Size getPicsize(List<Camera.Size> sizes, double targetRatio) {
        if (targetRatio > 1 || targetRatio <= 0)
            targetRatio = 0.75;
        if (sizes == null) return null;
        List<Camera.Size> Picsizes = new ArrayList<>();
        for (Camera.Size size : sizes) {
            if ((double) size.height / size.width == targetRatio) {
                Picsizes.add(size);
            }
        }
        if (Picsizes.size() == 0) return camera.new Size(1920, 1080);
        Collections.sort(Picsizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size o1, Camera.Size o2) {
                return o2.width - o1.width;
            }
        });
        return Picsizes.get(0);
    }

    byte[] data;

    private void savePicture() {
        String fileName = System.currentTimeMillis() + ".jpg";
        FileUtil.saveJPG(data, fileName, new SavePictureListener() {
            @Override
            public void onSuccess() {
                Message msg = Message.obtain();
                msg.what = save_pic;
                msg.obj = "保存成功";
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(String reason) {
                Message msg = Message.obtain();
                msg.what = save_pic;
                msg.obj = reason;
                mHandler.sendMessage(msg);
            }
        });
    }


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


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case only_auto_focus:
                    if (camera != null)
                        camera.autoFocus(myAutoFocusCallback1);
                    break;
                case save_pic:
                    ToastUtil.showShort((String) msg.obj);
                    break;
            }
        }
    };
    MyThread myThread;

    private class MyThread extends Thread {
        WeakReference<SurfaceCameraActivity3> mThreadActivityRef;

        public MyThread(SurfaceCameraActivity3 activity) {
            mThreadActivityRef = new WeakReference<SurfaceCameraActivity3>(
                    activity);
        }

        @Override
        public void run() {
            super.run();
            if (mThreadActivityRef == null)
                return;
            if (mThreadActivityRef.get() != null)
                savePicture();
        }
    }


    //----------------打开/关闭 闪光灯-------开始-------

    /**
     * 设置相机预览
     **/
    public void setCameraPreView() {
        if (camera != null) {
            camera.stopPreview();
        } else {
            openCamera();
        }
        initCamera();
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
                Glide.with(this).load(R.mipmap.flash_on).into(imageLigth);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void Openshoudian() {
        if (camera != null) {
            //打开闪光灯
            Camera.Parameters parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
            FlashMode = Camera.Parameters.FLASH_MODE_TORCH;
        }
    }

    //关闭闪光灯
    public void Closeshoudian() {
        if (camera != null) {
            FlashMode = Camera.Parameters.FLASH_MODE_OFF;
            if (parameters != null) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } else {
                camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(camera.getParameters());
            }
            setCameraPreView();
        }
    }
    //----------------打开/关闭 闪光灯-------结束-------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myThread != null) {
            myThread.interrupt();
            myThread = null;
        }
    }

}
