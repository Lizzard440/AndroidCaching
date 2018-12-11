package com.example.fabiouceda.gui_test;

import android.content.Context;
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

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    // Objects
    private TextView tv_drawer_username;
    private TextView tv_drawer_aliasname;
    private ImageView iv_drawer_profilepic;
    private File saveFile;

    // primitive Variables
    private final String TAG = "TAG1_MAIN_ACT";
    private String s_username;
    private String s_aliasname;

    private int i_score;

    private boolean x_user_present;
    private boolean x_only_use_wlan;

    /**
     * onCreate gets called on App-Start
     * Code-Snippets by:
     * - Coding in Flow (Youtube)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // use Layout with Nav-Drawer
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate");

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
        }
        View Head = navigation_view.getHeaderView(0);
        // Snippet End

        s_username = "Lizzard440";
        s_aliasname = "Fabio Uceda Perona";
        i_score = 9999;


        tv_drawer_username = Head.findViewById(R.id.nav_head_username);
        tv_drawer_aliasname = Head.findViewById(R.id.nav_head_aliasname);
        saveFile = new File(getApplicationContext().getFilesDir(), "savefile");
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");

        try {
            FileOutputStream outStream = openFileOutput("savefile", Context.MODE_PRIVATE);
            // TODO save Data
            outStream.write(s_username.getBytes());
            outStream.write(s_aliasname.getBytes());
            outStream.write(Integer.toString(i_score).getBytes());
            outStream.close();
        } catch (Exception exept) {
            exept.printStackTrace();
        }
        super.onStop();
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

    public void take_photo(){
        // TODO get Photo with intent
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

    public boolean is_user_present(){
        return(x_user_present);
    }

    public void set_user_present(boolean user_present_){
        x_user_present = user_present_;
    }

    public void set_username(String username_){
        s_username = username_;
    }

    public void set_aliasname(String aliasname_){
        s_aliasname = aliasname_;
    }

    public void set_score(int score_){
        i_score = score_;
    }

    // Database Communication

    public void attempt_login(String email, String password){// TODO Implement Error Variable!!!
        Log.v(TAG, "Try to Login\ne-Mail: " + email + "\nPW:     " + password);
    }

    public void attempt_register(String email, String password){// TODO Implement Error Variable!!!
        Log.v(TAG, "Try to Register\ne-Mail: " + email + "\nPW:     " + password);
    }

    public void logout_user(){
        if (is_user_present()){
            // TODO implement lodout code
        } else{
            Log.v(TAG, "Logout while no user present");
        }
    }

    public void update_UI(){
        tv_drawer_username.setText(s_username);
        tv_drawer_aliasname.setText(s_aliasname);
    }

    public String get_username_from_DB(){
        Log.v(TAG, "get username from DB");
        // TODO fill Method
        return "Username";
    }

    public String get_aliasname_from_DB(){
        Log.v(TAG, "get aliasname from DB");
        // TODO fill Method (aliasname = e-mail address)
        return "mail@example.com";
    }

    public int get_score_from_DB(){
        Log.v(TAG, "get user score from DB");
        // TODO fill Method (aliasname = e-mail address)
        return 0;
    }

}
