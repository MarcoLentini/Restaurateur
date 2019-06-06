package com.example.restaurateur.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.restaurateur.Offer.MyOffersData;
import com.example.restaurateur.R;

import java.util.ArrayList;

public class TopSoldDishesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTopSoldDishes;
    private TopSoldDishesListAdapter topSoldDishesAdapter;
    private ArrayList<TopSoldDishModel> topSoldDishesData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_sold_dishes);

        String title=getString(R.string.title_top_sold_dishes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        topSoldDishesData = new ArrayList<>();
        fillWithData();

        //RecycleView Top Sold Dishes
        recyclerViewTopSoldDishes = findViewById(R.id.rvTopSoldDishes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewTopSoldDishes.setLayoutManager(layoutManager);
        // specify an Adapter
        topSoldDishesAdapter = new TopSoldDishesListAdapter(this, topSoldDishesData);
        recyclerViewTopSoldDishes.setAdapter(topSoldDishesAdapter);


    }

    //TODO lab5 fill data from firebase data
    // sort all the data with the monthlySold quantify
    public void fillWithData(){
        for (int i = 0; i < MyOffersData.id.length; i++){
            TopSoldDishModel dishModel = new TopSoldDishModel(
                    MyOffersData.offerName[i], //dish name
                    MyOffersData.quantity[i],  //dish sold quantity
                    ""                 //dish image
            );
            topSoldDishesData.add(dishModel);

        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


