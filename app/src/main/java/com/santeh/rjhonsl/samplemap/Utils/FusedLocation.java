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

    public synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

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

    public LatLng getLastKnowLocation(Context context, Activity activity) {
        LatLng latLng = new LatLng(0.0, 0.0);
        if (Helper.isLocationEnabled(context)) {
            if (!mGoogleApiClient.isConnected()) {
                connectToApiClient();
            }

            if (mLastLocation != null) {
               latLng =  new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }

        }else{
            Helper.toastLong(activity, "Location Service is not available");
        }

        return latLng;
    }

    @Override
    public void onConnected(Bundle bundle) {
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
