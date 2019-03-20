package com.example.anonymous.Mymechanic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment1=fm.findFragmentById(R.id.loginContainer);
        if(fragment1==null){
            fragment1=new LoginFragment();
            fm.beginTransaction()
                    .add(R.id.loginContainer,fragment1)
                    .commit();
        }
    }
    /*
    * setting error
    * userlayout.seterroenabled(true)
    * userlayout.setError("")
    * ontetxtchange

    *userlayout.seterroenabled(false)
    * */
}
