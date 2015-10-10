package com.example.chungmin.helpu.activities.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.TextView;

import com.example.chungmin.helpu.R;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;

import HelpUGenericUtilities.DateTimeUtils;
import HelpUGenericUtilities.Enumerations;

import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class Test extends AppCompatActivity {

    private TextView tvCurrentDateTime;
    private TextView tvResultSec, tvResultMin, tvResultHour, tvResultDay, tvResultWeek, tvResultYear;
    private TextView tvCurrentDateTimeMilis;
    private TextView tvItemDateTimeMilis;
    private TextView tvItemDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvCurrentDateTime = (TextView) findViewById(R.id.tvCurrentDateTime);
        tvCurrentDateTimeMilis = (TextView) findViewById(R.id.tvCurrentDateTimeMilis);
        tvItemDate = (TextView) findViewById(R.id.tvItemDate);
        tvItemDateTimeMilis = (TextView) findViewById(R.id.tvItemDateTimeMilis);
        tvResultSec = (TextView) findViewById(R.id.tvResultSec);
        tvResultMin = (TextView) findViewById(R.id.tvResultMin);
        tvResultHour = (TextView) findViewById(R.id.tvResultHour);
        tvResultDay = (TextView) findViewById(R.id.tvResultDay);
        tvResultWeek = (TextView) findViewById(R.id.tvResultWeek);
        tvResultYear = (TextView) findViewById(R.id.tvResultYear);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        DateTime currentDate = DateTime.now();
        long currentDateLong = currentDate.getMillis();
        String formattedCurrDate = df.format(currentDateLong);

        DateTime itemDate = new DateTime(2015, 10, 5, 13, 22, 10);
        long itemDateLong = itemDate.getMillis();
        String formattedItemDate = df.format(itemDateLong);

        tvCurrentDateTime.setText(formattedCurrDate);
        tvCurrentDateTimeMilis.setText(Long.toString(currentDateLong));
        tvItemDate.setText(formattedItemDate);
        tvItemDateTimeMilis.setText(Long.toString(itemDateLong));

        String resultSecInMillis = getRelativeTimeSpanString(itemDateLong, currentDateLong, DateUtils.SECOND_IN_MILLIS).toString();
        String resultMinInMillis = getRelativeTimeSpanString(itemDateLong, currentDateLong, DateUtils.MINUTE_IN_MILLIS).toString();
        String resultHourInMillis = getRelativeTimeSpanString(itemDateLong, currentDateLong, DateUtils.HOUR_IN_MILLIS).toString();
        String resultDayInMillis = getRelativeTimeSpanString(itemDateLong, currentDateLong, DateUtils.DAY_IN_MILLIS).toString();
        String resultWeekInMillis = DateTimeUtils.ToHuman(itemDate, Enumerations.DateStyle.HowLong, this);
        String resultYearInMillis = getRelativeTimeSpanString(itemDateLong, currentDateLong, DateUtils.YEAR_IN_MILLIS).toString();

        tvResultSec.setText(resultSecInMillis);
        tvResultMin.setText(resultMinInMillis);
        tvResultHour.setText(resultHourInMillis);
        tvResultDay.setText(resultDayInMillis);
        tvResultWeek.setText(resultWeekInMillis);
        tvResultYear.setText(resultYearInMillis);
    }

}