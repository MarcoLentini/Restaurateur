package com.example.restaurateur;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.restaurateur.History.HistoryMainFragment;
import com.example.restaurateur.Information.FirebaseAccount;
import com.example.restaurateur.Information.UserInformationActivity;
import com.example.restaurateur.Offer.Category;
import com.example.restaurateur.Offer.OfferModel;
import com.example.restaurateur.Offer.OffersCategoryFragment;
import com.example.restaurateur.Reservation.ReservatedDish;
import com.example.restaurateur.Reservation.ReservationModel;
import com.example.restaurateur.Reservation.ReservationState;
import com.example.restaurateur.Reservation.ReservationsMainFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String restaurantDataFile = "RestaurantDataFile";

    private ActionBar toolbar;
    public static ArrayList<ReservationModel> pendingReservationsData;
    public static ArrayList<ReservationModel> inProgressReservationsData;
    public static ArrayList<ReservationModel> finishedReservationsData;
    //public static HashMap<String, OfferModel> offersData;
    public static ArrayList<Category> categoriesData;
    public static int idDishes = 26; //TODO idDishes and image_id for dishes are to be removed
    public static int[] availableImageId = {R.drawable.ic_offer_pizza, R.drawable.ic_offer_cake, R.drawable.ic_offer_coffee, R.drawable.ic_offer_fries};
    public FirebaseAuth auth;
    public FirebaseFirestore db;
    public String restaurantKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        SharedPreferences sharedPref = getSharedPreferences(restaurantDataFile, Context.MODE_PRIVATE);
        restaurantKey = sharedPref.getString("restaurantKey","");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null || restaurantKey.equals("")) {
            finish();
        }

        //Get Firestore instance
        db = FirebaseFirestore.getInstance();

        //Adding TOOLBAR to the activity
        Toolbar toolbarMain = findViewById(R.id.toolbar_main);
        toolbarMain.setTitle(R.string.reservation_title);
        setSupportActionBar(toolbarMain);

        toolbar = getSupportActionBar();
        // Adding BOTTOM NAVIGATION to the activity
        BottomNavigationView navigation = findViewById(R.id.navigation_categories);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new ReservationsMainFragment());

        pendingReservationsData = new ArrayList<>();
        inProgressReservationsData = new ArrayList<>();
        finishedReservationsData = new ArrayList<>();
        categoriesData = new ArrayList<>();
        // offersData = new HashMap<>();
        // fillWithData() is used to put data into the previous ArrayLists and the HashMap
        fillWithData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings_firebase) {

            Intent information = new Intent(this, FirebaseAccount.class);
            startActivity(information);
        }
        if (id == R.id.action_settings) {

            Intent information = new Intent(this, UserInformationActivity.class);
            startActivity(information);
        }
        if(id == android.R.id.home){
            onBackPressed();
            //getSupportFragmentManager().popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }
    // Bottom Menu
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment;

        toolbar.setDisplayHomeAsUpEnabled(false);
        toolbar.setDisplayShowHomeEnabled(false);
        switch (item.getItemId()) {
            case R.id.navigation_order:
                toolbar.setTitle(R.string.reservation_title);
                fragment = new ReservationsMainFragment();
                loadFragment(fragment);
                return true;
            case R.id.navigation_offer:
                toolbar.setTitle(R.string.offers_title);
                fragment = new OffersCategoryFragment();
                loadFragment(fragment);
                return true;

            case R.id.navigation_history:
                toolbar.setTitle(R.string.history_title);
                fragment = new HistoryMainFragment();
                loadFragment(fragment);
                return true;
        }

        return false;
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_main, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            super.onBackPressed();
        }
    }

    private void fillWithData() {

        db.collection("reservations").whereEqualTo("rest_id", restaurantKey).addSnapshotListener((EventListener<QuerySnapshot>) (document, e) -> {

                    if (e != null)
                        return;

                    for(DocumentChange dc : document.getDocumentChanges())
                        if(dc.getType() == DocumentChange.Type.ADDED)
                        {//TODO incremento
                             }
                    if (!document.isEmpty()) {
                        for (DocumentSnapshot doc : document) {
                            ArrayList<ReservatedDish> tmpArrayList = new ArrayList<>();
                            for (HashMap<String, Object> dish : (ArrayList<HashMap<String, Object>>) doc.get("dishes")) {
                                tmpArrayList.add(new ReservatedDish(
                                        (String) dish.get("dish_name"),
                                        (Double) dish.get("dish_price"),
                                        (Long) dish.get("dish_qty")));
                            }

                            ReservationModel tmpReservationModel = new ReservationModel((Long) doc.get("rs_id"),
                                    (String) doc.get("cust_id"),
                                    (Timestamp) doc.get("upload_time"),
                                    (String) doc.get("notes"),
                                    (String) doc.get("cust_phone"),
                                    tmpArrayList,
                                    (String) doc.get("rs_status"),
                                    (Double) doc.get("total_income"));

                            switch ((String) doc.get("rs_status")) {
                                case ReservationState.STATE_PENDING:
                                    pendingReservationsData.add(tmpReservationModel);
                                    break;
                                case ReservationState.STATE_IN_PROGRESS:
                                    inProgressReservationsData.add(tmpReservationModel);
                                    break;
                                case ReservationState.STATE_FINISHED_SUCCESS:
                                    finishedReservationsData.add(tmpReservationModel);
                                    break;
                                default:
                                    finishedReservationsData.add(tmpReservationModel);
                                    break;
                            }
                        }
                        Collections.sort(pendingReservationsData);
                        Collections.sort(inProgressReservationsData);
                        Collections.sort(finishedReservationsData);
                    } else {
                        Log.d("QueryReservation", "No such document");
                    }

                });

        db.collection("category").whereEqualTo("rest_id", restaurantKey).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if (!document.isEmpty()) {
                            for(DocumentSnapshot doc : document){
                                Category c = new Category(
                                        (String) doc.get("category_name"),
                                        (String) doc.getId());
                                        //(String) doc.get("category_image_url"));
                                doc.getReference().collection("dishes").get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        QuerySnapshot document1 = task1.getResult();
                                        if (!document1.isEmpty()) {
                                            for(DocumentSnapshot doc1 : document1){
                                                OfferModel tmpOM = new OfferModel(
                                                                 doc1.getId(),
                                                        (String) doc1.get("name"),
                                                        (String) doc.get("category"),
                                                        (Double) doc1.get("price"),
                                                        (Long) doc1.get("quantity"),
                                                        (String) doc1.get("image"),
                                                        (String) doc1.get("description"),
                                                        (Boolean) doc1.get("state"));
                                                //offersData.put(doc1.getId(), tmpOM);
                                                c.getDishes().add(tmpOM);
                                            }
                                        } else {
                                            Log.d("QueryReservation", "No such document");
                                        }
                                        categoriesData.add(c);
                                    } else {
                                        Log.d("QueryReservation", "get failed with ", task.getException());
                                    }
                                });
                            }
                        } else {
                            Log.d("QueryReservation", "No such document");
                        }
                    } else {
                        Log.d("QueryReservation", "get failed with ", task.getException());
                    }
                });

    }

    public void addItemToPending(ReservationModel rm) {
        pendingReservationsData.add(rm);
    }

    public void removeItemFromPending(int position) {
        pendingReservationsData.remove(position);
    }

    public void addItemToInProgress(ReservationModel rm) {
        inProgressReservationsData.add(rm);
    }

    public void removeItemFromInProgress(int position) {
        inProgressReservationsData.remove(position);
    }

    public void addItemToFinished(ReservationModel rm) {
        finishedReservationsData.add(rm);
    }

    public void removeItemFromFinished(int position) {
        pendingReservationsData.remove(position);
    }
}