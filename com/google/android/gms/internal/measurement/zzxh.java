package com.google.android.gms.internal.measurement;

final class zzxh implements zzwr
{
    private final int flags;
    private final String info;
    private final Object[] zzcba;
    private final zzwt zzcbd;
    
    zzxh(final zzwt zzcbd, final String info, final Object[] zzcba) {
        this.zzcbd = zzcbd;
        this.info = info;
        this.zzcba = zzcba;
        final char char1 = info.charAt(0);
        if (char1 < '\ud800') {
            this.flags = char1;
            return;
        }
        int n = char1 & '\u1fff';
        int n2 = 1;
        int n3 = 13;
        char char2;
        while (true) {
            char2 = info.charAt(n2);
            if (char2 < '\ud800') {
                break;
            }
            n |= (char2 & '\u1fff') << n3;
            n3 += 13;
            ++n2;
        }
        this.flags = (n | char2 << n3);
    }
    
    @Override
    public final int zzxg() {
        if ((this.flags & 0x1) == 0x1) {
            return zzvm.zze.zzbzb;
        }
        return zzvm.zze.zzbzc;
    }
    
    @Override
    public final boolean zzxh() {
        return (this.flags & 0x2) == 0x2;
    }
    
    @Override
    public final zzwt zzxi() {
        return this.zzcbd;
    }
    
    final String zzxp() {
        return this.info;
    }
    
    final Object[] zzxq() {
        return this.zzcba;
    }
}
