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
import com.cs2340gt.nick.app_android.model.WaterPurityReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Created by SEAN on 3/12/17.
 * Edited by Armando on 4/2/17.
 */

public class WaterPuritySubmitActivity extends AppCompatActivity implements View.OnClickListener {
    // buttons for adding and cancelling
    private Button submitButton;
    private Button cancelButton;

    // textview for the id no. for the report
    private TextView reportID;
    //text view for the email of the current user making report
    private TextView emailText;
    //text view to fill with the current date/time instance
    private TextView dateTimeDisplay;

    //spinner to hold the possible water conditions (from the water purity class)
    private Spinner waterConditionsSpinner;

    // edit text field to take in signed decimal representing
    // latitude of the report's location
    private EditText latInput;
    /*  edit text field to take in signed decimal representing latitude
    of the report's location     */
    private EditText longInput;

    // edit text field for the contaminant ppm
    private EditText contaminantText;
    //edit text field for the viral ppm
    private EditText viralText;

    private ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference dbRef;

    // the report to be submitted
    private WaterPurityReport waterPurityReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_purity_submit);

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
//        dbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // this is called twice, once with initial value and again whenever changed
//                WaterPurityReport waterPurityReport = dataSnapshot.getValue(WaterPurityReport.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // failed to read value
//                // could be useful in differentiating different users' credentials?
//            }
//        });

        submitButton = (Button) findViewById(R.id.submitButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        waterConditionsSpinner = (Spinner) findViewById(R.id.conditionSpinner);
        reportID = (TextView) findViewById(R.id.idField);
        emailText = (TextView) findViewById(R.id.emailText);
        dateTimeDisplay = (TextView) findViewById(R.id.dateTimeField);
        latInput = (EditText) findViewById(R.id.latInput);
        longInput = (EditText) findViewById(R.id.longInput);
        contaminantText = (EditText) findViewById(R.id.contamInput);
        viralText = (EditText) findViewById(R.id.viralInput);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        WaterPurityReport.waterConditions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterConditionsSpinner.setAdapter(adapter);

        waterPurityReport = new WaterPurityReport(null, null, 0, 0, null, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        Model model = Model.getInstance();

        reportID.setText(" " + waterPurityReport.getId());
        emailText.setText(" " + model.getCurrentAccount().getEmailAddress());
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        dateTimeDisplay.setText(date);
    }

    /**
     * Attempts to submit a water purity report; initiated after the
     * submit button has been pressed.
     *
     * @param view button for report submission
     */
    protected void onSubmitPressed(View view) {
        Model model = Model.getInstance();

        Account reporter = model.getCurrentAccount();
        String condition = (String) waterConditionsSpinner.getSelectedItem();
        int viralPPM = Integer.parseInt(viralText.getText().toString());
        int contaminantPPM = Integer.parseInt(contaminantText.getText().toString());
        String dateTime = dateTimeDisplay.getText().toString();
        Location location = new Location(
                Double.parseDouble(latInput.getText().toString()),
                Double.parseDouble(longInput.getText().toString()));

        if (TextUtils.isEmpty(dateTime)
                || TextUtils.isEmpty(condition)) {
            Toast.makeText(this,
                    "Please enter in all relevant fields.",
                    Toast.LENGTH_SHORT).show();
        } else {
            waterPurityReport.setReporter(reporter);
            waterPurityReport.setCondition(condition);
            waterPurityReport.setViralPPM(viralPPM);
            waterPurityReport.setContaminantPPM(contaminantPPM);
            waterPurityReport.setDateTime(dateTime);
            waterPurityReport.setLocation(location);
            progressDialog.setMessage(
                    "Submitting water purity report. Report ID:"
                            + waterPurityReport.getId());
            progressDialog.show();
            submit(model, waterPurityReport);
        }
    }

    /**
     * A helper to consolidate the submission of new water purity reports. Takes in a
     * WaterPurityReport object and adds it to the model. If this is successful,
     * the report is added to Firebase. If unsuccessful the user is prompted.
     *
     * @param model the model which we are adding this report to.
     * @param newPurityReport the new report being added to the model.
     */
    protected void submit(Model model, WaterPurityReport newPurityReport) {
        Location location = newPurityReport.getLocation();
        if (model.checkInvalidLocation(location)) {
            progressDialog.dismiss();
            Toast.makeText(
                    WaterPuritySubmitActivity.this,
                    "Please enter in a valid location.",
                    Toast.LENGTH_SHORT).show();
        } else {
            model.addPurityReport(newPurityReport);
            dbRef.child("purity_reports").child("" + newPurityReport.getId())
                    .setValue(newPurityReport);
            progressDialog.dismiss();
            Toast.makeText(
                    WaterPuritySubmitActivity.this,
                    "Submission Accepted at Coordinates:" +
                            "\nLatitude: " + location.getLatitude() +
                            "\nLongitude: " + location.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(),
                    WaterPurityListActivity.class));
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
