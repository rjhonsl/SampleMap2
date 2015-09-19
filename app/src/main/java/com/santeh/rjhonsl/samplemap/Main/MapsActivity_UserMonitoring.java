package com.santeh.rjhonsl.samplemap.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.santeh.rjhonsl.samplemap.APIs.MyVolleyAPI;
import com.santeh.rjhonsl.samplemap.Obj.CustInfoObject;
import com.santeh.rjhonsl.samplemap.Parsers.UserActivityParser;
import com.santeh.rjhonsl.samplemap.R;
import com.santeh.rjhonsl.samplemap.Utils.FusedLocation;
import com.santeh.rjhonsl.samplemap.Utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rjhonsl on 9/11/2015.
 */
public class MapsActivity_UserMonitoring extends AppCompatActivity implements OnMapReadyCallback {

    Activity activity;
    Context context;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    FusedLocation fusedLocation;
    Intent extras;
    ProgressDialog PD;

    GoogleMap gmap;

    int passedUserid;

    List <CustInfoObject> useractivityList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_usermonitoring);
        activity = this;
        context = MapsActivity_UserMonitoring.this;

        fusedLocation = new FusedLocation(context, activity);
        fusedLocation.buildGoogleApiClient(context);
        fusedLocation.connectToApiClient();

        useractivityList = new ArrayList<>();

        PD = new ProgressDialog(this);
        PD.setCancelable(false);



        extras = getIntent();
        if (extras != null){

            if (extras.hasExtra("userid")){
                passedUserid= extras.getIntExtra("userid",0);
            }
        }




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity_UserMonitoring.this);

    }


    

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        gmap = googleMap;

        fusedLocation.connectToApiClient();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    LatLng latLng = fusedLocation.getLastKnowLocation();
                    Helper.moveCameraAnimate(googleMap, latLng, 14);
                } catch (Exception e) {
                    Helper.toastShort(activity, "Location is not available: " + e);
                }
            }
        }, 200);

        getAllUsers();

    }


    public void getAllUsers() {
        PD.setMessage("Please wait...");
        PD.show();

        if(!Helper.isNetworkAvailable(activity)) {
            Helper.toastShort(activity, "Internet Connection is not available. Please try again later.");
            PD.dismiss();
        }
        else{


            StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_SELECT_ALL_USERS_ACTIVITY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Helper.toastLong(activity, response);
                            if (response.substring(1,2).equalsIgnoreCase("0")){
                                PD.dismiss();
                                Helper.toastShort(activity, "Something happened. Please try again later.");
                            }else{

                                PD.dismiss();

                                useractivityList  = new ArrayList<CustInfoObject>();
                                useractivityList = UserActivityParser.parseFeed(response);
                                if (useractivityList != null){
                                    if (useractivityList.size() > 0){
                                        for (int i = 0; i < useractivityList.size(); i++) {
                                            Helper.map_addMarkerIconGen(gmap,
                                                   new LatLng(Double.parseDouble(useractivityList.get(i).getLatitude()), Double.parseDouble(useractivityList.get(i).getLongtitude())),
                                                    Helper.iconGeneratorSample(context, i+"", activity), "","","","","");
                                        }
                                    }
                                }else{
                                    Helper.toastLong(activity, "Something happened. Please try again later");
                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    PD.dismiss();
                    Helper.toastShort(activity, error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", Helper.variables.getGlobalVar_currentUsername(activity));
                    params.put("password", Helper.variables.getGlobalVar_currentUserpassword(activity));
                    params.put("deviceid", Helper.getMacAddress(context));
                    params.put("userid", Helper.variables.getGlobalVar_currentUserID(activity)+"");
                    params.put("idofuser", passedUserid+"");

                    return params;
                }
            };

            // Adding request to request queue
            MyVolleyAPI api = new MyVolleyAPI();
            api.addToReqQueue(postRequest, context);

        }

    }


}//end of class
