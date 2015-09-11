package com.santeh.rjhonsl.samplemap.Utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by rjhonsl on 9/11/2015.
 */


public class FusedLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    Context fixContext;
    Activity fixActivity;
    LatLng latLng;

    public FusedLocation(Context context, Activity activity){

        fixContext = context;
        fixActivity = activity;
        buildGoogleApiClient(fixContext);
    }

    public void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        connectToApiClient();
    }

    public void connectToApiClient() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }
    public void disconnectFromApiClient() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    public LatLng getLastKnowLocation() {
        latLng = new LatLng(0.0, 0.0);

        disconnectFromApiClient();
        connectToApiClient();

        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            latLng = new LatLng(0.0, 0.0);
        }

        return latLng;
    }

    @Override
    public  void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}



