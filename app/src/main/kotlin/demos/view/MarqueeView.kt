package demos.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.victor.androiddemos.R
import java.lang.reflect.Method
import java.util.*

/**
 * Created by victor on 16-7-14.
 */
class MarqueeView(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet), View.OnClickListener {
    private var temp = true
    private var data: List<String> = arrayListOf()
    private var onClick: ((Int) -> Unit)? = null
    private var tempPos = 1
    private lateinit var method: Method
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onClick(v: View?) {
        onClick?.invoke((tempPos + data.size - 1) % data.size)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        method = View::class.java.getDeclaredMethod("setMeasuredDimension", Int::class.java, Int::class.java)
        method.isAccessible = true
        setOnClickListener(this)
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                startTranslation()
            }
        }
    }

    private fun addContentViews() {
        if (data.size == 0) {
            return
        }
        if (data.size == 1) {
            addTextView(0)
            return
        }
        for (i in 0..1) {
            addTextView(i)
        }
    }

    private fun addTextView(index: Int) {
        val textView = TextView(context)
        method.invoke(textView, width, height)
        textView.text = data[index]
        textView.maxLines = 1
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.setPadding(60, 0, 60, 0)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setTextColor(ContextCompat.getColor(context, R.color.blue2))
        addView(textView)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (!changed)
            return
        var temp = 0
        for (i in 0..childCount - 1) {
            getChildAt(i).layout(0, temp, width, temp + height)
            temp += height
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0) {
            removeAllViews()
            addContentViews()
            timer.schedule(timerTask, 2000, 3800)
        }
    }

    fun setContentData(data: List<String>) {
        this.data = data
    }

    fun setOnItemClickListener(onClick: ((Int) -> Unit)) {
        this.onClick = onClick
    }

    private fun startTranslation() {
        for (i in 0..1) {
            val animator = ObjectAnimator.ofFloat(getChildAt(if (temp) i else 1 - i), "y", height.toFloat() * i, -height + height.toFloat() * i)
            animator.duration = 800
            if (i == 0) {
                animator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        tempPos++
                        if (tempPos >= data.size)
                            tempPos = 0
                        (getChildAt(if (temp) 1 - i else i) as TextView).text = data[tempPos]
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
            }
            post { animator.start() }
        }
        temp = !temp
    }

}