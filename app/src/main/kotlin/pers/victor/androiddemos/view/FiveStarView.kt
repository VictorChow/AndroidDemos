package pers.victor.androiddemos.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import pers.victor.androiddemos.R


/**
 * Copyright (c) 16-9-18 by loren
 */

class FiveStarView : View {
    private val mPointA = PointF()
    private val mPointB = PointF()
    private val mPointC = PointF()
    private val mPointD = PointF()
    private val mPointE = PointF()
    private val mPoint1 = PointF()
    private val mPoint2 = PointF()
    private val mPoint3 = PointF()
    private val mPoint4 = PointF()
    private val mPoint5 = PointF()
    private val mPaint = Paint()
    private val mPath = Path()
    private var pentagonWidth = 0f //五边形的宽
    private var width = 0f
    private var height = 0f
    private var color = 0

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FiveStarView)
        color = typedArray.getColor(R.styleable.FiveStarView_color, Color.BLACK)
        typedArray.recycle()
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 3f
        mPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(mPath, mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        pentagonWidth = View.MeasureSpec.getSize(widthMeasureSpec).toFloat() / 2f / cos(36)
        width = cos(36) * 2f * pentagonWidth
        height = pentagonWidth / (2 * tan(18))
        setMeasuredDimension(widthMeasureSpec, height.toInt())

        mPointA.set(width / 2, 0f)
        mPointB.set(0f, pentagonWidth * cos(54))
        mPointC.set((width - pentagonWidth) / 2, height)
        mPointD.set((width + pentagonWidth) / 2, height)
        mPointE.set(width, pentagonWidth * cos(54))
        mPoint1.set(width / 2 - mPointB.y * tan(18), mPointB.y)
        mPoint2.set(mPointC.x + width * cos(72) / (2 * (1 + sin(18))), height - width * sin(72) / (2 * (1 + sin(18))))
        mPoint3.set(width / 2, height - pentagonWidth * tan(36) / 2)
        mPoint4.set(mPointD.x - width * cos(72) / (2 * (1 + sin(18))), height - width * sin(72) / (2 * (1 + sin(18))))
        mPoint5.set(width / 2 + mPointB.y * tan(18), mPointB.y)

        mPath.reset()
        mPath.moveTo(mPointA.x, mPointA.y)
        mPath.lineTo(mPoint1.x, mPoint1.y)
        mPath.lineTo(mPointB.x, mPointB.y)
        mPath.lineTo(mPoint2.x, mPoint2.y)
        mPath.lineTo(mPointC.x, mPointC.y)
        mPath.lineTo(mPoint3.x, mPoint3.y)
        mPath.lineTo(mPointD.x, mPointD.y)
        mPath.lineTo(mPoint4.x, mPoint4.y)
        mPath.lineTo(mPointE.x, mPointE.y)
        mPath.lineTo(mPoint5.x, mPoint5.y)
        mPath.close()

        mPaint.color = color
    }

    private fun cos(degree: Int): Float {
        return Math.cos(Math.toRadians(degree.toDouble())).toFloat()
    }

    private fun sin(degree: Int): Float {
        return Math.sin(Math.toRadians(degree.toDouble())).toFloat()
    }

    private fun tan(degree: Int): Float {
        return Math.tan(Math.toRadians(degree.toDouble())).toFloat()
    }

    fun setColor(color: Int) {
        mPaint.color = color
        invalidate()
    }
}
