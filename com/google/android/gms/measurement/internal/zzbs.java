package com.google.android.gms.measurement.internal;

import java.util.concurrent.*;
import com.google.android.gms.common.internal.*;
import android.os.*;

final class zzbs extends Thread
{
    private final /* synthetic */ zzbo zzapg;
    private final Object zzapj;
    private final BlockingQueue<zzbr<?>> zzapk;
    
    public zzbs(final zzbo zzapg, final String name, final BlockingQueue<zzbr<?>> zzapk) {
        this.zzapg = zzapg;
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(zzapk);
        this.zzapj = new Object();
        this.zzapk = zzapk;
        this.setName(name);
    }
    
    private final void zza(final InterruptedException ex) {
        this.zzapg.zzgo().zzjg().zzg(String.valueOf(this.getName()).concat(" was interrupted"), ex);
    }
    
    @Override
    public final void run() {
        int i = 0;
        while (i == 0) {
            try {
                this.zzapg.zzapc.acquire();
                i = 1;
            }
            catch (InterruptedException ex) {
                this.zza(ex);
            }
        }
        while (true) {
            while (true) {
                try {
                    final int threadPriority = Process.getThreadPriority(Process.myTid());
                    while (true) {
                        final zzbr<?> zzbr = this.zzapk.poll();
                        if (zzbr != null) {
                            if (!zzbr.zzapi) {
                                break;
                            }
                            final int threadPriority2 = threadPriority;
                            Process.setThreadPriority(threadPriority2);
                            zzbr.run();
                        }
                        else {
                            synchronized (this.zzapj) {
                                if (this.zzapk.peek() == null && !this.zzapg.zzapd) {
                                    try {
                                        this.zzapj.wait(30000L);
                                    }
                                    catch (InterruptedException ex2) {
                                        this.zza(ex2);
                                    }
                                }
                                // monitorexit(this.zzapj)
                                this.zzapg.zzapb;
                                synchronized (this.zzapj) {
                                    if (this.zzapk.peek() == null) {
                                        // monitorexit(this.zzapj)
                                        this.zzapg.zzapb;
                                        synchronized (this.zzapj) {
                                            this.zzapg.zzapc.release();
                                            this.zzapg.zzapb.notifyAll();
                                            if (this == this.zzapg.zzaov) {
                                                zzbo.zza(this.zzapg, null);
                                            }
                                            else if (this == this.zzapg.zzaow) {
                                                zzbo.zzb(this.zzapg, null);
                                            }
                                            else {
                                                this.zzapg.zzgo().zzjd().zzbx("Current scheduler thread is neither worker nor network");
                                            }
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                finally {
                    synchronized (this.zzapg.zzapb) {
                        this.zzapg.zzapc.release();
                        this.zzapg.zzapb.notifyAll();
                        if (this != this.zzapg.zzaov) {
                            if (this == this.zzapg.zzaow) {
                                zzbo.zzb(this.zzapg, null);
                            }
                            else {
                                this.zzapg.zzgo().zzjd().zzbx("Current scheduler thread is neither worker nor network");
                            }
                        }
                        else {
                            zzbo.zza(this.zzapg, null);
                        }
                    }
                    // monitorexit(zzbo.zzc(this.zzapg))
                }
                final int threadPriority2 = 10;
                continue;
            }
        }
    }
    
    public final void zzke() {
        synchronized (this.zzapj) {
            this.zzapj.notifyAll();
        }
    }
}
