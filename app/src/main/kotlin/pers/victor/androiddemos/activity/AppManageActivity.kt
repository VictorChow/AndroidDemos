package pers.victor.androiddemos.activity

import android.os.Environment
import android.os.StatFs
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import pers.victor.androiddemos.R
import pers.victor.androiddemos.view.ArcView
import kotlinx.android.synthetic.main.activity_app_manage.*

class AppManageActivity : ToolbarActivity() {
    override fun initView() {
        readSDCardAndSystem()

    }

    override fun bindLayout() = R.layout.activity_app_manage

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return true
    }

    fun readSDCardAndSystem() {
        val arcList = arrayListOf<ArcView.ArcEntity>()
        val state = Environment.getExternalStorageState()
        var sdAvail = 0f
        val sdcardDir = Environment.getExternalStorageDirectory()
        val sfSdcardDir = StatFs(sdcardDir.path)
        val sdBlockSize = sfSdcardDir.blockSizeLong
        val sdBlockCount = sfSdcardDir.blockCountLong
        val sdAvailCount = sfSdcardDir.availableBlocksLong
        if (Environment.MEDIA_MOUNTED.contentEquals(state)) {
            sdAvail = sdAvailCount * sdBlockSize / (1024 * 1024 * 1024.toFloat())
            pb_app_sd_card.max = sdBlockCount.toInt()
            pb_app_sd_card.progress = (sdBlockCount - sdAvailCount).toInt()
            tv_app_sd_card.text = String.format("SD卡 %.1f GB/%.1f GB", sdBlockSize * sdBlockCount / (1024 * 1024 * 1024.toFloat()) - sdAvailCount * sdBlockSize / (1024 * 1024 * 1024.toFloat()), sdBlockSize * sdBlockCount / (1024 * 1024 * 1024.toFloat()))
            tv_app_data_used.text = String.format("数据分区\n%.1f GB", sdBlockSize * sdBlockCount / (1024 * 1024 * 1024.toFloat()) - sdAvailCount * sdBlockSize / (1024 * 1024 * 1024.toFloat()))
            //            println("SD卡 block大小:" + sdBlockSize + ",block数目:" + sdBlockCount + ",总大小:" + sdBlockSize * sdBlockCount / (1024 * 1024 * 1024.toFloat()) + "GB")
            //            println("SD卡 可用的block数目：:" + sdAvailCount + ",剩余空间:" + sdAvailCount * sdBlockSize / (1024 * 1024 * 1024.toFloat()) + "GB")
        }
        val root = Environment.getRootDirectory()
        val sfRoot = StatFs(root.path)
        val blockSize = sfRoot.blockSizeLong
        val blockCount = sfRoot.blockCountLong
        val availCount = sfRoot.availableBlocksLong
        tv_app_system_used.text = String.format("系统分区\n%.1f GB", blockSize * blockCount / (1024 * 1024 * 1024.toFloat()) - availCount * blockSize / (1024 * 1024 * 1024.toFloat()))
        tv_app_remain.text = String.format("剩余空间\n%.1f GB", availCount * blockSize / (1024 * 1024 * 1024.toFloat()) + sdAvail)
        tv_app_arc_total.text = String.format("%.1f GB", sdBlockSize * sdBlockCount / (1024 * 1024 * 1024.toFloat()) + blockSize * blockCount / (1024 * 1024 * 1024.toFloat()))
        tv_app_arc_used.text = String.format("%.1f GB", sdBlockSize * sdBlockCount / (1024 * 1024 * 1024.toFloat()) - sdAvailCount * sdBlockSize / (1024 * 1024 * 1024.toFloat()) + blockSize * blockCount / (1024 * 1024 * 1024.toFloat()) - availCount * blockSize / (1024 * 1024 * 1024.toFloat()))


        arcList.add(ArcView.ArcEntity("数据分区", ContextCompat.getColor(this, R.color.blue2), sdBlockSize * sdBlockCount / (1024 * 1024 * 1024.toFloat()) - sdAvailCount * sdBlockSize / (1024 * 1024 * 1024.toFloat())))
        arcList.add(ArcView.ArcEntity("系统分区", ContextCompat.getColor(this, R.color.green2), blockSize * blockCount / (1024 * 1024 * 1024.toFloat()) - availCount * blockSize / (1024 * 1024 * 1024.toFloat())))
        arcList.add(ArcView.ArcEntity("剩余空间", ContextCompat.getColor(this, R.color.blue), availCount * blockSize / (1024 * 1024 * 1024.toFloat()) + sdAvail))
        arc_app_manage.setArcEntities(arcList)
        //        println("系统 可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / (1024 * 1024 * 1024.toFloat()) + "GB")
        //        println("系统 block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / (1024 * 1024 * 1024.toFloat()) + "GB")
    }

}
