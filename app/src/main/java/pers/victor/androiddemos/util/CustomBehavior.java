package pers.victor.androiddemos.util;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import pers.victor.androiddemos.AndroidDemos;

/**
 * Created by Victor on 2017/4/1. (ง •̀_•́)ง
 */

public class CustomBehavior extends FloatingActionButton.Behavior {
    private boolean isAnim = false;
    private boolean isHide = false;
    private float y = 0f;

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        if (y == 0f) {
            y = child.getY();
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (isAnim) {
            return;
        }
        if (dyConsumed > 0 && !isHide) {
            hide(child);
        }
        if (dyConsumed < 0 && isHide) {
            show(child);
        }
    }

    private void hide(FloatingActionButton child) {
        isAnim = true;
        isHide = true;
        SimpleAnimator.create(child)
                .values(y, (float) AndroidDemos.screenHeight)
                .property("y")
                .duration(500)
                .listener(new SimpleAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnim = false;
                    }
                })
                .go();
    }

    private void show(FloatingActionButton child) {
        isAnim = true;
        isHide = false;
        SimpleAnimator.create(child)
                .values((float) AndroidDemos.screenHeight, y)
                .property("y")
                .duration(500)
                .listener(new SimpleAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnim = false;
                    }
                })
                .go();
    }
}
