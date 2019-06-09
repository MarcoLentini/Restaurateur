package com.example.restaurateur.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurateur.Information.RestInformationActivity;
import com.example.restaurateur.Information.UserInformationActivity;
import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;
import com.example.restaurateur.Reservation.ReservationModel;
import com.example.restaurateur.Home.TopSold.TopSoldDishesActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeMainFragment  extends Fragment {

    private final  static String TAG = "HomeMainFragment";
    private Double soldIncome = 0.00;
    private int soldQuantity = 0;
    private TextView dailySoldIncome;
    private TextView dailySoldQuantity;

    private static final String restaurantDataFile = "RestaurantDataFile";
    private String restaurantKey;

    private ArrayList<RestaurantStatistics> restaurantStatistics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView was called");
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        SharedPreferences sharedPref = getContext().getSharedPreferences(restaurantDataFile, Context.MODE_PRIVATE);
        restaurantKey = sharedPref.getString("restaurantKey","");

        restaurantStatistics = new ArrayList<>();
        fillWithData();

        dailySoldIncome = view.findViewById(R.id.tvDailySoldIncome);
        dailySoldQuantity = view.findViewById(R.id.tvDailySoldQuantity);

        updateDailyIncomeAndSoldQuantity();

        FloatingActionButton fabHistoryOrder = view.findViewById(R.id.btnHistoryOrder);
        FloatingActionButton fabComments = view.findViewById(R.id.btnComments);
        FloatingActionButton fabAnalysis = view.findViewById(R.id.btnAnalysis);
        FloatingActionButton fabTopSoldDishes = view.findViewById(R.id.btnTopDishes);
        FloatingActionButton fabMyAccountInfo = view.findViewById(R.id.btnMyAccountInfo);
        FloatingActionButton fabRestaurantInfo = view.findViewById(R.id.btnRestaurantInfo);

         fabHistoryOrder.setOnClickListener(v -> {
             Log.d(TAG, "You click FloatingActionButton fabHistoryOrder");
             Intent myIntent = new Intent(getActivity(), HistoryOrdersActivity.class);
             startActivity(myIntent);
         });
        fabComments.setOnClickListener(v -> {
            Log.d(TAG, "You click FloatingActionButton fabComments");
            Intent myIntent = new Intent(getActivity(), CommentsActivity.class);
            startActivity(myIntent);
        });
        fabAnalysis.setOnClickListener(v -> {
            Log.d(TAG, "You click FloatingActionButton fabAnalysis ");
            Intent myIntent = new Intent(getActivity(), AnalysisActivity.class);
            Bundle b = new Bundle();
            b.putParcelableArrayList("stats", restaurantStatistics);
            myIntent.putExtras(b);
            startActivity(myIntent);
        });
        fabTopSoldDishes.setOnClickListener(v -> {
            Log.d(TAG, "You click FloatingActionButton fabTopSoldDishes ");
            Intent myIntent = new Intent(getActivity(), TopSoldDishesActivity.class);
            Bundle b = new Bundle();
            b.putParcelableArrayList("stats", restaurantStatistics);
            myIntent.putExtras(b);
            startActivity(myIntent);
        });
        fabMyAccountInfo.setOnClickListener(v -> {
            Log.d(TAG, "You click FloatingActionButton fabTopSoldDishes ");
            Intent information = new Intent(getActivity(), UserInformationActivity.class);
            startActivity(information);
        });
        fabRestaurantInfo.setOnClickListener(v -> {
            Log.d(TAG, "You click FloatingActionButton fabRestaurantInfo ");
            Intent information = new Intent(getActivity(), RestInformationActivity.class);
            startActivity(information);
        });
        return  view;
    }

    public void fillWithData(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday_d = cal.getTime();

        Timestamp yesterday = new Timestamp(yesterday_d);

        db.collection("restaurant_statistics").whereEqualTo("restaurantID",restaurantKey).get().addOnCompleteListener(t -> {
            if(t.isSuccessful()){
                QuerySnapshot documents = t.getResult();
                if(!documents.isEmpty()){
                    for(DocumentSnapshot doc : documents){
                        if(getDays(new Date(), doc.getTimestamp("timestamp").toDate()) <= 1) {
                            RestaurantStatistics rs = new RestaurantStatistics(
                                    doc.getId(),
                                    doc.getString("reservationID"),
                                    doc.getString("restaurantID"),
                                    doc.getString("categoryID"),
                                    doc.getString("dishName"),
                                    doc.getString("hash"),
                                    doc.getTimestamp("timestamp"),
                                    doc.getLong("qty"),
                                    doc.getDouble("price")
                            );
                            restaurantStatistics.add(rs);
                        }
                    }
                }
            }
        });
    }

    private int getDays(Date current, Date old) {
        long difference = (current.getTime() - old.getTime()) / (24 * 60 * 60 * 1000);
        int ret = ((Long) Math.abs(difference)).intValue();
        return ret;
    }

    public void updateDailyIncomeAndSoldQuantity(){
        for(ReservationModel reservation : MainActivity.finishedReservationsData){
            if (reservation.getRs_status().equals("DELIVERED")){
                soldQuantity = soldQuantity +1;
                soldIncome = soldIncome + reservation.getTotal_income();
            }
        }
        dailySoldIncome.setText(String.valueOf(soldIncome));
        dailySoldQuantity.setText(String.valueOf(soldQuantity));
        soldQuantity = 0;
        soldIncome = 0.00;
    }
}
