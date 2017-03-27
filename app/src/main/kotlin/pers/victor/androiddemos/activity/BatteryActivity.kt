package pers.victor.androiddemos.activity

import android.animation.ArgbEvaluator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.os.BatteryManager
import pers.victor.androiddemos.R
import kotlinx.android.synthetic.main.activity_battery.*

class BatteryActivity : ToolbarActivity() {
    private var batteryStateReceiver: BroadcastReceiver? = null
    private var batteryHealth = ""
    private var batteryLevel = ""
    private var batteryTemperature = ""
    private var batteryVoltage = ""
    private var batteryTechnique = ""
    private var colorArray = arrayOf(0xFFE84E40.toInt(), 0xFF2BAF2B.toInt())

    override fun bindLayout() = R.layout.activity_battery

    override fun initView() {
        init()
    }

    override fun onDestroy() {
        unregisterReceiver(batteryStateReceiver)
        super.onDestroy()
    }

    private fun init() {
        batteryStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent!!.getIntExtra("level", 0)
                if (level == -1) {
                    tv_battery_level.text = "NaN"
                } else {
                    tv_battery_level.text = level.toString()
                }
                ll_battery_top.background = ColorDrawable(ArgbEvaluator().evaluate(level.toFloat() / 100, colorArray[0], colorArray[1]) as Int)
                batteryLevel = level.toString() + "%"
                wv_battery_level.setProgress(level)

                tv_battery_status.text = when (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
                    BatteryManager.BATTERY_STATUS_CHARGING -> {
                        when (intent.getIntExtra("plugged",
                                BatteryManager.BATTERY_PLUGGED_AC)) {
                            BatteryManager.BATTERY_PLUGGED_AC -> "充电中 (适配器充电)"
                            BatteryManager.BATTERY_PLUGGED_USB -> "充电中 (USB充电)"
                            else -> "充电中"
                        }
                    }
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> "放电中"
                    BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "未充电"
                    BatteryManager.BATTERY_STATUS_FULL -> "已充满"
                    else -> "未知状态"
                }

                batteryHealth = when (intent.getIntExtra("health",
                        BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
                    BatteryManager.BATTERY_HEALTH_GOOD -> "状态良好"
                    BatteryManager.BATTERY_HEALTH_DEAD -> "电池没电"
                    BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "电压过高"
                    BatteryManager.BATTERY_HEALTH_OVERHEAT -> "电池过热"
                    else -> "未知状态"
                }

                batteryTechnique = intent.getStringExtra("technology")
                batteryVoltage = String.format("%.2fV", intent.getIntExtra("voltage", 0).toFloat() / 1000);
                batteryTemperature = String.format("%.1f℃", intent.getIntExtra("temperature", 0).toFloat() / 10);

                tv_battery_detail_health.text = batteryHealth
                tv_battery_detail_temperature.text = batteryTemperature
                tv_battery_detail_technique.text = batteryTechnique
                tv_battery_detail_voltage.text = batteryVoltage
            }
        }
        registerReceiver(batteryStateReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }
}