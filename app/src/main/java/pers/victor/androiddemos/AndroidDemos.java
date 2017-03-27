package pers.victor.androiddemos;

import android.app.Application;
import android.os.StrictMode;
import android.support.v4.BuildConfig;
import android.util.DisplayMetrics;

import com.orhanobut.logger.Logger;

/**
 * Created by Victor on 16/2/9.
 */
public class AndroidDemos extends Application {

    /**
     * 屏幕宽度，屏幕高度，屏幕密度，字体缩放比
     */
    public static DisplayMetrics metric;
    public static int screenWidth;
    public static int screenHeight;
    public static float screenDensity;
    public static float scaledDensity;
    private static AndroidDemos application;

    public static AndroidDemos getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeathOnNetwork()
                    .build());
        }

        application = this;
        getDisplayMetrics();
        Logger.init("demos").hideThreadInfo().methodCount(0);
    }

    /**
     * 获取高度、宽度、密度、缩放比例
     */
    private void getDisplayMetrics() {
        metric = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        screenDensity = metric.density;
        scaledDensity = metric.scaledDensity;
    }
}