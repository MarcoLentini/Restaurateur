package com.example.restaurateur.Reservation;

import android.content.Context;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionSet;
import android.util.AttributeSet;

public class DetailsTransition extends TransitionSet {
    public DetailsTransition(){
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds())
                .addTransition(new ChangeTransform())
                .addTransition(new ChangeImageTransform());
    }

    public DetailsTransition(Context context, AttributeSet attrs){

    }
}
