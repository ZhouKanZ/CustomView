package com.zkqueen.customview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zkqueen.customview.R;

/**
 * Created by zhoukan on 2018/8/7.
 *
 * @desc: bitmap 从圆心绘制 并响应事件
 */

public class RotateView extends View {

    private Bitmap pointer;
    private Paint paint,textPaint;
    private RectF fitRectf;
    private Matrix matrix;
    private BitmapFactory.Options options;


    public RotateView(Context context) {
        this(context, null);
    }

    public RotateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(2);
        textPaint.setColor(Color.parseColor("#4f4e55"));
        textPaint.setTextSize(16);

        options = new BitmapFactory.Options();
        options.inScaled = false;
        pointer = BitmapFactory.decodeResource(getResources(), R.mipmap.point, options);
        fitRectf = new RectF();
        matrix = new Matrix();

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(200, 200, 150, paint);
        canvas.drawCircle(200, 200, 50, paint);

        // 1.angle = 0
        matrix.reset();
        matrix.postTranslate((float) (200 + 50 * Math.cos(0)), (float) (200 + 50 * Math.sin(0) - pointer.getHeight() / 2));
        matrix.postRotate(0);
        canvas.drawBitmap(pointer, matrix, null);

        // 2.45
        matrix.reset();
        // 移动到指定的位置
        matrix.postTranslate((float) (200 + 50 * Math.cos(Math.PI / 4)), (float) (200 + 50 * Math.sin(Math.PI / 4) - pointer.getHeight() / 2));
        // 旋转指定的角度，并以圆环上的角度作为
        matrix.postRotate(45, (float) (200 + 50 * Math.cos(Math.PI / 4)), (float) (200 + 50 * Math.sin(Math.PI / 4)));
        canvas.drawBitmap(pointer, matrix, null);

//        canvas.drawCircle(200, 200, 170, paint);

        // drawText around circle 45° 步进值
        for (int i = 0; i < 360; i = i+45) {
            float[] startPos = getStartPosition(i);
            String str = String.valueOf(i);
            canvas.drawText(str,startPos[0] - 10,startPos[1],textPaint);
            canvas.drawLine(startPos[0],startPos[1],200,200,paint);
        }

        String text = "TextView";

        Paint.FontMetrics fm = paint.getFontMetrics();
        float w = paint.measureText(text);
        RectF rectF = new RectF();
        rectF.left = 30 - 5;
        rectF.top  = 30 + fm.top;
        rectF.bottom = 30 + fm.bottom;
        rectF.right = 30 + w + 5;
        paint.setColor(Color.GREEN);
        canvas.drawRect(rectF,paint);


        Log.d("xxx", "onDraw: " + fm.top);
        Log.d("xxx", "onDraw: " + fm.ascent);
        Log.d("xxx", "onDraw: " + fm.descent);
        Log.d("xxx", "onDraw: " + fm.bottom);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        canvas.drawText(text,30,30,paint);



    }

    private float[] getStartPosition(int i) {
        //    200 ， 200 170
        float[] pos = new float[2];
        pos[0] = (float) (200 + 180*Math.cos(i*Math.PI / 180));
        pos[1] = (float) (200 + 180*Math.sin(i*Math.PI / 180));
        return pos;
    }

}
