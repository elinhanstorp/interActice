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

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                counterField.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                counterField.setText("Starting!");
                workoutHandler();
            }
        }.start();


    }

    public void workoutHandler(){
            Intent intent = new Intent(this, DistanceActivity.class);
            intent.putExtra("TIMELEFT", workoutTime*1000*60);
            startActivity(intent);
        //}

    }

}
