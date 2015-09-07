package com.santeh.rjhonsl.samplemap.Main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.rjhonsl.samplemap.APIs.MyVolleyAPI;
import com.santeh.rjhonsl.samplemap.Adapter.Adapter_Growouts_PondWeekLyConsumption;
import com.santeh.rjhonsl.samplemap.Obj.CustInfoObject;
import com.santeh.rjhonsl.samplemap.Parsers.PondInfoJsonParser;
import com.santeh.rjhonsl.samplemap.R;
import com.santeh.rjhonsl.samplemap.Utils.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rjhonsl on 8/25/2015.
 */
public class Activity_WeeklyReports_Growout_FarmSummary extends Activity {

    int customerID;
    int colorStartofWeek = Color.parseColor("#66F2C1D2");
    int headerColor = Color.parseColor("#ff80cbc4");
    int defaultBGcolor = Color.parseColor("#66c1f2ed");
    int colorCurrentWeek = Color.parseColor("#ff80bacb");
    int colorSameWeek = Color.parseColor("#dfa8e9");

    Adapter_Growouts_PondWeekLyConsumption adapterPondWeeklyReport;

    String farmName;

    Activity activity;

    ProgressDialog PD;

    CheckBox ch1, ch2, ch3, ch4, ch5, ch6, ch7, ch8, ch9, ch10, ch11, ch12, ch13, ch14, ch15, ch16, ch17, ch18;

    TextView txtSpecie, txtFarmName, txtdateStocked, txtQuantity,
    txtfeedtype_r1, txtfeedtype_r2,  txtfeedtype_r3, txtfeedtype_r4, txtfeedtype_r5, txtfeedtype_r6, txtfeedtype_r7, txtfeedtype_r8, txtfeedtype_r9, txtfeedtype_r10, txtfeedtype_r11,txtfeedtype_r12, txtfeedtype_r13, txtfeedtype_r14, txtfeedtype_r15, txtfeedtype_r16, txtfeedtype_r17, txtfeedtype_r18;

    TextView txtRecommendCons_r1, txtRecommendCons_r2, txtRecommendCons_r3, txtRecommendCons_r4, txtRecommendCons_r5, txtRecommendCons_r6, txtRecommendCons_r7, txtRecommendCons_r8, txtRecommendCons_r9, txtRecommendCons_r10, txtRecommendCons_r11, txtRecommendCons_r12, txtRecommendCons_r13, txtRecommendCons_r14, txtRecommendCons_r15, txtRecommendCons_r16, txtRecommendCons_r17, txtRecommendCons_r18;

    LinearLayout llr1, llr2, llr3, llr4, llr5, llr6, llr7, llr8, llr9, llr10, llr11, llr12, llr13, llr14, llr15, llr16, llr17, llr18, llWholeSpreadsheet;

    List<CustInfoObject> pondInfoList;
    List<CustInfoObject> pondconsumptionList;

    ListView lvPonds;
    String [] pondListArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growout_weeklyreport);
        activity = this;

        PD = new ProgressDialog(this);
        PD.setMessage("Updating database. Please wait....");
        PD.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if (extras !=null){
            customerID = extras.getInt("id");
            farmName = extras.getString("farmname");
            getdataByID(customerID);
        }else{
            customerID = 0;
        }

        initViewsFromXML();



        txtSpecie.setText("1 - Bangus");
        txtFarmName.setText("" + farmName);

//        = ABW x total number of stocks x feeding rate x survival rate

        txtSpecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pondListArray.length > 0) {
                    final Dialog d = Helper.createCustomListDialog(activity, pondListArray, "Ponds");
                    d.show();

                    ListView lstPond = (ListView) d.findViewById(R.id.dialog_list_listview);
                    lstPond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            txtSpecie.setText(pondListArray[position]);
                            txtQuantity.setText("Quantity: " + pondInfoList.get(position).getQuantity() + "");
                            txtdateStocked.setText("Date Stocked: " + pondInfoList.get(position).getDateStocked() + "");
                            d.hide();
                            populate_rows(position);
                        }
                    });
                }

            }
        });

    }

    private void populate_rows(int position) {
        int startWeek = Helper.get_StartWeekOf_Tilapia_ByABW(pondInfoList.get(position).getSizeofStock());
        int quantity = pondInfoList.get(position).getQuantity();
        int currentweek =  Helper.get_currentWeek_by_stockedDate(pondInfoList.get(position).getDateStocked(), pondInfoList.get(position).getSizeofStock());



//        setStartWeekHighLight(  startWeek, Helper.get_currentWeek_by_stockedDate(pondInfoList.get(position).getDateStocked(), pondInfoList.get(position).getSizeofStock())
//                               );


        pondconsumptionList = new ArrayList<>();
        for (int i = 0; i < 18; i++) {

            CustInfoObject obj = new CustInfoObject();
            Log.d("REPORT", "" + i);

            obj.setStartweekofStock(startWeek);
            obj.setCurrentfeedType(Helper.getFeedTypeByNumberOfWeeks(i + 1));
            obj.setCurrentweekofStock(i + 1);
            obj.setWeek(currentweek);
            obj.setRemarks("N/A");
            double abw = Helper.get_ABW_BY_WEEK_NO(i + 1), quantity1 = quantity , survivalrate = 0.7, feedingrate = Helper.get_FeedingRate_by_WeekNum(i+1);
            double recommended = (abw * quantity1 * survivalrate * feedingrate * 7) / 1000;
//            + " abw: "+ abw+ " quatity: " + quantity1 + " survival: " + survivalrate + " feedingrate: " +feedingrate
            DecimalFormat df = new DecimalFormat("#.##");
            obj.setRecommendedConsumption("" + df.format(recommended));

            obj.setActualConsumption("N/A");

            pondconsumptionList.add(obj);
        }






        adapterPondWeeklyReport = new Adapter_Growouts_PondWeekLyConsumption(Activity_WeeklyReports_Growout_FarmSummary.this,
                R.layout.item_lv_weeklypondsummary, pondconsumptionList);
        lvPonds.setAdapter(adapterPondWeeklyReport);
    }

    public void setStartWeekHighLight(int weekstarted, int weekCurrent) {



    }

    private void initViewsFromXML() {

        txtFarmName = (TextView) findViewById(R.id.lbl_weeklyreports_farmName);
        txtdateStocked = (TextView) findViewById(R.id.txt_weeklyreports_DateStocked);
        txtQuantity = (TextView) findViewById(R.id.txt_weeklyreports_quantity);
        txtSpecie = (TextView) findViewById(R.id.txt_weeklyreports_Specie);
        lvPonds = (ListView) findViewById(R.id.lv_pond_weeklyReports);

//
//        txtfeedtype_r1 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r1);
//        txtfeedtype_r2 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r2);
//        txtfeedtype_r3 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r3);
//        txtfeedtype_r4 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r4);
//        txtfeedtype_r5 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r5);
//        txtfeedtype_r6 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r6);
//        txtfeedtype_r7 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r7);
//        txtfeedtype_r8 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r8);
//        txtfeedtype_r9 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r9);
//        txtfeedtype_r10 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r10);
//        txtfeedtype_r11 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r11);
//        txtfeedtype_r12 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r12);
//        txtfeedtype_r13 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r13);
//        txtfeedtype_r14 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r14);
//        txtfeedtype_r15 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r15);
//        txtfeedtype_r16 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r16);
//        txtfeedtype_r17 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r17);
//        txtfeedtype_r18 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r18);
//
//        txtRecommendCons_r1 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r1);
//        txtRecommendCons_r2 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r2);
//        txtRecommendCons_r3 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r3);
//        txtRecommendCons_r4 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r4);
//        txtRecommendCons_r5 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r5);
//        txtRecommendCons_r6 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r6);
//        txtRecommendCons_r7 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r7);
//        txtRecommendCons_r8 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r8);
//        txtRecommendCons_r9 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r9);
//        txtRecommendCons_r10 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r10);
//        txtRecommendCons_r11 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r11);
//        txtRecommendCons_r12 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r12);
//        txtRecommendCons_r13 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r13);
//        txtRecommendCons_r14 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r14);
//        txtRecommendCons_r15 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r15);
//        txtRecommendCons_r16 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r16);
//        txtRecommendCons_r17 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r17);
//        txtRecommendCons_r18 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r18);
//
//        llWholeSpreadsheet = (LinearLayout) findViewById(R.id.spreadsheet_farmsummary);
//        llr1 = (LinearLayout) findViewById(R.id.reports_row1);
//        llr2 = (LinearLayout) findViewById(R.id.reports_row2);
//        llr3 = (LinearLayout) findViewById(R.id.reports_row3);
//        llr4 = (LinearLayout) findViewById(R.id.reports_row4);
//        llr5 = (LinearLayout) findViewById(R.id.reports_row5);
//        llr6 = (LinearLayout) findViewById(R.id.reports_row6);
//        llr7 = (LinearLayout) findViewById(R.id.reports_row7);
//        llr8 = (LinearLayout) findViewById(R.id.reports_row8);
//        llr9 = (LinearLayout) findViewById(R.id.reports_row9);
//        llr10 = (LinearLayout) findViewById(R.id.reports_row10);
//        llr11 = (LinearLayout) findViewById(R.id.reports_row11);
//        llr12 = (LinearLayout) findViewById(R.id.reports_row12);
//        llr13 = (LinearLayout) findViewById(R.id.reports_row13);
//        llr14 = (LinearLayout) findViewById(R.id.reports_row14);
//        llr15 = (LinearLayout) findViewById(R.id.reports_row15);
//        llr16 = (LinearLayout) findViewById(R.id.reports_row16);
//        llr17 = (LinearLayout) findViewById(R.id.reports_row17);
//        llr18 = (LinearLayout) findViewById(R.id.reports_row18);
//
//        ch1 = (CheckBox) findViewById(R.id.chk_farmsummary_r1);
//        ch2 = (CheckBox) findViewById(R.id.chk_farmsummary_r2);
//        ch3 = (CheckBox) findViewById(R.id.chk_farmsummary_r3);
//        ch4 = (CheckBox) findViewById(R.id.chk_farmsummary_r4);
//        ch5 = (CheckBox) findViewById(R.id.chk_farmsummary_r5);
//        ch6 = (CheckBox) findViewById(R.id.chk_farmsummary_r6);
//        ch7 = (CheckBox) findViewById(R.id.chk_farmsummary_r7);
//        ch8 = (CheckBox) findViewById(R.id.chk_farmsummary_r8);
//        ch9 = (CheckBox) findViewById(R.id.chk_farmsummary_r9);
//        ch10 = (CheckBox) findViewById(R.id.chk_farmsummary_r10);
//        ch11 = (CheckBox) findViewById(R.id.chk_farmsummary_r11);
//        ch12 = (CheckBox) findViewById(R.id.chk_farmsummary_r12);
//        ch13 = (CheckBox) findViewById(R.id.chk_farmsummary_r13);
//        ch14 = (CheckBox) findViewById(R.id.chk_farmsummary_r14);
//        ch15 = (CheckBox) findViewById(R.id.chk_farmsummary_r15);
//        ch16 = (CheckBox) findViewById(R.id.chk_farmsummary_r16);
//        ch17 = (CheckBox) findViewById(R.id.chk_farmsummary_r17);
//        ch18 = (CheckBox) findViewById(R.id.chk_farmsummary_r18);


    }



    public void getdataByID(final int custID) {

        if(!Helper.isNetworkAvailable(activity)) {
            Helper.toastShort(activity, "Internet Connection is not available. Please try again later.");
        }
        else{
            PD.setMessage("Getting information from server...");
            PD.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_SELECT_PONDINFO_BY_CUSTOMER_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            PD.dismiss();

                            if (response.substring(1,2).equalsIgnoreCase("0")){
//                                Helper.toastLong(activity, "failed. "+ response.substring(1,2));

                            }else{
                                pondInfoList = PondInfoJsonParser.parseFeed(response);

                                assert pondInfoList != null;
                                pondListArray = new String[pondInfoList.size()];
                                for (int i = 0; i < pondInfoList.size(); i++) {
                                    pondListArray[i] = pondInfoList.get(i).getPondID() + " - " + pondInfoList.get(i).getSpecie();
                                }

                                txtSpecie.setText(pondListArray[0]);
                                txtQuantity.setText("Quantity: "+pondInfoList.get(0).getQuantity()+"");
                                txtdateStocked.setText("Date Stocked: "+pondInfoList.get(0).getDateStocked()+"");
                                populate_rows(0);
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    final Dialog d = Helper.createCustomDialogOKOnly(Activity_WeeklyReports_Growout_FarmSummary.this, "OOPS",
                            "Something went wrong. Please try again later.", "OK");
                    TextView ok = (TextView) d.findViewById(R.id.btn_dialog_okonly_OK);
                    d.setCancelable(false);
                    d.show();
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.hide();
                        }
                    });
                    PD.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("customerId", String.valueOf(custID));
                    return params;
                }
            };

            // Adding request to request queue
            MyVolleyAPI api = new MyVolleyAPI();
            api.addToReqQueue(postRequest, Activity_WeeklyReports_Growout_FarmSummary.this);

        }

    }

    public void insertDataByCustomerID(final int pondId) {

        if(!Helper.isNetworkAvailable(activity)) {
            Helper.toastShort(activity, "Internet Connection is not available. Please try again later.");
        }
        else{
            PD.setMessage("Updating database... ");
            PD.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST, Helper.variables.URL_SELECT_PONDINFO_BY_CUSTOMER_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            PD.dismiss();

                            if (response.substring(1,2).equalsIgnoreCase("0")){
//                                Helper.toastLong(activity, "failed. "+ response.substring(1,2));

                            }else{

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    final Dialog d = Helper.createCustomDialogOKOnly(Activity_WeeklyReports_Growout_FarmSummary.this, "OOPS",
                            "Something went wrong. Please try again later.", "OK");
                    TextView ok = (TextView) d.findViewById(R.id.btn_dialog_okonly_OK);
                    d.setCancelable(false);
                    d.show();
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.hide();
                        }
                    });
                    PD.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

//                    params.put("p_wc_pond_indexid", indexid);
//                    params.put("p_wc_consumption", actualConsumption);
//                    params.put("p_wc_weekno", weekno);
//                    params.put("p_wc_visited", isVisited);
//                    params.put("p_wc_datevisited", dateVisited);
                    return params;
                }
            };

            // Adding request to request queue
            MyVolleyAPI api = new MyVolleyAPI();
            api.addToReqQueue(postRequest, Activity_WeeklyReports_Growout_FarmSummary.this);

        }

    }



    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }




}
