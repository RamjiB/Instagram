package com.example.android.instagramclone.profile;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Ramji on 9/10/2017.
 */

public class editProfileFragment extends Fragment {

    private static final String TAG = "editProfileFragment";
    private ImageView profilePhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        profilePhoto = (ImageView) view.findViewById(R.id.profileImage);

        setProfileImage();

        // cross image to go back to profile activity
        ImageView cross = (ImageView) view.findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                getActivity().finish();
            }
        });
        return view;
    }


    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting Profile Image");
        String imgURL = "secure.defenders.org/ac/img/carousel-tiger-900x500.jpg";
        UniversalImageLoader.setImage(imgURL, profilePhoto, null, "https://");
    }
}
