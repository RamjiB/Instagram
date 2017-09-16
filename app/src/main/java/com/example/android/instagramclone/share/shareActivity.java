package com.example.android.instagramclone.share;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.Utils.Permissions;
import com.example.android.instagramclone.Utils.bottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import static android.R.attr.permission;

/**
 * Created by Ramji on 9/5/2017.
 */

public class shareActivity extends AppCompatActivity{
    private static final String TAG = "shareActivity";

    //constants
    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST =1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG,"onCreate: started");

        if (checkPermissionsArray(Permissions.PERMISSIONS)){

        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }
        setupBottomNavigationView();
    }

    public void verifyPermissions(String[] permissions){
        Log.d(TAG,"verifyPermissions: verifying permission ");

        ActivityCompat.requestPermissions(shareActivity.this,permissions,VERIFY_PERMISSIONS_REQUEST);

    }

    /**
     * check an array of permisssions
     * @param permissions
     * @return
     */

    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG,"checkPermissionsArray: checking permissions array.");

        for (int i = 0; i < permissions.length; i++){
            String check = permissions[i];
            if (!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * check single permission
     * @param permission
     * @return
     */

    public boolean checkPermissions(String permission){
        Log.d(TAG,"checkPermissions: checking permission "+ permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(shareActivity.this,permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG,"checkPermissions:\n permission was not granted for: "+ permission);
            return false;
        }else{
            Log.d(TAG,"checkPermissions:\n permission was granted for: "+ permission);
            return true;
        }
    }

    /**
     * BottomNavigation View setup
     */
    private void setupBottomNavigationView(){

        Log.d(TAG,"setupBottomNavigationView: Setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        bottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        bottomNavigationViewHelper.enableNavigation(shareActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
