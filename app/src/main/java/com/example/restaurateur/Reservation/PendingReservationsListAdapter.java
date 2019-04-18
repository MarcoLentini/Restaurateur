package com.example.restaurateur.Reservation;import android.content.Context;import android.content.Intent;import android.os.Build;import android.os.Bundle;import android.support.annotation.NonNull;import android.support.transition.Fade;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentActivity;import android.support.v4.app.FragmentTransaction;import android.support.v4.view.ViewCompat;import android.support.v7.widget.CardView;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.Button;import android.widget.TextView;import com.example.restaurateur.Offer.OfferModel;import com.example.restaurateur.R;import java.util.ArrayList;import java.util.HashMap;public class PendingReservationsListAdapter extends RecyclerView.Adapter<PendingReservationsListAdapter.PendingReservationViewHolder> {    private Context context;    private FragmentActivity fragmentActivity;    private ArrayList<ReservationModel> pendingDataSet;    private ArrayList<ReservationModel> inProgressDataSet;    private ArrayList<ReservationModel> finishedDataSet;    private LayoutInflater mInflater;    private HashMap<Integer, OfferModel> offersData;    public PendingReservationsListAdapter(Context context, ArrayList<ReservationModel> pendingData, ArrayList<ReservationModel> inProgressData,                                          ArrayList<ReservationModel> finishedData, HashMap<Integer, OfferModel> offersData, FragmentActivity fragmentActivity) {        this.context = context;        this.fragmentActivity = fragmentActivity;        this.mInflater = LayoutInflater.from(context);        this.pendingDataSet = pendingData;        this.inProgressDataSet = inProgressData;        this.finishedDataSet = finishedData;        this.offersData = offersData;    }    @NonNull    @Override    public PendingReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {        View view = mInflater.inflate(R.layout.pending_reservation_cardview, parent, false);//        view.setOnClickListener(new View.OnClickListener() {//            @Override//            public void onClick(View v) {//////                //Intent myIntent = new Intent(context.getApplicationContext(), UserInformationActivity.class);//                //context.startActivity(myIntent);//            }//        });        return new PendingReservationViewHolder(view);    }    @Override    public void onBindViewHolder(@NonNull PendingReservationViewHolder pendingReservationViewHolder, int position) {        TextView textViewOrderIdReservation = pendingReservationViewHolder.textViewOrderIdReservation;        TextView textViewOrderId = pendingReservationViewHolder.textViewReservationId;        TextView textViewRemainingTime = pendingReservationViewHolder.textViewRemainingTime;        TextView textViewTotalIncome = pendingReservationViewHolder.textViewTotalIncome;        TextView textViewOrderedFood = pendingReservationViewHolder.textViewOrderedDishes;        TextView textViewReservationNotes = pendingReservationViewHolder.textViewReservationNotes;        Button btnAcceptReservation = pendingReservationViewHolder.btnAcceptReservation;        Button btnRejectReservation = pendingReservationViewHolder.btnRejectReservation;        ReservationModel tmpRM = pendingDataSet.get(position);        textViewOrderId.setText("" + tmpRM.getId());        textViewRemainingTime.setText("" + tmpRM.getRemainingMinutes() + " min");        textViewTotalIncome.setText("" + tmpRM.getTotalIncome());        String reservationOffer = "";        for (int i = 0; i < tmpRM.getReservatedDishes().size(); i++) {            String offerName = "▶" + offersData.get(tmpRM.getReservatedDishes().get(i).getDishId()).getName();            reservationOffer += offerName + "(" + tmpRM.getReservatedDishes().get(i).getDishMultiplier() + ")  ";        }        textViewOrderedFood.setText(reservationOffer);        if(!tmpRM.getNotes().equals(""))            textViewReservationNotes.setVisibility(View.VISIBLE);        btnAcceptReservation.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                int pos = pendingReservationViewHolder.getAdapterPosition();                pendingDataSet.remove(pos);                notifyItemRemoved(pos);                notifyItemRangeChanged(pos, pendingDataSet.size());                // TODO change the state of the reservation                inProgressDataSet.add(tmpRM);            }        });        btnRejectReservation.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                int pos = pendingReservationViewHolder.getAdapterPosition();                pendingDataSet.remove(pos);                notifyItemRemoved(pos);                notifyItemRangeChanged(pos, pendingDataSet.size());                // TODO change the state of the reservation                finishedDataSet.add(tmpRM);            }        });        pendingReservationViewHolder.itemView.setOnClickListener(v -> {            DetailsFragment detailsFragment = DetailsFragment.newInstance(tmpRM);            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {                detailsFragment.setSharedElementEnterTransition(new DetailsTransition());                detailsFragment.setEnterTransition(new Fade());                detailsFragment.setExitTransition(new Fade());                detailsFragment.setSharedElementReturnTransition(new DetailsTransition());            }            ViewCompat.setTransitionName(textViewOrderIdReservation,"lessDetails");            fragmentActivity.getSupportFragmentManager()                    .beginTransaction()                    .addSharedElement(textViewOrderIdReservation,                            "seeDetails")                    .replace(R.id.frame_container_reservations, detailsFragment)                    .addToBackStack(null)                    .commit();        });    }    @Override    public int getItemCount() {        return pendingDataSet.size();    }   static class PendingReservationViewHolder extends RecyclerView.ViewHolder {        TextView textViewReservationId;        TextView textViewRemainingTime;        TextView textViewTotalIncome;        TextView textViewOrderedDishes;        TextView textViewReservationNotes;        Button btnAcceptReservation;        Button btnRejectReservation;        TextView textViewOrderIdReservation;        PendingReservationViewHolder(View itemView) {            super(itemView);            this.textViewReservationId = itemView.findViewById(R.id.textViewOrderIdReservation);            this.textViewRemainingTime = itemView.findViewById(R.id.textViewRemainingTimeReservation);            this.textViewTotalIncome = itemView.findViewById(R.id.textViewTotalIncomeReservation);            this.textViewOrderedDishes = itemView.findViewById(R.id.textViewFoodReservation);            this.textViewReservationNotes = itemView.findViewById(R.id.textViewStateReservation);            btnAcceptReservation = itemView.findViewById(R.id.buttonResumeReservation);            btnRejectReservation = itemView.findViewById(R.id.buttonRemoveReservation);            this.textViewOrderIdReservation = itemView.findViewById(R.id.textViewOrderIdReservation);        }    }}