package demos.util.behavior

import android.animation.Animator
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import demos.AndroidDemos
import demos.util.SimpleAnimator

/**
 * Author : victor
 * Time : 16-9-20 10:10
 */
class CustomBehavior(context: Context, attributeSet: AttributeSet) : FloatingActionButton.Behavior(context, attributeSet) {
    private var isAnim = false
    private var isHide = false
    private var y = 0f

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout?, child: FloatingActionButton, directTargetChild: View?, target: View?, nestedScrollAxes: Int): Boolean {
        if (y == 0f) {
            y = child.y
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        if (isAnim) {
            return
        }
        if (dyConsumed > 0 && !isHide) {
            hide(child)
        }
        if (dyConsumed < 0 && isHide) {
            show(child)
        }
    }

    private fun hide(child: FloatingActionButton) {
        isAnim = true
        isHide = true
        SimpleAnimator(child).values(y, AndroidDemos.screenHeight).property("y").duration(500).listener(object : SimpleAnimator.AnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                isAnim = false
            }
        }).go()
    }

    private fun show(child: FloatingActionButton) {
        isAnim = true
        isHide = false
        SimpleAnimator(child).values(AndroidDemos.screenHeight, y).property("y").duration(500).listener(object : SimpleAnimator.AnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                isAnim = false
            }
        }).go()
    }

}