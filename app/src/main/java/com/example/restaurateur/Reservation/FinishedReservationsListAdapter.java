package com.example.restaurateur.Reservation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.Fade;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.Offer.OfferModel;
import com.example.restaurateur.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class FinishedReservationsListAdapter extends RecyclerView.Adapter<FinishedReservationsListAdapter.FinishedReservationViewHolder> {

    private Context context;
    private MainActivity fragmentActivity;
    private ArrayList<ReservationModel> finishedDataSet;
    private LayoutInflater mInflater;
    private ReservationsMainFragment mainFragment;
    private TabReservationsFinished tabFrag;
    private FirebaseFirestore db;


    public FinishedReservationsListAdapter(Context context, ArrayList<ReservationModel> finishedData,
                                           MainActivity fragmentActivity,  TabReservationsFinished tabFrag, ReservationsMainFragment mainFragment) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.mInflater = LayoutInflater.from(context);
        this.finishedDataSet = finishedData;
        this.tabFrag = tabFrag;
        this.mainFragment = mainFragment;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public FinishedReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.finished_reservation_cardview, parent, false);

        FinishedReservationViewHolder holder = new FinishedReservationViewHolder(view);
        view.setOnClickListener(v -> {
            Intent myIntent = new Intent(mainFragment.getContext(), FinishedDetailsActivity.class);
            int itemPosition = holder.getAdapterPosition();
            Bundle bn = new Bundle();
            bn.putInt("reservationCardData", itemPosition);
            myIntent.putExtras(bn);
            mainFragment.startActivityForResult(myIntent, tabFrag.FINISHED_REQ);
         });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedReservationViewHolder finishedReservationViewHolder, int position) {
        TextView textViewOrderId = finishedReservationViewHolder.textViewReservationId;
        //TextView textViewTimestamp = finishedReservationViewHolder.textViewTimestamp;
        TextView textViewTotalIncome = finishedReservationViewHolder.textViewTotalIncome;
        TextView textViewOrderedFood = finishedReservationViewHolder.textViewOrderedDishes;
        TextView textViewReservationState = finishedReservationViewHolder.textViewReservationState;
        Button btnRemoveReservation = finishedReservationViewHolder.btnRemoveReservation;

        ReservationModel tmpRM = finishedDataSet.get(position);
        textViewOrderId.setText("" + tmpRM.getRs_id());
        //textViewTimestamp.setText("" + tmpRM.getTimestamp());
        DecimalFormat format = new DecimalFormat("0.00");
        String formattedIncome = format.format((tmpRM.getTotal_income()-tmpRM.getDelivery_fee()));
        textViewTotalIncome.setText(formattedIncome);

        String reservationOffer = "";
        for (int i = 0; i < tmpRM.getDishesArrayList().size(); i++) {
            String offerName = tmpRM.getDishesArrayList().get(i).getDishName();
            reservationOffer += offerName + "(" + tmpRM.getDishesArrayList().get(i).getDishQty() + ")  ";
        }
        textViewOrderedFood.setText(reservationOffer);
        textViewReservationState.setText(tmpRM.getRs_status());
        btnRemoveReservation.setOnClickListener(v -> {
            int pos = finishedReservationViewHolder.getAdapterPosition();
            finishedReject(pos);
        });
    }

    public void finishedResume(int pos){
        ReservationModel tmpRM = finishedDataSet.get(pos);
        db.collection("reservations").document(tmpRM.getReservation_id()).update("rs_status", ReservationState.STATE_IN_PROGRESS).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                fragmentActivity.removeItemFromFinished(pos);//finishedDataSet.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, finishedDataSet.size());
                tmpRM.setRs_status(ReservationState.STATE_IN_PROGRESS);
                fragmentActivity.addItemToInProgress(tmpRM);//inProgressDataSet.add(tmpRM);
            }
        });
    }

    public void finishedReject(int pos){
        ReservationModel tmpRM = finishedDataSet.get(pos);
        fragmentActivity.removeItemFromFinished(pos);//finishedDataSet.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, finishedDataSet.size());
//        db.collection("reservations").document(tmpRM.getReservation_id()).update("rs_status", ReservationState.STATE_STORED).addOnCompleteListener(task -> {
//            if(task.isSuccessful()) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return finishedDataSet.size();
    }

    static class FinishedReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewReservationId;
        //TextView textViewTimestamp;
        TextView textViewTotalIncome;
        TextView textViewOrderedDishes;
        TextView textViewReservationState;
        Button btnRemoveReservation;

        FinishedReservationViewHolder(View itemView) {
            super(itemView);
            this.textViewReservationId = itemView.findViewById(R.id.textViewOrderIdReservationFinished);
            //this.textViewTimestamp = itemView.findViewById(R.id.textViewRemainingTimeReservationFinished);
            this.textViewTotalIncome = itemView.findViewById(R.id.textViewTotalIncomeReservationFinished);
            this.textViewOrderedDishes = itemView.findViewById(R.id.textViewFoodReservationFinished);
            this.textViewReservationState = itemView.findViewById(R.id.textViewStateReservationFinished);
            this.btnRemoveReservation = itemView.findViewById(R.id.buttonRemoveReservationFinished);
        }
    }
}
