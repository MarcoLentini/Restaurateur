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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class TabDishesActiveOffers extends android.support.v4.app.Fragment {

    private static final int EDIT_DISHES_ACTIVITY = 3;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public RecyclerView.Adapter ActiveDishesAdapter;
    private MainActivity reservationsActivity = (MainActivity) getActivity();
    private String category;
    private  TextView a;
    private FloatingActionButton FabCategory;
    private FloatingActionButton FabDishes;
    private ArrayList<OfferModel> dishesOfCategory;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_active_dishes_offers, container, false);
        a=view.findViewById(R.id.textViewDishesOffers);

        FabCategory= ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddCategories);
        FabDishes= ((FragmentActivity)view.getContext()).findViewById(R.id.FabAddDishes);
        FabCategory.hide();
        FabDishes.show();
        category=getArguments().getString("Category");
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
        for(int i=0;i<reservationsActivity.DishesOffers.size();i++)
            if(reservationsActivity.DishesOffers.get(i).getCategory().equals(category))
                dishesOfCategory.add(reservationsActivity.DishesOffers.get(i));
            if(dishesOfCategory.isEmpty())
            {
                a.setText(R.string.no_dishes_offers);
            }
        ActiveDishesAdapter = new DishesListAdapter(getContext(), dishesOfCategory,reservationsActivity); // getContext() forse non va bene
        recyclerView.setAdapter(ActiveDishesAdapter);


        return view;

    }

    @Override
    public void onPause() {

        super.onPause();
        FabDishes.hide();

    }

    @Override
    public void onResume() {
        dishesOfCategory.clear();
        for(int i=0;i<reservationsActivity.DishesOffers.size();i++)
            if(reservationsActivity.DishesOffers.get(i).getCategory().equals(category))
                dishesOfCategory.add(reservationsActivity.DishesOffers.get(i));
        if(dishesOfCategory.isEmpty())
        {
            a.setText(R.string.no_dishes_offers);
        }else{
            a.setVisibility(View.INVISIBLE);
        }
        ActiveDishesAdapter.notifyDataSetChanged();
        FabCategory.hide();
        FabDishes.show();
        super.onResume();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == EDIT_DISHES_ACTIVITY && resultCode == RESULT_OK) {

                String foodCategory=  data.getExtras().getString("foodCategory");
                String foodName=  data.getExtras().getString("foodName");
                String foodDescription=  data.getExtras().getString("foodDescription");
                int foodId=  data.getExtras().getInt("foodId");
                int foodImage=  data.getExtras().getInt("foodImage");
                int foodQuantity=  data.getExtras().getInt("foodQuantity");
                Double foodPrice=  data.getExtras().getDouble("foodPrice");
                String foodState= data.getExtras().getString("fooodState");
                MainActivity reservationsActivity = (MainActivity)getActivity();
                for(int i=1;i<reservationsActivity.DishesOffers.size();i++)
                {

                    if(reservationsActivity.DishesOffers.get(i).getId()==foodId)
                    {
                        reservationsActivity.DishesOffers.get(i).setCategory(foodCategory);
                        reservationsActivity.DishesOffers.get(i).setDescription(foodDescription);
                        reservationsActivity.DishesOffers.get(i).setImage(foodImage);
                        reservationsActivity.DishesOffers.get(i).setName(foodName);
                        reservationsActivity.DishesOffers.get(i).setPrice(foodPrice);
                        reservationsActivity.DishesOffers.get(i).setQuantity(foodQuantity);
                        reservationsActivity.DishesOffers.get(i).setState(foodState);
                        if(!reservationsActivity.categories.contains(new Category(foodCategory)))
                            reservationsActivity.categories.add(new Category(foodCategory));
                        break;
                    }
                }


            }

        }
    }
}