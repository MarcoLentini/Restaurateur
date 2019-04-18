package com.example.restaurateur.History;

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

public class History_f extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_history, container, false);
/*
//Initializing the tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_history);

        //Adding the tabs using addTab() method

        tabLayout.addTab(tabLayout.newTab().setText(R.string.active_label));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.disabled_label));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) view.findViewById(R.id.pager_history);
       viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        //Creating our pager adapter
       PagerHistory adapter = new PagerHistory(getFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
*/
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
