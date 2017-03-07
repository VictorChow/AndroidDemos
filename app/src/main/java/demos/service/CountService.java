package demos.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.TextView;

import com.victor.androiddemos.IMyActivity;
import com.victor.androiddemos.IMyService;

import demos.util.PrintLog;

public class CountService extends Service implements Runnable {
    private TextView textView;
    private boolean threadDisable;
    private int count;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (textView != null) {
                textView.setText(String.valueOf(count));
            }
        }
    };

    private IMyActivity iMyActivity;
    private IBinder iMyService = new IMyService.Stub() {

        @Override
        public int getNumber() throws RemoteException {
            return getCount();
        }

        @Override
        public void registerActivity(IMyActivity iActivity) throws RemoteException {
            iMyActivity = iActivity;
        }
    };
    private ServiceBinder binder;

    @Override
    public void onCreate() {
        new Thread(this).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
//        if (binder == null) {
//            binder = new ServiceBinder();
//        }
//        return binder;
        return iMyService;
    }

    @Override
    public void onDestroy() {
        threadDisable = true;
    }

    @Override
    public void run() {
        while (!threadDisable) {
            try {
                Thread.sleep(1000);
                count++;
                PrintLog.d("service count = " + count);
                handler.sendMessage(new Message());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCount() {
        if (iMyActivity != null) {
            try {
                iMyActivity.showToast("远端: " + count);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public class ServiceBinder extends Binder {
        public CountService getService() {
            return CountService.this;
        }
    }
}
