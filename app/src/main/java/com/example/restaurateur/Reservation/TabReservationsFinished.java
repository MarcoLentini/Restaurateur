package com.example.restaurateur.Reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

import java.util.Collections;

import static android.app.Activity.RESULT_OK;

public class TabReservationsFinished extends Fragment implements Updateable {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FinishedReservationsListAdapter finishedReservationsAdapter;
    public static final int FINISHED_REQ = 57;


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
        finishedReservationsAdapter = new FinishedReservationsListAdapter(getContext(),
                MainActivity.finishedReservationsData, (MainActivity)getActivity(), this, (ReservationsMainFragment)getParentFragment());
        recyclerView.setAdapter(finishedReservationsAdapter);
        sortDataAndNotify();
        Log.d("TabResPen","onCreateView()");

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == FINISHED_REQ){
                int pos = data.getExtras().getInt("pos");
                if(data.getExtras().getString("result").equals("Resume")){
                    finishedReservationsAdapter.finishedResume(pos);
                } else {
                    finishedReservationsAdapter.finishedReject(pos);
                }
            }
        }
    }

    private void sortDataAndNotify() {
        Collections.sort(MainActivity.finishedReservationsData);
        if(finishedReservationsAdapter != null)
            finishedReservationsAdapter.notifyDataSetChanged();
    }

    @Override
    public void update() {
        sortDataAndNotify();
    }
}

