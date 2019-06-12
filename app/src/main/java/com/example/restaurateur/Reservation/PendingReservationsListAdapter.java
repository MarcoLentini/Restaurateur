package com.example.restaurateur.Reservation;import android.Manifest;import android.content.Context;import android.content.Intent;import android.content.pm.PackageManager;import android.location.LocationManager;import android.os.Bundle;import android.os.Handler;import android.os.Message;import android.support.annotation.NonNull;import android.support.design.widget.Snackbar;import android.support.v4.app.ActivityCompat;import android.support.v4.app.DialogFragment;import android.support.v4.app.Fragment;import android.support.v4.content.ContextCompat;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.Button;import android.widget.ProgressBar;import android.widget.TextView;import android.widget.Toast;import com.example.restaurateur.ChooseBiker.BikerModel;import com.example.restaurateur.ChooseBiker.ChooseBiker;import com.example.restaurateur.ChooseBiker.GeocodingLocation;import com.example.restaurateur.ChooseBiker.Top3Biker;import com.example.restaurateur.MainActivity;import com.example.restaurateur.Offer.OfferModel;import com.example.restaurateur.R;import com.google.firebase.firestore.DocumentSnapshot;import com.google.firebase.firestore.FirebaseFirestore;import com.google.firebase.firestore.GeoPoint;import java.text.DecimalFormat;import java.util.ArrayList;import java.util.HashMap;import java.util.concurrent.CompletableFuture;import static android.content.Context.LOCATION_SERVICE;public class PendingReservationsListAdapter extends RecyclerView.Adapter<PendingReservationsListAdapter.PendingReservationViewHolder> {    private static final int PERMISSIONS_REQUEST = 100;    private Top3Biker dataB;    private ProgressBar progressBar;    private ReservationsMainFragment mainFragment;    private MainActivity fragmentActivity;    private ArrayList<ReservationModel> pendingDataSet;    private LayoutInflater mInflater;    private FirebaseFirestore db;    private TabReservationsPending tabFrag;    private int position;    public PendingReservationsListAdapter(Context context, ArrayList<ReservationModel> pendingData,                                          MainActivity fragmentActivity, TabReservationsPending tabFrag, ReservationsMainFragment mainFragment) {        this.mainFragment = mainFragment;        this.fragmentActivity = fragmentActivity;        this.mInflater = LayoutInflater.from(context);        this.pendingDataSet = pendingData;        this.tabFrag = tabFrag;        this.db = FirebaseFirestore.getInstance();    }    @NonNull    @Override    public PendingReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {        View view = mInflater.inflate(R.layout.pending_reservation_cardview, parent, false);        PendingReservationViewHolder holder = new PendingReservationViewHolder(view);        view.setOnClickListener(v -> {            Intent myIntent = new Intent(mainFragment.getContext(), PendingDetailsActivity.class);            int itemPosition = holder.getAdapterPosition();            Bundle bn = new Bundle();            bn.putInt("reservationCardData", itemPosition);            myIntent.putExtras(bn);            mainFragment.startActivityForResult(myIntent, tabFrag.PENDING_REQ);        });        return holder;    }    @Override    public void onBindViewHolder(@NonNull PendingReservationViewHolder pendingReservationViewHolder, int position) {        TextView textViewOrderId = pendingReservationViewHolder.textViewReservationId;        //TextView textViewTimestamp = pendingReservationViewHolder.textViewTimestamp;        TextView textViewTotalIncome = pendingReservationViewHolder.textViewTotalIncome;        TextView textViewOrderedFood = pendingReservationViewHolder.textViewOrderedDishes;        TextView textViewReservationNotes = pendingReservationViewHolder.textViewReservationNotes;        Button btnAcceptReservation = pendingReservationViewHolder.btnAcceptReservation;        Button btnRejectReservation = pendingReservationViewHolder.btnRejectReservation;        ReservationModel tmpRM = pendingDataSet.get(position);        textViewOrderId.setText(String.valueOf(tmpRM.getRs_id()));        //textViewTimestamp.setText(String.valueOf(tmpRM.getTimestamp()));        DecimalFormat format = new DecimalFormat("0.00");        String formattedIncome = format.format((tmpRM.getTotal_income()-tmpRM.getDelivery_fee()));        textViewTotalIncome.setText(formattedIncome);        String reservationOffer = "";        for (int i = 0; i < tmpRM.getDishesArrayList().size(); i++) {            String offerName = "▶" + tmpRM.getDishesArrayList().get(i).getDishName();            reservationOffer += offerName + "(" + tmpRM.getDishesArrayList().get(i).getDishQty() + ")  ";        }        textViewOrderedFood.setText(reservationOffer);        if(!tmpRM.getNotes().equals(""))            textViewReservationNotes.setVisibility(View.VISIBLE);        btnAcceptReservation.setOnClickListener(v -> {            int pos = pendingReservationViewHolder.getAdapterPosition();            pendingAccept(pos);        });        btnRejectReservation.setOnClickListener(v -> {            int pos = pendingReservationViewHolder.getAdapterPosition();            pendingReject(pos);        });    }    public void pendingAccept(int pos){        position = pos;        progressBar = fragmentActivity.findViewById(R.id.load_biker);        int res = check_GPS();        if(res == 0){            progressBar.setVisibility(View.VISIBLE);            dataB = new Top3Biker();            CompletableFuture<GeoPoint> completableFuture = null;            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {                completableFuture = new CompletableFuture<>();            }            GeocodingLocation locationAddress = new GeocodingLocation();            locationAddress.getAddressFromLocation(pendingDataSet.get(pos).getRest_address(),                    fragmentActivity, new PendingReservationsListAdapter.GeocoderHandler(), completableFuture);        }    }    public void pendingReject(int pos){        ReservationModel tmpRM = pendingDataSet.get(pos);        db.collection("reservations").document(tmpRM.getReservation_id()).update("rs_status", ReservationState.STATE_FINISHED_REJECTED).addOnCompleteListener(task -> {            if(task.isSuccessful()) {                fragmentActivity.removeItemFromPending(pos);//pendingDataSet.remove(pos);                notifyItemRemoved(pos);                notifyItemRangeChanged(pos, pendingDataSet.size());                tmpRM.setRs_status(ReservationState.STATE_FINISHED_REJECTED);                fragmentActivity.addItemToFinished(tmpRM);//finishedDataSet.add(tmpRM);                mainFragment.decrementPendingReservationsNumber();            }        });    }    private int check_GPS(){        LocationManager lm = (LocationManager) fragmentActivity.getSystemService(LOCATION_SERVICE);        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){            Snackbar.make(fragmentActivity.findViewById(R.id.frame_container_main), "Please active GPS!",                    Snackbar.LENGTH_LONG).show();//            Toast.makeText(getContext(), "Please active GPS!", Toast.LENGTH_LONG).show();            return 1;        }        //Check whether this app has access to the location permission//        int permission = ContextCompat.checkSelfPermission(fragmentActivity, Manifest.permission.ACCESS_FINE_LOCATION);        //If the location permission has been granted, then start the TrackerService//        if (permission == PackageManager.PERMISSION_GRANTED) {            return 0;        } else {            //If the app doesn’t currently have access to the user’s location, then request access//            ActivityCompat.requestPermissions(mainFragment.getActivity(),                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},                    PERMISSIONS_REQUEST);            return 2;        }    }    @Override    public int getItemCount() {        return pendingDataSet.size();    }   static class PendingReservationViewHolder extends RecyclerView.ViewHolder {        TextView textViewReservationId;        //TextView textViewTimestamp;        TextView textViewTotalIncome;        TextView textViewOrderedDishes;        TextView textViewReservationNotes;        Button btnAcceptReservation;        Button btnRejectReservation;        PendingReservationViewHolder(View itemView) {            super(itemView);            this.textViewReservationId = itemView.findViewById(R.id.textViewOrderIdReservationPending);            //this.textViewTimestamp = itemView.findViewById(R.id.textViewRemainingTimeReservationPending);            this.textViewTotalIncome = itemView.findViewById(R.id.textViewTotalIncomeReservationPending);            this.textViewOrderedDishes = itemView.findViewById(R.id.textViewFoodReservationPending);            this.textViewReservationNotes = itemView.findViewById(R.id.textViewStateReservationPending);            btnAcceptReservation = itemView.findViewById(R.id.buttonResumeReservationPending);            btnRejectReservation = itemView.findViewById(R.id.buttonRemoveReservationPending);        }    }    private void getBiker(GeoPoint restPosiion){        FirebaseFirestore db = FirebaseFirestore.getInstance();        db.collection("bikers").whereEqualTo("status", true).whereEqualTo("free", true).get().addOnCompleteListener(t -> {            if(t.isSuccessful()){                progressBar.setVisibility(View.GONE);                if(!t.getResult().isEmpty()){                    for(DocumentSnapshot bikerDoc : t.getResult()){                        db.collection("users").document( bikerDoc.getString("user_id")).get().addOnCompleteListener(task -> {                            if(task.isSuccessful()){                                DocumentSnapshot doc = task.getResult();                                BikerModel b = new BikerModel(                                        bikerDoc.getId(),                                         doc.getString("image_url"),                                         doc.getString("username"),                                         bikerDoc.getGeoPoint("position"),                                        restPosiion                                );                                dataB.addBiker(b);                                ChooseBiker choose = ChooseBiker.newInstance(mainFragment, dataB, position);                                choose.show(fragmentActivity.getSupportFragmentManager(),"dialog");                            }                        });                    }                } else {                    Snackbar.make(fragmentActivity.findViewById(R.id.frame_container_main), "There aren't available bikers, retry!",                            Snackbar.LENGTH_LONG).show();                }            }        });    }    private class GeocoderHandler extends Handler {        @Override        public void handleMessage(Message message) {            String locationAddress;            switch (message.what) {                case 1:                    Bundle bundle = message.getData();                    locationAddress = bundle.getString("address");                    break;                default:                    locationAddress = null;            }            if(locationAddress != null){                String lat = locationAddress.split(",")[0];                String lon = locationAddress.split(",")[1];                getBiker(new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon)));            }        }    }}