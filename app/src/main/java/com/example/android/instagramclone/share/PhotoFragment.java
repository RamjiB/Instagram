package com.example.android.instagramclone.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.Utils.Permissions;
import com.example.android.instagramclone.profile.accountSettingsActivity;

/**
 * Created by Ramji on 9/6/2017.
 */

public class PhotoFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "PhotoFragment";

    //constants

    private static final int GALLERY_FRAGMENT_NUMBER = 0;
    private static final int PHOTO_FRAGMENT_NUMBER = 1;
    private static final int VIDEO_FRAGMENT_NUMBER = 2;
    private static final int CAMERA_REQUEST_CODE = 5;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        Log.d(TAG,"onCreate View started.");

        Button btnLaunchCamera = (Button) view.findViewById(R.id.launchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: launching camera.");

                if (((shareActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUMBER) {

                    if (((shareActivity) getActivity()).checkPermissions(Permissions.CAMERA_PERMISSIONS[0])) {
                        Log.d(TAG,"onClick: starting camera");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
                    }else{
                        Intent intent = new Intent(getActivity(),shareActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

        return view;
    }

    private boolean isRootTask(){
        if (((shareActivity)getActivity()).getTask() == 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE){
            Log.d(TAG,"onActivityResult : done taking a photo");
            Log.d(TAG,"onActivityResult : navigating to share screen");

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");


            if (isRootTask()){

                try{
                    Log.d(TAG,"onActivityResult: received new bitmap from camera: "+ bitmap);
                    Intent intent = new Intent(getActivity(),NextActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap),bitmap);
                    startActivity(intent);

                }catch (NullPointerException e){
                    Log.d(TAG,"onActivityResult: NullPointerExeception: "+ e.getMessage());
                }


            }else{

                try{
                    Log.d(TAG,"onActivityResult: received new bitmap from camera: "+ bitmap);
                    Intent intent = new Intent(getActivity(),accountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap),bitmap);
                    intent.putExtra(getString(R.string.return_to_fragment),getString((R.string.Edit_Profile)));
                    startActivity(intent);
                    getActivity().finish();

                }catch (NullPointerException e){
                    Log.d(TAG,"onActivityResult: NullPointerExeception: "+ e.getMessage());
                }
            }


        }
    }
}
