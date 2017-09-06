package com.example.android.instagramclone.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.instagramclone.Home.HomeActivity;
import com.example.android.instagramclone.R;
import com.example.android.instagramclone.notification.notificationActivity;
import com.example.android.instagramclone.profile.profileActivity;
import com.example.android.instagramclone.search.searchActivity;
import com.example.android.instagramclone.share.shareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Ramji on 9/5/2017.
 */

public class bottomNavigationViewHelper {

    private static final String TAG = "bottomNavigationViewHel";
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){

        Log.d(TAG,"setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context,BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_home:
                        Intent intent1 = new Intent(context, HomeActivity.class);//ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(context, searchActivity.class);//ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_add:
                        Intent intent3 = new Intent(context,shareActivity.class);//ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_notifiaction:
                        Intent intent4 = new Intent(context,notificationActivity.class);//ACTIVITY_NUM = 3
                        context.startActivity(intent4);
                        break;
                    case R.id.ic_profile:
                        Intent intent5 = new Intent(context,profileActivity.class);//ACTIVITY_NUM = 4
                        context.startActivity(intent5);
                        break;
                }
                return false;
            }
        });
    }
}
