package com.nova.hro.novamaterial;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by shri6 on 3/3/2016.
 */
public class Animations {

    public void setFadeAnim(View view){
    ObjectAnimator  fadeAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1);
    fadeAnim.start();
    }

    public void setSwipeRight(View view){
        ObjectAnimator sr = ObjectAnimator.ofFloat(view, View.X, 1000).setDuration(1000);
        ObjectAnimator fa = ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0).setDuration(1000);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(sr,fa);
        set.start();
    }
}
