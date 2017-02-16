package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Model;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

}
