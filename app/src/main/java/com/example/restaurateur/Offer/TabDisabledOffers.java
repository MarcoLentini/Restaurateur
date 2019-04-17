package com.example.restaurateur.Offer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurateur.R;

public class TabDisabledOffers extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tab_doffers, container, false);
        loadFragment(new TabCategoryDisabledOffers(),"disabled");
        return view;
    }

    private void loadFragment(android.support.v4.app.Fragment fragment,String type) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("Type", type);
    // set Fragmentclass Arguments
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame_container_disabled_offers, fragment);
        transaction.commit();
    }
}