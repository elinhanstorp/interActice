package com.example.elin.interactice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity {
    public String level;
    public Long workoutTime;
    private MediaPlayer excellent;
    private MediaPlayer twominworkout;
    private MediaPlayer checkstat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        excellent = MediaPlayer.create(this, R.raw.excellent);
        twominworkout = MediaPlayer.create(this, R.raw.minworkout);
        checkstat= MediaPlayer.create(this, R.raw.gotostat);

        Intent intent = getIntent();
        level = intent.getStringExtra("LEVEL");
        workoutTime = intent.getLongExtra("TIME", 0)/1000;

        excellent.start();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
               twominworkout.start();
            }
        }, 2000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
               checkstat.start();
            }
        }, 5000);

        TextView levelField =  (TextView)findViewById(R.id.level);
        TextView timeField =  (TextView)findViewById(R.id.time);

        levelField.setText("Level: " + level);
        timeField.setText("Time: " + workoutTime + " min");
    }

    public void exit(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
