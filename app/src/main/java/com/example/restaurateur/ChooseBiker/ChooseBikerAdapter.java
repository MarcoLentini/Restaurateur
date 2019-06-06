package com.example.restaurateur.ChooseBiker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;
import com.example.restaurateur.Reservation.ReservationModel;
import com.example.restaurateur.Reservation.ReservationState;
import com.example.restaurateur.Reservation.ReservationsMainFragment;
import com.example.restaurateur.Reservation.TabReservationsFinished;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ChooseBikerAdapter extends RecyclerView.Adapter<ChooseBikerAdapter.ChooseBikerAdapterViewHolder> {


    private Context context;
    private MainActivity main;
    private ArrayList<BikerModel> bikerData;
    private LayoutInflater mInflater;
    private ReservationsMainFragment mainFragment;
    private TabReservationsFinished tabFrag;
    private FirebaseFirestore db;
    private int orderPosition;
    private DialogFragment dialog;


    public ChooseBikerAdapter(Context context, ArrayList<BikerModel> bikerData, MainActivity main, int orderPosition, ReservationsMainFragment mainFragment, DialogFragment dialog ) {
        this.context = context;
        this.main = main;
        this.mInflater = LayoutInflater.from(context);
        this.bikerData = bikerData;
        this.db = FirebaseFirestore.getInstance();
        this.orderPosition = orderPosition;
        this.dialog = dialog;
        this.mainFragment = mainFragment;
    }

    @NonNull
    @Override
    public ChooseBikerAdapter.ChooseBikerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.choose_biker_cardview, parent, false);

        ChooseBikerAdapter.ChooseBikerAdapterViewHolder holder = new ChooseBikerAdapter.ChooseBikerAdapterViewHolder(view);
        view.setOnClickListener(v -> {

        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseBikerAdapter.ChooseBikerAdapterViewHolder chooseBikerAdapterViewHolder, int position) {
        ImageView imgBiker = chooseBikerAdapterViewHolder.biker_img_card;
        TextView nameBiker = chooseBikerAdapterViewHolder.biker_name_card;
        TextView distBiker = chooseBikerAdapterViewHolder.biker_dist_card;

        LinearLayout card = chooseBikerAdapterViewHolder.biker_card;

        BikerModel tmpB = bikerData.get(position);
        Glide.with(context).load(tmpB.getBikerImageUrl()).placeholder(R.drawable.img_rest_1).into(imgBiker);
        nameBiker.setText("" + tmpB.getBikerName());
        distBiker.setText("" + tmpB.getBikerDist());

        card.setOnClickListener(v->{
            ReservationModel tmpRM = MainActivity.pendingReservationsData.get(orderPosition);
            db.collection("reservations").document(tmpRM.getReservation_id()).update(
                    "rs_status", ReservationState.STATE_IN_PROGRESS,
                    "biker_id", tmpB.getBikerID(),
                    "biker_check", false
                    ).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    main.removeItemFromPending(orderPosition);//pendingDataSet.remove(pos);
                    mainFragment.pageAdapter.notifyDataSetChanged();
                    tmpRM.setRs_status(ReservationState.STATE_IN_PROGRESS);
                    main.addItemToInProgress(tmpRM);//inProgressDataSet.add(tmpRM);
                    mainFragment.decrementPendingReservationsNumber();

                    // TODO - animation that move to in_progress tab

                }
            });
        });



    }

    @Override
    public int getItemCount() {
        return bikerData.size();
    }

    static class ChooseBikerAdapterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout biker_card;
        ImageView biker_img_card;
        TextView biker_name_card;
        TextView biker_dist_card;

        ChooseBikerAdapterViewHolder(View itemView) {
            super(itemView);
            this.biker_card = itemView.findViewById(R.id.biker_card);
            this.biker_img_card = itemView.findViewById(R.id.biker_img_card);
            this.biker_name_card = itemView.findViewById(R.id.biker_name_card);
            this.biker_dist_card = itemView.findViewById(R.id.biker_dist_card);
        }
    }
}