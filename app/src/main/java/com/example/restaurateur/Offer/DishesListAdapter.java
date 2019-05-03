package com.example.restaurateur.Offer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;

import java.text.DecimalFormat;
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
        DishesViewHolder holder = new DishesListAdapter.DishesViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(view.getContext(), EditOfferActivity.class);
                int position = holder.getAdapterPosition();
                int id = dataSet.get(position).getId();
                OfferModel selected = MainActivity.offersData.get(id);
                Bundle bn = new Bundle();
                bn.putInt("foodId", selected.getId());
                bn.putString("foodName", selected.getName());
                bn.putDouble("foodPrice", selected.getPrice());
                bn.putInt("foodQuantity", selected.getQuantity());
                bn.putString("foodDescription", selected.getDescription());
                bn.putInt("foodImage", selected.getImage());
                bn.putString("foodState", selected.getState());
                myIntent.putExtras(bn);
                parentFragment.startActivityForResult(myIntent, EDIT_DISHES_ACTIVITY);
            }});
        holder.switchOfferState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO decidere cosa fare quando vado on-line off-line
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DishesListAdapter.DishesViewHolder dishesViewHolder, int position) {

        TextView textViewFoodId = dishesViewHolder.textViewFoodId;
        TextView textViewFoodName = dishesViewHolder.textViewFoodName;
        TextView textViewQuantityOffer = dishesViewHolder.textViewQuantityOffer;
        TextView textViewPriceOffer = dishesViewHolder.textViewPriceOffer;
        ImageView offer_food_pic = dishesViewHolder.offer_food_pic;
        Switch switchOfferState = dishesViewHolder.switchOfferState;

        OfferModel tmpOM = dataSet.get(position);
        textViewFoodId.setText(String.valueOf(tmpOM.getId()));
        textViewFoodName.setText(tmpOM.getName());
        textViewQuantityOffer.setText(String.valueOf(tmpOM.getQuantity()));
        DecimalFormat format = new DecimalFormat("0.00");
        String formattedPrice = format.format(tmpOM.getPrice());
        textViewPriceOffer.setText(formattedPrice);
        offer_food_pic.setImageResource(tmpOM.getImage());
        if(tmpOM.getState().equals("Active")) {
            switchOfferState.setChecked(true);
            switchOfferState.setText("On-line");
        }
        else {
            switchOfferState.setChecked(false);
            switchOfferState.setText("Off-line");
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class DishesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView textViewFoodId;
        TextView textViewFoodName;
        TextView textViewQuantityOffer;
        TextView textViewPriceOffer;
        ImageView offer_food_pic;
        Switch switchOfferState;

        DishesViewHolder(View itemView) {
            super(itemView);
            this.textViewFoodId = itemView.findViewById(R.id.offer_food_id);
            this.textViewFoodName = itemView.findViewById(R.id.offer_food_name);
            this.textViewPriceOffer = itemView.findViewById(R.id.textViewPriceOfferValue);
            this.textViewQuantityOffer = itemView.findViewById(R.id.textViewQuantityOfferValue);
            this.offer_food_pic = itemView.findViewById(R.id.offer_food_pic);
            switchOfferState = itemView.findViewById(R.id.offerStateSwitch);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // I pass as first param to menu.add(...) the current adapter position that will be read in OffersDishFragment
            menu.add(this.getAdapterPosition(), 1, 1, R.string.remove_offer_item);
            if(MainActivity.categoriesData.size() > 1) {
                SubMenu menuCategory = menu.addSubMenu(this.getAdapterPosition(), 2, 2, "Change category");
                String currentCategory = dataSet.get(this.getAdapterPosition()).getCategory();
                int subItemId = 21;
                int subItemOrder = 1;
                for (Category c : MainActivity.categoriesData) {
                    if (!c.getCategoryName().equals(currentCategory)) {
                        menuCategory.add(this.getAdapterPosition(), subItemId, subItemOrder, c.getCategoryName());
                        subItemId++;
                        subItemOrder++;
                    }
                }
            }
        }
    }
}
