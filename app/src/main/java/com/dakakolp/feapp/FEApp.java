package com.dakakolp.feapp;

import android.app.Application;
import android.content.Context;

public class FEApp extends Application{
   
	 private static volatile Context applicationContext;
	 private static FEApp sApp;
	
	
	@Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    public static FEApp getApp() {
        return sApp;
    }

}
