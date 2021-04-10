package com.google.android.gms.tasks;

import com.google.android.gms.common.internal.*;
import java.util.concurrent.*;

public final class Tasks
{
    public static <TResult> TResult await(final Task<TResult> task) throws ExecutionException, InterruptedException {
        Preconditions.checkNotMainThread();
        Preconditions.checkNotNull(task, "Task must not be null");
        if (task.isComplete()) {
            return (TResult)zzb((Task<Object>)task);
        }
        final zza zza = new zza(null);
        zza(task, (zzb)zza);
        zza.await();
        return (TResult)zzb((Task<Object>)task);
    }
    
    public static <TResult> TResult await(final Task<TResult> task, final long n, final TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        Preconditions.checkNotMainThread();
        Preconditions.checkNotNull(task, "Task must not be null");
        Preconditions.checkNotNull(timeUnit, "TimeUnit must not be null");
        if (task.isComplete()) {
            return zzb(task);
        }
        final zza zza = new zza(null);
        zza(task, (zzb)zza);
        if (zza.await(n, timeUnit)) {
            return zzb(task);
        }
        throw new TimeoutException("Timed out waiting for Task");
    }
    
    public static <TResult> Task<TResult> forResult(final TResult result) {
        final zzu<TResult> zzu = new zzu<TResult>();
        zzu.setResult(result);
        return zzu;
    }
    
    private static void zza(final Task<?> task, final zzb zzb) {
        task.addOnSuccessListener(TaskExecutors.zzagd, zzb);
        task.addOnFailureListener(TaskExecutors.zzagd, zzb);
        task.addOnCanceledListener(TaskExecutors.zzagd, zzb);
    }
    
    private static <TResult> TResult zzb(final Task<TResult> task) throws ExecutionException {
        if (task.isSuccessful()) {
            return task.getResult();
        }
        if (task.isCanceled()) {
            throw new CancellationException("Task is already canceled");
        }
        throw new ExecutionException(task.getException());
    }
    
    private static final class zza implements zzb
    {
        private final CountDownLatch zzfd;
        
        private zza() {
            this.zzfd = new CountDownLatch(1);
        }
        
        public final void await() throws InterruptedException {
            this.zzfd.await();
        }
        
        public final boolean await(final long n, final TimeUnit timeUnit) throws InterruptedException {
            return this.zzfd.await(n, timeUnit);
        }
        
        @Override
        public final void onCanceled() {
            this.zzfd.countDown();
        }
        
        @Override
        public final void onFailure(final Exception ex) {
            this.zzfd.countDown();
        }
        
        @Override
        public final void onSuccess(final Object o) {
            this.zzfd.countDown();
        }
    }
    
    interface zzb extends OnCanceledListener, OnFailureListener, OnSuccessListener<Object>
    {
    }
}
