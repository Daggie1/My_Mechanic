package com.example.anonymous.Mymechanic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MasterMechanicActivi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_mechanic);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment1=fm.findFragmentById(R.id.mastermechContainer);
        if(fragment1==null){
            fragment1=new MasterMechanicFragment();
            fm.beginTransaction()
                    .add(R.id.mastermechContainer,fragment1)
                    .commit();
        }
    }

}
