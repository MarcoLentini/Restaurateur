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

import java.util.ArrayList;
import java.util.HashMap;

public class FinishedReservationsListAdapter extends RecyclerView.Adapter<FinishedReservationsListAdapter.FinishedReservationViewHolder> {

    private Context context;
    private MainActivity fragmentActivity;
    private ArrayList<ReservationModel> finishedDataSet;
    private LayoutInflater mInflater;
    private HashMap<String, OfferModel> offersData;

    public FinishedReservationsListAdapter(Context context, ArrayList<ReservationModel> finishedData,
                                           MainActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.mInflater = LayoutInflater.from(context);
        this.finishedDataSet = finishedData;
    }

    @NonNull
    @Override
    public FinishedReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.finished_reservation_cardview, parent, false);

        FinishedReservationViewHolder holder = new FinishedReservationViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent myIntent = new Intent(fragmentActivity, FinishedDetailsActivity.class);
               int itemPosition = holder.getAdapterPosition();
               ReservationModel selectedRm = finishedDataSet.get(itemPosition);
               Bundle bn = new Bundle();
               bn.putSerializable("reservationCardData", selectedRm);
               myIntent.putExtras(bn);
               fragmentActivity.startActivity(myIntent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedReservationViewHolder finishedReservationViewHolder, int position) {
        TextView textViewOrderId = finishedReservationViewHolder.textViewReservationId;
        TextView textViewTimestamp = finishedReservationViewHolder.textViewTimestamp;
        TextView textViewTotalIncome = finishedReservationViewHolder.textViewTotalIncome;
        TextView textViewOrderedFood = finishedReservationViewHolder.textViewOrderedDishes;
        TextView textViewReservationState = finishedReservationViewHolder.textViewReservationState;
        Button btnResumeReservation = finishedReservationViewHolder.btnResumeReservation;
        Button btnRemoveReservation = finishedReservationViewHolder.btnRemoveReservation;

        ReservationModel tmpRM = finishedDataSet.get(position);
        textViewOrderId.setText("" + tmpRM.getRs_id());
        textViewTimestamp.setText("" + tmpRM.getTimestamp());
        textViewTotalIncome.setText("" + tmpRM.getTotal_income());
        String reservationOffer = "";
        for (int i = 0; i < tmpRM.getDishesArrayList().size(); i++) {
            String offerName = tmpRM.getDishesArrayList().get(i).getDishName();
            reservationOffer += offerName + "(" + tmpRM.getDishesArrayList().get(i).getDishQty() + ")  ";
        }
        textViewOrderedFood.setText(reservationOffer);
        textViewReservationState.setText(tmpRM.getRs_status());
        btnResumeReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = finishedReservationViewHolder.getAdapterPosition();
                fragmentActivity.removeItemFromFinished(pos);//finishedDataSet.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, finishedDataSet.size());
                tmpRM.setRs_status(ReservationState.STATE_IN_PROGRESS);
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

        /*finishedReservationViewHolder.itemView.setOnClickListener(v -> {
            FinishedDetailsFragment finishedDetailsFragment = FinishedDetailsFragment.newInstance(tmpRM, position);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishedDetailsFragment.setSharedElementEnterTransition(new DetailsTransition());
                finishedDetailsFragment.setEnterTransition(new Fade());
                finishedDetailsFragment.setExitTransition(new Fade());
                finishedDetailsFragment.setSharedElementReturnTransition(new DetailsTransition());
            }

            ViewCompat.setTransitionName(textViewOrderedFood,"lessDetailsFinish");
            fragmentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(textViewOrderedFood,
                            "seeDetailsFinish")
                    .replace(R.id.frame_container_main, finishedDetailsFragment)
                    .addToBackStack(null)
                    .commit();
        });*/
    }

    @Override
    public int getItemCount() {
        return finishedDataSet.size();
    }

    static class FinishedReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewReservationId;
        TextView textViewTimestamp;
        TextView textViewTotalIncome;
        TextView textViewOrderedDishes;
        TextView textViewReservationState;
        Button btnResumeReservation;
        Button btnRemoveReservation;

        FinishedReservationViewHolder(View itemView) {
            super(itemView);
            this.textViewReservationId = itemView.findViewById(R.id.textViewOrderIdReservationFinished);
            this.textViewTimestamp = itemView.findViewById(R.id.textViewRemainingTimeReservationFinished);
            this.textViewTotalIncome = itemView.findViewById(R.id.textViewTotalIncomeReservationFinished);
            this.textViewOrderedDishes = itemView.findViewById(R.id.textViewFoodReservationFinished);
            this.textViewReservationState = itemView.findViewById(R.id.textViewStateReservationFinished);
            this.btnResumeReservation = itemView.findViewById(R.id.buttonResumeReservationFinished);
            this.btnRemoveReservation = itemView.findViewById(R.id.buttonRemoveReservationFinished);
        }
    }
}
