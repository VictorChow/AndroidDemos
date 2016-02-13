package demos.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.victor.androiddemos.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demos.receiver.TelephonyReceiver;
import demos.util.ShowToast;

public class TelephonyActivity extends BaseActivity {

//    <receiver android:name="demos.receiver.TelephonyReceiver">
//    <intent-filter>
//    <action android:name="android.intent.action.PHONE_STATE" />
//    <action android:name="android.intent.action.BOOT_COMPLETED" />
//    <action android:name="android.intent.action.NEW_OUTGOING_CALL" />
//    </intent-filter>
//    </receiver>

    private TelephonyReceiver telephonyReceiver;
    private IntentFilter filter;
    private boolean isRunning;

    @Bind(R.id.tv_phone_status)
    TextView tvPhoneStatus;

    @OnClick({R.id.btn_start_watch, R.id.btn_stop_watch})
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephony);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("通话录音");
        }
        telephonyReceiver = new TelephonyReceiver();
        filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
    }
}
