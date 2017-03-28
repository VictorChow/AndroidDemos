package pers.victor.androiddemos.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import pers.victor.androiddemos.receiver.SmsReceiver;

/**
 * Created by Victor on 16/2/13.
 */
public class SmsService extends Service {
    private SmsReceiver smsReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SmsBinder();
    }

    @Override
    public void onCreate() {
        smsReceiver = new SmsReceiver();
        registerReceiver(smsReceiver, new IntentFilter(SmsReceiver.SMS_FILTER));
        Logger.d("注册广播");
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(smsReceiver);
        Logger.d("解注册广播");
    }

    public SmsReceiver getSmsReceiver() {
        return smsReceiver;
    }

    public class SmsBinder extends Binder {
        public SmsService getService() {
            return SmsService.this;
        }
    }

}
