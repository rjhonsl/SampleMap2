package com.santeh.rjhonsl.samplemap.Main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.santeh.rjhonsl.samplemap.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rjhonsl on 8/28/2015.
 */
public class Activity_WeeklyReports_Growout_FeedDemands extends Activity {

    TextView title, txtheader;
    SimpleDateFormat formatter = new SimpleDateFormat("MMM d");//EEE, MMM d, ''yy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklyreports_growout_feeddemandsummary);

        title = (TextView) findViewById(R.id.titlebar_header);
        txtheader = (TextView) findViewById(R.id.lbl_weeklyreports_grow_out_summary);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);


        // get start of this week in milliseconds
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        txtheader.setText("Demands for this week, " + formatter.format(cal.getTimeInMillis())  + " - ");

        // start of the next week
        cal.add(Calendar.DAY_OF_WEEK, 6);
        txtheader.setText(txtheader.getText() + formatter.format(cal.getTimeInMillis()));


    }

}
