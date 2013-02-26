LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libonyxfileutil
LOCAL_LDLIBS += -llog
LOCAL_SRC_FILES := com_onyx_android_jni_NativeFileUtil.cpp
include $(BUILD_SHARED_LIBRARY)
