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
import android.view.View;

import com.zkqueen.customview.R;

/**
 * Created by zhoukan on 2018/8/7.
 *
 * @desc: bitmap 从圆心绘制 并响应事件
 */

public class RotateView extends View {

    private Bitmap pointer;
    private Paint paint;
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
    }

}
