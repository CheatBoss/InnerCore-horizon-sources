package com.google.android.gms.internal.measurement;

final class zzwi implements zzxk
{
    private static final zzws zzcap;
    private final zzws zzcao;
    
    static {
        zzcap = new zzwj();
    }
    
    public zzwi() {
        this(new zzwk(new zzws[] { zzvl.zzwb(), zzwz() }));
    }
    
    private zzwi(final zzws zzws) {
        this.zzcao = zzvo.zza(zzws, "messageInfoFactory");
    }
    
    private static boolean zza(final zzwr zzwr) {
        return zzwr.zzxg() == zzvm.zze.zzbzb;
    }
    
    private static zzws zzwz() {
        try {
            return (zzws)Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        catch (Exception ex) {
            return zzwi.zzcap;
        }
    }
    
    @Override
    public final <T> zzxj<T> zzh(final Class<T> clazz) {
        zzxl.zzj(clazz);
        final zzwr zzf = this.zzcao.zzf(clazz);
        if (zzf.zzxh()) {
            if (zzvm.class.isAssignableFrom(clazz)) {
                return (zzxj<T>)zzwy.zza(zzxl.zzxt(), zzvc.zzvr(), zzf.zzxi());
            }
            return (zzxj<T>)zzwy.zza(zzxl.zzxr(), zzvc.zzvs(), zzf.zzxi());
        }
        else if (zzvm.class.isAssignableFrom(clazz)) {
            if (zza(zzf)) {
                return zzwx.zza(clazz, zzf, zzxc.zzxl(), zzwd.zzwy(), zzxl.zzxt(), zzvc.zzvr(), zzwq.zzxe());
            }
            return zzwx.zza(clazz, zzf, zzxc.zzxl(), zzwd.zzwy(), zzxl.zzxt(), null, zzwq.zzxe());
        }
        else {
            if (zza(zzf)) {
                return zzwx.zza(clazz, zzf, zzxc.zzxk(), zzwd.zzwx(), zzxl.zzxr(), zzvc.zzvs(), zzwq.zzxd());
            }
            return zzwx.zza(clazz, zzf, zzxc.zzxk(), zzwd.zzwx(), zzxl.zzxs(), null, zzwq.zzxd());
        }
    }
}
