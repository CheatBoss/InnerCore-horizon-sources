package com.google.android.gms.tasks;

import java.util.*;

final class zzr<TResult>
{
    private final Object mLock;
    private Queue<zzq<TResult>> zzaga;
    private boolean zzagb;
    
    zzr() {
        this.mLock = new Object();
    }
    
    public final void zza(final Task<TResult> task) {
        synchronized (this.mLock) {
            if (this.zzaga != null) {
                if (!this.zzagb) {
                    this.zzagb = true;
                    // monitorexit(this.mLock)
                    while (true) {
                        final Object mLock = this.mLock;
                        synchronized (this.mLock) {
                            final zzq<TResult> zzq = this.zzaga.poll();
                            if (zzq == null) {
                                this.zzagb = false;
                                return;
                            }
                            // monitorexit(this.mLock)
                            zzq.onComplete(task);
                            continue;
                        }
                        break;
                    }
                }
            }
        }
    }
    
    public final void zza(final zzq<TResult> zzq) {
        synchronized (this.mLock) {
            if (this.zzaga == null) {
                this.zzaga = new ArrayDeque<zzq<TResult>>();
            }
            this.zzaga.add(zzq);
        }
    }
}
