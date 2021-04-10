package com.google.android.gms.internal.measurement;

public final class zzzc implements Cloneable
{
    private static final zzzd zzcff;
    private int mSize;
    private boolean zzcfg;
    private int[] zzcfh;
    private zzzd[] zzcfi;
    
    static {
        zzcff = new zzzd();
    }
    
    zzzc() {
        this(10);
    }
    
    private zzzc(int idealIntArraySize) {
        this.zzcfg = false;
        idealIntArraySize = idealIntArraySize(idealIntArraySize);
        this.zzcfh = new int[idealIntArraySize];
        this.zzcfi = new zzzd[idealIntArraySize];
        this.mSize = 0;
    }
    
    private static int idealIntArraySize(int n) {
        final int n2 = n << 2;
        n = 4;
        int n3;
        while (true) {
            n3 = n2;
            if (n >= 32) {
                break;
            }
            n3 = (1 << n) - 12;
            if (n2 <= n3) {
                break;
            }
            ++n;
        }
        return n3 / 4;
    }
    
    private final int zzcd(final int n) {
        int n2 = this.mSize - 1;
        int i = 0;
        while (i <= n2) {
            final int n3 = i + n2 >>> 1;
            final int n4 = this.zzcfh[n3];
            if (n4 < n) {
                i = n3 + 1;
            }
            else {
                if (n4 <= n) {
                    return n3;
                }
                n2 = n3 - 1;
            }
        }
        return ~i;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzzc)) {
            return false;
        }
        final zzzc zzzc = (zzzc)o;
        final int mSize = this.mSize;
        if (mSize != zzzc.mSize) {
            return false;
        }
        final int[] zzcfh = this.zzcfh;
        final int[] zzcfh2 = zzzc.zzcfh;
        while (true) {
            for (int i = 0; i < mSize; ++i) {
                if (zzcfh[i] != zzcfh2[i]) {
                    final boolean b = false;
                    if (b) {
                        final zzzd[] zzcfi = this.zzcfi;
                        final zzzd[] zzcfi2 = zzzc.zzcfi;
                        final int mSize2 = this.mSize;
                        int j = 0;
                        while (true) {
                            while (j < mSize2) {
                                if (!zzcfi[j].equals(zzcfi2[j])) {
                                    final boolean b2 = false;
                                    if (b2) {
                                        return true;
                                    }
                                    return false;
                                }
                                else {
                                    ++j;
                                }
                            }
                            final boolean b2 = true;
                            continue;
                        }
                    }
                    return false;
                }
            }
            final boolean b = true;
            continue;
        }
    }
    
    @Override
    public final int hashCode() {
        int n = 17;
        for (int i = 0; i < this.mSize; ++i) {
            n = (n * 31 + this.zzcfh[i]) * 31 + this.zzcfi[i].hashCode();
        }
        return n;
    }
    
    public final boolean isEmpty() {
        return this.mSize == 0;
    }
    
    final int size() {
        return this.mSize;
    }
    
    final void zza(final int n, final zzzd zzzd) {
        final int zzcd = this.zzcd(n);
        if (zzcd >= 0) {
            this.zzcfi[zzcd] = zzzd;
            return;
        }
        final int n2 = ~zzcd;
        if (n2 < this.mSize) {
            final zzzd[] zzcfi = this.zzcfi;
            if (zzcfi[n2] == zzzc.zzcff) {
                this.zzcfh[n2] = n;
                zzcfi[n2] = zzzd;
                return;
            }
        }
        final int mSize = this.mSize;
        if (mSize >= this.zzcfh.length) {
            final int idealIntArraySize = idealIntArraySize(mSize + 1);
            final int[] zzcfh = new int[idealIntArraySize];
            final zzzd[] zzcfi2 = new zzzd[idealIntArraySize];
            final int[] zzcfh2 = this.zzcfh;
            System.arraycopy(zzcfh2, 0, zzcfh, 0, zzcfh2.length);
            final zzzd[] zzcfi3 = this.zzcfi;
            System.arraycopy(zzcfi3, 0, zzcfi2, 0, zzcfi3.length);
            this.zzcfh = zzcfh;
            this.zzcfi = zzcfi2;
        }
        final int n3 = this.mSize - n2;
        if (n3 != 0) {
            final int[] zzcfh3 = this.zzcfh;
            final int n4 = n2 + 1;
            System.arraycopy(zzcfh3, n2, zzcfh3, n4, n3);
            final zzzd[] zzcfi4 = this.zzcfi;
            System.arraycopy(zzcfi4, n2, zzcfi4, n4, this.mSize - n2);
        }
        this.zzcfh[n2] = n;
        this.zzcfi[n2] = zzzd;
        ++this.mSize;
    }
    
    final zzzd zzcb(int zzcd) {
        zzcd = this.zzcd(zzcd);
        if (zzcd >= 0) {
            final zzzd[] zzcfi = this.zzcfi;
            if (zzcfi[zzcd] != zzzc.zzcff) {
                return zzcfi[zzcd];
            }
        }
        return null;
    }
    
    final zzzd zzcc(final int n) {
        return this.zzcfi[n];
    }
}
