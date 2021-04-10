package com.google.android.gms.dynamic;

import android.os.*;
import com.google.android.gms.internal.stable.*;

public interface IObjectWrapper extends IInterface
{
    public static class Stub extends zzb implements IObjectWrapper
    {
        public Stub() {
            super("com.google.android.gms.dynamic.IObjectWrapper");
        }
        
        public static IObjectWrapper asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.dynamic.IObjectWrapper");
            if (queryLocalInterface instanceof IObjectWrapper) {
                return (IObjectWrapper)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public static class Proxy extends zza implements IObjectWrapper
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.gms.dynamic.IObjectWrapper");
            }
        }
    }
}
