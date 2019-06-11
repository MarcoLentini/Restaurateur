package com.example.restaurateur.Reservation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;
import com.example.restaurateur.Home.RestaurantStatistics;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

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
        //TextView textViewTimestamp = inProgressReservationViewHolder.textViewTimestamp;
        TextView textViewTotalIncome = inProgressReservationViewHolder.textViewTotalIncome;
        TextView textViewOrderedFood = inProgressReservationViewHolder.textViewOrderedDishes;
        TextView textViewReservationNotes = inProgressReservationViewHolder.textViewReservationNotes;
        Button btnFinishReservation = inProgressReservationViewHolder.btnFinishtReservation;

        ReservationModel tmpRM = inProgressDataSet.get(position);
        textViewOrderId.setText("" + tmpRM.getRs_id());
        //textViewTimestamp.setText("" + tmpRM.getTimestamp());
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

        // Get a new write batch
        WriteBatch batch = db.batch();
        DocumentReference restDRef = db.collection("restaurant_statistics").document();

        for(ReservatedDish rd : tmpRM.getDishesArrayList()){
            RestaurantStatistics rs = new RestaurantStatistics("",
                tmpRM.getReservation_id(),
                fragmentActivity.restaurantKey,
                rd.getDishCategoryID(),
                rd.getDishName(),
                rd.getDishCategoryID()+rd.getDishName(),
                Timestamp.now(),
                rd.getDishQty(),
                rd.getDishPrice()
            );
            batch.set(restDRef,rs);
        }


        // Commit the batch
        batch.commit().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                // nothing to do
            } else {
                Log.d("RegisterRest", "Failed batch write");
            }
        });
    }

    @Override
    public int getItemCount() {
        return inProgressDataSet.size();
    }

   static class InProgressReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewReservationId;
        //TextView textViewTimestamp;
        TextView textViewTotalIncome;
        TextView textViewOrderedDishes;
        TextView textViewReservationNotes;
        Button btnFinishtReservation;

        InProgressReservationViewHolder(View itemView) {
            super(itemView);
            this.textViewReservationId = itemView.findViewById(R.id.textViewOrderIdReservationInProgress);
            //.textViewTimestamp = itemView.findViewById(R.id.textViewRemainingTimeReservationInProgress);
            this.textViewTotalIncome = itemView.findViewById(R.id.textViewTotalIncomeReservationInProgress);
            this.textViewOrderedDishes = itemView.findViewById(R.id.textViewFoodReservationInProgress);
            this.textViewReservationNotes = itemView.findViewById(R.id.textViewStateReservationInProgress);
            btnFinishtReservation = itemView.findViewById(R.id.buttonResumeReservationInProgress);
        }
    }
}
