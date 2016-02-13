package demos.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import demos.service.TelephonyService;

/**
 * Created by Victor on 16/2/13.
 */
public class TelephonyReceiver extends BroadcastReceiver {
    private static final String TAG = "BroadcastReceiverUtil";
    private static boolean incoming_Flag = false;
    private static String incoming_number = null;
    private static String out_Phone = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        //如果有新电话呼出，其Action为“ACTION_NEW_OUTGOING_CALL”，
        //其呼出号码由outPhone=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)获得
        //并启动服务
        //监听电话的状态
        Intent intentPhone = new Intent(context, TelephonyService.class);
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {    //如果是拨打电话
            incoming_Flag = false;
            out_Phone = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            intentPhone.putExtra("incomingFlag", incoming_Flag);
            intentPhone.putExtra("outphone", out_Phone);
            context.startService(intentPhone);
        } else {
            //如果有新电话呼入，直接启动服务
            //如果是来电 电话状态：IDLE  （空闲） RING（来电）  OFFHOOK（接听）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            incoming_Flag = true;//标识当前是来电
            incoming_number = intent.getStringExtra("incoming_number");
            intentPhone.putExtra("incomingFlag", incoming_Flag);
            intentPhone.putExtra("incoming_number", incoming_number);
            context.startService(intentPhone);
        }

    }

}
