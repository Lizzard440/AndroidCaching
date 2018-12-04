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

import com.google.firebase.auth.FirebaseAuth;

public class login_or_register_fragment extends Fragment {

    private final String TAG = "TAG1_LOGIN_FRAG";
    private Button bt_submit;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView called");

        mAuth = FirebaseAuth.getInstance();

        View v_play_rand_fragment = inflater.inflate(R.layout.login_and_register, container, false);

        bt_submit = v_play_rand_fragment.findViewById(R.id.login_submit_button);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).attempt_register("fabio.uceda.game@t-oonline.de", "12345");
            }
        });

        return v_play_rand_fragment;
    }


}
