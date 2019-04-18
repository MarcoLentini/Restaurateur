package com.example.restaurateur.Offer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

public class TabDishesActiveOffers extends android.support.v4.app.Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter ActiveDishesAdapter;
    private MainActivity reservationsActivity = (MainActivity) getActivity();
    private String type;

    private FloatingActionButton FabActive;
    private FloatingActionButton FabDisabled;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_active_dishes_offers, container, false);
        TextView a=view.findViewById(R.id.textViewDishesOffers);
        a.setText("DishesActiveOffers");
        FabActive= ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddActiveCategories);
        FabDisabled= ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddDisabledCategories);
        FabActive.hide();
        FabDisabled.hide();
        reservationsActivity=((MainActivity)view.getContext());
        reservationsActivity.state_offers[0]=1;

        type=getArguments().getString("Type");
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        recyclerView = view.findViewById(R.id.ActiveDishesRecyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*recyclerView.setHasFixedSize(true);*/
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        ActiveDishesAdapter = new DishesListAdapter(getContext(), reservationsActivity.ActiveDishes,reservationsActivity); // getContext() forse non va bene
        recyclerView.setAdapter(ActiveDishesAdapter);


        return view;

    }

    @Override
    public void onPause() {

        super.onPause();
        FabActive.show();
        FabDisabled.show();

    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        FabActive.show();
        FabDisabled.show();
        reservationsActivity.state_offers[0]=0;
        reservationsActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}