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

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;

import com.cs2340gt.nick.app_android.model.WaterReport;

import org.w3c.dom.Text;

/**
 * Created by SEAN on 2/19/17.
 */

public class WaterReportSubmitActivity extends AppCompatActivity {
    private TextView reportID;
    private TextView username;
    private TextView date_time;

    private Spinner waterSourceSpinner;
    private RadioButton wasteButton;
    private RadioButton treatMudButton;
    private RadioButton treatClearButton;
    private RadioButton potableButton;

    // water report currently being created/changed
    private WaterReport waterReport;

    //TODO: add modifications to process for 'editing'
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_water_report_submit);

        waterSourceSpinner = (Spinner) findViewById(R.id.source_options);
        wasteButton = (RadioButton) findViewById(R.id.waste_button);
        treatMudButton = (RadioButton) findViewById(R.id.treatable_muddy_button);
        treatClearButton = (RadioButton) findViewById(R.id.treatable_clear_button);
        potableButton = (RadioButton) findViewById(R.id.potable_button);
        reportID = (TextView) findViewById(R.id.id_field);
        username = (TextView) findViewById(R.id.name_field);
        date_time = (TextView) findViewById(R.id.date_time_field);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, WaterReport.waterSources);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterSourceSpinner.setAdapter(adapter);

//        reportID.setText(Model.getCurrentReport().getNextNo());
//        username.setText(Model.getCurrentAccount().getUsername());
    }

    /**
     * button handler for the submit button
     * @param view
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
        } else {
            waterReport.setCondition(WaterReport.waterCondition.get(3));
        }

        waterReport.setSource((String) waterSourceSpinner.getSelectedItem());
        waterReport.setReporter(model.getCurrentAccount());

        Intent intent =
                new Intent(getBaseContext(), LoggedInActivity.class);
        startActivity(intent);

    //TODO: make distinction for editing a water report
    }

    protected void onCancelPressed(View view) {
        Intent intent =
                new Intent(getBaseContext(), LoggedInActivity.class);
        startActivity(intent);
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
