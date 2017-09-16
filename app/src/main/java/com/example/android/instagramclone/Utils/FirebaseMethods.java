package com.example.android.instagramclone.Utils;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import com.example.android.instagramclone.R;
import com.example.android.instagramclone.login.RegisterActivity;
import com.example.android.instagramclone.models.User;
import com.example.android.instagramclone.models.UserAccountSettings;
import com.example.android.instagramclone.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.string.no;
import static com.example.android.instagramclone.R.id.username;

/**
 * Created by Ramji on 9/12/2017.
 */

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private Context mContext;

    public FirebaseMethods(Context context){

        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){

            userID = mAuth.getCurrentUser().getUid();

        }
    }

    /**
     * update username in users node and user account settings node
     * @param username
     */

    public void updateUsername(String username){

        Log.d(TAG,"updateUSernaem: updating username to "+ username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }

    /**
     * update email in the users node
     * @param email
     */

    public void updateEmail(String email){

        Log.d(TAG,"updateUSernaem: updating username to "+ email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);

    }

    /**
     * update user Account settings
     */
    public void updateUserAccountSettings(String displayName,String website,String description,long phoneNumber){

        Log.d(TAG,"updating user account settings");

        if (displayName != null){
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_display_name))
                    .setValue(displayName);
        }
        if (website != null){
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_website))
                    .setValue(website);
        }
        if (description != null){
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }
        if (phoneNumber != 0){

            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_phone_number))
                    .setValue(phoneNumber);

        }

    }


//    public boolean checkIfUSernameExists(String username, DataSnapshot datasnapshot) {
//        Log.d(TAG,"checkIfUSernameExists : checking if "+ username + " already exists");
//
//        User user = new User();
//        for (DataSnapshot ds:datasnapshot.child(userID).getChildren()){
//
//            Log.d(TAG,"checkIfUSernameExists: datasnapshot:" + ds);
//
//            user.setUsername(ds.getValue(User.class).getUsername());
//            Log.d(TAG,"checkIfUSernameExists: username: " + user.getUsername());
//
//            if (StringManipulation.expandUsername(user.getUsername()).equals(username)){
//                Log.d(TAG,"checkIfUSernameExists: FOUND A MATCH: " + user.getUsername());
//                return true;
//            }
//        }
//        return false;
//    }
    /**
     * Register a new email and password to Firebase Authentication
     */

    public void registerNewEmail(final String email, final String username,String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }else if (task.isSuccessful()){

                            //send verification email
                            sendVerificationEmail();

                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG,"onComplete: Authstate changed: " + userID);
                        }

                        // ...
                    }
                });
    }

    public void sendVerificationEmail(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                    }else {
                        Toast.makeText(mContext,"couldn't sent verifiaction email.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * Add information to the user nodes
     * Add information to the user_account_settings
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */

    public void addNewUser(String email, String username, String description, String website, String profile_photo){

        User user = new User(userID, email, 1, StringManipulation.condenseUsername(username));

        myRef.child(mContext.getString(R.string.dbname_users))
        .child(userID)
        .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);
    }

    /**
     * Retrieves user account details
     * DAtabase: user_account_settings node
     * @param dataSnapshot
     * @return
     */


    public UserSettings getUserSettings(DataSnapshot dataSnapshot){
        Log.d(TAG,"getUserAccountSettings: retrieving user account settings from database");

        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();

        for (DataSnapshot ds : dataSnapshot.getChildren()){

            // user accouint settings node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))){
                Log.d(TAG,"getUserAccountSettings: datasnapshot "+ ds);

                try {

                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );
                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    Log.d(TAG,"getUserAccountSettings: retrieved user account settings from database" + settings.toString());

                }catch(NullPointerException e){
                    Log.d(TAG,"getAccountSettings: NullPointerException "+ e.getMessage());
                }

            }

            // users node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getusers: datasnapshot " + ds);

                try {
                    user.setUser_id(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUser_id()
                    );
                    user.setEmail(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getEmail()
                    );
                    user.setPhone_number(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getPhone_number()
                    );
                    user.setUsername(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUsername()
                    );
                    Log.d(TAG, "getUserAccountSettings: retrieved users details from database" + user.toString());


                } catch (NullPointerException e) {
                    Log.d(TAG, "getAccountSettings: NullPointerException " + e.getMessage());
                }

            }
        }

        return new UserSettings(user,settings);
    }
}
