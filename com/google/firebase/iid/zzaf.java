package com.google.firebase.iid;

import java.util.concurrent.*;
import android.util.*;
import android.content.*;
import android.os.*;

final class zzaf implements Runnable
{
    private final zzac zzbz;
    
    zzaf(final zzac zzbz) {
        this.zzbz = zzbz;
    }
    
    @Override
    public final void run() {
        final zzac zzbz = this.zzbz;
        while (true) {
            synchronized (zzbz) {
                if (zzbz.state != 2) {
                    return;
                }
                if (zzbz.zzbw.isEmpty()) {
                    zzbz.zzy();
                    return;
                }
                final zzaj<?> zzaj = zzbz.zzbw.poll();
                zzbz.zzbx.put(zzaj.zzcc, (Object)zzaj);
                zzbz.zzby.zzbr.schedule(new zzag(zzbz, zzaj), 30L, TimeUnit.SECONDS);
                // monitorexit(zzbz)
                if (Log.isLoggable("MessengerIpcClient", 3)) {
                    final String value = String.valueOf(zzaj);
                    final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 8);
                    sb.append("Sending ");
                    sb.append(value);
                    Log.d("MessengerIpcClient", sb.toString());
                }
                final Context zza = zzbz.zzby.zzv;
                final Messenger zzbu = zzbz.zzbu;
                final Message obtain = Message.obtain();
                obtain.what = zzaj.what;
                obtain.arg1 = zzaj.zzcc;
                obtain.replyTo = zzbu;
                final Bundle data = new Bundle();
                data.putBoolean("oneWay", zzaj.zzaa());
                data.putString("pkg", zza.getPackageName());
                data.putBundle("data", zzaj.zzce);
                obtain.setData(data);
                try {
                    zzbz.zzbv.send(obtain);
                }
                catch (RemoteException ex) {
                    zzbz.zza(2, ex.getMessage());
                }
            }
        }
    }
}
