package com.example.android.instagramclone.Home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.instagramclone.R;

/**
 * Created by Ramji on 9/6/2017.
 */

public class MessageFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MessageFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        return view;
    }
}
