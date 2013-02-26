#include "com_onyx_android_jni_NativeFileUtil.h"

#include <jni.h>
#include <sys/stat.h>

#include <android/log.h>

JNIEXPORT jlong JNICALL Java_com_onyx_android_jni_NativeFileUtil_getChangeTimestamp
  (JNIEnv *env, jclass thiz, jstring path)
{
    const char *jstr = env->GetStringUTFChars(path, 0);
    if (jstr == 0) {
        //LOGE("GetStringChars failed");
        return -1;
    }

    struct stat st;
    if (stat(jstr, &st) == -1) {
        //LOGE("stat failed");
        env->ReleaseStringUTFChars(path, jstr);
        return -1;
    }

    env->ReleaseStringUTFChars(path, jstr);
    return st.st_ctime;
}

