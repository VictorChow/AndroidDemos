package pers.victor.androiddemos.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pers.victor.androiddemos.R;

import pers.victor.androiddemos.annotations.bus.Bus;
import pers.victor.androiddemos.annotations.bus.BusMethod;

public class EventActivity extends ToolbarActivity {
    private TextView tvSend;
    private EditText etMsg;
    private TextView tvReflect;
    private TextView tvAnnotation;

    @Override
    public int bindLayout() {
        return R.layout.activity_event;
    }

    @Override
    public void initView() {
        Bus.register(this);
        tvSend = $(R.id.tv_msg_send);
        tvReflect = $(R.id.tv_msg_reflect);
        tvAnnotation = $(R.id.tv_msg_annotation);
        etMsg = $(R.id.et_msg_send);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.postAnnotation(etMsg.getText().toString());
                Bus.postReflect(etMsg.getText().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        Bus.unregister(this);
        super.onDestroy();
    }

    public void onPostReflect(String s) {
        tvReflect.setText(s);
    }

    @BusMethod
    public void onEvent(String s) {
        tvAnnotation.setText(s);
    }
}
