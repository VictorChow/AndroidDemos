package pers.victor.androiddemos.util;

/**
 * Created by Victor on 2017/3/27. (ง •̀_•́)ง
 */

public class HelloJni {
    /* 生成.so
     * 1. 新建一个native修饰的方法
     * 2. app gradle 添加
          ndk{
            moduleName 'xxx'
          }
     * 3. gradle.properties中设置android.useDeprecatedNdk=true
     * 4. javac文件
     * 5. 在java目录下javah -jni 包名+类名 生成.h
     * 6. 新建jni Folder 移入.h 新建.c 实现方法
     * 7. 添加Application.mk Android.mk
     * 8. 进入jni目录 ndk-build
     */

    static {
        System.loadLibrary("hello");
    }

    public native static String sayHello();
}
