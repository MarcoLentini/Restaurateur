package com.example.restaurateur.Offer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

import java.util.ArrayList;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.CategoriesViewHolder> {

    private ArrayList<Category> dataSet;
    private LayoutInflater mInflater;
    private Context context;

    public CategoriesListAdapter(Context context, ArrayList<Category> categories) {
        this.dataSet = categories;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.category_cardview, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loadFragment(v, new OffersDishFragment(), v.findViewById(R.id.textViewCategoryName));
            }
        });

        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder categoriesViewHolder, int position) {
        TextView textViewCategoryName = categoriesViewHolder.textViewCategoryName;

        Category tmpRM = dataSet.get(position);
        textViewCategoryName.setText("" + tmpRM.getCategory());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCategoryName;

        CategoriesViewHolder(View itemView) {
            super(itemView);
            this.textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
        }
    }

    private void loadFragment(View view, android.support.v4.app.Fragment fragment,TextView v) {
        // load fragment
        FragmentTransaction transaction = ((FragmentActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        String category = v.getText().toString();
        bundle.putString("Category", category);
        // set Fragmentclass Arguments
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame_container_offers, fragment, "DishesOffers");
        transaction.addToBackStack("Category");
        transaction.commit();
        ((MainActivity)view.getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}







