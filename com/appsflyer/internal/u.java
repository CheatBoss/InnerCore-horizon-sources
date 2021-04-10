package com.appsflyer.internal;

import java.io.*;
import java.util.concurrent.*;
import android.content.*;
import android.os.*;

final class u
{
    static b \u03b9(final Context context) throws Exception {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            try {
                context.getPackageManager().getPackageInfo("com.android.vending", 0);
                final d d = new d((byte)0);
                final Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                intent.setPackage("com.google.android.gms");
                try {
                    try {
                        if (!context.bindService(intent, (ServiceConnection)d, 1)) {
                            if (context != null) {
                                context.unbindService((ServiceConnection)d);
                            }
                            throw new IOException("Google Play connection failed");
                        }
                        if (!d.\u0131) {
                            d.\u0131 = true;
                            final e e = new e(d.\u0399.take());
                            final b b = new b(e.\u01c3(), e.\u0131());
                            if (context != null) {
                                context.unbindService((ServiceConnection)d);
                            }
                            return b;
                        }
                        throw new IllegalStateException();
                    }
                    finally {
                        if (context != null) {
                            context.unbindService((ServiceConnection)d);
                        }
                    }
                }
                catch (Exception ex2) {}
            }
            catch (Exception ex) {
                throw ex;
            }
        }
        throw new IllegalStateException("Cannot be called from the main thread");
    }
    
    static final class b
    {
        private final boolean \u01c3;
        final String \u0269;
        
        b(final String \u0269, final boolean \u01c3) {
            this.\u0269 = \u0269;
            this.\u01c3 = \u01c3;
        }
        
        final boolean \u0269() {
            return this.\u01c3;
        }
    }
    
    static final class d implements ServiceConnection
    {
        boolean \u0131;
        final LinkedBlockingQueue<IBinder> \u0399;
        
        private d() {
            this.\u0399 = new LinkedBlockingQueue<IBinder>(1);
            this.\u0131 = false;
        }
        
        public final void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            try {
                this.\u0399.put(binder);
            }
            catch (InterruptedException ex) {}
        }
        
        public final void onServiceDisconnected(final ComponentName componentName) {
        }
    }
    
    static final class e implements IInterface
    {
        private IBinder \u01c3;
        
        e(final IBinder \u01c3) {
            this.\u01c3 = \u01c3;
        }
        
        public final IBinder asBinder() {
            return this.\u01c3;
        }
        
        final boolean \u0131() throws RemoteException {
            final Parcel obtain = Parcel.obtain();
            final Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                boolean b = true;
                obtain.writeInt(1);
                this.\u01c3.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() == 0) {
                    b = false;
                }
                return b;
            }
            finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
        
        public final String \u01c3() throws RemoteException {
            final Parcel obtain = Parcel.obtain();
            final Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.\u01c3.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                return obtain2.readString();
            }
            finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
    }
}
