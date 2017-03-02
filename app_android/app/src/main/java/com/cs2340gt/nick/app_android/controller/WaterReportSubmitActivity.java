package com.cs2340gt.nick.app_android.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;

import com.cs2340gt.nick.app_android.model.WaterReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by SEAN on 2/19/17.
 */

public class WaterReportSubmitActivity extends AppCompatActivity implements View.OnClickListener {
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

    private WaterReport waterReport;

    //TODO: add modifications to process for 'editing'
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_water_report_submit);

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

        waterSourceSpinner = (Spinner) findViewById(R.id.source_options);
        wasteButton = (RadioButton) findViewById(R.id.waste_button);
        treatableMudButton = (RadioButton) findViewById(R.id.treatable_muddy_button);
        treatableClearButton = (RadioButton) findViewById(R.id.treatable_clear_button);
        potableButton = (RadioButton) findViewById(R.id.potable_button);

        addButton = (Button) findViewById(R.id.buttonAddReport);
        cancelButton = (Button) findViewById(R.id.buttonCancelReport);
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, WaterReport.waterSources);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterSourceSpinner.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        Model model = Model.getInstance();

        idDisplay.setText(" " + waterReport.getId());
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
    protected void onAddPressed(View view) {
        Model model = Model.getInstance();

        Account reporter = model.getCurrentAccount();
        String dateTime = dateTimeDisplay.getText().toString();
        String source = (String) waterSourceSpinner.getSelectedItem();
        String condition = determineWaterCondition();
        String location = locationField.getText().toString();

        if (TextUtils.isEmpty(dateTime)
                || TextUtils.isEmpty(source)
                || TextUtils.isEmpty(condition)
                || TextUtils.isEmpty(location)) {
            Toast.makeText(this,
                    "Please enter in all relevant fields.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.setMessage("Submitting water report...");
            progressDialog.show();
            waterReport = new WaterReport(reporter, source,
                    condition, dateTime, location);
            submit(model, waterReport);
        }
    }

    /**
     * A helper to consolidate the submission of new water reports. Takes in
     * a WaterReport object and adds it to the model. If this is successful,
     * the report is added to Firebase. If unsuccessful the user is prompted.
     *
     * @param model the model which we are adding this water report to.
     * @param newReport the water report which we are trying to add.
     */
    private void submit(Model model, WaterReport newReport) {
        if (model.addReport(newReport)) {
            progressDialog.dismiss();
            dbRef.child("reports").child("" + newReport.getId())
                    .setValue(newReport);
            Toast.makeText(
                    WaterReportSubmitActivity.this,
                    "Submission Accepted",
                    Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(),
                    LoggedInActivity.class));
        } else {
            Toast.makeText(
                    WaterReportSubmitActivity.this,
                    "Submission Failed. Please try again.",
                    Toast.LENGTH_SHORT).show();
        } /* TODO: add ELSE IF above this failure ELSE to allow for editing
          search the model to determine report exists?
          check in firebase for it? */
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
            onAddPressed(view);
        }
        if (view == cancelButton) {
            startActivity(new Intent(getApplicationContext(), LoggedInActivity.class));
        }
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        _major = parent.getItemAtPosition(position).toString();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        _major = "NA";
//    }
}
