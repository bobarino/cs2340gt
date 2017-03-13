package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Location;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.WaterPurityReport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Created by SEAN on 3/12/17.
 */

public class WaterPuritySubmitActivity extends AppCompatActivity {
    // textview for the id no. for the report
    private TextView reportID;
    //text view for the username of the current user making report
    private TextView username;
    //text view to fill with the current date/time instance
    private TextView date_time;

    //spinner to hold the possible water conditions (from the water purity class)
    private Spinner waterConditions;

    // edit text field to take in signed decimal representing
    // latitude of the report's location
    private EditText latInput;
    /*  edit text field to take in signed decimal representing latitude
    of the report's location     */
    private EditText longInput;

    // edit text field for the contaminant ppm
    private EditText contaminantPPM;
    //edit text field for the viral ppm
    private EditText viralPPM;

    // text view to display error message upon missing lat/long values
    private TextView error;
    // text view to display error message if invalid lat/long values entered
    private TextView invalidInput;

    private WaterPurityReport waterPurityReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model model = Model.getInstance();
        setContentView(R.layout.content_water_purity_submit);

        waterConditions = (Spinner) findViewById(R.id.conditionSpinner);
        reportID = (TextView) findViewById(R.id.idField);
        username = (TextView) findViewById(R.id.userField);
        date_time = (TextView) findViewById(R.id.dateTimeField);
        latInput = (EditText) findViewById(R.id.latInput);
        longInput = (EditText) findViewById(R.id.longInput);
        error = (TextView) findViewById(R.id.errorText);
        invalidInput = (TextView) findViewById(R.id.invalidText);
        contaminantPPM = (EditText) findViewById(R.id.contamInput);
        viralPPM = (EditText) findViewById(R.id.viralInput);

        error.setVisibility(TextView.INVISIBLE);
        invalidInput.setVisibility(TextView.INVISIBLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, WaterPurityReport.waterConditions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterConditions.setAdapter(adapter);

        waterPurityReport = new WaterPurityReport(null, null, 0, 0, null, null);
        reportID.setText(" " + waterPurityReport.getId());

        username.setText(" " + model.getCurrentAccount().getUsername());

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        date_time.setText(date);
    }

    /**
     * button handler for the submit button
     * @param view button for report submission
     */
    protected void onAddPressed(View view) {
        Model model = Model.getInstance();

        if (latInput.getText() != null && longInput.getText() != null) {
            waterPurityReport.setLocation(new Location(Double.parseDouble(latInput.getText().toString()),
                    Double.parseDouble(longInput.getText().toString())));
            waterPurityReport.setAccount(model.getCurrentAccount());
            waterPurityReport.setDate_time((String) date_time.getText());
            waterPurityReport.setCondition((String) waterConditions.getSelectedItem());
            waterPurityReport.setContaminantPPM(Integer.parseInt(contaminantPPM.getText().toString()));
            waterPurityReport.setViralPPM(Integer.parseInt(viralPPM.getText().toString()));

            System.out.println(waterPurityReport);

            if (model.addPurityReport(waterPurityReport)) {
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
    }

    protected void onCancelPressed(View view) {
        Intent intent =
                new Intent(getBaseContext(), LoggedInActivity.class);
        startActivity(intent);
    }
}
