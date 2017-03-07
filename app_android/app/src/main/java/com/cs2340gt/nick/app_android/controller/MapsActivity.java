package com.cs2340gt.nick.app_android.controller;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cs2340gt.nick.app_android.model.WaterReport;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import com.cs2340gt.nick.app_android.R;
import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.Location;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // GoogleMap to put in the display
    private GoogleMap mMap;
    // Model that we're currently using
    private Model mFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {



                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);



                // Clears the previously touched position
                // mMap.clear();
//                WaterReport newRep = new WaterReport(Model.getInstance().getCurrentAccount(), )
//                mFacade.addReport("newly added", "Bobs Place", new Location(latLng.latitude, latLng.longitude));
//
//                // Setting the title for the marker.
//                // This will be displayed on taping the marker
//                markerOptions.title(mFacade.getLastReport().getName());
//                markerOptions.snippet(mFacade.getLastReport().getDescription());

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });

        List<WaterReport> reportList = mFacade.getReportList();
        for (WaterReport wr : reportList) {
            LatLng loc = new LatLng(wr.getLocation().getLatitude(), wr.getLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(loc).title(wr.getReporter().getUsername()).snippet(wr.getSource()
                    + " - " + wr.getCondition()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        }

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
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
