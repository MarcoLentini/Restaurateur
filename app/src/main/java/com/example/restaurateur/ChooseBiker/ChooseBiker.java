package com.example.restaurateur.ChooseBiker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;
import com.example.restaurateur.Reservation.ReservationsMainFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import static android.content.Context.LOCATION_SERVICE;

public class ChooseBiker extends DialogFragment {
    private RecyclerView mRecyclerView;

    private Top3Biker dataB;

    private ChooseBikerAdapter adapter;
    private ReservationsMainFragment mainFragment;

    public static ChooseBiker newInstance(ReservationsMainFragment mainFragment, Top3Biker dataB, int pos) {
        ChooseBiker choose = new ChooseBiker();
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        choose.setArguments(b);
        choose.setDataB(dataB);
        choose.setMainFragment(mainFragment);
        return choose;
    }

    public void setMainFragment(ReservationsMainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    public void setDataB(Top3Biker dataB) {
        this.dataB = dataB;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        int pos = getArguments().getInt("pos");
        mRecyclerView = new RecyclerView(getContext());
        // you can use LayoutInflater.from(getContext()).inflate(...) if you have xml layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChooseBikerAdapter(this.getContext(),dataB.getDataB(),(MainActivity)getActivity(), pos, mainFragment,this);
        mRecyclerView.setAdapter(adapter);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Choose Biker")
                .setView(mRecyclerView)
                .create();
    }
}