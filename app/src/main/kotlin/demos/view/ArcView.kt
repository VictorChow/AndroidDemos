package demos.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.victor.androiddemos.R
import java.util.*

/**
 * Created by Victor on 15/11/7.
 */
class ArcView : View {
    private var mPaint: Paint? = null
    private var mRectF: RectF? = null
    private var mDiameter: Int = 0
    private var arcEntities = ArrayList<ArcEntity>()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mRectF = RectF()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val d = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
        mRectF!!.set(0f, 0f, d.toFloat(), d.toFloat())
        mDiameter = d
        setMeasuredDimension(d, d)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var angle = 0
        for (arc in arcEntities) {
            mPaint!!.color = arc.color
            canvas.drawArc(mRectF, angle.toFloat() - 90, arc.value.toFloat(), true, mPaint)
            angle += arc.value
        }
        canvas.translate((mDiameter / 2).toFloat(), (mDiameter / 2).toFloat())
        mPaint!!.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        canvas.drawCircle(0f, 0f, (mDiameter / 3.4f).toFloat(), mPaint)
        mPaint!!.color = Color.BLACK
        mPaint!!.textAlign = Paint.Align.CENTER
        mPaint!!.textSize = 80f
        //        canvas.drawText("圆环", 0f, 0f, mPaint)
    }

    fun setArcEntities(arcEntities: ArrayList<ArcEntity>) {
        this.arcEntities = arcEntities
        var totalValue = 0
        this.arcEntities.forEach {
            totalValue += it.value
        }
        this.arcEntities.forEach {
            it.value = (it.value.toFloat() / totalValue * 360).toInt() + 1
        }
    }

    data class ArcEntity(var title: String?, var color: Int, var value: Int)
}
