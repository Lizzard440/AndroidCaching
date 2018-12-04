package com.example.fabiouceda.gui_test;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class profile_fragment extends Fragment {

    private final String TAG = "TAG1_PROFILE_FRAG";
    private TextView tv_username;
    private TextView tv_aliasname;
    private TextView tv_score;
    private ImageView iv_profilepic;
    private Button b_login_button;
    private Button b_help;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final String username;
        final String aliasname;
        final int score;

        Log.v(TAG, "onCreateView called");

        View v_profile_fragment = inflater.inflate(R.layout.fragment_profile, container, false);

        tv_username = (TextView) v_profile_fragment.findViewById(R.id.profile_frag_username);
        tv_aliasname = (TextView) v_profile_fragment.findViewById(R.id.profile_frag_aliasname);
        tv_score = (TextView) v_profile_fragment.findViewById(R.id.profile_frag_Score);
        iv_profilepic = (ImageView) v_profile_fragment.findViewById(R.id.profile_frag_profilepic);
        b_login_button = (Button) v_profile_fragment.findViewById(R.id.profile_frag_login_out_button);
        b_help = (Button) v_profile_fragment.findViewById(R.id.profile_frag_help);

        if(! ((MainActivity) getActivity()).is_user_present()){
            b_login_button.setText("Login or Register");
        }

        b_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! ((MainActivity) getActivity()).is_user_present()){
                    // Login or Register:
                    tv_username.setText("Lizzard440");
                    ((MainActivity) getActivity()).set_username("Lizzard440");
                    tv_aliasname.setText("Fabio Uceda Perona");
                    ((MainActivity) getActivity()).set_aliasname("Fabio Uceda Perona");
                    tv_score.setText("Score: " + Integer.toString(9999));
                    ((MainActivity) getActivity()).set_score(9999);
                    b_login_button.setText("Logout");

                    ((MainActivity) getActivity()).set_user_present(true);
                    // TODO replace Picture

                    ((MainActivity) getActivity()).display_login_Screen();


                } else{
                    // Logout
                    tv_username.setText("Username");
                    ((MainActivity) getActivity()).set_username("username");
                    tv_aliasname.setText("Alias Name");
                    ((MainActivity) getActivity()).set_aliasname("aliasname");
                    tv_score.setText("Score: " + Integer.toString(0));
                    ((MainActivity) getActivity()).set_score(0);
                    b_login_button.setText("Login or Register");

                    ((MainActivity) getActivity()).set_user_present(false);
                    // TODO replace Picture
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
                instruction_window.setMessage("Manage your Account, or play offline\n(Note: no Scoreboard offline)");
                instruction_window.create().show();
            }
        });


        return v_profile_fragment;
    }

}
