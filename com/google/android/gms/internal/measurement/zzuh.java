package com.google.android.gms.internal.measurement;

final class zzuh extends zzum
{
    private final int zzbud;
    private final int zzbue;
    
    zzuh(final byte[] array, final int zzbud, final int zzbue) {
        super(array);
        zzud.zzb(zzbud, zzbud + zzbue, array.length);
        this.zzbud = zzbud;
        this.zzbue = zzbue;
    }
    
    @Override
    public final int size() {
        return this.zzbue;
    }
    
    @Override
    public final byte zzal(final int n) {
        final int size = this.size();
        if ((size - (n + 1) | n) >= 0) {
            return this.zzbug[this.zzbud + n];
        }
        if (n < 0) {
            final StringBuilder sb = new StringBuilder(22);
            sb.append("Index < 0: ");
            sb.append(n);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder(40);
        sb2.append("Index > length: ");
        sb2.append(n);
        sb2.append(", ");
        sb2.append(size);
        throw new ArrayIndexOutOfBoundsException(sb2.toString());
    }
    
    @Override
    protected final int zzud() {
        return this.zzbud;
    }
}
