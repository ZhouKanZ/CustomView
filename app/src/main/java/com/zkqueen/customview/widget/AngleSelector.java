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

import java.text.DecimalFormat;

/**
 * Created by zhoukan on 2018/8/6.
 *
 * @desc:
 */
public class AngleSelector extends View {

    private static final String TAG = "AngleSelector";

    private float ringStartAngle = 135;
    private float ringSweepAngle = 270;
    private float unableArea = 90;

    // 缺省值
    private float startAngle = 135;
    private float relativeStartAngle = 0;
    private float endAngle = 335;
    private float relativeEndAngle = 200;
    private float sweepAngle;
    // view的常规属性
    private float laggerRadius = 200;
    private float sencodRadius = 166;
    private float thirdRadius = 119;
    private int padding = 50;

    private int textSize = 16;
    private int textContentPaddingTop = 3;
    private int textContentPaddingleft = 5;


    private int firstRingWidth = 56;

    private RectF rectF1;
    private RectF rectF2;
    private RectF rectF3;

    // 响应
    private float halfWidth = 15;
    private RectF firstTouchArea = new RectF();
    private RectF sencondTouchArea = new RectF();


    private Paint paint, rectPaint, textPaint, anglePaint;
    private Paint boundPaint;
    private Bitmap pointer;
    private Matrix matrix;

    private int draggable = -1; // 0 表示第一个区间  1表示第二个区间
    private int rotateDir = -1; // -1表示不动 0 表示角度增大的方向 1表示角度减小的方向
    private float[] lastPos = new float[2];
    private float[] currentPos = new float[2];
    private Paint.FontMetrics fm1, fm2;
    private DecimalFormat df = new DecimalFormat("0.0");


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
        rectF1.left = padding;
        rectF1.right = padding + laggerRadius * 2;
        rectF1.top = padding;
        rectF1.bottom = padding + laggerRadius * 2;

        rectF2 = new RectF();
        rectF2.left = padding + laggerRadius - sencodRadius;
        rectF2.right = padding + laggerRadius + sencodRadius;
        rectF2.top = padding + laggerRadius - sencodRadius;
        rectF2.bottom = padding + laggerRadius + sencodRadius;

        rectF3 = new RectF();
        rectF3.left = padding + laggerRadius - thirdRadius;
        rectF3.right = padding + laggerRadius + thirdRadius;
        rectF3.top = padding + laggerRadius - thirdRadius;
        rectF3.bottom = padding + laggerRadius + thirdRadius;


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.WHITE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(16);
        textPaint.setColor(Color.parseColor("#4f4e55"));
//        textPaint.setColor(Color.WHITE);

        boundPaint = new Paint();
        boundPaint.setAntiAlias(true);
        boundPaint.setStyle(Paint.Style.FILL);
        boundPaint.setStrokeWidth(1);
        boundPaint.setColor(Color.WHITE);


        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(1);
        rectPaint.setColor(Color.RED);

        anglePaint = new Paint();
        anglePaint.setAntiAlias(true);
        anglePaint.setStyle(Paint.Style.FILL);
        anglePaint.setTextSize(38);
        anglePaint.setColor(Color.WHITE);

        matrix = new Matrix();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        pointer = BitmapFactory.decodeResource(getResources(), R.mipmap.point, options);

        fm1 = textPaint.getFontMetrics();


    }

    private void buildTouchRectArea(float angle, RectF rectF) {
        float[] aimPos = new float[2];
        // x
        aimPos[0] = (float) (padding + laggerRadius + laggerRadius * Math.cos(angle * Math.PI / 180));
        // y
        aimPos[1] = (float) (padding + laggerRadius + laggerRadius * Math.sin(angle * Math.PI / 180));
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

        // paint.setColor(Color.RED);
        // paint.setStrokeWidth(2);
        // canvas.drawRect(rectF1,paint);

        // sweep angle
        paint.setStrokeWidth(firstRingWidth);
        paint.setColor(Color.parseColor("#5a595e"));
        canvas.drawArc(rectF2, ringStartAngle, ringSweepAngle, false, paint);

        // todo first draw right arc
        paint.setColor(Color.WHITE);
        canvas.drawArc(rectF2, relativeStartAngle < relativeEndAngle ? startAngle : endAngle, Math.abs(relativeEndAngle - relativeStartAngle), false, paint);
        // paint.setColor(Color.RED);
        // paint.setStrokeWidth(2);
        // canvas.drawRect(rectF2,paint);
        // rest angle

        paint.setColor(Color.parseColor("#419fff"));
        paint.setStrokeWidth(4);
        canvas.drawCircle(padding + laggerRadius, padding + laggerRadius, 130, paint);

        paint.setColor(Color.parseColor("#5a595e"));
        paint.setStrokeWidth(20);
        canvas.drawArc(rectF3, relativeStartAngle < relativeEndAngle ? startAngle : endAngle, Math.abs(relativeEndAngle - relativeStartAngle), false, paint);

        paint.setColor(Color.parseColor("#2e2e2e"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(padding + laggerRadius, padding + laggerRadius, 42, paint);

        paint.setColor(Color.parseColor("#383a3f"));
        canvas.drawCircle(padding + laggerRadius, padding + laggerRadius, 32, paint);

        paint.setColor(Color.parseColor("#f3c02f"));
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(padding + laggerRadius, padding + laggerRadius, 14, paint);

        rotate(startAngle);
        canvas.drawBitmap(pointer, matrix, paint);

        rotate(endAngle);
        canvas.drawBitmap(pointer, matrix, paint);

        // canvas.drawRect(firstTouchArea, rectPaint);
        // canvas.drawRect(sencondTouchArea, rectPaint);
        // canvas.drawCircle(padding + laggerRadius,padding + laggerRadius, laggerRadius+30,paint);

        RectF rectF = new RectF();
        for (int i = 0; i < 360; i = i + 45) {
            String angle = String.valueOf(i - 135 < 0 ? 360 + i - 135 : i - 135);
            float[] pos = getStartPosition(i);
            float w = textPaint.measureText(angle);
            rectF.left = pos[0] - w / 2 - textContentPaddingleft;
            rectF.top = pos[1] + fm1.top - textContentPaddingTop;
            rectF.bottom = pos[1] + fm1.bottom + textContentPaddingTop;
            rectF.right = pos[0] - w / 2 + w + textContentPaddingleft;
            if (i < 135 && i > 45) {
                continue;
            }
            canvas.drawRoundRect(rectF, 5, 5, boundPaint);
            canvas.drawText(angle, pos[0] - w / 2, pos[1], textPaint);
        }

        String angleDiff = String.valueOf(df.format(Math.abs(relativeEndAngle - relativeStartAngle))) + "°";
        float[] centerText = getStartPosition(90);
        float m = anglePaint.measureText(angleDiff);
        canvas.drawText(angleDiff, centerText[0] - m / 2, centerText[1] - 30, anglePaint);

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

    //    /**
    //     *  得到旋转方向
    //     * @param currentPos
    //     * @return
    //     */
    //    private void getRotateDir(float[] currentPos) {
    //        double nowAngle = Math.atan2((currentPos[1] - 210), (currentPos[0] - 210));
    //        double lastAngle = Math.atan2((lastPos[1] - 210), (lastPos[0] - 210));
    //        if (nowAngle - lastAngle > 0){
    //            rotateDir = 0;
    //        }else {
    //            rotateDir = 1;
    //        }
    //    }

    private void calcDistance(float[] currentPos, int type) {

        float angle;

        double nowAngle = Math.atan2((currentPos[1] - padding - laggerRadius), (currentPos[0] - padding - laggerRadius));
        double sweepAngle = Math.toDegrees(nowAngle);

        if (sweepAngle <= 0) {
            angle = (float) (360 + sweepAngle);
        } else {
            angle = (float) sweepAngle;
        }

        if (angle >= 45 && angle <= 135) {
            return;
        }

        if (type == 0) {
            startAngle = angle;
            // 得到相对坐标
            relativeStartAngle = getRelativeCoodinator(startAngle);
            Log.d(TAG, "calcDistance: " + relativeStartAngle);
        } else if (type == 1) {
            endAngle = angle;
            relativeEndAngle = getRelativeCoodinator(endAngle);
        }

        invalidate();
    }

    private float[] getStartPosition(int i) {
        //    200 ， 200 170
        float[] pos = new float[2];
        pos[0] = (float) (padding + laggerRadius + (laggerRadius + 30) * Math.cos(i * Math.PI / 180));
        pos[1] = (float) (padding + laggerRadius + (laggerRadius + 30) * Math.sin(i * Math.PI / 180));
        return pos;
    }

    private float getRelativeCoodinator(float absAngle) {
        if (absAngle < 90) {
            return 180 + (360 - ringSweepAngle) / 2 + absAngle;
        } else {
            return absAngle - ringStartAngle;
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
        matrix.postTranslate((float) (padding + laggerRadius + 14 * Math.cos(angle * Math.PI / 180)), (float) (padding + laggerRadius + 14 * Math.sin(angle * Math.PI / 180) - pointer.getHeight() / 2));
        // 旋转指定的角度，并以圆环上的角度作为
        matrix.postRotate(angle, (float) (padding + laggerRadius + 14 * Math.cos(angle * Math.PI / 180)), (float) (padding + laggerRadius + 14 * Math.sin(angle * Math.PI / 180)));
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
