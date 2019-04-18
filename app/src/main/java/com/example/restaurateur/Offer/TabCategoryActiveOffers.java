package com.example.restaurateur.Offer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;


public class TabCategoryActiveOffers extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter categoriesAdapter;
    private MainActivity reservationsActivity = (MainActivity) getActivity();
    private FloatingActionButton FabCategory;
    private FloatingActionButton FabDishes;



    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view=inflater.inflate(R.layout.fragment_tab_active_offers, container, false);
        recyclerView = view.findViewById(R.id.ActiveOfferRecyclerView);

        FabCategory= ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddCategories);
        FabDishes= ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddDishes);
        FabCategory.show();
        FabDishes.hide();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*recyclerView.setHasFixedSize(true);*/
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        categoriesAdapter = new CategoriesListAdapter(getContext(), reservationsActivity.categories,reservationsActivity); // getContext() forse non va bene
        recyclerView.setAdapter(categoriesAdapter);

        return view;}


    @Override
    public void onPause() {
        FabCategory.hide();
        FabDishes.hide();
        super.onPause();
    }

    @Override
    public void onResume() {
        FabCategory.show();
        FabDishes.hide();
        super.onResume();
    }
}
