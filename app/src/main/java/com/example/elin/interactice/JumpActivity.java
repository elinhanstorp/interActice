package com.example.elin.interactice;

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
import android.widget.Toast;

public class JumpActivity extends AppCompatActivity implements SensorEventListener {

    private Sensor mMagnetic;
    private Sensor mGravity;
    private float[] gravityValues = new float[3];
    private float[] magneticValues = new float[3];
    private TextView nbrJump;
    private TextView finishedField;
    private int currentNbrJumps = 0;
    private boolean currentPosUp = false;
    private Sensor mAccelerator;
    private SensorManager mSensorManager;
    private int nbrOfReps;
    private long startTime;
    private long endTime;


    //Soundfiles
    private MediaPlayer gb;
    private MediaPlayer threeJumps;
    private MediaPlayer one;
    private MediaPlayer two;
    private MediaPlayer three;
    private MediaPlayer letsJump;
    private MediaPlayer doubletaptoskip;
    private MediaPlayer activityskipped;
    private MediaPlayer tone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        threeJumps = MediaPlayer.create(this, R.raw.threejumpsleft);
        gb = MediaPlayer.create(this, R.raw.goodjob4);
        one = MediaPlayer.create(this, R.raw.one);
        two = MediaPlayer.create(this, R.raw.two);
        three = MediaPlayer.create(this, R.raw.three);
        letsJump = MediaPlayer.create(this, R.raw.letsdojumpten);
        doubletaptoskip=MediaPlayer.create(this, R.raw.duringtapskip);
        activityskipped=MediaPlayer.create(this, R.raw.activityskipped);
        tone=MediaPlayer.create(this, R.raw.tone);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerator = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        nbrJump = (TextView) findViewById(R.id.nbrJumps);
        finishedField = (TextView) findViewById(R.id.finishedView);
        mSensorManager.registerListener(this, mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);

        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);

        startTime = System.currentTimeMillis();
        nbrOfReps = getIntent().getIntExtra("JUMPS", 0);
       Integer c= WorkoutActivity.getCheck();

        if(checkIfFirstActivity(c)){
            doubletaptoskip.start();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    letsJump.start();
                }
            }, 6000);
        }else {
            letsJump.start();
        }

        final GestureDetector gd = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //activityskipped.start();
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
        }

        if ((gravityValues != null) && (magneticValues != null)
                && (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)) {
            float[] deviceRelativeAcceleration = new float[4];
            deviceRelativeAcceleration[0] = event.values[0];
            deviceRelativeAcceleration[1] = event.values[1];
            deviceRelativeAcceleration[2] = event.values[2];
            deviceRelativeAcceleration[3] = 0;

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
                    if (currentNbrJumps < nbrOfReps) {
                        currentPosUp = true;
                        currentNbrJumps++;
                        countWithMe(currentNbrJumps);
                        nbrJump.setText(Integer.toString(currentNbrJumps));
                        if (threeRepsLeft(currentNbrJumps, nbrOfReps)) {
                            threeJumps.start();
                        }
                        if (currentNbrJumps == nbrOfReps) {
                            nbrJump.setText("");
                            finishedField.setText("Good job!");
                            gb.start();
                            endTime = System.currentTimeMillis();
                            Intent intent = new Intent();
                            intent.putExtra("TIMELEFT", endTime - startTime);
                            setResult(RESULT_OK, intent);
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 3000);

                        }
                    }
                }
            }
        }
    }

    private boolean detectDown(float[] values) {
        if (values[2] > 12) {
            return true;
        }
        return false;
    }

    private boolean detectUp(float[] values) {
        if (values[2] < -2) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        overridePendingTransition(0,0);
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
        overridePendingTransition(0,0);
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

    public void countWithMe(int currentNbrJumps) {
        if (currentNbrJumps == 1) {
            one.start();
        } else if (currentNbrJumps == 2) {
            two.start();
        } else if (currentNbrJumps == 3) {
            three.start();
       }else if(!threeRepsLeft(currentNbrJumps, nbrOfReps)){
            tone.start();
        }
    }

    public boolean checkIfFirstActivity(int check){
        if(check==0){
            WorkoutActivity.setCheck(1);
            return true;
        }
        return false;
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
                        Intent intent = new Intent(JumpActivity.this, MainActivity.class);
                        startActivity(intent);

                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}