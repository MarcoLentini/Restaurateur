package com.example.restaurateur;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.restaurateur.History.History_f;
import com.example.restaurateur.Offer.OfferModel;
import com.example.restaurateur.Offer.Offers_f;
import com.example.restaurateur.Reservation.ReservatedDish;
import com.example.restaurateur.Reservation.ReservationModel;
import com.example.restaurateur.Reservation.ReservationState;
import com.example.restaurateur.Reservation.Reservations_f;

import java.util.ArrayList;
import java.util.HashMap;

public class Reservations extends AppCompatActivity {

    private ActionBar toolbar;
    public static ArrayList<ReservationModel> pendingReservationsData;
    public static ArrayList<ReservationModel> inProgressReservationsData;
    public static ArrayList<ReservationModel> finishedReservationsData;
    public static HashMap<Integer, OfferModel> offersData;
    public static ArrayList<Category> categories;

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

        loadFragment(new Reservations_f());

        pendingReservationsData = new ArrayList<ReservationModel>();
        inProgressReservationsData = new ArrayList<ReservationModel>();
        finishedReservationsData = new ArrayList<ReservationModel>();
        categories = new ArrayList<Category>();
        // The for cycle is used to put data into the previous 3 ArrayLists
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
                    MyReservationsData.reservationState[i]);
            switch(MyReservationsData.reservationState[i]) {
                    case ReservationState.STATE_PENDING:
                        pendingReservationsData.add(tmpReservationModel);
                        break;
                    case ReservationState.STATE_IN_PROGRESS:
                        inProgressReservationsData.add(tmpReservationModel);
                        break;
                    case ReservationState.STATE_FINISHED:
                        finishedReservationsData.add(tmpReservationModel);
                        break;
            }
        }
        for(int j =0; j< MyCategories.categories.length;j++)
        {
            categories.add(new Category(MyCategories.categories[j]));

        }
        offersData = new HashMap<>();
        // fillWithStaticData() is used to put data into the previous 3 ArrayLists and the HashMap
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
                    MyReservationsData.reservationState[i]);
            switch(MyReservationsData.reservationState[i]) {
                case ReservationState.STATE_PENDING:
                    pendingReservationsData.add(tmpReservationModel);
                    break;
                case ReservationState.STATE_IN_PROGRESS:
                    inProgressReservationsData.add(tmpReservationModel);
                    break;
                case ReservationState.STATE_FINISHED:
                    finishedReservationsData.add(tmpReservationModel);
                    break;
            }
        }

        for(int i = 0; i < MyOffersData.id.length; i++) {
            OfferModel tmpOM = new OfferModel(MyOffersData.id[i], MyOffersData.offerName[i], MyOffersData.price[i], MyOffersData.image[i]);
            offersData.put(MyOffersData.id[i], tmpOM);
        }
    }
}
