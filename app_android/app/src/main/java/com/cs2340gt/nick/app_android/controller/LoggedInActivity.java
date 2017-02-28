package com.cs2340gt.nick.app_android.controller;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cs2340gt.nick.app_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
* created by SeanBills on 2/14/17.
* edited by SeanBills on 2/19/17.
 */

public class LoggedInActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button submitReportButton;
    private Button logoutButton;
//    private TextView textViewWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    // no one is being shown as logged in
                } else {
                    //
                }
            }
        };

        // buttons
        submitReportButton = (Button) findViewById(R.id.report_submit);
        logoutButton = (Button) findViewById(R.id.logout_button);
        submitReportButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

//        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
//        textViewWelcome.setText("WELCOME " + currentUser.getEmail());

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(View view) {

        if (view == submitReportButton) {
            finish();
            startActivity(new Intent(getApplicationContext(), WaterReportSubmitActivity.class));
        }

        if (view == logoutButton) {
            auth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }
}
