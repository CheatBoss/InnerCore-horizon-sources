package com.google.android.gms.tasks;

final class zzd implements Runnable
{
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzc zzafo;
    
    zzd(final zzc zzafo, final Task zzafn) {
        this.zzafo = zzafo;
        this.zzafn = zzafn;
    }
    
    @Override
    public final void run() {
        if (this.zzafn.isCanceled()) {
            this.zzafo.zzafm.zzdp();
            return;
        }
        try {
            this.zzafo.zzafm.setResult(this.zzafo.zzafl.then(this.zzafn));
        }
        catch (Exception exception) {
            this.zzafo.zzafm.setException(exception);
        }
        catch (RuntimeExecutionException exception2) {
            if (exception2.getCause() instanceof Exception) {
                this.zzafo.zzafm.setException((Exception)exception2.getCause());
                return;
            }
            this.zzafo.zzafm.setException(exception2);
        }
    }
}
