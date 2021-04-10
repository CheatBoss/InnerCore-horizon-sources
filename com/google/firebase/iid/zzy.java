package com.google.firebase.iid;

import java.security.*;
import android.util.*;
import com.google.android.gms.common.internal.*;

final class zzy
{
    private final KeyPair zzbo;
    private final long zzbp;
    
    zzy(final KeyPair zzbo, final long zzbp) {
        this.zzbo = zzbo;
        this.zzbp = zzbp;
    }
    
    private final String zzu() {
        return Base64.encodeToString(this.zzbo.getPublic().getEncoded(), 11);
    }
    
    private final String zzv() {
        return Base64.encodeToString(this.zzbo.getPrivate().getEncoded(), 11);
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof zzy)) {
            return false;
        }
        final zzy zzy = (zzy)o;
        return this.zzbp == zzy.zzbp && this.zzbo.getPublic().equals(zzy.zzbo.getPublic()) && this.zzbo.getPrivate().equals(zzy.zzbo.getPrivate());
    }
    
    final KeyPair getKeyPair() {
        return this.zzbo;
    }
    
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.zzbo.getPublic(), this.zzbo.getPrivate(), this.zzbp);
    }
}
