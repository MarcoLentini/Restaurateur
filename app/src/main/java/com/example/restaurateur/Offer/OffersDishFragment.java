package com.example.restaurateur.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class OffersDishFragment extends android.support.v4.app.Fragment {

    private static final int EDIT_DISHES_ACTIVITY = 3;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public RecyclerView.Adapter dishesListAdapter;
    private MainActivity mainActivity = (MainActivity) getActivity();
    private String category;
    private TextView a;
    private FloatingActionButton fabCategory;
    private FloatingActionButton fabDishes;
    private ArrayList<OfferModel> dishesOfCategory;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_active_dishes_offers, container, false);
        a = view.findViewById(R.id.textViewDishesOffers);

        fabCategory = ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddCategories);
        fabDishes = ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddDishes);
        fabCategory.hide();
        fabDishes.show();
        category = getArguments().getString("Category");
        ((MainActivity)view.getContext()).getSupportActionBar().setTitle(category);

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
        dishesOfCategory = new ArrayList<>();
        for(OfferModel om : mainActivity.offersData.values())
            if(om.getCategory().equals(category))
                dishesOfCategory.add(om);
        if(dishesOfCategory.isEmpty())
            a.setText(R.string.no_dishes_offers);
        dishesListAdapter = new DishesListAdapter(getContext(), dishesOfCategory, mainActivity, this); // getContext() forse non va bene
        recyclerView.setAdapter(dishesListAdapter);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        fabDishes.hide();
    }

    @Override
    public void onResume() {
        dishesOfCategory.clear(); // TODO find a better way to update
        for(OfferModel om : mainActivity.offersData.values())
            if(om.getCategory().equals(category))
                dishesOfCategory.add(om);
        if(dishesOfCategory.isEmpty())
            a.setText(R.string.no_dishes_offers);
        else
            a.setVisibility(View.INVISIBLE);
        dishesListAdapter.notifyDataSetChanged();
        fabCategory.hide();
        fabDishes.show();
        super.onResume();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == EDIT_DISHES_ACTIVITY) {
                String foodCategory = data.getExtras().getString("foodCategory");
                String foodName = data.getExtras().getString("foodName");
                String foodDescription = data.getExtras().getString("foodDescription");
                int foodId = data.getExtras().getInt("foodId");
                int foodImage = data.getExtras().getInt("foodImage");
                int foodQuantity = data.getExtras().getInt("foodQuantity");
                Double foodPrice = data.getExtras().getDouble("foodPrice");
                String foodState = data.getExtras().getString("fooodState");
                OfferModel om = mainActivity.offersData.get(foodId);
                om.setCategory(foodCategory);
                om.setDescription(foodDescription);
                om.setImage(foodImage);
                om.setName(foodName);
                om.setPrice(foodPrice);
                om.setQuantity(foodQuantity);
                om.setState(foodState);
                if(mainActivity.categoriesData.get(foodCategory) == null)
                    mainActivity.categoriesData.put(foodCategory, new Category(foodCategory));
            }
        }
    }
}