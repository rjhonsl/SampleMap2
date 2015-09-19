package com.santeh.rjhonsl.samplemap.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.rjhonsl.samplemap.APIs.MyVolleyAPI;
import com.santeh.rjhonsl.samplemap.Adapter.AdapterUsermonitoring_ViewByUser;
import com.santeh.rjhonsl.samplemap.Obj.CustInfoObject;
import com.santeh.rjhonsl.samplemap.Parsers.AccountsParser;
import com.santeh.rjhonsl.samplemap.R;
import com.santeh.rjhonsl.samplemap.Utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rjhonsl on 9/19/2015.
 */
public class Activity_UserMonitoring_ViewByUser extends Activity {

    Activity activity;
    Context context;
    ListView lvUsers;

    List<CustInfoObject> userlist;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermonitoring_viewbyuser);
        activity = this;
        context = Activity_UserMonitoring_ViewByUser.this;


        PD = new ProgressDialog(this);
        PD. setCancelable(false);

        lvUsers = (ListView) findViewById(R.id.listview_userMonitoring);
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


            StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_SELECT_ALL_USERS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.substring(1,2).equalsIgnoreCase("0")){
                                Helper.toastShort(activity, "Something happened. Please try again later.");
                            }else{

                                PD.dismiss();
                                userlist = new ArrayList<CustInfoObject>();
                                userlist = AccountsParser.parseFeed(response);

                                if (userlist != null) {
                                    AdapterUsermonitoring_ViewByUser custinfoAdapter = new AdapterUsermonitoring_ViewByUser(context, R.layout.item_lv_viewcustomerinfo, userlist);
                                    lvUsers.setAdapter(custinfoAdapter);
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
                    return params;
                }
            };

            // Adding request to request queue
            MyVolleyAPI api = new MyVolleyAPI();
            api.addToReqQueue(postRequest, context);

        }

    }
}
