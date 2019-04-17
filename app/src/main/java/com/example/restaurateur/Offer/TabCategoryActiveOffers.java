package com.example.restaurateur.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurateur.R;
import com.example.restaurateur.Reservation.Reservations_f;
import com.example.restaurateur.Reservations;
import com.example.restaurateur.UserInformationActivity;


public class TabCategoryActiveOffers extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter categoriesAdapter;
    private Reservations reservationsActivity = (Reservations) getActivity();
    private String type;


    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type=getArguments().getString("Type");
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view=inflater.inflate(R.layout.fragment_tab_active_offers, container, false);
        recyclerView = view.findViewById(R.id.ActiveOfferRecyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*recyclerView.setHasFixedSize(true);*/
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        categoriesAdapter = new CategoriesListAdapter(getContext(), reservationsActivity.categories,reservationsActivity,type); // getContext() forse non va bene
        recyclerView.setAdapter(categoriesAdapter);

        return view;}




}
