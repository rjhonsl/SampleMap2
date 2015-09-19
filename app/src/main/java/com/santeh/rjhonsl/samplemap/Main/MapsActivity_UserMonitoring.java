package com.santeh.rjhonsl.samplemap.Main;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.santeh.rjhonsl.samplemap.R;
import com.santeh.rjhonsl.samplemap.Utils.FusedLocation;
import com.santeh.rjhonsl.samplemap.Utils.Helper;

/**
 * Created by rjhonsl on 9/11/2015.
 */
public class MapsActivity_UserMonitoring extends AppCompatActivity implements OnMapReadyCallback {

    Activity activity;
    Context context;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    FusedLocation fusedLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_usermonitoring);
        activity = this;
        context = MapsActivity_UserMonitoring.this;

        fusedLocation = new FusedLocation(context, activity);
        fusedLocation.connectToApiClient();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity_UserMonitoring.this);


        ImageButton button = (ImageButton) findViewById(R.id.btnaddMarker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Helper.isLocationEnabled(context)){
                    LatLng latLng =   fusedLocation.getLastKnowLocation();
                    Helper.toastShort(activity, latLng.latitude + " " + latLng.longitude);
                }else
                {
                    Helper.toastShort(activity, "Location not available");
                }

            }
        });

    }


    

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
    }


}
