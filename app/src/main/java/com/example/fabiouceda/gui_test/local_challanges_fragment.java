package com.example.fabiouceda.gui_test;
/**
 * inflates layout and contains basic functionality
 * everything else is fetched from the MainActivity
 * Created by: Fabio
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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

public class local_challanges_fragment extends Fragment {

    private final String TAG = "TAG1_LOC_CH_FRAG";

    private ImageButton ib_challenges[] = new ImageButton[12];

    private String filenames[] = new String[12];


    /**
     * Gets called when navigation item "local challanges" got selected
     * uses the view to reference the ImageButtons to create onClickViewers
     * and onLongClickViewers.
     * Created by: Fabio
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView called");

        View v_local_challanges = inflater.inflate(R.layout.fragment_local_challanges, container,
                false);

        Button b_help = (Button) v_local_challanges.findViewById(R.id.loc_ch_frag_help);

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

        ib_challenges[0] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_1);
        ib_challenges[1] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_2);
        ib_challenges[2] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_3);
        ib_challenges[3] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_4);
        ib_challenges[4] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_5);
        ib_challenges[5] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_6);
        ib_challenges[6] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_7);
        ib_challenges[7] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_8);
        ib_challenges[8] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_9);
        ib_challenges[9] =  (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_10);
        ib_challenges[10] = (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_11);
        ib_challenges[11] = (ImageButton) v_local_challanges.findViewById(R.id.Loc_Challange_12);

        // Create On click listeners for the different buttons
        create_onClickListeners();

        // Create on long clock listeners
        create_onLongClockListeners();

        update_challenges();

        return v_local_challanges;
    }


    /**
     * evaluates which slot got clicked and creates a corresponding dialogue with
     * further options
     * @param button_no number of slot that got clicked
     */
    private void stuff_got_clicked(final int button_no){
        if(((MainActivity)getActivity()).does_file_exist(button_no - 1)){
            // clicked on an existing challange
        } else {
            // clicked on an empty slot
            // TODO create new challange at this spot
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(getActivity());
            alert_builder.setMessage(getString(R.string.clicked_empty_challange_message));
            alert_builder.setTitle("Challenge Slot " + Integer.toString(button_no));
            alert_builder.setPositiveButton(getString(R.string.download_challange_label),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Download Challenge from database was selected. now handle it!
                    Log.v(TAG, "Download challenge selected");
                }
            }).setNegativeButton(getString(R.string.create_own_challange_label),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO create own challange was selected. Now handle it!
                    ((MainActivity) getActivity()).take_photo(button_no - 1);

                    Log.v(TAG, "Create Own challenge selected");
                }
            }).setNeutralButton(getString(R.string.cancel_operation), null);
            alert_builder.create();
            alert_builder.show();
        }
        Log.v(TAG, "Clicked: " + Integer.toString(button_no));
    }


    /**
     * Gets called by the onLongClickListeners witch the corresponding number
     * of the slot, that got selected.
     * If an existing challenge got selected, a dialogue with the option to delete
     * it will be shown.
     * @param selection_no number of slot that got selected
     */
    private void select_challange(final int selection_no){
        Log.v(TAG, "Selected: " + Integer.toString(selection_no));
        if(((MainActivity)getActivity()).does_file_exist(selection_no - 1)) {
            // selected an existing challenge
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(getActivity());
            alert_builder.setMessage(getString(R.string.selected_existing_challange));
            alert_builder.setTitle("Challenge Slot " + Integer.toString(selection_no));
            alert_builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity)getActivity()).delete_image(selection_no - 1);
                    update_challenges();
                }
            });
            alert_builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert_builder.create();
            alert_builder.show();
        }
    }


    /**
     * Creates all the required onClickListeners for this view
     * Created by: Fabio
     */
    private void create_onClickListeners(){
        ib_challenges[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(1);
            }
        });
        ib_challenges[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(2);
            }
        });
        ib_challenges[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(3);
            }
        });
        ib_challenges[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(4);
            }
        });
        ib_challenges[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(5);
            }
        });
        ib_challenges[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(6);
            }
        });
        ib_challenges[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(7);
            }
        });
        ib_challenges[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(8);
            }
        });
        ib_challenges[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(9);
            }
        });
        ib_challenges[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(10);
            }
        });
        ib_challenges[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(11);
            }
        });
        ib_challenges[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff_got_clicked(12);
            }
        });
    }


    /**
     * Creates all the required onLongClickListeners for this view
     * Created by: Fabio
     */
    private void create_onLongClockListeners(){
        ib_challenges[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(1);
                return false;
            }
        });
        ib_challenges[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(2);
                return false;
            }
        });
        ib_challenges[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(3);
                return false;
            }
        });
        ib_challenges[3].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(4);
                return false;
            }
        });
        ib_challenges[4].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(5);
                return false;
            }
        });
        ib_challenges[5].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(6);
                return false;
            }
        });
        ib_challenges[6].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(7);
                return false;
            }
        });
        ib_challenges[7].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(8);
                return false;
            }
        });
        ib_challenges[8].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(9);
                return false;
            }
        });
        ib_challenges[9].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(10);
                return false;
            }
        });
        ib_challenges[10].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(11);
                return false;
            }
        });
        ib_challenges[11].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select_challange(12);
                return false;
            }
        });
    }


    private void update_challenges(){
        for(int i = 0; i < 12; i++){
            if(((MainActivity)getActivity()).does_file_exist(i)){
                Bitmap mBmp = null;
                mBmp = ((MainActivity) getActivity()).restore_img_from_internal_storage(i);
                if (mBmp != null){
                    ib_challenges[i].setImageBitmap(mBmp);
                }
            } else {
                ib_challenges[i].setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_add_circle_outline_black_128dp));
            }
        }
    }

}
