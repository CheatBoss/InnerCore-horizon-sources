package com.google.android.gms.measurement.internal;

import android.app.*;
import android.os.*;
import android.text.*;
import android.content.*;
import android.net.*;

final class zzdm implements Application$ActivityLifecycleCallbacks
{
    private final /* synthetic */ zzcs zzarc;
    
    private zzdm(final zzcs zzarc) {
        this.zzarc = zzarc;
    }
    
    public final void onActivityCreated(final Activity activity, final Bundle bundle) {
        while (true) {
            while (true) {
                Label_0293: {
                    Label_0288: {
                        try {
                            this.zzarc.zzgo().zzjl().zzbx("onActivityCreated");
                            final Intent intent = activity.getIntent();
                            if (intent == null) {
                                break;
                            }
                            final Uri data = intent.getData();
                            if (data == null || !data.isHierarchical()) {
                                break;
                            }
                            if (bundle == null) {
                                final Bundle zza = this.zzarc.zzgm().zza(data);
                                this.zzarc.zzgm();
                                String s;
                                if (zzfk.zzd(intent)) {
                                    s = "gs";
                                }
                                else {
                                    s = "auto";
                                }
                                if (zza != null) {
                                    this.zzarc.logEvent(s, "_cmp", zza);
                                }
                            }
                            final String queryParameter = data.getQueryParameter("referrer");
                            if (TextUtils.isEmpty((CharSequence)queryParameter)) {
                                return;
                            }
                            if (queryParameter.contains("gclid") && (queryParameter.contains("utm_campaign") || queryParameter.contains("utm_source") || queryParameter.contains("utm_medium") || queryParameter.contains("utm_term") || queryParameter.contains("utm_content"))) {
                                break Label_0288;
                            }
                            break Label_0293;
                            // iftrue(Label_0214:, n != 0)
                            this.zzarc.zzgo().zzjk().zzbx("Activity created with data 'referrer' param without gclid and at least one utm field");
                            return;
                            Label_0214: {
                                this.zzarc.zzgo().zzjk().zzg("Activity created with referrer", queryParameter);
                            }
                            // iftrue(Label_0275:, TextUtils.isEmpty((CharSequence)queryParameter))
                            this.zzarc.zzb("auto", "_ldl", (Object)queryParameter, true);
                        }
                        catch (Exception ex) {
                            this.zzarc.zzgo().zzjd().zzg("Throwable caught in onActivityCreated", ex);
                        }
                        break;
                    }
                    final int n = 1;
                    continue;
                }
                final int n = 0;
                continue;
            }
        }
        Label_0275: {
            this.zzarc.zzgh().onActivityCreated(activity, bundle);
        }
    }
    
    public final void onActivityDestroyed(final Activity activity) {
        this.zzarc.zzgh().onActivityDestroyed(activity);
    }
    
    public final void onActivityPaused(final Activity activity) {
        this.zzarc.zzgh().onActivityPaused(activity);
        final zzeq zzgj = this.zzarc.zzgj();
        zzgj.zzgn().zzc(new zzeu(zzgj, zzgj.zzbx().elapsedRealtime()));
    }
    
    public final void onActivityResumed(final Activity activity) {
        this.zzarc.zzgh().onActivityResumed(activity);
        final zzeq zzgj = this.zzarc.zzgj();
        zzgj.zzgn().zzc(new zzet(zzgj, zzgj.zzbx().elapsedRealtime()));
    }
    
    public final void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
        this.zzarc.zzgh().onActivitySaveInstanceState(activity, bundle);
    }
    
    public final void onActivityStarted(final Activity activity) {
    }
    
    public final void onActivityStopped(final Activity activity) {
    }
}
