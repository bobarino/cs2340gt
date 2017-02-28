package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    private RadioButton selectedRadioButton;
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
        setContentView(R.layout.content_registration);

        // fetch the true widgets from our view
        usernameField = (EditText) findViewById(R.id.user_input);
        passwordField = (EditText) findViewById(R.id.pass_input);
        emailField = (EditText) findViewById(R.id.email_input);
//        credentialsRadioGroup = (RadioGroup) findViewById(R.id.cred_group);
//        selectedRadioButton = (RadioButton) findViewById(credentialsRadioGroup
//                .getCheckedRadioButtonId());
        userRadioButton = (RadioButton) findViewById(R.id.cred_user);
        workerRadioButton = (RadioButton) findViewById(R.id.cred_worker);
        managerRadioButton = (RadioButton) findViewById(R.id.cred_manager);
        adminRadioButton = (RadioButton) findViewById(R.id.cred_admin);
    }

    /**
     *
     * @param view
     */
    protected void onAddPressed(View view) {
        Model model = Model.getInstance();
        account = new Account();

        account.setUsername(usernameField.getText().toString());
        account.setPassword(passwordField.getText().toString());
        account.setEmailAddress(emailField.getText().toString());
        if (userRadioButton.isSelected()) {
            account.setCredential(Credential.USER);
        } else if (workerRadioButton.isSelected()) {
            account.setCredential(Credential.WORKER);
        } else if (managerRadioButton.isSelected()) {
            account.setCredential(Credential.MANAGER);
        } else if (adminRadioButton.isSelected()) {
            account.setCredential(Credential.ADMIN);
        }


//         TODO: make distinction for editing
//        if (editing) {
//            account.setUsername(usernameField.getText().toString());
//            account.setPassword(passwordField.getText().toString());
//            account.setEmailAddress(emailField.getText().toString());
//            account.setCredential(Credential.valueOf(selectedRadioButton.getText().toString()));
//        } else {
//
//        }

        if (model.addAccount(account)) {
            Intent intent =
                    new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
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
