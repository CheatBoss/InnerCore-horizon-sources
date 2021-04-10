package com.google.android.gms.tasks;

final class zzf implements Runnable
{
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zze zzafp;
    
    zzf(final zze zzafp, final Task zzafn) {
        this.zzafp = zzafp;
        this.zzafn = zzafn;
    }
    
    @Override
    public final void run() {
        try {
            final Task task = this.zzafp.zzafl.then(this.zzafn);
            if (task == null) {
                this.zzafp.onFailure(new NullPointerException("Continuation returned null"));
                return;
            }
            task.addOnSuccessListener(TaskExecutors.zzagd, this.zzafp);
            task.addOnFailureListener(TaskExecutors.zzagd, this.zzafp);
            task.addOnCanceledListener(TaskExecutors.zzagd, this.zzafp);
        }
        catch (Exception exception) {
            this.zzafp.zzafm.setException(exception);
        }
        catch (RuntimeExecutionException exception2) {
            if (exception2.getCause() instanceof Exception) {
                this.zzafp.zzafm.setException((Exception)exception2.getCause());
                return;
            }
            this.zzafp.zzafm.setException(exception2);
        }
    }
}
