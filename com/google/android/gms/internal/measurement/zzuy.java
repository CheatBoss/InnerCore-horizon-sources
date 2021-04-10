package com.google.android.gms.internal.measurement;

final class zzuy
{
    private static final Class<?> zzbvi;
    
    static {
        zzbvi = zzvk();
    }
    
    private static final zzuz zzfz(final String s) throws Exception {
        return (zzuz)zzuy.zzbvi.getDeclaredMethod(s, (Class<?>[])new Class[0]).invoke(null, new Object[0]);
    }
    
    private static Class<?> zzvk() {
        try {
            return Class.forName("com.google.protobuf.ExtensionRegistry");
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    public static zzuz zzvl() {
        if (zzuy.zzbvi != null) {
            try {
                return zzfz("getEmptyRegistry");
            }
            catch (Exception ex) {}
        }
        return zzuz.zzbvm;
    }
    
    static zzuz zzvm() {
        zzuz zzfz = null;
        Label_0018: {
            if (zzuy.zzbvi != null) {
                try {
                    zzfz = zzfz("loadGeneratedRegistry");
                    break Label_0018;
                }
                catch (Exception ex) {}
            }
            zzfz = null;
        }
        zzuz zzvm = zzfz;
        if (zzfz == null) {
            zzvm = zzuz.zzvm();
        }
        zzuz zzvl;
        if ((zzvl = zzvm) == null) {
            zzvl = zzvl();
        }
        return zzvl;
    }
}
