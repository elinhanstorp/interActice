package com.example.elin.interactice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

public class workoutstart extends Fragment {
    public Intent intent = null;

    public static workoutstart newInstance() {
        workoutstart fragment = new workoutstart();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_workoutstart, container, false);
        intent = new Intent(getActivity(), WorkoutActivity.class);
        intent.putExtra("LEVEL", "easy");
        intent.putExtra("TIME", "15");


        RadioGroup rgLevel = (RadioGroup) v.findViewById(R.id.LevelGroup);

        rgLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButtonEasy:
                        intent.putExtra("LEVEL", "easy");
                        break;
                    case R.id.radioButtonMedium:
                        intent.putExtra("LEVEL", "medium");
                        break;
                    case R.id.radioButtonHard:
                        intent.putExtra("LEVEL", "hard");
                        break;

                }

            }
        });


        RadioGroup rgTime = (RadioGroup) v.findViewById(R.id.TimeGroup);

        rgTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButton15min:
                        intent.putExtra("TIME", "15");
                        break;
                    case R.id.radioButton30min:
                        intent.putExtra("TIME", "30");
                        break;
                    case R.id.radioButton45min:
                        intent.putExtra("TIME", "45");
                        break;
                }
            }
        });


        Button newPage = (Button)v.findViewById(R.id.startbutton);
        newPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        return v;
    }

}
