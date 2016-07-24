package demos.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.TextView;

import demos.util.PrintLog;

public class CountService extends Service implements Runnable {
    private ServiceBinder binder;
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

    @Override
    public void onCreate() {
        new Thread(this).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new ServiceBinder();
        }
        return binder;
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
