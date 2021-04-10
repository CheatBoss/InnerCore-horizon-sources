package com.google.android.gms.common.internal;

import java.util.*;
import com.google.android.gms.common.stats.*;
import android.os.*;
import android.util.*;
import android.content.*;

final class zzh extends GmsClientSupervisor implements Handler$Callback
{
    private final Handler mHandler;
    private final Context zzau;
    private final HashMap<ConnectionStatusConfig, zzi> zztr;
    private final ConnectionTracker zzts;
    private final long zztt;
    private final long zztu;
    
    zzh(final Context context) {
        this.zztr = new HashMap<ConnectionStatusConfig, zzi>();
        this.zzau = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), (Handler$Callback)this);
        this.zzts = ConnectionTracker.getInstance();
        this.zztt = 5000L;
        this.zztu = 300000L;
    }
    
    @Override
    protected final boolean bindService(final ConnectionStatusConfig connectionStatusConfig, final ServiceConnection serviceConnection, final String s) {
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zztr) {
            final zzi zzi = this.zztr.get(connectionStatusConfig);
            zzi zzi3;
            if (zzi == null) {
                final zzi zzi2 = new zzi(this, connectionStatusConfig);
                zzi2.zza(serviceConnection, s);
                zzi2.zzj(s);
                this.zztr.put(connectionStatusConfig, zzi2);
                zzi3 = zzi2;
            }
            else {
                this.mHandler.removeMessages(0, (Object)connectionStatusConfig);
                if (zzi.zza(serviceConnection)) {
                    final String value = String.valueOf(connectionStatusConfig);
                    final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 81);
                    sb.append("Trying to bind a GmsServiceConnection that was already connected before.  config=");
                    sb.append(value);
                    throw new IllegalStateException(sb.toString());
                }
                zzi.zza(serviceConnection, s);
                final int state = zzi.getState();
                if (state != 1) {
                    if (state != 2) {
                        zzi3 = zzi;
                    }
                    else {
                        zzi.zzj(s);
                        zzi3 = zzi;
                    }
                }
                else {
                    serviceConnection.onServiceConnected(zzi.getComponentName(), zzi.getBinder());
                    zzi3 = zzi;
                }
            }
            return zzi3.isBound();
        }
    }
    
    public final boolean handleMessage(final Message message) {
        final int what = message.what;
        if (what != 0) {
            if (what != 1) {
                return false;
            }
            synchronized (this.zztr) {
                final ConnectionStatusConfig connectionStatusConfig = (ConnectionStatusConfig)message.obj;
                final zzi zzi = this.zztr.get(connectionStatusConfig);
                if (zzi != null && zzi.getState() == 3) {
                    final String value = String.valueOf(connectionStatusConfig);
                    final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 47);
                    sb.append("Timeout waiting for ServiceConnection callback ");
                    sb.append(value);
                    Log.wtf("GmsClientSupervisor", sb.toString(), (Throwable)new Exception());
                    ComponentName componentName;
                    if ((componentName = zzi.getComponentName()) == null) {
                        componentName = connectionStatusConfig.getComponentName();
                    }
                    ComponentName componentName2;
                    if ((componentName2 = componentName) == null) {
                        componentName2 = new ComponentName(connectionStatusConfig.getPackage(), "unknown");
                    }
                    zzi.onServiceDisconnected(componentName2);
                }
                return true;
            }
        }
        synchronized (this.zztr) {
            final ConnectionStatusConfig connectionStatusConfig2 = (ConnectionStatusConfig)message.obj;
            final zzi zzi2 = this.zztr.get(connectionStatusConfig2);
            if (zzi2 != null && zzi2.zzcv()) {
                if (zzi2.isBound()) {
                    zzi2.zzk("GmsClientSupervisor");
                }
                this.zztr.remove(connectionStatusConfig2);
            }
            return true;
        }
    }
    
    @Override
    protected final void unbindService(final ConnectionStatusConfig connectionStatusConfig, final ServiceConnection serviceConnection, final String s) {
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zztr) {
            final zzi zzi = this.zztr.get(connectionStatusConfig);
            if (zzi == null) {
                final String value = String.valueOf(connectionStatusConfig);
                final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 50);
                sb.append("Nonexistent connection status for service config: ");
                sb.append(value);
                throw new IllegalStateException(sb.toString());
            }
            if (zzi.zza(serviceConnection)) {
                zzi.zzb(serviceConnection, s);
                if (zzi.zzcv()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, (Object)connectionStatusConfig), this.zztt);
                }
                return;
            }
            final String value2 = String.valueOf(connectionStatusConfig);
            final StringBuilder sb2 = new StringBuilder(String.valueOf(value2).length() + 76);
            sb2.append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=");
            sb2.append(value2);
            throw new IllegalStateException(sb2.toString());
        }
    }
}
