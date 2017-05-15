package com.example.elin.interactice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity {
    public String level;
    public Long workoutTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        Intent intent = getIntent();
        level = intent.getStringExtra("LEVEL");
        workoutTime = intent.getLongExtra("TIME", 0)/1000;


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
