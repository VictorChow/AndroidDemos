package pers.victor.androiddemos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Victor on 16/2/13.
 */
public class SmsReceiver extends BroadcastReceiver {
    public static String SMS_FILTER = "android.provider.Telephony.SMS_RECEIVED";
    private DateFormat dateFormat;
    private OnReceivedListener onReceivedListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (dateFormat == null) {
            dateFormat = SimpleDateFormat.getDateTimeInstance();

        }
        Logger.d("收到广播");
        SmsMessage[] messages = getMessagesFromIntent(intent);
        StringBuilder sms = new StringBuilder();
        if (messages != null) {
            sms.append(messages[0].getDisplayOriginatingAddress());
            sms.append("\n\r");
            sms.append(dateFormat.format(messages[0].getTimestampMillis()));
            sms.append("\n\r");
        }
        for (SmsMessage message : messages) {
            sms.append(message.getDisplayMessageBody());

        }
        if (onReceivedListener != null) {
            onReceivedListener.onReceived(sms.toString());
        }
    }

    public final SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];
        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }

    public void setOnReceivedListener(OnReceivedListener onReceivedListener) {
        this.onReceivedListener = onReceivedListener;
    }

    public interface OnReceivedListener {
        void onReceived(String sms);
    }
}
