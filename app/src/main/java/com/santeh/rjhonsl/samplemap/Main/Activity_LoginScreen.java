package com.santeh.rjhonsl.samplemap.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.rjhonsl.samplemap.APIs.MyVolleyAPI;
import com.santeh.rjhonsl.samplemap.R;
import com.santeh.rjhonsl.samplemap.Utils.Helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rjhonsl on 9/4/2015.
 */
public class Activity_LoginScreen extends Activity{

    EditText txtusername, txtpassword;
    TextView txtappname1, txtappname2, txtshowpassword, txtforgot, txtrequestaccount, txttester, txtprogressdialog_message;

    CheckBox chkshowpasword;
    ImageButton btnLogin;


    Activity activity; Context context;
    Dialog PD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        activity = this;
        context = Activity_LoginScreen.this;

        PD =  Helper.initProgressDialog(activity);
        txtprogressdialog_message = (TextView) PD.findViewById(R.id.progress_message);

        initViews();

        Typeface font_roboto = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        Helper.hidekeyboardOnLoad(activity);


        txtappname1.setTypeface(font_roboto);
        txtappname2.setTypeface(font_roboto);
        txtpassword.setTypeface(font_roboto);
        txtusername.setTypeface(font_roboto);

        txttester.setText(
                Helper.getIMEI(context)
                        + " " +
                Helper.getMacAddress(context)
        );

        txtshowpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (chkshowpasword.isChecked()) {
                    chkshowpasword.setChecked(false);
                } else {
                    chkshowpasword.setChecked(true);
                }

                toggle_showpassword();
            }
        });

        txtrequestaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        chkshowpasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_showpassword();
            }
        });

    }

    private void initViews() {

        txtusername = (EditText) findViewById(R.id.txt_loginscreen_username);
        txtpassword = (EditText) findViewById(R.id.txt_loginscreen_password);
        txtappname1 = (TextView) findViewById(R.id.txt_loginscreen_apptitle1);
        txtappname2 = (TextView) findViewById(R.id.txt_loginscreen_apptitle2);
        txtshowpassword = (TextView) findViewById(R.id.txt_loginscreen_showpassword);
        txtforgot = (TextView) findViewById(R.id.txtforgot_password);
        txtrequestaccount = (TextView) findViewById(R.id.txt_requestaccount);

        txttester = (TextView) findViewById(R.id.txttester);

        btnLogin = (ImageButton) findViewById(R.id.btn_login);

        chkshowpasword = (CheckBox) findViewById(R.id.chk_loginscreen_showpassword);

        txtusername.getBackground().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_IN);
        txtpassword.getBackground().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_IN);
    }

    private void toggle_showpassword() {
        if (chkshowpasword.isChecked()){
            txtpassword.setTransformationMethod(null);
        }else{
            txtpassword.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    public void login() {

        if(!Helper.isNetworkAvailable(activity)) {
            Helper.toastShort(activity, "Internet Connection is not available. Please try again later.");
        }
        else{
            txtprogressdialog_message.setText("Logging in...");
            PD.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            PD.dismiss();
                            if (response.substring(1,2).equalsIgnoreCase("0")){
                                Helper.toastShort(activity,"Invalid Login credentials");
                            }
                            else {
                                Helper.toastShort(activity,"Success");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    PD.dismiss();

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("deviceid", Helper.getMacAddress(activity));
                    params.put("username", txtusername.getText().toString());
                    params.put("password", txtpassword.getText().toString());
                    return params;
                }
            };

            // Adding request to request queue
            MyVolleyAPI api = new MyVolleyAPI();
            api.addToReqQueue(postRequest, Activity_LoginScreen.this);

        }

    }



}//end of class
