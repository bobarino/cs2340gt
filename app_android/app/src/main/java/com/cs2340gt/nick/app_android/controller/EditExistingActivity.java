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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public class EditExistingActivity extends AppCompatActivity implements View.OnClickListener {

    // Widgets represented in the view
    private EditText editTextEmail, editTextPass;

    private Button editButton, cancelButton;

    private RadioGroup credentialsRadioGroup;
    private RadioButton userRadioButton, workerRadioButton,
            managerRadioButton, adminRadioButton;

    // Fancy progress bar and dialogs
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog optionsDialog;
    private int wrongCount;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference dbRef;

    // Account that is being edited
    private Account existing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

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
                // could be useful in differentiating different users' credentials
            }
        });

        progressDialog = new ProgressDialog(this);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPass);

        editButton = (Button) findViewById(R.id.edit_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        editButton.setOnClickListener(this);
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
        editTextEmail.setText(auth.getCurrentUser().getEmail());
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
    protected void onEditPressed(View view) {
        Model model = Model.getInstance();
        final String newPass = editTextPass.getText().toString();

        if (TextUtils.isEmpty(newPass)) {
            Toast.makeText(this, "Please enter in a password.", Toast.LENGTH_SHORT).show();
            return;
        } else if (newPass.equals(existing.getPassword())) {
            Toast.makeText(this, "You must enter in a NEW password.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // all clear
            progressDialog.setMessage("Editing user...");
            progressDialog.show();

            existing = model.findAccountByEmail(editTextEmail.getText().toString());

            currentUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(EditExistingActivity.this,
                                "Something went very wrong.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        existing.setPassword(newPass);
                    }
                }
            });
            Credential newCred;
            if (workerRadioButton.isSelected()) {
                newCred = Credential.WORKER;
            } else if (managerRadioButton.isSelected()) {
                newCred = Credential.MANAGER;
            } else if (adminRadioButton.isSelected()) {
                newCred = Credential.ADMIN;
            } else {
                newCred = Credential.USER;
            }
            existing.setCredential(newCred);

            Map<String, Object> existingValues = existing.toMap();
            Map<String, Object> updates = new HashMap<>();
            updates.put("accounts", existingValues);
            dbRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(EditExistingActivity.this,
                                "Something went very wrong.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });

            progressDialog.setMessage("Logging in...");
            auth
                    .signInWithEmailAndPassword(existing.getEmailAddress(), existing.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(EditExistingActivity.this,
                                        "Something went very wrong.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                Toast.makeText(
                                        EditExistingActivity.this,
                                        "Edit Successful!",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoggedInActivity.class));
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
        if (view == editButton) {
            onEditPressed(view);
        }
    }

}
