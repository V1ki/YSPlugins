

//
// Created by Bugs Wan on 2020/9/18.
//

#ifndef YSPLUGINS_ENTRY_H
#define YSPLUGINS_ENTRY_H

#include <jni.h>
#include <android/log.h>

/** Defines tag used for Android logging. */
#define LIB_NAME "Plugin-Native"

/** Verbose Android logging macro. */
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LIB_NAME, __VA_ARGS__)

/** Debug Android logging macro. */
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LIB_NAME, __VA_ARGS__)

/** Info Android logging macro. */
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LIB_NAME, __VA_ARGS__)

/** Warn Android logging macro. */
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LIB_NAME, __VA_ARGS__)


const static int LEN_SMART_INFO_SEGMENT = 1024 - 32 ;
const static int LEN_SMART_INFO = 3 * LEN_SMART_INFO_SEGMENT;


#endif //YSPLUGINS_ENTRY_H
