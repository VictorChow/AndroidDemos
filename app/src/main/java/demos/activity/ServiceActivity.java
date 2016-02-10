package demos.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.victor.androiddemos.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demos.service.CountService;
import demos.util.ShowToast;

public class ServiceActivity extends AppCompatActivity {

    @Bind(R.id.tv_show)
    TextView tvShow;

    @OnClick({R.id.btn_on, R.id.btn_off, R.id.btn_bind, R.id.btn_unbind, R.id.btn_show})
    void onItemClick(View v) {
        switch (v.getId()) {
            case R.id.btn_on:
                startService(intent);
                break;
            case R.id.btn_off:
                stopService(intent);
                break;
            case R.id.btn_bind:
                bindService(intent, connection, BIND_AUTO_CREATE);
                isBound = true;
                break;
            case R.id.btn_unbind:
                if (isBound) {
                    unbindService(connection);
                } else {
                    ShowToast.shortToast("未进行绑定");
                }
                break;
            case R.id.btn_show:
                if (service == null) {
                    ShowToast.shortToast("未绑定Service");
                } else {
                    ShowToast.shortToast(service.getCount());
                }
                break;

        }
    }

    private boolean isBound;
    private CountService service;
    private Intent intent;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceActivity.this.service = ((CountService.ServiceBinder) service).getService();
            ServiceActivity.this.service.setTextView(tvShow);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        intent = new Intent(getApplicationContext(), CountService.class);
        ButterKnife.bind(this);
    }
}
