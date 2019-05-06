package com.example.restaurateur.Offer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

class DishesListAdapter extends RecyclerView.Adapter<DishesListAdapter.DishesViewHolder> {

    private static final int EDIT_DISHES_ACTIVITY = 3;
    private Category category;
    private LayoutInflater mInflater;
    private Context context;
    private OffersDishFragment parentFragment;

    public DishesListAdapter(Context context, Category category, OffersDishFragment parentFragment) {
        this.category = category;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public DishesListAdapter.DishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.offer_item_cardview, parent, false);
        DishesViewHolder holder = new DishesListAdapter.DishesViewHolder(view);
        view.setOnClickListener(v -> {
            Intent myIntent = new Intent(view.getContext(), EditOfferActivity.class);
            int position = holder.getAdapterPosition();
            //String id = dataSet.get(position).getId();
            //OfferModel selected = MainActivity.categoriesDataget(position);
            OfferModel selected = category.getDishes().get(position);
            Bundle bn = new Bundle();
            bn.putInt("foodId", position);
            bn.putString("foodName", selected.getName());
            bn.putDouble("foodPrice", selected.getPrice());
            bn.putLong("foodQuantity", selected.getQuantity());
            bn.putString("foodDescription", selected.getDescription());
            bn.putString("foodImage", selected.getImage());
            bn.putBoolean("foodState", selected.getState());
            myIntent.putExtras(bn);
            parentFragment.startActivityForResult(myIntent, EDIT_DISHES_ACTIVITY);
        });
        holder.switchOfferState.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO decidere cosa fare quando vado on-line off-line
            // Todo - update su firebase
            if(isChecked){
                buttonView.setChecked(true);
                buttonView.setText("On-line");
            }
            else {
                buttonView.setChecked(false);
                buttonView.setText("Off-line");
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DishesListAdapter.DishesViewHolder dishesViewHolder, int position) {

        TextView textViewFoodName = dishesViewHolder.textViewFoodName;
        TextView textViewQuantityOffer = dishesViewHolder.textViewQuantityOffer;
        TextView textViewPriceOffer = dishesViewHolder.textViewPriceOffer;
        ImageView offer_food_pic = dishesViewHolder.offer_food_pic;
        Switch switchOfferState = dishesViewHolder.switchOfferState;

        OfferModel tmpOM = category.getDishes().get(position);
        textViewFoodName.setText(tmpOM.getName());
        textViewQuantityOffer.setText(String.valueOf(tmpOM.getQuantity()));
        DecimalFormat format = new DecimalFormat("0.00");
        String formattedPrice = format.format(tmpOM.getPrice());
        textViewPriceOffer.setText(formattedPrice);
        // Todo - senza placeholder?
        Glide.with(this.context).load(Uri.parse(tmpOM.getImage())).into(offer_food_pic);
        // .placeholder(R.drawable.img_rest_1)
        if(tmpOM.getState()) {
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
        return category.getDishes().size();
    }

    class DishesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView textViewFoodName;
        TextView textViewQuantityOffer;
        TextView textViewPriceOffer;
        ImageView offer_food_pic;
        Switch switchOfferState;

        DishesViewHolder(View itemView) {
            super(itemView);
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
                // Todo - upload change cat firebase
                // String currentCategory = category.getDishes().get(this.getAdapterPosition()).getCategory();
                int subItemId = 21;
                int subItemOrder = 1;
                for (Category c : MainActivity.categoriesData) {
                    if (!c.getCategoryName().equals(category)) {
                        menuCategory.add(this.getAdapterPosition(), subItemId, subItemOrder, c.getCategoryName());
                        subItemId++;
                        subItemOrder++;
                    }
                }
            }
        }
    }
}
