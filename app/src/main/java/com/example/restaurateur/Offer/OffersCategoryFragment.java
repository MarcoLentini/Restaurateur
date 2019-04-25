package com.example.restaurateur.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

public class OffersCategoryFragment extends Fragment {

    private static final int ADD_CATEGORY_ACTIVITY = 1;
    private FloatingActionButton fabCategory;
    private RecyclerView.Adapter categoriesAdapter;
    private TextView tvNoCategories;
    private ArrayList<Category> categoriesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CAT_FRAGMENT", "onCreate(...) chiamato una volta sola!");
        categoriesList = new ArrayList<>(MainActivity.categoriesData.values());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("CAT_FRAGMENT", "onCreateView chiamato!");
        //Returning the layout file after inflating
        View view = inflater.inflate(R.layout.fragment_category_offers, container, false);
        tvNoCategories = view.findViewById(R.id.textViewCategoryOffers);
        fabCategory = view.findViewById(R.id.fabAddCategory);
        fabCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start a new Activity where you can add a new category
                Intent myIntent = new Intent(getActivity(), AddNewCategoryActivity.class);
                startActivityForResult(myIntent, ADD_CATEGORY_ACTIVITY);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.CategoryOfferRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        categoriesAdapter = new CategoriesListAdapter(getContext(), categoriesList);
        recyclerView.setAdapter(categoriesAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CAT_FRAGMENT", "onResume chiamato!");
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.offers_title);
        if(MainActivity.categoriesData.isEmpty()) // TODO split if-else and move in the function where the user inserts/deletes category
            tvNoCategories.setVisibility(View.VISIBLE);
        else
            tvNoCategories.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("CAT_FRAGMENT", "onStop chiamato!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("CAT_FRAGMENT", "onDestroy chiamato!");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CAT_FRAGMENT", "onActivityResult() chiamato dopo aver aggiunto una categoria!");
        if(resultCode == RESULT_OK) {

            if (requestCode == ADD_CATEGORY_ACTIVITY) {
                String categoryName = data.getStringExtra("category");
                Category category = new Category(categoryName);
                MainActivity.categoriesData.put(categoryName, category);
                categoriesList.add(category);
                categoriesAdapter.notifyItemInserted(categoriesList.size() - 1);
            }

        }
    }
}
