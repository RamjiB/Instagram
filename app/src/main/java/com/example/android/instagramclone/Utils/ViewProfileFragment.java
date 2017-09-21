package com.example.android.instagramclone.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.models.Comment;
import com.example.android.instagramclone.models.Like;
import com.example.android.instagramclone.models.Photo;
import com.example.android.instagramclone.models.User;
import com.example.android.instagramclone.models.UserAccountSettings;
import com.example.android.instagramclone.models.UserSettings;
import com.example.android.instagramclone.profile.accountSettingsActivity;
import com.example.android.instagramclone.profile.profileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewProfileFragment extends Fragment{
    private static final String TAG = "ProfileFragment";

    public interface OnGridImageSelectedListener{
        void onGridImageSelected(Photo photo, int activityNumber);
    }
    OnGridImageSelectedListener mOnGridImageSelectedListener;

    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    //firebase

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    private TextView mPosts,mFollowers,mFollowing,mDisplayName,mUsername,mWebsite,mDescription,editProfile;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;
    private Context mContext;

    //vars
    private User mUser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_profile,container,false);
        mDisplayName =(TextView) view.findViewById(R.id.display_name);
        mUsername = (TextView) view.findViewById(R.id.username);
        mWebsite = (TextView) view.findViewById(R.id.website);
        mDescription = (TextView) view.findViewById(R.id.description);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_image);
        mPosts = (TextView) view.findViewById(R.id.tvposts);
        mFollowers = (TextView) view.findViewById(R.id.tvfollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvfollowing);
        mProgressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        gridView = (GridView) view.findViewById(R.id.gridView);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavigationViewBar);
        mContext = getActivity();

        Log.d(TAG,"onCreateView: started");

        try{
            mUser = getUSerFromBundle();
            init();
        }catch (NullPointerException e){
            Log.d(TAG,"onCreateView : NullPointerException : "+e.getMessage());
            Toast.makeText(mContext, "Soemthing went wrong", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }

        setupBottomNavigationView();
        setupToolBar();
        setupFirebaseAuth();
//        setupGridView();
//
//        editProfile = (TextView) view.findViewById(R.id.textEditProfile);
//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG,"onClick: navigating to "+ getActivity().getString(R.string.Edit_Profile));
//                Intent intent = new Intent(getActivity(),accountSettingsActivity.class);
//                intent.putExtra(getString(R.string.calling_activity),getString(R.string.profile_activity));
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//            }
//        });


        return view;
    }

    private void init(){
        //set the profile widgets
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference1
                .child(getString(R.string.dbname_user_account_settings))
                .orderByChild(getString(R.string.field_user_id)).equalTo(mUser.getUser_id());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Log.d(TAG,"onDataChange: foundUSer: "+ singleSnapshot.getValue(UserAccountSettings.class).toString());
                    UserSettings settings = new UserSettings();
                    settings.setUser(mUser);
                    settings.setSettings(singleSnapshot.getValue(UserAccountSettings.class));
                    setProfileWidgets(settings);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //get the users profile photos that he uploaded

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        Query query2 = reference2.child(getString(R.string.dbname_user_photos))
                .child(mUser.getUser_id());
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<Photo> photos = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Photo photo = new Photo();
                    Map<String, Object> objectsMap = (HashMap<String ,Object>) singleSnapshot.getValue();

                    photo.setCaption(objectsMap.get(getString(R.string.field_caption)).toString());
                    photo.setTags(objectsMap.get(getString(R.string.field_tags)).toString());
                    photo.setPhoto_id(objectsMap.get(getString(R.string.field_photo_id)).toString());
                    photo.setUser_id(objectsMap.get(getString(R.string.field_user_id)).toString());
                    photo.setDate_created(objectsMap.get(getString(R.string.field_date_created)).toString());
                    photo.setImage_path(objectsMap.get(getString(R.string.field_image_path)).toString());

                    Log.d(TAG,"map,Object");

                    ArrayList<Comment> comments = new ArrayList<Comment>();

                    for (DataSnapshot dSnapshot : singleSnapshot
                            .child(getActivity().getString(R.string.field_comments)).getChildren()){
                        Comment comment = new Comment();
                        comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                        comment.setComment(dSnapshot.getValue(Comment.class).getComment());
                        comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
                        comments.add(comment);
                    }

                    photo.setComments(comments);

                    List<Like> likesList = new ArrayList<Like>();
                    for (DataSnapshot dSnapshot : singleSnapshot
                            .child(getString(R.string.field_likes)).getChildren()){
                        Like like = new Like();
                        like.setUser_id(dSnapshot.getValue(Like.class).getUser_id());
                        likesList.add(like);

                    }
                    photo.setLikes(likesList);
                    photos.add(photo);
                }

                setupImageGrid(photos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setupImageGrid(final ArrayList<Photo> photos ){
        //SETUP PROFILE IMAGE GRID

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        ArrayList<String> imgUrls = new ArrayList<String>();
        for (int i =0 ; i <photos.size(); i++){
            imgUrls.add(photos.get(i).getImage_path());
        }
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview,"",imgUrls);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnGridImageSelectedListener.onGridImageSelected(photos.get(position),ACTIVITY_NUM);
            }
        });

    }

    private User getUSerFromBundle(){
        Log.d(TAG,"getUSerFromBundle: arguments: "+ getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null){
            return bundle.getParcelable(getString(R.string.intent_user));
        }else{
            return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        try{
            mOnGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG,"onAttach:ClassCast Exception: "+ e.getMessage());
        }
        super.onAttach(context);
    }


    private void setProfileWidgets(UserSettings userSettings){

        Log.d(TAG,"setProfileWidgets: setting widgets with data retrieved from database " + userSettings.toString() );

        User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(),mProfilePhoto,null,"");
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(user.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     //     * Responsible for setting profile tool bar
     //     */

    private void setupToolBar(){

        ((profileActivity)getActivity()).setSupportActionBar(toolbar);


        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: navigating to account settings");
                Intent intent = new Intent(getActivity(),accountSettingsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

    }

    /**
     //     * BottomNavigation View setup
     //     */
    private void setupBottomNavigationView(){

        Log.d(TAG,"setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        bottomNavigationViewHelper.enableNavigation(getActivity(),getActivity(),bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    /**
     * -------------------------------------Firebase----------------------------------------
     */

    /**
     * Setup the firebase auth object
     */

    private void setupFirebaseAuth(){
        Log.d(TAG,"setupFirebaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
