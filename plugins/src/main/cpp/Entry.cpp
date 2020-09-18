//
// Created by Bugs Wan on 2020/9/18.
//

#include "Entry.h"
#include <jni.h>
#include <string>
#include "FFUBase.h"

extern "C" jstring
Java_com_yeestor_plugins_smartinfo_SmartInfoReader_stringFromJNI(JNIEnv *env, jobject thiz) {


    std::string hello = "Hello from C++";
    FFUBase* base = new FFUBase("/mnt/sdcard/");

    int result = -1 ;


    char buff[LEN_SMART_INFO] = {0};

    for (int i = 0xA9; i < 0xAC; ++i) {

        result = base->sendCmd(i,buff + (LEN_SMART_INFO_SEGMENT * (i - 0xA9)),LEN_SMART_INFO_SEGMENT);
        LOGI(" -=---  SEND Cmd 0x%02x return with result: 0x%02x",i,result);
        if(result != YS_SUCCESS){
            return env->NewStringUTF(hello.c_str());
        }

    }

}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_yeestor_plugins_smartinfo_SmartInfoReader_readSmartInfo(JNIEnv *env, jobject thiz, jstring filename) {
    int result = -1 ;

    const char *filepath = env->GetStringUTFChars(filename, NULL);

    FFUBase* base = new FFUBase(filepath);
    base->InitFile();
    char buff[LEN_SMART_INFO] = {0};

    for (int i = 0xA9; i < 0xAC; ++i) {

        result = base->sendCmd(i,buff + (LEN_SMART_INFO_SEGMENT * (i - 0xA9)),LEN_SMART_INFO_SEGMENT);
        LOGI(" -=---  SEND Cmd 0x%02x return with result: 0x%02x",i,result);
        if(result != YS_SUCCESS){
            return NULL;
        }
    }
    jbyteArray b = env->NewByteArray(LEN_SMART_INFO);
    env->SetByteArrayRegion( b, 0, LEN_SMART_INFO, (jbyte *)buff);
    return b ;
}