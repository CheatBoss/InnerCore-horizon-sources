package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.stats.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.internal.safeparcel.*;
import java.util.*;
import android.os.*;
import java.util.concurrent.atomic.*;
import com.google.android.gms.common.util.*;
import com.google.android.gms.common.*;
import android.content.*;

public final class zzdr extends zzf
{
    private final zzef zzarz;
    private zzag zzasa;
    private volatile Boolean zzasb;
    private final zzv zzasc;
    private final zzev zzasd;
    private final List<Runnable> zzase;
    private final zzv zzasf;
    
    protected zzdr(final zzbt zzbt) {
        super(zzbt);
        this.zzase = new ArrayList<Runnable>();
        this.zzasd = new zzev(zzbt.zzbx());
        this.zzarz = new zzef(this);
        this.zzasc = new zzds(this, zzbt);
        this.zzasf = new zzdx(this, zzbt);
    }
    
    private final void onServiceDisconnected(final ComponentName componentName) {
        this.zzaf();
        if (this.zzasa != null) {
            this.zzasa = null;
            this.zzgo().zzjl().zzg("Disconnected from device MeasurementService", componentName);
            this.zzaf();
            this.zzdj();
        }
    }
    
    private final void zzcy() {
        this.zzaf();
        this.zzasd.start();
        this.zzasc.zzh(zzaf.zzakj.get());
    }
    
    private final void zzcz() {
        this.zzaf();
        if (!this.isConnected()) {
            return;
        }
        this.zzgo().zzjl().zzbx("Inactivity, disconnecting from the service");
        this.disconnect();
    }
    
    private final void zzf(final Runnable runnable) throws IllegalStateException {
        this.zzaf();
        if (this.isConnected()) {
            runnable.run();
            return;
        }
        if (this.zzase.size() >= 1000L) {
            this.zzgo().zzjd().zzbx("Discarding data. Max runnable queue size reached");
            return;
        }
        this.zzase.add(runnable);
        this.zzasf.zzh(60000L);
        this.zzdj();
    }
    
    private final boolean zzld() {
        this.zzgr();
        return true;
    }
    
    private final void zzlf() {
        this.zzaf();
        this.zzgo().zzjl().zzg("Processing queued up service tasks", this.zzase.size());
        for (final Runnable runnable : this.zzase) {
            try {
                runnable.run();
            }
            catch (Exception ex) {
                this.zzgo().zzjd().zzg("Task exception while flushing queue", ex);
            }
        }
        this.zzase.clear();
        this.zzasf.cancel();
    }
    
    private final zzh zzm(final boolean b) {
        this.zzgr();
        final zzaj zzgf = this.zzgf();
        String zzjn;
        if (b) {
            zzjn = this.zzgo().zzjn();
        }
        else {
            zzjn = null;
        }
        return zzgf.zzbr(zzjn);
    }
    
    public final void disconnect() {
        this.zzaf();
        this.zzcl();
        if (zzn.zzia()) {
            this.zzarz.zzlg();
        }
        try {
            ConnectionTracker.getInstance().unbindService(this.getContext(), (ServiceConnection)this.zzarz);
        }
        catch (IllegalStateException ex) {}
        catch (IllegalArgumentException ex2) {}
        this.zzasa = null;
    }
    
    public final boolean isConnected() {
        this.zzaf();
        this.zzcl();
        return this.zzasa != null;
    }
    
    protected final void zza(final zzag zzasa) {
        this.zzaf();
        Preconditions.checkNotNull(zzasa);
        this.zzasa = zzasa;
        this.zzcy();
        this.zzlf();
    }
    
    final void zza(final zzag zzag, final AbstractSafeParcelable abstractSafeParcelable, final zzh zzh) {
        this.zzaf();
        this.zzgb();
        this.zzcl();
        final boolean zzld = this.zzld();
        for (int n = 0, size = 100; n < 1001 && size == 100; ++n) {
            final ArrayList<AbstractSafeParcelable> list = new ArrayList<AbstractSafeParcelable>();
            Label_0095: {
                if (zzld) {
                    final List<AbstractSafeParcelable> zzr = this.zzgi().zzr(100);
                    if (zzr != null) {
                        list.addAll((Collection<?>)zzr);
                        size = zzr.size();
                        break Label_0095;
                    }
                }
                size = 0;
            }
            if (abstractSafeParcelable != null && size < 100) {
                list.add(abstractSafeParcelable);
            }
            final ArrayList<AbstractSafeParcelable> list2 = list;
            final int size2 = list2.size();
            int i = 0;
            while (i < size2) {
                final AbstractSafeParcelable value = list2.get(i);
                ++i;
                final AbstractSafeParcelable abstractSafeParcelable2 = value;
                zzar zzar = null;
                String s = null;
                Label_0200: {
                    if (!(abstractSafeParcelable2 instanceof zzad)) {
                        if (abstractSafeParcelable2 instanceof zzfh) {
                            try {
                                zzag.zza((zzfh)abstractSafeParcelable2, zzh);
                                continue;
                            }
                            catch (RemoteException ex) {
                                zzar = this.zzgo().zzjd();
                                s = "Failed to send attribute to the service";
                                break Label_0200;
                            }
                        }
                        if (abstractSafeParcelable2 instanceof zzl) {
                            try {
                                zzag.zza((zzl)abstractSafeParcelable2, zzh);
                                continue;
                            }
                            catch (RemoteException ex) {
                                zzar = this.zzgo().zzjd();
                                s = "Failed to send conditional property to the service";
                                break Label_0200;
                            }
                        }
                        this.zzgo().zzjd().zzbx("Discarding data. Unrecognized parcel type.");
                        continue;
                    }
                    try {
                        zzag.zza((zzad)abstractSafeParcelable2, zzh);
                        continue;
                    }
                    catch (RemoteException ex) {
                        zzar = this.zzgo().zzjd();
                        s = "Failed to send event to the service";
                    }
                }
                final RemoteException ex;
                zzar.zzg(s, ex);
            }
        }
    }
    
    public final void zza(final AtomicReference<String> atomicReference) {
        this.zzaf();
        this.zzcl();
        this.zzf(new zzdu(this, atomicReference, this.zzm(false)));
    }
    
    protected final void zza(final AtomicReference<List<zzl>> atomicReference, final String s, final String s2, final String s3) {
        this.zzaf();
        this.zzcl();
        this.zzf(new zzeb(this, atomicReference, s, s2, s3, this.zzm(false)));
    }
    
    protected final void zza(final AtomicReference<List<zzfh>> atomicReference, final String s, final String s2, final String s3, final boolean b) {
        this.zzaf();
        this.zzcl();
        this.zzf(new zzec(this, atomicReference, s, s2, s3, b, this.zzm(false)));
    }
    
    protected final void zzb(final zzad zzad, final String s) {
        Preconditions.checkNotNull(zzad);
        this.zzaf();
        this.zzcl();
        final boolean zzld = this.zzld();
        this.zzf(new zzdz(this, zzld, zzld && this.zzgi().zza(zzad), zzad, this.zzm(true), s));
    }
    
    protected final void zzb(final zzdn zzdn) {
        this.zzaf();
        this.zzcl();
        this.zzf(new zzdw(this, zzdn));
    }
    
    protected final void zzb(final zzfh zzfh) {
        this.zzaf();
        this.zzcl();
        this.zzf(new zzed(this, this.zzld() && this.zzgi().zza(zzfh), zzfh, this.zzm(true)));
    }
    
    protected final void zzd(final zzl zzl) {
        Preconditions.checkNotNull(zzl);
        this.zzaf();
        this.zzcl();
        this.zzgr();
        this.zzf(new zzea(this, true, this.zzgi().zzc(zzl), new zzl(zzl), this.zzm(true), zzl));
    }
    
    final void zzdj() {
        this.zzaf();
        this.zzcl();
        if (this.isConnected()) {
            return;
        }
        final Boolean zzasb = this.zzasb;
        final boolean b = false;
        if (zzasb == null) {
            this.zzaf();
            this.zzcl();
            final Boolean zzju = this.zzgp().zzju();
            boolean b2;
            if (zzju != null && zzju) {
                b2 = true;
            }
            else {
                this.zzgr();
                boolean b3 = false;
                boolean b4 = false;
                Label_0340: {
                    Label_0338: {
                        if (this.zzgf().zzjb() != 1) {
                            this.zzgo().zzjl().zzbx("Checking service availability");
                            final int googlePlayServicesAvailable = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(this.zzgm().getContext(), 12451000);
                            zzar zzar = null;
                            String s = null;
                            Label_0328: {
                                if (googlePlayServicesAvailable != 0) {
                                    Label_0308: {
                                        if (googlePlayServicesAvailable != 1) {
                                            Label_0290: {
                                                Label_0281: {
                                                    if (googlePlayServicesAvailable != 2) {
                                                        zzar zzar2;
                                                        String s2;
                                                        if (googlePlayServicesAvailable != 3) {
                                                            if (googlePlayServicesAvailable != 9) {
                                                                if (googlePlayServicesAvailable != 18) {
                                                                    this.zzgo().zzjg().zzg("Unexpected service status", googlePlayServicesAvailable);
                                                                    break Label_0281;
                                                                }
                                                                zzar = this.zzgo().zzjg();
                                                                s = "Service updating";
                                                                break Label_0328;
                                                            }
                                                            else {
                                                                zzar2 = this.zzgo().zzjg();
                                                                s2 = "Service invalid";
                                                            }
                                                        }
                                                        else {
                                                            zzar2 = this.zzgo().zzjg();
                                                            s2 = "Service disabled";
                                                        }
                                                        zzar2.zzbx(s2);
                                                    }
                                                    else {
                                                        this.zzgo().zzjk().zzbx("Service container out of date");
                                                        if (this.zzgm().zzme() < 13000) {
                                                            break Label_0308;
                                                        }
                                                        final Boolean zzju2 = this.zzgp().zzju();
                                                        if (zzju2 == null || zzju2) {
                                                            b3 = true;
                                                            break Label_0290;
                                                        }
                                                    }
                                                }
                                                b3 = false;
                                            }
                                            b4 = false;
                                            break Label_0340;
                                        }
                                        this.zzgo().zzjl().zzbx("Service missing");
                                    }
                                    b3 = false;
                                    break Label_0338;
                                }
                                zzar = this.zzgo().zzjl();
                                s = "Service available";
                            }
                            zzar.zzbx(s);
                        }
                        b3 = true;
                    }
                    b4 = true;
                }
                boolean b5 = b4;
                if (!b3) {
                    b5 = b4;
                    if (this.zzgq().zzib()) {
                        this.zzgo().zzjd().zzbx("No way to upload. Consider using the full version of Analytics");
                        b5 = false;
                    }
                }
                b2 = b3;
                if (b5) {
                    this.zzgp().zzg(b3);
                    b2 = b3;
                }
            }
            this.zzasb = b2;
        }
        if (this.zzasb) {
            this.zzarz.zzlh();
            return;
        }
        if (!this.zzgq().zzib()) {
            this.zzgr();
            final List queryIntentServices = this.getContext().getPackageManager().queryIntentServices(new Intent().setClassName(this.getContext(), "com.google.android.gms.measurement.AppMeasurementService"), 65536);
            boolean b6 = b;
            if (queryIntentServices != null) {
                b6 = b;
                if (queryIntentServices.size() > 0) {
                    b6 = true;
                }
            }
            if (b6) {
                final Intent intent = new Intent("com.google.android.gms.measurement.START");
                final Context context = this.getContext();
                this.zzgr();
                intent.setComponent(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementService"));
                this.zzarz.zzc(intent);
                return;
            }
            this.zzgo().zzjd().zzbx("Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest");
        }
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
    
    protected final void zzkz() {
        this.zzaf();
        this.zzcl();
        this.zzf(new zzdv(this, this.zzm(true)));
    }
    
    protected final void zzlc() {
        this.zzaf();
        this.zzcl();
        this.zzf(new zzdy(this, this.zzm(true)));
    }
    
    final Boolean zzle() {
        return this.zzasb;
    }
}
