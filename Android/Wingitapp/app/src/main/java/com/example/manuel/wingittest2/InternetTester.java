package com.example.manuel.wingittest2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Manuel on 5/11/2016.
 * Inspired by Androidhive tutorial on checking internet connectivity
 * adapted to remove deprecated methods
 * http://www.androidhive.info/2012/07/android-detect-internet-connection-status/
 */
public class InternetTester {
    private Context _context;

    public InternetTester (Context context){
        this._context = context;
    }

    /**
     * Check if device is connected or connecting
     * **/

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnectedOrConnecting()){
            return true;
        }
        else
            return false;
    }
}
