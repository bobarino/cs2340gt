package com.cs2340gt.nick.app_android.controller;

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

public class EditAccountInfoActivity extends AppCompatActivity {

    // Widgets represented in the view
    private EditText usernameField;
    private EditText passwordField;
    private EditText emailField;
    private RadioGroup credentialsRadioGroup;
    private RadioButton userRadioButton;
    private RadioButton workerRadioButton;
    private RadioButton managerRadioButton;
    private RadioButton adminRadioButton;

    // Account that is being created / changed
    private Account account;

    // TODO: add modifications to process for 'editing'
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fetch the true widgets from our view
        usernameField = (EditText) findViewById(R.id.user_input);
        passwordField = (EditText) findViewById(R.id.pass_input);
        emailField = (EditText) findViewById(R.id.email_input);
        credentialsRadioGroup = (RadioGroup) findViewById(R.id.cred_group);
        userRadioButton = (RadioButton) findViewById(R.id.cred_user);
        workerRadioButton = (RadioButton) findViewById(R.id.cred_worker);
        managerRadioButton = (RadioButton) findViewById(R.id.cred_manager);
        adminRadioButton = (RadioButton) findViewById(R.id.cred_admin);

    }

    protected void onAddPressed(View view) {
        Model model = Model.getInstance();

        // set text-based fields
        account.setUsername(usernameField.getText().toString());
        account.setPassword(passwordField.getText().toString());
        account.setEmailAddress(emailField.getText().toString());

        // set credential based on radio group, user is default
        if (workerRadioButton.isSelected()) {
            account.setCredential(Credential.WORKER);
        } else if (managerRadioButton.isSelected()) {
            account.setCredential(Credential.MANAGER);
        } else if (adminRadioButton.isSelected()) {
            account.setCredential(Credential.ADMIN);
        } else {
            account.setCredential(Credential.USER);
        }

        model.addAccount(account);

        // TODO: make distinction for editing
    }

    protected void onCancelPressed(View view) {
        //
    }

}
