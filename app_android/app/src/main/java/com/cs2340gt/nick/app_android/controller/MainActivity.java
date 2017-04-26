package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cs2340gt.nick.app_android.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == loginButton) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        if (view == registerButton) {
            finish();
            startActivity(new Intent(this, RegistrationActivity.class));
        }

    }

}
