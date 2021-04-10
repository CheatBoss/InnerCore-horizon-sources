package com.google.android.gms.internal.measurement;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.lang.reflect.*;

public abstract class zzvm<MessageType extends zzvm<MessageType, BuilderType>, BuilderType extends zza<MessageType, BuilderType>> extends zztw<MessageType, BuilderType>
{
    private static Map<Object, zzvm<?, ?>> zzbyo;
    protected zzyc zzbym;
    private int zzbyn;
    
    static {
        zzvm.zzbyo = new ConcurrentHashMap<Object, zzvm<?, ?>>();
    }
    
    public zzvm() {
        this.zzbym = zzyc.zzyf();
        this.zzbyn = -1;
    }
    
    static <T extends zzvm<T, ?>> T zza(T t, final zzuo zzuo, final zzuz zzuz) throws zzvt {
        t = (T)t.zza(zze.zzbyw, null, (Object)null);
        try {
            zzxf.zzxn().zzag(t).zza(t, zzur.zza(zzuo), zzuz);
            zzxf.zzxn().zzag(t).zzu(t);
            return t;
        }
        catch (RuntimeException ex) {
            if (ex.getCause() instanceof zzvt) {
                throw (zzvt)ex.getCause();
            }
            throw ex;
        }
        catch (IOException ex2) {
            if (ex2.getCause() instanceof zzvt) {
                throw (zzvt)ex2.getCause();
            }
            throw new zzvt(ex2.getMessage()).zzg(t);
        }
    }
    
    protected static Object zza(final zzwt zzwt, final String s, final Object[] array) {
        return new zzxh(zzwt, s, array);
    }
    
    static Object zza(final Method method, final Object o, final Object... array) {
        try {
            return method.invoke(o, array);
        }
        catch (InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }
            if (cause instanceof Error) {
                throw (Error)cause;
            }
            throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
        }
        catch (IllegalAccessException ex2) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", ex2);
        }
    }
    
    protected static <T extends zzvm<?, ?>> void zza(final Class<T> clazz, final T t) {
        zzvm.zzbyo.put(clazz, t);
    }
    
    protected static final <T extends zzvm<T, ?>> boolean zza(final T t, final boolean b) {
        final byte byteValue = (byte)t.zza(zze.zzbyt, null, (Object)null);
        return byteValue == 1 || (byteValue != 0 && zzxf.zzxn().zzag(t).zzaf(t));
    }
    
    static <T extends zzvm<?, ?>> T zzg(final Class<T> clazz) {
        zzvm<?, ?> zzvm;
        if ((zzvm = com.google.android.gms.internal.measurement.zzvm.zzbyo.get(clazz)) == null) {
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
                zzvm = com.google.android.gms.internal.measurement.zzvm.zzbyo.get(clazz);
            }
            catch (ClassNotFoundException ex) {
                throw new IllegalStateException("Class initialization cannot fail.", ex);
            }
        }
        if (zzvm == null) {
            final String value = String.valueOf(clazz.getName());
            String concat;
            if (value.length() != 0) {
                concat = "Unable to get default instance for: ".concat(value);
            }
            else {
                concat = new String("Unable to get default instance for: ");
            }
            throw new IllegalStateException(concat);
        }
        return (T)zzvm;
    }
    
    protected static <E> zzvs<E> zzwc() {
        return (zzvs<E>)zzxg.zzxo();
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (((zzvm)this.zza(zze.zzbyy, null, (Object)null)).getClass().isInstance(o) && zzxf.zzxn().zzag(this).equals(this, (zzvm)o));
    }
    
    @Override
    public int hashCode() {
        if (this.zzbtr != 0) {
            return this.zzbtr;
        }
        return this.zzbtr = zzxf.zzxn().zzag(this).hashCode(this);
    }
    
    @Override
    public final boolean isInitialized() {
        final boolean booleanValue = Boolean.TRUE;
        final byte byteValue = (byte)this.zza(zze.zzbyt, null, (Object)null);
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        final boolean zzaf = zzxf.zzxn().zzag(this).zzaf(this);
        if (booleanValue) {
            final int zzbyu = zze.zzbyu;
            zzvm zzvm;
            if (zzaf) {
                zzvm = this;
            }
            else {
                zzvm = null;
            }
            this.zza(zzbyu, zzvm, null);
        }
        return zzaf;
    }
    
    @Override
    public String toString() {
        return zzww.zza(this, super.toString());
    }
    
    protected abstract Object zza(final int p0, final Object p1, final Object p2);
    
    @Override
    final void zzah(final int zzbyn) {
        this.zzbyn = zzbyn;
    }
    
    @Override
    public final void zzb(final zzut zzut) throws IOException {
        zzxf.zzxn().zzi(this.getClass()).zza((zzvm)this, (zzyw)zzuv.zza(zzut));
    }
    
    @Override
    final int zztu() {
        return this.zzbyn;
    }
    
    @Override
    public final int zzvu() {
        if (this.zzbyn == -1) {
            this.zzbyn = zzxf.zzxn().zzag(this).zzae(this);
        }
        return this.zzbyn;
    }
    
    public static class zza<MessageType extends zzvm<MessageType, BuilderType>, BuilderType extends zza<MessageType, BuilderType>> extends zztx<MessageType, BuilderType>
    {
        private final MessageType zzbyp;
        private MessageType zzbyq;
        private boolean zzbyr;
        
        protected zza(final MessageType zzbyp) {
            this.zzbyp = zzbyp;
            this.zzbyq = (MessageType)zzbyp.zza(zze.zzbyw, null, (Object)null);
            this.zzbyr = false;
        }
        
        private static void zza(final MessageType messageType, final MessageType messageType2) {
            zzxf.zzxn().zzag(messageType).zzd(messageType, messageType2);
        }
        
        @Override
        public final boolean isInitialized() {
            return zzvm.zza(this.zzbyq, false);
        }
        
        public final BuilderType zza(final MessageType messageType) {
            if (this.zzbyr) {
                final zzvm zzbyq = (zzvm)this.zzbyq.zza(zze.zzbyw, null, (Object)null);
                zza((MessageType)zzbyq, this.zzbyq);
                this.zzbyq = (MessageType)zzbyq;
                this.zzbyr = false;
            }
            zza(this.zzbyq, messageType);
            return (BuilderType)this;
        }
        
        public MessageType zzwg() {
            if (this.zzbyr) {
                return this.zzbyq;
            }
            final zzvm<MessageType, BuilderType> zzbyq = this.zzbyq;
            zzxf.zzxn().zzag(zzbyq).zzu(zzbyq);
            this.zzbyr = true;
            return this.zzbyq;
        }
        
        public final MessageType zzwh() {
            final zzvm zzvm = (zzvm)this.zzwi();
            final boolean booleanValue = Boolean.TRUE;
            final byte byteValue = (byte)zzvm.zza(zze.zzbyt, null, null);
            boolean zzaf = true;
            if (byteValue != 1) {
                if (byteValue == 0) {
                    zzaf = false;
                }
                else {
                    final boolean b = zzaf = zzxf.zzxn().zzag(zzvm).zzaf((MessageType)zzvm);
                    if (booleanValue) {
                        final int zzbyu = zze.zzbyu;
                        zzvm<MessageType, BuilderType> zzvm2;
                        if (b) {
                            zzvm2 = (zzvm<MessageType, BuilderType>)zzvm;
                        }
                        else {
                            zzvm2 = null;
                        }
                        zzvm.zza(zzbyu, zzvm2, null);
                        zzaf = b;
                    }
                }
            }
            if (zzaf) {
                return (MessageType)zzvm;
            }
            throw new zzya(zzvm);
        }
        
        @Override
        public /* synthetic */ zzwt zzwi() {
            return this.zzwg();
        }
    }
    
    public static final class zzb<T extends zzvm<T, ?>> extends zzty<T>
    {
        private final T zzbyp;
        
        public zzb(final T zzbyp) {
            this.zzbyp = zzbyp;
        }
    }
    
    public abstract static class zzc<MessageType extends zzc<MessageType, BuilderType>, BuilderType> extends zzvm<MessageType, BuilderType>
    {
        protected zzvd<Object> zzbys;
        
        public zzc() {
            this.zzbys = zzvd.zzvt();
        }
    }
    
    public static final class zzd<ContainingType extends zzwt, Type> extends zzux<ContainingType, Type>
    {
    }
    
    public enum zze
    {
        public static final int zzbyt = 1;
        public static final int zzbyu = 2;
        public static final int zzbyv = 3;
        public static final int zzbyw = 4;
        public static final int zzbyx = 5;
        public static final int zzbyy = 6;
        public static final int zzbyz = 7;
        private static final /* synthetic */ int[] zzbza;
        public static final int zzbzb = 1;
        public static final int zzbzc = 2;
        public static final int zzbze = 1;
        public static final int zzbzf = 2;
        
        static {
            zzbza = new int[] { 1, 2, 3, 4, 5, 6, 7 };
            zzbzd = new int[] { 1, 2 };
            zzbzg = new int[] { 1, 2 };
        }
        
        public static int[] values$50KLMJ33DTMIUPRFDTJMOP9FE1P6UT3FC9QMCBQ7CLN6ASJ1EHIM8JB5EDPM2PR59HKN8P949LIN8Q3FCHA6UIBEEPNMMP9R0() {
            return zze.zzbza.clone();
        }
    }
}
