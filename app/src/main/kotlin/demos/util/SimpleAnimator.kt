package demos.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

/**
 * Created by victor on 16-7-7.
 */
class SimpleAnimator(private val targetView: View?) {
    private var duration = 300L
    private var property = ""
    private var values: Array<out Number> = arrayOf()
    private var interpolator: Interpolator = AccelerateDecelerateInterpolator()
    private var listener: AnimatorListener? = null
    private var updateListener: ValueAnimator.AnimatorUpdateListener? = null

    fun values(vararg values: Number): SimpleAnimator {
        this.values = values
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

    fun listener(listener: AnimatorListener): SimpleAnimator {
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
        if (values.size < 2) {
            throw IllegalArgumentException("values起码得两个值啊！")
        }
        val animator: ValueAnimator
        if (property.contentEquals("height") || property.contentEquals("width")) {
            val intValues = arrayListOf<Int>()
            this.values.forEach { intValues.add(it.toInt()) }
            val lp = targetView.layoutParams
            animator = ValueAnimator()
            animator.setIntValues(*(intValues.toIntArray()))
            animator.addUpdateListener {
                when (property) {
                    "width" -> lp.width = it.animatedValue as Int
                    "height" -> lp.height = it.animatedValue as Int
                }
                targetView.layoutParams = lp
            }
            animator.addListener(object : AnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    if (intValues[intValues.lastIndex] == 0) {
                        targetView.visibility = View.INVISIBLE
                    }
                }

                override fun onAnimationStart(animation: Animator?) {
                    targetView.visibility = View.VISIBLE
                }
            })
        } else {
            val floatValues = arrayListOf<Float>()
            this.values.forEach { floatValues.add(it.toFloat()) }
            animator = ObjectAnimator.ofFloat(this.targetView, property, *(floatValues.toFloatArray()))
            animator.addListener(object : AnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    if (property.contentEquals("alpha") && floatValues[floatValues.lastIndex] == 0f) {
                        targetView.visibility = View.INVISIBLE
                    }
                }

                override fun onAnimationStart(animation: Animator?) {
                    targetView.visibility = View.VISIBLE
                }
            })
        }
        animator.duration = duration
        animator.interpolator = interpolator
        listener?.let { animator.addListener(listener) }
        updateListener?.let { animator.addUpdateListener(updateListener) }
        animator.start()
    }

    open class AnimatorListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }
    }
}