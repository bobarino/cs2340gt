package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public class RegistrationActivity extends AppCompatActivity {

    // Widgets represented in the view
    private EditText usernameField;
    private EditText passwordField;
    private EditText emailField;
    private RadioGroup credentialsRadioGroup;
    private RadioButton userRadioButton;
    private RadioButton workerRadioButton;
    private RadioButton managerRadioButton;
    private RadioButton adminRadioButton;
    private RadioButton selectedRadioButton;

    // Account that is being created / changed
    private Account account;

    // A boolean telling whether or not an account is being edited
    // TODO: create and expose a service to allow editing to be set true
    public static boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // fetch the true widgets from our view
        usernameField = (EditText) findViewById(R.id.user_input);
        passwordField = (EditText) findViewById(R.id.pass_input);
        emailField = (EditText) findViewById(R.id.email_input);
        credentialsRadioGroup = (RadioGroup) findViewById(R.id.cred_group);
        userRadioButton = (RadioButton) findViewById(R.id.cred_user);
        workerRadioButton = (RadioButton) findViewById(R.id.cred_worker);
        managerRadioButton = (RadioButton) findViewById(R.id.cred_manager);
        adminRadioButton = (RadioButton) findViewById(R.id.cred_admin);

        if (workerRadioButton.isSelected()) {
            account = new Account(usernameField.getText().toString(),
                    passwordField.getText().toString(),
                    emailField.getText().toString(),
                    Credential.WORKER);
        } else if (managerRadioButton.isSelected()) {
            account = new Account(usernameField.getText().toString(),
                    passwordField.getText().toString(),
                    emailField.getText().toString(),
                    Credential.MANAGER);
        } else if (adminRadioButton.isSelected()) {
            account = new Account(usernameField.getText().toString(),
                    passwordField.getText().toString(),
                    emailField.getText().toString(),
                    Credential.ADMIN);
        } else {
            account = new Account(usernameField.getText().toString(),
                    passwordField.getText().toString(),
                    emailField.getText().toString(),
                    Credential.USER);
        }
    }

    /**
     *
     * @param view
     */
    protected void onAddPressed(View view) {
        Model model = Model.getInstance();
        boolean successful;

        if (editing) {
            successful = model.editAccountInfo(account);
        } else {
            successful = model.addAccount(account);
        }


        if (successful) {
            setContentView(R.layout.logged_in);
            System.out.println(model.getAccountList());
//            Intent intent =
//                    new Intent(getBaseContext(), MainActivity.class);
//            startActivity(intent);
        }

    }

    /**
     *
     * @param view
     */
    protected void onCancelPressed(View view) {
        Intent intent =
                new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

}
