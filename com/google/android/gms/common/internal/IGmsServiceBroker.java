package com.google.android.gms.common.internal;

import android.os.*;

public interface IGmsServiceBroker extends IInterface
{
    void getService(final IGmsCallbacks p0, final GetServiceRequest p1) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IGmsServiceBroker
    {
        public static IGmsServiceBroker asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
            if (queryLocalInterface != null && queryLocalInterface instanceof IGmsServiceBroker) {
                return (IGmsServiceBroker)queryLocalInterface;
            }
            return new zza(binder);
        }
        
        protected void getLegacyService(final int n, final IGmsCallbacks gmsCallbacks, final int n2, final String s, final String s2, final String[] array, final Bundle bundle, final IBinder binder, final String s3, final String s4) throws RemoteException {
            throw new UnsupportedOperationException();
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, int int1) throws RemoteException {
            if (n > 16777215) {
                return super.onTransact(n, parcel, parcel2, int1);
            }
            parcel.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
            final IGmsCallbacks interface1 = IGmsCallbacks.Stub.asInterface(parcel.readStrongBinder());
            final ValidateAccountRequest validateAccountRequest = null;
            GetServiceRequest getServiceRequest = null;
            if (n == 46) {
                if (parcel.readInt() != 0) {
                    getServiceRequest = (GetServiceRequest)GetServiceRequest.CREATOR.createFromParcel(parcel);
                }
                this.getService(interface1, getServiceRequest);
            }
            else if (n == 47) {
                ValidateAccountRequest validateAccountRequest2 = validateAccountRequest;
                if (parcel.readInt() != 0) {
                    validateAccountRequest2 = (ValidateAccountRequest)ValidateAccountRequest.CREATOR.createFromParcel(parcel);
                }
                this.validateAccount(interface1, validateAccountRequest2);
            }
            else {
                int1 = parcel.readInt();
                String string;
                if (n != 4) {
                    string = parcel.readString();
                }
                else {
                    string = null;
                }
                Object string2 = null;
                Object o = null;
                Object o2 = null;
                String string3 = null;
                Object o3 = null;
                Object string4 = null;
                Label_0595: {
                    if (n != 1) {
                        Label_0532: {
                            Label_0529: {
                                Label_0526: {
                                    Label_0524: {
                                        Label_0495: {
                                            if (n != 2 && n != 23 && n != 25 && n != 27) {
                                                if (n != 30) {
                                                    if (n == 34) {
                                                        string2 = parcel.readString();
                                                        break Label_0526;
                                                    }
                                                    if (n == 41 || n == 43 || n == 37 || n == 38) {
                                                        break Label_0495;
                                                    }
                                                    switch (n) {
                                                        default: {
                                                            break Label_0524;
                                                        }
                                                        case 19: {
                                                            o = parcel.readStrongBinder();
                                                            Bundle bundle;
                                                            if (parcel.readInt() != 0) {
                                                                bundle = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                                                            }
                                                            else {
                                                                bundle = null;
                                                            }
                                                            o2 = bundle;
                                                            o3 = (string3 = null);
                                                            string4 = (string2 = string3);
                                                            break Label_0595;
                                                        }
                                                        case 10: {
                                                            final String string5 = parcel.readString();
                                                            o3 = parcel.createStringArray();
                                                            string2 = string5;
                                                            break Label_0529;
                                                        }
                                                        case 9: {
                                                            final String string6 = parcel.readString();
                                                            o3 = parcel.createStringArray();
                                                            final String string7 = parcel.readString();
                                                            o = parcel.readStrongBinder();
                                                            string4 = parcel.readString();
                                                            if (parcel.readInt() != 0) {
                                                                o2 = Bundle.CREATOR.createFromParcel(parcel);
                                                                string2 = string6;
                                                                string3 = string7;
                                                                break Label_0595;
                                                            }
                                                            o2 = null;
                                                            string2 = string6;
                                                            string3 = string7;
                                                            break Label_0595;
                                                        }
                                                        case 20: {
                                                            break;
                                                        }
                                                        case 5:
                                                        case 6:
                                                        case 7:
                                                        case 8:
                                                        case 11:
                                                        case 12:
                                                        case 13:
                                                        case 14:
                                                        case 15:
                                                        case 16:
                                                        case 17:
                                                        case 18: {
                                                            break Label_0495;
                                                        }
                                                    }
                                                }
                                                o3 = parcel.createStringArray();
                                                final String string8 = parcel.readString();
                                                Bundle bundle2;
                                                if (parcel.readInt() != 0) {
                                                    bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                                                }
                                                else {
                                                    bundle2 = null;
                                                }
                                                o2 = bundle2;
                                                o = null;
                                                string3 = (String)(string4 = o);
                                                string2 = string8;
                                                break Label_0595;
                                            }
                                        }
                                        if (parcel.readInt() != 0) {
                                            o2 = Bundle.CREATOR.createFromParcel(parcel);
                                            string2 = (o3 = null);
                                            break Label_0532;
                                        }
                                    }
                                    string2 = null;
                                }
                                o3 = null;
                            }
                            o2 = null;
                        }
                        o = null;
                        string3 = (String)(string4 = o);
                    }
                    else {
                        string3 = parcel.readString();
                        o3 = parcel.createStringArray();
                        final String string9 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            o2 = Bundle.CREATOR.createFromParcel(parcel);
                            o = (string4 = null);
                            string2 = string9;
                        }
                        else {
                            o = null;
                            string4 = (o2 = o);
                            string2 = string9;
                        }
                    }
                }
                this.getLegacyService(n, interface1, int1, string, (String)string2, (String[])o3, (Bundle)o2, (IBinder)o, string3, (String)string4);
            }
            parcel2.writeNoException();
            return true;
        }
        
        protected void validateAccount(final IGmsCallbacks gmsCallbacks, final ValidateAccountRequest validateAccountRequest) throws RemoteException {
            throw new UnsupportedOperationException();
        }
        
        private static final class zza implements IGmsServiceBroker
        {
            private final IBinder zza;
            
            zza(final IBinder zza) {
                this.zza = zza;
            }
            
            public final IBinder asBinder() {
                return this.zza;
            }
            
            @Override
            public final void getService(final IGmsCallbacks gmsCallbacks, final GetServiceRequest getServiceRequest) throws RemoteException {
                while (true) {
                    final Parcel obtain = Parcel.obtain();
                    final Parcel obtain2 = Parcel.obtain();
                    while (true) {
                        try {
                            obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                            if (gmsCallbacks != null) {
                                final IBinder binder = gmsCallbacks.asBinder();
                                obtain.writeStrongBinder(binder);
                                if (getServiceRequest != null) {
                                    obtain.writeInt(1);
                                    getServiceRequest.writeToParcel(obtain, 0);
                                }
                                else {
                                    obtain.writeInt(0);
                                }
                                this.zza.transact(46, obtain, obtain2, 0);
                                obtain2.readException();
                                return;
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
        }
    }
}
