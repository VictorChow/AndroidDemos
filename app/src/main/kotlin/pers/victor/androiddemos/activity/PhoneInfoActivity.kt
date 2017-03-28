package pers.victor.androiddemos.activity

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import pers.victor.androiddemos.R
import kotlinx.android.synthetic.main.activity_phone_info.*
import kotlinx.android.synthetic.main.item_phone_info.view.*
import pers.victor.androiddemos.util.CPUTool

class PhoneInfoActivity : ToolbarActivity() {

    private var map = hashMapOf<Int, String>()

    override fun bindLayout() = R.layout.activity_phone_info

    override fun initView() {
        init()
    }

    private fun initMap() {
        map.put(Sensor.TYPE_ACCELEROMETER, "加速度传感器")
        map.put(Sensor.TYPE_MAGNETIC_FIELD, "磁力传感器")
        map.put(Sensor.TYPE_ORIENTATION, "方向传感器")
        map.put(Sensor.TYPE_GYROSCOPE, "陀螺仪传感器")
        map.put(Sensor.TYPE_LIGHT, "光线感应传感器")
        map.put(Sensor.TYPE_PRESSURE, "压力传感器")
        map.put(Sensor.TYPE_AMBIENT_TEMPERATURE, "温度传感器")
        map.put(Sensor.TYPE_PROXIMITY, "距离传感器")
        map.put(Sensor.TYPE_GRAVITY, "重力传感器")
        map.put(Sensor.TYPE_LINEAR_ACCELERATION, "线性加速度传感器")
        map.put(Sensor.TYPE_ROTATION_VECTOR, "旋转矢量传感器")
        map.put(Sensor.TYPE_RELATIVE_HUMIDITY, "湿度传感器")
    }

    private fun init() {
        var view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "品牌"
        view.tv_item_phone_info_right.text = Build.BRAND
        ll_phone_info_container.addView(view)

        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "型号"
        view.tv_item_phone_info_right.text = Build.MODEL
        ll_phone_info_container.addView(view)

        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "Android版本"
        view.tv_item_phone_info_right.text = Build.VERSION.RELEASE
        ll_phone_info_container.addView(view)

        val info = ActivityManager.MemoryInfo()
        (getSystemService(ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(info)
        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "内存"
        view.tv_item_phone_info_right.text = String.format("%.1f GB", (info.totalMem shr 10).toFloat() / 1024 / 1024)
        ll_phone_info_container.addView(view)

        val sdcardDir = Environment.getExternalStorageDirectory()
        val sfSdcardDir = StatFs(sdcardDir.path)
        val sdBlockSize = sfSdcardDir.blockSizeLong
        val sdBlockCount = sfSdcardDir.blockCountLong
        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "存储空间"
        view.tv_item_phone_info_right.text = String.format("%.1f GB", sdBlockSize * sdBlockCount / (1024 * 1024 * 1024.toFloat()))
        ll_phone_info_container.addView(view)

        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "IMEI"
        view.tv_item_phone_info_right.text = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
        ll_phone_info_container.addView(view)

        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "CPU最低频率"
        view.tv_item_phone_info_right.text = "${CPUTool.getMinCpuFreq() / 1000} MHz"
        ll_phone_info_container.addView(view)

        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "CPU最高频率"
        view.tv_item_phone_info_right.text = "${CPUTool.getMaxCpuFreq() / 1000} MHz"
        ll_phone_info_container.addView(view)

        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "CPU核心数"
        view.tv_item_phone_info_right.text = CPUTool.getNumCores().toString()
        ll_phone_info_container.addView(view)

        val dm = DisplayMetrics();
        windowManager.defaultDisplay.getMetrics(dm);

        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "分辨率"
        view.tv_item_phone_info_right.text = "${dm.heightPixels} × ${dm.widthPixels}"
        ll_phone_info_container.addView(view)

        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "像素密度"
        view.tv_item_phone_info_right.text = dm.densityDpi.toString()
        ll_phone_info_container.addView(view)

        view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
        view.tv_item_phone_info_left.text = "多点触控"
        view.tv_item_phone_info_right.text = if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) "支持" else "不支持"
        ll_phone_info_container.addView(view)

        initMap()
        val manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val listSensor = manager.getSensorList(Sensor.TYPE_ALL)
        map.forEach {
            var entry = it
            var isFind = false
            listSensor.forEach {
                if (it.type == entry.key) {
                    isFind = true
                }
            }
            view = layoutInflater.inflate(R.layout.item_phone_info, ll_phone_info_container, false)
            view.tv_item_phone_info_left.text = entry.value
            view.tv_item_phone_info_right.text = if (isFind) "支持" else "不支持"
            ll_phone_info_container.addView(view)
        }
    }

}
