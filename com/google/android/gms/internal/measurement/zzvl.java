package com.google.android.gms.internal.measurement;

final class zzvl implements zzws
{
    private static final zzvl zzbyl;
    
    static {
        zzbyl = new zzvl();
    }
    
    private zzvl() {
    }
    
    public static zzvl zzwb() {
        return zzvl.zzbyl;
    }
    
    @Override
    public final boolean zze(final Class<?> clazz) {
        return zzvm.class.isAssignableFrom(clazz);
    }
    
    @Override
    public final zzwr zzf(final Class<?> clazz) {
        if (!zzvm.class.isAssignableFrom(clazz)) {
            final String value = String.valueOf(clazz.getName());
            String concat;
            if (value.length() != 0) {
                concat = "Unsupported message type: ".concat(value);
            }
            else {
                concat = new String("Unsupported message type: ");
            }
            throw new IllegalArgumentException(concat);
        }
        try {
            return (zzwr)((zzvm)zzvm.zzg(clazz.asSubclass(zzvm.class))).zza(zzvm.zze.zzbyv, null, (Object)null);
        }
        catch (Exception ex) {
            final String value2 = String.valueOf(clazz.getName());
            String concat2;
            if (value2.length() != 0) {
                concat2 = "Unable to get message info for ".concat(value2);
            }
            else {
                concat2 = new String("Unable to get message info for ");
            }
            throw new RuntimeException(concat2, ex);
        }
    }
}
