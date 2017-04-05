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
 * Created by SEAN on 2/19/17.
 * Edited by ARMANDO on 4/2/17.
 */

public class WaterReportSubmitActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView emailDisplay;
    private TextView dateTimeDisplay;
    private TextView reportIdText;

    private Button submitButton;
    private Button cancelButton;

    // spinner to hold the possible water sources (from the water report class)
    private Spinner waterSourceSpinner;

    // spinner to hold the possible water conditions
    private Spinner waterConditionsSpinner;

    // editText for the latitude of the current location
    private EditText latInput;
    // editText for the longitude input of current location
    private EditText longInput;

    private ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference dbRef;

    // the report to be submitted
    private WaterReport waterReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_submit);

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

        reportIdText = (TextView) findViewById(R.id.displayTextId);
        emailDisplay = (TextView) findViewById(R.id.displayTextUser);
        dateTimeDisplay = (TextView) findViewById(R.id.displayTextDateTime);

        waterConditionsSpinner = (Spinner) findViewById(R.id.conditionSpinner);
        waterSourceSpinner = (Spinner) findViewById(R.id.spinnerSource);

        submitButton = (Button) findViewById(R.id.buttonSubmitReport);
        cancelButton = (Button) findViewById(R.id.buttonCancelReport);
        submitButton.setOnClickListener(this);
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

        waterReport = new WaterReport(null, "null", "null", "null", null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        Model model = Model.getInstance();

        reportIdText.setText(" " + waterReport.getId());
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
     * Attempts to add a new report to the model.
     *
     * @param view a View object included as convention
     */
    protected void onSubmitPressed(View view) {
        Model model = Model.getInstance();

        Account reporter = Model.getCurrentAccount();
        String source = (String) waterSourceSpinner.getSelectedItem();
        String condition = (String) waterConditionsSpinner.getSelectedItem();
        String dateTime = dateTimeDisplay.getText().toString();
        Location location = new Location(
                Double.parseDouble(latInput.getText().toString()),
                Double.parseDouble(longInput.getText().toString()));

        if (TextUtils.isEmpty(dateTime)
                || TextUtils.isEmpty(source)
                || TextUtils.isEmpty(condition)) {
            Toast.makeText(this,
                    "Please enter in all relevant fields.",
                    Toast.LENGTH_SHORT).show();
        } else {
            waterReport = new WaterReport(reporter, source,
                    condition, dateTime, location);
            progressDialog.setMessage(
                    "Submitting water report. Report ID: "
                            + waterReport.getId());
            progressDialog.show();
            submit(model, waterReport);
        }
    }

    /**
     * A helper to consolidate the submission of new water reports. Takes in
     * a WaterReport object and adds it to the model. If this is successful,
     * the report is added to Firebase. If unsuccessful the user is prompted.
     *
     * @param model the model which we are adding this report to.
     * @param newReport the water report which we are trying to add.
     */
    private void submit(Model model, WaterReport newReport) {
        Location location = newReport.getLocation();
        if (model.checkInvalidLocation(location)) {
            progressDialog.dismiss();
            Toast.makeText(
                    WaterReportSubmitActivity.this,
                    "Please enter in a valid location.",
                    Toast.LENGTH_SHORT).show();
        } else if (model.addReport(newReport)) {
            model.addReport(newReport);
            dbRef.child("reports").child("" + newReport.getId())
                    .setValue(newReport);
            progressDialog.dismiss();
            Toast.makeText(
                    WaterReportSubmitActivity.this,
                    "Submission Accepted at Coordinates:" +
                            "\nLatitude: " + location.getLatitude() +
                            "\nLongitude: " + location.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(),
                    WaterReportListActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == submitButton) {
            onSubmitPressed(view);
        }
        if (view == cancelButton) {
            startActivity(new Intent(getApplicationContext(), LoggedInActivity.class));
        }
    }


}
