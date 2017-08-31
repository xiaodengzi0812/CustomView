how to use :

    xml布局文件中 ：

    <com.dengzi.stepview.StepView
        android:id="@+id/step_view"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_gravity="center_horizontal"
        android:paddingLeft="10dp"

        custom:innerColor="@color/colorAccent"
        custom:outerColor="@color/colorPrimaryDark"
        custom:stepLineSize="10dp"
        custom:stepTextColor="@color/colorAccent"
        custom:stepTextSize="25sp" />


    activity中 ：

    private void initStepView() {
        StepView stepView = (StepView) findViewById(R.id.step_view);
        stepView.setStep(10000, 6000);
    }