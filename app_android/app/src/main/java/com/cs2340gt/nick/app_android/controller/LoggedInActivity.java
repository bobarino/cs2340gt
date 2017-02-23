package com.cs2340gt.nick.app_android.controller;

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

    private FirebaseAuth firebaseAuth;

    private Button submitReportButton;
    private Button logoutButton;
    private TextView textViewWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser current = firebaseAuth.getCurrentUser();

        // buttons
        submitReportButton = (Button) findViewById(R.id.report_submit);
        logoutButton = (Button) findViewById(R.id.logout_button);

        // button listeners
        submitReportButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
        textViewWelcome.setText("WELCOME " + current.getEmail());

    }

    @Override
    public void onClick(View view) {

        if (view == submitReportButton) {
            finish();
            startActivity(new Intent(getBaseContext(), WaterReportSubmitActivity.class));
        }

        if (view == logoutButton) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
