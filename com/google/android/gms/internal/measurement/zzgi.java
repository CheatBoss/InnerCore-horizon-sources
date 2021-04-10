package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzgi extends zzza<zzgi>
{
    private static volatile zzgi[] zzawz;
    public String zzafw;
    public String zzafx;
    public String zzafz;
    public String zzage;
    public String zzagv;
    public String zzaia;
    public String zzawj;
    public Integer zzaxa;
    public zzgf[] zzaxb;
    public zzgl[] zzaxc;
    public Long zzaxd;
    public Long zzaxe;
    public Long zzaxf;
    public Long zzaxg;
    public Long zzaxh;
    public String zzaxi;
    public String zzaxj;
    public String zzaxk;
    public Integer zzaxl;
    public Long zzaxm;
    public Long zzaxn;
    public String zzaxo;
    public Boolean zzaxp;
    public Long zzaxq;
    public Integer zzaxr;
    public Boolean zzaxs;
    public zzgd[] zzaxt;
    public Integer zzaxu;
    private Integer zzaxv;
    private Integer zzaxw;
    public String zzaxx;
    public Long zzaxy;
    public Long zzaxz;
    public String zzaya;
    private String zzayb;
    public Integer zzayc;
    private zzfq.zzb zzayd;
    public String zzts;
    public String zztt;
    
    public zzgi() {
        this.zzaxa = null;
        this.zzaxb = zzgf.zzmq();
        this.zzaxc = zzgl.zzmu();
        this.zzaxd = null;
        this.zzaxe = null;
        this.zzaxf = null;
        this.zzaxg = null;
        this.zzaxh = null;
        this.zzaxi = null;
        this.zzaxj = null;
        this.zzaxk = null;
        this.zzaia = null;
        this.zzaxl = null;
        this.zzage = null;
        this.zztt = null;
        this.zzts = null;
        this.zzaxm = null;
        this.zzaxn = null;
        this.zzaxo = null;
        this.zzaxp = null;
        this.zzafw = null;
        this.zzaxq = null;
        this.zzaxr = null;
        this.zzagv = null;
        this.zzafx = null;
        this.zzaxs = null;
        this.zzaxt = zzgd.zzmo();
        this.zzafz = null;
        this.zzaxu = null;
        this.zzaxv = null;
        this.zzaxw = null;
        this.zzaxx = null;
        this.zzaxy = null;
        this.zzaxz = null;
        this.zzaya = null;
        this.zzayb = null;
        this.zzayc = null;
        this.zzawj = null;
        this.zzayd = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }
    
    public static zzgi[] zzms() {
        if (zzgi.zzawz == null) {
            synchronized (zzze.zzcfl) {
                if (zzgi.zzawz == null) {
                    zzgi.zzawz = new zzgi[0];
                }
            }
        }
        return zzgi.zzawz;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzgi)) {
            return false;
        }
        final zzgi zzgi = (zzgi)o;
        final Integer zzaxa = this.zzaxa;
        if (zzaxa == null) {
            if (zzgi.zzaxa != null) {
                return false;
            }
        }
        else if (!zzaxa.equals(zzgi.zzaxa)) {
            return false;
        }
        if (!zzze.equals(this.zzaxb, zzgi.zzaxb)) {
            return false;
        }
        if (!zzze.equals(this.zzaxc, zzgi.zzaxc)) {
            return false;
        }
        final Long zzaxd = this.zzaxd;
        if (zzaxd == null) {
            if (zzgi.zzaxd != null) {
                return false;
            }
        }
        else if (!zzaxd.equals(zzgi.zzaxd)) {
            return false;
        }
        final Long zzaxe = this.zzaxe;
        if (zzaxe == null) {
            if (zzgi.zzaxe != null) {
                return false;
            }
        }
        else if (!zzaxe.equals(zzgi.zzaxe)) {
            return false;
        }
        final Long zzaxf = this.zzaxf;
        if (zzaxf == null) {
            if (zzgi.zzaxf != null) {
                return false;
            }
        }
        else if (!zzaxf.equals(zzgi.zzaxf)) {
            return false;
        }
        final Long zzaxg = this.zzaxg;
        if (zzaxg == null) {
            if (zzgi.zzaxg != null) {
                return false;
            }
        }
        else if (!zzaxg.equals(zzgi.zzaxg)) {
            return false;
        }
        final Long zzaxh = this.zzaxh;
        if (zzaxh == null) {
            if (zzgi.zzaxh != null) {
                return false;
            }
        }
        else if (!zzaxh.equals(zzgi.zzaxh)) {
            return false;
        }
        final String zzaxi = this.zzaxi;
        if (zzaxi == null) {
            if (zzgi.zzaxi != null) {
                return false;
            }
        }
        else if (!zzaxi.equals(zzgi.zzaxi)) {
            return false;
        }
        final String zzaxj = this.zzaxj;
        if (zzaxj == null) {
            if (zzgi.zzaxj != null) {
                return false;
            }
        }
        else if (!zzaxj.equals(zzgi.zzaxj)) {
            return false;
        }
        final String zzaxk = this.zzaxk;
        if (zzaxk == null) {
            if (zzgi.zzaxk != null) {
                return false;
            }
        }
        else if (!zzaxk.equals(zzgi.zzaxk)) {
            return false;
        }
        final String zzaia = this.zzaia;
        if (zzaia == null) {
            if (zzgi.zzaia != null) {
                return false;
            }
        }
        else if (!zzaia.equals(zzgi.zzaia)) {
            return false;
        }
        final Integer zzaxl = this.zzaxl;
        if (zzaxl == null) {
            if (zzgi.zzaxl != null) {
                return false;
            }
        }
        else if (!zzaxl.equals(zzgi.zzaxl)) {
            return false;
        }
        final String zzage = this.zzage;
        if (zzage == null) {
            if (zzgi.zzage != null) {
                return false;
            }
        }
        else if (!zzage.equals(zzgi.zzage)) {
            return false;
        }
        final String zztt = this.zztt;
        if (zztt == null) {
            if (zzgi.zztt != null) {
                return false;
            }
        }
        else if (!zztt.equals(zzgi.zztt)) {
            return false;
        }
        final String zzts = this.zzts;
        if (zzts == null) {
            if (zzgi.zzts != null) {
                return false;
            }
        }
        else if (!zzts.equals(zzgi.zzts)) {
            return false;
        }
        final Long zzaxm = this.zzaxm;
        if (zzaxm == null) {
            if (zzgi.zzaxm != null) {
                return false;
            }
        }
        else if (!zzaxm.equals(zzgi.zzaxm)) {
            return false;
        }
        final Long zzaxn = this.zzaxn;
        if (zzaxn == null) {
            if (zzgi.zzaxn != null) {
                return false;
            }
        }
        else if (!zzaxn.equals(zzgi.zzaxn)) {
            return false;
        }
        final String zzaxo = this.zzaxo;
        if (zzaxo == null) {
            if (zzgi.zzaxo != null) {
                return false;
            }
        }
        else if (!zzaxo.equals(zzgi.zzaxo)) {
            return false;
        }
        final Boolean zzaxp = this.zzaxp;
        if (zzaxp == null) {
            if (zzgi.zzaxp != null) {
                return false;
            }
        }
        else if (!zzaxp.equals(zzgi.zzaxp)) {
            return false;
        }
        final String zzafw = this.zzafw;
        if (zzafw == null) {
            if (zzgi.zzafw != null) {
                return false;
            }
        }
        else if (!zzafw.equals(zzgi.zzafw)) {
            return false;
        }
        final Long zzaxq = this.zzaxq;
        if (zzaxq == null) {
            if (zzgi.zzaxq != null) {
                return false;
            }
        }
        else if (!zzaxq.equals(zzgi.zzaxq)) {
            return false;
        }
        final Integer zzaxr = this.zzaxr;
        if (zzaxr == null) {
            if (zzgi.zzaxr != null) {
                return false;
            }
        }
        else if (!zzaxr.equals(zzgi.zzaxr)) {
            return false;
        }
        final String zzagv = this.zzagv;
        if (zzagv == null) {
            if (zzgi.zzagv != null) {
                return false;
            }
        }
        else if (!zzagv.equals(zzgi.zzagv)) {
            return false;
        }
        final String zzafx = this.zzafx;
        if (zzafx == null) {
            if (zzgi.zzafx != null) {
                return false;
            }
        }
        else if (!zzafx.equals(zzgi.zzafx)) {
            return false;
        }
        final Boolean zzaxs = this.zzaxs;
        if (zzaxs == null) {
            if (zzgi.zzaxs != null) {
                return false;
            }
        }
        else if (!zzaxs.equals(zzgi.zzaxs)) {
            return false;
        }
        if (!zzze.equals(this.zzaxt, zzgi.zzaxt)) {
            return false;
        }
        final String zzafz = this.zzafz;
        if (zzafz == null) {
            if (zzgi.zzafz != null) {
                return false;
            }
        }
        else if (!zzafz.equals(zzgi.zzafz)) {
            return false;
        }
        final Integer zzaxu = this.zzaxu;
        if (zzaxu == null) {
            if (zzgi.zzaxu != null) {
                return false;
            }
        }
        else if (!zzaxu.equals(zzgi.zzaxu)) {
            return false;
        }
        final Integer zzaxv = this.zzaxv;
        if (zzaxv == null) {
            if (zzgi.zzaxv != null) {
                return false;
            }
        }
        else if (!zzaxv.equals(zzgi.zzaxv)) {
            return false;
        }
        final Integer zzaxw = this.zzaxw;
        if (zzaxw == null) {
            if (zzgi.zzaxw != null) {
                return false;
            }
        }
        else if (!zzaxw.equals(zzgi.zzaxw)) {
            return false;
        }
        final String zzaxx = this.zzaxx;
        if (zzaxx == null) {
            if (zzgi.zzaxx != null) {
                return false;
            }
        }
        else if (!zzaxx.equals(zzgi.zzaxx)) {
            return false;
        }
        final Long zzaxy = this.zzaxy;
        if (zzaxy == null) {
            if (zzgi.zzaxy != null) {
                return false;
            }
        }
        else if (!zzaxy.equals(zzgi.zzaxy)) {
            return false;
        }
        final Long zzaxz = this.zzaxz;
        if (zzaxz == null) {
            if (zzgi.zzaxz != null) {
                return false;
            }
        }
        else if (!zzaxz.equals(zzgi.zzaxz)) {
            return false;
        }
        final String zzaya = this.zzaya;
        if (zzaya == null) {
            if (zzgi.zzaya != null) {
                return false;
            }
        }
        else if (!zzaya.equals(zzgi.zzaya)) {
            return false;
        }
        final String zzayb = this.zzayb;
        if (zzayb == null) {
            if (zzgi.zzayb != null) {
                return false;
            }
        }
        else if (!zzayb.equals(zzgi.zzayb)) {
            return false;
        }
        final Integer zzayc = this.zzayc;
        if (zzayc == null) {
            if (zzgi.zzayc != null) {
                return false;
            }
        }
        else if (!zzayc.equals(zzgi.zzayc)) {
            return false;
        }
        final String zzawj = this.zzawj;
        if (zzawj == null) {
            if (zzgi.zzawj != null) {
                return false;
            }
        }
        else if (!zzawj.equals(zzgi.zzawj)) {
            return false;
        }
        final zzfq.zzb zzayd = this.zzayd;
        if (zzayd == null) {
            if (zzgi.zzayd != null) {
                return false;
            }
        }
        else if (!zzayd.equals(zzgi.zzayd)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(zzgi.zzcfc);
        }
        return zzgi.zzcfc == null || zzgi.zzcfc.isEmpty();
    }
    
    @Override
    public final int hashCode() {
        final int hashCode = this.getClass().getName().hashCode();
        final Integer zzaxa = this.zzaxa;
        int hashCode2;
        if (zzaxa == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzaxa.hashCode();
        }
        final int hashCode3 = zzze.hashCode(this.zzaxb);
        final int hashCode4 = zzze.hashCode(this.zzaxc);
        final Long zzaxd = this.zzaxd;
        int hashCode5;
        if (zzaxd == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = zzaxd.hashCode();
        }
        final Long zzaxe = this.zzaxe;
        int hashCode6;
        if (zzaxe == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = zzaxe.hashCode();
        }
        final Long zzaxf = this.zzaxf;
        int hashCode7;
        if (zzaxf == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = zzaxf.hashCode();
        }
        final Long zzaxg = this.zzaxg;
        int hashCode8;
        if (zzaxg == null) {
            hashCode8 = 0;
        }
        else {
            hashCode8 = zzaxg.hashCode();
        }
        final Long zzaxh = this.zzaxh;
        int hashCode9;
        if (zzaxh == null) {
            hashCode9 = 0;
        }
        else {
            hashCode9 = zzaxh.hashCode();
        }
        final String zzaxi = this.zzaxi;
        int hashCode10;
        if (zzaxi == null) {
            hashCode10 = 0;
        }
        else {
            hashCode10 = zzaxi.hashCode();
        }
        final String zzaxj = this.zzaxj;
        int hashCode11;
        if (zzaxj == null) {
            hashCode11 = 0;
        }
        else {
            hashCode11 = zzaxj.hashCode();
        }
        final String zzaxk = this.zzaxk;
        int hashCode12;
        if (zzaxk == null) {
            hashCode12 = 0;
        }
        else {
            hashCode12 = zzaxk.hashCode();
        }
        final String zzaia = this.zzaia;
        int hashCode13;
        if (zzaia == null) {
            hashCode13 = 0;
        }
        else {
            hashCode13 = zzaia.hashCode();
        }
        final Integer zzaxl = this.zzaxl;
        int hashCode14;
        if (zzaxl == null) {
            hashCode14 = 0;
        }
        else {
            hashCode14 = zzaxl.hashCode();
        }
        final String zzage = this.zzage;
        int hashCode15;
        if (zzage == null) {
            hashCode15 = 0;
        }
        else {
            hashCode15 = zzage.hashCode();
        }
        final String zztt = this.zztt;
        int hashCode16;
        if (zztt == null) {
            hashCode16 = 0;
        }
        else {
            hashCode16 = zztt.hashCode();
        }
        final String zzts = this.zzts;
        int hashCode17;
        if (zzts == null) {
            hashCode17 = 0;
        }
        else {
            hashCode17 = zzts.hashCode();
        }
        final Long zzaxm = this.zzaxm;
        int hashCode18;
        if (zzaxm == null) {
            hashCode18 = 0;
        }
        else {
            hashCode18 = zzaxm.hashCode();
        }
        final Long zzaxn = this.zzaxn;
        int hashCode19;
        if (zzaxn == null) {
            hashCode19 = 0;
        }
        else {
            hashCode19 = zzaxn.hashCode();
        }
        final String zzaxo = this.zzaxo;
        int hashCode20;
        if (zzaxo == null) {
            hashCode20 = 0;
        }
        else {
            hashCode20 = zzaxo.hashCode();
        }
        final Boolean zzaxp = this.zzaxp;
        int hashCode21;
        if (zzaxp == null) {
            hashCode21 = 0;
        }
        else {
            hashCode21 = zzaxp.hashCode();
        }
        final String zzafw = this.zzafw;
        int hashCode22;
        if (zzafw == null) {
            hashCode22 = 0;
        }
        else {
            hashCode22 = zzafw.hashCode();
        }
        final Long zzaxq = this.zzaxq;
        int hashCode23;
        if (zzaxq == null) {
            hashCode23 = 0;
        }
        else {
            hashCode23 = zzaxq.hashCode();
        }
        final Integer zzaxr = this.zzaxr;
        int hashCode24;
        if (zzaxr == null) {
            hashCode24 = 0;
        }
        else {
            hashCode24 = zzaxr.hashCode();
        }
        final String zzagv = this.zzagv;
        int hashCode25;
        if (zzagv == null) {
            hashCode25 = 0;
        }
        else {
            hashCode25 = zzagv.hashCode();
        }
        final String zzafx = this.zzafx;
        int hashCode26;
        if (zzafx == null) {
            hashCode26 = 0;
        }
        else {
            hashCode26 = zzafx.hashCode();
        }
        final Boolean zzaxs = this.zzaxs;
        int hashCode27;
        if (zzaxs == null) {
            hashCode27 = 0;
        }
        else {
            hashCode27 = zzaxs.hashCode();
        }
        final int hashCode28 = zzze.hashCode(this.zzaxt);
        final String zzafz = this.zzafz;
        int hashCode29;
        if (zzafz == null) {
            hashCode29 = 0;
        }
        else {
            hashCode29 = zzafz.hashCode();
        }
        final Integer zzaxu = this.zzaxu;
        int hashCode30;
        if (zzaxu == null) {
            hashCode30 = 0;
        }
        else {
            hashCode30 = zzaxu.hashCode();
        }
        final Integer zzaxv = this.zzaxv;
        int hashCode31;
        if (zzaxv == null) {
            hashCode31 = 0;
        }
        else {
            hashCode31 = zzaxv.hashCode();
        }
        final Integer zzaxw = this.zzaxw;
        int hashCode32;
        if (zzaxw == null) {
            hashCode32 = 0;
        }
        else {
            hashCode32 = zzaxw.hashCode();
        }
        final String zzaxx = this.zzaxx;
        int hashCode33;
        if (zzaxx == null) {
            hashCode33 = 0;
        }
        else {
            hashCode33 = zzaxx.hashCode();
        }
        final Long zzaxy = this.zzaxy;
        int hashCode34;
        if (zzaxy == null) {
            hashCode34 = 0;
        }
        else {
            hashCode34 = zzaxy.hashCode();
        }
        final Long zzaxz = this.zzaxz;
        int hashCode35;
        if (zzaxz == null) {
            hashCode35 = 0;
        }
        else {
            hashCode35 = zzaxz.hashCode();
        }
        final String zzaya = this.zzaya;
        int hashCode36;
        if (zzaya == null) {
            hashCode36 = 0;
        }
        else {
            hashCode36 = zzaya.hashCode();
        }
        final String zzayb = this.zzayb;
        int hashCode37;
        if (zzayb == null) {
            hashCode37 = 0;
        }
        else {
            hashCode37 = zzayb.hashCode();
        }
        final Integer zzayc = this.zzayc;
        int hashCode38;
        if (zzayc == null) {
            hashCode38 = 0;
        }
        else {
            hashCode38 = zzayc.hashCode();
        }
        final String zzawj = this.zzawj;
        int hashCode39;
        if (zzawj == null) {
            hashCode39 = 0;
        }
        else {
            hashCode39 = zzawj.hashCode();
        }
        final zzfq.zzb zzayd = this.zzayd;
        int hashCode40;
        if (zzayd == null) {
            hashCode40 = 0;
        }
        else {
            hashCode40 = zzayd.hashCode();
        }
        int hashCode41;
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            hashCode41 = this.zzcfc.hashCode();
        }
        else {
            hashCode41 = 0;
        }
        return ((((((((((((((((((((((((((((((((((((((((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode9) * 31 + hashCode10) * 31 + hashCode11) * 31 + hashCode12) * 31 + hashCode13) * 31 + hashCode14) * 31 + hashCode15) * 31 + hashCode16) * 31 + hashCode17) * 31 + hashCode18) * 31 + hashCode19) * 31 + hashCode20) * 31 + hashCode21) * 31 + hashCode22) * 31 + hashCode23) * 31 + hashCode24) * 31 + hashCode25) * 31 + hashCode26) * 31 + hashCode27) * 31 + hashCode28) * 31 + hashCode29) * 31 + hashCode30) * 31 + hashCode31) * 31 + hashCode32) * 31 + hashCode33) * 31 + hashCode34) * 31 + hashCode35) * 31 + hashCode36) * 31 + hashCode37) * 31 + hashCode38) * 31 + hashCode39) * 31 + hashCode40) * 31 + hashCode41;
    }
    
    @Override
    public final void zza(final zzyy zzyy) throws IOException {
        final Integer zzaxa = this.zzaxa;
        if (zzaxa != null) {
            zzyy.zzd(1, zzaxa);
        }
        final zzgf[] zzaxb = this.zzaxb;
        final int n = 0;
        if (zzaxb != null && zzaxb.length > 0) {
            int n2 = 0;
            while (true) {
                final zzgf[] zzaxb2 = this.zzaxb;
                if (n2 >= zzaxb2.length) {
                    break;
                }
                final zzgf zzgf = zzaxb2[n2];
                if (zzgf != null) {
                    zzyy.zza(2, zzgf);
                }
                ++n2;
            }
        }
        final zzgl[] zzaxc = this.zzaxc;
        if (zzaxc != null && zzaxc.length > 0) {
            int n3 = 0;
            while (true) {
                final zzgl[] zzaxc2 = this.zzaxc;
                if (n3 >= zzaxc2.length) {
                    break;
                }
                final zzgl zzgl = zzaxc2[n3];
                if (zzgl != null) {
                    zzyy.zza(3, zzgl);
                }
                ++n3;
            }
        }
        final Long zzaxd = this.zzaxd;
        if (zzaxd != null) {
            zzyy.zzi(4, zzaxd);
        }
        final Long zzaxe = this.zzaxe;
        if (zzaxe != null) {
            zzyy.zzi(5, zzaxe);
        }
        final Long zzaxf = this.zzaxf;
        if (zzaxf != null) {
            zzyy.zzi(6, zzaxf);
        }
        final Long zzaxh = this.zzaxh;
        if (zzaxh != null) {
            zzyy.zzi(7, zzaxh);
        }
        final String zzaxi = this.zzaxi;
        if (zzaxi != null) {
            zzyy.zzb(8, zzaxi);
        }
        final String zzaxj = this.zzaxj;
        if (zzaxj != null) {
            zzyy.zzb(9, zzaxj);
        }
        final String zzaxk = this.zzaxk;
        if (zzaxk != null) {
            zzyy.zzb(10, zzaxk);
        }
        final String zzaia = this.zzaia;
        if (zzaia != null) {
            zzyy.zzb(11, zzaia);
        }
        final Integer zzaxl = this.zzaxl;
        if (zzaxl != null) {
            zzyy.zzd(12, zzaxl);
        }
        final String zzage = this.zzage;
        if (zzage != null) {
            zzyy.zzb(13, zzage);
        }
        final String zztt = this.zztt;
        if (zztt != null) {
            zzyy.zzb(14, zztt);
        }
        final String zzts = this.zzts;
        if (zzts != null) {
            zzyy.zzb(16, zzts);
        }
        final Long zzaxm = this.zzaxm;
        if (zzaxm != null) {
            zzyy.zzi(17, zzaxm);
        }
        final Long zzaxn = this.zzaxn;
        if (zzaxn != null) {
            zzyy.zzi(18, zzaxn);
        }
        final String zzaxo = this.zzaxo;
        if (zzaxo != null) {
            zzyy.zzb(19, zzaxo);
        }
        final Boolean zzaxp = this.zzaxp;
        if (zzaxp != null) {
            zzyy.zzb(20, zzaxp);
        }
        final String zzafw = this.zzafw;
        if (zzafw != null) {
            zzyy.zzb(21, zzafw);
        }
        final Long zzaxq = this.zzaxq;
        if (zzaxq != null) {
            zzyy.zzi(22, zzaxq);
        }
        final Integer zzaxr = this.zzaxr;
        if (zzaxr != null) {
            zzyy.zzd(23, zzaxr);
        }
        final String zzagv = this.zzagv;
        if (zzagv != null) {
            zzyy.zzb(24, zzagv);
        }
        final String zzafx = this.zzafx;
        if (zzafx != null) {
            zzyy.zzb(25, zzafx);
        }
        final Long zzaxg = this.zzaxg;
        if (zzaxg != null) {
            zzyy.zzi(26, zzaxg);
        }
        final Boolean zzaxs = this.zzaxs;
        if (zzaxs != null) {
            zzyy.zzb(28, zzaxs);
        }
        final zzgd[] zzaxt = this.zzaxt;
        if (zzaxt != null && zzaxt.length > 0) {
            int n4 = n;
            while (true) {
                final zzgd[] zzaxt2 = this.zzaxt;
                if (n4 >= zzaxt2.length) {
                    break;
                }
                final zzgd zzgd = zzaxt2[n4];
                if (zzgd != null) {
                    zzyy.zza(29, zzgd);
                }
                ++n4;
            }
        }
        final String zzafz = this.zzafz;
        if (zzafz != null) {
            zzyy.zzb(30, zzafz);
        }
        final Integer zzaxu = this.zzaxu;
        if (zzaxu != null) {
            zzyy.zzd(31, zzaxu);
        }
        final Integer zzaxv = this.zzaxv;
        if (zzaxv != null) {
            zzyy.zzd(32, zzaxv);
        }
        final Integer zzaxw = this.zzaxw;
        if (zzaxw != null) {
            zzyy.zzd(33, zzaxw);
        }
        final String zzaxx = this.zzaxx;
        if (zzaxx != null) {
            zzyy.zzb(34, zzaxx);
        }
        final Long zzaxy = this.zzaxy;
        if (zzaxy != null) {
            zzyy.zzi(35, zzaxy);
        }
        final Long zzaxz = this.zzaxz;
        if (zzaxz != null) {
            zzyy.zzi(36, zzaxz);
        }
        final String zzaya = this.zzaya;
        if (zzaya != null) {
            zzyy.zzb(37, zzaya);
        }
        final String zzayb = this.zzayb;
        if (zzayb != null) {
            zzyy.zzb(38, zzayb);
        }
        final Integer zzayc = this.zzayc;
        if (zzayc != null) {
            zzyy.zzd(39, zzayc);
        }
        final String zzawj = this.zzawj;
        if (zzawj != null) {
            zzyy.zzb(41, zzawj);
        }
        final zzfq.zzb zzayd = this.zzayd;
        if (zzayd != null) {
            zzyy.zze(44, zzayd);
        }
        super.zza(zzyy);
    }
    
    @Override
    protected final int zzf() {
        final int zzf = super.zzf();
        final Integer zzaxa = this.zzaxa;
        int n = zzf;
        if (zzaxa != null) {
            n = zzf + zzyy.zzh(1, zzaxa);
        }
        final zzgf[] zzaxb = this.zzaxb;
        final int n2 = 0;
        int n3 = n;
        if (zzaxb != null) {
            n3 = n;
            if (zzaxb.length > 0) {
                int n4 = 0;
                while (true) {
                    final zzgf[] zzaxb2 = this.zzaxb;
                    n3 = n;
                    if (n4 >= zzaxb2.length) {
                        break;
                    }
                    final zzgf zzgf = zzaxb2[n4];
                    int n5 = n;
                    if (zzgf != null) {
                        n5 = n + zzyy.zzb(2, zzgf);
                    }
                    ++n4;
                    n = n5;
                }
            }
        }
        final zzgl[] zzaxc = this.zzaxc;
        int n6 = n3;
        if (zzaxc != null) {
            n6 = n3;
            if (zzaxc.length > 0) {
                int n7 = 0;
                while (true) {
                    final zzgl[] zzaxc2 = this.zzaxc;
                    n6 = n3;
                    if (n7 >= zzaxc2.length) {
                        break;
                    }
                    final zzgl zzgl = zzaxc2[n7];
                    int n8 = n3;
                    if (zzgl != null) {
                        n8 = n3 + zzyy.zzb(3, zzgl);
                    }
                    ++n7;
                    n3 = n8;
                }
            }
        }
        final Long zzaxd = this.zzaxd;
        int n9 = n6;
        if (zzaxd != null) {
            n9 = n6 + zzyy.zzd(4, zzaxd);
        }
        final Long zzaxe = this.zzaxe;
        int n10 = n9;
        if (zzaxe != null) {
            n10 = n9 + zzyy.zzd(5, zzaxe);
        }
        final Long zzaxf = this.zzaxf;
        int n11 = n10;
        if (zzaxf != null) {
            n11 = n10 + zzyy.zzd(6, zzaxf);
        }
        final Long zzaxh = this.zzaxh;
        int n12 = n11;
        if (zzaxh != null) {
            n12 = n11 + zzyy.zzd(7, zzaxh);
        }
        final String zzaxi = this.zzaxi;
        int n13 = n12;
        if (zzaxi != null) {
            n13 = n12 + zzyy.zzc(8, zzaxi);
        }
        final String zzaxj = this.zzaxj;
        int n14 = n13;
        if (zzaxj != null) {
            n14 = n13 + zzyy.zzc(9, zzaxj);
        }
        final String zzaxk = this.zzaxk;
        int n15 = n14;
        if (zzaxk != null) {
            n15 = n14 + zzyy.zzc(10, zzaxk);
        }
        final String zzaia = this.zzaia;
        int n16 = n15;
        if (zzaia != null) {
            n16 = n15 + zzyy.zzc(11, zzaia);
        }
        final Integer zzaxl = this.zzaxl;
        int n17 = n16;
        if (zzaxl != null) {
            n17 = n16 + zzyy.zzh(12, zzaxl);
        }
        final String zzage = this.zzage;
        int n18 = n17;
        if (zzage != null) {
            n18 = n17 + zzyy.zzc(13, zzage);
        }
        final String zztt = this.zztt;
        int n19 = n18;
        if (zztt != null) {
            n19 = n18 + zzyy.zzc(14, zztt);
        }
        final String zzts = this.zzts;
        int n20 = n19;
        if (zzts != null) {
            n20 = n19 + zzyy.zzc(16, zzts);
        }
        final Long zzaxm = this.zzaxm;
        int n21 = n20;
        if (zzaxm != null) {
            n21 = n20 + zzyy.zzd(17, zzaxm);
        }
        final Long zzaxn = this.zzaxn;
        int n22 = n21;
        if (zzaxn != null) {
            n22 = n21 + zzyy.zzd(18, zzaxn);
        }
        final String zzaxo = this.zzaxo;
        int n23 = n22;
        if (zzaxo != null) {
            n23 = n22 + zzyy.zzc(19, zzaxo);
        }
        final Boolean zzaxp = this.zzaxp;
        int n24 = n23;
        if (zzaxp != null) {
            zzaxp;
            n24 = n23 + (zzyy.zzbb(20) + 1);
        }
        final String zzafw = this.zzafw;
        int n25 = n24;
        if (zzafw != null) {
            n25 = n24 + zzyy.zzc(21, zzafw);
        }
        final Long zzaxq = this.zzaxq;
        int n26 = n25;
        if (zzaxq != null) {
            n26 = n25 + zzyy.zzd(22, zzaxq);
        }
        final Integer zzaxr = this.zzaxr;
        int n27 = n26;
        if (zzaxr != null) {
            n27 = n26 + zzyy.zzh(23, zzaxr);
        }
        final String zzagv = this.zzagv;
        int n28 = n27;
        if (zzagv != null) {
            n28 = n27 + zzyy.zzc(24, zzagv);
        }
        final String zzafx = this.zzafx;
        int n29 = n28;
        if (zzafx != null) {
            n29 = n28 + zzyy.zzc(25, zzafx);
        }
        final Long zzaxg = this.zzaxg;
        int n30 = n29;
        if (zzaxg != null) {
            n30 = n29 + zzyy.zzd(26, zzaxg);
        }
        final Boolean zzaxs = this.zzaxs;
        int n31 = n30;
        if (zzaxs != null) {
            zzaxs;
            n31 = n30 + (zzyy.zzbb(28) + 1);
        }
        final zzgd[] zzaxt = this.zzaxt;
        int n32 = n31;
        if (zzaxt != null) {
            n32 = n31;
            if (zzaxt.length > 0) {
                int n33 = n2;
                while (true) {
                    final zzgd[] zzaxt2 = this.zzaxt;
                    n32 = n31;
                    if (n33 >= zzaxt2.length) {
                        break;
                    }
                    final zzgd zzgd = zzaxt2[n33];
                    int n34 = n31;
                    if (zzgd != null) {
                        n34 = n31 + zzyy.zzb(29, zzgd);
                    }
                    ++n33;
                    n31 = n34;
                }
            }
        }
        final String zzafz = this.zzafz;
        int n35 = n32;
        if (zzafz != null) {
            n35 = n32 + zzyy.zzc(30, zzafz);
        }
        final Integer zzaxu = this.zzaxu;
        int n36 = n35;
        if (zzaxu != null) {
            n36 = n35 + zzyy.zzh(31, zzaxu);
        }
        final Integer zzaxv = this.zzaxv;
        int n37 = n36;
        if (zzaxv != null) {
            n37 = n36 + zzyy.zzh(32, zzaxv);
        }
        final Integer zzaxw = this.zzaxw;
        int n38 = n37;
        if (zzaxw != null) {
            n38 = n37 + zzyy.zzh(33, zzaxw);
        }
        final String zzaxx = this.zzaxx;
        int n39 = n38;
        if (zzaxx != null) {
            n39 = n38 + zzyy.zzc(34, zzaxx);
        }
        final Long zzaxy = this.zzaxy;
        int n40 = n39;
        if (zzaxy != null) {
            n40 = n39 + zzyy.zzd(35, zzaxy);
        }
        final Long zzaxz = this.zzaxz;
        int n41 = n40;
        if (zzaxz != null) {
            n41 = n40 + zzyy.zzd(36, zzaxz);
        }
        final String zzaya = this.zzaya;
        int n42 = n41;
        if (zzaya != null) {
            n42 = n41 + zzyy.zzc(37, zzaya);
        }
        final String zzayb = this.zzayb;
        int n43 = n42;
        if (zzayb != null) {
            n43 = n42 + zzyy.zzc(38, zzayb);
        }
        final Integer zzayc = this.zzayc;
        int n44 = n43;
        if (zzayc != null) {
            n44 = n43 + zzyy.zzh(39, zzayc);
        }
        final String zzawj = this.zzawj;
        int n45 = n44;
        if (zzawj != null) {
            n45 = n44 + zzyy.zzc(41, zzawj);
        }
        final zzfq.zzb zzayd = this.zzayd;
        int n46 = n45;
        if (zzayd != null) {
            n46 = n45 + zzut.zzc(44, zzayd);
        }
        return n46;
    }
}
