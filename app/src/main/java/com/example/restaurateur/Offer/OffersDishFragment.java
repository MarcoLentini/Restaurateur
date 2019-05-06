package com.example.restaurateur.Offer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;
import com.google.firebase.firestore.DocumentReference;

import static android.app.Activity.RESULT_OK;

public class OffersDishFragment extends android.support.v4.app.Fragment {

    private static final int ADD_FOOD_OFFER_ACTIVITY = 2;
    private static final int EDIT_DISHES_ACTIVITY = 3;
    private RecyclerView.Adapter dishesListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Category category;
    private Integer categoryPosition;
    private TextView tvNoDishes;
    private View view;

    private MainActivity main;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity) getActivity();
        Log.d("DISH_FRAGMENT", "onCreate(...) chiamato una volta sola!");

        categoryPosition = getArguments().getInt("Category");
        category = MainActivity.categoriesData.get(categoryPosition);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("DISH_FRAGMENT", "onCreateView chiamato!");
        view = inflater.inflate(R.layout.fragment_dishes_offers, container, false);
        tvNoDishes = view.findViewById(R.id.textViewDishesOffers);
        if(category.getDishes().isEmpty())
            tvNoDishes.setVisibility(View.VISIBLE);
        FloatingActionButton fabDishes = view.findViewById(R.id.fabAddDish);
        fabDishes.setOnClickListener(view -> {
            //start tvNoDishes new Activity that you can add food
            Intent myIntent = new Intent(getActivity(), AddNewOfferActivity.class);
            Bundle bn = new Bundle();
            bn.putInt("category", categoryPosition);
            myIntent.putExtras(bn);
            startActivityForResult(myIntent, ADD_FOOD_OFFER_ACTIVITY);
        });
        //Returning the layout file after inflating
        RecyclerView recyclerView = view.findViewById(R.id.ActiveDishesRecyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*recyclerView.setHasFixedSize(true);*/
        // use tvNoDishes linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        dishesListAdapter = new DishesListAdapter(getContext(), category, this);
        recyclerView.setAdapter(dishesListAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DISH_FRAGMENT", "onResume chiamato!");
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(category.getCategoryName());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("DISH_FRAGMENT", "onStop chiamato!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DISH_FRAGMENT", "onDestroy chiamato!");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int selectedPosition = item.getGroupId();
        OfferModel selectedOffer = category.getDishes().get(selectedPosition);

        if (item.getItemId() == 1) {
            AlertDialog.Builder removeAlertDialog = new AlertDialog.Builder(getActivity());
            removeAlertDialog.setTitle(getString(R.string.alert_dialog_offer_remove).
                    concat(" " + selectedOffer.getName())  + "?");
            removeAlertDialog.setPositiveButton(R.string.alert_dialog_offer_remove_btn_ok, (dialog, which) -> removeOffer(selectedPosition, selectedOffer));
            removeAlertDialog.setNegativeButton(R.string.alert_dialog_offer_remove_btn_cancel, (dialog, which) -> {});
            removeAlertDialog.show();
            return true;
        }

        if(MainActivity.categoriesData.size() > 1) {
            // TODO - update firebase - change category
            if(item.getItemId() >= 21) {
                String newCategory = item.getTitle().toString();
                selectedOffer.setCategory(newCategory);
                dishesListAdapter.notifyItemRemoved(selectedPosition);
                Snackbar.make(view, getString(R.string.snackbar_offer_category_changed).concat(" " + newCategory), Snackbar.LENGTH_LONG).show();
            }
        }

        return super.onContextItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("DISH_FRAGMENT", "onActivityResult chiamato dopo aggiunta/modifica!");
        if(resultCode == RESULT_OK) {

            if (requestCode == EDIT_DISHES_ACTIVITY) {
                String foodName = data.getExtras().getString("foodName");
                String foodDescription = data.getExtras().getString("foodDescription");
                int foodId = data.getExtras().getInt("foodId");
                String foodImage = data.getExtras().getString("foodImage");
                Long foodQuantity = data.getExtras().getLong("foodQuantity");
                Double foodPrice = data.getExtras().getDouble("foodPrice");
                OfferModel om = category.getDishes().get(foodId);
                om.setDescription(foodDescription);
                om.setImage(foodImage);
                om.setName(foodName);
                om.setPrice(foodPrice);
                om.setQuantity(foodQuantity);
                DocumentReference dr = main.db.collection("category").document(category.getCategoryID()).collection("dishes").document(om.getId());
                dr.set(om).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        dishesListAdapter.notifyDataSetChanged(); // TODO find a better way to update
                    } else {
                        // Probably only on timeout, from test the request are stored offline
                        Toast.makeText(getContext(),"Internet problem, retry!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            if(requestCode == ADD_FOOD_OFFER_ACTIVITY) {
                String image = data.getExtras().getString("foodImage");
                String foodName = data.getExtras().getString("foodName");
                Double foodPrice = data.getExtras().getDouble("foodPrice");
                Long foodQuantity = data.getExtras().getLong("foodQuantity");
                String foodDescription = data.getExtras().getString("foodDescription");
                String foodCategory = data.getExtras().getString("category");
                Boolean foodState = data.getExtras().getBoolean("foodState");
                OfferModel offerDish = new OfferModel(null, foodName, foodCategory, foodPrice,
                        foodQuantity, image,foodDescription, foodState);

                DocumentReference dr = main.db.collection("category").document(category.getCategoryID()).collection("dishes").document();
                dr.set(offerDish).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        offerDish.setId(dr.getId());
                        category.getDishes().add(offerDish);
                        dishesListAdapter.notifyItemInserted(category.getDishes().size() - 1);
                        // TODO visualizzare il dato aggiunto secondo un ordine prestabilito
                        if(tvNoDishes.getVisibility() == View.VISIBLE)
                            tvNoDishes.setVisibility(View.INVISIBLE);
                    } else {
                        // Probably only on timeout, from test the request are stored offline
                        Toast.makeText(getContext(),"Internet problem, retry!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void removeOffer(int selectedPosition, OfferModel selectedOffer) {
        main.db.collection("category").document(category.getCategoryID())
                .collection("dishes").document(selectedOffer.getId()).delete().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                category.getDishes().remove(selectedOffer);
                dishesListAdapter.notifyItemRemoved(selectedPosition);
                if(category.getDishes().isEmpty())
                    tvNoDishes.setVisibility(View.VISIBLE);
                View.OnClickListener snackbarListener = v -> {
                    // Todo - firebase revert - esiste un ondisappear della snack?
                    category.getDishes().add(selectedOffer);
                    dishesListAdapter.notifyItemInserted(selectedPosition);
                    restoreScrollPositionAfterUndo();
                    if(tvNoDishes.getVisibility() == View.VISIBLE)
                        tvNoDishes.setVisibility(View.INVISIBLE);
                };
                Snackbar.make(view, getString(R.string.snackbar_offer_remove), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.snackbar_offer_undo), snackbarListener).show();
            } else {
                Log.e("OfferDish/Remove", "Remove Error");
            }
        });
    }

    private void restoreScrollPositionAfterUndo() {
        if(((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition() == 0)
            layoutManager.scrollToPosition(0);
    }
}