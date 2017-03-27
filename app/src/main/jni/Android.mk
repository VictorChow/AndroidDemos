LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE :=hello
LOCAL_SRC_FILES =: pers_victor_androiddemos_util_HelloJni.c
include $(BUILD_SHARED_LIBRARY)