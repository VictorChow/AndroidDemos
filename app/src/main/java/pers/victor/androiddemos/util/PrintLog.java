package pers.victor.androiddemos.util;

import android.text.TextUtils;
import android.util.Log;

public class PrintLog {

    public static String customTagPrefix = "h_log";
    public static boolean isDebug = true;

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void d(Object content) {
        if (!isDebug) return;
        String tag = generateTag();
        Log.d(tag, content.toString());
    }

    public static void d(Object content, Throwable tr) {
        if (!isDebug) return;
        String tag = generateTag();
        Log.d(tag, content.toString(), tr);
    }

    public static void e(Object content) {
        if (!isDebug) return;
        String tag = generateTag();
        Log.e(tag, content.toString());
    }

    public static void e(Object content, Throwable tr) {
        if (!isDebug) return;
        String tag = generateTag();
        Log.e(tag, content.toString(), tr);
    }

}
