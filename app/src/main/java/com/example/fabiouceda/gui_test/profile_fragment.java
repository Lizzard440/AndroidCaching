package com.example.fabiouceda.gui_test;
/**
 * inflates layout and contains basic functionality
 * everything else is fetched from the MainActivity
 * Created by: Fabio
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class profile_fragment extends Fragment {

    private final String TAG = "TAG1_PROFILE_FRAG";
    private TextView tv_username;
    private TextView tv_aliasname;
    private TextView tv_score;
    private ImageView iv_profilepic;
    private Button b_login_button;
    private String login_mail;
    private String login_pw;
    private String register_mail;
    private String register_username;
    private String register_pw1;
    private String register_pw2;


    /**
     * Gets called if the profile-item from the navigation got selected.
     * Creates onClickListeners for the Buttons and displays a login-screen
     * if needed.
     * Displays a custom dialogue (see layout/login_and_register.xml) for
     * registering new users or logging in existing ones.
     * Created by: Fabio
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.v(TAG, "onCreateView called");

        View v_profile_fragment = inflater.inflate(R.layout.fragment_profile, container,
                false);

        tv_username = v_profile_fragment.findViewById(R.id.profile_frag_username);
        tv_aliasname = v_profile_fragment.findViewById(R.id.profile_frag_aliasname);
        tv_score = v_profile_fragment.findViewById(R.id.profile_frag_Score);
        iv_profilepic = v_profile_fragment.findViewById(R.id.profile_frag_profilepic);
        b_login_button = v_profile_fragment.findViewById(R.id.profile_frag_login_out_button);
        Button b_help = v_profile_fragment.findViewById(R.id.profile_frag_help);

        if(! ((MainActivity) getActivity()).is_user_present()){
            b_login_button.setText(getString(R.string.login_label));
        }

        b_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! ((MainActivity) getActivity()).is_user_present()){
                    // Login or Register:


                    // Create Alert Dialog Builder
                    AlertDialog.Builder login_builder = new AlertDialog.Builder(getActivity());
                    // Create Inflater for Login-Layout
                    LayoutInflater login_inflater = getActivity().getLayoutInflater();

                            final View v_login_or_register = login_inflater
                                    .inflate(R.layout.login_and_register, null);

                    login_builder.setView(v_login_or_register)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Log in or Sign in User
                                    Log.v(TAG, "Submit Login or Register clicked");
                                    grab_login_screen_content(v_login_or_register);
                                    switch(evaluate_login_content()){
                                        case 0: // success (Correct data)
                                            // TODO remove line underneath after testing
                                            ((MainActivity) getActivity()).set_user_present(true);
                                            tv_username.setText(((MainActivity)getActivity())
                                                    .get_username());
                                            tv_aliasname.setText(((MainActivity)getActivity())
                                                    .get_aliasname());
                                            tv_score.setText("Score: " + Integer
                                                    .toString(((MainActivity)getActivity())
                                                            .get_score()));
                                            b_login_button.setText(getText(R.string.logout_label));
                                            break;
                                        case -1: // failure (Invalid data)
                                            tv_username.setText(getString(R.string.no_user_username));
                                            tv_aliasname.setText(getString(R.string.no_user_aliasname));
                                            tv_score.setText("Score: " + getString(R.string.no_user_score));
                                            break;
                                        default:
                                    }
                                }
                            })
                            .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.v(TAG, "Cancle Login or Register clicked");
                                }
                            });
                    login_builder.create();
                    login_builder.show();
                    Log.v(TAG, "Login Screen Created");

                } else{
                    // Logout
                    tv_username.setText(getString(R.string.no_user_username));
                    tv_aliasname.setText(getString(R.string.no_user_aliasname));
                    tv_score.setText("Score: " + getString(R.string.no_user_score));
                    b_login_button.setText(getText(R.string.login_label));

                    ((MainActivity) getActivity()).logout_user();
                    // TODO remove line underneath after testing
                    ((MainActivity) getActivity()).set_user_present(false);
                }

                ((MainActivity) getActivity()).update_UI();
            }
        });

        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder instruction_window = new AlertDialog.Builder(getActivity());
                instruction_window.setPositiveButton("understand...", null);
                instruction_window.setTitle("Help");
                instruction_window.setMessage(R.string.help_in_profile);
                instruction_window.create().show();
            }
        });


        return v_profile_fragment;
    }

    /**
     * Gets the login-window view after submit got clicked
     * extracts the entered information and saves it to the corresponding
     * variables.
     * Created by: Fabio
     * @param v_login_window view of the login window
     */
    private void grab_login_screen_content(View v_login_window){
        login_mail          = null;
        login_pw            = null;
        register_mail       = null;
        register_username   = null;
        register_pw1        = null;
        register_pw2        = null;
        EditText et_contentgrabber = (EditText) v_login_window.findViewById(R.id.login_eMail);
        login_mail = et_contentgrabber.getText().toString();
        et_contentgrabber = (EditText) v_login_window.findViewById(R.id.login_PW);
        login_pw = et_contentgrabber.getText().toString();
        et_contentgrabber = (EditText) v_login_window.findViewById(R.id.register_eMail);
        register_mail = et_contentgrabber.getText().toString();
        et_contentgrabber = (EditText) v_login_window.findViewById(R.id.register_username);
        register_username = et_contentgrabber.getText().toString();
        et_contentgrabber = (EditText) v_login_window.findViewById(R.id.register_PW1);
        register_pw1 = et_contentgrabber.getText().toString();
        et_contentgrabber = (EditText) v_login_window.findViewById(R.id.register_PW2);
        register_pw2 = et_contentgrabber.getText().toString();
        /* Test
        Log.v(TAG, "Read Data:\nlogin mail:        " + login_mail
                                + "\nlogin PW:          " + login_pw
                                + "\nregister username: " + register_username
                                + "\nregister mail:     " + register_mail
                                + "\nregister PW:       " + register_pw1 + " & " + register_pw2);
        */
    }

    /**
     * Checks the data that was put in by the user and decides, weather or not
     * a method for registration or login should be called.
     * Checks login-data first. Therefore, if data for every line got entered,
     * only login will be evaluated.
     * Checks if the passwords for registration match
     * shows a toast with information for the user if an error occurred.
     * Created by: Fabio
     * @return error-code 0-OK | -1-Error
     */
    private int evaluate_login_content(){
        /* Test
        String message = "login Mail:    " + login_mail
                        + "\nlogin PW:      " + login_pw
                        + "\nregister Mail: " + register_mail
                        + "\nregister PW1:  " + register_pw1
                        + "\nregister PW2:  " + register_pw2;
        Log.v(TAG, message);
        */

        if(isEmailValid(login_mail) == true){
            if(TextUtils.isEmpty(login_pw)){
                // missing login Password
                // Error Message
                Toast.makeText(getContext(), getString(R.string.missing_pw), Toast.LENGTH_LONG).show();
                return (-1);
            }
            // Go on to attempt login
            Log.v(TAG, "attempt Login");
            ((MainActivity) getActivity()).attempt_login(login_mail, login_pw);

        } else if(isEmailValid(register_mail) == true){
            if(TextUtils.isEmpty(register_username)){
                // without username
                return (-1);
            }
            if(TextUtils.isEmpty(register_pw1)){
                // without password
                // Error Message
                Toast.makeText(getContext(), getString(R.string.missing_pw), Toast.LENGTH_LONG).show();
                return (-1);
            }
            if(register_pw2.equals(register_pw1) == false){//
                // if passwords don't match
                // Error Message
                Toast.makeText(getContext(), getString(R.string.unequal_pw), Toast.LENGTH_LONG).show();
                return (-1);
            }
            // Go on to attempt register
            Log.v(TAG, "attempt Register");
            switch(((MainActivity) getActivity()).attempt_register(register_mail, register_pw1,
                    register_username)){
                case 0: // success
                    break;
                case 1: // no internet connection
                    Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                            .show();
                    return -1;
                case 2: // other error
                    Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                            .show();
                    return -1;
                default: break;
            }
        }else{
            // Error Message
            Toast.makeText(getContext(), getString(R.string.no_entry_was_made), Toast.LENGTH_LONG).show();
        }

        return 0;
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email string to check if it is a e-mail
     * @return boolean true for valid false for invalid
     * from: https://stackoverflow.com/questions/6119722
     * /how-to-check-edittexts-text-is-email-address-or-not
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"; // ???
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}