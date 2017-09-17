package com.example.android.instagramclone.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.instagramclone.R;

/**
 * Created by Ramji on 9/6/2017.
 */

public class VideoFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "VideoFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video,container,false);

        Log.d(TAG,"onCreate View started.");
        return view;
    }
}
