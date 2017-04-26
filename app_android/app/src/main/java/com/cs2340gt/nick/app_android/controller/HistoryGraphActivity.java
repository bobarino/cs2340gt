package com.cs2340gt.nick.app_android.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.WaterPurityReport;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cs2340gt.nick.app_android.R.id.cancelButton;

/**
 * the main activity class for the History Graph Activity
 * produced by SEAN on 4/4/17.
 */

public class HistoryGraphActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * edit text field to take in a signed decimal representing
     * longitude of the approximate area the user is looking for
     */
    private EditText latInput;
    /*  edit text field to take in signed decimal representing latitude
    approximate area the user is looking for     */
    private EditText longInput;

    /*
    edit text for the start year/lower bound year the user is
    looking for
     */
    private EditText startYearInput;
    /*
    edit text for the end year/upper bound year the user is
    looking for reports between
     */
    private EditText endYearInput;

    /*
    spinner object to allow user to specify how many degrees +/-
    the latitude/longitude pairing they specified
     */
    private Spinner varianceSpinner;

    /*
    button for cancelling the history graph action --> takes you
    back to the previous screen
     */
    private Button cancel;

    /*
    button for generating a history graph action --> generates
    the history graph using the desired start/end years and
    the lat/long pairs (plus/minus the specified variance)
     */
    private Button search;

    /*
    the graph view that sets up the data points for the graph display
     */
    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_history_graph);

        latInput = (EditText) findViewById(R.id.latInput);
        longInput = (EditText) findViewById(R.id.longInput);
        startYearInput = (EditText) findViewById(R.id.startYearInput);
        endYearInput = (EditText) findViewById(R.id.endYearInput);

        varianceSpinner = (Spinner) findViewById(R.id.varianceSpinner);

        cancel = (Button) findViewById(cancelButton);
        search = (Button) findViewById(R.id.searchButton);
        search.setOnClickListener(this);
        cancel.setOnClickListener(this);


        List<String> varianceOptions = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, varianceOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        varianceSpinner.setAdapter(adapter);

        graph = (GraphView) findViewById(R.id.graph);
        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(1000);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(2014);
        graph.getViewport().setMaxX(2018);

        graph.getGridLabelRenderer().setVerticalAxisTitle("ppm");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Year");
    }

    /**
     * button handler for the submit button
     * @param view button for report submission
     */


    private void onSearchButtonPressed(android.view.View view) {
        Model model = Model.getInstance();
        int startYear = Integer.parseInt(startYearInput.getText().toString());
        int endYear = Integer.parseInt(endYearInput.getText().toString());

        double latitude = Double.parseDouble(latInput.getText().toString());
        double longitude = Double.parseDouble(longInput.getText().toString());

        List<DataPoint> viralPoints = new ArrayList<>();
        List<DataPoint> contaminantPoints = new ArrayList<>();

        double variance = Double.parseDouble((String) varianceSpinner.getSelectedItem());

        for (WaterPurityReport wpr : model.getPurityReportList()) {
            String temp = wpr.getDateTime();
            int length = temp.length();
            int year = Integer.parseInt(temp.substring(length - 11, length - 7));
            double lat = wpr.getLocation().getLatitude();
            double lon = wpr.getLocation().getLongitude();
            if (year >= startYear && year <= endYear) {
                if (lat <= latitude + variance && lat >= latitude - variance) {
                    if (lon <= longitude + variance && lon >= longitude - variance) {
                        viralPoints.add(new DataPoint(year, wpr.getViralPPM()));
                        contaminantPoints.add(new DataPoint(year, wpr.getContaminantPPM()));
                    }
                }
            }
        }
        DataPoint[] viralData = viralPoints.toArray(new DataPoint[viralPoints.size()]);
        DataPoint[] contaminantData = contaminantPoints.toArray(new DataPoint[contaminantPoints.size()]);
        LineGraphSeries<DataPoint> viralSeries =
                new LineGraphSeries<>(viralData);
        LineGraphSeries<DataPoint> contaminantSeries =
                new LineGraphSeries<>(contaminantData);
        viralSeries.setDrawDataPoints(true);
        contaminantSeries.setDrawDataPoints(true);
        viralSeries.setColor(Color.RED);
        contaminantSeries.setColor(Color.BLUE);
        viralSeries.setTitle("Viral Data");
        contaminantSeries.setTitle("Contaminant Data");
        graph.addSeries(contaminantSeries);
        graph.addSeries(viralSeries);
    }

    @Override
    public void onClick(View view) {
        if (view == search) {
            onSearchButtonPressed(view);
        }

        if (view == cancel) {
            startActivity(new Intent(getApplicationContext(), LoggedInActivity.class));
        }
    }
}