package com.example.restaurateur.Offer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;

import java.util.ArrayList;

class DishesListAdapter extends RecyclerView.Adapter<DishesListAdapter.DishesViewHolder> {

    private static final int EDIT_DISHES_ACTIVITY = 3;
    private ArrayList<OfferModel> dataSet;
    private LayoutInflater mInflater;
    private Context context;
    private OffersDishFragment parentFragment;

    public DishesListAdapter(Context context, ArrayList<OfferModel> dishes, OffersDishFragment parentFragment) {
        this.dataSet = dishes;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public DishesListAdapter.DishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.offer_item_info, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                        //start a new Activity that you can add food
                Intent myIntent = new Intent(view.getContext(), EditOfferActivity.class);
                TextView tvFoodId = v.findViewById(R.id.offer_food_id);
                int id = Integer.parseInt(tvFoodId.getText().toString());
                OfferModel selected = MainActivity.offersData.get(id);
                Bundle bn = new Bundle();
                bn.putInt("foodId", selected.getId());
                bn.putString("foodCategory", selected.getCategory());
                bn.putString("foodName", selected.getName());
                bn.putDouble("foodPrice", selected.getPrice());
                bn.putInt("foodQuantity", selected.getQuantity());
                bn.putString("foodDescription", selected.getDescription());
                bn.putInt("foodImage", selected.getImage());
                bn.putString("foodState", selected.getState());
                myIntent.putExtras(bn);
                parentFragment.startActivityForResult(myIntent, EDIT_DISHES_ACTIVITY);
            }});

        return new DishesListAdapter.DishesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishesListAdapter.DishesViewHolder dishesViewHolder, int position) {

        TextView textViewFoodId = dishesViewHolder.textViewFoodId;
        TextView textViewFoodName = dishesViewHolder.textViewFoodName;
        TextView textViewQuantityOffer = dishesViewHolder.textViewQuantityOffer;
        TextView textViewPriceOffer = dishesViewHolder.textViewPriceOffer;
        ImageView offer_food_pic = dishesViewHolder.offer_food_pic;

        OfferModel tmpOM = dataSet.get(position);
        textViewFoodId.setText("" + tmpOM.getId());
        textViewFoodName.setText("" + tmpOM.getName());
        textViewQuantityOffer.setText("" + tmpOM.getQuantity());
        textViewPriceOffer.setText("" + tmpOM.getPrice());
        offer_food_pic.setImageResource(tmpOM.getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class DishesViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFoodId;
        TextView textViewFoodName;
        TextView textViewQuantityOffer;
        TextView textViewPriceOffer;
        ImageView offer_food_pic;

        DishesViewHolder(View itemView) {
            super(itemView);
            this.textViewFoodId = itemView.findViewById(R.id.offer_food_id);
            this.textViewFoodName = itemView.findViewById(R.id.offer_food_name);
            this.textViewPriceOffer = itemView.findViewById(R.id.textViewPriceOfferValue);
            this.textViewQuantityOffer = itemView.findViewById(R.id.textViewQuantityOfferValue);
            this.offer_food_pic = itemView.findViewById(R.id.offer_food_pic);
        }
    }
}
