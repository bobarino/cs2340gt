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

    private EditText username;
    private EditText password;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        username = (EditText) findViewById(R.id.username_input);
        password = (EditText) findViewById(R.id.password_input);
        error = (TextView) findViewById(R.id.error_text);
        error.setVisibility(TextView.INVISIBLE);

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
            }
        });
    }

    protected void onLoginPressed(View view) {
        Model model = Model.getInstance();
        Account cur_user = model.findAccountByUser(username.getText().toString());
        if (cur_user.getCredential().identify() != "N" && cur_user.getPassword() == password.getText().toString()) {
            model.setCurrentAcc(cur_user);
            Intent intent =
                    new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        } else {
            username.setText("");
            password.setText("");
            error.setVisibility(TextView.VISIBLE);
        }
    }

    protected void onCancelPressed(View view) {
        //
    }

}
