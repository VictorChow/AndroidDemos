package demos.view

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.victor.androiddemos.R
import demos.util.DisplayUtil

/**
 * Created by victor on 16-8-4.
 */
class ReceiversGroup(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {

    private val items = arrayListOf<Receiver>()
    private val textViews = arrayListOf<TextView>()
    private lateinit var editText: EditText
    private lateinit var alertDialog: AlertDialog.Builder
    private var isDeleting = false

    private val childHeight = DisplayUtil.dp2px(30f)
    private val verSpacing = DisplayUtil.dp2px(5f)
    private val horSpacing = DisplayUtil.dp2px(10f)
    private val leftPadding = DisplayUtil.dp2px(10f)
    private val rightPadding = DisplayUtil.dp2px(10f)

    override fun onFinishInflate() {
        super.onFinishInflate()
        alertDialog = AlertDialog.Builder(context)
        initEditText()
    }

    fun addTextViews(items: List<Receiver>) {
        this.items.addAll(items)
        items.forEach { addTextView(it.name) }
    }

    fun addTextView(text: String) {
        val textView = TextView(context)
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.textSize = 15f
        textView.setTextColor(ContextCompat.getColor(context, R.color.white1))
        textView.setBackgroundResource(R.drawable.bg_blue_corner5)
        textView.setPadding(DisplayUtil.dp2px(10f), 0, DisplayUtil.dp2px(10f), 0)
        textViews.add(textView)
        addView(textView, childCount - 1, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, childHeight))
    }

    private fun initEditText() {
        editText = EditText(context)
        editText.background = null
        editText.maxLines = 1
        editText.hint = "..."
        editText.textSize = 15f
        editText.setTextColor(ContextCompat.getColor(context, R.color.blue2))
        editText.minWidth = editText.paint.measureText("123456789012").toInt()
        editText.setPadding(0, 0, 0, 0)
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.imeOptions = EditorInfo.IME_ACTION_DONE
        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!v.text.toString().contentEquals("")) {
                    items.addItem(Receiver(v.text.toString()))
                    v.text = ""
                }
            }
            false
        }
        editText.setOnKeyListener { v, keyCode, event ->
            if (!isDeleting && event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && editText.text.toString().contentEquals("")) {
                if (items.size > 0) {
                    val child = getChildAt(childCount - 2) as TextView
                    deleteItem(child.text.toString(), items.lastIndex)
                }
            }
            false
        }
        addView(editText, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, childHeight))
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount > 0)
            measureChildren(widthMeasureSpec, heightMeasureSpec)
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec) - rightPadding
        var tempWidth = leftPadding
        var tempHeight = getChildAt(0).measuredHeight
        textViews.forEach {
            if (tempWidth + it.measuredWidth > maxWidth) {
                tempWidth = leftPadding
                tempHeight += (it.measuredHeight + verSpacing)
            }
            tempWidth += (it.measuredWidth + horSpacing)
        }
        if (tempWidth + editText.measuredWidth > maxWidth) {
            tempHeight += editText.measuredHeight
        }
        setMeasuredDimension(widthMeasureSpec, tempHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var tempWidth = leftPadding
        var tempHeight = 0
        textViews.forEach {
            if (tempWidth + it.measuredWidth > measuredWidth - rightPadding) {
                tempWidth = leftPadding
                tempHeight += (it.measuredHeight + verSpacing)
            }
            it.layout(tempWidth, tempHeight, tempWidth + it.measuredWidth, tempHeight + it.measuredHeight)
            tempWidth += (it.measuredWidth + horSpacing)
        }
        if (tempWidth + editText.measuredWidth > measuredWidth - rightPadding) {
            tempWidth = leftPadding
            tempHeight += editText.measuredHeight
        }
        editText.layout(tempWidth, tempHeight, tempWidth + editText.measuredWidth, tempHeight + editText.measuredHeight)

        if (childCount > 1) {
            //有至少一个联系人
            for (pos in 0..childCount - 2) {
                getChildAt(pos).setOnClickListener {
                    val child = getChildAt(pos) as TextView
                    deleteItem(child.text.toString(), pos)
                }
            }
        }
    }

    private fun deleteItem(text: String, pos: Int) {
        hideInputMethod()
        isDeleting = true
        alertDialog.setTitle("提示").setMessage("确定删除 $text 么？")
                .setNegativeButton("不", { p0, p1 -> isDeleting = false })
                .setPositiveButton("嗯", { p0, p1 ->
                    items.removeItem(pos)
                    isDeleting = false
                })
                .setCancelable(false)
                .show()
    }

    private fun MutableList<Receiver>.addItem(receiver: Receiver) {
        this.add(receiver)
        addTextView(receiver.name)
    }

    private fun MutableList<Receiver>.removeItem(position: Int) {
        this.removeAt(position)
        removeView(textViews[position])
        textViews.removeAt(position)
    }

    class Receiver(val mobile: String, name: String = "") {
        val name = if (name.contentEquals("")) mobile else name
    }

    fun getMobileNumber(): String {
        if (items.size == 0)
            return ""
        val sb = StringBuilder()
        items.forEach {
            sb.append(",").append(it.mobile)
        }
        return sb.substring(1, sb.length)
    }

    fun hideInputMethod() {
        val activity = context as Activity
        activity.window.peekDecorView()?.let {
            val inputManger: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManger.hideSoftInputFromWindow(activity.window.peekDecorView().windowToken, 0)
        }
    }
}