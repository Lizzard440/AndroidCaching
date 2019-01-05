package com.example.fabiouceda.gui_test;
/**
 * inflates layout and contains basic functionality
 * everything else is fetched from the MainActivity
 * Created by: Fabio
 */
import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.Toast;

public class play_random_fragment extends Fragment {

    private final String TAG = "TAG1_PLAY_RAND_FRAG";
    private Button b_help;
    private ImageButton ib_set_pin_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView called");

        View v_play_rand_fragment = inflater.inflate(R.layout.fragment_play_random, container,
                false);

        b_help = (Button) v_play_rand_fragment.findViewById(R.id.play_rand_frag_help);
        ib_set_pin_button = (ImageButton) v_play_rand_fragment.findViewById(R.id.set_pin_button);

        b_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder instruction_window = new AlertDialog.Builder(getActivity());
                instruction_window.setPositiveButton("copy that.", null);
                instruction_window.setTitle("Help");
                instruction_window.setMessage(R.string.help_in_play_random);
                instruction_window.create().show();
            }
        });

        ib_set_pin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Set Pin pressed", Toast.LENGTH_SHORT).show();
                // TODO record position data and compare it to the coordinates from the challange
            }
        });

        return v_play_rand_fragment;
    }


}
