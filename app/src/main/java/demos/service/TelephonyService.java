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

    private static final String TAG = "TelephonyService";
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AndroidDemos.getInstance());
    private String sendMsgPhoneNum = sharedPreferences.getString("phoneNum", "");

    @Override
    public void onCreate() {
        //取得TELEPHONY服务
        telephonyManager = (TelephonyManager) super.getSystemService(Context.TELEPHONY_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        incomingFlag = intent.getBooleanExtra("incomingFlag", false);
        if (incomingFlag) {
            incomeNumber = intent.getStringExtra("incoming_number");
        } else {
            outPhone = intent.getStringExtra("outphone");
        }
        //电话临听
        telephonyManager.listen(new PhoneStateListenerOk(), PhoneStateListener.LISTEN_CALL_STATE);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class PhoneStateListenerOk extends PhoneStateListener {

        private MediaRecorder recorder;
        private boolean isRecord;

        @SuppressLint("SdCardPath")
        @Override
        public void onCallStateChanged(int state, String inPhone) {

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://无效状态
                    incomeNumber = null;
                    if (recorder != null && isRecord) {
                        recorder.stop();
                        recorder.release();
                        isRecord = false;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接起电话时
                    String telNum;
                    if (incomingFlag) {
                        telNum = incomeNumber;
                    } else {
                        telNum = outPhone;
                    }
                    if (telNum == null) {
                        break;
                    }
                    String SD_PATH = Environment.getExternalStorageDirectory() + "/";
                    try {
                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
                        String fileName = telNum + "_" + format.format(new Date()) + ".mp3";
                        recorder.setOutputFile(SD_PATH + fileName);
                        recorder.prepare();
                        recorder.start();
                        isRecord = true;
                        Log.i(TAG, "record start");
                    } catch (Exception e) {
                        Log.i(TAG, e.toString());
                        e.printStackTrace();
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:/*电话进来时*/
                    Log.i(TAG, sendMsgPhoneNum + " " + incomeNumber + " 呼入");
                    Log.i(TAG, " CALL_STATE_RINGING");
                    break;
            }
        }
    }
}