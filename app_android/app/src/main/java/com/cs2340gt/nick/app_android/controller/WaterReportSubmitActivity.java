package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.Location;

import com.cs2340gt.nick.app_android.model.WaterReport;

import org.w3c.dom.Text;

/**
 * Created by SEAN on 2/19/17.
 */

public class WaterReportSubmitActivity extends AppCompatActivity {
    // textview for the id no. for the report
    private TextView reportID;
    // textview for the username of the curernt user making the report
    private TextView username;
    // textview to fill in with the current date/time instance
    private TextView date_time;

    // spinner to hold the possible water sources (from the water report class)
    private Spinner waterSourceSpinner;
    // radio button to represent waste quality water
    private RadioButton wasteButton;
    // radio button to represent muddy water
    private RadioButton treatMudButton;
    // radio button to represent clear water
    private RadioButton treatClearButton;
    // radio button for potable water
    private RadioButton potableButton;

    // editText for the latitude of the current location
    private EditText latInput;
    // editText for the longitude input of current location
    private EditText longInput;

    // TextView to display the an error if long/lat are missing
    private TextView error;
    // TextView to display an error of inputs are invalid
    private TextView invalidInput;

    // water report currently being created/changed
    private WaterReport waterReport;

    //TODO: add modifications to process for 'editing'
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model model = Model.getInstance();
        setContentView(R.layout.content_water_report_submit);

        waterSourceSpinner = (Spinner) findViewById(R.id.source_options);
        wasteButton = (RadioButton) findViewById(R.id.waste_button);
        treatMudButton = (RadioButton) findViewById(R.id.treatable_muddy_button);
        treatClearButton = (RadioButton) findViewById(R.id.treatable_clear_button);
        potableButton = (RadioButton) findViewById(R.id.potable_button);
        reportID = (TextView) findViewById(R.id.id_field);
        username = (TextView) findViewById(R.id.name_field);
        date_time = (TextView) findViewById(R.id.date_time_field);
        latInput = (EditText) findViewById(R.id.latInput);
        longInput = (EditText) findViewById(R.id.longInput);
        error = (TextView) findViewById(R.id.errorInvalid);
        invalidInput = (TextView) findViewById(R.id.invalidInput);

        error.setVisibility(TextView.INVISIBLE);
        invalidInput.setVisibility(TextView.INVISIBLE);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, WaterReport.waterSources);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterSourceSpinner.setAdapter(adapter);

        waterReport = new WaterReport(null, null, null, null, null);
        reportID.setText(" " + waterReport.getId());

        username.setText(" " + model.getCurrentAccount().getUsername());

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        date_time.setText(date);
//        reportID.setText(Model.getCurrentReport().getNextNo());
//        username.setText(Model.getCurrentAccount().getUsername());
    }

    /**
     * button handler for the submit button
     * @param view button for report submission
     */
    protected void onAddPressed(View view) {
        Model model = Model.getInstance();
        //set water condition based on radio group, user is default
        if (wasteButton.isSelected()) {
            waterReport.setCondition(WaterReport.waterCondition.get(0));
        } else if (treatMudButton.isSelected()) {
            waterReport.setCondition(WaterReport.waterCondition.get(1));
        } else if (treatClearButton.isSelected()) {
            waterReport.setCondition(WaterReport.waterCondition.get(2));
        } else if (potableButton.isSelected()){
            waterReport.setCondition(WaterReport.waterCondition.get(3));
        } else {
            waterReport.setCondition(WaterReport.waterCondition.get(0));
        }
        if (latInput.getText() != null && longInput.getText() != null) {
            waterReport.setLocation(new Location(Double.parseDouble(latInput.getText().toString()),
                    Double.parseDouble(longInput.getText().toString())));
            waterReport.setSource((String) waterSourceSpinner.getSelectedItem());
            waterReport.setReporter(model.getCurrentAccount());
            waterReport.setDate_time((String) date_time.getText());
            if (model.addReport(waterReport)) {
                Intent intent =
                        new Intent(getBaseContext(), LoggedInActivity.class);
                startActivity(intent);
            } else {
                latInput.setText("Latitude: ");
                longInput.setText("Longitude: ");
                invalidInput.setVisibility(TextView.VISIBLE);
            }
        } else {
            latInput.setText("Latitude: ");
            longInput.setText("Longitude: ");
            error.setVisibility(TextView.VISIBLE);
        }

    //TODO: make distinction for editing a water report
    }

    /**
     * button handler for cancel button
     * @param view the button to cancel report submission
     */
    protected void onCancelPressed(View view) {
        Intent intent =
                new Intent(getBaseContext(), LoggedInActivity.class);
        startActivity(intent);
    }

}
