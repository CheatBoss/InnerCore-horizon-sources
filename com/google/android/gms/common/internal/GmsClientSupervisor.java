package com.google.android.gms.common.internal;

import android.content.*;

public abstract class GmsClientSupervisor
{
    private static final Object zztm;
    private static GmsClientSupervisor zztn;
    
    static {
        zztm = new Object();
    }
    
    public static GmsClientSupervisor getInstance(final Context context) {
        synchronized (GmsClientSupervisor.zztm) {
            if (GmsClientSupervisor.zztn == null) {
                GmsClientSupervisor.zztn = new zzh(context.getApplicationContext());
            }
            return GmsClientSupervisor.zztn;
        }
    }
    
    protected abstract boolean bindService(final ConnectionStatusConfig p0, final ServiceConnection p1, final String p2);
    
    public boolean bindService(final String s, final String s2, final int n, final ServiceConnection serviceConnection, final String s3) {
        return this.bindService(new ConnectionStatusConfig(s, s2, n), serviceConnection, s3);
    }
    
    protected abstract void unbindService(final ConnectionStatusConfig p0, final ServiceConnection p1, final String p2);
    
    public void unbindService(final String s, final String s2, final int n, final ServiceConnection serviceConnection, final String s3) {
        this.unbindService(new ConnectionStatusConfig(s, s2, n), serviceConnection, s3);
    }
    
    protected static final class ConnectionStatusConfig
    {
        private final ComponentName mComponentName;
        private final String zzto;
        private final String zztp;
        private final int zztq;
        
        public ConnectionStatusConfig(final String s, final String s2, final int zztq) {
            this.zzto = Preconditions.checkNotEmpty(s);
            this.zztp = Preconditions.checkNotEmpty(s2);
            this.mComponentName = null;
            this.zztq = zztq;
        }
        
        @Override
        public final boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ConnectionStatusConfig)) {
                return false;
            }
            final ConnectionStatusConfig connectionStatusConfig = (ConnectionStatusConfig)o;
            return Objects.equal(this.zzto, connectionStatusConfig.zzto) && Objects.equal(this.zztp, connectionStatusConfig.zztp) && Objects.equal(this.mComponentName, connectionStatusConfig.mComponentName) && this.zztq == connectionStatusConfig.zztq;
        }
        
        public final int getBindFlags() {
            return this.zztq;
        }
        
        public final ComponentName getComponentName() {
            return this.mComponentName;
        }
        
        public final String getPackage() {
            return this.zztp;
        }
        
        public final Intent getStartServiceIntent(final Context context) {
            if (this.zzto != null) {
                return new Intent(this.zzto).setPackage(this.zztp);
            }
            return new Intent().setComponent(this.mComponentName);
        }
        
        @Override
        public final int hashCode() {
            return Objects.hashCode(this.zzto, this.zztp, this.mComponentName, this.zztq);
        }
        
        @Override
        public final String toString() {
            String s;
            if ((s = this.zzto) == null) {
                s = this.mComponentName.flattenToString();
            }
            return s;
        }
    }
}
