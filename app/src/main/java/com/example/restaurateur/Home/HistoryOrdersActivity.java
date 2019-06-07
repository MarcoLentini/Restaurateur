package com.example.restaurateur.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.restaurateur.Information.LoginActivity;
import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;
import com.example.restaurateur.Reservation.ReservatedDish;
import com.example.restaurateur.Reservation.ReservationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HistoryOrdersActivity extends AppCompatActivity {
    private static final String TAG = "HistoryOrdersActivity";
    public static ArrayList<ReservationModel> historyOrdersData;
    private HistoryOrdersListAdapter historyOrdersListAdapter;
    public FirebaseAuth auth;
    public FirebaseFirestore db;
    public String restaurantKey;
    private static final String restaurantDataFile = "RestaurantDataFile";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_reservations);
        Log.d(TAG, "onCreate called.");

        String title = getString(R.string.title_history_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        SharedPreferences sharedPref = getSharedPreferences(restaurantDataFile, Context.MODE_PRIVATE);
        restaurantKey = sharedPref.getString("restaurantKey","");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null || restaurantKey.equals("")) {
            startActivity(new Intent(HistoryOrdersActivity.this, LoginActivity.class));
            finish();
        }

        //Get Firestore instance
        db = FirebaseFirestore.getInstance();
        historyOrdersData = new ArrayList<>();
        fillWithData();

        RecyclerView recyclerView = findViewById(R.id.reservationsRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        historyOrdersListAdapter = new HistoryOrdersListAdapter(this,historyOrdersData);
        recyclerView.setAdapter(historyOrdersListAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void fillWithData(){
        Query request = db.collection("reservations").whereEqualTo("rest_id", restaurantKey);

        request.whereEqualTo("rs_status", "DELIVERED").addSnapshotListener((EventListener<QuerySnapshot>) (document, e) -> {
            if (e != null) return;
            for(DocumentChange dc : document.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    ArrayList<ReservatedDish> tmpArrayList = new ArrayList<>();
                    if(dc.getDocument().get("dishes") != null) {
                        for (HashMap<String, Object> dish : (ArrayList<HashMap<String, Object>>) dc.getDocument().get("dishes")) {
                            tmpArrayList.add(new ReservatedDish(
                                    (String) dish.get("dish_name"),
                                    (Double) dish.get("dish_price"),
                                    (Long) dish.get("dish_qty")));
                        }
                        ReservationModel tmpReservationModel = new ReservationModel(
                                dc.getDocument().getId(),
                                dc.getDocument().getLong("rs_id") ,
                                dc.getDocument().getString("cust_id"),
                                dc.getDocument().getTimestamp("delivery_time"),
                                dc.getDocument().getString("notes"),
                                dc.getDocument().getString("cust_phone"),
                                dc.getDocument().getString("cust_name"),
                                tmpArrayList,
                                dc.getDocument().getString("rs_status"),
                                dc.getDocument().getDouble("total_income"),
                                dc.getDocument().getString("rest_address")
                        );
                        historyOrdersData.add(tmpReservationModel);
                    }
                    Collections.sort(historyOrdersData);
                    historyOrdersListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
