package com.google.firebase.iid;

import android.os.*;
import android.util.*;

public class zzk implements Parcelable
{
    public static final Parcelable$Creator<zzk> CREATOR;
    private Messenger zzad;
    private zzu zzae;
    
    static {
        CREATOR = (Parcelable$Creator)new zzl();
    }
    
    public zzk(final IBinder binder) {
        if (Build$VERSION.SDK_INT >= 21) {
            this.zzad = new Messenger(binder);
            return;
        }
        this.zzae = new zzv(binder);
    }
    
    private final IBinder getBinder() {
        final Messenger zzad = this.zzad;
        if (zzad != null) {
            return zzad.getBinder();
        }
        return this.zzae.asBinder();
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        try {
            return this.getBinder().equals(((zzk)o).getBinder());
        }
        catch (ClassCastException ex) {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return this.getBinder().hashCode();
    }
    
    public final void send(final Message message) throws RemoteException {
        final Messenger zzad = this.zzad;
        if (zzad != null) {
            zzad.send(message);
            return;
        }
        this.zzae.send(message);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final Messenger zzad = this.zzad;
        IBinder binder;
        if (zzad != null) {
            binder = zzad.getBinder();
        }
        else {
            binder = this.zzae.asBinder();
        }
        parcel.writeStrongBinder(binder);
    }
    
    public static final class zza extends ClassLoader
    {
        @Override
        protected final Class<?> loadClass(final String s, final boolean b) throws ClassNotFoundException {
            if ("com.google.android.gms.iid.MessengerCompat".equals(s)) {
                if (FirebaseInstanceId.zzk()) {
                    Log.d("FirebaseInstanceId", "Using renamed FirebaseIidMessengerCompat class");
                }
                return zzk.class;
            }
            return super.loadClass(s, b);
        }
    }
}
