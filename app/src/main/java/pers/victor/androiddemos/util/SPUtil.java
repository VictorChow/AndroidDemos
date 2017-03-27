package pers.victor.androiddemos.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Victor on 2015/8/29.
 */
public class SPUtil {
    private static SharedPreferences mSharedPreferences;

    private static void init(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
        }
    }

    /**
     * 添加Int类型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setInt(Context context, String key, int value) {
        if (mSharedPreferences == null) {
            init(context);
        }
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * 获取Int类型
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return getInt(context, key, -1);
    }

    /**
     * 获取Int类型
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String key,
                             int defaultValue) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 添加Long类型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setLong(Context context, String key, long value) {
        if (mSharedPreferences == null) {
            init(context);
        }
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    /**
     * 获取Long类型
     *
     * @param context
     * @param key
     * @return
     */
    public static long getLong(Context context, String key) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return getLong(context, key, -1L);
    }

    /**
     * 获取Long类型
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(Context context, String key,
                               long defaultValue) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return mSharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 添加Float类型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setFloat(Context context, String key,
                                float value) {
        if (mSharedPreferences == null) {
            init(context);
        }
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    /**
     * 获取Float类型
     *
     * @param context
     * @param key
     * @return
     */
    public static Float getFloat(Context context, String key) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return getFloat(context, key, -1f);
    }

    /**
     * 获取Float类型
     *
     * @param context
     * @param key
     * @return
     */
    public static Float getFloat(Context context, String key,
                                 float defaultValue) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * 添加Boolean类型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setBoolean(Context context, String key,
                                  boolean value) {
        if (mSharedPreferences == null) {
            init(context);
        }
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * 获取Boolean类型
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return getBoolean(context, key, false);
    }

    /**
     * 获取Boolean类型
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key,
                                     boolean defaultValue) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 添加String类型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context, String key,
                                 String value) {
        if (mSharedPreferences == null) {
            init(context);
        }
        mSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * 获取String类型
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return getString(context, key, "");
    }

    /**
     * 获取String类型
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Context context, String key,
                                   String defaultValue) {
        if (mSharedPreferences == null) {
            init(context);
        }
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * 删除某个键�?内容
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        if (mSharedPreferences == null) {
            init(context);
        }
        mSharedPreferences.edit().remove(key).apply();
    }

    /**
     * 清空�?��
     *
     * @param context
     */
    public static void clearAll(Context context) {
        if (mSharedPreferences == null) {
            init(context);
        }
        mSharedPreferences.edit().clear().apply();
    }

}
