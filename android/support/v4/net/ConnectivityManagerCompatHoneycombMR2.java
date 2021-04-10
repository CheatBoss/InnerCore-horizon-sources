package android.support.v4.net;

import android.net.*;

class ConnectivityManagerCompatHoneycombMR2
{
    public static boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return true;
        }
        switch (activeNetworkInfo.getType()) {
            default: {
                return true;
            }
            case 1:
            case 7:
            case 9: {
                return false;
            }
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                return true;
            }
        }
    }
}
