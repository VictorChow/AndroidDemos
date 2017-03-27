package pers.victor.androiddemos.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText
import pers.victor.androiddemos.util.DisplayUtil

/**
 * Author : victor
 * Time : 16-9-16 21:06
 */
class PayPwdView(context: Context, attributeSet: AttributeSet) : EditText(context, attributeSet) {
    private var inputText = StringBuffer()
    private val borderPaint: Paint = Paint()
    private val circlePaint: Paint
    private val borderWidth = DisplayUtil.dp2px(0.5f).toFloat()
    private val circleRadius = DisplayUtil.dp2px(8f).toFloat()
    private var eachDis = 0f
    private var finishListener: (() -> Unit)? = null
    private var useSystemInput = false

    init {
        borderPaint.isAntiAlias = true
        borderPaint.color = Color.DKGRAY
        borderPaint.strokeWidth = borderWidth
        borderPaint.style = Paint.Style.STROKE

        circlePaint = Paint()
        circlePaint.isAntiAlias = true
        circlePaint.color = Color.DKGRAY
        circlePaint.style = Paint.Style.FILL

        background = null
        inputType = InputType.TYPE_CLASS_NUMBER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.getSize(widthMeasureSpec) / 6)
        eachDis = ((width - borderWidth) / 6)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(0f, borderWidth, width.toFloat(), borderWidth, borderPaint)
        canvas.drawLine(0f, height.toFloat() - borderWidth, width.toFloat(), height.toFloat() - borderWidth, borderPaint)
        canvas.drawLine(borderWidth, 0f, borderWidth, height.toFloat(), borderPaint)
        canvas.drawLine(width - borderWidth, 0f, width - borderWidth, height.toFloat(), borderPaint)
        for (i in 1..5) {
            canvas.drawLine(eachDis * i + borderWidth, 0f, eachDis * i + borderWidth, height.toFloat(), borderPaint)
        }
        for (i in 0..inputText.length - 1) {
            canvas.drawCircle(borderWidth + eachDis * i + eachDis / 2, (height / 2).toFloat(), circleRadius, circlePaint)
        }
    }

    override fun onCheckIsTextEditor() = if (useSystemInput) super.onCheckIsTextEditor() else false

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (useSystemInput && event.action == KeyEvent.ACTION_DOWN) {
            if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)
                append((keyCode - KeyEvent.KEYCODE_0).toString())
            if (keyCode == KeyEvent.KEYCODE_DEL)
                delete()
        }
        return super.onKeyDown(keyCode, event)
    }

    fun append(s: String) {
        if (inputText.length == 6) {
            return
        }
        inputText.append(s)
        invalidate()
        if (inputText.length == 6) {
            finishListener?.invoke()
        }
    }

    fun delete() {
        if (inputText.isEmpty())
            return
        inputText.delete(inputText.length - 1, inputText.length)
        invalidate()
    }

    fun clear() {
        if (inputText.isEmpty())
            return
        inputText.delete(0, inputText.length)
        invalidate()
    }

    fun getPassword() = inputText.toString()

    fun setOnFinishListener(finishListener: () -> Unit) {
        this.finishListener = finishListener
    }

    fun isUseInputMethod(useInputMethod: Boolean) {
        this.useSystemInput = useInputMethod
    }
}