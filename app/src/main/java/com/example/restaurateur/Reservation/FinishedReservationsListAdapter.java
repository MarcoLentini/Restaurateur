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

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.Offer.OfferModel;
import com.example.restaurateur.R;
import com.example.restaurateur.UserInformationActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class FinishedReservationsListAdapter extends RecyclerView.Adapter<FinishedReservationsListAdapter.FinishedReservationViewHolder> {

    private Context context;
    private MainActivity fragmentActivity;
    private ArrayList<ReservationModel> finishedDataSet;
    private LayoutInflater mInflater;
    private HashMap<Integer, OfferModel> offersData;

    public FinishedReservationsListAdapter(Context context, ArrayList<ReservationModel> finishedData,
                                           HashMap<Integer, OfferModel> offersData, MainActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.mInflater = LayoutInflater.from(context);
        this.finishedDataSet = finishedData;
        this.offersData = offersData;
    }

    @NonNull
    @Override
    public FinishedReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.finished_reservation_cardview, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context.getApplicationContext(), UserInformationActivity.class);
                context.startActivity(myIntent);
            }
        });

        return new FinishedReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedReservationViewHolder finishedReservationViewHolder, int position) {
        TextView textViewOrderId = finishedReservationViewHolder.textViewReservationId;
        TextView textViewRemainingTime = finishedReservationViewHolder.textViewRemainingTime;
        TextView textViewTotalIncome = finishedReservationViewHolder.textViewTotalIncome;
        TextView textViewOrderedFood = finishedReservationViewHolder.textViewOrderedDishes;
        TextView textViewReservationState = finishedReservationViewHolder.textViewReservationState;
        Button btnResumeReservation = finishedReservationViewHolder.btnResumeReservation;
        Button btnRemoveReservation = finishedReservationViewHolder.btnRemoveReservation;

        ReservationModel tmpRM = finishedDataSet.get(position);
        textViewOrderId.setText("" + tmpRM.getId());
        textViewRemainingTime.setText("" + tmpRM.getRemainingMinutes() + " min");
        textViewTotalIncome.setText("" + tmpRM.getTotalIncome());
        String reservationOffer = "";
        for (int i = 0; i < tmpRM.getReservatedDishes().size(); i++) {
            String offerName = offersData.get(tmpRM.getReservatedDishes().get(i).getDishId()).getName();
            reservationOffer += offerName + "(" + tmpRM.getReservatedDishes().get(i).getDishMultiplier() + ")  ";
        }
        textViewOrderedFood.setText(reservationOffer);
        textViewReservationState.setText(tmpRM.getState());
        btnResumeReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = finishedReservationViewHolder.getAdapterPosition();
                fragmentActivity.removeItemFromFinished(pos);//finishedDataSet.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, finishedDataSet.size());
                tmpRM.setState(ReservationState.STATE_IN_PROGRESS);
                fragmentActivity.addItemToInProgress(tmpRM);//inProgressDataSet.add(tmpRM);
            }
        });
        btnRemoveReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = finishedReservationViewHolder.getAdapterPosition();
                fragmentActivity.removeItemFromFinished(pos);//finishedDataSet.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, finishedDataSet.size());
                // TODO probably we will need to save on the history database
            }
        });
    }

    @Override
    public int getItemCount() {
        return finishedDataSet.size();
    }

    static class FinishedReservationViewHolder extends RecyclerView.ViewHolder {

        TextView textViewReservationId;
        TextView textViewRemainingTime;
        TextView textViewTotalIncome;
        TextView textViewOrderedDishes;
        TextView textViewReservationState;
        Button btnResumeReservation;
        Button btnRemoveReservation;

        FinishedReservationViewHolder(View itemView) {
            super(itemView);
            this.textViewReservationId = itemView.findViewById(R.id.textViewOrderIdReservation);
            this.textViewRemainingTime = itemView.findViewById(R.id.textViewRemainingTimeReservation);
            this.textViewTotalIncome = itemView.findViewById(R.id.textViewTotalIncomeReservation);
            this.textViewOrderedDishes = itemView.findViewById(R.id.textViewFoodReservation);
            this.textViewReservationState = itemView.findViewById(R.id.textViewStateReservation);
            this.btnResumeReservation = itemView.findViewById(R.id.buttonResumeReservation);
            this.btnRemoveReservation = itemView.findViewById(R.id.buttonRemoveReservation);
        }
    }
}
