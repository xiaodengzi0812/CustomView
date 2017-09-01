package com.dengzi.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dengzi.stepview.StepView;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_activity);
        initStepView();
    }

    private void initStepView() {
        StepView stepView = (StepView) findViewById(R.id.step_view);
        stepView.setStep(10000, 6000);
    }

}
