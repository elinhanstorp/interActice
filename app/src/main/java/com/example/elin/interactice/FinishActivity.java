package com.example.elin.interactice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity {
    public String level;
    public Long workoutTime;
    private MediaPlayer excellent;
    private MediaPlayer twominworkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        excellent = MediaPlayer.create(this, R.raw.excellent);
        twominworkout = MediaPlayer.create(this, R.raw.welldonetoday);

        Intent intent = getIntent();
        level = intent.getStringExtra("LEVEL");
        workoutTime = intent.getLongExtra("TIME", 0)/1000;

        excellent.start();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                twominworkout.start();
            }
        }, 5000);

        TextView levelField =  (TextView)findViewById(R.id.level);
        TextView timeField =  (TextView)findViewById(R.id.time);

        levelField.setText("Level: " + level);
        timeField.setText("Time: " + workoutTime/(60) + " min");
    }

    public void exit(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
