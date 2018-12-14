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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

public class settings_fragment extends Fragment {

    private Button b_help;
    private Switch sw_only_Wlan;

    private final String TAG = "TAG1_SETT_FRAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView called");
        View v_settings_fragment = inflater.inflate(R.layout.fragment_settings, container, false);

        b_help = (Button) v_settings_fragment.findViewById(R.id.settings_frag_help);

        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder instruction_window = new AlertDialog.Builder(getActivity());
                instruction_window.setPositiveButton("got it!", null);
                instruction_window.setTitle("Help");
                instruction_window.setMessage(R.string.help_in_settings);
                instruction_window.create().show();
            }
        });

        sw_only_Wlan = (Switch) v_settings_fragment.findViewById(R.id.use_only_wlan_switch);
        ((MainActivity)getActivity()).set_only_use_wlan(sw_only_Wlan.isChecked());

        return v_settings_fragment;
    }
}
