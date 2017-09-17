package com.example.android.instagramclone.Utils;

import android.os.Environment;

/**
 * Created by Ramji on 9/16/2017.
 */

public class FilePaths {

    //"storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String DCIM = ROOT_DIR + "/DCIM";


    public String FIREBASE_IMAGE_STORAGE = "photos/users/";
}
