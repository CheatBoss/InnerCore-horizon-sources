package com.google.android.gms.measurement.internal;

import android.content.*;
import com.google.android.gms.common.util.*;

public final class zzaj extends zzf
{
    private String zzafx;
    private String zzage;
    private long zzagh;
    private String zzagk;
    private int zzagy;
    private int zzalo;
    private long zzalp;
    private String zztr;
    private String zzts;
    private String zztt;
    
    zzaj(final zzbt zzbt) {
        super(zzbt);
    }
    
    private final String zziz() {
        try {
            final Class<?> loadClass = this.getContext().getClassLoader().loadClass("com.google.firebase.analytics.FirebaseAnalytics");
            if (loadClass == null) {
                return null;
            }
            try {
                final Object invoke = loadClass.getDeclaredMethod("getInstance", Context.class).invoke(null, this.getContext());
                if (invoke == null) {
                    return null;
                }
                zzar zzar;
                String s;
                try {
                    return (String)loadClass.getDeclaredMethod("getFirebaseInstanceId", (Class<?>[])new Class[0]).invoke(invoke, new Object[0]);
                }
                catch (Exception ex) {
                    zzar = this.zzgo().zzji();
                    s = "Failed to retrieve Firebase Instance Id";
                }
                zzar.zzbx(s);
                return null;
            }
            catch (Exception ex2) {
                final zzar zzar = this.zzgo().zzjh();
                final String s = "Failed to obtain Firebase Analytics instance";
            }
        }
        catch (ClassNotFoundException ex3) {
            return null;
        }
    }
    
    final String getGmpAppId() {
        this.zzcl();
        return this.zzafx;
    }
    
    final String zzal() {
        this.zzcl();
        return this.zztt;
    }
    
    final zzh zzbr(final String s) {
        this.zzaf();
        this.zzgb();
        final String zzal = this.zzal();
        final String gmpAppId = this.getGmpAppId();
        this.zzcl();
        final String zzts = this.zzts;
        final long n = this.zzja();
        this.zzcl();
        final String zzage = this.zzage;
        final long zzhc = this.zzgq().zzhc();
        this.zzcl();
        this.zzaf();
        if (this.zzalp == 0L) {
            this.zzalp = this.zzadj.zzgm().zzd(this.getContext(), this.getContext().getPackageName());
        }
        final long zzalp = this.zzalp;
        final boolean enabled = this.zzadj.isEnabled();
        final boolean zzanv = this.zzgp().zzanv;
        this.zzaf();
        this.zzgb();
        String zziz;
        if (this.zzgq().zzbc(this.zztt) && !this.zzadj.isEnabled()) {
            zziz = null;
        }
        else {
            zziz = this.zziz();
        }
        this.zzcl();
        final long zzagh = this.zzagh;
        final long zzkp = this.zzadj.zzkp();
        final int zzjb = this.zzjb();
        final zzn zzgq = this.zzgq();
        zzgq.zzgb();
        final Boolean zzau = zzgq.zzau("google_analytics_adid_collection_enabled");
        final boolean booleanValue = (boolean)(zzau == null || zzau);
        final zzn zzgq2 = this.zzgq();
        zzgq2.zzgb();
        final Boolean zzau2 = zzgq2.zzau("google_analytics_ssaid_collection_enabled");
        return new zzh(zzal, gmpAppId, zzts, n, zzage, zzhc, zzalp, s, enabled, zzanv ^ true, zziz, zzagh, zzkp, zzjb, booleanValue, (boolean)(zzau2 == null || zzau2), this.zzgp().zzjx(), this.zzgw());
    }
    
    @Override
    protected final boolean zzgt() {
        return true;
    }
    
    @Override
    protected final void zzgu() {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    final String zzgw() {
        this.zzcl();
        return this.zzagk;
    }
    
    final int zzja() {
        this.zzcl();
        return this.zzalo;
    }
    
    final int zzjb() {
        this.zzcl();
        return this.zzagy;
    }
}
