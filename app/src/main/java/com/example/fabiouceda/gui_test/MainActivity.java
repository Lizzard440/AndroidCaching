package com.example.fabiouceda.gui_test;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
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
import android.net.Uri;
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
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Objects
    private DrawerLayout drawer;
    private TextView tv_drawer_username;
    private TextView tv_drawer_aliasname;
    private LocationManager locManager;
    private LocationListener locListener;
    private SharedPreferences sharedPref;
    private ACUser androidCachingUser;// contains FB user and additional information
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
    private int i_last_active_fragment;
    private int i_login_state;
    private int i_register_state;
    private int i_uploadStatus = 0;
    private int i_selected_challenge;

    private boolean x_user_present;
    private boolean x_only_use_wlan;
    private boolean x_gps_permission_granted;
    private boolean x_challenge_exists[] = new boolean[12];

    // Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference mDatabaseRef;

    private FirebaseStorage storage;
    private StorageReference mStorageRef;

    private Uri mImageUri;


    /**
     * onCreate gets called on App-Start. Creates instances of our objects and initiates them.
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

        // Firebase variables and other objects
        mAuth = FirebaseAuth.getInstance();
        androidCachingUser = new ACUser();
        View Head = navigation_view.getHeaderView(0);
        tv_drawer_username = Head.findViewById(R.id.nav_head_username);
        tv_drawer_aliasname = Head.findViewById(R.id.nav_head_aliasname);
        random_nr_generator = new Random();
        androidCachingUser = new ACUser();

        filenames[0] = getString(R.string.pic_ch1);
        filenames[1] = getString(R.string.pic_ch2);
        filenames[2] = getString(R.string.pic_ch3);
        filenames[3] = getString(R.string.pic_ch4);
        filenames[4] = getString(R.string.pic_ch5);
        filenames[5] = getString(R.string.pic_ch6);
        filenames[6] = getString(R.string.pic_ch7);
        filenames[7] = getString(R.string.pic_ch8);
        filenames[8] = getString(R.string.pic_ch9);
        filenames[9] = getString(R.string.pic_ch10);
        filenames[10] = getString(R.string.pic_ch11);
        filenames[11] = getString(R.string.pic_ch12);

        // database
        db = FirebaseFirestore.getInstance();
        mDatabaseRef = db.document("users");

        // Storage
        storage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

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
            i_last_active_fragment = PLAY;
        }
        // end of Code-Snippet

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        // Location strategies
        // Provider : GPS
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

        // Registering the locationListener with the Location-Manager to receive updates
        // ... after checking for permission
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

        locManager.removeUpdates(locListener);
        save_Settings();

        super.onStop();
    }


    /**
     * Overrides evaluation method for permission request to fit our needs
     * Created by: Fabio
     * Code by: stackoverflow
     * https://stackoverflow.com/questions/33865445/
     * gps-location-provider-requires-access-fine-location-permission-for-android-6-0
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


    /**
     * Returns an object of the Class "Location" which contains the current position data of the
     * device.
     * Created by: Fabio
     * @return
     */
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
                i_last_active_fragment = PROFILE;
                break;
            case R.id.nav_play:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new play_random_fragment()).commit();
                i_last_active_fragment = PLAY;
                break;
            case R.id.nav_list_challanges:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new local_challanges_fragment()).commit();
                i_last_active_fragment = LOCALS;
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new settings_fragment()).commit();
                i_last_active_fragment = SETTINGS;
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
     * Takes photo via intent and saves it to the corresponding image-file for later evaluation
     * Created by: Fabio
     * Code example by android developer reference
     * https://stackoverflow.com/questions/17674634/
     * saving-and-reading-bitmaps-images-from-internal-memory-in-android
     */
    public void take_photo(int number){
        Log.v(TAG, "take photo");
        if ( true /*hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)*/){
            // If Camera is existent
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                i_selected_challenge = number;
                startActivityForResult(takePictureIntent, REQ_IMAGE_CAPTURE);
            }
        }
    }
    

    /**
     * Evaluates intent results
     * Checking if capturing Image was successful
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
            saveImageToInternalStorage(imageBmp, i_selected_challenge);
        } else{
            // Taking a photo failed
        }
    }


    /**
     * Saves a bitmap to a corresponding file.
     * Created by: Fabio
     * example : https://stackoverflow.com/questions/17674634/
     * saving-and-reading-bitmaps-images-from-internal-memory-in-android
     * @param bmp_img bitmap that has to be saved
     * @return absolute Path to Image File
     */
    private String saveImageToInternalStorage(Bitmap bmp_img, int challenge_no){
        // path to /data/data/androidcaching/app_data/imageDir
        File directory = getDir("imageDir", Context.MODE_PRIVATE);

        // Create new image file
        File img_Path = new File(directory, filenames[challenge_no]);

        FileOutputStream mfos = null;
        try{
            mfos = new FileOutputStream(img_Path);
            // Use the compress method on the bitmap to write to the Outputstream
            bmp_img.compress(Bitmap.CompressFormat.PNG, 100, mfos);
            Log.v(TAG+"_STORAGE", "success");
            x_challenge_exists[i_selected_challenge] = true;
            // onCreate gets called without calling onStop, therefore the array above never gets
            // saved. So we do it manually:
            SharedPreferences.Editor edit = sharedPref.edit();
            for(int i=0; i<12; i++){
                edit.putBoolean(filenames[i], x_challenge_exists[i]);
            }
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                mfos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.v(TAG+"Filesystem", "File saved to: " + directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }


    /**
     * Restores an image from the internal app-storage
     * Created by: Fabio
     * @param ref_no which challenge image should be loaded
     * @return read Bitmap from internal Storage
     */
    public Bitmap restore_img_from_internal_storage(int ref_no){
        Bitmap mBmp = null;
        // path to /data/data/androidcaching/app_data/imageDir
        File directory = getDir("imageDir", Context.MODE_PRIVATE);
        File img_to_load = new File (directory, filenames[ref_no]);

        try{
            mBmp = BitmapFactory.decodeStream(new FileInputStream(img_to_load));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // if file was not found, declare the corresponging slot as free with setting the
            // existence of the challenge to false
            x_challenge_exists[ref_no] = false;
        }
        return mBmp;
    }


    /**
     * deletes an Image from the local app storage
     * Created by: Fabio
     * @param number which image / challenge should be deleted
     */
    public void delete_image(int number){
        File directory = getDir("imageDir", Context.MODE_PRIVATE);
        File del_file = new File (directory.getAbsolutePath(), filenames[number]);
        del_file.delete();
        x_challenge_exists[number] = false;
        Log.v(TAG, "Challange " + String.valueOf(number) + "deleted");
    }


    /**
     * Reads bool array if file should be existent
     * Created by:Fabio
     * @param num number of the slot that has to be checked
     * @return true if file should exists in internal app storage
     */
    public boolean does_file_exist(int num){
        return x_challenge_exists[num];
    }


    /**
     * returns the current users username
     * Created by: Kevin
     * @return username of current user
     */
    public String get_username(){
        return (androidCachingUser.getUsername());
    }


    /**
     * returns the aliasname (in our case the users e-Mail address).
     * Created by: Kevin
     * @return aliasname of current user
     */
    public String get_aliasname(){
        return (androidCachingUser.geteMail());
    }


    /**
     * returnes the score of the user as an int value
     * Created by: Kevin
     * @return score of current user
     */
    public int get_score(){
        return (androidCachingUser.getScore());
    }


    /**
     * is a user present?
     * Created by: Kevin
     * @return true if there is a logged in user
     */
    public boolean is_user_present(){
        return(x_user_present);
    }


    /**
     * returns the value of the switch in the settings menu
     * Created by: Kevin
     * @return true if app should only use WLAN
     */
    public boolean get_only_use_wlan() {
        return x_only_use_wlan;
    }


    /**
     * is used by the settings fragment
     * Created by: Fabio
     * @param value state of the switch
     */
    public void set_only_use_wlan(boolean value){
        x_only_use_wlan = value;
    }


    /**
     * sets the corresponding variable to "true"
     * Created by: Kevin
     * @param user_present_ setting corresponding variable to "true"
     */
    public void set_user_present(boolean user_present_){
        x_user_present = user_present_;
    }


    /**
     * Sets current users score
     * Created by: Kevin
     * @param score_ new score
     */
    public void set_score(int score_){
        androidCachingUser.setScore(score_);
    }


    /**
     * Attempts login and returns value whether or not it was successful
     * Created by: Kevin
     * Code example by FireBase Assistent
     * @param email users e-mail
     * @param password users password
     * @return 0: success
     *         1: connection invalid
     *         2: other error
     */
    public int attempt_login(String email, String password) {
       if((x_only_use_wlan && (check_connectivity() < 2)) || (!x_only_use_wlan
               && (check_connectivity() < 3))){
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
     * Attempts registering new user and returns value whether or not it was successful
     * Created by: Kevin
     * Code example by FireBase Assistent
     * @param email users e-mail
     * @param password users password
     * @return 0: success
     *         1: connection invalid
     *         2: other error
     */
    public int attempt_register(String email, String password, String username) {
        // set username
        androidCachingUser.setUsername(username);

        // Check if the mobile is connected to network
        if((x_only_use_wlan && (check_connectivity() < 2)) || (!x_only_use_wlan
                && (check_connectivity() < 3))) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.v(TAG, "register attempt: success");
                                x_user_present = true;

                                FirebaseUser user = mAuth.getCurrentUser();
                                update_UI(user);

                                // set UserID
                                androidCachingUser.setUser_ID(user.getUid());
                                // set eMail
                                androidCachingUser.seteMail(user.getEmail());

                                uploadUserInDB(androidCachingUser);

                                i_register_state = 0;
                            }
                            else {
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
        x_user_present = false;
        update_UI(null);
        androidCachingUser.setUsername(getString(R.string.no_user_username));
        androidCachingUser.seteMail(getString(R.string.no_user_aliasname));
        androidCachingUser.setScore(Integer.parseInt(getString(R.string.no_user_score)));
        x_user_present = false;
    }


    /**
     * Updating UI with user information from DB or setting it to default values if no user is
     * logged in
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
            tv_drawer_username.setText(getString(R.string.no_user_username));
            tv_drawer_aliasname.setText(getString(R.string.no_user_aliasname));
        }

    }


    /**
     * Updating UI with user information from DB or setting it to default values if no user is
     * logged in
     * Created by: Kevin
     */
    public void update_UI() {
            tv_drawer_username.setText(androidCachingUser.getUsername());
            tv_drawer_aliasname.setText(androidCachingUser.geteMail());

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
        switch (i_last_active_fragment) {
            case PROFILE:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new profile_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_profile);
                i_last_active_fragment = PROFILE;
                break;
            case PLAY:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new play_random_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_play);
                i_last_active_fragment = PLAY;
                break;
            case LOCALS:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new local_challanges_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_list_challanges);
                i_last_active_fragment = LOCALS;
                break;
            case SETTINGS:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new settings_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_settings);
                i_last_active_fragment = SETTINGS;
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new play_random_fragment()).commit();
                navigation_view.setCheckedItem(R.id.nav_play);
                i_last_active_fragment = PLAY;
        }

        // switch to profile fragment if no user is present
        if (currentUser == null) {
            // TODO: Wechsel zu profile_fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new profile_fragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_profile);
            i_last_active_fragment = PROFILE;
        }
    }


    /**
     *  method from Android Studio Devolopers website
     *  Created by: Kevin
     *  Code snippets from Android-Developer
     *  @return :       0: Wifi + Mobile connected
     *                  1: Only Wifi connected
     *                  2: Only Mobile connected
     *                  3: Nothing connected
     */
    public int check_connectivity() {
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
        else if(isWifiConn && !isMobileConn) {
            return 1;
        }
        else if(!isWifiConn && isMobileConn) {
            return 2;
        }
        else
            return 3;
    }


    /**
     * method to upload user data into database
     * created by: Kevin
     * code from Firebase Assistant
     * @param user AndroidCaching-User to upload to database
     */
    public void uploadUserInDB(ACUser user) {
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.v(TAG, "DocumentSnapshot added with ID: "
                                + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v(TAG, "Error adding document", e);
                    }
                });
    }

    /**
     * method to read data from database
     * created by: Kevin
     * code from Firebase Assistant
     */
    public void readUserFromDB() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.v(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * method to upload an image to database
     * created by: Kevin
     * Code from Firebase Assistant
     * @param uri image-uri to upload to database
     * @param name image name contains coordinates
     * @return 0: successful
     *         1: fail
     */
    public int uploadImage(Uri uri, String name) {

        if(uri != null) {
            StorageReference imageReference = mStorageRef.child(name
                    + "." + getFileExtension(uri));

            imageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            i_uploadStatus = 0;
                            Log.v(TAG, "Upload was successful.");

                            Task<Uri> downloadUri = taskSnapshot.getMetadata()
                                    .getReference().getDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            i_uploadStatus = 1;
                            Log.v(TAG, "Upload has failed.");
                        }
                    });
        }
        return i_uploadStatus;
    }


    /**
     * method to download an image from database
     * created by: Kevin
     * code from Firebase Assistant
     * @param name image name
     * @return 0: ok
     *         1: fail
     */
    public int downloadImage(String name) {
        mStorageRef.child(name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return 0;
    }

    /**
     * method to get the file extension of the image
     * created by: Kevin
     * code from https://www.youtube.com/watch?v=lPfQN-Sfnjw&list=PLrnPJCHvNZuB_
     * 7nB5QD-4bNg6tpdEUImQ&index=3
     * @param uri Uri of the image
     * @return image extension
     */
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
        edit.putInt(getString(R.string.last_active_fragment), i_last_active_fragment);


        for(int i=0; i<12; i++){
            edit.putBoolean(filenames[i], x_challenge_exists[i]);
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
        i_last_active_fragment = sharedPref.getInt(getString(R.string.last_active_fragment), PLAY);

        for (int i=0; i<12; i++){
            x_challenge_exists[i] = sharedPref.getBoolean(filenames[i], false);
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

}