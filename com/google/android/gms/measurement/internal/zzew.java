package com.google.android.gms.measurement.internal;

import android.app.*;
import com.google.android.gms.common.util.*;
import android.content.*;
import android.os.*;
import android.app.job.*;

public final class zzew extends zzez
{
    private final zzv zzata;
    private final AlarmManager zzyt;
    private Integer zzyu;
    
    protected zzew(final zzfa zzfa) {
        super(zzfa);
        this.zzyt = (AlarmManager)this.getContext().getSystemService("alarm");
        this.zzata = new zzex(this, zzfa.zzmb(), zzfa);
    }
    
    private final int getJobId() {
        if (this.zzyu == null) {
            final String value = String.valueOf(this.getContext().getPackageName());
            String concat;
            if (value.length() != 0) {
                concat = "measurement".concat(value);
            }
            else {
                concat = new String("measurement");
            }
            this.zzyu = concat.hashCode();
        }
        return this.zzyu;
    }
    
    private final PendingIntent zzeo() {
        final Intent setClassName = new Intent().setClassName(this.getContext(), "com.google.android.gms.measurement.AppMeasurementReceiver");
        setClassName.setAction("com.google.android.gms.measurement.UPLOAD");
        return PendingIntent.getBroadcast(this.getContext(), 0, setClassName, 0);
    }
    
    private final void zzlm() {
        final JobScheduler jobScheduler = (JobScheduler)this.getContext().getSystemService("jobscheduler");
        this.zzgo().zzjl().zzg("Cancelling job. JobID", this.getJobId());
        jobScheduler.cancel(this.getJobId());
    }
    
    public final void cancel() {
        this.zzcl();
        this.zzyt.cancel(this.zzeo());
        this.zzata.cancel();
        if (Build$VERSION.SDK_INT >= 24) {
            this.zzlm();
        }
    }
    
    @Override
    protected final boolean zzgt() {
        this.zzyt.cancel(this.zzeo());
        if (Build$VERSION.SDK_INT >= 24) {
            this.zzlm();
        }
        return false;
    }
    
    public final void zzh(final long minimumLatency) {
        this.zzcl();
        this.zzgr();
        if (!zzbj.zza(this.getContext())) {
            this.zzgo().zzjk().zzbx("Receiver not registered/enabled");
        }
        this.zzgr();
        if (!zzfk.zza(this.getContext(), false)) {
            this.zzgo().zzjk().zzbx("Service not registered/enabled");
        }
        this.cancel();
        final long elapsedRealtime = this.zzbx().elapsedRealtime();
        if (minimumLatency < Math.max(0L, zzaf.zzaka.get()) && !this.zzata.zzej()) {
            this.zzgo().zzjl().zzbx("Scheduling upload with DelayedRunnable");
            this.zzata.zzh(minimumLatency);
        }
        this.zzgr();
        if (Build$VERSION.SDK_INT >= 24) {
            this.zzgo().zzjl().zzbx("Scheduling upload with JobScheduler");
            final ComponentName componentName = new ComponentName(this.getContext(), "com.google.android.gms.measurement.AppMeasurementJobService");
            final JobScheduler jobScheduler = (JobScheduler)this.getContext().getSystemService("jobscheduler");
            final JobInfo$Builder jobInfo$Builder = new JobInfo$Builder(this.getJobId(), componentName);
            jobInfo$Builder.setMinimumLatency(minimumLatency);
            jobInfo$Builder.setOverrideDeadline(minimumLatency << 1);
            final PersistableBundle extras = new PersistableBundle();
            extras.putString("action", "com.google.android.gms.measurement.UPLOAD");
            jobInfo$Builder.setExtras(extras);
            final JobInfo build = jobInfo$Builder.build();
            this.zzgo().zzjl().zzg("Scheduling job. JobID", this.getJobId());
            jobScheduler.schedule(build);
            return;
        }
        this.zzgo().zzjl().zzbx("Scheduling upload with AlarmManager");
        this.zzyt.setInexactRepeating(2, elapsedRealtime + minimumLatency, Math.max(zzaf.zzajv.get(), minimumLatency), this.zzeo());
    }
}
