package pers.victor.androiddemos.activity

import android.text.Editable
import android.text.TextWatcher
import pers.victor.androiddemos.R
import kotlinx.android.synthetic.main.activity_call_intercept.*

class CallInterceptActivity : ToolbarActivity() {
    companion object {
        var interceptNumber = ""
    }

    override fun bindLayout() = R.layout.activity_call_intercept

    override fun initView() {
        init()
    }

    private fun init() {

        et_intercept_num.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                interceptNumber = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

}
