package com.google.android.gms.common.api.internal;

import java.lang.ref.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.api.*;
import android.os.*;
import android.util.*;

public abstract class BasePendingResult<R extends Result> extends PendingResult<R>
{
    static final ThreadLocal<Boolean> zzez;
    private zza mResultGuardian;
    private Status mStatus;
    private R zzdm;
    private final Object zzfa;
    private final CallbackHandler<R> zzfb;
    private final WeakReference<GoogleApiClient> zzfc;
    private final CountDownLatch zzfd;
    private final ArrayList<StatusListener> zzfe;
    private ResultCallback<? super R> zzff;
    private final AtomicReference<zzcn> zzfg;
    private volatile boolean zzfh;
    private boolean zzfi;
    private boolean zzfj;
    private ICancelToken zzfk;
    private boolean zzfm;
    
    static {
        zzez = new zzo();
    }
    
    @Deprecated
    BasePendingResult() {
        this.zzfa = new Object();
        this.zzfd = new CountDownLatch(1);
        this.zzfe = new ArrayList<StatusListener>();
        this.zzfg = new AtomicReference<zzcn>();
        this.zzfm = false;
        this.zzfb = new CallbackHandler<R>(Looper.getMainLooper());
        this.zzfc = new WeakReference<GoogleApiClient>(null);
    }
    
    private final R get() {
        Object zzfa = this.zzfa;
        synchronized (zzfa) {
            Preconditions.checkState(this.zzfh ^ true, "Result has already been consumed.");
            Preconditions.checkState(this.isReady(), "Result is not ready.");
            final Result zzdm = this.zzdm;
            this.zzdm = null;
            this.zzff = null;
            this.zzfh = true;
            // monitorexit(zzfa)
            zzfa = this.zzfg.getAndSet(null);
            if (zzfa != null) {
                ((zzcn)zzfa).zzc(this);
            }
            return (R)zzdm;
        }
    }
    
    private final void zza(final R zzdm) {
        this.zzdm = zzdm;
        this.zzfk = null;
        this.zzfd.countDown();
        this.mStatus = this.zzdm.getStatus();
        if (this.zzfi) {
            this.zzff = null;
        }
        else if (this.zzff == null) {
            if (this.zzdm instanceof Releasable) {
                this.mResultGuardian = new zza((zzo)null);
            }
        }
        else {
            this.zzfb.removeMessages(2);
            this.zzfb.zza(this.zzff, this.get());
        }
        final ArrayList<StatusListener> list = this.zzfe;
        final int size = list.size();
        int i = 0;
        while (i < size) {
            final StatusListener value = list.get(i);
            ++i;
            value.onComplete(this.mStatus);
        }
        this.zzfe.clear();
    }
    
    public static void zzb(final Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable)result).release();
            }
            catch (RuntimeException ex) {
                final String value = String.valueOf(result);
                final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 18);
                sb.append("Unable to release ");
                sb.append(value);
                Log.w("BasePendingResult", sb.toString(), (Throwable)ex);
            }
        }
    }
    
    protected abstract R createFailedResult(final Status p0);
    
    public final boolean isReady() {
        return this.zzfd.getCount() == 0L;
    }
    
    public final void setResult(final R r) {
        synchronized (this.zzfa) {
            if (!this.zzfj && !this.zzfi) {
                this.isReady();
                Preconditions.checkState(this.isReady() ^ true, "Results have already been set");
                Preconditions.checkState(this.zzfh ^ true, "Result has already been consumed");
                this.zza(r);
                return;
            }
            zzb(r);
        }
    }
    
    public final void zzb(final Status status) {
        synchronized (this.zzfa) {
            if (!this.isReady()) {
                this.setResult(this.createFailedResult(status));
                this.zzfj = true;
            }
        }
    }
    
    public static class CallbackHandler<R extends Result> extends Handler
    {
        public CallbackHandler() {
            this(Looper.getMainLooper());
        }
        
        public CallbackHandler(final Looper looper) {
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            final int what = message.what;
            if (what != 1) {
                if (what != 2) {
                    final int what2 = message.what;
                    final StringBuilder sb = new StringBuilder(45);
                    sb.append("Don't know how to handle message: ");
                    sb.append(what2);
                    Log.wtf("BasePendingResult", sb.toString(), (Throwable)new Exception());
                    return;
                }
                ((BasePendingResult)message.obj).zzb(Status.RESULT_TIMEOUT);
            }
            else {
                final Pair pair = (Pair)message.obj;
                final ResultCallback resultCallback = (ResultCallback)pair.first;
                final Result result = (Result)pair.second;
                try {
                    resultCallback.onResult(result);
                }
                catch (RuntimeException ex) {
                    BasePendingResult.zzb(result);
                    throw ex;
                }
            }
        }
        
        public final void zza(final ResultCallback<? super R> resultCallback, final R r) {
            this.sendMessage(this.obtainMessage(1, (Object)new Pair((Object)resultCallback, (Object)r)));
        }
    }
    
    private final class zza
    {
        @Override
        protected final void finalize() throws Throwable {
            BasePendingResult.zzb(BasePendingResult.this.zzdm);
            super.finalize();
        }
    }
}
