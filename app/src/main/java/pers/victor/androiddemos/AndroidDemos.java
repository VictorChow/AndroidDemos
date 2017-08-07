package pers.victor.androiddemos;

import android.app.Application;
import android.util.DisplayMetrics;

import com.orhanobut.logger.Logger;

/**
 * Created by Victor on 16/2/9.
 */
public class AndroidDemos extends Application {

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
        application = this;
        getDisplayMetrics();
        Logger.init("demos").hideThreadInfo().methodCount(0);
    }

    private void getDisplayMetrics() {
        DisplayMetrics metric = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        screenDensity = metric.density;
        scaledDensity = metric.scaledDensity;
    }
}
