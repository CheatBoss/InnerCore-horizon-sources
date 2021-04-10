package com.google.android.gms.internal.measurement;

import java.lang.reflect.*;

public enum zzvg
{
    zzbvu(0, zzvi.zzbyc, zzvv.zzbzr), 
    zzbvv(1, zzvi.zzbyc, zzvv.zzbzq), 
    zzbvw(2, zzvi.zzbyc, zzvv.zzbzp), 
    zzbvx(3, zzvi.zzbyc, zzvv.zzbzp), 
    zzbvy(4, zzvi.zzbyc, zzvv.zzbzo), 
    zzbvz(5, zzvi.zzbyc, zzvv.zzbzp), 
    zzbwa(6, zzvi.zzbyc, zzvv.zzbzo), 
    zzbwb(7, zzvi.zzbyc, zzvv.zzbzs), 
    zzbwc(8, zzvi.zzbyc, zzvv.zzbzt), 
    zzbwd(9, zzvi.zzbyc, zzvv.zzbzw), 
    zzbwe(10, zzvi.zzbyc, zzvv.zzbzu), 
    zzbwf(11, zzvi.zzbyc, zzvv.zzbzo), 
    zzbwg(12, zzvi.zzbyc, zzvv.zzbzv), 
    zzbwh(13, zzvi.zzbyc, zzvv.zzbzo), 
    zzbwi(14, zzvi.zzbyc, zzvv.zzbzp), 
    zzbwj(15, zzvi.zzbyc, zzvv.zzbzo), 
    zzbwk(16, zzvi.zzbyc, zzvv.zzbzp), 
    zzbwl(17, zzvi.zzbyc, zzvv.zzbzw), 
    zzbwm(18, zzvi.zzbyd, zzvv.zzbzr), 
    zzbwn(19, zzvi.zzbyd, zzvv.zzbzq), 
    zzbwo(20, zzvi.zzbyd, zzvv.zzbzp), 
    zzbwp(21, zzvi.zzbyd, zzvv.zzbzp), 
    zzbwq(22, zzvi.zzbyd, zzvv.zzbzo), 
    zzbwr(23, zzvi.zzbyd, zzvv.zzbzp), 
    zzbws(24, zzvi.zzbyd, zzvv.zzbzo), 
    zzbwt(25, zzvi.zzbyd, zzvv.zzbzs), 
    zzbwu(26, zzvi.zzbyd, zzvv.zzbzt), 
    zzbwv(27, zzvi.zzbyd, zzvv.zzbzw), 
    zzbww(28, zzvi.zzbyd, zzvv.zzbzu), 
    zzbwx(29, zzvi.zzbyd, zzvv.zzbzo), 
    zzbwy(30, zzvi.zzbyd, zzvv.zzbzv), 
    zzbwz(31, zzvi.zzbyd, zzvv.zzbzo), 
    zzbxa(32, zzvi.zzbyd, zzvv.zzbzp), 
    zzbxb(33, zzvi.zzbyd, zzvv.zzbzo), 
    zzbxc(34, zzvi.zzbyd, zzvv.zzbzp), 
    zzbxd(35, zzvi.zzbye, zzvv.zzbzr), 
    zzbxe(36, zzvi.zzbye, zzvv.zzbzq), 
    zzbxf(37, zzvi.zzbye, zzvv.zzbzp), 
    zzbxg(38, zzvi.zzbye, zzvv.zzbzp), 
    zzbxh(39, zzvi.zzbye, zzvv.zzbzo), 
    zzbxi(40, zzvi.zzbye, zzvv.zzbzp), 
    zzbxj(41, zzvi.zzbye, zzvv.zzbzo), 
    zzbxk(42, zzvi.zzbye, zzvv.zzbzs), 
    zzbxl(43, zzvi.zzbye, zzvv.zzbzo), 
    zzbxm(44, zzvi.zzbye, zzvv.zzbzv), 
    zzbxn(45, zzvi.zzbye, zzvv.zzbzo), 
    zzbxo(46, zzvi.zzbye, zzvv.zzbzp), 
    zzbxp(47, zzvi.zzbye, zzvv.zzbzo), 
    zzbxq(48, zzvi.zzbye, zzvv.zzbzp), 
    zzbxr(49, zzvi.zzbyd, zzvv.zzbzw), 
    zzbxs(50, zzvi.zzbyf, zzvv.zzbzn);
    
    private static final zzvg[] zzbxx;
    private static final Type[] zzbxy;
    private final int id;
    private final zzvv zzbxt;
    private final zzvi zzbxu;
    private final Class<?> zzbxv;
    private final boolean zzbxw;
    
    static {
        final zzvg zzbvu2 = zzvg.zzbvu;
        int i = 0;
        zzbxy = new Type[0];
        final zzvg[] values = values();
        zzbxx = new zzvg[values.length];
        while (i < values.length) {
            final zzvg zzvg2 = values[i];
            zzvg.zzbxx[zzvg2.id] = zzvg2;
            ++i;
        }
    }
    
    private zzvg(final int id, final zzvi zzbxu, final zzvv zzbxt) {
        this.id = id;
        this.zzbxu = zzbxu;
        this.zzbxt = zzbxt;
        n = zzvh.zzbya[zzbxu.ordinal()];
        boolean zzbxw = true;
        Class<?> zzws;
        if (n != 1 && n != 2) {
            zzws = null;
        }
        else {
            zzws = zzbxt.zzws();
        }
        this.zzbxv = zzws;
        Label_0101: {
            if (zzbxu == zzvi.zzbyc) {
                n = zzvh.zzbyb[zzbxt.ordinal()];
                if (n != 1 && n != 2 && n != 3) {
                    break Label_0101;
                }
            }
            zzbxw = false;
        }
        this.zzbxw = zzbxw;
    }
    
    public final int id() {
        return this.id;
    }
}
