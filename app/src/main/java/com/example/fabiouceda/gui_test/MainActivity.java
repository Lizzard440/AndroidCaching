package com.example.fabiouceda.gui_test;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private final String TAG = "TAG1_MAIN_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate called");

        // Use Toolbar as new default Action Bar
        Toolbar toolbar = findViewById(R.id.toolar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigation_view = findViewById(R.id.nav_view);
        navigation_view.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new profile_fragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_profile);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.v(TAG, "onNavigationItemSelected called");
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new profile_fragment()).commit();
                break;
            case R.id.nav_play:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new play_random_fragment()).commit();
                break;
            case R.id.nav_list_challanges:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new local_challanges_fragment()).commit();
                break;
            case R.id.nav_browse_challanges:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new get_challanges_fragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new settings_fragment()).commit();
                break;
            case R.id.nav_update_with_DB:
                Toast.makeText(this, "I have to sync now!!! leave me alone!!!", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed called");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
