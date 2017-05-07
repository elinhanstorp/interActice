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
    public static workoutstart newInstance() {
        workoutstart fragment = new workoutstart();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_workoutstart, container, false);

        Button newPage = (Button)v.findViewById(R.id.startbutton);
        newPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DistanceActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }


    public void startWorkout(View view){

    }

}
