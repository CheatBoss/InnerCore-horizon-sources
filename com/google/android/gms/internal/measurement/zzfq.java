package com.google.android.gms.internal.measurement;

public final class zzfq
{
    public static final class zza extends zzvm<zzfq.zza, zzfq.zza.zza>
    {
        private static final zzfq.zza zzauq;
        private static volatile zzxd<zzfq.zza> zznw;
        private String zzauo;
        
        static {
            zzvm.zza(zzfq.zza.class, zzauq = new zzfq.zza());
        }
        
        private zza() {
            this.zzauo = "";
        }
        
        @Override
        protected final Object zza(final int n, final Object o, final Object o2) {
            switch (zzfr.zznq[n - 1]) {
                default: {
                    throw new UnsupportedOperationException();
                }
                case 7: {
                    return null;
                }
                case 6: {
                    return 1;
                }
                case 5: {
                    final zzxd<zzfq.zza> zznw = zzfq.zza.zznw;
                    if (zznw == null) {
                        synchronized (zzfq.zza.class) {
                            zzxd<zzfq.zza> zznw2;
                            if ((zznw2 = zzfq.zza.zznw) == null) {
                                zznw2 = (zzfq.zza.zznw = new zzvm.zzb<zzfq.zza>(zzfq.zza.zzauq));
                            }
                            return zznw2;
                        }
                    }
                    return zznw;
                }
                case 4: {
                    return zzfq.zza.zzauq;
                }
                case 3: {
                    return zzvm.zza(zzfq.zza.zzauq, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001\b\u0000\u0002\u0002\u0001", new Object[] { "zznr", "zzauo", "zzaup" });
                }
                case 2: {
                    return new zzfq.zza.zza((zzfr)null);
                }
                case 1: {
                    return new zzfq.zza();
                }
            }
        }
        
        public static final class zza extends zzvm.zza<zzfq.zza, zza>
        {
            private zza() {
                super(zzfq.zza.zzauq);
            }
        }
    }
    
    public static final class zzb extends zzvm<zzfq.zzb, zza>
    {
        private static final zzfq.zzb zzaut;
        private static volatile zzxd<zzfq.zzb> zznw;
        private int zzaur;
        private zzvs<zzfq.zza> zzaus;
        
        static {
            zzvm.zza(zzfq.zzb.class, zzaut = new zzfq.zzb());
        }
        
        private zzb() {
            this.zzaur = 1;
            this.zzaus = zzvm.zzwc();
        }
        
        public static zzxd<zzfq.zzb> zza() {
            return (zzxd<zzfq.zzb>)zzfq.zzb.zzaut.zza(zze.zzbyz, null, (Object)null);
        }
        
        @Override
        protected final Object zza(final int n, final Object o, final Object o2) {
            switch (zzfr.zznq[n - 1]) {
                default: {
                    throw new UnsupportedOperationException();
                }
                case 7: {
                    return null;
                }
                case 6: {
                    return 1;
                }
                case 5: {
                    final zzxd<zzfq.zzb> zznw = zzfq.zzb.zznw;
                    if (zznw == null) {
                        synchronized (zzfq.zzb.class) {
                            zzxd<zzfq.zzb> zznw2;
                            if ((zznw2 = zzfq.zzb.zznw) == null) {
                                zznw2 = (zzfq.zzb.zznw = new zzvm.zzb<zzfq.zzb>(zzfq.zzb.zzaut));
                            }
                            return zznw2;
                        }
                    }
                    return zznw;
                }
                case 4: {
                    return zzfq.zzb.zzaut;
                }
                case 3: {
                    return zzvm.zza(zzfq.zzb.zzaut, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001\f\u0000\u0002\u001b", new Object[] { "zznr", "zzaur", zzfq.zzb.zzb.zzd(), "zzaus", zzfq.zza.class });
                }
                case 2: {
                    return new zza((zzfr)null);
                }
                case 1: {
                    return new zzfq.zzb();
                }
            }
        }
        
        public static final class zza extends zzvm.zza<zzfq.zzb, zza>
        {
            private zza() {
                super(zzfq.zzb.zzaut);
            }
        }
        
        public enum zzb implements zzvp
        {
            zzauu(1), 
            zzauv(2);
            
            private static final zzvq<zzb> zzoa;
            private final int value;
            
            static {
                zzoa = new zzfs();
            }
            
            private zzb(final int value) {
                this.value = value;
            }
            
            public static zzvr zzd() {
                return zzft.zzoc;
            }
            
            public static zzb zzs(final int n) {
                if (n == 1) {
                    return zzb.zzauu;
                }
                if (n != 2) {
                    return null;
                }
                return zzb.zzauv;
            }
            
            @Override
            public final int zzc() {
                return this.value;
            }
        }
    }
}
