package com.example.anonymous.Mymechanic;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends SingleFragment implements NavigationView.OnNavigationItemSelectedListener{
 Toolbar toolbar;
 DrawerLayout drawerLayout;
 NavigationView navigationView;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        setContentView(R.layout.navigation_drawer);
        auth= FirebaseAuth.getInstance();
        if(auth.getCurrentUser().getEmail().equalsIgnoreCase("admin@example.com")){
            createFragment(new PlayingAdminFragment());}
        else{
            createFragment(new NonAdminPlaying());
        }



       toolbar=(Toolbar) findViewById(R.id.hometoolbar);
       drawerLayout=(DrawerLayout) findViewById(R.id.navdrawerHome);
       navigationView=(NavigationView) findViewById(R.id.naviewHome);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //the navigation backButton
/*getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.settings_id:


                Toast.makeText(getApplicationContext(),"Settings Clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.about_id:


                Toast.makeText(getApplicationContext(),"About Us Clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.users_id:


                Toast.makeText(getApplicationContext(),"Users Clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.contact_id:


                Toast.makeText(getApplicationContext(),"Contact us Clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.searchId:
                Toast.makeText(getApplicationContext(),"button not coded for",Toast.LENGTH_SHORT).show();
                break;
            case R.id.addId:
                Toast.makeText(getApplicationContext(),"button not coded for",Toast.LENGTH_SHORT).show();
                break;
                //handling back Button
           /* case android.R.id.home:
                Toast.makeText(getApplicationContext(),"button not coded for",Toast.LENGTH_SHORT).show();
                break;*/

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        switch (id){
            //implement on drawer items clicked
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }


}
