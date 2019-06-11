package com.example.restaurateur.Reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;

import static android.app.Activity.RESULT_OK;

public class ReservationsMainFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    public PageReservations pageAdapter;
    private TextView tvPendingReservationsNumber;
    private int pendingReservationsNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pendingReservationsNumber = MainActivity.pendingReservationsData.size();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations_f, container, false);

        //Initializing the tablayout
        tabLayout = view.findViewById(R.id.tabLayout_reservations);
        //Adding the tabs using addTab() method
        TabLayout.Tab tabPending = tabLayout.newTab();
        tabPending.setCustomView(R.layout.tab_header_badge);
        tabPending.setText(R.string.pending_label); // to make setText() work in the xml layout the TextView for the text must have the system Resource ID @android:id/text1
        tabLayout.addTab(tabPending);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.accepted_label));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.refused_label));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tvPendingReservationsNumber = view.findViewById(R.id.textViewActiveAlarmBadge);

        //Initializing viewPager
        viewPager = view.findViewById(R.id.pager_reservations);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Creating our pager pageAdapter(a ViewPager has an associated pageAdapter)
        pageAdapter = new PageReservations(getChildFragmentManager(), tabLayout.getTabCount());

        //Adding pageAdapter to pager
        viewPager.setAdapter(pageAdapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);

        initializeTvPendingReservationsNumber();

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();

        viewPager.setCurrentItem(pos);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        pageAdapter.getTabPending().onActivityResult(requestCode,resultCode,data);
        pageAdapter.getTabInProgress().onActivityResult(requestCode,resultCode,data);
        pageAdapter.getTabFinished().onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void initializeTvPendingReservationsNumber() {
        if(pendingReservationsNumber > 0) {
            tvPendingReservationsNumber.setText(String.valueOf(pendingReservationsNumber));
            tvPendingReservationsNumber.setVisibility(View.VISIBLE);
            pageAdapter.notifyDataSetChanged();
        }
    }

    public void incrementPendingReservationsNumber() {
        if(pendingReservationsNumber == 0)
            tvPendingReservationsNumber.setVisibility(View.VISIBLE);
        pendingReservationsNumber++;
        tvPendingReservationsNumber.setText(String.valueOf(pendingReservationsNumber));
    }

    public void decrementPendingReservationsNumber() {
        if(pendingReservationsNumber >= 1) {
            if(pendingReservationsNumber == 1)
                tvPendingReservationsNumber.setVisibility(View.INVISIBLE);
            pendingReservationsNumber--;
            if(pendingReservationsNumber > 0)
                tvPendingReservationsNumber.setText(String.valueOf(pendingReservationsNumber));
        }
    }

}
