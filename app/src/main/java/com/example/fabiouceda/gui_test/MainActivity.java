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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    // Objects
    private TextView tv_drawer_username;
    private TextView tv_drawer_aliasname;
    private ImageView iv_drawer_profilepic;

    // "Simple" Variables
    private final String TAG = "TAG1_MAIN_ACT";
    private String s_username;
    private String s_aliasname;
    private int i_score;

    /**
     * onCreate gets called on App-Start
     * Code-Snippets by:
     * - Coding in Flow (Youtube)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate called");

        // Code Snippet by "Coding in Flow" (Youtube)
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
                    new play_random_fragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_play);
        } // Snippet End

        s_username = "Lizzard440";
        s_aliasname = "Fabio Uceda Perona";
        i_score = 9999;

        View Head = navigation_view.getHeaderView(0);

        Head.findViewById(R.id.nav_head_username);

        tv_drawer_username = Head.findViewById(R.id.nav_head_username);
        tv_drawer_aliasname = Head.findViewById(R.id.nav_head_aliasname);
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

    public String get_username(){
        return (s_username); // TODO replace with Variable Username
    }

    public String get_aliasname(){
        return (s_aliasname); // TODO replace with Variable Alias-Name
    }

    public int get_score(){
        return (i_score); // TODO replace with Variable Score
    }

    public void update_UI(){
        tv_drawer_username.setText(s_username);
        tv_drawer_aliasname.setText(s_aliasname);
    }
}
