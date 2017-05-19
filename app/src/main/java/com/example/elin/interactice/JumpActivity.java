package com.example.elin.interactice;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
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
    private Vibrator v;
    private boolean instructionsDone = false;


    //Soundfiles
    //private MediaPlayer gb;
    private MediaPlayer gb;
    private MediaPlayer one;
    private MediaPlayer two;
    private MediaPlayer three;
    private MediaPlayer four;
    private MediaPlayer five;
    private MediaPlayer six;
    private MediaPlayer seven;
    private MediaPlayer eight;
    private MediaPlayer nine;
    private MediaPlayer letsJump;
    //private MediaPlayer tone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gb = MediaPlayer.create(this, R.raw.goodjob);
        one = MediaPlayer.create(this, R.raw.one);
        two = MediaPlayer.create(this, R.raw.two);
        three = MediaPlayer.create(this, R.raw.three);
        four = MediaPlayer.create(this, R.raw.four);
        five = MediaPlayer.create(this, R.raw.five);
        six = MediaPlayer.create(this, R.raw.six);
        seven = MediaPlayer.create(this, R.raw.seven);
        eight = MediaPlayer.create(this, R.raw.eight);
        nine = MediaPlayer.create(this, R.raw.nine);
        letsJump = MediaPlayer.create(this, R.raw.nowdotenjump);

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

        letsJump.start();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                instructionsDone = true;
            }
        }, 3000);

        final GestureDetector gd = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if(gb != null){
                    gb.release();
                    gb = null;
                }
                if(letsJump != null){
                    letsJump.release();
                    letsJump = null;
                }
                if(one != null){
                    one.release();
                    one = null;
                }
                if(two != null){
                    two.release();
                    two = null;
                }
                if(three != null){
                    three.release();
                    three = null;
                }
                if(four != null){
                    four.release();
                    four = null;
                }
                if(five != null){
                    five.release();
                    five = null;
                }
                if(six != null){
                    six.release();
                    six = null;
                }
                if(seven != null){
                    seven.release();
                    seven = null;
                }
                if(eight != null){
                    eight.release();
                    eight = null;
                }
                if(nine != null){
                    nine.release();
                    nine = null;
                }
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
        if(instructionsDone) {
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
                            Vib();
                            if (currentNbrJumps == nbrOfReps) {
                                nbrJump.setText("");
                                finishedField.setText("Good job!");
                                endTime = System.currentTimeMillis();
                                Intent intent = new Intent();
                                intent.putExtra("TIMELEFT", endTime - startTime);
                                setResult(RESULT_OK, intent);

                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        letsJump.release();
                                        letsJump = null;
                                        gb.release();
                                        gb = null;
                                        finish();
                                    }
                                }, 3000);
                            }
                        }
                    }
                }
            }
        }
    }

    public void Vib(){

        v =  (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        v.vibrate(300);
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
        overridePendingTransition(0, 0);
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
        overridePendingTransition(0, 0);
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

    public void countWithMe(int currentNbrJumps) {
        if (currentNbrJumps == 1) {
            one.start();
        } else if (currentNbrJumps == 2) {
            one.release();
            one = null;
            two.start();
        } else if (currentNbrJumps == 3) {
            two.release();
            two = null;
            three.start();
        } else if (currentNbrJumps == 4) {
            three.release();
            three = null;
            four.start();
        } else if (currentNbrJumps == 5) {
            four.release();
            four = null;
            five.start();
        } else if (currentNbrJumps == 6) {
            five.release();
            five = null;
            six.start();
        } else if (currentNbrJumps == 7) {
            six.release();
            six = null;
            seven.start();
        } else if (currentNbrJumps == 8) {
            seven.release();
            seven = null;
            eight.start();
        } else if (currentNbrJumps == 9) {
            eight.release();
            eight = null;
            nine.start();
        } else if (currentNbrJumps == 10) {
            nine.release();
            nine = null;
            gb.start();
        }
    }

    @Override
    public void onBackPressed() {
        String msg = "Are you sure you want to end this workout?";
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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