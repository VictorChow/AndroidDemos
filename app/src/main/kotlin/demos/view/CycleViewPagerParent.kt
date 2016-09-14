package demos.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.victor.androiddemos.R
import demos.util.DisplayUtil

/**
 * Created by victor on 16-8-15.
 */
class CycleViewPagerParent(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {
    private val proportionHeight: Int
    private val proportionWidth: Int
    private var indicatorMarginBottom: Int
    private var cycleTime: Int
    private var isCycleSlide: Boolean
    private var isAutomaticSlide: Boolean
    private var showIndicator: Boolean
    private val cycleViewPager: CycleViewPager
    private lateinit var indicatorContainer: RelativeLayout

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CycleViewPagerParent)
        proportionHeight = typedArray.getInt(R.styleable.CycleViewPagerParent_proportionHeight, 0)
        proportionWidth = typedArray.getInt(R.styleable.CycleViewPagerParent_proportionWidth, 0)
        isCycleSlide = typedArray.getBoolean(R.styleable.CycleViewPagerParent_slideCycle, true)
        isAutomaticSlide = typedArray.getBoolean(R.styleable.CycleViewPagerParent_slideAutomatic, true)
        showIndicator = typedArray.getBoolean(R.styleable.CycleViewPagerParent_showIndicator, true)
        cycleTime = typedArray.getInt(R.styleable.CycleViewPagerParent_slideCycleTime, 3000)
        indicatorMarginBottom = typedArray.getDimensionPixelSize(R.styleable.CycleViewPagerParent_indicatorMarginBottom, DisplayUtil.dp2px(10f))
        typedArray.recycle()
        cycleViewPager = CycleViewPager(context, attributeSet)
        addView(cycleViewPager, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        initContainer(context, attributeSet)
        addView(indicatorContainer, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        indicatorContainer.visibility = if (showIndicator) View.VISIBLE else View.GONE
    }

    private fun initContainer(context: Context, attributeSet: AttributeSet) {
        indicatorContainer = RelativeLayout(context, attributeSet)
        val linerLayout = LinearLayout(context, attributeSet)
        linerLayout.gravity = Gravity.CENTER
        linerLayout.orientation = LinearLayout.HORIZONTAL
        val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.addRule(RelativeLayout.CENTER_IN_PARENT)
        indicatorContainer.addView(linerLayout, lp)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY && proportionHeight * proportionWidth != 0) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = width * proportionHeight / proportionWidth
            val newWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec))
            val newHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec))
            setMeasuredDimension(newWidthSpec, newHeightSpec)
            for (i in 0..childCount - 1) {
                if (getChildAt(i) is CycleViewPager) {
                    val lp = getChildAt(i).layoutParams
                    lp.height = height
                    lp.width = width
                    getChildAt(i).layoutParams = lp
                }
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0..childCount - 1) {
            if (getChildAt(i) is CycleViewPager) {
                getChildAt(i).layout(0, 0, width, height)
            } else {
                getChildAt(i).layout(0, height - indicatorContainer.measuredHeight - indicatorMarginBottom, width, height - indicatorMarginBottom)
            }
        }
    }

    fun setUp(resource: IntArray) {
        cycleViewPager.setUp(resource, isCycleSlide, indicatorContainer)
        if (isAutomaticSlide) {
            cycleViewPager.enableAuto(cycleTime.toLong())
        }
    }

    fun setUp(imageUrl: MutableList<String>) {
        cycleViewPager.setUp(imageUrl, isCycleSlide, indicatorContainer)
        if (isAutomaticSlide) {
            cycleViewPager.enableAuto(cycleTime.toLong())
        }
    }

    fun setIndicator(selectorId: Int, indicatorWidthDip: Int, indicatorHeightDip: Int, spacingDip: Int) {
        cycleViewPager.setIndicator(selectorId, indicatorWidthDip, indicatorHeightDip, spacingDip)
    }

    fun setOnPagerItemClickListener(func: (Int) -> Unit) {
        cycleViewPager.setOnPagerItemClickListener(func)
    }
}