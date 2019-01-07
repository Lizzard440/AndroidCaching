package com.example.fabiouceda.gui_test;

/*
 * Position mit location manager und location listener
 * Daten speichern mit sharedPreferences
 * */

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    // Objects
    private TextView tv_drawer_username;
    private TextView tv_drawer_aliasname;
    private LocationManager locManager;
    private LocationListener locListener;
    private SharedPreferences sharedPref;
    private ACUser androidCachingUser;

    // primitive Variables
    private final String TAG = "TAG1_MAIN_ACT";
    private String s_username;
    private String s_aliasname;

    static final int REQ_IMAGE_CAPTURE = 1;
    private int i_score;
    private int i_login_state;
    private int i_register_state;
    private int i_uploadStatus = 0;

    private boolean x_user_present;
    private boolean x_only_use_wlan;
    private boolean x_gps_permission_granted;

    // Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference mDatabaseRef;

    private FirebaseStorage storage;
    private StorageReference mStorageRef;

    private Uri mImageUri;



    /**
     * onCreate gets called on App-Start
     * Created by: Fabio
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

        // Firebase variables
            // authentication
        mAuth = FirebaseAuth.getInstance();
            // database
        db = FirebaseFirestore.getInstance();
        mDatabaseRef = db.document("users");
            // Storage
        storage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        androidCachingUser = new ACUser();

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
        // end of Code-Snippet

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        read_Settings(); // restore saved settings from shared Preferences

        tv_drawer_username = Head.findViewById(R.id.nav_head_username);
        tv_drawer_aliasname = Head.findViewById(R.id.nav_head_aliasname);

        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.v(TAG, "Location changed");
                Log.v(TAG, "Lat:  " + String.valueOf(location.getLatitude()));
                Log.v(TAG, "Long: " + String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.v(TAG, "Status changed");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.v(TAG, "Provider enabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.v(TAG, "Provider disabled");
            }
        };

        //Registering the listener with the Location-Manager to receive updates
        // of cause after checking for permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            x_gps_permission_granted = false;
            return;
        }
        x_gps_permission_granted = true;
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

    }

    /**
     * saving Data to internal App-Storage
     * Created by: Fabio
     * saving data example by android developer
     */
    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        // TODO shared preferences
        save_Settings();

        super.onStop();
    }



    /**
     * Evaluates wich navigation item had been selected
     * Created by: Fabio
     * Code Snippet by Coding in flow (edited to fit our needs)
     * @param menuItem
     * @return
     */
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
                if(x_only_use_wlan == false
                        ||(x_only_use_wlan == true
                        &&    check_connectivity() <= 1)){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new get_challanges_fragment()).commit();
                }else{
                    Toast.makeText(this, "Check your WiFi Connection",
                            Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new settings_fragment()).commit();
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
    public void take_photo(){
        // TODO get Photo with intent
        if ( true /*hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)*/){
            // If Camera is existent
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takePictureIntent, REQ_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Evaluates intent results
     * Checking for capture Image was successful
     * Created by: Fabio
     * Code example by android developer
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBmp = (Bitmap) extras.get("data");
            // Save Image to local Storage
        }
    }

    /**
     * Count out how many Challanges exist in the local storage.
     * Created by: Fabio
     * @return
     */
    public short count_local_challanges(){
        // TODO Implement Method, that counts the amount of downloaded challanges
        return 0;
    }


    public int get_score(){
        return (i_score); // TODO replace with Variable Score
    }

    public String get_username() {
        return (s_username);
    }

    public String get_aliasname() {
        return (s_aliasname);
    }

    public boolean is_user_present(){
        return(x_user_present);
    }

    public boolean get_only_use_wlan() {
        return x_only_use_wlan;
    }

    public void set_only_use_wlan(boolean value){
        x_only_use_wlan = value;
        Log.v(TAG, "Val: " + x_only_use_wlan);
    }

    public void set_user_present(boolean user_present_){
        x_user_present = user_present_;
    }

    public void set_score(int score_){
        i_score = score_;
    }

    /**
     * Attempt login and return value whether or not it was successful
     * Created by: Kevin
     * Code example by FireBase Assistent
     * @param email
     * @param password
     * @return
     */
    // code sippets from firebase assistent
    public int attempt_login(String email, String password) {
       if((x_only_use_wlan && (check_connectivity() < 2)) || (!x_only_use_wlan && (check_connectivity() < 3))){
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
    // Database Communication

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
        // set username
        androidCachingUser.setUsername(username);

        // Check if the mobile is connected to network
        if((x_only_use_wlan && (check_connectivity() < 2)) || (!x_only_use_wlan && (check_connectivity() < 3))) {
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

                                uploadInDB(androidCachingUser);

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
        s_username = getString(R.string.no_user_username);
        s_aliasname = getString(R.string.no_user_aliasname);
        i_score = Integer.parseInt(getString(R.string.no_user_score));
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
        if (currentUser == null) {
            // TODO: Wechsel zu profile_fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new profile_fragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_profile);

        }

        // TODO Laden von sharedPreferences
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
    public void uploadInDB(ACUser user) {
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.v(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
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
    public void readFromDB() {
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
     * @return i_uploadStatus: 0: successful, 1: fail
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

                            Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
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
     * @return 0: ok, 1: fail
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
     * code from https://www.youtube.com/watch?v=lPfQN-Sfnjw&list=PLrnPJCHvNZuB_7nB5QD-4bNg6tpdEUImQ&index=3
     * @param uri Uri of the image
     * @return image extension
     */
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    /**
     * Save local data to shared preferences
     * Created by: Fabio
     */
    public void save_Settings() {
        SharedPreferences.Editor edit = sharedPref.edit();
        // Save current Settings
        edit.putBoolean(getString(R.string.onlyUseWlan), x_only_use_wlan);
        edit.putBoolean(getString(R.string.ac_gps_perm_granted), x_gps_permission_granted);
        // Save user-data for offline useage
        edit.putString(getString(R.string.ac_username), s_username);
        edit.putString(getString(R.string.ac_aliasname), s_aliasname);
        edit.putInt(getString(R.string.ac_score), i_score);
        edit.putBoolean(getString(R.string.ac_user_present), x_user_present);
        edit.commit();
    }

    public void read_Settings() {
        // read saved settings from shared preferences
        Log.v(TAG, "Val: " + x_only_use_wlan);
        x_only_use_wlan = sharedPref.getBoolean(getString(R.string.onlyUseWlan), true);
        // read user-data
        s_username = sharedPref.getString(getString(R.string.ac_username),
                getString(R.string.no_user_username)); // default value
        s_aliasname = sharedPref.getString(getString(R.string.ac_aliasname),
                getString(R.string.no_user_aliasname)); // default value
        i_score = sharedPref.getInt(getString(R.string.ac_score),
                Integer.parseInt(getString(R.string.no_user_score))); // default value
        x_user_present = sharedPref.getBoolean(getString(R.string.ac_user_present), false);
    }

}