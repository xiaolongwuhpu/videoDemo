package com.video.longwu.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.video.longwu.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class testSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private int painStrokeWidth = 10;

    private List<DrawPath> savePath;
    private List<DrawPath> deletePath;
    private DrawPath dp;

    public testSurfaceView(Context context) {
        this(context, null);
    }

    public testSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public testSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //路径对象
    class DrawPath {
        Path path;
    }

    private boolean isRunning = true;
    private boolean isThreadOpening = true;

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        mPaint.setStrokeWidth(painStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }

    @Override
    public void run() {
        while (isRunning) {
            draw();
        }
    }

    private void draw() {
        try {
            mCanvas = holder.lockCanvas();
            if (mCanvas == null) {
                return;
            }
            mCanvas.drawColor(getResources().getColor(R.color.white));
            mCanvas.drawPath(mPath, mPaint);
            //将路径保存列表中的路径重绘在画布上
            for (int i = 0; i < savePath.size(); i++) {
                mCanvas.drawPath(savePath.get(i).path, mPaint);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } finally {
            stopThread();
            if (holder != null)
                holder.unlockCanvasAndPost(mCanvas);
        }
    }

    Thread myThread = null;

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            draw();
        }
    };


    public void stopThread() {
        if (myThread != null) {
            myThread.interrupt();
            myThread = null;
        }
        isThreadOpening = false;
    }

    public void startThread() {
        if (myThread == null) {
            myThread = new Thread(myRunnable);
        }
        if (!isThreadOpening) {
            myThread.start();
            isThreadOpening = true;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    /**
     * 重置画布
     */
    public void ressetCanvas() {
        Iterator<DrawPath> iterator = savePath.iterator();
        while (iterator.hasNext()) {
            iterator.next().path.reset();
        }
    }

    /**
     * 撤销的核心思想就是将画布清空，
     * 将保存下来的Path路径最后一个移除掉，
     * 重新将路径画在画布上面。
     */
    public void undo() {
        System.out.println(savePath.size() + "--------------");
        if (savePath != null && savePath.size() > 0) {
            //调用初始化画布函数以清空画布
            initCanvas();
            //将路径保存列表中的最后一个元素删除 ,并将其保存在路径删除列表中
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            if (savePath.contains(drawPath)) {
                savePath.remove(savePath.size() - 1);
            }
            if (!deletePath.contains(drawPath)) {
                deletePath.add(drawPath);
            }
            startThread();
        }
    }

    /**
     * 恢复的核心思想就是将撤销的路径保存到另外一个列表里面(栈)，
     * 然后从redo的列表里面取出最顶端对象，
     * 画在画布上面即可
     */
    public void redo() {
        if (deletePath.size() > 0) {
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath drawPath = deletePath.get(deletePath.size() - 1);
            mPath = drawPath.path;
//            savePath.add(drawPath);
            if (!savePath.contains(drawPath)) {
                savePath.add(drawPath);
            }
            if (deletePath.contains(drawPath)) {
                //将该路径从删除的路径列表中去除
                deletePath.remove(deletePath.size() - 1);
            }
            startThread();
        }
    }

    public void initCanvas() {
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                dp = new DrawPath();
                dp.path = mPath;
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
        }
        return true;
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        savePath.add(dp);
    }
}
