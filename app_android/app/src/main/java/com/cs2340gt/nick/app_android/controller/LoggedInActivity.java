package com.cs2340gt.nick.app_android.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.cs2340gt.nick.app_android.R;

/*
* created by SeanBills on 2/14/17.
* edited by SeanBills on 2/19/17.
 */

public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);

        Button submitReportButton = (Button) findViewById(R.id.report_submit);
        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), WaterReportSubmitActivity.class);
                startActivity(intent);
            }
        });

        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
