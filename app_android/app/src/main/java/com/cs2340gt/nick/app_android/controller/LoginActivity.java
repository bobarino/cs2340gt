package com.cs2340gt.nick.app_android.controller;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by nick on 2/14/17.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPass;
    private TextView error;

    private Button loginButton, cancelButton, registerButton, editButton;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user signed in
                } else {
                    // no user signed in
                }
            }
        };

        error = (TextView) findViewById(R.id.error_text);
        error.setVisibility(TextView.INVISIBLE);
        editTextEmail = (EditText) findViewById(R.id.email_input);
        editTextPass = (EditText) findViewById(R.id.password_input);

        progressDialog = new ProgressDialog(this);

        // buttons
        loginButton = (Button) findViewById(R.id.login_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        registerButton = (Button) findViewById(R.id.register_button);
        loginButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

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

    /**
     *
     */
    protected void onLoginPressed() {
        final Model model = Model.getInstance();

        if (TextUtils.isEmpty(editTextEmail.toString())
                || TextUtils.isEmpty(editTextPass.toString())) {

            Toast.makeText(this, "Please fill all relevant fields.", Toast.LENGTH_SHORT).show();
            return;

        } else {
            final String email = editTextEmail.getText().toString();
            String pass = editTextPass.getText().toString();

            progressDialog.setMessage("Logging in...");
            progressDialog.show();

            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Successful",
                                        Toast.LENGTH_SHORT).show();
                                model.setCurrentAcc(model.findAccountByEmail(email));
                                finish();
                                startActivity(new Intent(getApplicationContext(),
                                        LoggedInActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed. Please try again.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == loginButton) {
            onLoginPressed();
        }
        if (view == cancelButton) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        if (view == registerButton) {
            finish();
            startActivity(new Intent(this, RegistrationActivity.class));
        }
    }

}
