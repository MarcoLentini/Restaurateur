package com.example.restaurateur.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.restaurateur.Home.TopSold.TopSoldDishModel;
import com.example.restaurateur.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalysisActivity extends AppCompatActivity {

    private ArrayList<TopSoldDishModel> dishesByHour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        String title=getString(R.string.title_analysis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        Bundle b = this.getIntent().getExtras();
        ArrayList<RestaurantStatistics> restaurantStatistics = b.getParcelableArrayList("stats");

        dishesByHour = aggregateByHour(restaurantStatistics);

        // TODO - doing chart
    }

    private ArrayList<TopSoldDishModel> aggregateByHour(ArrayList<RestaurantStatistics> restaurantStatistics){
        HashMap<Integer, TopSoldDishModel> dailyDishTop = new HashMap<>();
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
        }

        return new ArrayList<>(dailyDishTop.values());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
