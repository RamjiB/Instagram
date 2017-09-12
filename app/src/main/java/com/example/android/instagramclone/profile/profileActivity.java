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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.Utils.GridImageAdapter;
import com.example.android.instagramclone.Utils.UniversalImageLoader;
import com.example.android.instagramclone.Utils.bottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * Created by Ramji on 9/5/2017.
 */

public class profileActivity extends AppCompatActivity{
    private static final String TAG = "profileActivity";
    private static final int ACTIVITY_NUM = 4;
    private ImageView profilePhoto;
    private ProgressBar progressBar;
    private static final int NUM_GRID_COLUMNS = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG,"onCreate: started");


        setupBottomNavigationView();
        setupToolBar();
        setupActivityWidgets();
        setupProfileImage();
        tempGridSetup();
    }

    private  void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://i.pinimg.com/736x/e6/19/a4/e619a4eb8af0a0bef8e9316c4bbe5f61--the-lion-king-a-lion.jpg");
        imgURLs.add("http://www.readersdigest.ca/wp-content/uploads/2011/01/4-ways-cheer-up-depressed-cat.jpg");
        imgURLs.add("http://1.bp.blogspot.com/-QS8DdjQq32g/VTZx2vJ_rHI/AAAAAAAAEqQ/gtMZsY8mFr8/s1600/puppy-500x350.jpg");
        imgURLs.add("https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAx3AAAAJDM4NmUzN2I2LTJiZjAtNDFiNS04ZmMxLWQ3OTlkZjI0NzM2NA.jpg");
        imgURLs.add("https://d1j3wd17d78ehn.cloudfront.net/system/images/000/062/850/a20b55aa99e08784986e3ca9373f91d9/original/Botanical-Garden-ooty.jpg?1491543015");
        imgURLs.add("http://travel.home.sndimg.com/content/dam/images/travel/fullset/2015/08/03/top-florida-beaches/key-west-beach-florida.jpg.rend.hgtvcom.966.725.suffix/1491580836931.jpeg");
        imgURLs.add("https://www.keralatourism.org/images/picture/large/Athirapally_falls_in_Thrissur_16.jpg");
        imgURLs.add("http://data.whicdn.com/images/4079214/original.jpg");
        imgURLs.add("https://media.cntraveler.com/photos/53daa81adcd5888e145c32a7/master/w_775,c_limit/tbt-the-best-beach-vacation-2.jpg");

        setupImageGrid(imgURLs);

    }
    private void setupImageGrid(ArrayList<String> imgURLs){
        GridView gridView = (GridView) findViewById(R.id.gridView);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);
        GridImageAdapter adapter = new GridImageAdapter(profileActivity.this, R.layout.layout_grid_imageview,"",imgURLs);
        gridView.setAdapter(adapter);
    }
    private void setupProfileImage(){
        Log.d(TAG,"setProfileImage: setting profile photo");
        String imgURL = "secure.defenders.org/ac/img/carousel-tiger-900x500.jpg";
        UniversalImageLoader.setImage(imgURL,profilePhoto,progressBar,"https://");


    }

    private void setupActivityWidgets(){
        progressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        progressBar.setVisibility(View.GONE);

        profilePhoto = (ImageView) findViewById(R.id.profile_image);

    }
    /**
     * Responsible for setting profile tool bar
     */

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
