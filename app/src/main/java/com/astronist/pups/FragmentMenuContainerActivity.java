
package com.astronist.pups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentMenuContainerActivity extends AppCompatActivity {
    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fargment_menu_container);
        inItView();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new HomeFragment())
                .commit();

        initBottomNavigation();
    }

    private void inItView() {
        fragmentContainer = findViewById(R.id.fragmentContainer);
        bottomNav = findViewById(R.id.bottomNavigationView);
    }

    private void initBottomNavigation() {

       bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               Fragment selectedFragment = null;
               switch (item.getItemId()) {
                   case R.id.home:
                       selectedFragment = new HomeFragment();
                       break;

                   case R.id.order:
                       selectedFragment = new OrderFragment();
                       break;

                   case R.id.account:
                       selectedFragment = new ProfileFragment();
                       break;

                   default:
                       break;
               }
               if (selectedFragment != null) {
                   FragmentManager fm = getSupportFragmentManager();
                   FragmentTransaction ft = fm.beginTransaction();
                   ft.replace(R.id.fragmentContainer, selectedFragment)
                           .commit();
               }
               return true;
           }
       });
    }

}