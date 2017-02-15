package com.cs2340gt.nick.app_android.services;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cs2340gt.nick.app_android.MainActivity;
import com.cs2340gt.nick.app_android.R;

//import com.cs2340gt.nick.app_android.model.Account;

/**
 * Created by nick on 2/14/17.
 */

public class login_service extends MainActivity {

    private EditText username;
    private EditText password;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.username_input);
        password = (EditText) findViewById(R.id.password_input);
        error = (TextView) findViewById(R.id.error_text);
        error.setVisibility(TextView.INVISIBLE);
    }

    public void onLoginPressed(View view) {
        if(username.equals("user") && password.equals("pass")) {
            error.setVisibility(TextView.INVISIBLE);
            finish();
        } else {
            error.setVisibility(TextView.VISIBLE);
            finish();
        }
    }

}
