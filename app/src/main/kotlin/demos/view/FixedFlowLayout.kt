package demos.view

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.victor.androiddemos.R

/**
 * Author : victor
 * Time : 16-8-10 15:25
 */
class FixedFlowLayout(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {

    private var columns: Int
    private var isSingleSelected: Boolean
    private var horizontalSpacing: Int
    private var verticalSpacing: Int
    private var eachWidth = 0
    private var eachHeight = 0
    private var childGravity = -1
    private var func: ((Int) -> Boolean)? = null
    private var hasSelected = false
    private var preClickPos = 0
    val childList = arrayListOf<ChildView>()
    private var tag = false

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.FixedFlowLayout)
        columns = typedArray.getInt(R.styleable.FixedFlowLayout_columns_per_line, 1)
        isSingleSelected = typedArray.getBoolean(R.styleable.FixedFlowLayout_single_selected, false)
        horizontalSpacing = typedArray.getDimensionPixelOffset(R.styleable.FixedFlowLayout_horizontal_spacing, 0)
        verticalSpacing = typedArray.getDimensionPixelOffset(R.styleable.FixedFlowLayout_vertical_spacing, 0)
        childGravity = typedArray.getInt(R.styleable.FixedFlowLayout_child_gravity, Gravity.TOP or Gravity.START)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount == 0)
            return
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        if (eachHeight == 0 || eachWidth == 0) {
            for (i in 0..childCount - 1) {
                eachWidth = (MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight - (columns - 1) * horizontalSpacing) / columns
                eachHeight = eachWidth
                break
            }
        }
        for (i in 0..childCount - 1) {
            val lp = getChildAt(i).layoutParams
            if (lp != null) {
                lp.width = eachWidth
                lp.height = eachHeight
            }
            getChildAt(i).layoutParams = lp
        }
        var tempHeight = paddingTop + paddingBottom
        if (childCount % columns != 0) {
            tempHeight += ((childCount / columns + 1) * eachHeight + (childCount / columns) * verticalSpacing)
        } else {
            tempHeight += ((childCount / columns) * eachHeight + (childCount / columns - 1) * verticalSpacing)
        }
        val measuredHeightSpec = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY)
        setMeasuredDimension(widthMeasureSpec, measuredHeightSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0)
            return
        if (!tag) {
            var curY = paddingTop
            var curX = paddingStart
            var itemsCountEachLine = 0
            childList.clear()
            for (i in 0..childCount - 1) {
                if (isSingleSelected) {
                    if (!hasSelected) {
                        getChildAt(i).isSelected = true
                        hasSelected = true
                    }
                    getChildAt(i).setOnClickListener {
                        it.selectMe()
                    }
                }
                if (itemsCountEachLine == columns) {
                    itemsCountEachLine = 0
                    curY += (eachHeight + verticalSpacing)
                    curX = paddingStart
                }
                if (getChildAt(i) is TextView) {
                    (getChildAt(i) as TextView).gravity = childGravity
                }
                childList.add(ChildView.newInstance(getChildAt(i), curX, curY))
//            getChildAt(i).layout(curX, curY, curX + eachWidth, curY + eachHeight)
                curX += (eachWidth + horizontalSpacing)
                itemsCountEachLine++
            }
        }
        childList.forEach {
            it.child.layout(it.position.x, it.position.y,
                    it.position.x + eachWidth, it.position.y + eachHeight)
        }
    }

    override fun bringChildToFront(child: View?) {
        tag = true
        super.bringChildToFront(child)
    }

    private fun View.selectMe() {
        for (i in 0..childCount - 1) {
            if (this == getChildAt(i)) {
                if (preClickPos == i)
                    return
                preClickPos = i
                if (func == null) {
                    getChildAt(i).isSelected = true
                } else {
                    if (func!!(i)) {
                        getChildAt(i).isSelected = true
                    } else {
                        return
                    }
                }
            }
        }
        for (i in 0..childCount - 1) {
            if (this != getChildAt(i)) {
                getChildAt(i).isSelected = false
            }
        }
    }

    fun setColumns(columns: Int) {
        this.columns = columns
        layout(0, 0, measuredWidth, measuredHeight)
    }

    fun setOnSelectedChangeListener(func: (Int) -> Boolean) {
        this.func = func
    }

    class ChildView {

        private constructor(child: View, x: Int, y: Int) {
            this.child = child
            position.set(x, y)
        }

        companion object {
            fun newInstance(child: View, x: Int, y: Int) = ChildView(child, x, y)
        }

        val position = Point()
        lateinit var child: View
    }
}