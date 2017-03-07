package demos.activity;

import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import com.victor.androiddemos.R;

import demos.annotations.bind.Bind;
import demos.annotations.bind.BindClick;
import demos.annotations.bind.BindView;
import demos.receiver.TelephonyReceiver;
import demos.util.ShowToast;

public class TelephonyActivity extends ToolbarActivity {

//    <receiver android:name="demos.receiver.TelephonyReceiver">
//    <intent-filter>
//    <action android:name="android.intent.action.PHONE_STATE" />
//    <action android:name="android.intent.action.BOOT_COMPLETED" />
//    <action android:name="android.intent.action.NEW_OUTGOING_CALL" />
//    </intent-filter>
//    </receiver>

    @BindView(R.id.tv_phone_status)
    TextView tvPhoneStatus;
    private TelephonyReceiver telephonyReceiver;
    private IntentFilter filter;
    private boolean isRunning;

    @BindClick({R.id.btn_start_watch, R.id.btn_stop_watch})
    void onItemClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_watch:
                if (!isRunning) {
                    registerReceiver(telephonyReceiver, filter);
                    isRunning = true;
                    ShowToast.shortToast("已开启监听");
                    tvPhoneStatus.setText("状态: 已开启");
                } else {
                    ShowToast.shortToast("已经在监听了");
                }
                break;
            case R.id.btn_stop_watch:
                if (isRunning) {
                    unregisterReceiver(telephonyReceiver);
                    isRunning = false;
                    ShowToast.shortToast("已关闭监听");
                    tvPhoneStatus.setText("状态: 未开启");
                } else {
                    ShowToast.shortToast("未开启监听");
                }
                break;
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_telephony;
    }

    @Override
    public void initView() {
        Bind.bind(this);
        telephonyReceiver = new TelephonyReceiver();
        filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
    }
}
