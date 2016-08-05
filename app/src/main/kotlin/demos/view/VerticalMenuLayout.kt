package demos.view

import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.support.v4.widget.ScrollerCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator

/**
 * Created by victor on 16-8-1.
 */
class VerticalMenuLayout(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {

    private var currPage = 0
    private var downX = 0f
    private var downY = 0f
    private lateinit var scroller: ScrollerCompat
    private var duration = 300
    private var tracker: VelocityTracker? = null

    init {
        scroller = ScrollerCompat.create(context, DecelerateInterpolator())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        var maxHeight = 0
        var maxWidth = 0
        for (i in 0..childCount - 1) {
            if (getChildAt(i).measuredWidth > maxWidth)
                maxWidth = getChildAt(i).measuredWidth
            if (getChildAt(i).measuredHeight > maxHeight)
                maxHeight = getChildAt(i).measuredHeight
        }
        setMeasuredDimension(maxWidth, maxHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0..childCount - 1) {
            getChildAt(i).layout(0, i * measuredHeight, getChildAt(i).measuredWidth, i * measuredHeight + getChildAt(i).measuredHeight)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var interceptAsNeed = false
        when (MotionEventCompat.getActionMasked(ev)) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                interceptAsNeed = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (Math.abs(ev.y - downY) > Math.abs(ev.x - downX)) {
                    interceptAsNeed = true
                }
            }
            MotionEvent.ACTION_UP -> {
                downX = 0f
                downY = 0f
                interceptAsNeed = false
            }
        }
        return interceptAsNeed
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (tracker == null) {
            tracker = VelocityTracker.obtain()
        }
        tracker?.addMovement(ev)
        when (MotionEventCompat.getActionMasked(ev)) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                scrollTo(0, -(ev.y - downY).toInt() + currPage * measuredHeight)
            }
            MotionEvent.ACTION_UP -> {
                if (scrollY < 0) {
                    scroll(scrollY, -scrollY)
                    postInvalidate()
                    return true
                }

                if (scrollY > (childCount - 1) * measuredHeight) {
                    scroll(scrollY, (childCount - 1) * measuredHeight - scrollY)
                    postInvalidate()
                    return true
                }
                tracker?.computeCurrentVelocity(1000)
                val velocity = tracker!!.yVelocity
                if (velocity > 2000) {//上一页
                    scrollToPreviousPage(scrollY, ((ev.y - downY) - measuredHeight).toInt())
                    return true
                }
                if (velocity < -2000) {//下一页
                    scrollToNextPage(scrollY, (measuredHeight - (downY - ev.y)).toInt())
                    return true
                }

                if (ev.y - downY < 0) {// 向上滑
                    if (Math.abs(ev.y - downY) < measuredHeight / 3) {
                        scroll(scrollY, (ev.y - downY).toInt())
                    } else {
                        scrollToNextPage(scrollY, (measuredHeight - (downY - ev.y)).toInt())
                    }
                } else {// 向下滑
                    if (Math.abs(ev.y - downY) < measuredHeight / 3) {
                        scroll(scrollY, (ev.y - downY).toInt())
                    } else {
                        scrollToPreviousPage(scrollY, ((ev.y - downY) - measuredHeight).toInt())
                    }
                }
                tracker?.clear()

            }
        }
        return true
    }

    private fun scroll(startY: Int, dy: Int) {
        scroller.startScroll(0, startY, 0, dy, duration)
        postInvalidate()
    }

    private fun scrollToNextPage(startY: Int, dy: Int) {
        scroll(startY, dy)
        currPage++
    }

    private fun scrollToPreviousPage(startY: Int, dy: Int) {
        scroll(startY, dy)
        currPage--
    }


    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }
}