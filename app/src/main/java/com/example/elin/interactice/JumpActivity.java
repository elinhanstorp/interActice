package com.example.elin.interactice;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class JumpActivity extends AppCompatActivity implements SensorEventListener{

    private TextView nbrJump;
    private int currentNbrJumps = 0;
    private boolean currentPosUp = false;
    private Sensor mAccelerator;
    private SensorManager mSensorManager;
    private int nbrOfReps;
    private long startTime;
    private long endTime;

    private MediaPlayer gb;
    private MediaPlayer threeJumps;
    private MediaPlayer one;
    private MediaPlayer two;
    private MediaPlayer three;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);

        threeJumps = MediaPlayer.create(this, R.raw.threejumpsleft);
        gb = MediaPlayer.create(this, R.raw.goodjob4);
        one = MediaPlayer.create(this, R.raw.one);
        two = MediaPlayer.create(this, R.raw.two);
        three = MediaPlayer.create(this, R.raw.three);

        mSensorManager= (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerator=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        nbrJump = (TextView) findViewById(R.id.nbrJumps);
        mSensorManager.registerListener(this, mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);

        startTime = System.currentTimeMillis();
        nbrOfReps = getIntent().getIntExtra("JUMPS", 0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] accelerationValues = getValues(event);

        for (int i = 0; i < accelerationValues.length; i++) {
            if (i == 1) {
                Log.d(Integer.toString(i), Float.toString(accelerationValues[i]));
            }
        }

        if (currentPosUp) {
            if (detectDown(accelerationValues)) {
                currentPosUp = false;
            }
        } else {
            if (detectUp(accelerationValues)) {
                if (currentNbrJumps < nbrOfReps) {
                    currentPosUp = true;
                    currentNbrJumps++;
                    countWithMe(currentNbrJumps);
                    nbrJump.setText(Integer.toString(currentNbrJumps));
                    if (threeRepsLeft(currentNbrJumps, nbrOfReps)) {
                        threeJumps.start();
                    }
                    if (currentNbrJumps == nbrOfReps) {
                        nbrJump.setText("Good job");
                        gb.start();
                        endTime = System.currentTimeMillis();
                        Intent intent = new Intent();
                        intent.putExtra("TIMELEFT", endTime - startTime);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        }
    }

    private boolean detectDown(float[] values) {
        if(values[1]>4) {
            return true;
        }
        return false;
    }

    private boolean detectUp(float[] values) {
        if(values[1]<1.5){
            return true;
        }
        return false;
    }

    private float[] getValues(SensorEvent event){
        final float alpha = 0.8f;
        float[] gravity= new float[3];
        float[] linear_acceleration= new float[3];

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        return linear_acceleration;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(mAccelerator !=  null) {
            mSensorManager.registerListener(this, mAccelerator, SensorManager.SENSOR_DELAY_FASTEST);
        }else{
            Toast.makeText(this, "This is not supported", Toast.LENGTH_SHORT).show();

        }
    }

    public void reset(View view) {
        currentNbrJumps=0;
        nbrJump.setText(Integer.toString(currentNbrJumps));
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
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

    public boolean threeRepsLeft(int currentNbrJumps, int nbrOfReps) {
        int remaining = nbrOfReps - currentNbrJumps;
        return remaining == 3;
    }

    public void countWithMe(int currentNbrJumps){
        if(currentNbrJumps==1){
            one.start();
        }else if(currentNbrJumps==2){
            two.start();
        }else if(currentNbrJumps==3){
            three.start();
        }
    }
}

