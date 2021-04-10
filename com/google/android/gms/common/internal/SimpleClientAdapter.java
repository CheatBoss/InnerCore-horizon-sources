package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.*;
import android.os.*;

public class SimpleClientAdapter<T extends IInterface> extends GmsClient<T>
{
    private final SimpleClient<T> zzva;
    
    @Override
    protected T createServiceInterface(final IBinder binder) {
        return this.zzva.createServiceInterface(binder);
    }
    
    public SimpleClient<T> getClient() {
        return this.zzva;
    }
    
    @Override
    public int getMinApkVersion() {
        return super.getMinApkVersion();
    }
    
    @Override
    protected String getServiceDescriptor() {
        return this.zzva.getServiceDescriptor();
    }
    
    @Override
    protected String getStartServiceAction() {
        return this.zzva.getStartServiceAction();
    }
    
    protected void onSetConnectState(final int n, final T t) {
        this.zzva.setState(n, t);
    }
}
