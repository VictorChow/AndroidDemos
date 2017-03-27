package pers.victor.androiddemos.activity

import android.os.AsyncTask
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import pers.victor.androiddemos.R
import pers.victor.androiddemos.adapter.ApkListAdapter
import pers.victor.androiddemos.annotations.bus.Bus
import pers.victor.androiddemos.annotations.bus.BusMethod
import pers.victor.androiddemos.module.ApkModule
import pers.victor.androiddemos.util.ApkUtil
import kotlinx.android.synthetic.main.activity_apk_list.*
import java.io.File

class ApkListActivity : ToolbarActivity() {
    private var apkList = arrayListOf<ApkModule>()
    private var adapter: ApkListAdapter? = null
    private var searchTask: SearchTask? = null
    private var isSearching: Boolean? = null
    private var searchType = 1;
    private var largeLength = 1024 * 1024 * 20.toLong() //20M

    override fun bindLayout() = R.layout.activity_apk_list

    override fun initView() {
        Bus.register(this)

        init()
        searchFile()
    }

    companion object {
        val SEARCH_APK = 1
        val SEARCH_LARGE_FILES = 2
    }

    override fun onDestroy() {
        Bus.unregister(this)
        if (!searchTask!!.isCancelled)
            searchTask!!.cancel(true)
        super.onDestroy()
    }

    private fun init() {
        searchType = intent.getIntExtra("search_type", SEARCH_APK)

        rv_sdk_list.layoutManager = LinearLayoutManager(this)
        adapter = ApkListAdapter(this, apkList, object : ApkListAdapter.OnItemDeleteListener {
            override fun itemDelete() {
                updateState()
            }
        })
        rv_sdk_list.adapter = adapter
    }


    fun searchFile() {
        searchTask = SearchTask(".apk", Environment.getExternalStorageDirectory())
        searchTask!!.execute()
    }


    private fun searchFile(keyword: String, filepath: File) {
        if (!isSearching!!) {
            return
        }
        //判断SD卡是否存在
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val files = filepath.listFiles()
            if (files.size > 0) {
                for (file in files) {
                    if (file.isDirectory) {
                        //微信和QQ里面东西太多了!!!直接放弃扫描~ MicroMsg MobileQQ
                        if (file.path.contains("tencent"))
                            continue
                        //如果目录可读就执行（一定要加，不然会挂掉）
                        if (file.canRead())
                            searchFile(keyword, file)
                    } else {
                        //判断是文件，则进行文件名判断
                        if (file.name.contains(keyword) || file.name.contains(keyword.toUpperCase())) {
                            println(file.path)
                            apkList.add(ApkModule(file.name, file.path, ApkUtil.getApkIcon(this, file.path), String.format("%.1f MB", file.length() / 1024 / 1024.toFloat())))
                            Bus.postAnnotation("找到了新的Apk")
                        }
                    }
                }
            }
        }
    }

    private fun searchLargeFiles(maxLength: Long, filepath: File) {
        if (!isSearching!!) {
            return
        }
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val files = filepath.listFiles()
            if (files.isNotEmpty()) {
                for (file in files) {
                    if (file.isDirectory) {
                        if (file.canRead())
                            searchLargeFiles(maxLength, file)
                    } else {
                        if (file.length() >= maxLength) {
                            apkList.add(ApkModule(file.name, file.path, ContextCompat.getDrawable(this, R.drawable.ic_launcher), String.format("%.1f MB", file.length() / 1024 / 1024.toFloat())))
                            Bus.postAnnotation("找到了大文件")
                        }
                    }
                }
            }
        }
    }

    @BusMethod
    fun findNewApk(msg: String) {
        println(msg)
        adapter!!.notifyDataSetChanged()
        supportActionBar!!.title = if (searchType == SEARCH_APK) "扫描APK (${apkList.size})" else "扫描大文件 (${apkList.size})"
    }

    inner class SearchTask(val keyword: String, val filepath: File) : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            isSearching = true
        }

        override fun doInBackground(vararg params: Void): Void? {
            if (!isSearching!!)
                return null
            if (searchType == SEARCH_APK) {
                searchFile(keyword, filepath)
            } else {
                searchLargeFiles(largeLength, filepath)
            }
            return null
        }

        override fun onPostExecute(p: Void?) {
            isSearching = false
            adapter!!.notifyDataSetChanged()
            pb_sdk_list.visibility = View.GONE
            updateState()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (isSearching!!) {
            Snackbar.make(window.decorView, "还未扫描完, 确定退出么?", Snackbar.LENGTH_SHORT)
                    .setAction("确定", {
                        finishTask()
                        finish()
                    })
                    .show()
            return false
        } else {
            finishTask()
            finish()
            return true
        }
    }

    private fun finishTask() {
        isSearching = false
        if (searchTask != null && searchTask!!.status == AsyncTask.Status.RUNNING) {
            searchTask!!.cancel(true);
        }
    }

    override fun onBackPressed() {
        onOptionsItemSelected(null)
    }

    private fun updateState() {
        if (apkList.size == 0) {
            tv_sdk_list_none.visibility = View.VISIBLE
            if (searchType == SEARCH_APK) {
                supportActionBar!!.title = "扫描APK"
            } else {
                supportActionBar!!.title = "扫描大文件"
                tv_sdk_list_none.text = "暂无大文件"
            }
        } else {
            if (searchType == SEARCH_APK) {
                supportActionBar!!.title = "扫描APK (${apkList.size})"
            } else {
                supportActionBar!!.title = "扫描大文件 (${apkList.size})"
            }
        }
    }

    fun getSearchType() = searchType
}
