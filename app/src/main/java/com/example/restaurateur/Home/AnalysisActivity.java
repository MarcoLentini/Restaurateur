package com.example.restaurateur.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.restaurateur.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AnalysisActivity extends AppCompatActivity {
    private static final int WEEK = 1;
    private static final int MONTH = 2;
    private static final int HOURS = 24;
    private static final int MINUTES = 60;
    private static final int SECONDS = 60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        String title=getString(R.string.title_analysis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        BarChart lastWeekBarChart = findViewById(R.id.lastWeekBarChart);
        BarChart lastMonthBarChart = findViewById(R.id.lastMonthBarChart);

        Bundle b = this.getIntent().getExtras();
        ArrayList<RestaurantStatistics> restaurantStatistics = b.getParcelableArrayList("stats");
        setDataAndDrawChart(lastWeekBarChart, aggregateByRangeHour(restaurantStatistics, WEEK));
        setDataAndDrawChart(lastMonthBarChart, aggregateByRangeHour(restaurantStatistics, MONTH));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private ArrayList<Integer> aggregateByRangeHour(ArrayList<RestaurantStatistics> restaurantStatistics, int filter){
        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getRawOffset() + tz.getDSTSavings();
        // 24 indices, one for each hour of day starting from 0 up to 23
        ArrayList<Integer> reservationsPerHour = new ArrayList<>();
        for(int i = 0; i < HOURS; i++)
            reservationsPerHour.add(i, 0);

        Date refDate = Timestamp.now().toDate();
        if(filter == WEEK)
            refDate = getLastWeekStart();
        else if(filter == MONTH)
            refDate = getLastMonthStart();
        for(RestaurantStatistics rs : restaurantStatistics) {
            Timestamp currentTimestamp = rs.getTimestamp();
            if(!currentTimestamp.toDate().before(refDate)) {
                int rangeHour = getRangeHourOfDay(currentTimestamp, offset);
                Integer reservations = reservationsPerHour.get(rangeHour);
                reservations++;
                reservationsPerHour.set(rangeHour, reservations);
            }
        }

        return reservationsPerHour;
    }


    // returns an Integer from 0 to 23 considering the current TimeZone(offset parameter)
    private int getRangeHourOfDay(Timestamp timestamp, int offset) {
        Date date = timestamp.toDate();
        return ((Long)(((date.getTime() + offset) % (HOURS * MINUTES * SECONDS * 1000)) / (MINUTES * SECONDS * 1000))).intValue();
    }

    private void setDataAndDrawChart(BarChart barChart, ArrayList<Integer> reservationsPerHour) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < HOURS; i = i + 2)
            entries.add(new BarEntry(i, (reservationsPerHour.get(i) + reservationsPerHour.get(i + 1))));
        BarDataSet dataSet = new BarDataSet(entries, null);
        // Dataset chart properties
        dataSet.setValueTextSize(12f);
        ValueFormatter barValueFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value > 0)
                    return String.valueOf((int)value);
                else
                    return "";
            }
        };
        dataSet.setValueFormatter(barValueFormatter);
        BarData barData = new BarData();
        barData.addDataSet(dataSet);
        barChart.setData(barData);
        // Chart properties
        barChart.setDescription(null);
        // Legend properties
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        // X axis Properties
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(14f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE); // Set the xAxis position to bottom. Default is top
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setLabelCount(12);
        ValueFormatter xAxisFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value > 9)
                    return String.valueOf((int)value);
                else
                    return "0" + ((int)value);
            }
        };
        xAxis.setValueFormatter(xAxisFormatter);
        // Controlling left side of y axis
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        // Controlling right side of y axis //
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        barChart.invalidate();
    }

    private Date getLastWeekStart() {
        Calendar c = Calendar.getInstance();
        // set the calendar to start of 7th day ago
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, -7);

        return c.getTime();
    }

    private Date getLastMonthStart() {
        Calendar c = Calendar.getInstance();
        // set the calendar to start of 30th day ago
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, -30);

        return c.getTime();
    }


}
