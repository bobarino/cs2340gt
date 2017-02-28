package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;

/**
 * Created by SEAN on 2/28/17.
 */

public class EditUserInfoActivity extends AppCompatActivity {
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
        setContentView(R.layout.edit_user_info);

        // fetch the true widgets from our view
        usernameField = (EditText) findViewById(R.id.editText2);
        passwordField = (EditText) findViewById(R.id.editText3);
        emailField = (EditText) findViewById(R.id.editText);
        userRadioButton = (RadioButton) findViewById(R.id.user_button);
        workerRadioButton = (RadioButton) findViewById(R.id.worker_button);
        managerRadioButton = (RadioButton) findViewById(R.id.worker_button);
        adminRadioButton = (RadioButton) findViewById(R.id.admin_button);

        Model model = Model.getInstance();
        System.out.println(model.getCurrentAccount());
        Account current = model.getCurrentAccount();
        System.out.println(current);
//        usernameField.setText(current.getUsername().toString());
//        passwordField.setText(model.getCurrentAccount().getPassword());
//        emailField.setText(model.getCurrentAccount().getEmailAddress());
//
//        Credential cred = model.getCurrentAccount().getCredential();
//        if (cred.equals("USER")) {
//            userRadioButton.setSelected(true);
//        } else if (cred.equals("WORKER")) {
//            workerRadioButton.setSelected(true);
//        } else if (cred.equals("MANAGER")) {
//            managerRadioButton.setSelected(true);
//        } else if (cred.equals("ADMIN")) {
//            adminRadioButton.setSelected(true);
//        }
    }

    /**
     *
     * @param view
     */
    protected void onAddPressed(View view) {
        Model model = Model.getInstance();
        Account update = model.getCurrentAccount();

        update.setUsername(usernameField.getText().toString());
        update.setPassword(passwordField.getText().toString());
        update.setEmailAddress(emailField.getText().toString());
        if (userRadioButton.isSelected()) {
            update.setCredential(Credential.USER);
        } else if (workerRadioButton.isSelected()) {
            update.setCredential(Credential.WORKER);
        } else if (managerRadioButton.isSelected()) {
            update.setCredential(Credential.MANAGER);
        } else if (adminRadioButton.isSelected()) {
            update.setCredential(Credential.ADMIN);
        }

        System.out.println(update);
        System.out.println(model.getCurrentAccount());
//        if (model.addAccount(account)) {
//            Intent intent =
//                    new Intent(getBaseContext(), LoggedInActivity.class);
//            startActivity(intent);
//        }
        Intent intent =
                new Intent(getBaseContext(), LoggedInActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    protected void onCancelPressed(View view) {
        Intent intent =
                new Intent(getBaseContext(), LoggedInActivity.class);
        startActivity(intent);
    }

}
