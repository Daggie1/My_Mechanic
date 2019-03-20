package com.example.anonymous.Mymechanic;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MechanicActivity extends SingleFragment {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createFragment(new MechanicFragment());



        toolbar=(Toolbar) findViewById(R.id.hometoolbar);

        setSupportActionBar(toolbar);



        //the navigation backButton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                startActivity(new Intent(this,HomeActivity.class));
                break;
        }
        return true;
    }
}
