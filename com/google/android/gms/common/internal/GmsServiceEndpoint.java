package com.google.android.gms.common.internal;

public class GmsServiceEndpoint
{
    private final String mPackageName;
    private final int zztq;
    private final String zzue;
    private final boolean zzuf;
    
    public GmsServiceEndpoint(final String mPackageName, final String zzue, final boolean zzuf, final int zztq) {
        this.mPackageName = mPackageName;
        this.zzue = zzue;
        this.zzuf = zzuf;
        this.zztq = zztq;
    }
    
    final int getBindFlags() {
        return this.zztq;
    }
    
    final String getPackageName() {
        return this.mPackageName;
    }
    
    final String zzcw() {
        return this.zzue;
    }
}
