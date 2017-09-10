package com.example.android.instagramclone.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.Utils.bottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viji on 9/9/2017.
 */

public class accountSettingsActivity extends AppCompatActivity {
    private static final String TAG = "accountSettingsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Log.d(TAG,"OnCreate Started");
        ImageView imageView = (ImageView) findViewById(R.id.backArrow);

        setupSettingsList();
        // setting up backArrow imageview to Prfile activity
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Go back to profile activity");
                finish();
            }
        });

    }

    private void setupSettingsList() {
        Log.d(TAG,"setup Settings List: initialise account settings");
        ListView listView = (ListView) findViewById(R.id.accountListView);
        ArrayList<String> account = new ArrayList<>();
        account.add(getString(R.string.Edit_Profile));
        account.add(getString(R.string.Logout));

        ArrayAdapter adapter = new ArrayAdapter(accountSettingsActivity.this,android.R.layout.simple_list_item_1,account);
        listView.setAdapter(adapter);
    }
//    /**
//     * BottomNavigation View setup
//     */
//    private void setupBottomNavigationView(){
//
//        Log.d(TAG,"setupBottomNavigationView: Setting up BottomNavigationView");
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
//        bottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        bottomNavigationViewHelper.enableNavigation(accountSettingsActivity.this,bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }
}
