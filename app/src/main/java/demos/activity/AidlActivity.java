package demos.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.victor.androiddemos.IMyActivity;
import com.victor.androiddemos.IMyService;
import com.victor.androiddemos.R;

import demos.annotations.bind.Bind;
import demos.annotations.bind.BindClick;
import demos.annotations.bind.BindView;
import demos.service.CountService;
import demos.util.ShowToast;

public class AidlActivity extends ToolbarActivity {

    @BindView(R.id.tv_show)
    TextView tvShow;

    private boolean isBind;
    private Intent intent;
    private IMyService iMyService;
    private IMyActivity iMyActivity = new IMyActivity.Stub() {

        @Override
        public void showToast(String number) throws RemoteException {
            tvShow.setText(String.valueOf(number));
            ShowToast.shortToast(number);
        }
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyService = IMyService.Stub.asInterface(service);
            try {
                iMyService.registerActivity(iMyActivity);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @BindClick({R.id.btn_on, R.id.btn_off, R.id.btn_bind, R.id.btn_unbind, R.id.btn_show})
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
                isBind = true;
                break;
            case R.id.btn_unbind:
                if (isBind) {
                    unbindService(connection);
                    isBind = false;
                } else {
                    ShowToast.shortToast("未进行绑定");
                }
                break;
            case R.id.btn_show:
                if (iMyService == null) {
                    ShowToast.shortToast("未绑定Service");
                } else {
                    try {
                        iMyService.getNumber();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_aidl;
    }

    @Override
    public void initView() {
        intent = new Intent(getApplicationContext(), CountService.class);
        Bind.bind(this);
    }

    @Override
    protected void onDestroy() {
        stopService(intent);
        if (isBind) {
            unbindService(connection);
        }
        super.onDestroy();
    }
}
