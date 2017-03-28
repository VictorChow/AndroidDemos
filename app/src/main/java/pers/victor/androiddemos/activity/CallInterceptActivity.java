package pers.victor.androiddemos.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import pers.victor.androiddemos.R;

/**
 * Created by Victor on 2017/4/1. (ง •̀_•́)ง
 */

public class CallInterceptActivity extends ToolbarActivity {
    public static String interceptNumber = "";

    @Override
    public int bindLayout() {
        return R.layout.activity_call_intercept;
    }

    @Override
    public void initView() {
        EditText editText = $(R.id.et_intercept_num);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                interceptNumber = s.toString();
            }
        });
    }
}
