package com.example.restaurateur.Reservation;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentStatePagerAdapter;public class PageReservations extends FragmentStatePagerAdapter {    int tabCount; //integer to count number of tabs    private TabReservationsPending tabReservationsPending;    private TabReservationsInProgress tabReservationsInProgress;    private TabReservationsFinished tabReservationsFinished;    //Constructor of the class    public PageReservations(FragmentManager fm, int tabCount) {        super(fm);        this.tabCount = tabCount;        tabReservationsPending = new TabReservationsPending();        tabReservationsInProgress = new TabReservationsInProgress();        tabReservationsFinished = new TabReservationsFinished();    }    //Overriding method getItem    @Override    public Fragment getItem(int position) {        //Returning the current tabs        switch (position) {            case 0://                tabReservationsPending = new TabReservationsPending();                return tabReservationsPending;            case 1://                 tabReservationsInProgress = new TabReservationsInProgress();                return tabReservationsInProgress;            case 2://                 tabReservationsFinished = new TabReservationsFinished();                return tabReservationsFinished;            default:                return null;        }    }    @Override    public int getCount() {        return tabCount;    }    public TabReservationsPending getTabPending() {        return tabReservationsPending;    }    public TabReservationsInProgress getTabInProgress() {        return tabReservationsInProgress;    }    public TabReservationsFinished getTabFinished() {        return tabReservationsFinished;    }}