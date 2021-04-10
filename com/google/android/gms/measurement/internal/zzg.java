package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.text.*;

final class zzg
{
    private final zzbt zzadj;
    private long zzadt;
    private String zzafw;
    private String zzafx;
    private String zzafy;
    private String zzafz;
    private long zzaga;
    private long zzagb;
    private long zzagc;
    private long zzagd;
    private String zzage;
    private long zzagf;
    private boolean zzagg;
    private long zzagh;
    private boolean zzagi;
    private boolean zzagj;
    private String zzagk;
    private long zzagl;
    private long zzagm;
    private long zzagn;
    private long zzago;
    private long zzagp;
    private long zzagq;
    private String zzagr;
    private boolean zzags;
    private long zzagt;
    private long zzagu;
    private String zzts;
    private final String zztt;
    
    zzg(final zzbt zzadj, final String zztt) {
        Preconditions.checkNotNull(zzadj);
        Preconditions.checkNotEmpty(zztt);
        this.zzadj = zzadj;
        this.zztt = zztt;
        zzadj.zzgn().zzaf();
    }
    
    public final String getAppInstanceId() {
        this.zzadj.zzgn().zzaf();
        return this.zzafw;
    }
    
    public final String getFirebaseInstanceId() {
        this.zzadj.zzgn().zzaf();
        return this.zzafz;
    }
    
    public final String getGmpAppId() {
        this.zzadj.zzgn().zzaf();
        return this.zzafx;
    }
    
    public final boolean isMeasurementEnabled() {
        this.zzadj.zzgn().zzaf();
        return this.zzagg;
    }
    
    public final void setAppVersion(final String zzts) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (zzfk.zzu(this.zzts, zzts) ^ true);
        this.zzts = zzts;
    }
    
    public final void setMeasurementEnabled(final boolean zzagg) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagg != zzagg);
        this.zzagg = zzagg;
    }
    
    public final void zzaa(final long zzagl) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagl != zzagl);
        this.zzagl = zzagl;
    }
    
    public final void zzab(final long zzagm) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagm != zzagm);
        this.zzagm = zzagm;
    }
    
    public final void zzac(final long zzagn) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagn != zzagn);
        this.zzagn = zzagn;
    }
    
    public final void zzad(final long zzago) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzago != zzago);
        this.zzago = zzago;
    }
    
    public final void zzae(final long zzagq) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagq != zzagq);
        this.zzagq = zzagq;
    }
    
    public final void zzaf(final long zzagp) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagp != zzagp);
        this.zzagp = zzagp;
    }
    
    public final void zzag(final long zzagh) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagh != zzagh);
        this.zzagh = zzagh;
    }
    
    public final String zzak() {
        this.zzadj.zzgn().zzaf();
        return this.zzts;
    }
    
    public final String zzal() {
        this.zzadj.zzgn().zzaf();
        return this.zztt;
    }
    
    public final void zzam(final String zzafw) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (zzfk.zzu(this.zzafw, zzafw) ^ true);
        this.zzafw = zzafw;
    }
    
    public final void zzan(final String s) {
        this.zzadj.zzgn().zzaf();
        String zzafx = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            zzafx = null;
        }
        this.zzags |= (zzfk.zzu(this.zzafx, zzafx) ^ true);
        this.zzafx = zzafx;
    }
    
    public final void zzao(final String s) {
        this.zzadj.zzgn().zzaf();
        String zzagk = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            zzagk = null;
        }
        this.zzags |= (zzfk.zzu(this.zzagk, zzagk) ^ true);
        this.zzagk = zzagk;
    }
    
    public final void zzap(final String zzafy) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (zzfk.zzu(this.zzafy, zzafy) ^ true);
        this.zzafy = zzafy;
    }
    
    public final void zzaq(final String zzafz) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (zzfk.zzu(this.zzafz, zzafz) ^ true);
        this.zzafz = zzafz;
    }
    
    public final void zzar(final String zzage) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (zzfk.zzu(this.zzage, zzage) ^ true);
        this.zzage = zzage;
    }
    
    public final void zzas(final String zzagr) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (zzfk.zzu(this.zzagr, zzagr) ^ true);
        this.zzagr = zzagr;
    }
    
    public final void zze(final boolean zzagi) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagi != zzagi);
        this.zzagi = zzagi;
    }
    
    public final void zzf(final boolean zzagj) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagj != zzagj);
        this.zzagj = zzagj;
    }
    
    public final void zzgv() {
        this.zzadj.zzgn().zzaf();
        this.zzags = false;
    }
    
    public final String zzgw() {
        this.zzadj.zzgn().zzaf();
        return this.zzagk;
    }
    
    public final String zzgx() {
        this.zzadj.zzgn().zzaf();
        return this.zzafy;
    }
    
    public final long zzgy() {
        this.zzadj.zzgn().zzaf();
        return this.zzagb;
    }
    
    public final long zzgz() {
        this.zzadj.zzgn().zzaf();
        return this.zzagc;
    }
    
    public final long zzha() {
        this.zzadj.zzgn().zzaf();
        return this.zzagd;
    }
    
    public final String zzhb() {
        this.zzadj.zzgn().zzaf();
        return this.zzage;
    }
    
    public final long zzhc() {
        this.zzadj.zzgn().zzaf();
        return this.zzadt;
    }
    
    public final long zzhd() {
        this.zzadj.zzgn().zzaf();
        return this.zzagf;
    }
    
    public final long zzhe() {
        this.zzadj.zzgn().zzaf();
        return this.zzaga;
    }
    
    public final long zzhf() {
        this.zzadj.zzgn().zzaf();
        return this.zzagt;
    }
    
    public final long zzhg() {
        this.zzadj.zzgn().zzaf();
        return this.zzagu;
    }
    
    public final void zzhh() {
        this.zzadj.zzgn().zzaf();
        long zzaga;
        if ((zzaga = this.zzaga + 1L) > 2147483647L) {
            this.zzadj.zzgo().zzjg().zzg("Bundle index overflow. appId", zzap.zzbv(this.zztt));
            zzaga = 0L;
        }
        this.zzags = true;
        this.zzaga = zzaga;
    }
    
    public final long zzhi() {
        this.zzadj.zzgn().zzaf();
        return this.zzagl;
    }
    
    public final long zzhj() {
        this.zzadj.zzgn().zzaf();
        return this.zzagm;
    }
    
    public final long zzhk() {
        this.zzadj.zzgn().zzaf();
        return this.zzagn;
    }
    
    public final long zzhl() {
        this.zzadj.zzgn().zzaf();
        return this.zzago;
    }
    
    public final long zzhm() {
        this.zzadj.zzgn().zzaf();
        return this.zzagq;
    }
    
    public final long zzhn() {
        this.zzadj.zzgn().zzaf();
        return this.zzagp;
    }
    
    public final String zzho() {
        this.zzadj.zzgn().zzaf();
        return this.zzagr;
    }
    
    public final String zzhp() {
        this.zzadj.zzgn().zzaf();
        final String zzagr = this.zzagr;
        this.zzas(null);
        return zzagr;
    }
    
    public final long zzhq() {
        this.zzadj.zzgn().zzaf();
        return this.zzagh;
    }
    
    public final boolean zzhr() {
        this.zzadj.zzgn().zzaf();
        return this.zzagi;
    }
    
    public final boolean zzhs() {
        this.zzadj.zzgn().zzaf();
        return this.zzagj;
    }
    
    public final void zzs(final long zzagb) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagb != zzagb);
        this.zzagb = zzagb;
    }
    
    public final void zzt(final long zzagc) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagc != zzagc);
        this.zzagc = zzagc;
    }
    
    public final void zzu(final long zzagd) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagd != zzagd);
        this.zzagd = zzagd;
    }
    
    public final void zzv(final long zzadt) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzadt != zzadt);
        this.zzadt = zzadt;
    }
    
    public final void zzw(final long zzagf) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagf != zzagf);
        this.zzagf = zzagf;
    }
    
    public final void zzx(final long zzaga) {
        boolean b = false;
        Preconditions.checkArgument(zzaga >= 0L);
        this.zzadj.zzgn().zzaf();
        final boolean zzags = this.zzags;
        if (this.zzaga != zzaga) {
            b = true;
        }
        this.zzags = (b | zzags);
        this.zzaga = zzaga;
    }
    
    public final void zzy(final long zzagt) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagt != zzagt);
        this.zzagt = zzagt;
    }
    
    public final void zzz(final long zzagu) {
        this.zzadj.zzgn().zzaf();
        this.zzags |= (this.zzagu != zzagu);
        this.zzagu = zzagu;
    }
}
