package demos.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator

/**
 * Created by victor on 16-7-7.
 */
class SimpleAnimator(private val targetView: View?) {
    private var duration = 300L
    private var property = ""
    private var floatValues: FloatArray = floatArrayOf()
    private var intValues: IntArray = intArrayOf()
    private var interpolator: Interpolator? = null
    private var listener: Animator.AnimatorListener? = null
    private var updateListener: ValueAnimator.AnimatorUpdateListener? = null

    fun floatValues(vararg values: Float): SimpleAnimator {
        this.floatValues = values
        return this
    }

    fun intValues(vararg values: Int): SimpleAnimator {
        this.intValues = values
        return this
    }

    fun duration(duration: Long): SimpleAnimator {
        this.duration = duration
        return this
    }

    fun property(property: String): SimpleAnimator {
        this.property = property
        return this
    }

    fun interpolator(interpolator: Interpolator): SimpleAnimator {
        this.interpolator = interpolator
        return this
    }

    fun listener(listener: Animator.AnimatorListener): SimpleAnimator {
        this.listener = listener
        return this
    }

    fun updateListener(updateListener: ValueAnimator.AnimatorUpdateListener): SimpleAnimator {
        this.updateListener = updateListener
        return this
    }

    fun go() {
        if (targetView == null)
            return
        if (property.contentEquals("height") || property.contentEquals("width")) {
            val lp = targetView.layoutParams
            val animator = ValueAnimator()
            animator.duration = duration
            animator.setIntValues(*intValues)
            animator.addUpdateListener {
                when (property) {
                    "width" -> lp.width = it.animatedValue as Int
                    "height" -> lp.height = it.animatedValue as Int
                }
                targetView.layoutParams = lp
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (intValues[intValues.lastIndex] == 0) {
                        targetView.visibility = View.INVISIBLE
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    targetView.visibility = View.VISIBLE
                }
            })
            interpolator?.let { animator.interpolator = interpolator }
            listener?.let { animator.addListener(listener) }
            updateListener?.let { animator.addUpdateListener(updateListener) }
            animator.start()
        } else {
            val animator = ObjectAnimator.ofFloat(this.targetView, property, *floatValues)
            animator.duration = duration
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    if (property.contentEquals("alpha") && floatValues[floatValues.lastIndex] == 0f) {
                        targetView.visibility = View.INVISIBLE
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                    targetView.visibility = View.VISIBLE
                }
            })
            interpolator?.let { animator.interpolator = interpolator }
            listener?.let { animator.addListener(listener) }
            updateListener?.let { animator.addUpdateListener(updateListener) }
            animator.start()
        }
    }
}