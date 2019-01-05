package com.example.fabiouceda.gui_test;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Objects
    private DrawerLayout drawer;
    private TextView tv_drawer_username;
    private TextView tv_drawer_aliasname;
    private acUser androidCachingUser; // contains FB user and additional information
    private LocationManager locManager;
    private LocationListener locListener;
    private SharedPreferences sharedPref;
    private Random random_nr_generator;

    // primitive Variables
    private final String TAG = "TAG1_MAIN_ACT";
    private final String LOC_PROVIDER = LocationManager.GPS_PROVIDER;
    private String filenames[] = new String[12];

    static final int REQ_IMAGE_CAPTURE = 1;
    static final int PROFILE = 1;
    static final int PLAY = 2;
    static final int LOCALS = 3;
    static final int SETTINGS = 4;
    private int last_active_fragment;
    private int i_login_state;
    private int i_register_state;
    private int i_selected_challange;

    private boolean x_user_present;
    private boolean x_only_use_wlan;
    private boolean x_gps_permission_granted;
    private boolean challange_exists[] = new boolean[12];

    // Firebase Variables
    private FirebaseAuth mAuth;



    /**
     * onCreate gets called on App-Start
     * Created by: Fabio
     * Code-Snippets by:
     * - Coding in Flow (Youtube)
     * @param savedInstanceState state of the last instance (e.g.: used if screen gets rotated)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // use Layout with Nav-Drawer
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigation_view = findViewById(R.id.nav_view);
        navigation_view.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        androidCachingUser = new acUser();
        View Head = navigation_view.getHeaderView(0);
        tv_drawer_username = Head.findViewById(R.id.nav_head_username);
        tv_drawer_aliasname = Head.findViewById(R.id.nav_head_aliasname);
        random_nr_generator = new Random();

        filenames[0] = getString(R.string.pic_ch1) + ".jpg";
        filenames[1] = getString(R.string.pic_ch2) + ".jpg";
        filenames[2] = getString(R.string.pic_ch3) + ".jpg";
        filenames[3] = getString(R.string.pic_ch4) + ".jpg";
        filenames[4] = getString(R.string.pic_ch5) + ".jpg";
        filenames[5] = getString(R.string.pic_ch6) + ".jpg";
        filenames[6] = getString(R.string.pic_ch7) + ".jpg";
        filenames[7] = getString(R.string.pic_ch8) + ".jpg";
        filenames[8] = getString(R.string.pic_ch9) + ".jpg";
        filenames[9] = getString(R.string.pic_ch10) + ".jpg";
        filenames[10] = getString(R.string.pic_ch11) + ".jpg";
        filenames[11] = getString(R.string.pic_ch12) + ".jpg";

        // Code Snippet by "Coding in Flow" (Youtube)
        // Use Toolbar as new default Action Bar
        // so that the drawer can hover above
        Toolbar toolbar = findViewById(R.id.toolar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new play_random_fragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_play);
            last_active_fragment = PLAY;
        }
        // end of Code-Snippet

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.v(TAG + "_GPS", "Location changed");
                Log.v(TAG + "_GPS", "Lat:  " + String.valueOf(location.getLatitude()));
                Log.v(TAG + "_GPS", "Long: " + String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.v(TAG + "_GPS", "Status changed");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.v(TAG + "_GPS", "Provider enabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.v(TAG + "_GPS", "Provider disabled");
            }
        };

        //Registering the listener with the Location-Manager to receive updates
        // of cause after checking for permission

        x_gps_permission_granted = false;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            return;
        } else {
            x_gps_permission_granted = true;
        }

    }


    /**
     * saving Data to internal App-Storage and disabling location updates to
     * save power
     * Created by: Fabio
     * saving data example by android developer
     */
    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        // TODO shared preferences
        save_Settings();

        locManager.removeUpdates(locListener);

        super.onStop();
    }


    /**
     * Checks if GPS permission is granted
     * Created by: Fabio
     * Code by: stackoverflow
     *
     * @return true if GPS permission is granted
     */
    private boolean gps_permission_granted() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Overrides evaluation method fpr permission request to fit out needs
     * Created by: Fabio
     * Code by stackoverflow
     *
     * @param requestCode defined request code
     * @param permissions string for requested permissions
     * @param grantResults results of request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // GPS Permission Granted
                } else {
                    // GPS Permission NOT granted
                }
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private Location get_Location() {
        Location curr_loc = null;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            return curr_loc;
        } else {
            locManager.requestLocationUpdates(LOC_PROVIDER, 0, 0, locListener);

            curr_loc = locManager.getLastKnownLocation(LOC_PROVIDER);
        }

        locManager.removeUpdates(locListener);
        return curr_loc;
    }


    /**
     * Evaluates which navigation item had been selected
     * Created by: Fabio
     * Code Snippet by Coding in flow (edited to fit our needs)
     * @param menuItem selected navigation item
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.v(TAG, "onNavigationItemSelected called");
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new profile_fragment()).commit();
                last_active_fragment = PROFILE;
                break;
            case R.id.nav_play:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new play_random_fragment()).commit();
                last_active_fragment = PLAY;
                break;
            case R.id.nav_list_challanges:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new local_challanges_fragment()).commit();
                last_active_fragment = LOCALS;
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new settings_fragment()).commit();
                last_active_fragment = SETTINGS;
                break;
            case R.id.nav_update_with_DB:
                Toast.makeText(this, "I have to sync now!!! leave me alone!!!",
                        Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    /**
     * Makes sure that the Drawer gets closed if it is open and "back" gets pressed
     * Created by: Fabio
     * Code snippet by coding in flow
     */
    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed called");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Takes photo and saves it to an image file for later evaluation
     * Created by: Fabio
     * Code example by android developer reference
     */
    public void take_photo(int number){
        Log.v(TAG, "take photo");
        // TODO get Photo with intent
        if ( true /*hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)*/){
            // If Camera is existent
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                i_selected_challange = number;
                startActivityForResult(takePictureIntent, REQ_IMAGE_CAPTURE);
            }
        }
    }


    /**
     * Evaluates intent results
     * Checking for capture Image was successful
     * Created by: Fabio
     * Code example by android developer
     * @param requestCode key-code of intent
     * @param resultCode code for completion
     * @param data returned data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBmp = (Bitmap) extras.get("data");
            // TODO Save Image to local Storage and capture location
            saveImageToInternalStorage(imageBmp, i_selected_challange);
        } else{
            // Taking a photo failed

        }
    }


    /**
     * Saves a bitmap to a corresponging file.
     * Created by: Fabio
     * example : https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-
     * from-internal-memory-in-android
     * @param bmp_img bitmap that has to be saved
     * @return absolute Path to Image File
     */
    private String saveImageToInternalStorage(Bitmap bmp_img, int challange_no){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/androidcaching/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // resolve Image-Name
        String filename = null;
        switch(challange_no){
            case 1: filename = getString(R.string.pic_ch1) + ".jpg";
                break;
            case 2: filename = getString(R.string.pic_ch2) + ".jpg";
                break;
            case 3: filename = getString(R.string.pic_ch3) + ".jpg";
                break;
            case 4: filename = getString(R.string.pic_ch4) + ".jpg";
                break;
            case 5: filename = getString(R.string.pic_ch5) + ".jpg";
                break;
            case 6: filename = getString(R.string.pic_ch6) + ".jpg";
                break;
            case 7: filename = getString(R.string.pic_ch7) + ".jpg";
                break;
            case 8: filename = getString(R.string.pic_ch8) + ".jpg";
                break;
            case 9: filename = getString(R.string.pic_ch9) + ".jpg";
                break;
            case 10: filename = getString(R.string.pic_ch10) + ".jpg";
                break;
            case 11: filename = getString(R.string.pic_ch11) + ".jpg";
                break;
            case 12: filename = getString(R.string.pic_ch12) + ".jpg";
                break;
        }

        // Create new image file
        File mpath = new File(directory, filename);

        FileOutputStream mfos = null;
        try{
            mfos = new FileOutputStream(mpath);
            // Use the compress method on the bitmap to write to the Outputstream
            bmp_img.compress(Bitmap.CompressFormat.PNG, 100, mfos);
            Log.v(TAG+"_STORAGE", "success");
            challange_exists[i_selected_challange] = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try{
                mfos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    public Bitmap restore_img_from_internal_storage(int ref_no){
        Bitmap mBmp = null;
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/androidcaching/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        try{
            File mfile = new File (directory.getAbsolutePath(), filenames[ref_no]);
            mBmp = BitmapFactory.decodeStream(new FileInputStream(mfile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return mBmp;

    }


    /**
     * Tests if a file is existent
     * Created by:Fabio
     * code by: https://stackoverflow.com/questions/10576930/trying-to-check-if-a-file-exists-in-
     * internal-storage
     * @param num number of the slot the file must be present for
     * @return true if file exists in internal app storage
     */
    public boolean does_file_exist(int num){

        return challange_exists[num];
    }


    public String get_username(){
        return (androidCachingUser.getUsername());
    }


    public String get_aliasname(){
        return (androidCachingUser.geteMail());
    }


    public int get_score(){
        return (androidCachingUser.getScore());
    }


    public boolean is_user_present(){
        return(x_user_present);
    }


    public boolean get_only_use_wlan() {
        return x_only_use_wlan;
    }


    public void set_only_use_wlan(boolean value){
        x_only_use_wlan = value;
    }


    public void set_user_present(boolean user_present_){
        x_user_present = user_present_;
    }


    public void set_score(int score_){
        androidCachingUser.setScore(score_);
    }


    /**
     * Attempt login and return value whether or not it was successful
     * Created by: Kevin
     * Code example by FireBase Assistent
     * @param email
     * @param password
     * @return
     */
    //TODO: mehrere Fehlercodes: 0:alles ok, 1:keine Verbindung, 2:sonstiges
    // code sippets from firebase assistent
    public int attempt_login(String email, String password) {
       if(check_Connectivity() < 3) {
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
                           } else {
                               // If sign in fails, display a message to the user.
                               Log.v(TAG, "login attempt: failure", task.getException());
                               update_UI(null);
                               i_login_state = 2;
                           }
                       }
                   });
           return i_login_state;
       }
       else
           return 1;
    }


    /**
     * Attempt registering new user and return value whether or not it was successful
     * Created by: Kevin
     * Code example by FireBase Assistent
     * @param email
     * @param password
     * @return
     */
    //TODO: Fehlercodes: 0:alles ok, 1:keine Verbindung, 2:sonstiges
    // code sippets from firebase assistent
    public int attempt_register(String email, String password, String username) {
        // Check if the mobile is connected to network
        if(check_Connectivity() < 3) {
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

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.v(TAG, "regigster attempt: failure", task.getException());
                                update_UI(null);
                                i_register_state = 2;
                            }
                        }
                    });
            return i_register_state;
        }
        else
            return 1;
    }


    /**
     * Logging out user
     * Created by: Kevin
     * Code by Firebase Assistent
     */
    public void logout_user()
    {
        mAuth.signOut();
        // TODO: Update UI
        update_UI(null);
        androidCachingUser.setUsername(getString(R.string.no_user_username));
        androidCachingUser.seteMail(getString(R.string.no_user_aliasname));
        androidCachingUser.setScore(Integer.parseInt(getString(R.string.no_user_score)));
        x_user_present = false;
    }


    public void update_UI(){
        Log.v(TAG, "logout:success");
    }


    /**
     * Updating UI with user information from DB
     * Created by: Kevin
     * @param user
     */
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


    /**
     * Loading presets for Firebase user
     * Created by: Kevin
     */
    @Override
    protected void onStart() {
        super.onStart();
        //
        NavigationView navigation_view = findViewById(R.id.nav_view);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // TODO Laden von sharedPreferences // DONE
        read_Settings(); // restore saved settings from shared Preferences

        // restore the last state navigation selection
        switch (last_active_fragment) {
            case PROFILE:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new profile_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_profile);
                last_active_fragment = PROFILE;
                break;
            case PLAY:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new play_random_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_play);
                last_active_fragment = PLAY;
                break;
            case LOCALS:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new local_challanges_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_list_challanges);
                last_active_fragment = LOCALS;
                break;
            case SETTINGS:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new settings_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_settings);
                last_active_fragment = SETTINGS;
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new play_random_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_play);
                last_active_fragment = PLAY;
        }

        // switch to profile fragment if no user is present
        if (currentUser == null) {
            // TODO: Wechsel zu profile_fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new profile_fragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_profile);
            last_active_fragment = PROFILE;
        }
    }


    // method from Android Studio Devolopers website
    // returncodes: 0: Wifi + Mobile connected
    //              1: Only Wifi connected
    //              2: Only Mobile connected
    //              3: Nothing connected
    public int check_Connectivity() {
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
        Log.v(TAG, "Wifi connected: " + isWifiConn);
        Log.v(TAG, "Mobile connected: " + isMobileConn);

        if(isWifiConn && isMobileConn) {
            return 0;
        }
        else if((isWifiConn == true) && (isMobileConn == false)) {
            return 1;
        }
        else if((isWifiConn == false) && (isMobileConn == true)) {
            return 2;
        }
        else
            return 3;
    }


    /**
     * Save data to shared preferences
     * Created by: Fabio
     */
    public void save_Settings() {
        SharedPreferences.Editor edit = sharedPref.edit();
        // Save current Settings
        edit.putBoolean(getString(R.string.onlyUseWlan), x_only_use_wlan);
        edit.putBoolean(getString(R.string.ac_gps_perm_granted), x_gps_permission_granted);
        edit.putInt(getString(R.string.last_active_fragment), last_active_fragment);


        for(int i=0; i<12; i++){
            edit.putBoolean(filenames[i], challange_exists[i]);
        }

        // Save user-data for offline useage
        edit.putString(getString(R.string.ac_username), androidCachingUser.getUsername());
        edit.putString(getString(R.string.ac_aliasname), androidCachingUser.geteMail());
        edit.putInt(getString(R.string.ac_score), androidCachingUser.getScore());
        edit.putBoolean(getString(R.string.ac_user_present), x_user_present);
        edit.apply();
    }


    /**
     * Reads the saved data from shared preferences
     * if there was no user present the last time, the user-data is not read but
     * replaced with the default values defined in strings.xml
     */
    public void read_Settings() {
        // read saved settings from shared preferences
        Log.v(TAG, "Val: " + x_only_use_wlan);
        x_only_use_wlan = sharedPref.getBoolean(getString(R.string.onlyUseWlan), true);
        x_gps_permission_granted = sharedPref.getBoolean(getString(R.string.ac_gps_perm_granted),
                false);
        last_active_fragment = sharedPref.getInt(getString(R.string.last_active_fragment), PLAY);

        for (int i=0; i<12; i++){
            challange_exists[i] = sharedPref.getBoolean(filenames[i], false);
        }

        // read user-data
        x_user_present = sharedPref.getBoolean(getString(R.string.ac_user_present), false);
        if (x_user_present == true){
            androidCachingUser.setUsername(sharedPref.getString(getString(R.string.ac_username),
                    getString(R.string.no_user_username)));
            androidCachingUser.seteMail(sharedPref.getString(getString(R.string.ac_aliasname),
                    getString(R.string.no_user_aliasname)));
            androidCachingUser.setScore(sharedPref.getInt(getString(R.string.ac_score),
                    Integer.parseInt(getString(R.string.no_user_score))));
        } else{
            // if there was no logged in user last time, load the default values
            androidCachingUser.setUsername(getString(R.string.no_user_username));
            androidCachingUser.seteMail(getString(R.string.no_user_aliasname));
            androidCachingUser.setScore(Integer.parseInt(getString(R.string.no_user_score)));
        }

    }
}// End of main Activity




















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