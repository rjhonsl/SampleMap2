package com.santeh.rjhonsl.samplemap.Main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santeh.rjhonsl.samplemap.APIs.MyVolleyAPI;
import com.santeh.rjhonsl.samplemap.Obj.CustInfoObject;
import com.santeh.rjhonsl.samplemap.Parsers.PondInfoJsonParser;
import com.santeh.rjhonsl.samplemap.R;
import com.santeh.rjhonsl.samplemap.Utils.Helper;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rjhonsl on 8/25/2015.
 */
public class Activity_WeeklyReports_Growout_FarmSummary extends Activity {

    int customerID;
    String farmName;

    Activity activity;

    ProgressDialog PD;

    TextView txtSpecie, txtFarmName, txtdateStocked, txtQuantity,
    txtfeedtype_r1, txtfeedtype_r2,  txtfeedtype_r3, txtfeedtype_r4, txtfeedtype_r5, txtfeedtype_r6, txtfeedtype_r7, txtfeedtype_r8, txtfeedtype_r9, txtfeedtype_r10, txtfeedtype_r11,txtfeedtype_r12, txtfeedtype_r13, txtfeedtype_r14, txtfeedtype_r15, txtfeedtype_r16, txtfeedtype_r17, txtfeedtype_r18;

    TextView txtRecommendCons_r1, txtRecommendCons_r2, txtRecommendCons_r3, txtRecommendCons_r4, txtRecommendCons_r5, txtRecommendCons_r6, txtRecommendCons_r7, txtRecommendCons_r8, txtRecommendCons_r9, txtRecommendCons_r10, txtRecommendCons_r11, txtRecommendCons_r12, txtRecommendCons_r13, txtRecommendCons_r14, txtRecommendCons_r15, txtRecommendCons_r16, txtRecommendCons_r17, txtRecommendCons_r18;

    LinearLayout llr1, llr2, llr3, llr4, llr5, llr6, llr7, llr8, llr9, llr10, llr11, llr12, llr13, llr14, llr15, llr16, llr17, llr18;

    List<CustInfoObject> pondInfoList;
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

                if (pondListArray.length > 0){
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
        setStartWeekHighLight(startWeek);


        txtRecommendCons_r1.setText("Blind Feeding");
        txtRecommendCons_r2.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(2), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[1], 0.70)+"" );
        txtRecommendCons_r3.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(3), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[2], 0.70)+"" );
        txtRecommendCons_r4.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(4), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[3], 0.70)+"" );
        txtRecommendCons_r5.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(5), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[4], 0.70)+"" );
        txtRecommendCons_r6.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(6), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[5], 0.70)+"" );
        txtRecommendCons_r7.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(7), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[6], 0.70)+"" );
        txtRecommendCons_r8.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(8), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[7], 0.70)+"" );
        txtRecommendCons_r9.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(9), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[8], 0.70)+"" );
        txtRecommendCons_r10.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(10), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[9], 0.70)+"" );
        txtRecommendCons_r11.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(11), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[10], 0.70)+"" );
        txtRecommendCons_r12.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(12), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[11], 0.70)+"" );
        txtRecommendCons_r13.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(13), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[12], 0.70)+"" );
        txtRecommendCons_r14.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(14), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[13], 0.70)+"" );
        txtRecommendCons_r15.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(15), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[14], 0.70)+"" );
        txtRecommendCons_r16.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(16), pondInfoList.get(position).getQuantity(),
                        Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[15], 0.70) + "");
        txtRecommendCons_r17.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(17), pondInfoList.get(position).getQuantity(),
                Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[16], 0.70)+"" );
        txtRecommendCons_r18.setText(
                computeWeeklyFeedConsumption(Helper.get_ABW_BY_WEEK_NO(18), pondInfoList.get(position).getQuantity(),
                        Helper.variables.ARRAY_FEEDING_RATE_PER_WEEK[17], 0.70) + "");


        txtfeedtype_r1.setText(Helper.getFeedTypeByNumberOfWeeks(1));
        txtfeedtype_r2.setText(Helper.getFeedTypeByNumberOfWeeks(2));
        txtfeedtype_r3.setText(Helper.getFeedTypeByNumberOfWeeks(3));
        txtfeedtype_r4.setText(Helper.getFeedTypeByNumberOfWeeks(4));
        txtfeedtype_r5.setText(Helper.getFeedTypeByNumberOfWeeks(5));
        txtfeedtype_r6.setText(Helper.getFeedTypeByNumberOfWeeks(6));
        txtfeedtype_r7.setText(Helper.getFeedTypeByNumberOfWeeks(7));
        txtfeedtype_r8.setText(Helper.getFeedTypeByNumberOfWeeks(8));
        txtfeedtype_r9.setText(Helper.getFeedTypeByNumberOfWeeks(9));
        txtfeedtype_r10.setText(Helper.getFeedTypeByNumberOfWeeks(10));
        txtfeedtype_r11.setText(Helper.getFeedTypeByNumberOfWeeks(11));
        txtfeedtype_r12.setText(Helper.getFeedTypeByNumberOfWeeks(12));
        txtfeedtype_r13.setText(Helper.getFeedTypeByNumberOfWeeks(13));
        txtfeedtype_r14.setText(Helper.getFeedTypeByNumberOfWeeks(14));
        txtfeedtype_r15.setText(Helper.getFeedTypeByNumberOfWeeks(15));
        txtfeedtype_r16.setText(Helper.getFeedTypeByNumberOfWeeks(16));
        txtfeedtype_r17.setText(Helper.getFeedTypeByNumberOfWeeks(17));
        txtfeedtype_r18.setText(Helper.getFeedTypeByNumberOfWeeks(18));
    }

    public void setStartWeekHighLight(int week) {
        int hightlightcolor = Color.parseColor("#66F2C1D2");

        if (week == 1) {
            llr1.setBackgroundColor(hightlightcolor);
        } else if (week == 2) {
            llr2.setBackgroundColor(hightlightcolor);
        } else if (week == 3) {
            llr3.setBackgroundColor(hightlightcolor);
        } else if (week == 4) {
            llr4.setBackgroundColor(hightlightcolor);
        } else if (week == 5) {
            llr5.setBackgroundColor(hightlightcolor);
        } else if (week == 6) {
            llr6.setBackgroundColor(hightlightcolor);
        } else if (week == 7) {
            llr7.setBackgroundColor(hightlightcolor);
        } else if (week == 8) {
            llr8.setBackgroundColor(hightlightcolor);
        } else if (week == 9) {
            llr9.setBackgroundColor(hightlightcolor);
        } else if (week == 10) {
            llr10.setBackgroundColor(hightlightcolor);
        } else if (week == 11) {
            llr11.setBackgroundColor(hightlightcolor);
        } else if (week == 12) {
            llr12.setBackgroundColor(hightlightcolor);
        } else if (week == 13) {
            llr13.setBackgroundColor(hightlightcolor);
        } else if (week == 14) {
            llr14.setBackgroundColor(hightlightcolor);
        } else if (week == 15) {
            llr15.setBackgroundColor(hightlightcolor);
        } else if (week == 16) {
            llr16.setBackgroundColor(hightlightcolor);
        } else if (week == 17) {
            llr17.setBackgroundColor(hightlightcolor);
        } else if (week == 18) {
            llr18.setBackgroundColor(hightlightcolor);
        }

    }

    private void initViewsFromXML() {
        txtFarmName = (TextView) findViewById(R.id.lbl_weeklyreports_farmName);
        txtdateStocked = (TextView) findViewById(R.id.txt_weeklyreports_DateStocked);
        txtQuantity = (TextView) findViewById(R.id.txt_weeklyreports_quantity);

        txtfeedtype_r1 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r1);
        txtfeedtype_r2 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r2);
        txtfeedtype_r3 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r3);
        txtfeedtype_r4 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r4);
        txtfeedtype_r5 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r5);
        txtfeedtype_r6 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r6);
        txtfeedtype_r7 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r7);
        txtfeedtype_r8 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r8);
        txtfeedtype_r9 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r9);
        txtfeedtype_r10 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r10);
        txtfeedtype_r11 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r11);
        txtfeedtype_r12 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r12);
        txtfeedtype_r13 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r13);
        txtfeedtype_r14 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r14);
        txtfeedtype_r15 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r15);
        txtfeedtype_r16 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r16);
        txtfeedtype_r17 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r17);
        txtfeedtype_r18 = (TextView) findViewById(R.id.lbl_weeklyreports_feedtype_r18);

        txtRecommendCons_r1 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r1);
        txtRecommendCons_r2 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r2);
        txtRecommendCons_r3 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r3);
        txtRecommendCons_r4 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r4);
        txtRecommendCons_r5 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r5);
        txtRecommendCons_r6 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r6);
        txtRecommendCons_r7 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r7);
        txtRecommendCons_r8 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r8);
        txtRecommendCons_r9 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r9);
        txtRecommendCons_r10 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r10);
        txtRecommendCons_r11 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r11);
        txtRecommendCons_r12 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r12);
        txtRecommendCons_r13 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r13);
        txtRecommendCons_r14 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r14);
        txtRecommendCons_r15 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r15);
        txtRecommendCons_r16 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r16);
        txtRecommendCons_r17 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r17);
        txtRecommendCons_r18 = (TextView) findViewById(R.id.lbl_weeklyreports_recommendedConsumption_r18);

        llr1 = (LinearLayout) findViewById(R.id.reports_row1);
        llr2 = (LinearLayout) findViewById(R.id.reports_row2);
        llr3 = (LinearLayout) findViewById(R.id.reports_row3);
        llr4 = (LinearLayout) findViewById(R.id.reports_row4);
        llr5 = (LinearLayout) findViewById(R.id.reports_row5);
        llr6 = (LinearLayout) findViewById(R.id.reports_row6);
        llr7 = (LinearLayout) findViewById(R.id.reports_row7);
        llr8 = (LinearLayout) findViewById(R.id.reports_row8);
        llr9 = (LinearLayout) findViewById(R.id.reports_row9);
        llr10 = (LinearLayout) findViewById(R.id.reports_row10);
        llr11 = (LinearLayout) findViewById(R.id.reports_row11);
        llr12 = (LinearLayout) findViewById(R.id.reports_row12);
        llr13 = (LinearLayout) findViewById(R.id.reports_row13);
        llr14 = (LinearLayout) findViewById(R.id.reports_row14);
        llr15 = (LinearLayout) findViewById(R.id.reports_row15);
        llr16 = (LinearLayout) findViewById(R.id.reports_row16);
        llr17 = (LinearLayout) findViewById(R.id.reports_row17);
        llr18 = (LinearLayout) findViewById(R.id.reports_row18);


        txtSpecie = (TextView) findViewById(R.id.txt_weeklyreports_Specie);
    }

    private String computeWeeklyFeedConsumption(double ABW, double NumberofStock, double feedingrate, double survivalrate) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format ((ABW*NumberofStock*feedingrate*survivalrate)*7);
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



    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }




}
