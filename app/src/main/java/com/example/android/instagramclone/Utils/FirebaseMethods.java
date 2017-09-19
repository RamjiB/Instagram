package com.example.android.instagramclone.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import com.example.android.instagramclone.Home.HomeActivity;
import com.example.android.instagramclone.R;
import com.example.android.instagramclone.login.RegisterActivity;
import com.example.android.instagramclone.models.Photo;
import com.example.android.instagramclone.models.User;
import com.example.android.instagramclone.models.UserAccountSettings;
import com.example.android.instagramclone.models.UserSettings;
import com.example.android.instagramclone.profile.accountSettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Locale;

import static android.R.string.no;
import static com.example.android.instagramclone.R.id.accountSettings;
import static com.example.android.instagramclone.R.id.username;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageRefernece;

    private Context mContext;
    private double mPhotoUploadProgress;

    public FirebaseMethods(Context context){

        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mStorageRefernece = FirebaseStorage.getInstance().getReference();

        if (mAuth.getCurrentUser() != null){

            userID = mAuth.getCurrentUser().getUid();

        }
    }

    public void uploadNewPhoto(String photoType, final String caption, int count, final String imgUrl,Bitmap bm){

        Log.d(TAG,"uploadNewPhoto: attempting to upload a new photo");

        FilePaths filePaths = new FilePaths();

        //case 1: new photo

        if (photoType.equals(mContext.getString(R.string.new_photo))){
            Log.d(TAG,"uploadNewPhoto: uploading NEW photo");

            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = mStorageRefernece
                    .child(filePaths.FIREBASE_IMAGE_STORAGE +"/"+ user_id + "/photo" + (count +1));

            //convert image uri to bitmap
            if (bm == null){
                bm = ImageManager.getBitmap(imgUrl);
            }

            byte[] bytes= ImageManager.getBytesFromBitmap(bm,100);

            UploadTask uploadTask = null;
            uploadTask =storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri firebaseUrl = taskSnapshot.getDownloadUrl();

                    Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();

                    // add the new photo to 'photos' node and 'users_photo' node

                    addPhotoToDatabase(caption, firebaseUrl.toString());

                    //navigate to the main feed so the user cans ee their photo

                    Intent intent = new Intent(mContext, HomeActivity.class);
                    mContext.startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d(TAG,"onFailure: photo upload failed");
                    Toast.makeText(mContext, "photo upload failed", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100 * taskSnapshot.getBytesTransferred())/ taskSnapshot.getTotalByteCount();

                    if (progress - 15 > mPhotoUploadProgress){
                        Toast.makeText(mContext, "photo upload progress: "+ String.format("%.0f",progress) + "%", Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }
                    Log.d(TAG,"onProgress: upload progress: "+ progress+ " % done");
                }
            });

        }

        //case 2: new profile photo

        else if (photoType.equals(mContext.getString(R.string.profile_photo))){
            Log.d(TAG,"uploadNewPhoto: uploading new PROFILE photo");
            ((accountSettingsActivity)mContext).setViewPager(
                    ((accountSettingsActivity)mContext).pagerAdapter
                            .getFragmentNumber(mContext.getString(R.string.Edit_Profile))
            );

            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = mStorageRefernece
                    .child(filePaths.FIREBASE_IMAGE_STORAGE +"/"+ user_id + "/profile_photo" );

            //convert image uri to bitmap
            if (bm == null){
                bm = ImageManager.getBitmap(imgUrl);
            }
            byte[] bytes= ImageManager.getBytesFromBitmap(bm,100);

            UploadTask uploadTask = null;
            uploadTask =storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri firebaseUrl = taskSnapshot.getDownloadUrl();

                    Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();

                    //insert into the user_account_settings node
                    setProfilePhoto(firebaseUrl.toString());




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d(TAG,"onFailure: photo upload failed");
                    Toast.makeText(mContext, "photo upload failed", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100 * taskSnapshot.getBytesTransferred())/ taskSnapshot.getTotalByteCount();

                    if (progress - 15 > mPhotoUploadProgress){
                        Toast.makeText(mContext, "photo upload progress: "+ String.format("%.0f",progress) + "%", Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }
                    Log.d(TAG,"onProgress: upload progress: "+ progress+ " % done");
                }
            });

        }
    }

    private void setProfilePhoto(String url){
        Log.d(TAG,"setProfilePhoto: setting profile photo: "+ url);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mContext.getString(R.string.profile_photo))
                .setValue(url);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getTimeStamp(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        return sdf.format(new Date());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addPhotoToDatabase(String caption, String url){
        Log.d(TAG,"addPhotoToDatabase: adding photo to database");

        String tags = StringManipulation.getTags(caption);
        String newPhotoKey = myRef.child(mContext.getString(R.string.dbname_photos)).push().getKey();
        Photo photo = new Photo();
        photo.setCaption(caption);
        photo.setDate_created(getTimeStamp());
        photo.setImage_path(url);
        photo.setTags(tags);
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setPhoto_id(newPhotoKey);;

        //insert into database
        myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(newPhotoKey).setValue(photo);
        myRef.child(mContext.getString(R.string.dbname_photos))
                .child(newPhotoKey).setValue(photo);
    }

    public int getImageCount(DataSnapshot datasnapshot){

        int count = 0;
        for (DataSnapshot ds: datasnapshot
                .child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()){
            count++;
        }

        return count;
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

        Log.d(TAG,"updateEmail: updating email to "+ email);

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
                        Toast.makeText(mContext, "check your email inbox", Toast.LENGTH_SHORT).show();
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
                website,
                userID
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
