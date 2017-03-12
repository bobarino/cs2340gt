package com.cs2340gt.nick.app_android.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Model;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.Location;
import com.cs2340gt.nick.app_android.model.WaterReport;

import java.util.List;

/*
* created by SeanBills on 2/14/17.
* edited by SeanBills on 2/19/17.
 */

public class LoggedInActivity extends android.support.v4.app.FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    private Model mFacade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);

        // the controller for the submit water report button
        Button submitReportButton = (Button) findViewById(R.id.report_submit);
        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), WaterReportSubmitActivity.class);
                startActivity(intent);
            }
        });

        // the controller for the logout button
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model model = Model.getInstance();
                model.setCurrentAcc(null);
                Intent intent =
                        new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // controller for the edit user information button
        Button editInfoButton = (Button) findViewById(R.id.edit_user);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        Button showReportList = (Button) findViewById(R.id.show_reports_button);
        showReportList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getBaseContext(), WaterReportListActivity.class);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFacade = Model.getInstance();
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
        mMap = googleMap;
        // Setting a click event handler for the map
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//
//
//                // Creating a marker
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                // Setting the position for the marker
//                markerOptions.position(latLng);
//
//
//
//                // Clears the previously touched position
//                // mMap.clear();
////                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
////                String date = df.format(Calendar.getInstance().getTime());
////                WaterReport newRep = new WaterReport(mFacade.getCurrentAccount(), WaterReport.waterSources.get(0),
////                        WaterReport.waterCondition.get(0), date, new Location(latLng.latitude, latLng.longitude));
////                mFacade.addReport(newRep);
////                int temp_id = newRep.getId();
////
////                // Setting the title for the marker.
////                // This will be displayed on taping the marker
////                markerOptions.title(mFacade.findReportById(temp_id).getCondition());
////                markerOptions.snippet(mFacade.findReportById(temp_id).getDate_time() + "\n"
////                        + mFacade.findReportById(temp_id).getLocation());
//
//                // Animating to the touched position
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                // Placing a marker on the touched position
//                mMap.addMarker(markerOptions);
//            }
//        });



//        LatLng hold = new LatLng(51.5074, 0.1278);
//        mMap.addMarker(new MarkerOptions().position(hold).title("London").snippet("City of London"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(hold));

        List<WaterReport> reportList = mFacade.getReportList();
        for (WaterReport wr : reportList) {
            LatLng loc = new LatLng(wr.getLocation().getLatitude(), wr.getLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(loc).title(wr.getReporter().getUsername()).snippet(wr.getSource()
                    + " - " + wr.getCondition()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        }

        mMap.setInfoWindowAdapter(new LoggedInActivity.CustomInfoWindowAdapter());
    }

    /**
     * class to create a WindowAdapter to adapt to eht GoogleMap
     */
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

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
