package com.example.restaurateur.Offer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurateur.R;
import com.example.restaurateur.UserInformationActivity;

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
                    Intent myIntent = new Intent(context.getApplicationContext(), UserInformationActivity.class);
                    context.startActivity(myIntent);
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
    }







