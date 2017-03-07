package demos.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
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
import demos.util.DisplayUtil

/**
 * Created by victor on 16-8-4.
 */
class ReceiversGroup(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {

    class Contact(mobile: String, name: String = "") {
        val mobile = if (mobile.startsWith("+86")) mobile.substring(3) else mobile
        val name = if (name.contentEquals("")) mobile else name
    }

    private class ReceiverItem(val itemView: TextView, val contact: Contact)

    private val showTitle = false
    private val textSize = 20f

    private val childHeight = LayoutParams.WRAP_CONTENT
    private val receiverItems = arrayListOf<ReceiverItem>()
    private lateinit var editText: EditText
    private lateinit var titleText: TextView
    private val inputManager: InputMethodManager
    private val alertDialog: AlertDialog.Builder
    private var isDeleting = false
    private val verSpacing = DisplayUtil.dp2px(0f)
    private val horSpacing = DisplayUtil.dp2px(0f)
    private val color = Color.parseColor("#f8f8f8")

    init {
        alertDialog = AlertDialog.Builder(context as Activity)
        inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addTitleText()
        addEditText()
        setOnClickListener { inputManager.showSoftInput(editText, 0) }
    }

    fun addContacts(items: List<Contact>) = items.forEach { addContact(it) }

    private fun addTitleText() {
        titleText = TextView(context)
        titleText.text = "收件人："
        titleText.gravity = Gravity.CENTER
        titleText.textSize = textSize
        titleText.setTextColor(color)
//        titleText.setPadding(0, 0, DisplayUtil.dp2px(10f), 0)
        addView(titleText, childCount - 1, LayoutParams(LayoutParams.WRAP_CONTENT, childHeight))
    }

    private fun addEditText() {
        editText = EditText(context)
        editText.background = null
        editText.maxLines = 1
        editText.hint = "..."
        editText.textSize = textSize
        editText.setTextColor(color)
        editText.setHintTextColor(color)
        editText.minWidth = editText.paint.measureText("123456789012").toInt()
        editText.setPadding(0, 0, 0, 0)
        editText.filters = arrayOf<InputFilter>(android.text.InputFilter.LengthFilter(11))
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.imeOptions = EditorInfo.IME_ACTION_DONE
        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!v.text.toString().contentEquals("")) {
                    addContact(Contact(v.text.toString()))
                    v.text = ""
                }
            }
            false
        }
        editText.setOnKeyListener { v, keyCode, event ->
            if (!isDeleting && event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && editText.text.toString().contentEquals("")) {
                if (receiverItems.size > 0) {
                    val child = getChildAt(childCount - 2) as TextView
                    deleteItem(child, receiverItems.lastIndex)
                }
            }
            false
        }
        addView(editText, LayoutParams(LayoutParams.WRAP_CONTENT, childHeight))
    }

    fun addContact(contact: Contact) {
        val textView = TextView(context)
        textView.text = "${contact.name}、"
        if (contact.mobile.contentEquals(contact.name)) {
            textView.tag = null
        } else {
            textView.tag = contact.mobile
        }
        textView.gravity = Gravity.CENTER
        textView.textSize = textSize
        textView.setTextColor(color)
//        textView.setPadding(DisplayUtil.dp2px(10f), 0, DisplayUtil.dp2px(10f), 0)
        addView(textView, childCount - 1, LayoutParams(LayoutParams.WRAP_CONTENT, childHeight))
        receiverItems.add(ReceiverItem(textView, contact))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isInEditMode)
            return
        if (childCount > 0) {
            measureChildren(widthMeasureSpec, heightMeasureSpec)
        }
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingRight
        var tempWidth = paddingLeft
        var tempHeight = getChildAt(0).measuredHeight + paddingTop
        if (showTitle) {
            tempWidth += titleText.measuredWidth
        }
        receiverItems.forEach {
            if (tempWidth + it.itemView.measuredWidth > maxWidth) {
                tempWidth = paddingLeft
                tempHeight += (it.itemView.measuredHeight + verSpacing)
            }
            tempWidth += (it.itemView.measuredWidth + horSpacing)
        }
        if (tempWidth + editText.measuredWidth > maxWidth) {
            tempHeight += editText.measuredHeight + verSpacing
        }
        tempHeight += paddingBottom
        setMeasuredDimension(widthMeasureSpec, tempHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isInEditMode)
            return
        var tempWidth = paddingLeft
        var tempHeight = paddingTop
        if (showTitle) {
            titleText.layout(tempWidth, tempHeight, tempWidth + titleText.measuredWidth, tempWidth + titleText.measuredHeight)
            tempWidth += titleText.measuredWidth
        }
        receiverItems.forEach {
            if (tempWidth + it.itemView.measuredWidth > measuredWidth - paddingRight) {
                tempWidth = paddingLeft
                tempHeight += (it.itemView.measuredHeight + verSpacing)
            }
            it.itemView.layout(tempWidth, tempHeight, tempWidth + it.itemView.measuredWidth, tempHeight + it.itemView.measuredHeight)
            tempWidth += (it.itemView.measuredWidth + horSpacing)
        }
        if (tempWidth + editText.measuredWidth > measuredWidth - paddingRight) {
            tempWidth = paddingLeft
            tempHeight += editText.measuredHeight + verSpacing
        }
        editText.layout(tempWidth, tempHeight, tempWidth + editText.measuredWidth, tempHeight + editText.measuredHeight)

        if (receiverItems.size > 0) {
            //有至少一个联系人
            for (pos in 1..childCount - 2) {
                getChildAt(pos).setOnClickListener {
                    val child = getChildAt(pos) as TextView
                    //有个TitleTextView在位置0，跟receiverItems对应位置时要-1
                    deleteItem(child, pos - 1)
                }
            }
        }
    }

    private fun deleteItem(tv: TextView, pos: Int) {
        hideInputMethod()
        isDeleting = true
        var msg = if (tv.tag == null) tv.text else "${tv.text}(${tv.tag})"
        if ("、" in msg) {
            msg = msg.replace("、".toRegex(), "")
        }
        alertDialog.setTitle("提示").setMessage("确定删除 $msg 么？")
                .setNegativeButton("不", { p0, p1 -> isDeleting = false })
                .setPositiveButton("嗯", { p0, p1 ->
                    receiverItems.removeAt(pos)
                    //有个TitleTextView在位置0，但是receiverItems不包含这个，要+1
                    removeViewAt(pos + 1)
                    isDeleting = false
                })
                .setCancelable(false)
                .show()
    }

    fun getMobileNumbers(): String {
        if (receiverItems.size == 0) return ""
        val sb = StringBuilder()
        receiverItems.forEach { sb.append(",").append(it.contact.mobile) }
        return sb.substring(1, sb.length)
    }

    fun hideInputMethod() {
        val activity = context as Activity
        activity.window.peekDecorView()?.let {
            val inputManger: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManger.hideSoftInputFromWindow(activity.window.peekDecorView().windowToken, 0)
        }
    }

    fun getEditText() = editText
}