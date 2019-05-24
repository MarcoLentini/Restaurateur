package com.example.restaurateur.ChooseBiker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.Reservation.ReservationsMainFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class ChooseBiker extends DialogFragment {
    private RecyclerView mRecyclerView;

    private Top3Biker dataB;

    private ChooseBikerAdapter adapter;
    private ReservationsMainFragment mainFragment;

    public static ChooseBiker newInstance(ReservationsMainFragment mainFragment, String restAddr, int pos) {
        ChooseBiker choose = new ChooseBiker();
        Bundle b = new Bundle();
        b.putString("restAddress", restAddr );
        b.putInt("pos", pos);
        choose.setArguments(b);
        choose.setMainFragment(mainFragment);
        return choose;
    }

    public void setMainFragment(ReservationsMainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        dataB = new Top3Biker();

        String restAddr = getArguments().getString("restAddress");
        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(restAddr,
                getContext(), new GeocoderHandler());

        int pos = getArguments().getInt("pos");
        mRecyclerView = new RecyclerView(getContext());
        // you can use LayoutInflater.from(getContext()).inflate(...) if you have xml layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChooseBikerAdapter(getContext(),dataB.getDataB(),(MainActivity)getActivity(), pos, mainFragment,this);
        mRecyclerView.setAdapter(adapter);

        return new AlertDialog.Builder(getActivity())
                // Todo - move this into strings.xml
                .setTitle("Choose Biker")
                .setView(mRecyclerView)
                // Todo - remove this?
//                .setPositiveButton(android.R.string.ok,
//                        (dialog, whichButton) -> {
//                            // do something
//                            Toast.makeText(getContext(), "Hello", Toast.LENGTH_LONG).show();
//                        }
//                )
                .create();
    }

    private void getBiker(GeoPoint restPosiion){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bikers").whereEqualTo("status", true).get().addOnCompleteListener(t -> {
            if(t.isSuccessful()){
                if(!t.getResult().isEmpty()){
                    for(DocumentSnapshot bikerDoc : t.getResult()){
                        db.collection("users").document((String) bikerDoc.get("user_id")).get().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                BikerModel b = new BikerModel(
                                        bikerDoc.getId(),
                                        (String) doc.get("image_url"),
                                        (String) doc.get("username"),
                                        (GeoPoint) bikerDoc.get("position"),
                                        restPosiion
                                );

                                dataB.addBiker(b);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            if(locationAddress != null){
                String lat = locationAddress.split(",")[0];
                String lon = locationAddress.split(",")[1];

                getBiker(new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon)));
            }

        }
    }
}