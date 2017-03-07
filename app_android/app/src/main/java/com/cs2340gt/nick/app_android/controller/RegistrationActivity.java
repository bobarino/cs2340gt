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

    // editText for the username of registration
    private EditText usernameField;
    // edittext for password of registration
    private EditText passwordField;
    // editText for email of registartion
    private EditText emailField;
    // radioGroup for controlling which button is selected
    private RadioGroup credentialsRadioGroup;
    // radio button for selected button
    private RadioButton selectedRadioButton;
    // radio button for user button
    private RadioButton userRadioButton;
    // radio button for worker button
    private RadioButton workerRadioButton;
    // radio button for manager button
    private RadioButton managerRadioButton;
    // radio button for admin button
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
     * update the model and the view upon pressing hte add button
     * @param view the button
     */
    protected void onAddPressed(View view) {
        Model model = Model.getInstance();
        if (model.getCurrentAccount() != null) {
            account = model.getCurrentAccount();
            editing = true;
        } else {
            account = new Account();
        }

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
        System.out.println(account);
        System.out.println(model.getCurrentAccount());
        System.out.println(model.getAccountList());
        if (editing == true) {
            Intent intent = new Intent(getBaseContext(), LoggedInActivity.class);
            startActivity(intent);
        } else {
            if (model.addAccount(account)) {
                System.out.println(model.getAccountList());
                Intent intent =
                        new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        }


    }

    /**
     * update the model and the view upon pressing the cancel button
     * @param view the button
     */
    protected void onCancelPressed(View view) {
        Model model = Model.getInstance();
        System.out.println(account);
        System.out.println(model.getAccountList());
        if (model.getCurrentAccount() != null) {
            Intent intent = new Intent(getBaseContext(), LoggedInActivity.class);
            startActivity(intent);
        } else {
            Intent intent =
                    new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }

    }

}
