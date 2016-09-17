package demos.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView

/**
 * Author : victor
 * Time : 16-9-17 16:39
 */
class CircleRevealView(context: Context, attributeSet: AttributeSet) : ImageView(context, attributeSet) {
    private val circlePath = Path()
    private var radius = 0f
    private var maxRadius = 0f
    private val anim = ValueAnimator()

    init {
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = 1500
        anim.addUpdateListener {
            radius = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        maxRadius = (if (w > h) w else h).toFloat()
        anim.setFloatValues(0f, maxRadius)
    }

    override fun onDraw(canvas: Canvas) {
        circlePath.reset()
        circlePath.addCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, Path.Direction.CW)
        canvas.save()
        canvas.clipPath(circlePath)
        super.onDraw(canvas)
        canvas.restore()
    }

    fun circleReveal() {
        if (!anim.isRunning) {
            anim.start()
        }
    }
}