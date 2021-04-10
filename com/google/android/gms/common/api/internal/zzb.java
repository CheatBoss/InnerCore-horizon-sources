package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;
import com.google.android.gms.common.util.*;
import android.os.*;

public abstract class zzb
{
    private final int type;
    
    public zzb(final int type) {
        this.type = type;
    }
    
    private static Status zza(final RemoteException ex) {
        final StringBuilder sb = new StringBuilder();
        if (PlatformVersion.isAtLeastIceCreamSandwichMR1() && ex instanceof TransactionTooLargeException) {
            sb.append("TransactionTooLargeException: ");
        }
        sb.append(ex.getLocalizedMessage());
        return new Status(8, sb.toString());
    }
    
    public abstract void zza(final Status p0);
    
    public abstract void zza(final GoogleApiManager.zza<?> p0) throws DeadObjectException;
    
    public abstract void zza(final zzaa p0, final boolean p1);
    
    public abstract void zza(final RuntimeException p0);
}
