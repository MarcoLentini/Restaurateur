package com.example.restaurateur;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.restaurateur.History.History_f;
import com.example.restaurateur.Offer.Category;
import com.example.restaurateur.Offer.MyCategories;
import com.example.restaurateur.Offer.OfferModel;
import com.example.restaurateur.Offer.Offers_f;
import com.example.restaurateur.Reservation.PendingReservationsListAdapter;
import com.example.restaurateur.Reservation.ReservatedDish;
import com.example.restaurateur.Reservation.ReservationModel;
import com.example.restaurateur.Reservation.ReservationState;
import com.example.restaurateur.Reservation.Reservations_f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    public static ArrayList<ReservationModel> pendingReservationsData; // remove static from all ArrayLists
    public static ArrayList<ReservationModel> inProgressReservationsData;
    public static ArrayList<ReservationModel> finishedReservationsData;
    public static ArrayList<OfferModel> DishesOffers;

    public static HashMap<Integer, OfferModel> offersData;
    public static ArrayList<Category> categories;
    public static int[] state_offers={0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations);

        //Adding toolbar to the activity
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar_reservations);
        toolbar1.setTitle(R.string.reservation_title);
        setSupportActionBar(toolbar1);
        toolbar = getSupportActionBar();
        // Adding bottom navigation to the activity
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_categories);
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

        loadFragment(new Reservations_f());

        pendingReservationsData = new ArrayList<ReservationModel>();
        inProgressReservationsData = new ArrayList<ReservationModel>();
        finishedReservationsData = new ArrayList<ReservationModel>();
        DishesOffers=new ArrayList<OfferModel>();


        categories = new ArrayList<Category>();
        offersData = new HashMap<>();
        // fillWithStaticData() is used to put data into the previous first 3 ArrayLists and the HashMap
        fillWithStaticData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

        switch (item.getItemId()) {
            case R.id.navigation_order:
                toolbar.setTitle(R.string.reservation_title);
                fragment = new Reservations_f();
                loadFragment(fragment);
                return true;
            case R.id.navigation_offer:
                toolbar.setTitle(R.string.offers_title);
                fragment = new Offers_f();
                loadFragment(fragment);
                return true;

            case R.id.navigation_history:
                toolbar.setTitle(R.string.history_title);
                fragment = new History_f();
                loadFragment(fragment);
                return true;
        }
        return false;
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_reservations, fragment);
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
        for (int i = 0; i < MyReservationsData.id.length; i++) {
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
                    MyReservationsData.totalIncome[i]);
            switch(MyReservationsData.reservationState[i]) {
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
        Collections.sort(pendingReservationsData);
        Collections.sort(inProgressReservationsData);
        Collections.sort(finishedReservationsData);

        for(int i = 0;  i< MyCategories.categories.length;i++)
        {
            categories.add(new Category(MyCategories.categories[i]));
        }
        for(int i = 0; i < MyOffersData.id.length; i++) {
            OfferModel tmpOM = new OfferModel(MyOffersData.id[i], MyOffersData.offerName[i],MyOffersData.category[i], MyOffersData.price[i], MyOffersData.quantity[i],MyOffersData.image[i],MyOffersData.state[i]);
            offersData.put(MyOffersData.id[i],tmpOM);
            DishesOffers.add(tmpOM);

        }

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