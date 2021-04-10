package com.google.firebase.iid;

import android.util.*;
import android.os.*;

final class zzah
{
    private final Messenger zzad;
    private final zzk zzcb;
    
    zzah(final IBinder binder) throws RemoteException {
        final String interfaceDescriptor = binder.getInterfaceDescriptor();
        if ("android.os.IMessenger".equals(interfaceDescriptor)) {
            this.zzad = new Messenger(binder);
            this.zzcb = null;
            return;
        }
        if ("com.google.android.gms.iid.IMessengerCompat".equals(interfaceDescriptor)) {
            this.zzcb = new zzk(binder);
            this.zzad = null;
            return;
        }
        final String value = String.valueOf(interfaceDescriptor);
        String concat;
        if (value.length() != 0) {
            concat = "Invalid interface descriptor: ".concat(value);
        }
        else {
            concat = new String("Invalid interface descriptor: ");
        }
        Log.w("MessengerIpcClient", concat);
        throw new RemoteException();
    }
    
    final void send(final Message message) throws RemoteException {
        final Messenger zzad = this.zzad;
        if (zzad != null) {
            zzad.send(message);
            return;
        }
        final zzk zzcb = this.zzcb;
        if (zzcb != null) {
            zzcb.send(message);
            return;
        }
        throw new IllegalStateException("Both messengers are null");
    }
}
