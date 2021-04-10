package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import java.util.concurrent.*;

final class zzbr<V> extends FutureTask<V> implements Comparable<zzbr>
{
    private final String zzapf;
    private final /* synthetic */ zzbo zzapg;
    private final long zzaph;
    final boolean zzapi;
    
    zzbr(final zzbo zzapg, final Runnable runnable, final boolean b, final String zzapf) {
        this.zzapg = zzapg;
        super(runnable, null);
        Preconditions.checkNotNull(zzapf);
        final long andIncrement = zzbo.zzape.getAndIncrement();
        this.zzaph = andIncrement;
        this.zzapf = zzapf;
        this.zzapi = false;
        if (andIncrement == Long.MAX_VALUE) {
            zzapg.zzgo().zzjd().zzbx("Tasks index overflow");
        }
    }
    
    zzbr(final zzbo zzapg, final Callable<V> callable, final boolean zzapi, final String zzapf) {
        this.zzapg = zzapg;
        super(callable);
        Preconditions.checkNotNull(zzapf);
        final long andIncrement = zzbo.zzape.getAndIncrement();
        this.zzaph = andIncrement;
        this.zzapf = zzapf;
        this.zzapi = zzapi;
        if (andIncrement == Long.MAX_VALUE) {
            zzapg.zzgo().zzjd().zzbx("Tasks index overflow");
        }
    }
    
    @Override
    protected final void setException(final Throwable exception) {
        this.zzapg.zzgo().zzjd().zzg(this.zzapf, exception);
        if (exception instanceof zzbp) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
        }
        super.setException(exception);
    }
}
