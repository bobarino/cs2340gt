package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cs2340gt.nick.app_android.R;

/**
 * Created by ArmandoGonzalez on 2/15/17.
 */

public class WelcomeDirectionActivity extends AppCompatActivity {

    // button for logging in
    private Button loginButton;
    // button for registering user
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // fetch the true widgets from our view
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

    }

    /**
     * Directs application to the login activity screen.
     * @param view
     */
    protected void onLoginPressed(View view) {
        Intent intent =
                new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);    }

    /**
     * Directs application to the registration activity screen.
     * @param view
     */
    protected void onRegistrationPressed(View view) {
        Intent intent =
                new Intent(getBaseContext(), RegistrationActivity.class);
        startActivity(intent);    }

}
