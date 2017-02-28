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

    private Button addButton, editButton, cancelButton;

    private RadioGroup credentialsRadioGroup;
    private RadioButton userRadioButton, workerRadioButton,
            managerRadioButton, adminRadioButton;

    // Fancy progress bar and dialogs
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog optionsDialog;
    private String alertEmailRef = "The email you entered ";
    private String alertPassRef = "";

    // Firebase Authorization
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    // Database
    private DatabaseReference dbRef;

    // Account that is being created / changed
    private Account account;

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
                        currentUser = firebaseAuth.getCurrentUser();
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

        // progress and alert dialogs
        progressDialog = new ProgressDialog(this);
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Trying to edit instead?")
                .setMessage(alertEmailRef + "is already registered to an account. "
                        + "Select EDIT to edit this account's info, or CANCEL to cancel.")
                .setCancelable(false)
                .setPositiveButton("EDIT",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent intent = new Intent(getApplicationContext(), EditExistingActivity.class);
                        intent.putExtra("email", "password");
                        intent.putExtra(alertEmailRef, alertPassRef);
                        auth.signInWithEmailAndPassword(alertEmailRef, alertPassRef);

                        finish();
                        startActivity(intent);
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

        addButton = (Button) findViewById(R.id.add_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        credentialsRadioGroup = (RadioGroup) findViewById(R.id.cred_group);
        userRadioButton = (RadioButton) findViewById(R.id.cred_user);
        workerRadioButton = (RadioButton) findViewById(R.id.cred_worker);
        managerRadioButton = (RadioButton) findViewById(R.id.cred_manager);
        adminRadioButton = (RadioButton) findViewById(R.id.cred_admin);

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
     * @param view
     */
    protected void onAddPressed(View view) {
        Model model = Model.getInstance();

        final String email = editTextEmail.getText().toString();
        alertEmailRef = email;
        final String pass = editTextPass.getText().toString();
        alertPassRef = pass;

        // check if necessary fields are filled in
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please enter in all relevant fields.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!model.findAccountByEmail(email).equals(new Account(9999))) {
            optionsDialog = alertDialogBuilder.create();
            optionsDialog.show();
        } else {
            Credential credential;
            if (workerRadioButton.isSelected()) {
                account = new Account(editTextEmail.getText().toString(),
                        editTextPass.getText().toString(), Credential.WORKER);
            } else if (managerRadioButton.isSelected()) {
                account = new Account(editTextEmail.getText().toString(),
                        editTextPass.getText().toString(), Credential.MANAGER);
            } else if (adminRadioButton.isSelected()) {
                account = new Account(editTextEmail.getText().toString(),
                        editTextPass.getText().toString(), Credential.ADMIN);
            } else {
                account = new Account(editTextEmail.getText().toString(),
                        editTextPass.getText().toString(), Credential.USER);
            }

            progressDialog.setMessage("Registering user...");
            progressDialog.show();

            auth
                    .createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                dbRef.child("accounts").child("" + account.getId()).setValue(account);
                                Toast.makeText(
                                        RegistrationActivity.this,
                                        "Registration Successful",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoggedInActivity.class));
                            } else {
                                Toast.makeText(
                                        RegistrationActivity.this,
                                        "Registration Failed. Please try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
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
