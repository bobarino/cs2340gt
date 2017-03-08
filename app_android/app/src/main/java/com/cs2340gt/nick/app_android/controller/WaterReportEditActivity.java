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
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.WaterReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ArmandoGonzalez on 3/8/17.
 */

public class WaterReportEditActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView idDisplay;
    private TextView emailDisplay;
    private TextView dateTimeDisplay;

    private Button addButton;
    private Button cancelButton;

    private Spinner waterSourceSpinner;

    private RadioButton wasteButton;
    private RadioButton treatableMudButton;
    private RadioButton treatableClearButton;
    private RadioButton potableButton;

    private EditText locationField;

    private ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference dbRef;

    private WaterReport existing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_report_submit);

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
        locationField = (EditText) findViewById(R.id.editLocation);

        waterSourceSpinner = (Spinner) findViewById(R.id.spinnerSource);
        wasteButton = (RadioButton) findViewById(R.id.rButtonWaste);
        treatableMudButton = (RadioButton) findViewById(R.id.rButtonTreatableMuddy);
        treatableClearButton = (RadioButton) findViewById(R.id.rButtonTreatableClear);
        potableButton = (RadioButton) findViewById(R.id.rButtonPotable);

        addButton = (Button) findViewById(R.id.buttonAddReport);
        cancelButton = (Button) findViewById(R.id.buttonCancelReport);
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, WaterReport.waterSources);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterSourceSpinner.setAdapter(adapter);
    }

    /**
     * Attempts to edit an existing report in the model.
     *
     * @param view a View object included as convention
     */
    private void onEditPressed(View view) {
        Model model = Model.getInstance();

        // TODO: get the accurate ID from prev screen
        existing = model.findReportById(0);

        // TODO: maybe check if current user is allowed to edit reports made?
        Account newReporter = model.getCurrentAccount();
        String newDateTime = dateTimeDisplay.getText().toString();
        String newSource = (String) waterSourceSpinner.getSelectedItem();
        String newCondition = determineWaterCondition();
        String newLocation = locationField.getText().toString();

        if (TextUtils.isEmpty(newDateTime)
                || TextUtils.isEmpty(newSource)
                || TextUtils.isEmpty(newCondition)
                || TextUtils.isEmpty(newLocation)) {
            Toast.makeText(this,
                    "Please enter in all relevant fields.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (existing.getReporter() == newReporter
                & existing.getDate_time() == newDateTime
                & existing.getSource() == newSource
                & existing.getCondition() == newCondition
                & existing.getLocation() == newLocation) {
            Toast.makeText(this,
                    "No edit needed; nothing has been changed.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.setMessage("Making edits...");
            progressDialog.show();
            edit(model, existing);
        }
    }

    /**
     * A helper to consolidate the editing of an existing account. Takes in
     * a WaterReport object and edits it accordingly. If this is successful,
     * the report is updated Firebase. If unsuccessful the user is prompted.
     *
     * @param model the model which we are adding this water report to.
     * @param existing the water report which we are trying to edit.
     */
    private void edit(Model model, WaterReport existing) {
        progressDialog.dismiss();
        dbRef.child("reports").child("" + existing.getId())
                .setValue(existing);
        Toast.makeText(
                WaterReportEditActivity.this,
                "Edit Accepted",
                Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(getApplicationContext(),
                LoggedInActivity.class));
    }

    /**
     * Intended to determine the water condition selected by the user. Should
     * determine the water condition based on which radio button is selected.
     *
     * @return a String representing the chosen condition of the water.
     */
    private String determineWaterCondition() {
        if (wasteButton.isSelected()) {
            return WaterReport.waterCondition.get(0);
        }
        if (treatableMudButton.isSelected()) {
            return WaterReport.waterCondition.get(1);
        }
        if (treatableClearButton.isSelected()) {
            return WaterReport.waterCondition.get(2);
        }
        if (potableButton.isSelected()){
            return WaterReport.waterCondition.get(3);
        }
        return WaterReport.waterCondition.get(0);
    }

    @Override
    public void onClick(View view) {
        if (view == addButton) {
            onEditPressed(view);
        }
        if (view == cancelButton) {
            startActivity(new Intent(getApplicationContext(), LoggedInActivity.class));
        }
    }
}