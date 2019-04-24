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
import android.widget.TextView;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

import java.util.ArrayList;


public class OffersCategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter categoriesAdapter;
    //private MainActivity mainActivity = (MainActivity) getActivity();
    private FloatingActionButton fabCategory;
    private FloatingActionButton fabDishes;
    private TextView a;
    private ArrayList<Category> categoriesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view=inflater.inflate(R.layout.fragment_category_offers, container, false);
        recyclerView = view.findViewById(R.id.ActiveOfferRecyclerView);
        a=view.findViewById(R.id.textViewCategoryOffers);
        fabCategory = ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddCategories);
        fabDishes = ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddDishes);
        fabCategory.show();
        fabDishes.hide();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*recyclerView.setHasFixedSize(true);*/
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        if(MainActivity.categoriesData.isEmpty())
            a.setText(R.string.no_category_offers);
        categoriesList = new ArrayList<>(MainActivity.categoriesData.values());
        categoriesAdapter = new CategoriesListAdapter(getContext(), categoriesList); // getContext() forse non va bene
        recyclerView.setAdapter(categoriesAdapter);

        return view;
    }

    @Override
    public void onPause() {
        fabCategory.hide();
        fabDishes.hide();
        super.onPause();
    }

    @Override
    public void onResume() {
        categoriesList.clear();
        categoriesList.addAll(MainActivity.categoriesData.values());
        if(MainActivity.categoriesData.isEmpty())
            a.setText(R.string.no_category_offers);
        else
            a.setVisibility(View.INVISIBLE);
        categoriesAdapter.notifyDataSetChanged();
        fabCategory.show();
        fabDishes.hide();
        super.onResume();
    }
}