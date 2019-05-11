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

import java.util.ArrayList;
import java.util.HashMap;

public class InProgressReservationsListAdapter extends RecyclerView.Adapter<InProgressReservationsListAdapter.InProgressReservationViewHolder> {

    private Context context;
    private ReservationsMainFragment mainFragment;
    private MainActivity fragmentActivity;
    private ArrayList<ReservationModel> inProgressDataSet;
    private LayoutInflater mInflater;
    private FirebaseFirestore db;
    private TabReservationsInProgress tabFrag;

    public InProgressReservationsListAdapter(Context context, ArrayList<ReservationModel> inProgressData,
                                             MainActivity fragmentActivity, TabReservationsInProgress tabFrag, ReservationsMainFragment mainFragment) {
        this.context = context;
        this.mainFragment = mainFragment;
        this.fragmentActivity = fragmentActivity;
        this.mInflater = LayoutInflater.from(context);
        this.inProgressDataSet = inProgressData;
        this.tabFrag = tabFrag;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public InProgressReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.in_progress_reservation_cardview, parent, false);
        InProgressReservationViewHolder holder = new InProgressReservationViewHolder(view);
       view.setOnClickListener(v -> {
           Intent myIntent = new Intent(mainFragment.getContext(), InProgressDetailsActivity.class);
           int itemPosition = holder.getAdapterPosition();
           Bundle bn = new Bundle();
           bn.putInt("reservationCardData", itemPosition);
           myIntent.putExtras(bn);
           mainFragment.startActivityForResult(myIntent, tabFrag.INPROGRESS_REQ);
      });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InProgressReservationViewHolder inProgressReservationViewHolder, int position) {
        TextView textViewOrderId = inProgressReservationViewHolder.textViewReservationId;
        TextView textViewTimestamp = inProgressReservationViewHolder.textViewTimestamp;
        TextView textViewTotalIncome = inProgressReservationViewHolder.textViewTotalIncome;
        TextView textViewOrderedFood = inProgressReservationViewHolder.textViewOrderedDishes;
        TextView textViewReservationNotes = inProgressReservationViewHolder.textViewReservationNotes;
        Button btnFinishReservation = inProgressReservationViewHolder.btnFinishtReservation;
//        Button btnRejectReservation = inProgressReservationViewHolder.btnRejectReservation;

        ReservationModel tmpRM = inProgressDataSet.get(position);
        textViewOrderId.setText("" + tmpRM.getRs_id());
        textViewTimestamp.setText("" + tmpRM.getTimestamp());
        textViewTotalIncome.setText("" + tmpRM.getTotal_income());
        String reservationOffer = "";
        for (int i = 0; i < tmpRM.getDishesArrayList().size(); i++) {
            String offerName = tmpRM.getDishesArrayList().get(i).getDishName();
            reservationOffer += offerName + "(" + tmpRM.getDishesArrayList().get(i).getDishQty() + ")  ";
        }
        textViewOrderedFood.setText(reservationOffer);
        if(!inProgressDataSet.get(position).getNotes().equals(""))
            textViewReservationNotes.setVisibility(View.VISIBLE);
        btnFinishReservation.setOnClickListener(v -> {
            int pos = inProgressReservationViewHolder.getAdapterPosition();
            inprogressFinish(pos);
        });
//        btnRejectReservation.setOnClickListener(v -> {
//            int pos = inProgressReservationViewHolder.getAdapterPosition();
//            fragmentActivity.removeItemFromInProgress(pos);//inProgressDataSet.remove(pos);
//            notifyItemRemoved(pos);
//            notifyItemRangeChanged(pos, inProgressDataSet.size());
//            tmpRM.setRs_status(ReservationState.STATE_FINISHED_REJECTED);
//            fragmentActivity.addItemToFinished(tmpRM);//finishedDataSet.add(tmpRM);
//        });
    }

    public void inprogressFinish(int pos){
        ReservationModel tmpRM = inProgressDataSet.get(pos);
        db.collection("reservations").document(tmpRM.getReservation_id()).update("rs_status", ReservationState.STATE_FINISHED_SUCCESS).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                fragmentActivity.removeItemFromInProgress(pos);//inProgressDataSet.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, inProgressDataSet.size());
                tmpRM.setRs_status(ReservationState.STATE_FINISHED_SUCCESS);
                fragmentActivity.addItemToFinished(tmpRM);//finishedDataSet.add(tmpRM);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inProgressDataSet.size();
    }

   static class InProgressReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewReservationId;
        TextView textViewTimestamp;
        TextView textViewTotalIncome;
        TextView textViewOrderedDishes;
        TextView textViewReservationNotes;
        Button btnFinishtReservation;
//        Button btnRejectReservation;

        InProgressReservationViewHolder(View itemView) {
            super(itemView);
            this.textViewReservationId = itemView.findViewById(R.id.textViewOrderIdReservationInProgress);
            this.textViewTimestamp = itemView.findViewById(R.id.textViewRemainingTimeReservationInProgress);
            this.textViewTotalIncome = itemView.findViewById(R.id.textViewTotalIncomeReservationInProgress);
            this.textViewOrderedDishes = itemView.findViewById(R.id.textViewFoodReservationInProgress);
            this.textViewReservationNotes = itemView.findViewById(R.id.textViewStateReservationInProgress);
            btnFinishtReservation = itemView.findViewById(R.id.buttonResumeReservationInProgress);
//            btnRejectReservation = itemView.findViewById(R.id.buttonRemoveReservationInProgress);

        }
    }
}
