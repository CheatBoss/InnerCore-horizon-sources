package com.google.android.gms.common.api.internal;

public final class ListenerHolder<L>
{
    private volatile L zzli;
    
    public final void clear() {
        this.zzli = null;
    }
    
    public static final class ListenerKey<L>
    {
        private final L zzli;
        private final String zzll;
        
        @Override
        public final boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ListenerKey)) {
                return false;
            }
            final ListenerKey listenerKey = (ListenerKey)o;
            return this.zzli == listenerKey.zzli && this.zzll.equals(listenerKey.zzll);
        }
        
        @Override
        public final int hashCode() {
            return System.identityHashCode(this.zzli) * 31 + this.zzll.hashCode();
        }
    }
}
