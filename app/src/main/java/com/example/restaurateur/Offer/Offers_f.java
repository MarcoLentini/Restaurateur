package com.example.restaurateur.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurateur.R;
import com.example.restaurateur.MainActivity;

public class Offers_f extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    FloatingActionButton FabActive;
    FloatingActionButton FabDisabled;


    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //echo -- this is floatingActionButton
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers_f, container, false);

//Initializing the tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_offers);
//echo -- initializing the floatingActionButton

        FabActive= view.findViewById(R.id.FabAddActiveCategories);
        FabDisabled= view.findViewById(R.id.FabAddDisabledCategories);
        FabDisabled.hide();

        FabActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //start a new Activity that you can add food
                Intent myIntent = new Intent(getActivity(), AddNewOfferActivity.class);
                startActivity(myIntent);
            }
        });
        //Adding the tabs using addTab() method

        tabLayout.addTab(tabLayout.newTab().setText(R.string.active_label));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.disabled_label));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) view.findViewById(R.id.pager_offers);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //Creating our pager adapter
        PagerOffers adapter = new PagerOffers(getChildFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                animateFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;


    }

// Tab

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        animateFab(tab.getPosition());
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void animateFab(int position) {

         MainActivity reservationsActivity = (MainActivity) getActivity();

        switch (position) {
            case 0:
                if(reservationsActivity.state_offers[0]==0) {
                    FabActive.show();
                    FabDisabled.hide();
                    ((MainActivity) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
                else {
                    FabDisabled.hide();
                    ((MainActivity) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } break;
            case 1:
                if(reservationsActivity.state_offers[1]==0) {
                    FabActive.hide();
                    FabDisabled.show();
                    ((MainActivity) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
                else {
                    FabActive.hide();
                    ((MainActivity) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }break;

            default:
                FabActive.show();
                FabDisabled.hide();
                break;
        }
    }

}
