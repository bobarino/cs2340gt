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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
    private Spinner credentials;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference dbRef;

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
                        FirebaseUser user = auth.getCurrentUser();
                        if (user == null) {
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
                // could be useful in differentiating different users' credentials
            }
        });

        editTextEmail = (EditText) findViewById(R.id.editEmail);
        editTextPass = (EditText) findViewById(R.id.editPassword);

        editButton = (Button) findViewById(R.id.buttonEditAcc);
        cancelButton = (Button) findViewById(R.id.buttonCancelEdit);
        editButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        credentials = (Spinner) findViewById(R.id.credSpinnerEdit);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        Credential.credentialsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        credentials.setAdapter(adapter);

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
     * Attempts to start the edit process based on the required fields being filled in and being
     * filled in correctly.
     *
     * @param view a view being passed in by convention.
     */
    protected void onEditPressed(View view) {
        Model model = Model.getInstance();
        final Account existing = model.findAccountByEmail(editTextEmail.getText().toString());
        final String newPass = editTextPass.getText().toString();

        if (TextUtils.isEmpty(newPass)) {
            Toast.makeText(this, "Please enter in a password.", Toast.LENGTH_SHORT).show();
            return;
        } else if (newPass.equals(existing.getPassword())) {
            Toast.makeText(this, "You must enter in a NEW password.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // all clear
            editAccountInfo(existing, newPass, Credential.valueOf(credentials.getSelectedItem().toString()));
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPass);
            }

            Map<String, Object> existingValues = existing.toMap();
            Map<String, Object> updates = new HashMap<>();
            dbRef.child("accounts").child("" + existing.getId())
                    .setValue(existing);
            Toast.makeText(this, "Edit successful.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(), LoggedInActivity.class));
        }
    }

    /**
     * Edits the account's information in the model.
     *
     * @param existing the existing account which we are trying to edit.
     * @param newPassword the new password being set to the account.
     * @param newCredential the new Credential being assigned to the account.
     */
    private void editAccountInfo(final Account existing,
                                 final String newPassword,
                                 Credential newCredential) {
        if (existing.equals(new Account(9999))) {
            // something is very wrong
            return;
        } else {
            existing.setPassword(newPassword);
            existing.setCredential(newCredential);
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
