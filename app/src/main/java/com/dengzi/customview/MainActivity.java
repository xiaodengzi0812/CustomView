package com.dengzi.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.dengzi.stepview.StepView;
import com.dengzi.trackcolor.TrackColorTextView;

public class MainActivity extends AppCompatActivity {

    TrackColorTextView trackColorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initStepView();
        trackColorTextView = (TrackColorTextView) findViewById(R.id.track);
    }

    private void initStepView() {
        StepView stepView = (StepView) findViewById(R.id.step_view);
        stepView.setStep(10000, 6000);
    }

    public void leftToRight(View view) {
        trackColorTextView.setDirection(TrackColorTextView.Direction.LEFT_TO_RIGHT);
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                trackColorTextView.setProgress(value);
            }
        });
        animator.start();
    }

    public void rightToLeft(View view) {
        trackColorTextView.setDirection(TrackColorTextView.Direction.RIGHT_TO_LEFT);
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                trackColorTextView.setProgress(value);
            }
        });
        animator.start();
    }

}
