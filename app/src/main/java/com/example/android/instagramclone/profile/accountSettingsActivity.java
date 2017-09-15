package com.example.android.instagramclone.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.Utils.SectionsStatePagerAdapter;
import com.example.android.instagramclone.Utils.bottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.instagramclone.R.id.backArrow;
import static com.example.android.instagramclone.R.string.Logout;

/**
 * Created by Ramji on 9/9/2017.
 */

public class accountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "accountSettingsActivity";
    private SectionsStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private static final int ACTIVITY_NUM = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Log.d(TAG,"OnCreate Started");

        mViewPager = (ViewPager) findViewById(R.id.container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayout1);

        setupSettingsList();
        setupBottomNavigationView();
        setupFragments();
        getIncomingIntent();

        // setting up backArrow imageview to Profile activity

        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Go back to profile activity");
                finish();
            }
        });

    }

    private  void getIncomingIntent(){
        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG,"getIncomingIntent: received Incoming intent from" + getString(R.string.profile_activity));
            setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.Edit_Profile)));
        }
    }
    private void setupFragments(){
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new editProfileFragment(),getString(R.string.Edit_Profile));
        pagerAdapter.addFragment(new logoutFragment(),getString(R.string.Logout));

    }
    private void setViewPager(int FragmentNumber){
        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG,"setViewPager: navigating to fragment #"+ FragmentNumber);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(FragmentNumber);

    }

    private void setupSettingsList() {
        Log.d(TAG,"setup Settings List: initialise account settings");
        ListView listView = (ListView) findViewById(R.id.accountListView);
        ArrayList<String> account = new ArrayList<>();
        account.add(getString(R.string.Edit_Profile));
        account.add(getString(Logout));

        ArrayAdapter adapter = new ArrayAdapter(accountSettingsActivity.this,android.R.layout.simple_list_item_1,account);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"setItemClick : navigation to fragment #"+position);
                setViewPager(position);
                
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
        bottomNavigationViewHelper.enableNavigation(accountSettingsActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
