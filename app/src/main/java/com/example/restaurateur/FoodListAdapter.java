package com.example.restaurateur;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    private ArrayList<FoodOfferModel> dataSet;
    private LayoutInflater mInflater;

    public FoodListAdapter(Context context, ArrayList<FoodOfferModel> data) {
        this.dataSet = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.reservation_item, parent, false);

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tmp = v.findViewById(R.id.textViewName);
                tmp.setText("Ciao");
            }
        });*/

        FoodViewHolder myViewHolder = new FoodViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int position) {
        TextView textViewTitle = foodViewHolder.textViewTitle;
        TextView textViewContent = foodViewHolder.textViewContent;
        ImageView imageView = foodViewHolder.imageViewIcon;
        TextView textViewPrice = foodViewHolder.textViewPrice;
        TextView textViewQuantity = foodViewHolder.textViewQuantity;

        textViewTitle.setText(dataSet.get(position).getTitle());
        textViewContent.setText(dataSet.get(position).getContent());
        imageView.setImageResource(dataSet.get(position).getImage());
        textViewPrice.setText("" + dataSet.get(position).getPrice());
        textViewQuantity.setText("" + dataSet.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        /**/
        ImageView imageViewIcon;
        TextView textViewTitle;
        TextView textViewContent;
        TextView textViewPrice;
        TextView textViewQuantity;
        /**/

        public FoodViewHolder(View itemView) {
            super(itemView);
            this.imageViewIcon = itemView.findViewById(R.id.imageViewOffer);
            this.textViewTitle = itemView.findViewById(R.id.textViewTitleOffer);
            this.textViewContent = itemView.findViewById(R.id.textViewContentOffer);
            this.textViewPrice = itemView.findViewById(R.id.textViewPriceOfferValue);
            this.textViewQuantity = itemView.findViewById(R.id.textViewQuantityOfferValue);
        }
    }
}
