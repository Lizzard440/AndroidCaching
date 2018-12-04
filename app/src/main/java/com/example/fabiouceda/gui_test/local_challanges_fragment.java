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

public class local_challanges_fragment extends Fragment {

    private final String TAG = "TAG1_LOC_CH_FRAG";
    private Button b_help;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView called");

        View v_local_challanges = inflater.inflate(R.layout.fragment_local_challanges, container, false);

        b_help = (Button) v_local_challanges.findViewById(R.id.loc_ch_frag_help);

        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder instruction_window = new AlertDialog.Builder(getActivity());
                instruction_window.setPositiveButton("okay... I guess...", null);
                instruction_window.setTitle("Help");
                instruction_window.setMessage("Browse and delete your downloaded challanges");
                instruction_window.create().show();
            }
        });

        return v_local_challanges;
    }
}
