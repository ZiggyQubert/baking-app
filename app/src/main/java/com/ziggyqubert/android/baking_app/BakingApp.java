package com.ziggyqubert.android.baking_app;

import android.app.Application;

import com.ziggyqubert.android.baking_app.utilities.SSLCertificateChecking;

public class BakingApp extends Application {

    //set a tag to use for logging etc
    public static String APP_TAG = "baking-app";

    @Override
    public void onCreate() {
        //added to resolve a network issue with my local corporate development environment
        SSLCertificateChecking.disableSSLCertificateChecking();

        super.onCreate();
    }

}
