package com.google.android.gms.common.internal;

import android.content.*;
import android.os.*;
import java.util.*;

final class zzi implements ServiceConnection
{
    private ComponentName mComponentName;
    private int mState;
    private IBinder zzry;
    private final Set<ServiceConnection> zztv;
    private boolean zztw;
    private final GmsClientSupervisor.ConnectionStatusConfig zztx;
    private final /* synthetic */ zzh zzty;
    
    public zzi(final zzh zzty, final GmsClientSupervisor.ConnectionStatusConfig zztx) {
        this.zzty = zzty;
        this.zztx = zztx;
        this.zztv = new HashSet<ServiceConnection>();
        this.mState = 2;
    }
    
    public final IBinder getBinder() {
        return this.zzry;
    }
    
    public final ComponentName getComponentName() {
        return this.mComponentName;
    }
    
    public final int getState() {
        return this.mState;
    }
    
    public final boolean isBound() {
        return this.zztw;
    }
    
    public final void onServiceConnected(final ComponentName mComponentName, final IBinder zzry) {
        synchronized (this.zzty.zztr) {
            this.zzty.mHandler.removeMessages(1, (Object)this.zztx);
            this.zzry = zzry;
            this.mComponentName = mComponentName;
            final Iterator<ServiceConnection> iterator = this.zztv.iterator();
            while (iterator.hasNext()) {
                iterator.next().onServiceConnected(mComponentName, zzry);
            }
            this.mState = 1;
        }
    }
    
    public final void onServiceDisconnected(final ComponentName mComponentName) {
        synchronized (this.zzty.zztr) {
            this.zzty.mHandler.removeMessages(1, (Object)this.zztx);
            this.zzry = null;
            this.mComponentName = mComponentName;
            final Iterator<ServiceConnection> iterator = this.zztv.iterator();
            while (iterator.hasNext()) {
                iterator.next().onServiceDisconnected(mComponentName);
            }
            this.mState = 2;
        }
    }
    
    public final void zza(final ServiceConnection serviceConnection, final String s) {
        this.zzty.zzts.logConnectService(this.zzty.zzau, serviceConnection, s, this.zztx.getStartServiceIntent(this.zzty.zzau));
        this.zztv.add(serviceConnection);
    }
    
    public final boolean zza(final ServiceConnection serviceConnection) {
        return this.zztv.contains(serviceConnection);
    }
    
    public final void zzb(final ServiceConnection serviceConnection, final String s) {
        this.zzty.zzts.logDisconnectService(this.zzty.zzau, serviceConnection);
        this.zztv.remove(serviceConnection);
    }
    
    public final boolean zzcv() {
        return this.zztv.isEmpty();
    }
    
    public final void zzj(final String s) {
        this.mState = 3;
        final boolean bindService = this.zzty.zzts.bindService(this.zzty.zzau, s, this.zztx.getStartServiceIntent(this.zzty.zzau), (ServiceConnection)this, this.zztx.getBindFlags());
        this.zztw = bindService;
        if (bindService) {
            this.zzty.mHandler.sendMessageDelayed(this.zzty.mHandler.obtainMessage(1, (Object)this.zztx), this.zzty.zztu);
            return;
        }
        this.mState = 2;
        try {
            this.zzty.zzts.unbindService(this.zzty.zzau, (ServiceConnection)this);
        }
        catch (IllegalArgumentException ex) {}
    }
    
    public final void zzk(final String s) {
        this.zzty.mHandler.removeMessages(1, (Object)this.zztx);
        this.zzty.zzts.unbindService(this.zzty.zzau, (ServiceConnection)this);
        this.zztw = false;
        this.mState = 2;
    }
}
