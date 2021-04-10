package com.google.firebase.iid;

import android.util.*;
import com.google.android.gms.common.stats.*;
import java.util.*;
import android.os.*;
import com.google.android.gms.common.internal.*;
import android.content.*;
import java.util.concurrent.*;

final class zzac implements ServiceConnection
{
    int state;
    final Messenger zzbu;
    zzah zzbv;
    final Queue<zzaj<?>> zzbw;
    final SparseArray<zzaj<?>> zzbx;
    final /* synthetic */ zzaa zzby;
    
    private zzac(final zzaa zzby) {
        this.zzby = zzby;
        this.state = 0;
        this.zzbu = new Messenger(new Handler(Looper.getMainLooper(), (Handler$Callback)new zzad(this)));
        this.zzbw = new ArrayDeque<zzaj<?>>();
        this.zzbx = (SparseArray<zzaj<?>>)new SparseArray();
    }
    
    private final void zzx() {
        this.zzby.zzbr.execute(new zzaf(this));
    }
    
    public final void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        synchronized (this) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Service connected");
            }
            if (binder == null) {
                this.zza(0, "Null service connection");
                return;
            }
            try {
                this.zzbv = new zzah(binder);
                this.state = 2;
                this.zzx();
            }
            catch (RemoteException ex) {
                this.zza(0, ex.getMessage());
            }
        }
    }
    
    public final void onServiceDisconnected(final ComponentName componentName) {
        synchronized (this) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Service disconnected");
            }
            this.zza(2, "Service disconnected");
        }
    }
    
    final void zza(final int n) {
        synchronized (this) {
            final zzaj zzaj = (zzaj)this.zzbx.get(n);
            if (zzaj != null) {
                final StringBuilder sb = new StringBuilder(31);
                sb.append("Timing out request: ");
                sb.append(n);
                Log.w("MessengerIpcClient", sb.toString());
                this.zzbx.remove(n);
                zzaj.zza(new zzak(3, "Timed out waiting for response"));
                this.zzy();
            }
        }
    }
    
    final void zza(int i, final String s) {
        synchronized (this) {
            if (Log.isLoggable("MessengerIpcClient", 3)) {
                final String value = String.valueOf(s);
                String concat;
                if (value.length() != 0) {
                    concat = "Disconnected: ".concat(value);
                }
                else {
                    concat = new String("Disconnected: ");
                }
                Log.d("MessengerIpcClient", concat);
            }
            final int state = this.state;
            if (state == 0) {
                throw new IllegalStateException();
            }
            if (state == 1 || state == 2) {
                if (Log.isLoggable("MessengerIpcClient", 2)) {
                    Log.v("MessengerIpcClient", "Unbinding service");
                }
                this.state = 4;
                ConnectionTracker.getInstance().unbindService(this.zzby.zzv, (ServiceConnection)this);
                final zzak zzak = new zzak(i, s);
                final Iterator<zzaj> iterator = (Iterator<zzaj>)this.zzbw.iterator();
                while (iterator.hasNext()) {
                    iterator.next().zza(zzak);
                }
                this.zzbw.clear();
                for (i = 0; i < this.zzbx.size(); ++i) {
                    ((zzaj)this.zzbx.valueAt(i)).zza(zzak);
                }
                this.zzbx.clear();
                return;
            }
            if (state == 3) {
                this.state = 4;
                return;
            }
            if (state == 4) {
                return;
            }
            i = this.state;
            final StringBuilder sb = new StringBuilder(26);
            sb.append("Unknown state: ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    final boolean zza(final Message message) {
        final int arg1 = message.arg1;
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            final StringBuilder sb = new StringBuilder(41);
            sb.append("Received response to request: ");
            sb.append(arg1);
            Log.d("MessengerIpcClient", sb.toString());
        }
        synchronized (this) {
            final zzaj zzaj = (zzaj)this.zzbx.get(arg1);
            if (zzaj == null) {
                final StringBuilder sb2 = new StringBuilder(50);
                sb2.append("Received response for unknown request: ");
                sb2.append(arg1);
                Log.w("MessengerIpcClient", sb2.toString());
                return true;
            }
            this.zzbx.remove(arg1);
            this.zzy();
            // monitorexit(this)
            final Bundle data = message.getData();
            if (data.getBoolean("unsupported", false)) {
                zzaj.zza(new zzak(4, "Not supported by GmsCore"));
                return true;
            }
            zzaj.zzb(data);
            return true;
        }
    }
    
    final boolean zzb(final zzaj zzaj) {
        while (true) {
            while (true) {
                synchronized (this) {
                    final int state = this.state;
                    if (state != 0) {
                        if (state == 1) {
                            this.zzbw.add(zzaj);
                            return true;
                        }
                        if (state == 2) {
                            this.zzbw.add(zzaj);
                            this.zzx();
                            return true;
                        }
                        if (state != 3 && state != 4) {
                            final int state2 = this.state;
                            final StringBuilder sb = new StringBuilder(26);
                            sb.append("Unknown state: ");
                            sb.append(state2);
                            throw new IllegalStateException(sb.toString());
                        }
                        return false;
                    }
                    else {
                        this.zzbw.add(zzaj);
                        if (this.state == 0) {
                            final boolean b = true;
                            Preconditions.checkState(b);
                            if (Log.isLoggable("MessengerIpcClient", 2)) {
                                Log.v("MessengerIpcClient", "Starting bind to GmsCore");
                            }
                            this.state = 1;
                            final Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
                            intent.setPackage("com.google.android.gms");
                            if (!ConnectionTracker.getInstance().bindService(this.zzby.zzv, intent, (ServiceConnection)this, 1)) {
                                this.zza(0, "Unable to bind to service");
                            }
                            else {
                                this.zzby.zzbr.schedule(new zzae(this), 30L, TimeUnit.SECONDS);
                            }
                            return true;
                        }
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
    
    final void zzy() {
        synchronized (this) {
            if (this.state == 2 && this.zzbw.isEmpty() && this.zzbx.size() == 0) {
                if (Log.isLoggable("MessengerIpcClient", 2)) {
                    Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
                }
                this.state = 3;
                ConnectionTracker.getInstance().unbindService(this.zzby.zzv, (ServiceConnection)this);
            }
        }
    }
    
    final void zzz() {
        synchronized (this) {
            if (this.state == 1) {
                this.zza(1, "Timed out while binding");
            }
        }
    }
}
