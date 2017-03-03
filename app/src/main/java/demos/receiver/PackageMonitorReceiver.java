package demos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import demos.util.PrintLog;

/**
 * Created by Victor on 16/2/13.
 */
public class PackageMonitorReceiver extends BroadcastReceiver {
    private List<ActionDoneCallback> callbacks;

    @Override
    public void onReceive(Context context, Intent intent) {
        int pos = intent.getIntExtra("APP_POS", -1);
        boolean isSystemApp = intent.getBooleanExtra("IS_SYSTEM_APP", false);

        //接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            PrintLog.d("安装");
            if (callbacks != null) {
                for (ActionDoneCallback callback : callbacks) {
                    callback.onAdded();
                }
            }
        }
        //接收替换广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            PrintLog.d("替换");
            if (callbacks != null) {
                for (ActionDoneCallback callback : callbacks) {
                    callback.onReplaced();
                }
            }
        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            PrintLog.d("卸载");
            if (callbacks != null) {
                for (ActionDoneCallback callback : callbacks) {
                    callback.onRemoved();
                }
            }
        }
    }

    public void addCallback(ActionDoneCallback callback) {
        if (callbacks == null) {
            callbacks = new ArrayList<>();
        }
        callbacks.add(callback);
    }

    public interface ActionDoneCallback {
        void onRemoved();

        void onAdded();

        void onReplaced();
    }
}
