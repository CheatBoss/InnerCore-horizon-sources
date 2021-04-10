package android.support.customtabs;

import android.net.*;
import java.util.*;
import android.os.*;

public interface ICustomTabsService extends IInterface
{
    Bundle extraCommand(final String p0, final Bundle p1) throws RemoteException;
    
    boolean mayLaunchUrl(final ICustomTabsCallback p0, final Uri p1, final Bundle p2, final List<Bundle> p3) throws RemoteException;
    
    boolean newSession(final ICustomTabsCallback p0) throws RemoteException;
    
    boolean updateVisuals(final ICustomTabsCallback p0, final Bundle p1) throws RemoteException;
    
    boolean warmup(final long p0) throws RemoteException;
    
    public abstract static class Stub extends Binder implements ICustomTabsService
    {
        private static final String DESCRIPTOR = "android.support.customtabs.ICustomTabsService";
        static final int TRANSACTION_extraCommand = 5;
        static final int TRANSACTION_mayLaunchUrl = 4;
        static final int TRANSACTION_newSession = 3;
        static final int TRANSACTION_updateVisuals = 6;
        static final int TRANSACTION_warmup = 2;
        
        public Stub() {
            this.attachInterface((IInterface)this, "android.support.customtabs.ICustomTabsService");
        }
        
        public static ICustomTabsService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("android.support.customtabs.ICustomTabsService");
            if (queryLocalInterface != null && queryLocalInterface instanceof ICustomTabsService) {
                return (ICustomTabsService)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
        }
        
        private static class Proxy implements ICustomTabsService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public Bundle extraCommand(final String s, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                    obtain.writeString(s);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle2;
                    if (obtain2.readInt() != 0) {
                        bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        bundle2 = null;
                    }
                    return bundle2;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public String getInterfaceDescriptor() {
                return "android.support.customtabs.ICustomTabsService";
            }
            
            @Override
            public boolean mayLaunchUrl(final ICustomTabsCallback customTabsCallback, final Uri uri, final Bundle bundle, final List<Bundle> list) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                            if (customTabsCallback != null) {
                                final IBinder binder = customTabsCallback.asBinder();
                                obtain.writeStrongBinder(binder);
                                boolean b = true;
                                if (uri != null) {
                                    obtain.writeInt(1);
                                    uri.writeToParcel(obtain, 0);
                                }
                                else {
                                    obtain.writeInt(0);
                                }
                                if (bundle != null) {
                                    obtain.writeInt(1);
                                    bundle.writeToParcel(obtain, 0);
                                }
                                else {
                                    obtain.writeInt(0);
                                }
                                obtain.writeTypedList((List)list);
                                this.mRemote.transact(4, obtain, obtain2, 0);
                                obtain2.readException();
                                if (obtain2.readInt() == 0) {
                                    b = false;
                                }
                                return b;
                            }
                        }
                        finally {
                            obtain2.recycle();
                            obtain.recycle();
                        }
                        final IBinder binder = null;
                        continue;
                    }
                }
            }
            
            @Override
            public boolean newSession(final ICustomTabsCallback customTabsCallback) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                            if (customTabsCallback != null) {
                                final IBinder binder = customTabsCallback.asBinder();
                                obtain.writeStrongBinder(binder);
                                final IBinder mRemote = this.mRemote;
                                boolean b = false;
                                mRemote.transact(3, obtain, obtain2, 0);
                                obtain2.readException();
                                if (obtain2.readInt() != 0) {
                                    b = true;
                                }
                                return b;
                            }
                        }
                        finally {
                            obtain2.recycle();
                            obtain.recycle();
                        }
                        final IBinder binder = null;
                        continue;
                    }
                }
            }
            
            @Override
            public boolean updateVisuals(final ICustomTabsCallback customTabsCallback, final Bundle bundle) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                            if (customTabsCallback != null) {
                                final IBinder binder = customTabsCallback.asBinder();
                                obtain.writeStrongBinder(binder);
                                boolean b = true;
                                if (bundle != null) {
                                    obtain.writeInt(1);
                                    bundle.writeToParcel(obtain, 0);
                                }
                                else {
                                    obtain.writeInt(0);
                                }
                                this.mRemote.transact(6, obtain, obtain2, 0);
                                obtain2.readException();
                                if (obtain2.readInt() == 0) {
                                    b = false;
                                }
                                return b;
                            }
                        }
                        finally {
                            obtain2.recycle();
                            obtain.recycle();
                        }
                        final IBinder binder = null;
                        continue;
                    }
                }
            }
            
            @Override
            public boolean warmup(final long n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.customtabs.ICustomTabsService");
                    obtain.writeLong(n);
                    final IBinder mRemote = this.mRemote;
                    boolean b = false;
                    mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
