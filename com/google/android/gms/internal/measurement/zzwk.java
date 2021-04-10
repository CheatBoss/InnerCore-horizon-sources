package com.google.android.gms.internal.measurement;

final class zzwk implements zzws
{
    private zzws[] zzcaq;
    
    zzwk(final zzws... zzcaq) {
        this.zzcaq = zzcaq;
    }
    
    @Override
    public final boolean zze(final Class<?> clazz) {
        final zzws[] zzcaq = this.zzcaq;
        for (int length = zzcaq.length, i = 0; i < length; ++i) {
            if (zzcaq[i].zze(clazz)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final zzwr zzf(final Class<?> clazz) {
        final zzws[] zzcaq = this.zzcaq;
        for (int length = zzcaq.length, i = 0; i < length; ++i) {
            final zzws zzws = zzcaq[i];
            if (zzws.zze(clazz)) {
                return zzws.zzf(clazz);
            }
        }
        final String value = String.valueOf(clazz.getName());
        String concat;
        if (value.length() != 0) {
            concat = "No factory is available for message type: ".concat(value);
        }
        else {
            concat = new String("No factory is available for message type: ");
        }
        throw new UnsupportedOperationException(concat);
    }
}
