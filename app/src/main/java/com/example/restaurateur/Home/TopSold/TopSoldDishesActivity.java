package com.example.restaurateur.Home.TopSold;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.restaurateur.Home.RestaurantStatistics;
import com.example.restaurateur.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TopSoldDishesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTopSoldDishes;
    private TopSoldDishesListAdapter topSoldDishesAdapter;
    private ArrayList<TopSoldDishModel> topSoldDishes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_sold_dishes);



        String title=getString(R.string.title_top_sold_dishes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        Bundle b = this.getIntent().getExtras();
        ArrayList<RestaurantStatistics> restaurantStatistics = b.getParcelableArrayList("stats");

        topSoldDishes = aggregateByName(restaurantStatistics);

        //fillWithData();

        //RecycleView Top Sold Dishes
        recyclerViewTopSoldDishes = findViewById(R.id.rvTopSoldDishes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewTopSoldDishes.setLayoutManager(layoutManager);
        // specify an Adapter
        topSoldDishesAdapter = new TopSoldDishesListAdapter(this, topSoldDishes);
        recyclerViewTopSoldDishes.setAdapter(topSoldDishesAdapter);


    }

    private ArrayList<TopSoldDishModel> aggregateByName(ArrayList<RestaurantStatistics> restaurantStatistics){
        HashMap<String, TopSoldDishModel> dailyDishTop = new HashMap<>();
        for(RestaurantStatistics rs : restaurantStatistics){
            TopSoldDishModel tsdm;
            if(dailyDishTop.containsKey(rs.getHash()))
                tsdm = dailyDishTop.get(rs.getHash());
            else {
                tsdm = new TopSoldDishModel(rs.getDishName(), new Long(0), null);
                dailyDishTop.put(rs.getHash(), tsdm);
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


