package com.example.restaurateur;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.restaurateur.Home.HomeMainFragment;
import com.example.restaurateur.Information.LoginActivity;
import com.example.restaurateur.Offer.Category;
import com.example.restaurateur.Offer.OfferModel;
import com.example.restaurateur.Offer.OffersCategoryFragment;
import com.example.restaurateur.Reservation.ReservatedDish;
import com.example.restaurateur.Reservation.ReservationModel;
import com.example.restaurateur.Reservation.ReservationState;
import com.example.restaurateur.Reservation.ReservationsMainFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 100;
    private static final String restaurantDataFile = "RestaurantDataFile";

    private ActionBar toolbar;
    public static ArrayList<ReservationModel> pendingReservationsData;
    public static ArrayList<ReservationModel> inProgressReservationsData;
    public static ArrayList<ReservationModel> finishedReservationsData;
    public static ArrayList<Category> categoriesData;
    public FirebaseAuth auth;
    public FirebaseFirestore db;
    public String restaurantKey;

    private ReservationsMainFragment reservationsMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        SharedPreferences sharedPref = getSharedPreferences(restaurantDataFile, Context.MODE_PRIVATE);
        restaurantKey = sharedPref.getString("restaurantKey","");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null || restaurantKey.equals("")) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

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

        reservationsMainFragment = new ReservationsMainFragment();
        loadFragment(reservationsMainFragment);

        pendingReservationsData = new ArrayList<>();
        inProgressReservationsData = new ArrayList<>();
        finishedReservationsData = new ArrayList<>();
        categoriesData = new ArrayList<>();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
                toolbar.setTitle(R.string.home_title);
                fragment = new HomeMainFragment();
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

    private void getDataFromDoc(QuerySnapshot document){
        if (!document.isEmpty()) {
            for (DocumentSnapshot doc : document) {
                ArrayList<ReservatedDish> tmpArrayList = new ArrayList<>();
                if (doc.get("dishes") != null) {
                    for (HashMap<String, Object> dish : (ArrayList<HashMap<String, Object>>) doc.get("dishes")) {
                        tmpArrayList.add(new ReservatedDish(
                                (String) dish.get("dish_name"),
                                (Double) dish.get("dish_price"),
                                (Long) dish.get("dish_qty")));
                    }
                }
                ReservationModel tmpReservationModel = new ReservationModel(
                        doc.getId(),
                         doc.getLong("rs_id"),
                         doc.getString("cust_id"),
                         doc.getTimestamp("delivery_time"),
                         doc.getString("notes"),
                         doc.getString("cust_phone"),
                         doc.getString("cust_name"),
                        tmpArrayList,
                         doc.getString("rs_status"),
                         doc.getDouble("total_income"),
                         doc.getString("rest_address"));

                switch ( doc.getString("rs_status")) {
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
            reservationsMainFragment.pageAdapter.notifyDataSetChanged();
        } else {
            Log.d("QueryReservation", "No such document");
        }
    }

    private void fillWithData() {
        Query request = db.collection("reservations").whereEqualTo("rest_id", restaurantKey);

        request.whereEqualTo("rs_status", "PENDING").addSnapshotListener((EventListener<QuerySnapshot>) (document, e) -> {
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
                        pendingReservationsData.add(tmpReservationModel);
                    }
                    Collections.sort(pendingReservationsData);
                    reservationsMainFragment.incrementPendingReservationsNumber();
                    reservationsMainFragment.pageAdapter.notifyDataSetChanged();
                }
            }
        });

        request.whereEqualTo("rs_status", ReservationState.STATE_IN_PROGRESS).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                QuerySnapshot document = task.getResult();
                getDataFromDoc(document);
            }
        });
        request.whereEqualTo("rs_status", ReservationState.STATE_FINISHED_SUCCESS).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                QuerySnapshot document = task.getResult();
                getDataFromDoc(document);
            }
        });

        db.collection("category").whereEqualTo("rest_id", restaurantKey).get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    if (!document.isEmpty()) {
                        for(DocumentSnapshot doc : document){
                            Category c = new Category(
                                     doc.getString("category_name"),
                                    (String) doc.getId(),
                                     doc.getLong("category_position"));
                            //(String) doc.get("category_image_url"));
                            doc.getReference().collection("dishes").get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    QuerySnapshot document1 = task1.getResult();
                                    if (!document1.isEmpty()) {
                                        for(DocumentSnapshot doc1 : document1){
                                            OfferModel tmpOM = new OfferModel(
                                                    doc1.getId(),
                                                     doc1.getString("name"),
                                                     doc1.getString("category"),
                                                     doc1.getDouble("price"),
                                                     doc1.getLong("quantity"),
                                                     doc1.getString("image"),
                                                     doc1.getString("description"),
                                                     doc1.getBoolean("state"));
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
        Collections.sort(categoriesData);

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
        finishedReservationsData.remove(position);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //If the permission has been granted...//
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return;

        } else {
            //If the user denies the permission request, then display a snackBar with some more information//
            Snackbar.make(findViewById(R.id.frame_container_main), "Please enable location services to allow GPS tracking",
                    Snackbar.LENGTH_LONG).show();
        }
    }
}