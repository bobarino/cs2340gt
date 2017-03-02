package com.cs2340gt.nick.app_android.controller;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.cs2340gt.nick.app_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.cs2340gt.nick.app_android.model.Model;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        currentUser = auth.getCurrentUser();
                        if (currentUser == null) {
                            // no user is signed in
                        } else {
                            // some user is signed in
                        }
                    }
                };
            }
        };

        // buttons
        submitReportButton = (Button) findViewById(R.id.report_submit);
        logoutButton = (Button) findViewById(R.id.logout_button);
        submitReportButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);


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
        Model model = Model.getInstance();

        if (view == submitReportButton) {
            finish();
            startActivity(new Intent(getApplicationContext(), WaterReportSubmitActivity.class));
        }

        if (view == logoutButton) {
            auth.getInstance().signOut();
            model.setCurrentAcc(null);
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        if (view == submitReportButton)

        // the controller for the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model model = Model.getInstance();
                model.setCurrentAcc(null);
                Intent intent =
                        new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // controller for the edit user information button
        Button editInfoButton = (Button) findViewById(R.id.edit_user);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        Button showReportList = (Button) findViewById(R.id.show_reports_button);
        showReportList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), WaterReportListActivity.class);
                startActivity(intent);
            }
        });
    }
}
