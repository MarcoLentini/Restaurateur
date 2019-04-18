package com.example.restaurateur.Reservation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

import java.util.Collections;

public class TabReservationsFinished extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter finishedReservationsAdapter;
    private MainActivity reservationsActivity = (MainActivity) getActivity();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_reservations, container, false);
        recyclerView = view.findViewById(R.id.reservationsRecyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        /*recyclerView.setHasFixedSize(true);*/
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // specify an Adapter
        finishedReservationsAdapter = new FinishedReservationsListAdapter(getContext(), reservationsActivity.finishedReservationsData,
                reservationsActivity.offersData, (MainActivity)getActivity()); // getContext() forse non va bene
        recyclerView.setAdapter(finishedReservationsAdapter);

        return view;
    }

    public void sortDataAndNotify() {
        Collections.sort(reservationsActivity.finishedReservationsData);
        finishedReservationsAdapter.notifyDataSetChanged();
    }
}

