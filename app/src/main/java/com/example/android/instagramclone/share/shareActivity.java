package com.example.android.instagramclone.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.Utils.Permissions;
import com.example.android.instagramclone.Utils.SectionsPagerAdapter;
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

    private ViewPager mviewPager;

    private Context mContext = shareActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG,"onCreate: started");

        if (checkPermissionsArray(Permissions.PERMISSIONS)){
            setupViewPager();

        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }
    }

    /**
     * return the current tab nummber
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     * 2 = VideoFragment
     * @return
     */
    public int getCurrentTabNumber(){
        return mviewPager.getCurrentItem();
    }

    /**
     * setting up fragments
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());
        adapter.addFragment(new VideoFragment());

        mviewPager = (ViewPager) findViewById(R.id.container);
        mviewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mviewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.galley));
        tabLayout.getTabAt(1).setText(getString(R.string.photo));
        tabLayout.getTabAt(2).setText(getString(R.string.video));

    }

    public int getTask(){
        Log.d(TAG,"getTask: TASK: "+ getIntent().getFlags());
        return getIntent().getFlags();
    }

    /**
     * verify all the permissions passed to the array
     * @param permissions
     */

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

}
