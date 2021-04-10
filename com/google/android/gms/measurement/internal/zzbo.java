package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.*;
import android.content.*;
import java.util.concurrent.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.util.*;

public final class zzbo extends zzcp
{
    private static final AtomicLong zzape;
    private zzbs zzaov;
    private zzbs zzaow;
    private final PriorityBlockingQueue<zzbr<?>> zzaox;
    private final BlockingQueue<zzbr<?>> zzaoy;
    private final Thread.UncaughtExceptionHandler zzaoz;
    private final Thread.UncaughtExceptionHandler zzapa;
    private final Object zzapb;
    private final Semaphore zzapc;
    private volatile boolean zzapd;
    
    static {
        zzape = new AtomicLong(Long.MIN_VALUE);
    }
    
    zzbo(final zzbt zzbt) {
        super(zzbt);
        this.zzapb = new Object();
        this.zzapc = new Semaphore(2);
        this.zzaox = new PriorityBlockingQueue<zzbr<?>>();
        this.zzaoy = new LinkedBlockingQueue<zzbr<?>>();
        this.zzaoz = new zzbq(this, "Thread death: Uncaught exception on worker thread");
        this.zzapa = new zzbq(this, "Thread death: Uncaught exception on network thread");
    }
    
    private final void zza(final zzbr<?> zzbr) {
        synchronized (this.zzapb) {
            this.zzaox.add(zzbr);
            if (this.zzaov == null) {
                (this.zzaov = new zzbs("Measurement Worker", this.zzaox)).setUncaughtExceptionHandler(this.zzaoz);
                this.zzaov.start();
            }
            else {
                this.zzaov.zzke();
            }
        }
    }
    
    @Override
    public final void zzaf() {
        if (Thread.currentThread() == this.zzaov) {
            return;
        }
        throw new IllegalStateException("Call expected from worker thread");
    }
    
    public final <V> Future<V> zzb(final Callable<V> callable) throws IllegalStateException {
        this.zzcl();
        Preconditions.checkNotNull(callable);
        final zzbr<V> zzbr = new zzbr<V>(callable, false, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzaov) {
            if (!this.zzaox.isEmpty()) {
                this.zzgo().zzjg().zzbx("Callable skipped the worker queue.");
            }
            zzbr.run();
            return zzbr;
        }
        this.zza(zzbr);
        return zzbr;
    }
    
    public final <V> Future<V> zzc(final Callable<V> callable) throws IllegalStateException {
        this.zzcl();
        Preconditions.checkNotNull(callable);
        final zzbr<V> zzbr = new zzbr<V>(callable, true, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzaov) {
            zzbr.run();
            return zzbr;
        }
        this.zza(zzbr);
        return zzbr;
    }
    
    public final void zzc(final Runnable runnable) throws IllegalStateException {
        this.zzcl();
        Preconditions.checkNotNull(runnable);
        this.zza(new zzbr<Object>(this, runnable, false, "Task exception on worker thread"));
    }
    
    public final void zzd(final Runnable runnable) throws IllegalStateException {
        this.zzcl();
        Preconditions.checkNotNull(runnable);
        final zzbr<Object> zzbr = new zzbr<Object>(this, runnable, false, "Task exception on network thread");
        synchronized (this.zzapb) {
            this.zzaoy.add(zzbr);
            if (this.zzaow == null) {
                (this.zzaow = new zzbs("Measurement Network", this.zzaoy)).setUncaughtExceptionHandler(this.zzapa);
                this.zzaow.start();
            }
            else {
                this.zzaow.zzke();
            }
        }
    }
    
    @Override
    public final void zzgc() {
        if (Thread.currentThread() == this.zzaow) {
            return;
        }
        throw new IllegalStateException("Call expected from network thread");
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
    
    public final boolean zzkb() {
        return Thread.currentThread() == this.zzaov;
    }
}
