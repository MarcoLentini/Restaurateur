package com.example.restaurateur.Home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.restaurateur.Home.TopSold.TopSoldDishModel;
import com.example.restaurateur.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AnalysisActivity extends AppCompatActivity {
    private static final int HOURS = 24;
    private static final int MINUTES = 60;
    private static final int SECONDS = 60;

    private BarChart barChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        String title=getString(R.string.title_analysis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        barChart = findViewById(R.id.perHourChart);

        Bundle b = this.getIntent().getExtras();
        ArrayList<RestaurantStatistics> restaurantStatistics = b.getParcelableArrayList("stats");
        setDataAndDrawChart(restaurantStatistics);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private ArrayList<Integer> aggregateByRangeHour(ArrayList<RestaurantStatistics> restaurantStatistics){
        // 24 indices, one for each hour of day starting from 0 up to 23
        ArrayList<Integer> reservationsPerHour = new ArrayList<>();
        for(int i = 0; i < HOURS; i++)
            reservationsPerHour.add(i, 0);
        for(RestaurantStatistics rs : restaurantStatistics) {
            int rangeHour = getRangeHourOfDay(rs.getTimestamp());
            Integer reservations = reservationsPerHour.get(rangeHour);
            reservations++;
            reservationsPerHour.set(rangeHour, reservations);
        }

        return reservationsPerHour;
        /*HashMap<Integer, TopSoldDishModel> dailyDishTop = new HashMap<>();
        for(RestaurantStatistics rs : restaurantStatistics){
            int hour = rs.getTimestamp().toDate().getHours();
            TopSoldDishModel tsdm;
            if(dailyDishTop.containsKey(hour))
                tsdm = dailyDishTop.get(hour);
            else {
                tsdm = new TopSoldDishModel(rs.getDishName(), new Long(0), null);
                dailyDishTop.put(hour, tsdm);
            }
            tsdm.setMonthlySoldQuantity(tsdm.getMonthlySoldQuantity() + rs.getQty());
        }*/

        //return new ArrayList<>(dailyDishTop.values());
    }


    // returns an Integere from 0 to 23
    private int getRangeHourOfDay(Timestamp timestamp) {
        Date date = timestamp.toDate();

        return ((Long)((date.getTime() % (HOURS * MINUTES * SECONDS * 1000)) / (MINUTES * SECONDS * 1000))).intValue();
    }

    private void setDataAndDrawChart(ArrayList<RestaurantStatistics> restaurantStatistics) {
        ArrayList<Integer> reservationsPerHour = aggregateByRangeHour(restaurantStatistics);
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < HOURS; i++)
            entries.add(new BarEntry(i, reservationsPerHour.get(i).floatValue()));
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
                    return "0" + String.valueOf((int)value);
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
}
