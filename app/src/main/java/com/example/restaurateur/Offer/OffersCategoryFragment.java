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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;

import static android.app.Activity.RESULT_OK;

public class OffersCategoryFragment extends Fragment {

    private static final int ADD_CATEGORY_ACTIVITY = 1;
    private static final int EDIT_CATEGORY_ACTIVITY = 2;
    private FloatingActionButton fabCategory;
    private RecyclerView.Adapter categoriesAdapter;
    private TextView tvNoCategories;
    //private ArrayList<Category> categoriesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CAT_FRAGMENT", "onCreate(...) chiamato una volta sola!");
        //categoriesList = new ArrayList<>(MainActivity.categoriesData.values());
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
        categoriesAdapter = new CategoriesListAdapter(getContext(), MainActivity.categoriesData);
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
    public boolean onContextItemSelected(MenuItem item) {
        int selectedPosition = item.getGroupId();
        switch (item.getItemId()) {
            case 1:
                Intent myIntent = new Intent(getActivity(), EditCategoryActivity.class);
                Bundle bn = new Bundle();
                Category selectedCategory = MainActivity.categoriesData.get(selectedPosition);
                bn.putInt("selectedPosition", selectedPosition);
                bn.putString("categoryName", selectedCategory.getCategoryName());
                myIntent.putExtras(bn);
                startActivityForResult(myIntent, EDIT_CATEGORY_ACTIVITY);
                return true;

            case 2:
                MainActivity.categoriesData.remove(selectedPosition);
                categoriesAdapter.notifyItemRemoved(selectedPosition);
                categoriesAdapter.notifyItemRangeChanged(selectedPosition, MainActivity.categoriesData.size());
                return true;

            default: return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CAT_FRAGMENT", "onActivityResult() chiamato dopo aver aggiunto una categoria!");
        if(resultCode == RESULT_OK) {

            if (requestCode == ADD_CATEGORY_ACTIVITY) {
                String categoryName = data.getStringExtra("category");
                Category category = new Category(categoryName);
                MainActivity.categoriesData.add(category);
                categoriesAdapter.notifyItemInserted(MainActivity.categoriesData.size() - 1);
            }

            if (requestCode == EDIT_CATEGORY_ACTIVITY) {
                int position = data.getIntExtra("selectedPosition", 0);
                String categoryName = data.getStringExtra("categoryName");
                MainActivity.categoriesData.get(position).setCategoryName(categoryName);
                categoriesAdapter.notifyItemChanged(position);
            }

        }
    }
}
