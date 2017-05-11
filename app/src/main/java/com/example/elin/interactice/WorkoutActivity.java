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
    public long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Intent intent = getIntent();
        level = intent.getStringExtra("LEVEL");
        workoutTime = Long.valueOf(intent.getStringExtra("TIME"));
        startTime = System.currentTimeMillis();


        TextView levelField =  (TextView)findViewById(R.id.level);
        TextView timeField =  (TextView)findViewById(R.id.time);
        final TextView counterField =  (TextView)findViewById(R.id.countdown);

        levelField.setText("Level: " + level);
        timeField.setText("Time: " + workoutTime + " min");

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                counterField.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                counterField.setText("Starting!");
                workoutHandler();
            }
        }.start();
      /*hej*/

    }

    public void workoutHandler(){
        //while(((startTime + (workoutTime*60000))  - System.currentTimeMillis()) > 0){
            Intent intent = new Intent(this, DistanceActivity.class);
            startActivity(intent);
        //}

    }

}
