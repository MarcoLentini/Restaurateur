package com.example.restaurateur.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.restaurateur.R;
import com.example.restaurateur.Reservation.ReservationModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryOrdersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ReservationModel> historyOrders;
    private LayoutInflater mInflater;

    public HistoryOrdersListAdapter(Context context, ArrayList<ReservationModel> historyOrders) {
           this.context = context;
           this.historyOrders = historyOrders;
           this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.finished_reservation_cardview, viewGroup,false);
        HistoryOrdersViewHolder holder = new HistoryOrdersViewHolder(view);
        view.setOnClickListener(v -> {
            Intent myIntent = new Intent(context, HistoryOrdersDetalisActivity.class);
            int itemPosition = holder.getAdapterPosition();
            Bundle bn = new Bundle();
            bn.putInt("historyOrderData", itemPosition);
            myIntent.putExtras(bn);
            context.startActivity(myIntent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        HistoryOrdersViewHolder historyOrdersViewHolder = (HistoryOrdersViewHolder)viewHolder;
        TextView textViewOrderId = historyOrdersViewHolder.textViewReservationId;
        //TextView textViewTimestamp = historyOrdersViewHolder.textViewTimestamp;
        TextView textViewTotalIncome = historyOrdersViewHolder.textViewTotalIncome;
        TextView textViewOrderedFood = historyOrdersViewHolder.textViewOrderedDishes;
        TextView textViewDeliveryTime = historyOrdersViewHolder.textViewDeliveryTime;
        Button btnRemoveReservation = historyOrdersViewHolder.btnRemoveReservation;

        ReservationModel tmpRM = historyOrders.get(position);
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
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date=tmpRM.getTimestamp().toDate();
        textViewDeliveryTime.setText( dateFormat.format(date));


        btnRemoveReservation.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return historyOrders.size();
    }

    private class HistoryOrdersViewHolder extends RecyclerView.ViewHolder{
        TextView textViewReservationId;
        //TextView textViewTimestamp;
        TextView textViewTotalIncome;
        TextView textViewOrderedDishes;
        TextView textViewDeliveryTime;
        Button btnRemoveReservation;
        public HistoryOrdersViewHolder(View view) {
            super(view);
            this.textViewReservationId = itemView.findViewById(R.id.textViewOrderIdReservationFinished);
            //this.textViewTimestamp = itemView.findViewById(R.id.textViewRemainingTimeReservationFinished);
            this.textViewTotalIncome = itemView.findViewById(R.id.textViewTotalIncomeReservationFinished);
            this.textViewOrderedDishes = itemView.findViewById(R.id.textViewFoodReservationFinished);
            this.textViewDeliveryTime = itemView.findViewById(R.id.textViewDeliveryReservationFinished);
            this.btnRemoveReservation = itemView.findViewById(R.id.buttonRemoveReservationFinished);
        }
    }
}
