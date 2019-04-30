package com.example.restaurateur.Reservation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

import java.util.Collections;

public class TabReservationsPending extends Fragment {

    private RecyclerView.Adapter pendingReservationsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_reservations, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.reservationsRecyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*recyclerView.setHasFixedSize(true);*/
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        pendingReservationsAdapter = new PendingReservationsListAdapter(getContext(),
                MainActivity.pendingReservationsData, MainActivity.offersData, (MainActivity)getActivity());
        recyclerView.setAdapter(pendingReservationsAdapter);

        return view;
    }

    public void sortDataAndNotify() {
        Collections.sort(MainActivity.pendingReservationsData);
        pendingReservationsAdapter.notifyDataSetChanged();
    }
}
