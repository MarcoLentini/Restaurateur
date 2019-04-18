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
        private String type;
    private MainActivity reservationsActivity ;

    public CategoriesListAdapter(Context context, ArrayList<Category> categories, MainActivity reservationsActivity, String type) {
        this.dataSet = categories;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.type=type;
        this.reservationsActivity = reservationsActivity;
        }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = mInflater.inflate(R.layout.category_cardview, parent, false);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type=="disabled")
                    loadFragment(v,new TabDishesDisabledOffers(),v.findViewById(R.id.textViewCategoryName));
                    else
                        loadFragment(v,new TabDishesActiveOffers(),v.findViewById(R.id.textViewCategoryName));
                }
            });

            CategoriesViewHolder myViewHolder = new CategoriesViewHolder(view);
            return myViewHolder;
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

        public static class CategoriesViewHolder extends RecyclerView.ViewHolder {

            TextView textViewCategoryName;

            public CategoriesViewHolder(View itemView) {
                super(itemView);
                this.textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
                 }
        }
    private void loadFragment(View view, android.support.v4.app.Fragment fragment,TextView v) {
        // load fragment
        FragmentTransaction transaction = ((FragmentActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        String category=v.getText().toString();
        bundle.putString("Category", category);
        // set Fragmentclass Arguments
        fragment.setArguments(bundle);
        if(type=="active"){
            transaction.replace(R.id.frame_container_active_offers, fragment,"ActiveDishesOffers");
            transaction.addToBackStack("ActiveCategory");
        }else if(type=="disabled") {
            transaction.replace(R.id.frame_container_disabled_offers, fragment, "DisabledDishesOffers");
            transaction.addToBackStack("DisabledCategoryOffers");
        }

        transaction.commit();
        ((MainActivity)view.getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    }







