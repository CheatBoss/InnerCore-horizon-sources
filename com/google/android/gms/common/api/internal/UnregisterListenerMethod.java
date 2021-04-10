package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;
import com.google.android.gms.tasks.*;
import android.os.*;

public abstract class UnregisterListenerMethod<A extends Api.AnyClient, L>
{
    protected abstract void unregisterListener(final A p0, final TaskCompletionSource<Boolean> p1) throws RemoteException;
}
