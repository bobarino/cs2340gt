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
    private EditText editTextUser;
    private EditText editTextPass;
    private EditText editTextEmail;
    private RadioGroup credentialsRadioGroup;
    private RadioButton userRadioButton;
    private RadioButton workerRadioButton;
    private RadioButton managerRadioButton;
    private RadioButton adminRadioButton;

    // Account that is being created / changed
    private Account account;

    // A boolean telling whether or not an account is being edited
    public static boolean editing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (editing) {
            setContentView(R.layout.content_edit_account);
        } else {
            setContentView(R.layout.content_register_account);
        }

        // fetch the true widgets from our view
        editTextUser = (EditText) findViewById(R.id.user_input);
        editTextPass = (EditText) findViewById(R.id.pass_input);
        editTextEmail = (EditText) findViewById(R.id.email_input);
        credentialsRadioGroup = (RadioGroup) findViewById(R.id.cred_group);
        userRadioButton = (RadioButton) findViewById(R.id.cred_user);
        workerRadioButton = (RadioButton) findViewById(R.id.cred_worker);
        managerRadioButton = (RadioButton) findViewById(R.id.cred_manager);
        adminRadioButton = (RadioButton) findViewById(R.id.cred_admin);

        if (workerRadioButton.isSelected()) {
            account = new Account(editTextUser.getText().toString(),
                    editTextPass.getText().toString(),
                    editTextEmail.getText().toString(),
                    Credential.WORKER);
        } else if (managerRadioButton.isSelected()) {
            account = new Account(editTextUser.getText().toString(),
                    editTextPass.getText().toString(),
                    editTextEmail.getText().toString(),
                    Credential.MANAGER);
        } else if (adminRadioButton.isSelected()) {
            account = new Account(editTextUser.getText().toString(),
                    editTextPass.getText().toString(),
                    editTextEmail.getText().toString(),
                    Credential.ADMIN);
        } else {
            account = new Account(editTextUser.getText().toString(),
                    editTextPass.getText().toString(),
                    editTextEmail.getText().toString(),
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
            successful = model.addAccountInfo(account);
        }

        // TESTING
        if (successful) {
            setContentView(R.layout.logged_in);
            System.out.println(model.getAccountList());
        }

        // ACTUAL
//        if (successful) {
//            Intent intent =
//                    new Intent(getBaseContext(), MainActivity.class);
//            startActivity(intent);
//        }

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
