package android.support.v4.net;

import android.net.*;

class ConnectivityManagerCompatJellyBean
{
    public static boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
        return connectivityManager.isActiveNetworkMetered();
    }
}
