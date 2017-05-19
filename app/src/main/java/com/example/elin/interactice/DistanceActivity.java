package com.example.elin.interactice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class DistanceActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private TextView meter;
    boolean activityRunning;
    private int initCountValue = 0;
    public int meters;
    public long startTime;
    public long endTime;

    private MediaPlayer startRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        startRun = MediaPlayer.create(this, R.raw.startrunning);

        meter = (TextView) findViewById(R.id.meters);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        meters = getIntent().getIntExtra("DISTANCE", 0);
        startTime = System.currentTimeMillis();
        startRun.start();

        final GestureDetector gd = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                nextActivity(findViewById(android.R.id.content));
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gd.onTouchEvent(event);
            }
        });
    }

    @Override
    protected void onResume() {
        overridePendingTransition(0,0);
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }

    }

    @Override
    protected void onPause() {
        overridePendingTransition(0,0);
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
            double remaining = meters - steps*0.74;


            if(remaining <= 0){
                meter.setText("Well done!");
                endTime = System.currentTimeMillis();
                Intent intent = new Intent();
                intent.putExtra("TIMELEFT", endTime - startTime);
                setResult(RESULT_OK, intent);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }else{
                meter.setText("Number of meters left:  " + String.valueOf((int)remaining));
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void nextActivity(View view) {
        endTime = System.currentTimeMillis();
        Intent intent = new Intent();
        intent.putExtra("TIMELEFT", endTime - startTime);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        String msg = "Are you sure you want to end this workout?";
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage(msg)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(DistanceActivity.this, MainActivity.class);
                                startActivity(intent);

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
    }

}
