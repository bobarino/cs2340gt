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

    // edit text for updating username of user
    private EditText usernameField;
    // edit text for updating password of user
    private EditText passwordField;
    // edit text for updating email address
    private EditText emailField;
    // radio group for holding all possible credentials
    private RadioGroup credentialsRadioGroup;
    // radio button for selected radio button
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


    /**
     * updates the model based on the information in the current InstanceState
     * upon creation of the screen/activity object
     * @param savedInstanceState the instance state to be used to create the new
     *                           view/activity object
     */
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
    }

    /**
     * update the model when the user presses the "ADD" button
     * update to update the updated user's information
     * @param view the button
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

        /**
         * display the new view (the logged in state screen)
         */
        Intent intent =
                new Intent(getBaseContext(), LoggedInActivity.class);
        startActivity(intent);
    }

    /**
     * boot the user back out to the logged in screen without updating their information
     * at all if they press the cancel button
     * @param view the button
     */
    protected void onCancelPressed(View view) {
        Intent intent =
                new Intent(getBaseContext(), LoggedInActivity.class);
        startActivity(intent);
    }

}
