package demos.activity;

import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.victor.androiddemos.R;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import demos.module.AppModule;

public class ClearCacheActivity extends BaseActivity {
    /**
     * /data/data/ package_name /cache（应用缓存）
     * /mnt/sdcard/Android/ package_name /cache（外部应用缓存，FROYO以后支持）
     * /data/data/package_name/database/webview.db*（WebView缓存）
     * /data/data/package_name/database/webviewCache.db*（WebView缓存）
     * 其他一些/data/data/ package_name /*cache目录（应用缓存）
     * 其他一些/data/data/ package_name /app_webview（webView缓存）
     * /data/data/package_name/files（比较严格的清理策略时也可以选择清理）
     */

    @Bind(R.id.tv_cache_app)
    TextView tvCacheApp;
    @Bind(R.id.tv_cache_package)
    TextView tvCachePackage;
    @Bind(R.id.btn_cache_scan)
    Button btnCacheScan;
    @Bind(R.id.tv_cache_size)
    TextView tvCacheSize;
    @Bind(R.id.tv_cache_detail)
    TextView tvCacheDetail;

    private List<AppModule> appList;
    private ClearCacheTask clearCacheTask;
    private List<String> dirList;
    private long cacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);
        ButterKnife.bind(this);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("扫描缓存");

        dirList = new ArrayList<>();
        dirList.add("/data/data/xxx/cache");
        dirList.add("/data/data/xxx/app_webview");
        dirList.add("/mnt/sdcard/Android/data/xxx/cache");

        clearCacheTask = new ClearCacheTask();
        btnCacheScan.setOnClickListener(v -> {
            clearCacheTask.execute(Boolean.TRUE);
            btnCacheScan.setEnabled(false);
        });

//        /data/data/com.victor.androiddemos/files
//        PrintLog.d("位置是  " + mContext.getFilesDir());
//        PrintLog.d("位置是  " + mContext.getCacheDir());
//
//        File file = new File("/data/data/com.victor.androiddemos/cache");
//        PrintLog.d("大小是  " + formatCacheSize(getFolderSize(file)));
//        deleteFolderFile("/data/data/com.victor.androiddemos/shared_prefs", true);
    }

    /**
     * 获取文件夹大小
     */
    public static long getFolderSize(File file) {
        if (!file.exists()) {
            return 0;
        }
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList == null) {
                return 0;
            }
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     */
    public void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    return;
                }
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     */
    public static String formatCacheSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    private void getPackages() {
        appList = new ArrayList<>(); //用来存储获取的应用信息数据
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppModule tmpInfo = new AppModule();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
            tmpInfo.setPackageName(packageInfo.packageName);
            appList.add(tmpInfo);
        }
    }

    private class ClearCacheTask extends AsyncTask<Boolean, String, String> {

        @Override
        protected String doInBackground(Boolean... isScan) {
            StringBuilder sb = new StringBuilder();
            if (appList == null) {
                publishProgress("正在初始化", "0KB");
                getPackages();
            }
            if (isScan[0]) {
                //扫描
                for (AppModule appModule : appList) {
                    long preSize = cacheSize;
                    for (String dir : dirList) {
                        String path = dir.replace("xxx", appModule.getPackageName());
                        cacheSize += getFolderSize(new File(path));
                        publishProgress("正在扫描: " + appModule.getAppName(), dir);
                    }
                    if (cacheSize - preSize > 0) {
                        sb.append(appModule.getAppName())
                                .append(": ")
                                .append(formatCacheSize(cacheSize - preSize))
                                .append("\n");
                        publishProgress(sb.toString());
                    }
                }
            } else {
                //清理

            }
            return "扫描完成";
        }

        @Override
        protected void onPostExecute(String s) {
            tvCacheApp.setText(s);
            tvCachePackage.setText("");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values.length > 1) {
                //显示实时
                tvCacheApp.setText(values[0]);
                tvCachePackage.setText(values[1]);
                tvCacheSize.setText(formatCacheSize(cacheSize));
            } else {
                //显示详情列表
                tvCacheDetail.setText(values[0]);
            }
        }
    }
}
