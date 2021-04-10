package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;
import com.google.android.gms.tasks.*;
import android.os.*;

public abstract class RegisterListenerMethod<A extends Api.AnyClient, L>
{
    private final ListenerHolder<L> zzls;
    
    public void clearListener() {
        this.zzls.clear();
    }
    
    protected abstract void registerListener(final A p0, final TaskCompletionSource<Void> p1) throws RemoteException;
}
