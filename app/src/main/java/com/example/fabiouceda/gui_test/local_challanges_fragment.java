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
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

public class local_challanges_fragment extends Fragment {

    private final String TAG = "TAG1_LOC_CH_FRAG";
    private Button b_help;

    private ImageButton ib_challanges[] = new ImageButton[12];

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
                instruction_window.setMessage(R.string.help_in_local_challanges);
                instruction_window.create().show();
            }
        });

        ib_challanges[0] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_1);
        ib_challanges[1] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_2);
        ib_challanges[2] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_3);
        ib_challanges[3] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_4);
        ib_challanges[4] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_5);
        ib_challanges[5] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_6);
        ib_challanges[6] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_7);
        ib_challanges[7] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_8);
        ib_challanges[8] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_9);
        ib_challanges[9] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_10);
        ib_challanges[10] = (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_11);
        ib_challanges[11] = (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_12);

        // TODO Store / retrieve images from private app-storage

        return v_local_challanges;
    }

}
