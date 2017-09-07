package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dengzi.stepview.StepView;

/**
 * @author Djk
 * @Title: 运动app中步数
 * @Time: 2017/9/4.
 * @Version:1.0.0
 */
public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_activity);
        initStepView();
        initStepView2();
    }

    private void initStepView() {
        StepView stepView = (StepView) findViewById(R.id.step_view);
        stepView.setStep(10000, 6000);
    }

    private void initStepView2() {
        StepView stepView = (StepView) findViewById(R.id.step_view2);
        stepView.setStep(10000, 6000);
    }

}
