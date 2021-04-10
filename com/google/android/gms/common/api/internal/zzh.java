package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;
import com.google.android.gms.common.internal.*;

public final class zzh<O extends Api.ApiOptions>
{
    private final Api<O> mApi;
    private final O zzcl;
    private final boolean zzeb;
    private final int zzec;
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzh)) {
            return false;
        }
        final zzh zzh = (zzh)o;
        return !this.zzeb && !zzh.zzeb && Objects.equal(this.mApi, zzh.mApi) && Objects.equal(this.zzcl, zzh.zzcl);
    }
    
    @Override
    public final int hashCode() {
        return this.zzec;
    }
    
    public final String zzq() {
        return this.mApi.getName();
    }
}
