package com.santeh.rjhonsl.samplemap.Obj;

import android.app.Application;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by rjhonsl on 8/12/2015.
 */
public class Var extends Application {


//    intent.putExtra("userid", listaccounts.get(0).getUserid());
//    intent.putExtra("userlevel", listaccounts.get(0).getUserid());
//    intent.putExtra("username", listaccounts.get(0).getUserid());
//    intent.putExtra("firstname", listaccounts.get(0).getUserid());
//    intent.putExtra("userid", listaccounts.get(0).getUserid());
//    intent.putExtra("userdescription", listaccounts.get(0).getAccountlevelDescription());

    public int currentuser;


    public int currentuserLvl;

    private GoogleMap googleMap;

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public int getCurrentuser() {
        return currentuser;
    }

    public void setCurrentuser(int currentuser) {
        this.currentuser = currentuser;
    }

    public int getCurrentuserLvl() {
        return currentuserLvl;
    }

    public void setCurrentuserLvl(int currentuserLvl) {
        this.currentuserLvl = currentuserLvl;
    }


}
