package com.li.xroads.util;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by nagnath_p on 3/4/2016.
 */
public class NetworkConnectionUtility {

    public static boolean isInternetConnected(Activity context) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // Check for network connections
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
