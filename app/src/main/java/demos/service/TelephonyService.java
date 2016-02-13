package demos.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import demos.AndroidDemos;

public class TelephonyService extends Service {
    private TelephonyManager telephonyManager = null;
    private String outPhone;
    private String incomeNumber;
    private boolean incomingFlag = false;

    private Intent intent = null;
    private static final String TAG = "TelephonyService";
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AndroidDemos.getInstance());
    private String sendMsgPhoneNum = sharedPreferences.getString("phoneNum", "");

    @Override
    public void onCreate() {

        //取得TELEPHONY服务
        telephonyManager = (TelephonyManager) super.getSystemService(Context.TELEPHONY_SERVICE);

        //telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();

    }

    //@Override
    //  public int onStartCommand(Intent intent, int flags, int startId) {
    //     Log.i(TAG, "onStartCommand called.");
    //获取呼出电话，从广播接收器传过来的值
    //  outPhone=intent.getStringExtra("outphone");  
    //   incomeNumber=intent.getStringExtra("inphone");
    //  return super.onStartCommand(intent, flags, startId);  
// }  


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        System.out.println("onstartcommand");

        incomingFlag = intent.getBooleanExtra("incomingFlag", false);
        if (incomingFlag) {
            incomeNumber = intent.getStringExtra("incoming_number");

        } else {
            outPhone = intent.getStringExtra("outphone");
//            new SmsUtil(TelephonyService.this, TelephonyService.this.intent).sendMsg(sendMsgPhoneNum, outPhone, "呼出");
            Log.i(TAG, sendMsgPhoneNum + " " + outPhone + " 呼出");
        }

        //电话临听
        telephonyManager.listen(new PhoneStateListenerOk(), PhoneStateListener.LISTEN_CALL_STATE);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        this.intent = intent;
        return null;
    }


    private class PhoneStateListenerOk extends PhoneStateListener {

        private MediaRecorder recorder;
        private boolean isrecord;

        @SuppressLint("SdCardPath")
        @Override
        public void onCallStateChanged(int state, String inPhone) {


            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://无效状态
                    Log.i(TAG, "CALL_STATE_IDLE");
                    incomeNumber = null;
                    if (recorder != null && isrecord) {
                        recorder.stop();
                        Log.i(TAG, "record finish");
                        recorder.release();
                        isrecord = false;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接起电话时
                    Log.i(TAG, "CALL_STATE_OFFHOOK");
                    String telNum = null;
                    if (incomingFlag) {
//                        new SmsUtil(TelephonyService.this, TelephonyService.this.intent)
//                                .sendMsg(sendMsgPhoneNum, incomeNumber, "接听电话");
                        telNum = incomeNumber;
                        Log.i(TAG, sendMsgPhoneNum + " " + incomeNumber + " 接听电话");
                    } else {
//                        new SmsUtil(TelephonyService.this, TelephonyService.this.intent)
//                                .sendMsg(sendMsgPhoneNum, outPhone, " 拨打完毕挂断");
                        telNum = outPhone;
                        //Log.i(TAG, sendMsgPhoneNum + " " + outPhone + " 拨打完毕挂断");
                    }
                    String SDPATH = Environment.getExternalStorageDirectory() + "/";
                    try {
                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
                        String fileName = telNum + "_" + format.format(new Date()) + ".mp3";
                        //	SDPATH
                        recorder.setOutputFile(SDPATH + fileName);
                        //	recorder.setOutputFile("/sdcard/"+fileName);
                        recorder.prepare();
                        recorder.start();
                        isrecord = true;
                        Log.i(TAG, "record start");
                    } catch (Exception e) {
                        Log.i(TAG, e.toString());
                        e.printStackTrace();
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:/*电话进来时*/
                    Log.i(TAG, sendMsgPhoneNum + " " + incomeNumber + " 呼入");
                    Log.i(TAG, " CALL_STATE_RINGING");
//                    new SmsUtil(TelephonyService.this, TelephonyService.this.intent)
//                            .sendMsg(sendMsgPhoneNum, incomeNumber, "呼入");
                    break;

            }
        }
    }


    public static class SmsUtil {
        private Context context = null;
        private Intent intent = null;

        public SmsUtil(Context context, Intent intent) {
            this.context = context;
            this.intent = intent;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AndroidDemos.getInstance());


        //发送短信(接收手机号码，监听号码，类型）
//        public void sendMsg(String receivePhone, String phoneNumber, String type) {
//            if (!sharedPreferences.getString("lastType", "").equals(type) && phoneNumber != null) {
//                SmsManager smsManager = SmsManager.getDefault();
//                PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, this.intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                String content = "电话号码："
//                        + phoneNumber
//                        + "\n类型："
//                        + type
//                        + "\n操作时间："
//                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(new Date());
//                smsManager.sendTextMessage(receivePhone, null, content, pendingIntent, null);
//                Log.i("TelephonyService 发短信", receivePhone + " " + phoneNumber + " " + type);
//                sharedPreferences.edit().putString("lastType", type).apply();
//            }
//        }
    }
}