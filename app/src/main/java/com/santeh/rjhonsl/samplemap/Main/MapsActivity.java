package com.santeh.rjhonsl.samplemap.Main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.santeh.rjhonsl.samplemap.APIs.MyVolleyAPI;
import com.santeh.rjhonsl.samplemap.Obj.CustInfoObject;
import com.santeh.rjhonsl.samplemap.Obj.Var;
import com.santeh.rjhonsl.samplemap.Parsers.CustAndPondParser;
import com.santeh.rjhonsl.samplemap.R;
import com.santeh.rjhonsl.samplemap.Utils.GPSTracker;
import com.santeh.rjhonsl.samplemap.Utils.Helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ProgressDialog PD;
    boolean isDrawerOpen = false;
    private boolean mPaused;

    private LocationSource.OnLocationChangedListener mListener;
    Location mLastLocation;

    String username, firstname, lastname, userdescription;
    int userlevel, userid;

    DrawerLayout drawerLayout;

    ImageButton map_add_marker;
    ActionBarDrawerToggle drawerListener;


    double curlat, curLong;
    int zoom = 15,
        activeFilter;

    Activity context;
    GoogleApiClient mGoogleApiClient;
    GoogleMap maps, googleMap;

    LatLng curLatlng;

    TextView textView, tvlat, tvlong;
    TextView nav_fingerlings, nav_Stockings, nav_sperms, nav_maptype, nav_displayAllMarkers, nav_settings, nav_growout;

    EditText editSearch;
    String Stritem;


    List<CustInfoObject> custInfoObjectList;
    List<CustInfoObject> searchedIDlist = null;

    Bundle extrass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = MapsActivity.this;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extrass = getIntent().getExtras();

        //Connect app to google maps api
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nav_displayAllMarkers = (TextView) findViewById(R.id.txt_Nav_displayAll);
        nav_fingerlings = (TextView) findViewById(R.id.txt_Nav_fingerlings);
        nav_Stockings = (TextView) findViewById(R.id.txt_Nav_stockings);
        nav_sperms = (TextView) findViewById(R.id.txt_Nav_sperms);
        nav_maptype = (TextView) findViewById(R.id.txt_Nav_changeMapType);
        nav_settings = (TextView) findViewById(R.id.txt_Nav_settings);
        map_add_marker = (ImageButton) findViewById(R.id.btnaddMarker);
        nav_growout = (TextView) findViewById(R.id.txt_Nav_growOut);
        textView = (TextView) findViewById(R.id.latLong);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActionToggleDrawerListner();
        drawerListener.syncState();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        PD = new ProgressDialog(this);
        PD.setMessage("Getting data from server.\nPlease wait....");
        PD.setCancelable(false);

    }


    private void setUpMap(){
        googleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        curLatlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        curlat = myLocation.getLatitude();
        curLong = myLocation.getLongitude();

//        Helper.map_addMarker(googleMap, new LatLng(myLocation.getLatitude(), myLocation.getLongitude()),
//                R.drawable.ic_beenhere_red_24dp, "My Location",
//                String.valueOf(myLocation.getLatitude() + " " + myLocation.getLongitude()), 125+"");

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("PROCESS", "REsume");

        if(activeFilter==0){
            activeFilter = 0;
        }
        else{

        }
    }

    private void initMarkers() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("fromActivity") != null){
                String from = extras.getString("fromActivity");

                if (from.equalsIgnoreCase("viewCustinfo")) {
                    if (extras.getString("lat")!=null && extras.get("long")!= null) {
                        LatLng latLng = new LatLng(
                                Double.parseDouble(extras.getString("lat")),
                                Double.parseDouble(extras.getString("long"))  );

                        if(((Var) this.getApplication()).getGoogleMap() != null){
                            moveCameraAnimate(((Var) this.getApplication()).getGoogleMap(), latLng, 14);
                        maps.clear();
                        Helper.map_addMarker(((Var) this.getApplication()).getGoogleMap(), latLng, R.drawable.ic_place_red_24dp,
                                extras.getString("contactName"), extras.getString("address"), extras.getInt("id")+"",null, null);
                        PD.dismiss();
                        }
                        else{
                            Helper.toastShort(context, "Can't find current location. Please try again later.");
                        }

                    }
                    
                }else{
                    showAllMarker();
                }

            }
            else {
                    showAllMarker();
            }

        }
        else{
            showAllMarker();
        }
    }

    //when map is read
    @Override
    public void onMapReady(final GoogleMap map) {
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);
        maps = map;
        ((Var) this.getApplication()).setGoogleMap(map);
        Log.d("PROCESS", "onMapReady");


        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        try{

            getLastKnownLocation();

            if (checkIfLocationAvailable()){
                moveCameraAnimate(maps, getLastKnownLocation(), zoom);
                initMarkers();
            }
            else{
                //West avenue
                curlat = 14.651391;
                curLong = 121.029335;
                zoom = 9;
            }

        }catch(Exception e){
            Helper.toastShort(context, "Location is not available: "+e);
        }






        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

            }
        });


        nav_displayAllMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapClear(map);
                showAllMarker();
                closeDrawer();
            }
        });


        nav_fingerlings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapClear(map);
                closeDrawer();
            }
        });

        nav_Stockings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapClear(map);
                closeDrawer();
            }
        });

        nav_sperms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapClear(map);
                closeDrawer();
            }
        });

        map_add_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setUpMap();
                LatLng latlng = getLastKnownLocation();

                if(checkIfLocationAvailable() || latlng != null){
                    try{
                        //getLastKnownLocation();
                        final Intent intent = new Intent(MapsActivity.this, Activity_AddMarker_CustomerInfo.class);
                        intent.putExtra("latitude", latlng.latitude);
                        intent.putExtra("longtitude", latlng.longitude);
                        moveCameraAnimate(map, new LatLng(latlng.latitude, latlng.longitude), 15);
//                        Helper.toastShort(MapsActivity.this,latlng.latitude+ " "+ latlng.longitude);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog d = Helper.createCustomDialoYesNO(MapsActivity.this, R.layout.dialog_material_yesno,
                                        "Do you wish to add customer information here at your current location?\n\n" +
                                                "Latitude  : " + curlat + "\n" +
                                                "Longtitude: " + curLong, "ADD MARKER", "YES", "NO");
                                d.show();
                                TextView opt1 = (TextView) d.findViewById(R.id.btn_dialog_yesno_opt1),
                                        opt2 = (TextView) d.findViewById(R.id.btn_dialog_yesno_opt2);

                                opt1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        d.hide();
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    }
                                });

                                opt2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        d.hide();
                                    }
                                });
                            }
                        }, 1200);

                    }catch (Exception e){
                        dialogLocationNotAvailableOkOnly();
                    };
                }
                else {
                    dialogLocationNotAvailableOkOnly();
                }

            }
        });

        nav_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, Activity_Settings.class);
                closeDrawer();
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });



        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String ID = marker.getId(), curId = "";
                for (int i = 0; i < marker.getTitle().length() ; i++) {
                   char c = marker.getTitle().charAt(i);

                    if (c=='-'){
                        break;
                    }
                    curId = curId+c;

                }

                String[] details = marker.getTitle().split("-");

                Intent intent = new Intent(MapsActivity.this, Activity_FarmViewOptions.class);
                intent.putExtra("customerID","" + curId.trim());
                intent.putExtra("farmname","" + details[1]);
                startActivity(intent);

//                Dialog d = Helper.createCustomDialog(MapsActivity.this, R.layout.dialogbottomofmap);
//                d.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                WindowManager.LayoutParams wmlp = d.getWindow().getAttributes();
//                wmlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
//                wmlp.y = 20;
//
////                Button pondinfo
//
//                d.show();


//                getinfoByID(curId);
            }

        });

        nav_growout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MapsActivity.this, Activity_WeeklyReports_Growout_Option.class);
                closeDrawer();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                }, 280);

            }
        });

//        getCurrentLoc();
        // Acquire a reference to the system Location Manager


    }

    private void dialogLocationNotAvailableOkOnly() {
        final Dialog d = Helper.createCustomDialogOKOnly(MapsActivity.this,
                "LOCATION SERVICE",
                "Location is not available. Please turn your Location(GPS) Service ON and stand in an area with no obstruction for better accuracy.",
                "OK");
        TextView ok = (TextView) d.findViewById(R.id.btn_dialog_okonly_OK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.hide();
            }
        });
    }

    private boolean checkIfLocationAvailable() {
        GPSTracker gpstracker = new GPSTracker(this);
        return gpstracker.getIsGPSTrackingEnabled();
    }

    private void moveCameraAnimate(final GoogleMap map, final LatLng latlng, final int zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    public void getDistance(LatLng oldposition, LatLng newPosition) {
        float[] results = new float[1];

        Location.distanceBetween(oldposition.latitude, oldposition.longitude,
                newPosition.latitude, newPosition.longitude, results);

    }



    private void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void openDrawer() {
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }



    private void mapClear(GoogleMap map) {
        map.clear();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerListener.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
         @Override
         public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void ActionToggleDrawerListner() {
        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_menu_white_24dp,
                R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrawerOpen = false;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpen = true;
            }

        };
    }


    @Override
    public void onLocationChanged(Location location) {
        textView.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    private LatLng getLastKnownLocation() {

//        GPSTracker gps = new GPSTracker(MapsActivity.this);
//        gps.getLocation();
//
//        return new LatLng(gps.latitude, gps.longitude);

        String location_context = Context.LOCATION_SERVICE;
        LocationManager mLocationManager = (LocationManager) context.getSystemService(location_context);

        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            final Location l = mLocationManager.getLastKnownLocation(provider);
//            int d = Log.d("last known location, provider: %s, location: %s", provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
//                Log.d("found best last known location: %s", String.valueOf(l));
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }


        curlat = bestLocation.getLatitude();
        curLong = bestLocation.getLongitude();



        return new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
//        return userLocation;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            textView.setText(mLastLocation.getLatitude()+" "+mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private void showAllMarker() {
        PD.setMessage("Loading markers....");
        PD.show();

        StringRequest request = new StringRequest(Helper.variables.URL_SELECT_ALL_CUSTINFO_LEFTJOIN_PONDINFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.substring(1,2).equalsIgnoreCase("0")){
                            PD.dismiss();
                            Helper.toastShort(context, "Something Happened. Please try again later");
                        }
                        else{
                            PD.dismiss();
                            custInfoObjectList = CustAndPondParser.parseFeed(response);
                            Log.d("JSON PARSE", "BEFORE UPDATE RESOPONSE");
                            if (Helper.isBundledKeywordNotNull("login", extrass)){
                                userid = extrass.getInt("userid");
                                userlevel = extrass.getInt("userlevel");
                                username = extrass.getString("username");
                                firstname = extrass.getString("firstname");
                                lastname = extrass.getString("lastname");
                                userdescription = extrass.getString("userdescription");

                            }
                            insertloginlocation();

                            updateDisplay();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError ex) {
                        PD.dismiss();

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void insertloginlocation(){
        PD.show();
        PD.setMessage("Getting location data...");


        if (Helper.isBundledKeywordNotNull("fromActivity", extrass)){
          if (extrass.getString("fromActivity").equalsIgnoreCase("login")) {
              Log.d("EXTRAS", "fromactivity = login");

              userid = extrass.getInt("userid");
              userlevel = extrass.getInt("userlevel");
              username = extrass.getString("username");
              firstname = extrass.getString("firstname");
              lastname = extrass.getString("lastname");
              userdescription = extrass.getString("userdescription");

              StringRequest request = new StringRequest(Helper.variables.URL_INSERT_LOGINLOCATION,
                      new Response.Listener<String>() {
                          @Override
                          public void onResponse(String response) {

                              if (response.substring(1, 2).equalsIgnoreCase("0")) {
                                  PD.dismiss();
                                  Helper.toastShort(context, "Something Happened. Please try again later" + response);
                              } else {
                                  PD.dismiss();
                                  Helper.toastShort(context, "Location found :) " + " " + userid + " " + curlat + " " + curLong + " "  + response);
                              }

                          }
                      },
                      new Response.ErrorListener() {
                          @Override
                          public void onErrorResponse(VolleyError error) {
                              PD.dismiss();
                              Helper.toastShort(MapsActivity.this, "Something happened. Please try again later");
                          }
                      }) {
                  @Override
                  protected Map<String, String> getParams() {
                      Map<String, String> params = new HashMap<String, String>();
                      params.put("userid", userid + "");
                      params.put("latitude", curlat + "");
                      params.put("longitude", curLong + "");
//
                      return params;
                  }
              };

              // Adding request to request queue
              MyVolleyAPI api = new MyVolleyAPI();
              api.addToReqQueue(request, MapsActivity.this);


          }

        }

    }



    protected void updateDisplay() {

        if(!custInfoObjectList.isEmpty() && custInfoObjectList.size()>0){
            for (int i = 0; i < custInfoObjectList.size(); i++) {
                final CustInfoObject ci;
                ci = custInfoObjectList.get(i);

                Log.d("JSON PARSE", "" + ci.getFarmID() +" " +ci.getFarmname());
                LatLng custLatlng = new LatLng(Double.parseDouble(ci.getLatitude()), Double.parseDouble(ci.getLongtitude()));
                Marker marker = Helper.map_addMarker(maps, custLatlng,
                        R.drawable.ic_beenhere_red_24dp, ci.getFarmname(), ci.getAddress(), ci.getCi_id()+"", ci.getTotalStockOfFarm()+"", ci.getAllSpecie());

            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("PROCESS","Onpause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("PROCESS", "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("PROCESS", "Start");
    }


    //class that handles CustomInfowWindow
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        CustomInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }


        @Override
        public View getInfoWindow(Marker marker) {

            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.addres));
            TextView txtStock = ((TextView) myContentsView.findViewById(R.id.stocks));
            TextView txtSpecie = ((TextView) myContentsView.findViewById(R.id.specie));
//            id + "-" + farmname +"-"+ totalstock + "-" + specie
            String[] details = marker.getTitle().split("-");

            tvTitle.setText(details[1]);
            if (details[2].equalsIgnoreCase("") || details[2].equalsIgnoreCase("null")){
                txtStock.setText("Stocks: n/a");
            } else{
                txtStock.setText("Stocks: " + details[2]);
            }

            if (details[3].equalsIgnoreCase("") || details[3].equalsIgnoreCase("null")){
                txtSpecie.setText("Specie: n/a");
            } else{
                txtSpecie.setText("Specie: " + details[3]);
            }


            tvSnippet.setText(marker.getSnippet());


            return myContentsView;
        }


        @Override
        public View getInfoContents(Marker marker) {

           return null;
        }
    }


}