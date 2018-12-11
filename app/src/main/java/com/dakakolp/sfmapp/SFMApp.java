package com.dakakolp.sfmapp;

import android.app.Application;

public class SFMApp extends Application {

    private static SFMApp sApp;


    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    public static SFMApp getApp() {
        return sApp;
    }

}
