package com.example.sbuddy.feedback;

import android.content.Context;

/**
 * Created by sbuddy on 10/8/2016.
 */
import android.app.Application;

public class checkingConnectivity extends Application {

    private static checkingConnectivity mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized checkingConnectivity getInstance() {

        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}