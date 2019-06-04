package com.example.restaurateur.Home;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.restaurateur.R;

import java.util.ArrayList;

public class TopSoldDishesListAdapter extends RecyclerView.Adapter {
    private Context context;
    private  ArrayList<TopSoldDishModel> topSoldDishesData;
    private LayoutInflater mInflater;

    public TopSoldDishesListAdapter(Context context, ArrayList<TopSoldDishModel> topSoldDishesData) {
         this.context = context;
         this.topSoldDishesData = topSoldDishesData;
         this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.cardview_top_sold_dishes, viewGroup,false);
        TopSoldDishesViewHolder holder = new TopSoldDishesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TopSoldDishesViewHolder topSoldDishesViewHolder = (TopSoldDishesViewHolder)viewHolder;
        TextView textViewDishName = topSoldDishesViewHolder.textViewDishName;
        TextView textViewDishSoldQuantity = topSoldDishesViewHolder.textViewDishSoldQuantity;
        TextView textViewRankingTopSold = topSoldDishesViewHolder.textViewRankingTopSold;
        ImageView imageViewFoodPic = topSoldDishesViewHolder.imageViewFoodPic;
        ImageView imageViewRankingTopSold = topSoldDishesViewHolder.imageViewRankingTopSold;

        TopSoldDishModel topSoldDish = topSoldDishesData.get(position);
        if (position == 0){
            imageViewRankingTopSold.setVisibility(View.VISIBLE);
            imageViewRankingTopSold.setImageResource(R.drawable.ic_ranking_1st);
        }else if (position == 1){
            imageViewRankingTopSold.setVisibility(View.VISIBLE);
            imageViewRankingTopSold.setImageResource(R.drawable.ic_ranking_2nd);
        }else if (position == 2){
            imageViewRankingTopSold.setVisibility(View.VISIBLE);
            imageViewRankingTopSold.setImageResource(R.drawable.ic_ranking_3rd);
        }else  imageViewRankingTopSold.setVisibility(View.GONE);

        Uri tmpUri = Uri.parse(topSoldDish.getImage());
        Glide.with(context).load(tmpUri).placeholder(R.drawable.img_rest_1).into(imageViewFoodPic);
        textViewRankingTopSold.setText(String.valueOf(position+1));
        textViewDishName.setText(topSoldDish.getDishName());
        textViewDishSoldQuantity.setText(String.valueOf(topSoldDish.getMonthlySoldQuantity()));

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class TopSoldDishesViewHolder extends RecyclerView.ViewHolder {
         TextView textViewDishName;
         TextView textViewDishSoldQuantity;
         TextView textViewRankingTopSold;
         ImageView imageViewFoodPic;
         ImageView imageViewRankingTopSold;
        public TopSoldDishesViewHolder(View view) {
            super(view);
            this.textViewDishName = view.findViewById(R.id.tvFoodNameTopSold);
            this.textViewDishSoldQuantity = view.findViewById(R.id.tvQuantityTopSold);
            this.textViewRankingTopSold = view.findViewById(R.id.tvRankingTopSold);
            this.imageViewFoodPic =view.findViewById(R.id.imgFoodPicTopSold);
            this.imageViewRankingTopSold = view.findViewById(R.id.ivRankingTopSold);
        }
    }
}
