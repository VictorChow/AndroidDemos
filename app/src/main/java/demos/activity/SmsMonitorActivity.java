package demos.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.victor.androiddemos.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demos.service.SmsService;
import demos.util.ShowToast;

public class SmsMonitorActivity extends BaseActivity {
    private SmsService smsService;
    private Intent intent;
    private boolean isRunning;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            smsService = ((SmsService.SmsBinder) service).getService();
            smsService.getSmsReceiver().setOnReceivedListener(sms -> tvShowSms.setText(sms));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            smsService = null;
        }
    };


    @Bind(R.id.tv_show_sms)
    TextView tvShowSms;
    @Bind(R.id.tv_sms_status)
    TextView tvSmsStatus;

    @OnClick({R.id.btn_start_watch, R.id.btn_stop_watch})
    void onItemClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_watch:
                if (!isRunning) {
                    bindService(intent, connection, BIND_AUTO_CREATE);
                    isRunning = true;
                    ShowToast.shortToast("已开启监视");
                    tvSmsStatus.setText("状态: 已开启");
                } else {
                    ShowToast.shortToast("已经在监视了");
                }
                break;
            case R.id.btn_stop_watch:
                if (isRunning) {
                    unbindService(connection);
                    ShowToast.shortToast("已关闭监视");
                    tvSmsStatus.setText("状态: 未开启");
                    isRunning = false;
                } else {
                    ShowToast.shortToast("未开启监视");
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_monitor);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("新短信监听");
        }
        ButterKnife.bind(this);
        intent = new Intent(mContext, SmsService.class);
    }


}
