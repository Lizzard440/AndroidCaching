package com.example.fabiouceda.gui_test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class play_random_fragment extends Fragment {

    private final String TAG = "TAG1_PLAY_RAND_FRAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView called");

        return inflater.inflate(R.layout.fragment_play_random, container, false);
    }


}
