package com.example.android.instagramclone.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.Utils.bottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Ramji on 9/5/2017.
 */

public class profileActivity extends AppCompatActivity{
    private static final String TAG = "profileActivity";
    private static final int ACTIVITY_NUM = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG,"onCreate: started");

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        progressBar.setVisibility(View.GONE);
        setupBottomNavigationView();
        setupToolBar();
    }

    private void setupToolBar(){

        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: navigating to account settings");
                Intent intent = new Intent(profileActivity.this,accountSettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * BottomNavigation View setup
     */
    private void setupBottomNavigationView(){

        Log.d(TAG,"setupBottomNavigationView: Setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        bottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        bottomNavigationViewHelper.enableNavigation(profileActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
