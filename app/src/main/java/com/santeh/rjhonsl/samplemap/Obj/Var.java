package com.santeh.rjhonsl.samplemap.Obj;

import android.app.Application;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by rjhonsl on 8/12/2015.
 */
public class Var extends Application {

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private GoogleMap googleMap;

}
