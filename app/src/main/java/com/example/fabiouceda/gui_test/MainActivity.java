package com.example.fabiouceda.gui_test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private boolean x_user_present;
    private int i_login_state;
    private int i_register_state;

    // Firebase Variables
    private FirebaseAuth mAuth;

    /**
     * onCreate gets called on App-Start
     * Code-Snippets by:
     * - Coding in Flow (Youtube)
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate called");

        mAuth = FirebaseAuth.getInstance();

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

    public String get_username() {
        return (s_username); // TODO replace with Variable Username
    }

    public String get_aliasname() {
        return (s_aliasname); // TODO replace with Variable Alias-Name
    }

    public int get_score() {
        return (i_score); // TODO replace with Variable Score
    }

    public boolean is_user_present() {
        return (x_user_present);
    }

    public void set_user_present(boolean user_present_) {
        x_user_present = user_present_;
    }

    public void set_username(String username_) {
        s_username = username_;
    }

    public void set_aliasname(String aliasname_) {
        s_aliasname = aliasname_;
    }

    public void set_score(int score_) {
        i_score = score_;
    }

    //TODO: mehrere Fehlercodes: 0:alles ok, 1:pw falsch, 2:Email falsch, 3:keine Verbindung, 4:sonstiges
    // code sippets from firebase assistent
    public int attempt_login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v(TAG, "login attempt: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            update_UI(user);
                            i_login_state = 0;
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.v(TAG, "login attempt: failure", task.getException());
                            update_UI(null);
                            i_login_state = 1;
                        }
                    }
                });

        return i_login_state;
    }


    //TODO: Fehlercodes: 0:alles ok, 1:keine Verbindung, 2:sonstiges
    // code sippets from firebase assistent
    public int attempt_register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v(TAG, "register attempt: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            update_UI(user);
                            i_register_state = 0;

                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.v(TAG, "regigster attempt: failure", task.getException());
                            update_UI(null);
                            i_register_state = 1;
                        }
                    }
                });

        return i_register_state;
    }

    public void logout_user()
    {
        mAuth.signOut();
        // TODO: Update UI
        update_UI(null);
        Log.v(TAG, "logout:success");
    }

    public void display_login_Screen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new login_or_register_fragment()).commit();
    }

    public void update_UI() {
        tv_drawer_username.setText(s_username);
        tv_drawer_aliasname.setText(s_aliasname);
    }

    public void update_UI(FirebaseUser user) {
        if(user != null)
        {
            tv_drawer_username.setText(user.getDisplayName());
            tv_drawer_aliasname.setText(user.getEmail());
        }
        else
        {
            tv_drawer_username.setText(null);
            tv_drawer_aliasname.setText(null);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();


        //
        NavigationView navigation_view = findViewById(R.id.nav_view);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // TODO: Wechsel zu profile_fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new profile_fragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_profile);
        }
    }

    public void Check_Connectivity() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        Log.d(TAG, "Wifi connected: " + isWifiConn);
        Log.d(TAG, "Mobile connected: " + isMobileConn);
    }

}




/*
private void updateUI(FirebaseUser user) {

        hideProgressDialog();

        if (user != null) {

            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,

                    user.getEmail(), user.isEmailVerified()));

            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));



            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);

            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);

            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);



            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());

        } else {

            mStatusTextView.setText(R.string.signed_out);

            mDetailTextView.setText(null);



            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);

            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);

            findViewById(R.id.signedInButtons).setVisibility(View.GONE);

        }

    }
 */