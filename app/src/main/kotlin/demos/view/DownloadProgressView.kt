package demos.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import demos.util.DisplayUtil

/**
 * Author : victor
 * Time : 16-9-14 16:15
 */
class DownloadProgressView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val textPaint: Paint
    private val bgPaint: Paint
    private val borderPaint: Paint

    private var textWidth = 0f
    private var textX = 0f
    private var textY = 0f
    private var progress = 0f
    private val textSize = DisplayUtil.sp2px(16f).toFloat()
    private val borderWidth = DisplayUtil.dp2px(1.5f).toFloat()

    private val bgRect = RectF()
    private val textBounds = Rect()
    private val arcRectF = RectF()
    private val textClipRect = Rect()
    private val progressArcClipRectF = RectF()

    private var progressColor = Color.parseColor("#0074e1")

    init {
        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.color = progressColor
        textPaint.style = Paint.Style.STROKE

        bgPaint = Paint()
        bgPaint.isAntiAlias = true
        bgPaint.style = Paint.Style.FILL
        bgPaint.color = progressColor

        borderPaint = Paint()
        borderPaint.isAntiAlias = true
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
        borderPaint.color = progressColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        textX = ((measuredWidth - textWidth) / 2)
    }

    override fun onDraw(canvas: Canvas) {
        arcRectF.set(borderWidth, borderWidth, height - borderWidth, height.toFloat() - borderWidth)
        canvas.drawArc(arcRectF, -270f, 180f, false, borderPaint)
        arcRectF.set(width - (height - borderWidth), borderWidth, width - borderWidth, height.toFloat() - borderWidth)
        canvas.drawArc(arcRectF, -90f, 180f, false, borderPaint)
        canvas.drawLine((height - borderWidth) / 2, borderWidth, width - (height - borderWidth) / 2, borderWidth, borderPaint)
        canvas.drawLine((height - borderWidth) / 2, height - borderWidth, width - (height - borderWidth) / 2, height - borderWidth, borderPaint)

        val progressX = progress * width / 100

        if (progressX < (height - borderWidth) / 2) {
            progressArcClipRectF.set(borderWidth, borderWidth, progressX, height - borderWidth)
            canvas.save()
            canvas.clipRect(progressArcClipRectF)
            canvas.drawArc(borderWidth, borderWidth, height - borderWidth, height.toFloat() - borderWidth, -270f, 180f, false, bgPaint)
            canvas.restore()
        } else if (progressX < width - (height - borderWidth)) {
            canvas.drawArc(borderWidth, borderWidth, height - borderWidth, height.toFloat() - borderWidth, -270f, 180f, false, bgPaint)
            bgRect.set((height - borderWidth) / 2, borderWidth, progressX, height - borderWidth)
            canvas.drawRect(bgRect, bgPaint)
        } else {
            canvas.drawArc(borderWidth, borderWidth, height - borderWidth, height.toFloat() - borderWidth, -270f, 180f, false, bgPaint)
            if (progressX < width - (height - borderWidth) / 2)
                bgRect.set((height - borderWidth) / 2, borderWidth, progressX, height - borderWidth)
            else
                bgRect.set((height - borderWidth) / 2, borderWidth, width - (height - borderWidth) / 2, height - borderWidth)
            canvas.drawRect(bgRect, bgPaint)
            progressArcClipRectF.set(width - (height - borderWidth), borderWidth, progressX, height - borderWidth)
            canvas.save()
            canvas.clipRect(progressArcClipRectF)
            canvas.drawArc(width - (height - borderWidth), borderWidth, width - borderWidth, height.toFloat() - borderWidth, -90f, 180f, false, bgPaint)
            canvas.restore()
        }

        val text = String.format("已下载 %d%%", progress.toInt())
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textX = ((width - (textBounds.right - textBounds.left)) / 2).toFloat()
        textY = ((height - (textBounds.bottom - textBounds.top)) / 2 + (textBounds.bottom - textBounds.top).toFloat())

        if (progressX <= textX + (textBounds.right - textBounds.left)) {
            textPaint.color = progressColor
            canvas.drawText(text, textX, textY, textPaint)
        }
        if (progressX >= textX) {
            canvas.save()
            textClipRect.set(textX.toInt(), 0, progressX.toInt(), height)
            canvas.clipRect(textClipRect)
            textPaint.color = Color.WHITE
            canvas.drawText(text, textX, textY, textPaint)
            canvas.restore()
        }
    }

    fun setProgress(progress: Float) {
        if (progress < 0) this.progress = 0f
        if (progress > 100) this.progress = 100f
        this.progress = progress
        postInvalidate()
    }
}