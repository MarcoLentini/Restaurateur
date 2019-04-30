package com.example.restaurateur.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.restaurateur.MainActivity.availableImageId;

public class OffersDishFragment extends android.support.v4.app.Fragment {

    private static final int ADD_FOOD_OFFER_ACTIVITY = 2;
    private static final int EDIT_DISHES_ACTIVITY = 3;
    private RecyclerView.Adapter dishesListAdapter;
    private String category;
    private TextView tvNoDishes;
    private FloatingActionButton fabDishes;
    private ArrayList<OfferModel> dishesOfCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DISH_FRAGMENT", "onCreate(...) chiamato una volta sola!");
        category = getArguments().getString("Category");
        dishesOfCategory = new ArrayList<>();
        for(OfferModel om : MainActivity.offersData.values()) // TODO prendere direttamente dalla categoria la lista dei piatti
            if(om.getCategory().equals(category))
                dishesOfCategory.add(om);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("DISH_FRAGMENT", "onCreateView chiamato!");
        View view = inflater.inflate(R.layout.fragment_dishes_offers, container, false);
        tvNoDishes = view.findViewById(R.id.textViewDishesOffers);
        fabDishes = view.findViewById(R.id.fabAddDish);
        fabDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Here's tvNoDishes Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
                //start tvNoDishes new Activity that you can add food
                Intent myIntent = new Intent(getActivity(), AddNewOfferActivity.class);
                String category= ((MainActivity)getActivity()).getSupportActionBar().getTitle().toString();
                Bundle bn = new Bundle();
                bn.putString("category", category);
                myIntent.putExtras(bn);
                startActivityForResult(myIntent, ADD_FOOD_OFFER_ACTIVITY);
            }
        });
        //Returning the layout file after inflating
        RecyclerView recyclerView = view.findViewById(R.id.ActiveDishesRecyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*recyclerView.setHasFixedSize(true);*/
        // use tvNoDishes linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        dishesListAdapter = new DishesListAdapter(getContext(), dishesOfCategory, this);
        recyclerView.setAdapter(dishesListAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DISH_FRAGMENT", "onResume chiamato!");
        if(dishesOfCategory.isEmpty())
            tvNoDishes.setVisibility(View.VISIBLE);
        else
            tvNoDishes.setVisibility(View.INVISIBLE);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(category);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("DISH_FRAGMENT", "onStop chiamato!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DISH_FRAGMENT", "onDestroy chiamato!");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("DISH_FRAGMENT", "onActivityResult chiamato dopo aggiunta/modifica!");
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
                OfferModel om = MainActivity.offersData.get(foodId);
                om.setCategory(foodCategory);
                om.setDescription(foodDescription);
                om.setImage(foodImage);
                om.setName(foodName);
                om.setPrice(foodPrice);
                om.setQuantity(foodQuantity);
                om.setState(foodState);
                dishesListAdapter.notifyDataSetChanged(); // TODO find a better way to update
                if(MainActivity.categoriesData.get(foodCategory) == null) // TODO update also the category when the if is executed
                    MainActivity.categoriesData.put(foodCategory, new Category(foodCategory));
            }

            if(requestCode == ADD_FOOD_OFFER_ACTIVITY) {
                //TODO: to choise the id and the image: have to change it in future
                int foodId = MainActivity.idDishes++;
                java.util.Random random = new java.util.Random();
                int random_computer_card = random.nextInt(availableImageId.length);
                int image = availableImageId[random_computer_card];
                String foodName = data.getExtras().getString("foodName");
                Double foodPrice = data.getExtras().getDouble("foodPrice");
                Integer foodQuantity = data.getExtras().getInt("foodQuantity");
                String foodDescription = data.getExtras().getString("foodDescription");
                String foodCategory = data.getExtras().getString("category");
                OfferModel offerDish = new OfferModel(foodId, foodName, foodCategory, foodPrice,
                                                foodQuantity, image,"Active",foodDescription);
                MainActivity.offersData.put(foodId, offerDish);
                dishesOfCategory.add(offerDish);
                dishesListAdapter.notifyItemInserted(dishesOfCategory.size() - 1);
                // TODO visualizzare il dato aggiunto secondo un ordine prestabilito
            }
        }
    }
}