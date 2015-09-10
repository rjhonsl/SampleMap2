package com.santeh.rjhonsl.samplemap.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.rjhonsl.samplemap.APIs.MyVolleyAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rjhonsl on 9/10/2015.
 */
public class Logging {
    static boolean isRecorded = false;

    public static boolean InsertUserActivity(final Activity activity, final Context context,
    final String userid, final String action, final String actiontype, final String latitude, final String longitude){

        StringRequest postRequest = new StringRequest(Request.Method.POST,
                Helper.variables.URL_INSERT_USER_ACTIVITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.substring(1,2).equalsIgnoreCase("0")) {
                            Log.d("USER LOGGING", "Activity logging for user "+userid+" SUCEEDED. " + response);
                            isRecorded = true;
                        }
                        else{
                            Log.d("USER LOGGING", "Activity logging for user "+userid+" FAILED. " + response);
                            InsertUserActivity(activity, context, userid, action, actiontype, latitude, longitude);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("USER LOGGING", "Activity logging for user "+userid+" FAILED. " + error);
                InsertUserActivity(activity, context, userid, action, actiontype, latitude, longitude);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", String.valueOf(userid));
                params.put("action", String.valueOf(action));
                params.put("actiontype", String.valueOf(actiontype));
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));

                return params;
            }
        };

        // Adding request to request queue
        MyVolleyAPI api = new MyVolleyAPI();
        api.addToReqQueue(postRequest, context);
        return isRecorded;
    }
}
