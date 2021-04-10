package com.google.android.gms.common.stats;

import java.util.*;
import com.google.android.gms.common.util.*;
import android.util.*;
import android.content.*;

public class ConnectionTracker
{
    private static final Object zztm;
    private static volatile ConnectionTracker zzyg;
    private static boolean zzyh;
    private final List<String> zzyi;
    private final List<String> zzyj;
    private final List<String> zzyk;
    private final List<String> zzyl;
    
    static {
        zztm = new Object();
        ConnectionTracker.zzyh = false;
    }
    
    private ConnectionTracker() {
        this.zzyi = (List<String>)Collections.EMPTY_LIST;
        this.zzyj = (List<String>)Collections.EMPTY_LIST;
        this.zzyk = (List<String>)Collections.EMPTY_LIST;
        this.zzyl = (List<String>)Collections.EMPTY_LIST;
    }
    
    public static ConnectionTracker getInstance() {
        if (ConnectionTracker.zzyg == null) {
            synchronized (ConnectionTracker.zztm) {
                if (ConnectionTracker.zzyg == null) {
                    ConnectionTracker.zzyg = new ConnectionTracker();
                }
            }
        }
        return ConnectionTracker.zzyg;
    }
    
    private static boolean zza(final Context context, final String s, final Intent intent, final ServiceConnection serviceConnection, final int n, final boolean b) {
        if (b) {
            final ComponentName component = intent.getComponent();
            if (component != null && ClientLibraryUtils.isPackageStopped(context, component.getPackageName())) {
                Log.w("ConnectionTracker", "Attempted to bind to a service in a STOPPED package.");
                return false;
            }
        }
        return context.bindService(intent, serviceConnection, n);
    }
    
    public boolean bindService(final Context context, final Intent intent, final ServiceConnection serviceConnection, final int n) {
        return this.bindService(context, context.getClass().getName(), intent, serviceConnection, n);
    }
    
    public boolean bindService(final Context context, final String s, final Intent intent, final ServiceConnection serviceConnection, final int n) {
        return zza(context, s, intent, serviceConnection, n, true);
    }
    
    public void logConnectService(final Context context, final ServiceConnection serviceConnection, final String s, final Intent intent) {
    }
    
    public void logDisconnectService(final Context context, final ServiceConnection serviceConnection) {
    }
    
    public void unbindService(final Context context, final ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }
}
