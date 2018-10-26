package com.video.longwu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.video.longwu.R;

public class testpath extends View {
    private int painStrokeWidth = 10;
    private Paint mPaint, mTextPaint, mPointPaint;
    private Path mPath;

    public testpath(Context context) {
        this(context, null);
    }

    public testpath(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public testpath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        mPaint.setStrokeWidth(painStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStrokeWidth(painStrokeWidth - 9);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(40);
        mTextPaint.setTypeface(Typeface.SERIF);
        mTextPaint.setStyle(Paint.Style.STROKE);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.GREEN);
        mPointPaint.setStrokeWidth(painStrokeWidth + 10);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 使用moveTo（）
        // 起点默认是(0,0)
        //连接点(400,500)
    /*    mPath.lineTo(400, 500);
        drawtext(canvas,400,500);
        // 将当前点移动到(300, 300)
        mPath.moveTo(300, 300) ;
        drawtext(canvas,300,300);
        //连接点(900, 800)
        mPath.lineTo(900, 800);
        drawtext(canvas,900,800);
//        mPath.lineTo(200, 700);
//        drawtext(canvas,200,700);
        // 闭合路径，即连接当前点和起点
        // 即连接(200,700)与起点2(300, 300)
        // 注:此时起点已经进行变换
        mPath.close();

        // 画出路径
        canvas.drawPath(mPath, mPaint);*/

// 使用setLastPoint（）
// 起点默认是(0,0)
        //连接点(400,500)
 /*       mPath.lineTo(400, 500);
        drawtext(canvas,400,500);
        // 将当前点移动到(300, 300)
        // 会影响之前的操作
        // 但不将此设置为新起点
        mPath.setLastPoint(200, 300);
        drawtext(canvas,200,300);
        //连接点(900,800)
        mPath.lineTo(900, 800);
        drawtext(canvas,900,800);
        //连接点(200,700)
        mPath.lineTo(200, 700);
        drawtext(canvas,200,700);
        // 闭合路径，即连接当前点和起点
        // 即连接(200,700)与起点(0，0)
        // 注:起点一直没变化
        mPath.close();

        // 画出路径
        canvas.drawPath(mPath, mPaint);*/



   /*     // 轨迹2
        // 将Canvas坐标系移到屏幕正中
        canvas.translate(400,500);
        // 起点是(0,0)，连接点(-100,0)
        mPath.lineTo(-100,0);
        // 画圆：圆心=(0,0)，半径=100px
        // 此时路径起点改变 = (0,100)（记为起点2）
        // 起点改变原则：新画图形在x轴正方向的最后一个坐标
        // 后面路径的变化以这个点继续下去
        mPath.addCircle(0,0,100, Path.Direction.CCW);

        // 起点为：(0,100)，连接 (-100,200)
        mPath.lineTo(-100,200);
        // 连接 (200,200)
        mPath.lineTo(200,200);

        // 闭合路径，即连接当前点和起点（注：闭合的是起点2）
        // 即连接(200,200)与起点2(0,100)
        mPath.close();
        // 画出路径
        canvas.drawPath(mPath,mPaint);*/



        // 将Canvas坐标系移到屏幕正中
     /*   canvas.translate(400,500);
        mPath.lineTo(50,200);
        RectF rectF = new RectF(180,180,330,300);
        //是否将之前路径的结束点设置为圆弧起点
        // true：在新的起点画圆弧，不连接最后一个点与圆弧起点，即与之前路径没有交集（同addArc（））
        // false：在新的起点画圆弧，但会连接之前路径的结束点与圆弧起点，即与之前路径有交集（同arcTo（3参数））
//        mPath.addArc(rectF,0,220);  //默认true
        mPath.arcTo(rectF,0,180);//默认false
        canvas.drawPath(mPath,mPaint);*/




        // FillType － 4种填充模式
//        1.Path的默认FillType为 FillType.WINDING；
//        2.作用的范围为绘制 Path 的 Canvas 整体，而非 path 所在区域；
//        3.FillType.WINDING：取path所有所在区域；
//        4.FillType.EVEN_ODD：取path所在并不相交区域；
//        5.FillType.INVERSE_WINDING：取path所有未占区域；
//        6.FillType.INVERSE_EVEN_ODD：取path未占或相交区域；
    /*    mPath = new Path();
        mPath.addCircle(300, 300, 150, Path.Direction.CW);
        mPath.addCircle(380, 380, 150, Path.Direction.CW);
//        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        mPath.setFillType(Path.FillType.EVEN_ODD);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath,mPaint);*/



        //Path.Op.DIFFERENCE        表示从path中去除path2的部分，保留path的部分。如下案例：
        //Path.Op.INTERSECT         表示取path和path2相交的部分显示出来
        //Path.Op.REVERSE_DIFFERENCE表示除去path的部分，只显示path2的部分
        //Path.Op.UNION             表示path和path2的部分都要显示出来
        //Path.Op.XOR               表示显示path和path2但是不包含二者的交集
        Path mPath1 = new Path();
        Path mPath2 = new Path();
        mPath1.addCircle(300, 300, 150, Path.Direction.CW);
        mPath2.addCircle(380, 380, 150, Path.Direction.CW);
        mPath1.op(mPath2, Path.Op.DIFFERENCE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath1, mPaint);


    }

    void drawtext(Canvas canvas, int x, int y) {
//        Rect rect = new Rect();
        String str = x + "," + y;
        canvas.drawText(str, x, y, mTextPaint);
        canvas.drawPoint(x, y, mPointPaint);
    }
}
