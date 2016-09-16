package demos.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import com.victor.androiddemos.R
import demos.util.ShowToast
import kotlinx.android.synthetic.main.activity_pay_password.*

/**
 * Author : victor
 * Time : 16-9-16 20:17
 */
class PayPassWordActivity : AppCompatActivity() {
    private val btns = arrayListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_password)
        init()
    }

    private fun init() {
        for (i in 0..pay_num_container.childCount - 1) {
            if (pay_num_container.getChildAt(i) is LinearLayout) {
                for (j in 0..(pay_num_container.getChildAt(i) as ViewGroup).childCount - 1) {
                    if ((pay_num_container.getChildAt(i) as ViewGroup).getChildAt(j) is TextView) {
                        btns.add((pay_num_container.getChildAt(i) as ViewGroup).getChildAt(j))
                    }
                }
            }
        }
        btns.forEachIndexed { i, view -> view.setOnClickListener { pay_view.append((((i + 1) % 10).toString())) } }
        tv_pay_del.setOnClickListener { pay_view.delete() }
        tv_pay_del.setOnLongClickListener {
            pay_view.clear()
            true
        }
        pay_view.setOnFinishListener { ShowToast.shortToast(pay_view.getPassword()) }

        switch_pay.setOnCheckedChangeListener { compoundButton, b ->
            pay_view.isUseInputMethod(b)
            if (b) {
                pay_num_container.visibility = View.INVISIBLE
                showInputMethod(pay_view)
            } else {
                pay_num_container.visibility = View.VISIBLE
                hideInputMethod()
            }
        }
    }

    fun hideInputMethod() {
        window.peekDecorView()?.let {
            val inputManger: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManger.hideSoftInputFromWindow(window.peekDecorView().windowToken, 0)
        }
    }

    fun showInputMethod(v: View) {
        val inputManager: InputMethodManager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(v, 0)
    }
}