package pers.victor.androiddemos.activity

import android.os.AsyncTask
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import pers.victor.androiddemos.R
import pers.victor.androiddemos.adapter.CacheListAdapter
import pers.victor.androiddemos.annotations.bus.Bus
import pers.victor.androiddemos.annotations.bus.BusMethod
import pers.victor.androiddemos.module.AppModule
import kotlinx.android.synthetic.main.activity_clear_cache.*
import java.io.File
import java.math.BigDecimal


class ClearCacheActivity : ToolbarActivity() {

    //*/
    // * /data/data/package_name/files（比较严格的清理策略时也可以选择清理）
    // * 其他一些/data/data/ package_name /app_webview（webView缓存）
    // * 其他一些/data/data/ package_name /*cache目录（应用缓存）
    // * /data/data/package_name/database/webviewCache.db*（WebView缓存）
    // * /data/data/package_name/database/webview.db*（WebView缓存）
    // * /mnt/sdcard/Android/ package_name /cache（外部应用缓存，2.2以后支持）
    // * /data/data/ package_name /cache（应用缓存）
    ///**

    private var appList = arrayListOf<AppModule>()
    private var cacheList = arrayListOf<AppModule>()
    private var cacheSizeList = arrayListOf<String>()
    private var dirList = arrayListOf<String>()
    private var cacheSize: Double = 0.toDouble()
    private lateinit var adapter: CacheListAdapter


    override fun bindLayout() = R.layout.activity_clear_cache

    override fun initView() {
        Bus.register(this)

        rv_cache_list.layoutManager = GridLayoutManager(this, 1)
        adapter = CacheListAdapter(this, cacheList, cacheSizeList)
        rv_cache_list.adapter = adapter

        dirList.add("/data/data/xxx/cache")
        dirList.add("/data/data/xxx/app_webview")
        dirList.add("/mnt/sdcard/Android/data/xxx/cache")

        ClearCacheTask().execute(true);

        //        /data/data/com.victor.androiddemos/files
        //        PrintLog.d("位置是  " + mContext.getFilesDir());
        //        PrintLog.d("位置是  " + mContext.getCacheDir());
        //
        //        File file = new File("/data/data/com.victor.androiddemos/cache");
        //        PrintLog.d("大小是  " + formatCacheSize(getFolderSize(file)));
        //        deleteFolderFile("/data/data/com.victor.androiddemos/shared_prefs", true);
    }

    override fun onDestroy() {
        Bus.unregister(this)
        super.onDestroy()
    }

    /**
     * 删除指定目录下文件及目录
     */
    fun deleteFolderFile(filePath: String, deleteThisPath: Boolean) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                val file = File(filePath)
                if (!file.exists()) {
                    return
                }
                if (file.isDirectory) {
                    // 处理目录
                    val files = file.listFiles()
                    for (i in files.indices) {
                        deleteFolderFile(files[i].absolutePath, true)
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory) {
                        // 如果是文件，删除
                        file.delete()
                    } else {
                        // 目录
                        if (file.listFiles().size == 0) {
                            // 目录下没有文件或者目录，删除
                            file.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return true
    }

    private fun getPackages() {
        val packages = packageManager.getInstalledPackages(0)
        for (i in packages.indices) {
            val packageInfo = packages[i]
            val tmpInfo = AppModule()
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            tmpInfo.packageName = packageInfo.packageName
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(packageManager)
            appList.add(tmpInfo)
        }
    }

    @BusMethod()
    fun finishedClearCache(msg: String) {
        when (msg) {
            "scan" -> {
                btn_cache_scan.visibility = View.VISIBLE
                tv_cache_app.text = "扫描完成";
                tv_cache_package.text = "";
                btn_cache_scan.text = "一键清理 (${tv_cache_size.text})"
                btn_cache_scan.setOnClickListener { ClearCacheTask().execute(false) }
            }
            "clear" -> {
                btn_cache_scan.visibility = View.GONE
                tv_cache_app.text = "清理完成"
                tv_cache_size.text = "\n(╯‵□′)╯︵┻━┻"
                cacheList.clear()
                cacheSizeList.clear()
                adapter.notifyDataSetChanged()
                Snackbar.make(window.decorView, "效果上是删除了, 谨慎起见, 暂时不真删文件", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    private inner class ClearCacheTask : AsyncTask<Boolean, String, String>() {
        override fun doInBackground(vararg isScan: Boolean?): String {
            val sb = StringBuilder()
            if (appList.size == 0) {
                publishProgress("正在初始化", "0KB")
                getPackages()
            }
            if (isScan[0]!!) {
                //扫描
                for (appModule in appList) {
                    val preSize = cacheSize
                    for (dir in dirList) {
                        val path = dir.replace("xxx", appModule.packageName!!)
                        cacheSize += getFolderSize(File(path))
                        publishProgress("正在扫描: " + appModule.appName!!, dir)
                    }
                    if (cacheSize - preSize > 0) {
                        sb.append(appModule.appName).append(": ").append(formatCacheSize((cacheSize - preSize))).append("\n")
                        cacheList.add(appModule)
                        cacheSizeList.add(formatCacheSize((cacheSize - preSize)))
                        publishProgress(sb.toString())
                    }
                }
                Bus.postAnnotation("scan")
            } else {
                //清理
                Bus.postAnnotation("clear")
            }
            return "扫描完成"
        }


        override fun onProgressUpdate(vararg values: String) {
            if (values.size > 1) {
                //显示实时
                tv_cache_app.text = values[0];
                tv_cache_package.text = values[1];
                tv_cache_size.text = formatCacheSize(cacheSize);
            }
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * 获取文件夹大小
         */
        fun getFolderSize(file: File): Long {
            if (!file.exists()) {
                return 0
            }
            var size: Long = 0
            try {
                val fileList = file.listFiles() ?: return 0
                for (i in fileList.indices) {
                    if (fileList[i].isDirectory) {
                        size += getFolderSize(fileList[i])
                    } else {
                        size += fileList[i].length()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return size
        }

        /**
         * 格式化单位
         */
        fun formatCacheSize(size: Double): String {
            val kiloByte = size / 1024
            if (kiloByte < 1) {
                return size.toString() + "B"
            }

            val megaByte = kiloByte / 1024
            if (megaByte < 1) {
                val result1 = BigDecimal(java.lang.Double.toString(kiloByte.toDouble()))
                return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
            }

            val gigaByte = megaByte / 1024
            if (gigaByte < 1) {
                val result2 = BigDecimal(java.lang.Double.toString(megaByte.toDouble()))
                return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
            }

            val teraBytes = gigaByte / 1024
            if (teraBytes < 1) {
                val result3 = BigDecimal(java.lang.Double.toString(gigaByte.toDouble()))
                return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
            }
            val result4 = BigDecimal(teraBytes)
            return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
        }
    }
}
