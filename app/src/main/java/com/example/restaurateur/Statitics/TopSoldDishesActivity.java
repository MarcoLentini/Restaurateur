package com.example.restaurateur.Statitics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.restaurateur.Offer.MyOffersData;
import com.example.restaurateur.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class TopSoldDishesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTopSoldDishes;
    private TopSoldDishesListAdapter topSoldDishesAdapter;
    private ArrayList<RestaurantStatistics> topSoldDishesData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_sold_dishes);



        String title=getString(R.string.title_top_sold_dishes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        Bundle b = this.getIntent().getExtras();
        topSoldDishesData = b.getParcelableArrayList("stats");

        //fillWithData();

        //RecycleView Top Sold Dishes
        recyclerViewTopSoldDishes = findViewById(R.id.rvTopSoldDishes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewTopSoldDishes.setLayoutManager(layoutManager);
        // specify an Adapter
        topSoldDishesAdapter = new TopSoldDishesListAdapter(this, topSoldDishesData);
        recyclerViewTopSoldDishes.setAdapter(topSoldDishesAdapter);


    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


