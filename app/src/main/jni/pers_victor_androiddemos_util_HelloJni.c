#include "pers_victor_androiddemos_util_HelloJni.h"

JNIEXPORT jstring JNICALL Java_pers_victor_androiddemos_util_HelloJni_sayHello(JNIEnv *env, jobject object) {
    return (*env)->NewStringUTF(env, "Hello Jni");
}