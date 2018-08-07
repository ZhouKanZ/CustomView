package com.zkqueen.customview.widget;

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
import android.view.MotionEvent;
import android.view.View;

import com.zkqueen.customview.R;

/**
 * Created by zhoukan on 2018/8/6.
 *
 * @desc:
 */
public class AngleSelector extends View {

    private static final String TAG = "AngleSelector";

    private float ringStartAngle = 135;
    private float ringSweepAngle = 270;

    // 缺省值
    private float startAngle = 135;
    private float relativeStartAngle = 0;
    private float endAngle = 335;
    private float relativeEndAngle = 200;
    private float sweepAngle;

    private int firstRingWidth = 56;

    private RectF rectF1;
    private RectF rectF2;
    private RectF rectF3;

    // 响应
    private float halfWidth = 15;
    private RectF firstTouchArea = new RectF();
    private RectF sencondTouchArea = new RectF();


    private Paint paint, rectPaint;
    private Paint boundPaint;
    private Bitmap pointer;
    private Matrix matrix;

    private int draggable = -1; // 0 表示第一个区间  1表示第二个区间
    private float[] lastPos = new float[2];
    private float[] currentPos = new float[2];


    public AngleSelector(Context context) {
        this(context, null);
    }

    public AngleSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngleSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        rectF1 = new RectF();
        rectF1.left = 10;
        rectF1.right = 410;
        rectF1.top = 10;
        rectF1.bottom = 410;

        rectF2 = new RectF();
        rectF2.left = 44;
        rectF2.right = 376;
        rectF2.top = 44;
        rectF2.bottom = 376;

        rectF3 = new RectF();
        rectF3.left = 91;
        rectF3.right = 329;
        rectF3.top = 91;
        rectF3.bottom = 329;


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.WHITE);

        boundPaint = new Paint();
        boundPaint.setAntiAlias(true);
        boundPaint.setStyle(Paint.Style.STROKE);
        boundPaint.setStrokeWidth(1);
        boundPaint.setColor(Color.RED);


        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(1);
        rectPaint.setColor(Color.RED);

        matrix = new Matrix();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        pointer = BitmapFactory.decodeResource(getResources(), R.mipmap.point, options);


    }

    private void buildTouchRectArea(float angle, RectF rectF) {
        float[] aimPos = new float[2];
        // x
        aimPos[0] = (float) (210 + 200 * Math.cos(angle * Math.PI / 180));
        // y
        aimPos[1] = (float) (210 + 200 * Math.sin(angle * Math.PI / 180));
        rectF.left = aimPos[0] - halfWidth;
        rectF.top = aimPos[1] - halfWidth;
        rectF.right = aimPos[0] + halfWidth;
        rectF.bottom = aimPos[1] + halfWidth;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        buildTouchRectArea(startAngle, firstTouchArea);
        buildTouchRectArea(endAngle, sencondTouchArea);

        // 绘制指定角度圆弧
        // canvas.drawRect(getBound(rectF1,1),boundPaint);
        paint.setColor(Color.WHITE);
        canvas.drawArc(rectF1, ringStartAngle, ringSweepAngle, false, paint);

        // sweep angle
        paint.setStrokeWidth(firstRingWidth);
        // todo first draw right arc
        paint.setColor(Color.RED);

        canvas.drawArc(rectF2, relativeStartAngle < endAngle ? startAngle:endAngle, Math.abs(relativeEndAngle - relativeStartAngle), false, paint);

        // rest angle
        paint.setColor(Color.parseColor("#5a595e"));
//        canvas.drawArc(rectF2, endAngle, 405 - endAngle, false, paint);

        paint.setColor(Color.parseColor("#419fff"));
        paint.setStrokeWidth(4);
        canvas.drawCircle(210, 210, 130, paint);

        paint.setColor(Color.parseColor("#5a595e"));
        paint.setStrokeWidth(20);
        canvas.drawArc(rectF3, startAngle, endAngle - startAngle, false, paint);

        paint.setColor(Color.parseColor("#2e2e2e"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(210, 210, 42, paint);

        paint.setColor(Color.parseColor("#383a3f"));
        canvas.drawCircle(210, 210, 32, paint);


        paint.setColor(Color.parseColor("#f3c02f"));
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(210, 210, 14, paint);

        rotate(startAngle);
        canvas.drawBitmap(pointer, matrix, paint);

        rotate(endAngle);
        canvas.drawBitmap(pointer, matrix, paint);

        canvas.drawRect(firstTouchArea, rectPaint);
        canvas.drawRect(sencondTouchArea, rectPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 判断点是否在可以触发拖动事件内
                Log.d(TAG, "onTouchEvent: x" + x + "y" + y);
                draggable = isDragged(x, y);
                Log.d(TAG, "onTouchEvent: down" + draggable);

                lastPos[0] = x;
                lastPos[1] = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算拖动的距离，换算成角度并更新ui
                if (draggable != -1) {
                    currentPos[0] = x;
                    currentPos[1] = y;
                    calcDistance(currentPos, draggable);
                    lastPos[0] = x;
                    lastPos[1] = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                draggable = -1;
                break;
        }
        return true;
    }

    private void calcDistance(float[] currentPos, int type) {

        float angle;

        double nowAngle = Math.atan2((currentPos[1] - 210), (currentPos[0] - 210));
        double sweepAngle = Math.toDegrees(nowAngle);
        // (-PI,0]
        if (sweepAngle <= 0) {
            angle = (float) (360 + sweepAngle);
            // [0,PI]
        } else {
            angle = (float) sweepAngle;
        }

        if (type == 0){
            startAngle = angle;
            // 得到相对坐标
            relativeStartAngle = getRelativeCoodinator(startAngle);
        }else if (type == 1){
            endAngle = angle;
            relativeEndAngle = getRelativeCoodinator(endAngle);
            // 得到相对坐标
        }
        invalidate();
    }

    private float getRelativeCoodinator(float absAngle) {
        if (absAngle<90){
            return  225+absAngle;
        }else {
            return absAngle;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = 500;
        int desiredHeight = 500;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {        // 确认值
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) { // wrapcontent / matchparent
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);

    }

    private void rotate(float angle) {
        matrix.reset();
        matrix.postTranslate((float) (210 + 14 * Math.cos(angle * Math.PI / 180)), (float) (210 + 14 * Math.sin(angle * Math.PI / 180) - pointer.getHeight() / 2));
        // 旋转指定的角度，并以圆环上的角度作为
        matrix.postRotate(angle, (float) (210 + 14 * Math.cos(angle * Math.PI / 180)), (float) (210 + 14 * Math.sin(angle * Math.PI / 180)));
        // touchable area had been sure
    }

    /**
     * 是否是可拖动区域
     *
     * @param x
     * @param y
     * @return
     */
    private int isDragged(float x, float y) {

        int area = -1;
        if (firstTouchArea.contains(x, y)) {
            area = 0;
        } else if (sencondTouchArea.contains(x, y)) {
            area = 1;
        }

        return area;
    }
}
