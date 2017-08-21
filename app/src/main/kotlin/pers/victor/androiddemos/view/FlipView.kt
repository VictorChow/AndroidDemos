package pers.victor.androiddemos.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import pers.victor.androiddemos.R
import pers.victor.androiddemos.util.DisplayUtil

/**
 * Created by Victor on 2017/8/6. (ง •̀_•́)ง
 */
class FlipView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val len = DisplayUtil.dp2px(300f)
    private val camera = Camera()
    private val rectSrc = Rect()
    private val rectLeft = Rect()
    private val rectRight = Rect()
    private val bmpLeft: Bitmap
    private val bmpRight: Bitmap
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val animator = ValueAnimator()
    private var rotateY = 0f

    init {
        val src = BitmapFactory.decodeResource(resources, R.drawable.iv_myself)
        bmpLeft = Bitmap.createBitmap(src, 0, 0, src.width / 2, src.height)
        bmpRight = Bitmap.createBitmap(src, src.width / 2, 0, src.width / 2, src.height)
        rectSrc.set(0, 0, bmpLeft.width, bmpLeft.height)
        camera.setLocation(0f, 0f, -18f)
        animator.duration = 3000
        animator.setFloatValues(0f, -180f)
        animator.addUpdateListener {
            rotateY = it.animatedValue as Float
            invalidate()
        }

        setOnClickListener {
            if (!animator.isRunning) {
                animator.start()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rectLeft.set((w - len) / 2, (h - len) / 2, w / 2, (h + len) / 2)
        rectRight.set(w / 2, (h - len) / 2, (w + len) / 2, (h + len) / 2)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bmpLeft, rectSrc, rectLeft, paint)
        camera.save()
        camera.rotateY(rotateY)
        canvas.translate(width / 2f, height / 2f)
        camera.applyToCanvas(canvas)
        canvas.translate(-width / 2f, -height / 2f)
        camera.restore()
        canvas.drawBitmap(bmpRight, rectSrc, rectRight, paint)
    }
}