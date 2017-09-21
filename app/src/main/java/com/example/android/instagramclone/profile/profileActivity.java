package com.example.android.instagramclone.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.Utils.GridImageAdapter;
import com.example.android.instagramclone.Utils.UniversalImageLoader;
import com.example.android.instagramclone.Utils.ViewCommentsFragment;
import com.example.android.instagramclone.Utils.ViewPostFragment;
import com.example.android.instagramclone.Utils.ViewProfileFragment;
import com.example.android.instagramclone.Utils.bottomNavigationViewHelper;
import com.example.android.instagramclone.models.Photo;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import static android.R.attr.fragment;

public class profileActivity extends AppCompatActivity implements ProfileFragment.OnGridImageSelectedListener,ViewPostFragment.OnCommentThreadSelectedListener{

    private static final String TAG = "profileActivity";


    @Override
    public void onCommentThreadSelectedListener(Photo photo) {
        Log.d(TAG,"onCommentThreadSelectedListener: selected a comment thread");

        ViewCommentsFragment fragment = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo),photo);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();
    }


    @Override
    public void onGridImageSelected(Photo photo, int activityNumber) {
        Log.d(TAG,"onGridImageSelected: selected an image gridview: "+ photo.toString());

        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo),photo);
        args.putInt(getString(R.string.activity_number),activityNumber);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();

    }
    private static final int ACTIVITY_NUM = 4;
    private ImageView profilePhoto;
    private ProgressBar progressBar;
    private static final int NUM_GRID_COLUMNS = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG,"onCreate: started");

        init();


    }

    private void init() {

        Log.d(TAG,"init: inflating" + getString(R.string.Profile_fragment));

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.calling_activity ))){
            Log.d(TAG,"init: searching for user object attached as intent extra");
            if (intent.hasExtra(getString(R.string.intent_user))){
                Log.d(TAG,"init : inflating view Profile");
                ViewProfileFragment fragment = new ViewProfileFragment();
                Bundle args = new Bundle();
                args.putParcelable(getString(R.string.intent_user),intent.getParcelableExtra(getString(R.string.intent_user)));
                fragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container,fragment);
                transaction.addToBackStack(getString(R.string.view_profile_fragment));
                transaction.commit();
            }else{
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        }else{
            Log.d(TAG,"init : inflating Profile");
            ProfileFragment fragment = new ProfileFragment();
            FragmentTransaction transaction = profileActivity.this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container,fragment);
            transaction.addToBackStack(getString(R.string.Profile_fragment));
            transaction.commit();
        }
    }
}
