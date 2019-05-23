package com.example.restaurateur.ChooseBiker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;
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


    public ChooseBikerAdapter(Context context, ArrayList<BikerModel> bikerData, MainActivity main) {
        this.context = context;
        this.main = main;
        this.mInflater = LayoutInflater.from(context);
        this.bikerData = bikerData;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ChooseBikerAdapter.ChooseBikerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.choose_biker_cardview, parent, false);

        ChooseBikerAdapter.ChooseBikerAdapterViewHolder holder = new ChooseBikerAdapter.ChooseBikerAdapterViewHolder(view);
        view.setOnClickListener(v -> {
            // Todo - Set biker into Firebase and continue reservation workflow
            Toast.makeText(context, "Hello2", Toast.LENGTH_LONG).show();
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseBikerAdapter.ChooseBikerAdapterViewHolder chooseBikerAdapterViewHolder, int position) {
        ImageView imgBiker = chooseBikerAdapterViewHolder.biker_img_card;
        TextView nameBiker = chooseBikerAdapterViewHolder.biker_name_card;
        TextView distBiker = chooseBikerAdapterViewHolder.biker_dist_card;

        BikerModel tmpB = bikerData.get(position);
        Glide.with(context).load(tmpB.getBikerImageUrl()).placeholder(R.drawable.img_rest_1).into(imgBiker);
        nameBiker.setText("" + tmpB.getBikerName());
        distBiker.setText("" + tmpB.getBikerDist());

    }

    @Override
    public int getItemCount() {
        return bikerData.size();
    }

    static class ChooseBikerAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView biker_img_card;
        TextView biker_name_card;
        TextView biker_dist_card;

        ChooseBikerAdapterViewHolder(View itemView) {
            super(itemView);
            this.biker_img_card = itemView.findViewById(R.id.biker_img_card);
            this.biker_name_card = itemView.findViewById(R.id.biker_name_card);
            this.biker_dist_card = itemView.findViewById(R.id.biker_dist_card);
        }
    }
}