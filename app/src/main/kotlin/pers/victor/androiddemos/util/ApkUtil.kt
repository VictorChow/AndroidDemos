package pers.victor.androiddemos.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log

/**
 * Created by Victor on 16/3/24.
 */
object ApkUtil {
    /** 安装apk **/
    fun installApk(context: Context, fileName: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(Uri.parse("file://" + fileName), "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    /** 卸载apk **/
    fun uninstallApk(context: Context, packageName: String) {
        val uri = Uri.parse("package:" + packageName)
        val intent = Intent(Intent.ACTION_DELETE, uri)
        context.startActivity(intent)
    }

    /**
     * 获取APK图标
     */
    fun getApkIcon(context: Context, apkPath: String): Drawable? {
        var pm = context.packageManager;
        var info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            var appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (e: OutOfMemoryError) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }
}