package pers.victor.androiddemos.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;

import pers.victor.androiddemos.activity.CallInterceptActivity;

/**
 * Created by Victor on 2017/4/1. (ง •̀_•́)ง
 */

public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //拨打电话
            Logger.e("拨打" + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
        } else {
            //来电
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (intent.getStringExtra("incoming_number") != null) {
                        Logger.e(intent.getStringExtra("incoming_number") + "来电");
                        if (shouldEndCall(intent.getStringExtra("incoming_number"))) {
                            endCall(context);
                            Logger.e(intent.getStringExtra("incoming_number") + "拦截");
                        }
                    }
                    break;
//                TelephonyManager.CALL_STATE_OFFHOOK -> {
//                    println("CALL_STATE_OFFHOOK")
//                }
//                TelephonyManager.CALL_STATE_IDLE -> {
//                    println("CALL_STATE_IDLE")
//                }
            }
        }
    }

    private boolean shouldEndCall(String phone) {
        return phone != null && phone.contentEquals(CallInterceptActivity.interceptNumber);
    }

    private void endCall(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method methodGetITelephony = TelephonyManager.class.getDeclaredMethod("getITelephony");
            methodGetITelephony.setAccessible(true);
            Object ITelephony = methodGetITelephony.invoke(telephonyManager);
            Method methodEndCall = Class.forName(ITelephony.getClass().getName()).getDeclaredMethod("endCall");
            methodEndCall.setAccessible(true);
            methodEndCall.invoke(ITelephony);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据手机号得到名字
    private String getContactNameByAddress(Context context, String phoneNumber) {
        Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cur = context.getContentResolver().query(personUri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cur.moveToFirst()) {
            int nameIdx = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cur.getString(nameIdx);
            cur.close();
            return name;
        }
        return "未知";
    }

}
