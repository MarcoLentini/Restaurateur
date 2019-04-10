package com.example.restaurateur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TabReservationsPending extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter foodAdapter;
    private static ArrayList<FoodOfferModel> data;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_reservations, container, false);
        recyclerView = view.findViewById(R.id.foodRecyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*recyclerView.setHasFixedSize(true);*/
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<FoodOfferModel>();
        for (int i = 0; i < MyData.titleArray.length; i++) {
            data.add(new FoodOfferModel(
                    MyData.titleArray[i],
                    MyData.contentArray[i],
                    MyData.id_[i],
                    MyData.drawableArray[i],
                    MyData.priceArray[i],
                    MyData.quantityArray[i]
            ));
        }

        // specify an Adapter
        foodAdapter = new FoodListAdapter(getContext(), data); // getContext() forse non va bene
        recyclerView.setAdapter(foodAdapter);

        return view;
    }

}
