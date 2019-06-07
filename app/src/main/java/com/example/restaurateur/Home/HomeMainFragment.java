package com.example.restaurateur.Home;

import android.content.Intent;
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

public class HomeMainFragment  extends Fragment {

    private final  static String TAG = "HomeMainFragment";
    private Double soldIncome = 0.00;
    private int soldQuantity = 0;
    private TextView dailySoldIncome;
    private TextView dailySoldQuantity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView was called");
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        dailySoldIncome = view.findViewById(R.id.tvDailySoldIncome);
        dailySoldQuantity = view.findViewById(R.id.tvDailySoldQuantity);

        updateDailyIncomeAndSoldQuantity();

        FloatingActionButton fabHistoryOrder = view.findViewById(R.id.btnHistoryOrder);
        FloatingActionButton fabComments = view.findViewById(R.id.btnComments);
        FloatingActionButton fabAnalysis = view.findViewById(R.id.btnAnalysis);
        FloatingActionButton fabTopSoldDishes = view.findViewById(R.id.btnTopDishes);
        FloatingActionButton fabMyAccountInfo = view.findViewById(R.id.btnMyAccountInfo);
        FloatingActionButton fabRestaurantInfo = view.findViewById(R.id.btnRestaurantInfo);

         fabHistoryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "You click FloatingActionButton fabHistoryOrder");
                Intent myIntent = new Intent(getActivity(), HistoryOrdersActivity.class);
                startActivity(myIntent);
            }
        });
        fabComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "You click FloatingActionButton fabComments");
                Intent myIntent = new Intent(getActivity(), CommentsActivity.class);
                startActivity(myIntent);
            }
        });
        fabAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "You click FloatingActionButton fabAnalysis ");
                Intent myIntent = new Intent(getActivity(), AnalysisActivity.class);
                startActivity(myIntent);
            }
        });
        fabTopSoldDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "You click FloatingActionButton fabTopSoldDishes ");
                Intent myIntent = new Intent(getActivity(), TopSoldDishesActivity.class);
                startActivity(myIntent);
            }
        });
        fabMyAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "You click FloatingActionButton fabTopSoldDishes ");
                Intent information = new Intent(getActivity(), UserInformationActivity.class);
                startActivity(information);
            }
        });
        fabRestaurantInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "You click FloatingActionButton fabRestaurantInfo ");
                Intent information = new Intent(getActivity(), RestInformationActivity.class);
                startActivity(information);
            }
        });
        return  view;
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
