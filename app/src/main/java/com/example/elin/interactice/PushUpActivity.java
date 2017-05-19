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

public class PushUpActivity extends AppCompatActivity implements SensorEventListener{
    private TextView nbrPushUp;
    private TextView finishedField;
    private int currentNbrPushUp = 0;
    private boolean currentPosUp = false;
    private Sensor mAccelerator;
    private SensorManager mSensorManager;
    private Vibrator v ;
    private int nbrOfReps;
    private long startTime;
    private long endTime;
    private boolean instructionsDone = false;

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
    private MediaPlayer doPushUps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up);
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
        doPushUps = MediaPlayer.create(this, R.raw.timeforpushupsdoten);

        gb = MediaPlayer.create(this, R.raw.goodjob);
        mSensorManager= (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerator=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        nbrPushUp = (TextView) findViewById(R.id.nbrPushUps);
        finishedField = (TextView) findViewById(R.id.finishedView);
        mSensorManager.registerListener(this, mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);

        startTime = System.currentTimeMillis();
        nbrOfReps = getIntent().getIntExtra("REPS", 0);
        doPushUps.start();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                instructionsDone = true;
            }
        }, 2000);

        final GestureDetector gd = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if(gb != null){
                    gb.release();
                    gb = null;
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
                if(doPushUps != null){
                    doPushUps.release();
                    doPushUps = null;
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
        float[] accelerationValues= getValues(event);

        /*for(int i=0; i<accelerationValues.length; i++){
            if(i==1) {
                Log.d(Integer.toString(i), Float.toString(accelerationValues[i]));
            }
        }*/

        if(instructionsDone) {
            if (currentPosUp) {
                if (detectDown(accelerationValues)) {
                    currentPosUp = false;
                }
            } else {
                if (detectUp(accelerationValues)) {
                    if (currentNbrPushUp < nbrOfReps) {
                        currentPosUp = true;
                        currentNbrPushUp++;
                        Vib();
                        countWithMe(currentNbrPushUp);
                        nbrPushUp.setText(Integer.toString(currentNbrPushUp));
                        if (currentNbrPushUp == nbrOfReps) {
                            nbrPushUp.setText("");
                            finishedField.setText("Good job!");
                            gb.start();
                            endTime = System.currentTimeMillis();
                            Intent intent = new Intent();
                            intent.putExtra("TIMELEFT", endTime - startTime);
                            setResult(RESULT_OK, intent);

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    gb.release();
                                    gb = null;
                                    doPushUps.release();
                                    doPushUps = null;
                                    finish();
                                }
                            }, 3000);
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
        if(values[1]>3) {
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
        overridePendingTransition(0,0);
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

    public boolean threeRepsLeft(int currentNbrPushUp, int nbrOfReps) {
        int remaining = nbrOfReps - currentNbrPushUp;
        return remaining == 3;
    }

    public void countWithMe(int currentNbrPushUp){
        if(currentNbrPushUp==1){
            one.start();
        }else if(currentNbrPushUp==2){
            one.release();
            one = null;
            two.start();
        }else if(currentNbrPushUp==3){
            two.release();
            two = null;
            three.start();
        } else if (currentNbrPushUp == 4) {
            three.release();
            three = null;
            four.start();
        } else if (currentNbrPushUp == 5) {
            four.release();
            four = null;
            five.start();
        } else if (currentNbrPushUp == 6) {
            five.release();
            five = null;
            six.start();
        } else if (currentNbrPushUp == 7) {
            six.release();
            six = null;
            seven.start();
        } else if (currentNbrPushUp == 8) {
            seven.release();
            seven = null;
            eight.start();
        } else if (currentNbrPushUp == 9) {
            eight.release();
            eight = null;
            nine.start();
        } else if (currentNbrPushUp == 10) {
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
