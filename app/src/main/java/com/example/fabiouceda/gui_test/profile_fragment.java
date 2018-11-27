package com.example.fabiouceda.gui_test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class profile_fragment extends Fragment {

    private final String TAG = "TAG1_PROFILE_FRAG";
    private TextView tv_username;
    private TextView tv_aliasname;
    private TextView tv_score;
    private ImageView iv_profilepic;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String username;
        String aliasname;
        int score;

        Log.v(TAG, "onCreateView called");

        View v_profile_fragment = inflater.inflate(R.layout.fragment_profile, container, false);

        tv_username = (TextView) v_profile_fragment.findViewById(R.id.profile_frag_username);
        tv_aliasname = (TextView) v_profile_fragment.findViewById(R.id.profile_frag_aliasname);
        tv_score = (TextView) v_profile_fragment.findViewById(R.id.profile_frag_Score);
        iv_profilepic = (ImageView) v_profile_fragment.findViewById(R.id.profile_frag_profilepic);

        username = ((MainActivity) getActivity()).get_username();
        aliasname = ((MainActivity) getActivity()).get_aliasname();
        score = ((MainActivity) getActivity()).get_score();
        ((MainActivity) getActivity()).update_UI();


        tv_username.setText(username);
        tv_aliasname.setText(aliasname);
        tv_score.setText("Score: " + Integer.toString(score));

        return v_profile_fragment;
    }

}
