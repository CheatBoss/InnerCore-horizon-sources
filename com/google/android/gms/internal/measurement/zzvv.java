package com.google.android.gms.internal.measurement;

public enum zzvv
{
    zzbzn((Class<?>)Void.class, (Class<?>)Void.class, (Object)null), 
    zzbzo((Class<?>)Integer.TYPE, (Class<?>)Integer.class, (Object)0), 
    zzbzp((Class<?>)Long.TYPE, (Class<?>)Long.class, (Object)0L), 
    zzbzq((Class<?>)Float.TYPE, (Class<?>)Float.class, (Object)0.0f), 
    zzbzr((Class<?>)Double.TYPE, (Class<?>)Double.class, (Object)0.0), 
    zzbzs((Class<?>)Boolean.TYPE, (Class<?>)Boolean.class, (Object)false), 
    zzbzt((Class<?>)String.class, (Class<?>)String.class, (Object)""), 
    zzbzu((Class<?>)zzud.class, (Class<?>)zzud.class, (Object)zzud.zzbtz), 
    zzbzv((Class<?>)Integer.TYPE, (Class<?>)Integer.class, (Object)null), 
    zzbzw((Class<?>)Object.class, (Class<?>)Object.class, (Object)null);
    
    private final Class<?> zzbzx;
    private final Class<?> zzbzy;
    private final Object zzbzz;
    
    private zzvv(final Class<?> zzbzx, final Class<?> zzbzy, final Object zzbzz) {
        this.zzbzx = zzbzx;
        this.zzbzy = zzbzy;
        this.zzbzz = zzbzz;
    }
    
    public final Class<?> zzws() {
        return this.zzbzy;
    }
}
