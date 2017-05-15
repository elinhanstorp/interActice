package com.example.elin.interactice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class JumpActivity extends AppCompatActivity implements SensorEventListener {
    private TextView nbrJump;
    private TextView showrel0;
    private TextView showrel1;
    private TextView showrel2;
    private TextView showmag0;
    private TextView showmag1;
    private TextView showmag2;
    /*private TextView showgrav0;
    private TextView showgrav1;
    private TextView showgrav2;*/
    private int currentNbrJumps = 0;
    private int reps=9;
    private boolean currentPosUp = false;
    private Sensor mAccelerator;
    private Sensor mMagnetic;
    private Sensor mGravity;
    private SensorManager mSensorManager;
    private float[] gravityValues = new float[3];
    private float[] magneticValues = new float[3];

    //Soundfiles
    private MediaPlayer gb;
    private MediaPlayer threeJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);

        gb = MediaPlayer.create(this, R.raw.goodjob4);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mAccelerator = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);

        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);

        nbrJump = (TextView) findViewById(R.id.nbrJumps);
        showrel0 = (TextView) findViewById(R.id.showrel0);
        showrel1 = (TextView) findViewById(R.id.showrel1);
        showrel2 = (TextView) findViewById(R.id.showrel2);
        showmag0 = (TextView) findViewById(R.id.showmag0);
        showmag1 = (TextView) findViewById(R.id.showmag1);
        showmag2 = (TextView) findViewById(R.id.showmag2);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.8f;
        float[] gravity = new float[3];

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // Remove the gravity contribution with the high-pass filter.
            gravityValues[0] = event.values[0] - gravity[0];
            gravityValues[1] = event.values[1] - gravity[1];
            gravityValues[2] = event.values[2] - gravity[2];
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticValues[0] = event.values[0];
            magneticValues[1] = event.values[1];
            magneticValues[2] = event.values[2];
            showmag0.setText(Float.toString(magneticValues[0]));
            showmag1.setText(Float.toString(magneticValues[1]));
            showmag2.setText(Float.toString(magneticValues[2]));
        }

        if ((gravityValues != null) && (magneticValues != null)
                && (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)) {
            float[] deviceRelativeAcceleration = new float[4];
            deviceRelativeAcceleration[0] = event.values[0];
            deviceRelativeAcceleration[1] = event.values[1];
            deviceRelativeAcceleration[2] = event.values[2];
            deviceRelativeAcceleration[3] = 0;

            showrel0.setText(Float.toString(deviceRelativeAcceleration[0]));
            showrel1.setText(Float.toString(deviceRelativeAcceleration[1]));
            showrel2.setText(Float.toString(deviceRelativeAcceleration[2]));

            // Change the device relative acceleration values to earth relative values
            // X axis -> East
            // Y axis -> North Pole
            // Z axis -> Sky

            float[] R = new float[16], I = new float[16], earthAcc = new float[16];

            SensorManager.getRotationMatrix(R, I, gravityValues, magneticValues);

            float[] inv = new float[16];

            android.opengl.Matrix.invertM(inv, 0, R, 0);
            android.opengl.Matrix.multiplyMV(earthAcc, 0, inv, 0, deviceRelativeAcceleration, 0);

            if (currentPosUp) {
                if (detectDown(deviceRelativeAcceleration)) {
                    currentPosUp = false;
                }
            } else {
                if (detectUp(deviceRelativeAcceleration)) {
                    currentPosUp = true;
                    //Only count up if there are reps remaining.
                    if(!repsDone(reps)) {
                        currentNbrJumps++;
                        nbrJump.setText(Integer.toString(currentNbrJumps));
                    }
                    //If there are three reps left of the activity, let me know!
                    if(threeRepsLeft(currentNbrJumps, reps)){
                        threeJump.start();
                    }
                    // Notice me when Im done
                    if (currentNbrJumps == reps) {
                        nbrJump.setText("Good job");
                        gb.start();
                    }
                }
            }
        }
    }


    private boolean detectDown(float[] values) {
        if (values[2] > 15) {
            return true;
        }
        return false;
    }

    private boolean detectUp(float[] values) {
        if (values[2] < -3) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAccelerator != null) {
            mSensorManager.registerListener(this, mAccelerator, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "This is not supported", Toast.LENGTH_SHORT).show();

        }
    }

    public void reset(View view) {
        currentNbrJumps = 0;
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

    public void countWithMe(int currentNbrJumps) {
        if (currentNbrJumps == 1) {
        } else if (currentNbrJumps == 2) {
        } else if (currentNbrJumps == 3) {

        }
    }

    public boolean threeRepsLeft(int currentNbrJumps, int reps) {
        int remaining=reps-currentNbrJumps+1;
        if(remaining== 3){
            return true;
        }
        return false;
    }

    public boolean repsDone(int reps){
        if(currentNbrJumps>reps){
            return true;
        }
        return false;
    }



    /*Denna är antagligen fel, välj annan vid merge
    public void goToPushUps(View view) {
        Intent intent;
        intent = new Intent(this, PushUpActivity.class);
        startActivity(intent);
    }*/
}