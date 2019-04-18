package com.example.restaurateur.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurateur.Information.ModifyInfoActivity;
import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

import static android.app.Activity.RESULT_OK;

public class Offers_f extends Fragment  {

    private static final int ADD_CATEGORY_ACTIVITY = 1;
    private static final int ADD_FOOD_OFFER_ACTIVITY = 2;
    FloatingActionButton FabCategory;
    FloatingActionButton FabDishes;

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //echo -- this is floatingActionButton
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers_f, container, false);


//echo -- initializing the floatingActionButton

        FabCategory = view.findViewById(R.id.FabAddCategories);
        FabDishes = view.findViewById(R.id.FabAddDishes);


        FabCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //start a new Activity that you can add food
                Intent myIntent = new Intent(getActivity(), AddNewOfferActivity.class);
                startActivityForResult(myIntent, ADD_CATEGORY_ACTIVITY);
            }
        });

        //Adding the tabs using addTab() method
        FabDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //start a new Activity that you can add food
                Intent myIntent = new Intent(getActivity(), AddNewOfferActivity.class);
                startActivityForResult(myIntent, ADD_FOOD_OFFER_ACTIVITY);
            }
        });
        loadFragment(new TabCategoryActiveOffers());
    return view;
    }

    private void loadFragment(android.support.v4.app.Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_active_offers, fragment);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == ADD_FOOD_OFFER_ACTIVITY) {
                String foodName = data.getExtras().getString("foodName");
                String foodPrice = data.getExtras().getString("foodPrice");
                String foodQuantity = data.getExtras().getString("foodQuantity");
                String foodDescription = data.getExtras().getString("foodDescription");
                // TODO OfferModel om = new OfferModel(); passare i parametri al costruttore
            }
        }
    }
}
