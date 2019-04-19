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
import android.util.Log;
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

    //TODO:id e image_id for dishes: are to remove
    private int idDishes=26;
    //TODO:per scegliere id image a caso tra quelli dati (da togliere percò non so come recuperare l'immagine dei dishes immassa

    private int[] available_image_id = {R.drawable.ic_offer_pizza, R.drawable.ic_offer_cake, R.drawable.ic_offer_coffee, R.drawable.ic_offer_fries};
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
                Intent myIntent = new Intent(getActivity(), AddNewCategory.class);
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
                MainActivity reservationsActivity = (MainActivity)getActivity();
                String category= reservationsActivity.getSupportActionBar().getTitle().toString();
                Bundle bn = new Bundle();
                bn.putString("category", category);
                myIntent.putExtras(bn);
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
                //TODO: to choise the id and the image: have to change it in future
                int foodId = idDishes;
                idDishes++;
                java.util.Random random = new java.util.Random();
                int random_computer_card = random.nextInt(available_image_id.length);
                int image = available_image_id[random_computer_card];
                String foodName = data.getExtras().getString("foodName");
                Double foodPrice = Double.parseDouble(data.getExtras().getString("foodPrice"));
                Integer foodQuantity = Integer.parseInt(data.getExtras().getString("foodQuantity"));
                String foodDescription = data.getExtras().getString("foodDescription");
                String foodCategory= data.getExtras().getString("category");
                MainActivity reservationsActivity = (MainActivity)getActivity();
                reservationsActivity.DishesOffers.add(new OfferModel(foodId,foodName,foodCategory,foodPrice,foodQuantity,image,"Active",foodDescription));

            }

     if (requestCode == ADD_CATEGORY_ACTIVITY && resultCode == RESULT_OK) {
         String category = data.getStringExtra("category");
         MainActivity reservationsActivity = (MainActivity) getActivity();
         reservationsActivity.categories.add(new Category(category));
     }

    }
}}
