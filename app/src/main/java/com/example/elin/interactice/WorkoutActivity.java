package com.example.elin.interactice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {
    public String level;
    public String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Intent intent = getIntent();
        level = intent.getStringExtra("LEVEL");
        time = intent.getStringExtra("TIME");
        TextView test =  (TextView)findViewById(R.id.test);
        test.setText(level + " " + time);


    }

}
