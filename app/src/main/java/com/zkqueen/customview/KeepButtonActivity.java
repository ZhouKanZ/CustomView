package com.zkqueen.customview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class KeepButtonActivity extends AppCompatActivity {


    Button btn2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep);
        btn2 = findViewById(R.id.btn2);

        // onclick start animation
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int me = event.getAction();
                switch (me){
                    case MotionEvent.ACTION_DOWN:
                        // start scale Animtion
                        enlarge();
                        break;
                    case MotionEvent.ACTION_UP:
                        // start scale animation
                        zoomOut();
                        break;
                }
                return false;
            }
        });

    }

    private void zoomOut() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.2f,0);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            btn2.setScaleX(value);
            btn2.setScaleY(value);
        }
    });
        valueAnimator.start();
}


    private void enlarge(){

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1,1.2f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                btn2.setScaleX(value);
                btn2.setScaleY(value);
            }
        });
        valueAnimator.start();
    }
}
