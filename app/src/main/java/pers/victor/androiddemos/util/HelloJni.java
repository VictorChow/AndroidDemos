package pers.victor.androiddemos.util;

/**
 * Created by Victor on 2017/3/27. (ง •̀_•́)ง
 */

public class HelloJni {
    static {
        System.loadLibrary("hello");
    }

    public native static String sayHello();
}
