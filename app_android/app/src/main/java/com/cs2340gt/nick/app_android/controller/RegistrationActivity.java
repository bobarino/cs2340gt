package com.cs2340gt.nick.app_android.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    // Widgets represented in the view
    private EditText editTextEmail, editTextPass;
    private TextView textViewID;

    private Button addButton, editButton,
            cancelButton, switchButton;

    private RadioGroup credentialsRadioGroup;
    private RadioButton userRadioButton, workerRadioButton,
            managerRadioButton, adminRadioButton;

    // Fancy progress bar and dialogs
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog optionsDialog;

    // Firebase Authorization Obj
    private FirebaseAuth firebaseAuth;

    // Database
    private DatabaseReference dbRef;

    // Account that is being created / changed
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        // authorization and database
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        // progress and alert dialogs
        progressDialog = new ProgressDialog(this);
        alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Trying to edit instead?");

        // set dialog message
        alertDialogBuilder
                .setMessage("The email you entered is already registered to an account. "
                        + "Select EDIT to edit this account's info, or CANCEL to cancel.")
                .setCancelable(false)
                .setPositiveButton("EDIT",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        finish();
                        startActivity(new Intent(getBaseContext(), EditExistingActivity.class));
                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close dialog and do nothing
                        dialog.cancel();
                    }
                });

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPass);

        textViewID = (TextView) findViewById(R.id.textViewID);

        addButton = (Button) findViewById(R.id.add_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        switchButton = (Button) findViewById(R.id.switch_button);

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        credentialsRadioGroup = (RadioGroup) findViewById(R.id.cred_group);
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

        boolean successful;

        // check if necessary fields are filled in
        if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
            Toast.makeText(this, "Please enter in an email.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.isEmpty(editTextEmail.getText().toString())
                && TextUtils.isEmpty(editTextPass.getText().toString())) {
            optionsDialog = alertDialogBuilder.create();
            optionsDialog.show();
        } else {
            String email = editTextEmail.getText().toString();
            String pass = editTextPass.getText().toString();

            if (workerRadioButton.isSelected()) {
                account = new Account(editTextEmail.getText().toString(),
                        editTextPass.getText().toString(),
                        Credential.WORKER);
            } else if (managerRadioButton.isSelected()) {
                account = new Account(editTextEmail.getText().toString(),
                        editTextPass.getText().toString(),
                        Credential.MANAGER);
            } else if (adminRadioButton.isSelected()) {
                account = new Account(editTextEmail.getText().toString(),
                        editTextPass.getText().toString(),
                        Credential.ADMIN);
            } else {
                account = new Account(editTextEmail.getText().toString(),
                        editTextPass.getText().toString(),
                        Credential.USER);
            }

            progressDialog.setMessage("Registering user...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // user is successfully registered

                                FirebaseUser current = firebaseAuth.getCurrentUser();
                                dbRef.child(current.getUid()).setValue(account);

                                Toast.makeText(
                                        RegistrationActivity.this,
                                        "Registration Successful",
                                        Toast.LENGTH_SHORT).show();

                                finish();
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            } else {
                                Toast.makeText(
                                        RegistrationActivity.this,
                                        "Registration Failed. Please try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {

        if (view == cancelButton) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        if (view == addButton) {
            onAddPressed(view);
        }

    }

}
