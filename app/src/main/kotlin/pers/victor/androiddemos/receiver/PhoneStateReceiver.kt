package pers.victor.androiddemos.receiver

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import pers.victor.androiddemos.activity.CallInterceptActivity

/**
 * Created by Victor on 16/3/27.
 */
class PhoneStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //拨打电话
            println("拨打" + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER))
        } else {
            //来电
            var tm = context!!.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager;
            when (tm.callState) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    if (intent.getStringExtra("incoming_number") != null) {
                        println("${intent.getStringExtra("incoming_number")}来电")
                        if (shouldEndCall(intent.getStringExtra("incoming_number"), context)) {
                            endCall(context, intent.getStringExtra("incoming_number"))
                            println("${intent.getStringExtra("incoming_number")}拦截")
                        }
                    }
                }
            //                TelephonyManager.CALL_STATE_OFFHOOK -> {
            //                    println("CALL_STATE_OFFHOOK")
            //                }
            //                TelephonyManager.CALL_STATE_IDLE -> {
            //                    println("CALL_STATE_IDLE")
            //                }
            }
        }
    }

    //                    var mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager;
    //                    mAudioManager.ringerMode = AudioManager.RINGER_MODE_SILENT;//静音处理

    private fun shouldEndCall(phone: String?, context: Context): Boolean {
        if (phone == null) {
            return false
        } else {
            return phone.contentEquals(CallInterceptActivity.interceptNumber)
        }
    }

    private fun endCall(context: Context, phone: String) {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val methodGetITelephony = TelephonyManager::class.java.getDeclaredMethod("getITelephony")
            methodGetITelephony.isAccessible = true
            val ITelephony = methodGetITelephony.invoke(telephonyManager)
            val methodEndCall = Class.forName(ITelephony.javaClass.name).getDeclaredMethod("endCall")
            methodEndCall.isAccessible = true
            methodEndCall.invoke(ITelephony)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    // 根据手机号得到名字
    fun getContactNameByAddress(context: Context, phoneNumber: String): String {
        val personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val cur = context.contentResolver.query(personUri, arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME), null, null, null)
        if (cur.moveToFirst()) {
            val nameIdx = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
            val name = cur.getString(nameIdx)
            cur.close()
            return name
        }
        return "未知"
    }

}