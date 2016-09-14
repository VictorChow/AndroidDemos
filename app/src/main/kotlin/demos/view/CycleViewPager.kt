package demos.view

import android.animation.IntEvaluator
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.victor.androiddemos.R
import demos.util.DisplayUtil
import demos.util.ImageUtil
import kotlinx.android.synthetic.main.view_pager_layout.view.*
import java.util.*

/**
 * Created by Victor on 2015-8-31.
 */
class CycleViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    private var mCount = 0
    private var currentPosition = 0        // 当前滑动位置
    private var period = 0L               // 自动滑动时间间隔
    private var isCycle = false            // 是否循环滑动
    private var isAuto = false             // 是否自动滑动
    private var isTaskRunning = false
    private var isOnTouch = false
    private var shouldDestroy = false

    private var mTimer: Timer? = null
    private var mTask: TimerTask? = null
    private var containerView: RelativeLayout? = null     // indicator容器
    private val imageViews: MutableList<ImageView> = arrayListOf()
    private val indicators: MutableList<ImageView> = arrayListOf()

    // 自定义indicator相关属性
    private var selectorId: Int = 0
    private var spacingDip: Int = 0
    private var indicatorWidth: Int = 0
    private var indicatorHeight: Int = 0

    private var onPagerItemClick: ((Int) -> Unit)? = null

    private var startX = 0f

    private fun setAdapter(adapter: CycleViewPagerAdapter) {
        super.setAdapter(adapter)
        shouldDestroy = true
        mCount = adapter.count
        mCount = if (isCycle) mCount else mCount + 2
        offscreenPageLimit = if (mCount - 2 > 0) mCount - 2 else 1
        currentItem = if (isCycle) 1 else 0
        addIndicator()
        addOnPageChangeListener(object : OnPageChangeListener {
            private val ev = IntEvaluator()
            private var dis = 0
            private val tar by lazy { containerView!!.getChildAt(1) }
            private val indiNum = (containerView!!.getChildAt(0) as LinearLayout).childCount

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (isCycle) {
                    if (position == 0 && positionOffset.toDouble() == 0.0) {
                        setCurrentItem(mCount - 2, false)
                    } else if (position >= mCount - 1 && positionOffset.toDouble() == 0.0) {
                        setCurrentItem(1, false)
                    }
                }
                if (positionOffset.toDouble() == 0.0) {
                    currentPosition = position
                    if (isAuto) {
                        startTask()
                    }
                } else {
                    // 手动滑动时关闭自动滑动
                    if (isOnTouch && isAuto) {
                        if (isTaskRunning) {
                            stopTask()
                        }
                    }
                }

                if (containerView != null && (containerView!!.getChildAt(0) as LinearLayout).childCount > 1) {
                    if (isCycle) {
                        if (position == 0 || (position >= indiNum && positionOffset != 0f))
                            return
                    }
                    if (dis == 0) {
                        val container = containerView!!.getChildAt(0) as LinearLayout
                        dis = (container.getChildAt(1).x - container.getChildAt(0).x).toInt()
                    }
                    val realPos = if (isCycle) {
                        if (position > indiNum) 0 else (position - 1)
                    } else position
                    tar.x = startX + (realPos * DisplayUtil.dp2px(9f + 3f + 3f)) + ev.evaluate(positionOffset, 0, dis).toFloat()
                }
            }

            override fun onPageSelected(position: Int) {
                var index = position
                if (isCycle) {
                    if (position == 0) {
                        index = mCount - 2
                    } else if (position >= mCount - 1) {
                        index = 1
                    }
                }
                if (containerView != null) {
                    setIndicatorResource(index)
                }
            }

        })
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                if (!isOnTouch) {
                    isOnTouch = true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isOnTouch) {
                    isOnTouch = false
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    /**
     * CycleViewPager添加indicator
     */
    private fun addIndicator() {
        if (containerView == null) {
            return
        }
        if (containerView!!.childCount == 0) {
            return
        }
        val container = containerView!!.getChildAt(0) as LinearLayout
        if (container.childCount > 0) {
            container.removeAllViews()
        }
        for (i in 0..(mCount - 2) - 1) {
            val imageView = ImageView(context)
            indicators.add(imageView)
            imageView.isSelected = i == 0
            if (this.selectorId == 0) {
                imageView.setImageResource(R.drawable.indicator_circle_unselected)
                val px = DisplayUtil.dp2px(9f)
                val params = LinearLayout.LayoutParams(px, px)
                val margin = DisplayUtil.dp2px(3f)
                params.setMargins(margin, 0, margin, 0)
                container.addView(imageView, params)
            } else {
                imageView.setImageResource(this.selectorId)
                val height = DisplayUtil.dp2px(this.indicatorHeight.toFloat())
                val width = DisplayUtil.dp2px(this.indicatorWidth.toFloat())
                val margin = DisplayUtil.dp2px(this.spacingDip.toFloat())
                val params = LinearLayout.LayoutParams(width, height)
                params.setMargins(margin, 0, margin, 0)
                container.addView(imageView, params)
            }
        }
        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.indicator_circle_selected)
        val px = DisplayUtil.dp2px(9f)
        val params = LinearLayout.LayoutParams(px, px)
        containerView!!.addView(imageView, params)
        post {
            imageView.x = container.x + container.getChildAt(0).x
            startX = container.x + DisplayUtil.dp2px(3f)
        }
    }

    /**
     * 设置indicator的选中
     * @param pos viewpager当前位置
     */
    private fun setIndicatorResource(pos: Int) {
        var position = pos
        if (containerView == null) {
            return
        }
        if (containerView!!.childCount == 0) {
            return
        }
        if (position == 0 && isCycle) {
            return
        }
        val container = containerView!!.getChildAt(0) as LinearLayout

        if (!isCycle) {
            position++
        }
        if (container.childCount > 1) {
            post {
                container.getChildAt(position - 1).isSelected = true
                for (i in 0..(mCount - 2) - 1) {
                    if (i != position - 1) {
                        container.getChildAt(i).isSelected = false
                    }
                }
            }
        }
    }

    /**
     * 开启定时任务
     */
    private fun startTask() {
        if (containerView == null) {
            return
        }
        if (isTaskRunning) {
            //防止多次开启
            return
        }
        stopTask()
        isTaskRunning = true
        mTimer = Timer()
        mTask = object : TimerTask() {
            override fun run() {
                currentPosition++
                setPagerCurrentPage()
            }
        }
        mTimer!!.schedule(mTask, period, period)
    }

    /**
     * 停止定时任务
     */
    private fun stopTask() {
        isTaskRunning = false
        if (mTask != null) {
            mTask!!.cancel()
        }
        mTask = null
        if (mTimer != null) {
            mTimer!!.purge()
            mTimer!!.cancel()
        }
        mTimer = null
    }

    /**
     * 处理内存泄露
     */
    fun onDestroyView() {
        if (!shouldDestroy)
            return
//        for (imageView in imageViews) {
//            val drawable = imageView.drawable
//            if (drawable != null && drawable is BitmapDrawable) {
//                val bitmap: Bitmap? = drawable.bitmap
//                if (bitmap != null && !bitmap.isRecycled) {
//                    imageView.setImageBitmap(null)
//                    bitmap.recycle()
//                }
//            }
//            if (drawable != null) {
//                drawable.callback = null
//            }
//        }
        imageViews.clear()
//        for (indicator in indicators) {
//            val drawable = indicator.drawable
//            if (drawable != null) {
//                indicator.setImageDrawable(null)
//                drawable.callback = null
//            }
//        }
        indicators.clear()
        stopTask()
        System.gc()
        shouldDestroy = false
    }

    /**
     * 开启自动滑动
     * @param millisecond 间隔时间
     */
    fun enableAuto(millisecond: Long) {
        isAuto = true
        period = millisecond
    }

    /**
     * 设置当前显示页
     */
    private fun setPagerCurrentPage() {
        if (isCycle) {
            if (currentPosition == mCount - 1) {
                currentPosition = 1
                currentItem = currentPosition
            } else if (currentPosition == 0) {
                currentPosition = mCount - 2
                currentItem = currentPosition
            } else {
                currentItem = currentPosition
            }
        } else {
            currentItem = currentPosition % (mCount - 2)
        }
    }

    /**
     * 自定义indicator
     * @param selectorId
     * *
     * @param indicatorWidthDip
     * *
     * @param indicatorHeightDip
     * *
     * @param spacingDip
     */
    fun setIndicator(selectorId: Int, indicatorWidthDip: Int, indicatorHeightDip: Int, spacingDip: Int) {
        this.selectorId = selectorId
        this.spacingDip = spacingDip
        this.indicatorWidth = indicatorWidthDip
        this.indicatorHeight = indicatorHeightDip
    }

    fun setOnPagerItemClickListener(func: (Int) -> Unit) {
        this.onPagerItemClick = func
    }

    override fun setCurrentItem(item: Int) {
        var index = item
        if (isCycle && item >= mCount) {
            index = 2
        }
        currentPosition = index
        super.setCurrentItem(index)
    }

    fun setUp(resource: IntArray, isCycle: Boolean, container: RelativeLayout?) {
        this.isCycle = isCycle
        this.containerView = container
        val cycleViewPagerAdapter = CycleViewPagerAdapter(resource)
        setAdapter(cycleViewPagerAdapter)
    }

    fun setUp(imageUrl: MutableList<String>, isCycle: Boolean, container: RelativeLayout?) {
        this.isCycle = isCycle
        this.containerView = container
        val cycleViewPagerAdapter = CycleViewPagerAdapter(imageUrl)
        setAdapter(cycleViewPagerAdapter)
    }

    @Deprecated("直接调用setUp(), adapter在内部自动生成", ReplaceWith("CycleViewPager.setUp()"))
    override fun setAdapter(adapter: PagerAdapter?) {
    }

    private inner class CycleViewPagerAdapter : PagerAdapter {
        private var resource: IntArray? = null
        private var imageUrl: List<String>? = null

        constructor(resource: IntArray) {
            if (isCycle) {
                this.resource = if (resource.size == 1) resource else formatPagerAdapterRes(resource)
            } else {
                this.resource = resource
            }
            if (resource.size == 1) {
                isCycle = false
                containerView!!.visibility = View.INVISIBLE
            }
        }

        constructor(imageUrl: MutableList<String>) {
            if (isCycle) {
                this.imageUrl = if (imageUrl.size == 1)
                    imageUrl
                else
                    formatPagerAdapterRes(imageUrl)
            } else {
                this.imageUrl = imageUrl
            }
            if (imageUrl.size == 1) {
                isCycle = false
                containerView!!.visibility = View.INVISIBLE
            } else {
                containerView!!.visibility = View.VISIBLE
            }
        }

        override fun getCount() = if (resource != null) resource!!.size else imageUrl!!.size

        override fun isViewFromObject(view: View, o: Any) = (view == o)

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(context).inflate(R.layout.view_pager_layout, container, false)
            view.iv_pager_item.setOnClickListener { onPagerItemClick?.invoke(if (isCycle) position - 1 else position) }
            imageViews.add(view.iv_pager_item)
            if (resource != null) {
                post { view.iv_pager_item.setImageResource(resource!![position]) }
            } else {
                post { ImageUtil.bind(view.iv_pager_item, imageUrl!![position]) }
            }
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
        }

        /**
         * 处理原始图片资源数组，ABC变为CABCA
         * @param res 原始数组
         * *
         * @return 添加首尾的数组
         */
        private fun formatPagerAdapterRes(res: IntArray): IntArray {
            val displayRes = IntArray(res.size + 2)
            displayRes[0] = res[res.size - 1]
            displayRes[displayRes.size - 1] = res[0]
            System.arraycopy(res, 0, displayRes, 1, res.size)
            return displayRes
        }

        /**
         * 处理原始图片url列表，ABC变为CABCA
         * @param resource 原始列表
         * *
         * @return 添加首尾的列表
         */
        private fun formatPagerAdapterRes(resource: MutableList<String>): MutableList<String> {
            if (resource.size > 0) {
                resource.add(0, resource[resource.size - 1])
                resource.add(resource[1])
            }
            return resource
        }
    }
}
