package com.example.restaurateur.Reservation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.transition.Fade;
import android.support.v4.app.FragmentActivity;
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
import com.example.restaurateur.UserInformationActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class InProgressReservationsListAdapter extends RecyclerView.Adapter<InProgressReservationsListAdapter.InProgressReservationViewHolder> {

    private Context context;
    private MainActivity fragmentActivity;
    private ArrayList<ReservationModel> inProgressDataSet;
    private LayoutInflater mInflater;
    private HashMap<Integer, OfferModel> offersData;

    public InProgressReservationsListAdapter(Context context, ArrayList<ReservationModel> inProgressData,
                                             HashMap<Integer, OfferModel> offersData, MainActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.mInflater = LayoutInflater.from(context);
        this.inProgressDataSet = inProgressData;
        this.offersData = offersData;
    }

    @NonNull
    @Override
    public InProgressReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.in_progress_reservation_cardview, parent, false);

 //       view.setOnClickListener(new View.OnClickListener() {
 //           @Override
 //           public void onClick(View v) {
 //               Intent myIntent = new Intent(context.getApplicationContext(), UserInformationActivity.class);
 //               context.startActivity(myIntent);
  //          }
  //      });

        return new InProgressReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InProgressReservationViewHolder inProgressReservationViewHolder, int position) {
        //TextView textViewOrderIdReservation = inProgressReservationViewHolder.textViewOrderIdReservation;

        TextView textViewOrderId = inProgressReservationViewHolder.textViewReservationId;
        TextView textViewRemainingTime = inProgressReservationViewHolder.textViewRemainingTime;
        TextView textViewTotalIncome = inProgressReservationViewHolder.textViewTotalIncome;
        TextView textViewOrderedFood = inProgressReservationViewHolder.textViewOrderedDishes;
        TextView textViewReservationNotes = inProgressReservationViewHolder.textViewReservationNotes;
        Button btnFinishReservation = inProgressReservationViewHolder.btnFinishtReservation;
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
        btnFinishReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = inProgressReservationViewHolder.getAdapterPosition();
                fragmentActivity.removeItemFromInProgress(pos);//inProgressDataSet.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, inProgressDataSet.size());
                tmpRM.setState(ReservationState.STATE_FINISHED_SUCCESS);
                fragmentActivity.addItemToFinished(tmpRM);//finishedDataSet.add(tmpRM);
            }
        });
        btnRejectReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = inProgressReservationViewHolder.getAdapterPosition();
                fragmentActivity.removeItemFromInProgress(pos);//inProgressDataSet.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, inProgressDataSet.size());
                tmpRM.setState(ReservationState.STATE_FINISHED_REJECTED);
                fragmentActivity.addItemToFinished(tmpRM);//finishedDataSet.add(tmpRM);
            }
        });
/*
        inProgressReservationViewHolder.itemView.setOnClickListener(v -> {
            DetailsFragment detailsFragment = DetailsFragment.newInstance(tmpRM);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                detailsFragment.setSharedElementEnterTransition(new DetailsTransition());
                detailsFragment.setEnterTransition(new Fade());
                detailsFragment.setExitTransition(new Fade());
                detailsFragment.setSharedElementReturnTransition(new DetailsTransition());
            }

            ViewCompat.setTransitionName(textViewOrderIdReservation,"lessDetails");
            fragmentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(textViewOrderIdReservation,
                            "seeDetails")
                    .replace(R.id.frame_container_reservations, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });

*/
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
