package demos.view

import android.animation.FloatEvaluator
import android.content.Context
import android.support.v4.widget.ScrollerCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import java.lang.reflect.ParameterizedType

/**
 * Created by victor on 16-8-12.
 */
class PTIRecyclerView(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var headerView: LinearLayout
    private val scroller: ScrollerCompat
    private var downY = 0f
    private var isComplete = false
    private var isResetDownY = false
    private val evaluator: FloatEvaluator
    private var moveTimes = 0
    private var isInit: Boolean = false

    init {
        scroller = ScrollerCompat.create(context, DecelerateInterpolator())
        evaluator = FloatEvaluator()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isInit) {
            headerView.layout(0, -headerView.measuredHeight, measuredWidth, 0)
            recyclerView.layout(0, 0, measuredWidth, measuredHeight)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (isInit) {
            headerView.pivotX = headerView.measuredWidth.toFloat() / 2
            headerView.pivotY = headerView.measuredHeight.toFloat()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                moveTimes++
                if (ev.y > downY && recyclerView.computeVerticalScrollOffset() == 0) {
                    if (!isResetDownY) {
                        downY = ev.y
                        isResetDownY = true
                    }
                    if (!isComplete) {
                        if (ev.y - downY >= headerView.height) {
                            scrollY = -headerView.height
                            isComplete = true
                            headerView.rotationX = 0f
                            return true
                        }
                        if (scrollY == 0 && moveTimes > 2 && ev.y != downY) {
                            downY = ev.y
                            moveTimes = 0
                        }
                        if (ev.y - downY > 0) {
                            headerView.rotationX = evaluator.evaluate((ev.y - downY) / headerView.height, 90, 0)
                        }
                        scrollY = (downY - ev.y).toInt()
                    } else {
                        if (ev.y - downY < headerView.height) {
                            isComplete = false
                            scrollY = (downY - ev.y).toInt()
                            return true
                        }
                    }
                    return true
                } else {
                    scrollY = 0
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isComplete) {
                    scrollY = 0
                    isComplete = false
                    (recyclerView.adapter as Adapter<*>).addItem()
                    return true
                } else {
                    scroller.startScroll(0, scrollY, 0, -scrollY, 400)
                    postInvalidate()
                }
                moveTimes = 0
                isResetDownY = false
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollY = scroller.currY
            headerView.rotationX = evaluator.evaluate(-scrollY.toFloat() / headerView.height, 90, 0)
            postInvalidate()
        }
    }

    fun setUp(adapter: Adapter<*>) {
        recyclerView = RecyclerView(context)
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.adapter = adapter
        headerView = LinearLayout(context)
        LayoutInflater.from(context).inflate(adapter.layoutId, headerView, true)
        addView(headerView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(recyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        isInit = true
    }


    @Suppress("UNCHECKED_CAST")
    abstract class Adapter<T : Any>(val context: Context, val layoutId: Int, val data: MutableList<T>) : RecyclerView.Adapter<Adapter.Holder>() {
        private val clazz: Class<T>

        init {
            val genType = javaClass.genericSuperclass
            val params = (genType as ParameterizedType).actualTypeArguments
            clazz = params[0] as Class<T>
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = Holder(LayoutInflater.from(context).inflate(layoutId, parent, false))

        override fun getItemCount() = data.size

        class Holder(view: View) : RecyclerView.ViewHolder(view)

        fun addItem() {
            data.add(0, clazz.newInstance())
            notifyDataSetChanged()
        }
    }
}