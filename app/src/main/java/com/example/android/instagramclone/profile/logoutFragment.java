package com.example.android.instagramclone.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.instagramclone.R;

/**
 * Created by Viji on 9/10/2017.
 */

public class logoutFragment extends Fragment {

    private static final String TAG = "logoutFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout,container,false);
        return view;
    }
}
