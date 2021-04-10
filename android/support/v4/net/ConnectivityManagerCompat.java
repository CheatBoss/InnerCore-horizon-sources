package android.support.v4.net;

import android.os.*;
import android.content.*;
import android.net.*;

public final class ConnectivityManagerCompat
{
    private static final ConnectivityManagerCompatImpl IMPL;
    
    static {
        ConnectivityManagerCompatImpl impl;
        if (Build$VERSION.SDK_INT >= 16) {
            impl = new JellyBeanConnectivityManagerCompatImpl();
        }
        else if (Build$VERSION.SDK_INT >= 13) {
            impl = new HoneycombMR2ConnectivityManagerCompatImpl();
        }
        else if (Build$VERSION.SDK_INT >= 8) {
            impl = new GingerbreadConnectivityManagerCompatImpl();
        }
        else {
            impl = new BaseConnectivityManagerCompatImpl();
        }
        IMPL = impl;
    }
    
    private ConnectivityManagerCompat() {
    }
    
    public static NetworkInfo getNetworkInfoFromBroadcast(final ConnectivityManager connectivityManager, final Intent intent) {
        final NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
        if (networkInfo != null) {
            return connectivityManager.getNetworkInfo(networkInfo.getType());
        }
        return null;
    }
    
    public static boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
        return ConnectivityManagerCompat.IMPL.isActiveNetworkMetered(connectivityManager);
    }
    
    static class BaseConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl
    {
        @Override
        public boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
            final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return true;
            }
            switch (activeNetworkInfo.getType()) {
                default: {
                    return true;
                }
                case 1: {
                    return false;
                }
                case 0: {
                    return true;
                }
            }
        }
    }
    
    interface ConnectivityManagerCompatImpl
    {
        boolean isActiveNetworkMetered(final ConnectivityManager p0);
    }
    
    static class GingerbreadConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl
    {
        @Override
        public boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
            return ConnectivityManagerCompatGingerbread.isActiveNetworkMetered(connectivityManager);
        }
    }
    
    static class HoneycombMR2ConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl
    {
        @Override
        public boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
            return ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(connectivityManager);
        }
    }
    
    static class JellyBeanConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl
    {
        @Override
        public boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
            return ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(connectivityManager);
        }
    }
}
