package com.cs2340gt.nick.app_android.controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.Account;

/**
 * Created by nick on 2/14/17.
 */

public class LoginActivity extends AppCompatActivity {

    // editText to hold entered username for login
    private EditText username;
    // editText for entered password for login
    private EditText password;
    // TextView to hold error message if value is missing
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        username = (EditText) findViewById(R.id.username_input);
        password = (EditText) findViewById(R.id.password_input);
        error = (TextView) findViewById(R.id.error_text);
        error.setVisibility(TextView.INVISIBLE);

    }

    /**
     * method to handle when the Login button is pressed
     * @param view the login button
     */
    protected void onLoginPressed(View view) {
        Model model = Model.getInstance();
        Account cur_user = model.findAccountByUser(username.getText().toString());
//        if (!(cur_user.getCredential().identify().equals("N"))
//                && cur_user.getPassword().equals(password.getText().toString())) {
        if (username.getText().toString().equals(cur_user.getUsername().toString())
                && password.getText().toString().equals(cur_user.getPassword().toString())) {
            model.setCurrentAcc(cur_user);
            System.out.println(model.getCurrentAccount());
            Intent intent =
                    new Intent(getBaseContext(), LoggedInActivity.class);
            startActivity(intent);
        } else {
            username.setText("");
            password.setText("");
            error.setVisibility(TextView.VISIBLE);
        }
    }

    /**
     * method to handle the cancel button being pressed
     * @param view the cancel button
     */
    protected void onCancelPressed(View view) {
        Intent intent =
                new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

}
