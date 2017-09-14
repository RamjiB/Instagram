package com.example.android.instagramclone.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.instagramclone.Home.HomeActivity;
import com.example.android.instagramclone.R;
import com.example.android.instagramclone.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

/**
 * Created by Ramji on 9/10/2017.
 */

public class logoutFragment extends Fragment {

    private static final String TAG = "logoutFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressBar mProgressBAr;
    private TextView tvLogout,tvloggingout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout,container,false);

        tvLogout = (TextView) view.findViewById(R.id.tvLogout);
        mProgressBAr = (ProgressBar) view.findViewById(R.id.logoutProgressBar);
        tvloggingout = (TextView) view.findViewById(R.id.loggingout);

        Button btnlogout = (Button) view.findViewById(R.id.btn_Logout);


        mProgressBAr.setVisibility(View.GONE);
        tvloggingout.setVisibility(View.GONE);

        setupFirebaseAuth();


        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Onclick: Sttempting to log out.");

                mProgressBAr.setVisibility(View.VISIBLE);
                tvloggingout.setVisibility(View.VISIBLE);

                mAuth.signOut();
                getActivity().finish();
            }
        });


        return view;


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

                    Log.d(TAG,"onAuthStateChanged: navigating to login screen");
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
