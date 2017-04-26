package com.cs2340gt.nick.app_android.controller;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.cs2340gt.nick.app_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.TextView;

import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.WaterPurityReport;
import com.cs2340gt.nick.app_android.model.WaterReport;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/*
* created by SeanBills on 2/14/17.
* edited by SeanBills on 2/19/17.
 */
public class LoggedInActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button editAccountButton;
    private Button logoutButton;
    private Button showReportListButton;
    private Button showPurityReportButton;
    private Button submitPurityReportButton;
    private Button submitReportButton;

    private GoogleMap map;

    private Model modelFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);

        modelFacade = Model.getInstance();
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

        editAccountButton = (Button) findViewById(R.id.edit_user);
        logoutButton = (Button) findViewById(R.id.logout_button);
        showReportListButton = (Button) findViewById(R.id.show_reports_button);
        showPurityReportButton = (Button) findViewById(R.id.showPurityListButton);
        submitReportButton = (Button) findViewById(R.id.report_submit);
        submitPurityReportButton = (Button) findViewById(R.id.submitPurityButton);

        editAccountButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        showReportListButton.setOnClickListener(this);
        showPurityReportButton.setOnClickListener(this);
        submitReportButton.setOnClickListener(this);
        submitPurityReportButton.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (modelFacade.getCurrentAccount().getCredential().equals(Credential.USER)) {
            submitPurityReportButton.setVisibility(Button.INVISIBLE);
            showPurityReportButton.setVisibility(Button.INVISIBLE);
        }
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(View view) {
        Model model = Model.getInstance();

        if (view == editAccountButton) {
            finish();
            startActivity(new Intent(getApplicationContext(), EditExistingActivity.class));
        }

        if (view == logoutButton) {
            auth.signOut();
            model.setCurrentAcc(null);
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        if (view == showReportListButton) {
            finish();
            startActivity(new Intent(getApplicationContext(), WaterReportListActivity.class));
        }

        if (view == showPurityReportButton) {
            finish();
            startActivity(new Intent(getApplicationContext(), WaterPurityListActivity.class));
        }

        if (view == submitPurityReportButton) {
            finish();
            startActivity(new Intent(getApplicationContext(), WaterPuritySubmitActivity.class));
        }

        if (view == submitReportButton) {
            finish();
            startActivity(new Intent(getApplicationContext(), WaterReportSubmitActivity.class));
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        List<WaterReport> reportList = modelFacade.getReportList();
        for (WaterReport wr : reportList) {
            LatLng location = new LatLng(wr.getLocation().getLatitude(), wr.getLocation().getLongitude());
            map.addMarker(new MarkerOptions().position(location).title(wr.getReporter().getEmailAddress()).snippet(wr.getSource()
                    + " - " + wr.getCondition()));
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
        }

        if (modelFacade.getCurrentAccount().getCredential() != Credential.USER) {
            List<WaterPurityReport> purityReportList = modelFacade.getPurityReportList();
            for (WaterPurityReport wpr : purityReportList) {
                LatLng location = new LatLng(wpr.getLocation().getLatitude(), wpr.getLocation().getLongitude());
                map.addMarker(new MarkerOptions().position(location).title(wpr.getReporter().getEmailAddress()).snippet("ViralPPM: "
                        + wpr.getViralPPM() + " Contaminant PPM: "
                        + wpr.getContaminantPPM()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        }

        map.setInfoWindowAdapter(new LoggedInActivity.CustomInfoWindowAdapter());
    }

    /**
     * class to create a WindowAdapter to adapt to eht GoogleMap
     */
    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        CustomInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
