package com.google.android.gms.measurement.internal;

final class zzaq implements Runnable
{
    private final /* synthetic */ int zzamh;
    private final /* synthetic */ String zzami;
    private final /* synthetic */ Object zzamj;
    private final /* synthetic */ Object zzamk;
    private final /* synthetic */ Object zzaml;
    private final /* synthetic */ zzap zzamm;
    
    zzaq(final zzap zzamm, final int zzamh, final String zzami, final Object zzamj, final Object zzamk, final Object zzaml) {
        this.zzamm = zzamm;
        this.zzamh = zzamh;
        this.zzami = zzami;
        this.zzamj = zzamj;
        this.zzamk = zzamk;
        this.zzaml = zzaml;
    }
    
    @Override
    public final void run() {
        final zzba zzgp = this.zzamm.zzadj.zzgp();
        if (!zzgp.isInitialized()) {
            this.zzamm.zza(6, "Persisted config not initialized. Not logging error/warn");
            return;
        }
        if (this.zzamm.zzalw == '\0') {
            zzap zzap;
            char c;
            if (this.zzamm.zzgq().zzdw()) {
                zzap = this.zzamm;
                zzap.zzgr();
                c = 'C';
            }
            else {
                zzap = this.zzamm;
                zzap.zzgr();
                c = 'c';
            }
            zzap.zzalw = c;
        }
        if (this.zzamm.zzadt < 0L) {
            final zzap zzamm = this.zzamm;
            zzamm.zzadt = zzamm.zzgq().zzhc();
        }
        final char char1 = "01VDIWEA?".charAt(this.zzamh);
        final char zza = this.zzamm.zzalw;
        final long zzb = this.zzamm.zzadt;
        final String zza2 = zzap.zza(true, this.zzami, this.zzamj, this.zzamk, this.zzaml);
        final StringBuilder sb = new StringBuilder(String.valueOf(zza2).length() + 24);
        sb.append("2");
        sb.append(char1);
        sb.append(zza);
        sb.append(zzb);
        sb.append(":");
        sb.append(zza2);
        String s;
        if ((s = sb.toString()).length() > 1024) {
            s = this.zzami.substring(0, 1024);
        }
        zzgp.zzand.zzc(s, 1L);
    }
}
