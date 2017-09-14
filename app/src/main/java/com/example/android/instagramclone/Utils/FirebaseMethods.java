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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.string.no;

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

    public boolean checkIfUSernameExists(String username, DataSnapshot datasnapshot) {
        Log.d(TAG,"checkIfUSernameExists : checking if "+ username + " already exists");

        User user = new User();
        for (DataSnapshot ds:datasnapshot.child(userID).getChildren()){

            Log.d(TAG,"checkIfUSernameExists: datasnapshot:" + ds);

            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG,"checkIfUSernameExists: username: " + user.getUsername());

            if (StringManipulation.expandUsername(user.getUsername()).equals(username)){
                Log.d(TAG,"checkIfUSernameExists: FOUND A MATCH: " + user.getUsername());
                return true;
            }
        }
        return false;
    }
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
                username,
                website
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);
    }
}
