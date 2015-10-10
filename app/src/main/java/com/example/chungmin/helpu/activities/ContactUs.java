package com.example.chungmin.helpu.activities;

import android.os.Bundle;

import com.example.chungmin.helpu.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ContactUs extends HelpUBaseActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        double currentLatitude = 5.363647071692414;
        double currentLongitude = 100.4497599180541;
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(getString(R.string.strEnterprise)))
                .showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 15.0f));
    }
}
