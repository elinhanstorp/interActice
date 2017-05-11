package com.example.elin.interactice;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {
    public String level;
    public long workoutTime;
    public final int REQUEST_CODE = 1;
    public boolean timeToRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Intent intent = getIntent();
        level = intent.getStringExtra("LEVEL");
        workoutTime = Long.valueOf(intent.getStringExtra("TIME"));


        TextView levelField =  (TextView)findViewById(R.id.level);
        TextView timeField =  (TextView)findViewById(R.id.time);
        final TextView counterField =  (TextView)findViewById(R.id.countdown);

        levelField.setText("Level: " + level);
        timeField.setText("Time: " + workoutTime + " min");

        //converting to millisec from min
        //workoutTime = workoutTime*1000*60;
        workoutTime = 60*1000*3;

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                counterField.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                counterField.setText("Starting!");
                workoutHandler(workoutTime);
            }
        }.start();


    }

    public void workoutHandler(long time){
        if (time > 0) {
            if (timeToRun) {
                Intent intent = new Intent(this, DistanceActivity.class);

                int distance = (int) (30 + Math.random() * 70);
                intent.putExtra("DISTANCE", distance);

                startActivityForResult(intent, REQUEST_CODE);
            } else {
                Intent intent = new Intent(this, JumpActivity.class);

                int nbrOfJumps = 10;
                intent.putExtra("JUMPS", nbrOfJumps);
            }
        }
        else {
            Intent intent = new Intent(this, FinishedActivity.class);
            startActivity(intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                workoutTime -= data.getLongExtra("TIMELEFT", 0);
            }
        }
        workoutHandler(workoutTime);
    }

}
