package com.example.anonymous.Mymechanic;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class SingleFragment extends AppCompatActivity {

   public  void createFragment(Fragment fragment){
       FragmentManager fm=getSupportFragmentManager();
       Fragment fragment1=fm.findFragmentById(R.id.fragmentContainer);
       if(fragment1==null){
fragment1=fragment;
fm.beginTransaction()
        .add(R.id.fragmentContainer,fragment1)
        .commit();
       }
   }
   /*
   * protected abstract Fragment createFragment();
@Override
protected void onCreate(Bundle
savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_fragment);
FragmentManager fm =
getSupportFragmentManager();
Fragment fragment =
fm.findFragmentById(R.id.fragment_container);
if (fragment == null) {
fragment = createFragment();
fm.beginTransaction()
.add(R.id.fragment_container,
fragment)
.commit();
}
}

and on Actvity
@Override
protected Fragment createFragment() {
return new CrimeFragment();
   * */
}
