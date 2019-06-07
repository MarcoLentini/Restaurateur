package com.example.restaurateur.Offer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class OffersCategoryFragment extends Fragment {

    private static final int ADD_CATEGORY_ACTIVITY = 1;
    private static final int EDIT_CATEGORY_ACTIVITY = 2;
    private View view;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter categoriesAdapter;
    private TextView tvNoCategories;
    private MainActivity main;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CAT_FRAGMENT", "onCreate(...) chiamato una volta sola!");
        main = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("CAT_FRAGMENT", "onCreateView chiamato!");
        //Returning the layout file after inflating
        view = inflater.inflate(R.layout.fragment_category_offers, container, false);
        tvNoCategories = view.findViewById(R.id.textViewCategoryOffers);
        if(MainActivity.categoriesData.isEmpty())
            tvNoCategories.setVisibility(View.VISIBLE);
        FloatingActionButton fabCategory = view.findViewById(R.id.fabAddCategory);
        fabCategory.setOnClickListener(view -> {
            //start a new Activity where you can add a new category
            Intent myIntent = new Intent(getActivity(), AddNewCategoryActivity.class);
            startActivityForResult(myIntent, ADD_CATEGORY_ACTIVITY);
        });
        RecyclerView recyclerView = view.findViewById(R.id.CategoryOfferRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Collections.sort(MainActivity.categoriesData);
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
        Category selectedCategory = MainActivity.categoriesData.get(selectedPosition);

        switch (item.getItemId()) {
            case 1:
                Intent myIntent = new Intent(getActivity(), EditCategoryActivity.class);
                Bundle bn = new Bundle();
                bn.putInt("selectedPosition", selectedPosition);
                bn.putString("categoryName", selectedCategory.getCategoryName());
                bn.putLong("categoryPosition", selectedCategory.getCategoryPosition());
                myIntent.putExtras(bn);
                startActivityForResult(myIntent, EDIT_CATEGORY_ACTIVITY);
                return true;

            case 2:
                AlertDialog.Builder removeAlertDialog = new AlertDialog.Builder(getActivity());
                removeAlertDialog.setTitle("Do you really want to remove category " + selectedCategory.getCategoryName() + "?");
                removeAlertDialog.setMessage("NOTE: removing the category you will delete also the dishes inside!");
                removeAlertDialog.setPositiveButton("OK", (dialog, which) -> removeCategory(selectedPosition, selectedCategory));
                removeAlertDialog.setNegativeButton("CANCEL", (dialog, which) -> {});
                removeAlertDialog.show();
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
                Long categoryPosition = data.getExtras().getLong("position");

                Map<String,Object> cat = new HashMap<>();
                cat.put("category_name",categoryName);
                cat.put("category_position",categoryPosition);
                cat.put("rest_id",main.restaurantKey);

                DocumentReference dr = main.db.collection("category").document();
                dr.set(cat).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Category category = new Category(categoryName, dr.getId(),categoryPosition);
                        MainActivity.categoriesData.add(category);
                        Collections.sort(MainActivity.categoriesData);
                        categoriesAdapter.notifyDataSetChanged();
                        categoriesAdapter.notifyItemInserted(MainActivity.categoriesData.size() - 1);
                        if(tvNoCategories.getVisibility() == View.VISIBLE)
                            tvNoCategories.setVisibility(View.INVISIBLE);
                    } else {
                        // Probably only on timeout, from test the request are stored offline
                        Toast.makeText(getContext(),"Internet problem, retry!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            if (requestCode == EDIT_CATEGORY_ACTIVITY) {
                int position = data.getIntExtra("selectedPosition", 0);
                String categoryName = data.getStringExtra("categoryName");
                Long categoryPosition = data.getExtras().getLong("categoryPosition");
                MainActivity.categoriesData.get(position).setCategoryName(categoryName);
                main.db.collection("category").document(MainActivity.categoriesData.get(position).getCategoryID()).update("category_name",categoryName).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        MainActivity.categoriesData.get(position).setCategoryPosition(categoryPosition);
                        main.db.collection("category").document(MainActivity.categoriesData.get(position).getCategoryID()).update("category_position", categoryPosition).addOnCompleteListener(task1 -> {
                            if (task.isSuccessful()) {
                                Collections.sort(MainActivity.categoriesData);
                                categoriesAdapter.notifyDataSetChanged();
                              //  categoriesAdapter.notifyItemChanged(position);
                                // Todo - check snackbar
                                Snackbar.make(view, getString(R.string.snackbar_category_edited), Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                    });
            }

        }
    }

    private void removeCategory(int selectedPosition, Category selectedCategory) {
        main.db.collection("category").document(selectedCategory.getCategoryID()).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                MainActivity.categoriesData.remove(selectedPosition);
                categoriesAdapter.notifyItemRemoved(selectedPosition);
                if(MainActivity.categoriesData.isEmpty())
                    tvNoCategories.setVisibility(View.VISIBLE);
                View.OnClickListener snackbarListener = v -> {
                    MainActivity.categoriesData.add(selectedPosition, selectedCategory);
                    categoriesAdapter.notifyItemInserted(selectedPosition);
                    restoreScrollPositionAfterUndo();
                    if(tvNoCategories.getVisibility() == View.VISIBLE)
                        tvNoCategories.setVisibility(View.INVISIBLE);
                };
                // Todo - snackbar rivedere
                Snackbar.make(view, getString(R.string.snackbar_category_removed), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.snackbar_category_undo), snackbarListener).show();
            }
        });
    }

    private void restoreScrollPositionAfterUndo() {
        if(((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition() == 0)
            layoutManager.scrollToPosition(0);
    }
}
