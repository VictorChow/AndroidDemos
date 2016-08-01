# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Victor/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}



#银联支付混淆
-keep class org.simalliance.openmobileapi.** {*;}
-keep class org.simalliance.openmobileapi.service.** {*;}
-keep class com.unionpay.** {*;}

################### region for leakcanary
-dontwarn org.eclipse.mat.**
-dontwarn com.squareup.leakcanary.**
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
#################### end region

################### region for kotlin
-dontwarn kotlin.**
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
#################### end region

################### region for okhttp
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-keep class sun.misc.Unsafe { *; }
#################### end region

################### region for gson
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
#################### end region

################### region for bean,R
-keep class com.centling.cef.bean.** { *; }
-keep public class com.centling.cef.R$*{
    public static final int *;
}
#################### end region

################### region for getui
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
#################### end region

################### region for alipay
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
#################### end region

#################### region for umeng
-dontwarn android.webkit.WebView
-dontwarn com.google.android.maps.**
-dontwarn com.umeng.**
-dontwarn com.tencent.**
-dontwarn com.sina.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep public class com.tencent.** {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep class com.umeng.scrshot.**
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class com.tencent.** {*;}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.sina.** {*;}

##################### region for amap
-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
-keep class com.amap.api.services.**{*;}
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
#################### end region

################### region for eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#################### end region

################### region for glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#################### end region

################### region for picasso
-dontwarn com.squareup.okhttp.**
#################### end region


################### region for xunfei
-keep class com.iflytek.**{*;}
#################### end region


