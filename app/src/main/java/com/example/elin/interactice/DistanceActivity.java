package com.example.elin.interactice;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DistanceActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private TextView meter;
    boolean activityRunning;
    private int initCountValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        meter = (TextView) findViewById(R.id.meters);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {

            if(initCountValue < 1){
                initCountValue = (int)event.values[0];
            }

            //Since it will return the total number since we registered, we need to subtract the initial amount of steps
            int steps = ((int)event.values[0]) - initCountValue;
            int meters = 20;
            double remaining = meters - steps*0.74;


            if(remaining <= 0){
                meter.setText("Well done!");
            }else{
                meter.setText("Number of meters left:  " + String.valueOf((int)remaining));
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
