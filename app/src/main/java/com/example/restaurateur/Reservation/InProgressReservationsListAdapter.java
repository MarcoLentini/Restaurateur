package com.example.restaurateur.Reservation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.restaurateur.Offer.OfferModel;
import com.example.restaurateur.R;
import com.example.restaurateur.UserInformationActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class InProgressReservationsListAdapter extends RecyclerView.Adapter<InProgressReservationsListAdapter.InProgressReservationViewHolder> {

    private Context context;
    private ArrayList<ReservationModel> pendingDataSet;
    private ArrayList<ReservationModel> inProgressDataSet;
    private ArrayList<ReservationModel> finishedDataSet;
    private LayoutInflater mInflater;
    private HashMap<Integer, OfferModel> offersData;

    public InProgressReservationsListAdapter(Context context, ArrayList<ReservationModel> pendingData, ArrayList<ReservationModel> inProgressData,
                                             ArrayList<ReservationModel> finishedData, HashMap<Integer, OfferModel> offersData) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.pendingDataSet = pendingData;
        this.inProgressDataSet = inProgressData;
        this.finishedDataSet = finishedData;
        this.offersData = offersData;
    }

    @NonNull
    @Override
    public InProgressReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.in_progress_reservation_cardview, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context.getApplicationContext(), UserInformationActivity.class);
                context.startActivity(myIntent);
            }
        });

        return new InProgressReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InProgressReservationViewHolder inProgressReservationViewHolder, int position) {
        TextView textViewOrderId = inProgressReservationViewHolder.textViewReservationId;
        TextView textViewRemainingTime = inProgressReservationViewHolder.textViewRemainingTime;
        TextView textViewTotalIncome = inProgressReservationViewHolder.textViewTotalIncome;
        TextView textViewOrderedFood = inProgressReservationViewHolder.textViewOrderedDishes;
        TextView textViewReservationNotes = inProgressReservationViewHolder.textViewReservationNotes;
        Button btnFinishtReservation = inProgressReservationViewHolder.btnFinishtReservation;
        Button btnRejectReservation = inProgressReservationViewHolder.btnRejectReservation;

        ReservationModel tmpRM = inProgressDataSet.get(position);
        textViewOrderId.setText("" + tmpRM.getId());
        textViewRemainingTime.setText("" + tmpRM.getRemainingMinutes() + " min");
        textViewTotalIncome.setText("" + tmpRM.getTotalIncome());
        String reservationOffer = "";
        for (int i = 0; i < tmpRM.getReservatedDishes().size(); i++) {
            String offerName = offersData.get(tmpRM.getReservatedDishes().get(i).getDishId()).getName();
            reservationOffer += offerName + "(" + tmpRM.getReservatedDishes().get(i).getDishMultiplier() + ")  ";
        }
        textViewOrderedFood.setText(reservationOffer);
        if(!inProgressDataSet.get(position).getNotes().equals(""))
            textViewReservationNotes.setVisibility(View.VISIBLE);
        btnFinishtReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = inProgressReservationViewHolder.getAdapterPosition();
                inProgressDataSet.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, inProgressDataSet.size());
                // TODO change the state of the reservation to successful
                finishedDataSet.add(tmpRM);
            }
        });
        btnRejectReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = inProgressReservationViewHolder.getAdapterPosition();
                inProgressDataSet.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, inProgressDataSet.size());
                // TODO change the state of the reservation to rejected
                finishedDataSet.add(tmpRM);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inProgressDataSet.size();
    }

   static class InProgressReservationViewHolder extends RecyclerView.ViewHolder {

        TextView textViewReservationId;
        TextView textViewRemainingTime;
        TextView textViewTotalIncome;
        TextView textViewOrderedDishes;
        TextView textViewReservationNotes;
        Button btnFinishtReservation;
        Button btnRejectReservation;

        InProgressReservationViewHolder(View itemView) {
            super(itemView);
            this.textViewReservationId = itemView.findViewById(R.id.textViewOrderIdReservation);
            this.textViewRemainingTime = itemView.findViewById(R.id.textViewRemainingTimeReservation);
            this.textViewTotalIncome = itemView.findViewById(R.id.textViewTotalIncomeReservation);
            this.textViewOrderedDishes = itemView.findViewById(R.id.textViewFoodReservation);
            this.textViewReservationNotes = itemView.findViewById(R.id.textViewStateReservation);
            btnFinishtReservation = itemView.findViewById(R.id.buttonResumeReservation);
            btnRejectReservation = itemView.findViewById(R.id.buttonRemoveReservation);
        }
    }
}
