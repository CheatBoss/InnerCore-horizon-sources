package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;
import com.google.android.gms.common.*;
import com.google.android.gms.tasks.*;
import android.os.*;

public abstract class TaskApiCall<A extends Api.AnyClient, ResultT>
{
    private final Feature[] zzlz;
    private final boolean zzma;
    
    protected abstract void doExecute(final A p0, final TaskCompletionSource<ResultT> p1) throws RemoteException;
    
    public boolean shouldAutoResolveMissingFeatures() {
        return this.zzma;
    }
    
    public final Feature[] zzca() {
        return this.zzlz;
    }
}
