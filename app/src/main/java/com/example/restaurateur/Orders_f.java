package com.example.restaurateur;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class Orders_f extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_f, container, false);

//Initializing the tablayout
    tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_orders);

    //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(R.string.pending_label));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.accepted_label));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.refused_label));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    //Initializing viewPager
    viewPager = (ViewPager) view.findViewById(R.id.pager_orders);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    //Creating our pager adapter
    PagerOrders adapter = new PagerOrders(getFragmentManager(), tabLayout.getTabCount());

    //Adding adapter to pager
        viewPager.setAdapter(adapter);

    //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);

        return view;


}

// Tab

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }



}