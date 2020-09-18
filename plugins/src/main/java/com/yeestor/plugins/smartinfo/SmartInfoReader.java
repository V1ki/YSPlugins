package com.yeestor.plugins.smartinfo;

public class SmartInfoReader {


    static {

        System.loadLibrary("plugin-native");
    }

    public native String stringFromJNI();

    public native byte[] readSmartInfo(String filename) ;


}
