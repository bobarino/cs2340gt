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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    // Widgets represented in the view
    private EditText editTextEmail, editTextPass;

    private Button addButton, cancelButton;

    private RadioGroup credentialsRadioGroup;
    private RadioButton userRadioButton, workerRadioButton,
            managerRadioButton, adminRadioButton;

    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog optionsDialog;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference dbRef;

    private Account account;
    private String emailString;
    private String passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        // authorization and database
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        currentUser = auth.getCurrentUser();
                        if (currentUser == null) {
                            // no user is signed in
                        } else {
                            // some user is signed in
                        }
                    }
                };
            }
        };
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // this is called twice, once with initial value and again whenever changed
                Account account = dataSnapshot.getValue(Account.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // failed to read value
                // could be useful in differentiating different users' credentials?
            }
        });

        progressDialog = new ProgressDialog(this);
        alertDialogBuilder = new AlertDialog.Builder(this);

        editTextEmail = (EditText) findViewById(R.id.editEmail);
        editTextPass = (EditText) findViewById(R.id.editPassword);

        addButton = (Button) findViewById(R.id.buttonAddAcc);
        cancelButton = (Button) findViewById(R.id.buttonCancelRgstr);
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        credentialsRadioGroup = (RadioGroup) findViewById(R.id.rGroupCred);
        userRadioButton = (RadioButton) findViewById(R.id.rButtonUser);
        workerRadioButton = (RadioButton) findViewById(R.id.rButtonWorker);
        managerRadioButton = (RadioButton) findViewById(R.id.rButtonManager);
        adminRadioButton = (RadioButton) findViewById(R.id.rButtonAdmin);

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
     * Attempts to add a new account to the model. If account exists,
     * tries to reroute persons to edit this existing account; disallows any
     * persons from registering preexisting accounts.
     *
     * @param view a View object included as convention
     */
    protected void onAddPressed(View view) {
        Model model = Model.getInstance();

        emailString = editTextEmail.getText().toString();
        passwordString = editTextPass.getText().toString();

        setUpAlertDialogBuilder(emailString, passwordString);

        // check if necessary fields are filled in
        if (TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString)) {
            Toast.makeText(this,
                    "Please enter in all relevant fields.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.setMessage("Registering user...");
            progressDialog.show();
            account = new Account(editTextEmail.getText().toString(),
                    editTextPass.getText().toString(), determineCredential());
            register(model, account);
        }
    }

    /**
     * A helper to set up the alert dialog based on the String values obtained from the
     * view which represent an email and password used to attempt registration of an
     * existing account. Alert Dialog is not always created/shown.
     *
     * @param emailRef a String representing the existing account's email address
     * @param passwordRef a String representing the existing account's password
     */
    private void setUpAlertDialogBuilder(final String emailRef, final String passwordRef) {
        alertDialogBuilder
                .setView(R.layout.dialog_options_registration)
                .setTitle("Trying to edit?")
                .setPositiveButton("EDIT",new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        progressDialog.setMessage("Verifying password...");
                        auth
                                .signInWithEmailAndPassword(emailRef, passwordRef)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), EditExistingActivity.class));
                                } else {
                                    Toast.makeText(
                                            RegistrationActivity.this,
                                            "Wrong password. Please try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close dialog and do nothing
                        dialog.cancel();
                    }
                });
    }

    /**
     * A helper to consolidate the registration of new accounts. Takes in
     * an Account object and registers to the model based on whether or not Firebase
     * authentication approves this new registration to occur.
     *
     * @param model the model which the account is being added to.
     * @param newAccount the new account being registered.
     */
    private void register(final Model model, final Account newAccount) {
        auth
                .createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            model.addAccountInfo(account);
                            dbRef.child("accounts").child("" + newAccount.getId())
                                    .setValue(newAccount);
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Registration Successful",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),
                                    LoggedInActivity.class));
                        } else {
                            // must be attempting to edit an account that exists
                            optionsDialog = alertDialogBuilder.create();
                            optionsDialog.show();
                        }
                    }
                });
    }

    /**
     * Intended to determine the credential level selected for the new user. Should
     * determine the credential level based on the radio button that is pushed.
     *
     * @return the Credential value that has been selected on this screen.
     */
    private Credential determineCredential() {
        if (userRadioButton.isSelected()) {
            return Credential.USER;
        }
        if (workerRadioButton.isSelected()) {
            return Credential.WORKER;
        }
        if (managerRadioButton.isSelected()) {
            return Credential.MANAGER;
        }
        if (adminRadioButton.isSelected()) {
            return Credential.ADMIN;
        }
        return Credential.USER;
    }

    @Override
    public void onClick(View view) {
        if (view == cancelButton) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        if (view == addButton) {
            onAddPressed(view);
        }
    }

}
