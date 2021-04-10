package com.google.firebase.iid;

final class zzw implements InstanceIdResult
{
    private final String zzbm;
    private final String zzbn;
    
    zzw(final String zzbm, final String zzbn) {
        this.zzbm = zzbm;
        this.zzbn = zzbn;
    }
    
    @Override
    public final String getToken() {
        return this.zzbn;
    }
}
