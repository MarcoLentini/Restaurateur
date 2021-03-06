package com.example.restaurateur.Reservation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurateur.R;

public class ReservationsMainFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageReservations pageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations_f, container, false);

        //Initializing the tablayout
        tabLayout = view.findViewById(R.id.tabLayout_reservations);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(R.string.pending_label));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.accepted_label));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.refused_label));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = view.findViewById(R.id.pager_reservations);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Creating our pager pageAdapter(a ViewPager has an associated pageAdapter)
        pageAdapter = new PageReservations(getChildFragmentManager(), tabLayout.getTabCount());

        //Adding pageAdapter to pager
        viewPager.setAdapter(pageAdapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        // SORTING - When the user changes tab I sort the item inside the corresponding RecyclerView
        // by calling "sortDataAndNotify" which will sort data and update the view
        switch (pos) {
            case 0:TabReservationsPending tabP = pageAdapter.getTabPending();
                if(tabP != null)
                    tabP.sortDataAndNotify();
                break;
            case 1:TabReservationsInProgress tabIp = pageAdapter.getTabInProgress();
                if(tabIp != null)
                    tabIp.sortDataAndNotify();
                break;
            case 2:TabReservationsFinished tabF = pageAdapter.getTabFinished();
            if(tabF != null)
                tabF.sortDataAndNotify();
                break;
        }
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
