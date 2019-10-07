package com.example.restaurateur.Reservation;import android.support.annotation.NonNull;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentStatePagerAdapter;import android.util.Log;public class PageReservations extends FragmentStatePagerAdapter {    int tabCount; //integer to count number of tabs    private TabReservationsPending tabReservationsPending;    private TabReservationsInProgress tabReservationsInProgress;    private TabReservationsFinished tabReservationsFinished;    //Constructor of the class    public PageReservations(FragmentManager fm, int tabCount) {        super(fm);        this.tabCount = tabCount;        tabReservationsPending = new TabReservationsPending();        tabReservationsInProgress = new TabReservationsInProgress();        tabReservationsFinished = new TabReservationsFinished();    }    //Overriding method getItem    @Override    public Fragment getItem(int position) {        //Returning the current tabs        switch (position) {            case 0://                tabReservationsPending = new TabReservationsPending();                return tabReservationsPending;            case 1://                 tabReservationsInProgress = new TabReservationsInProgress();                return tabReservationsInProgress;            case 2://                 tabReservationsFinished = new TabReservationsFinished();                return tabReservationsFinished;            default:                return null;        }    }    @Override    public int getCount() {        return tabCount;    }    @Override    public int getItemPosition(@NonNull Object object) {        Log.d("PR", "getItemPosition() chiamato");        if(object instanceof TabReservationsPending) {            TabReservationsPending f = (TabReservationsPending) object;            if(f != null) {                f.update();                Log.d("PR", "getItemPosition() con pending update() chiamato");            }        }        if(object instanceof TabReservationsInProgress) {            TabReservationsInProgress f = (TabReservationsInProgress) object;            if(f != null) {                f.update();                Log.d("PR", "getItemPosition() con inprogrss update() chiamato");            }        }        if(object instanceof TabReservationsFinished) {            TabReservationsFinished f = (TabReservationsFinished) object;            if(f != null) {                f.update();                Log.d("PR", "getItemPosition() con finished update() chiamato");            }        }        return super.getItemPosition(object);    }    public TabReservationsPending getTabPending() {        return tabReservationsPending;    }    public TabReservationsInProgress getTabInProgress() {        return tabReservationsInProgress;    }    public TabReservationsFinished getTabFinished() {        return tabReservationsFinished;    }}