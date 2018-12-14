package com.example.fabiouceda.gui_test;

/**
 * inflates layout and contains basic functionality
 * everything else is fetched from the MainActivity
 * Created by: Fabio
 */

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

public class get_challanges_fragment extends Fragment {

    private final String TAG = "TAG1_GET_CH_FRAG";
    private Button b_help;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView called");

        View v_get_challanges = inflater.inflate(R.layout.fragment_get_challanges, container, false);

        b_help = (Button) v_get_challanges.findViewById(R.id.get_ch_frag_help);

        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder instruction_window = new AlertDialog.Builder(getActivity());
                instruction_window.setPositiveButton("if you say so", null);
                instruction_window.setTitle("Help");
                instruction_window.setMessage(R.string.help_in_browse_challanges);
                instruction_window.create().show();
            }
        });

        return v_get_challanges;
    }
}
