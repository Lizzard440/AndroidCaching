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

public class play_random_fragment extends Fragment {

    private final String TAG = "TAG1_PLAY_RAND_FRAG";
    private Button b_help;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView called");

        View v_play_rand_fragment = inflater.inflate(R.layout.fragment_play_random, container, false);

        b_help = (Button) v_play_rand_fragment.findViewById(R.id.play_rand_frag_help);

        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder instruction_window = new AlertDialog.Builder(getActivity());
                instruction_window.setPositiveButton("copy that.", null);
                instruction_window.setTitle("Help");
                instruction_window.setMessage("Play a random selected challange from your local storage");
                instruction_window.create().show();
            }
        });

        return v_play_rand_fragment;
    }


}
