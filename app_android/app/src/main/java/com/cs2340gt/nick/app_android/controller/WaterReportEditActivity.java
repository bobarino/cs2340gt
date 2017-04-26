package com.cs2340gt.nick.app_android.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Location;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.WaterReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ArmandoGonzalez on 3/8/17.
 */

public class WaterReportEditActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView idDisplay;
    private TextView emailDisplay;
    private TextView dateTimeDisplay;

    private Button editButton;
    private Button cancelButton;

    private Spinner waterSourceSpinner;
    private Spinner waterConditionsSpinner;

    private EditText latInput;
    private EditText longInput;

    private ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference dbRef;

    private WaterReport existing;
    private WaterReport edited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_edit);

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
                WaterReport waterReport = dataSnapshot.getValue(WaterReport.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // failed to read value
                // could be useful in differentiating different users' credentials?
            }
        });

        progressDialog = new ProgressDialog(this);

        idDisplay = (TextView) findViewById(R.id.displayTextId);
        emailDisplay = (TextView) findViewById(R.id.displayTextUser);
        dateTimeDisplay = (TextView) findViewById(R.id.displayTextDateTime);

        waterSourceSpinner = (Spinner) findViewById(R.id.spinnerSource);
        waterConditionsSpinner = (Spinner) findViewById(R.id.conditionSpinner);

        editButton = (Button) findViewById(R.id.buttonEditPurity);
        cancelButton = (Button) findViewById(R.id.buttonCancelPurity);
        editButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        latInput = (EditText) findViewById(R.id.latInput);
        longInput = (EditText) findViewById(R.id.longInput);

        ArrayAdapter<String> conditionAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        WaterReport.waterCondition);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterConditionsSpinner.setAdapter(conditionAdapter);

        ArrayAdapter<String> sourceAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        WaterReport.waterSources);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterSourceSpinner.setAdapter(sourceAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        Model model = Model.getInstance();

        idDisplay.setText(" " + existing.getId());
        emailDisplay.setText(" " + model.getCurrentAccount().getEmailAddress());
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        dateTimeDisplay.setText(date);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    /**
     * Attempts to edit an existing report in the model.
     *
     * @param view a View object included as convention
     */
    private void onEditPressed(View view) {
        Account reporter = Model.getCurrentAccount();
        String newCondition = (String) waterConditionsSpinner.getSelectedItem();
        String newDateTime = dateTimeDisplay.getText().toString();
        String newSource = (String) waterSourceSpinner.getSelectedItem();
        Location newLocation = new Location(Double.parseDouble(latInput.getText().toString()),
                Double.parseDouble(longInput.getText().toString()));

        existing = Model.getCurrentReport();
        edited = new WaterReport(reporter, newSource, newCondition, newDateTime, newLocation);

        if (TextUtils.isEmpty(newDateTime)
                || TextUtils.isEmpty(newSource)
                || TextUtils.isEmpty(newCondition)) {
            Toast.makeText(this,
                    "Please enter in all relevant fields.",
                    Toast.LENGTH_SHORT).show();
        } else if (existing.equals(edited)) {
            Toast.makeText(this,
                    "No edit needed; nothing has been changed.",
                    Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Making edits...");
            progressDialog.show();
            edit(Model.getInstance(), existing, edited);
        }
    }

    /**
     * A helper to consolidate the editing of an existing account. Takes in
     * a WaterReport object and edits it accordingly. If this is successful,
     * the report is updated Firebase. If unsuccessful the user is prompted.
     *
     * @param model the model where this report is stored.
     * @param existing the water report which we are trying to edit.
     * @param edited the fully edited version of the report, whose fields
     *               will be copied onto existing.
     */
    private void edit(Model model, WaterReport existing, WaterReport edited) {
        Location location = edited.getLocation();
        if (model.checkInvalidLocation(location)) {
            progressDialog.dismiss();
            Toast.makeText(
                    WaterReportEditActivity.this,
                    "Please enter in a valid location.",
                    Toast.LENGTH_SHORT).show();
        } else {
            existing.setCondition(edited.getCondition());
            existing.setSource(edited.getSource());
            existing.setLocation(edited.getLocation());
            dbRef.child("reports").child("" + existing.getId())
                    .setValue(existing);
            progressDialog.dismiss();
            Toast.makeText(
                    WaterReportEditActivity.this,
                    "Edit Accepted",
                    Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(),
                    WaterReportListActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == editButton) {
            onEditPressed(view);
        }
        if (view == cancelButton) {
            startActivity(new Intent(getApplicationContext(), LoggedInActivity.class));
        }
    }
}