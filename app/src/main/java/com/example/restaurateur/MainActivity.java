package com.example.restaurateur;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.restaurateur.Helper.Firebase;
import com.example.restaurateur.History.HistoryMainFragment;
import com.example.restaurateur.Information.FirebaseAccount;
import com.example.restaurateur.Information.LoginActivity;
import com.example.restaurateur.Information.UserInformationActivity;
import com.example.restaurateur.Offer.Category;
import com.example.restaurateur.Offer.MyCategories;
import com.example.restaurateur.Offer.MyOffersData;
import com.example.restaurateur.Offer.OfferModel;
import com.example.restaurateur.Offer.OffersCategoryFragment;
import com.example.restaurateur.Reservation.MyReservationsData;
import com.example.restaurateur.Reservation.ReservatedDish;
import com.example.restaurateur.Reservation.ReservationModel;
import com.example.restaurateur.Reservation.ReservationState;
import com.example.restaurateur.Reservation.ReservationsMainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    public static ArrayList<ReservationModel> pendingReservationsData; // remove static from all ArrayLists
    public static ArrayList<ReservationModel> inProgressReservationsData;
    public static ArrayList<ReservationModel> finishedReservationsData;
    public static HashMap<String, OfferModel> offersData;
    public static ArrayList<Category> categoriesData;
    public static int idDishes = 26; //TODO idDishes and image_id for dishes are to be removed
    //TODO:per scegliere id image a caso tra quelli dati (da togliere percò non so come recuperare l'immagine dei dishes immassa
    public static int[] availableImageId = {R.drawable.ic_offer_pizza, R.drawable.ic_offer_cake, R.drawable.ic_offer_coffee, R.drawable.ic_offer_fries};
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        //Get Firestore instance
        db = FirebaseFirestore.getInstance();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }

        //Adding TOOLBAR to the activity
        Toolbar toolbarMain = findViewById(R.id.toolbar_main);
        toolbarMain.setTitle(R.string.reservation_title);
        setSupportActionBar(toolbarMain);

        toolbar = getSupportActionBar();
        // Adding BOTTOM NAVIGATION to the activity
        BottomNavigationView navigation = findViewById(R.id.navigation_categories);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                } else {
//                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                }
//            }
//        });

        loadFragment(new ReservationsMainFragment());

        pendingReservationsData = new ArrayList<>();
        inProgressReservationsData = new ArrayList<>();
        finishedReservationsData = new ArrayList<>();
        categoriesData = new ArrayList<>();
        offersData = new HashMap<>();
        // fillWithStaticData() is used to put data into the previous ArrayLists and the HashMap
        fillWithStaticData();
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
        if (id == R.id.upload) {

            Intent information = new Intent(this, Firebase.class);
            startActivity(information);
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

    private void fillWithStaticData() {

        String restaurant_name = "Luigi's";
        db.collection("reservations").whereEqualTo("rest_name", restaurant_name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if (!document.isEmpty()) {
                            for(DocumentSnapshot doc : document){
                                ArrayList<ReservatedDish> tmpArrayList = new ArrayList<>();
                                for(HashMap<String,Object> dish : (ArrayList<HashMap<String,Object>>) doc.get("dishes")){
                                    tmpArrayList.add(new ReservatedDish(
                                            (String) dish.get("dish_name"),
                                            (Long) dish.get("dish_qty"),
                                            (Double) dish.get("dish_price")));
                                }

                                ReservationModel tmpReservationModel = new ReservationModel((Long) doc.get("rs_id"),
                                        (String) doc.get("cust_id"),
                                        (Timestamp) doc.get("desidered_hour"),
                                        (String) doc.get("notes"),
                                        (String) doc.get("cust_phone"),
                                        tmpArrayList,
                                        (String) doc.get("rs_status"),
                                        (Double) doc.get("total_income"));

                                switch((String) doc.get("rs_status")) {
                                    case ReservationState.STATE_PENDING:
                                        pendingReservationsData.add(tmpReservationModel);
                                        break;
                                    case ReservationState.STATE_IN_PROGRESS:
                                        inProgressReservationsData.add(tmpReservationModel);
                                        break;
                                    case ReservationState.STATE_FINISHED_SUCCESS:
                                        finishedReservationsData.add(tmpReservationModel);
                                        break;
                                    default: finishedReservationsData.add(tmpReservationModel);
                                        break;
                                }
                            }
                        } else {
                            Log.d("QueryReservation", "No such document");
                        }
                    } else {
                        Log.d("QueryReservation", "get failed with ", task.getException());
                    }
                });

        /*for (int i = 0; i < MyReservationsData.id.length; i++) {
            ArrayList<ReservatedDish> tmpArrayList = new ArrayList<>();
            for(int j = 0; j < MyReservationsData.orderedDish[i].length; j++)
                tmpArrayList.add(new ReservatedDish(MyReservationsData.orderedDish[i][j], MyReservationsData.multiplierDish[i][j]));
            ReservationModel tmpReservationModel = new ReservationModel(MyReservationsData.id[i],
                    MyReservationsData.customerId[i],
                    MyReservationsData.remainingMinutes[i],
                    MyReservationsData.notes[i],
                    MyReservationsData.customerPhoneNumber[i],
                    tmpArrayList,
                    MyReservationsData.reservationState[i],
                    MyReservationsData.totalIncome[i]);*/

            /*switch(MyReservationsData.reservationState[i]) {
                case ReservationState.STATE_PENDING:
                    pendingReservationsData.add(tmpReservationModel);
                    break;
                case ReservationState.STATE_IN_PROGRESS:
                    inProgressReservationsData.add(tmpReservationModel);
                    break;
                case ReservationState.STATE_FINISHED_SUCCESS:
                    finishedReservationsData.add(tmpReservationModel);
                    break;
                default: finishedReservationsData.add(tmpReservationModel);
                    break;
            }
           }*/

        Collections.sort(pendingReservationsData);
        Collections.sort(inProgressReservationsData);
        Collections.sort(finishedReservationsData);

        String restaurant_id = "";
        db.collection("menus").whereEqualTo("rest_id", restaurant_id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if (!document.isEmpty()) {
                            for(DocumentSnapshot doc : document){
                                categoriesData.add(new Category(
                                        (String) doc.get("menu_name"),
                                        (String) doc.get("image_url"),
                                        (String) doc.get("state")));
                                doc.getReference().collection("dishes").get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        QuerySnapshot document1 = task1.getResult();
                                        if (!document1.isEmpty()) {
                                            for(DocumentSnapshot doc1 : document1){
                                                OfferModel tmpOM = new OfferModel(
                                                                 doc1.getId(),
                                                        (String) doc1.get("dish_name"),
                                                        (String) doc.get("menu_name"),
                                                        (Double) doc1.get("dish_cost"),
                                                        (Long) doc1.get("dish_qty"),
                                                        (String) doc1.get("image_url"),
                                                        (String) doc1.get("state"),
                                                        (String) doc1.get("dish_descr"));
                                                offersData.put(doc1.getId(), tmpOM);
                                            }
                                        } else {
                                            Log.d("QueryReservation", "No such document");
                                        }
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

/*        for(int i = 0; i< MyCategories.categories.length; i++)
        {
            categoriesData.add(new Category(MyCategories.categories[i]));
        }*/

   /*     for(int i = 0; i < MyOffersData.id.length; i++) {
            OfferModel tmpOM = new OfferModel(MyOffersData.id[i], MyOffersData.offerName[i],MyOffersData.category[i], MyOffersData.price[i], MyOffersData.quantity[i],MyOffersData.image[i],MyOffersData.state[i],MyOffersData.description[i]);
            offersData.put(MyOffersData.id[i], tmpOM);
        }*/
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