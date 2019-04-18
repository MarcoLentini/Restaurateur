package com.example.restaurateur.Offer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurateur.R;
import com.example.restaurateur.Reservations;

public class TabDishesDisabledOffers extends android.support.v4.app.Fragment {
    private FloatingActionButton FabActive;
    private FloatingActionButton FabDisabled;
    private Reservations reservationsActivity;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_dishes_offers, container, false);
        TextView a=view.findViewById(R.id.textViewDishesOffers);
        a.setText("DishesDisabledOffers");

        FabActive= ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddActiveCategories);
        FabDisabled= ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddDisabledCategories);
        FabActive.hide();
        FabDisabled.hide();
        reservationsActivity=((Reservations)view.getContext());
        reservationsActivity.state_offers[1]=1;

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
        reservationsActivity.state_offers[1]=0;
        reservationsActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

}
