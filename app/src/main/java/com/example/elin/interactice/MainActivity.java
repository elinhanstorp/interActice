package com.example.elin.interactice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioGroup;

public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                               selectedFragment = workoutstart.newInstance();
                                break;
                            case R.id.menu_guide:
                                selectedFragment = GuideFragment.newInstance();
                                break;
                            case R.id.menu_settings:
                            selectedFragment = ItemFragment.newInstance();
                            break;
                            case R.id.menu_statistics:
                                selectedFragment = ItemFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, workoutstart.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);

        /**
        final Intent intent = new Intent(this, WorkoutActivity.class);
        Button startButton = (Button) findViewById(R.id.startbutton);

        RadioGroup rgLevel = (RadioGroup) findViewById(R.id.LevelGroup);

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


        RadioGroup rgTime = (RadioGroup) findViewById(R.id.TimeGroup);

        rgTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButton15min:
                        intent.putExtra("TIME", "15min");
                        break;
                    case R.id.radioButton30min:
                        intent.putExtra("TIME", "30min");
                        break;
                    case R.id.radioButton45min:
                        intent.putExtra("TIME", "45min");
                        break;
                }
            }
        });

        if((intent.getStringExtra("LEVEL") !=null) && (intent.getStringExtra("TIME") !=null)){
            startButton.setEnabled(true);
        }
        */
    }

}
