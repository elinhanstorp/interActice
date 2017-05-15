package com.example.elin.interactice;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PushUpActivity extends AppCompatActivity implements SensorEventListener{
    private TextView nbrPushUp;
    private int currentNbrPushUp = 0;
    private boolean currentPosUp = false;
    private Sensor mAccelerator;
    private SensorManager mSensorManager;
    private int nbrOfReps;
    private long startTime;
    private long endTime;

    private MediaPlayer gb;
    private MediaPlayer threePush;
    private MediaPlayer one;
    private MediaPlayer two;
    private MediaPlayer three;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up);

        threePush = MediaPlayer.create(this, R.raw.threepushlefttodo);
        gb = MediaPlayer.create(this, R.raw.goodjob4);
        one = MediaPlayer.create(this, R.raw.one);
        two = MediaPlayer.create(this, R.raw.two);
        three = MediaPlayer.create(this, R.raw.three);

        gb = MediaPlayer.create(this, R.raw.goodjob4);
        mSensorManager= (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerator=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        nbrPushUp = (TextView) findViewById(R.id.nbrPushUps);
        mSensorManager.registerListener(this, mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);

        startTime = System.currentTimeMillis();
        nbrOfReps = getIntent().getIntExtra("REPS", 0);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] accelerationValues= getValues(event);

        for(int i=0; i<accelerationValues.length; i++){
            if(i==1) {
                Log.d(Integer.toString(i), Float.toString(accelerationValues[i]));
            }
        }

        if (currentPosUp) {
            if (detectDown(accelerationValues)) {
                currentPosUp = false;
            }
        } else {
            if (detectUp(accelerationValues)) {
                if (currentNbrPushUp < nbrOfReps) {
                    currentPosUp = true;
                    currentNbrPushUp++;
                    countWithMe(currentNbrPushUp);
                    nbrPushUp.setText(Integer.toString(currentNbrPushUp));
                    if (threeRepsLeft(currentNbrPushUp, nbrOfReps)) {
                        threePush.start();
                    }
                    if (currentNbrPushUp == nbrOfReps) {
                        nbrPushUp.setText("Good job");
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
        currentNbrPushUp=0;
        nbrPushUp.setText(Integer.toString(currentNbrPushUp));
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

    public boolean threeRepsLeft(int currentNbrPushUp, int nbrOfReps) {
        int remaining = nbrOfReps - currentNbrPushUp;
        return remaining == 3;
    }

    public void countWithMe(int currentNbrPushUp){
        if(currentNbrPushUp==1){
            one.start();
        }else if(currentNbrPushUp==2){
            two.start();
        }else if(currentNbrPushUp==3){
            three.start();
        }
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
                        Intent intent = new Intent(PushUpActivity.this, MainActivity.class);
                        startActivity(intent);

                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
