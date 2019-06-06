package com.example.restaurateur.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.restaurateur.Information.LoginActivity;
import com.example.restaurateur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CommentsActivity extends AppCompatActivity {
    private static final String TAG = "CommentsActivity";
    private RecyclerView recyclerViewComments;
    private CommentsListAdapter commentsListAdapter;
    private ArrayList<CommentModel> commentsDate;
    public FirebaseAuth auth;
    public FirebaseFirestore db;
    public String restaurantKey;
    private static final String restaurantDataFile = "RestaurantDataFile";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        String title = getString(R.string.title_comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        SharedPreferences sharedPref = getSharedPreferences(restaurantDataFile, Context.MODE_PRIVATE);
        restaurantKey = sharedPref.getString("restaurantKey","");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null || restaurantKey.equals("")) {
            startActivity(new Intent(CommentsActivity.this, LoginActivity.class));
            finish();
        }

        //Get Firestore instance
        db = FirebaseFirestore.getInstance();
        //data
        commentsDate = new ArrayList<>();
        fillWithData();

        //RecycleView comments
        recyclerViewComments = findViewById(R.id.rvCommentsFromCustomer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewComments.setLayoutManager(layoutManager);
        //specify an Adapter
        commentsListAdapter = new CommentsListAdapter(this,commentsDate);
        recyclerViewComments.setAdapter(commentsListAdapter);
    }

    public void fillWithData(){
        Log.d("QueryComments", "Start fill with data...");
        db.collection("comments")
                .whereEqualTo("restId", "restaurantKey")
                .addSnapshotListener((EventListener<QuerySnapshot>) (document, e) -> {

                    if (e != null) return;
                    for(DocumentChange dc : document.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED){

                                CommentModel tmpComments = new CommentModel(
                                         dc.getDocument().getId(),
                                        (String)dc.getDocument().get("restId"),//should be restid
                                        (String) dc.getDocument().get("custName"),//should be user id
                                        ((Double) dc.getDocument().get("voteForRestaurant")).floatValue(),//should be vote for restaurant
                                        (String)dc.getDocument().get("notes") //should be notes
                                );
                                //add this current order into the arraylist
                                Log.e(TAG, "tmpComments" + tmpComments.getCommentsId());
                                commentsDate.add(tmpComments);
                                commentsListAdapter.notifyDataSetChanged();
                                Log.d("Query comments", "get tmpComments from firebase and add successful to arraylist!"+tmpComments.getCommentsId());
                            }
                        }
                });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
