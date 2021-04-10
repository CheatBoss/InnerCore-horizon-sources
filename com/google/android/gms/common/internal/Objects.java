package com.google.android.gms.common.internal;

import java.util.*;

public final class Objects
{
    public static boolean equal(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static int hashCode(final Object... array) {
        return Arrays.hashCode(array);
    }
    
    public static ToStringHelper toStringHelper(final Object o) {
        return new ToStringHelper(o, null);
    }
    
    public static final class ToStringHelper
    {
        private final List<String> zzul;
        private final Object zzum;
        
        private ToStringHelper(final Object o) {
            this.zzum = Preconditions.checkNotNull(o);
            this.zzul = new ArrayList<String>();
        }
        
        public final ToStringHelper add(String s, final Object o) {
            final List<String> zzul = this.zzul;
            s = Preconditions.checkNotNull(s);
            final String value = String.valueOf(o);
            final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 1 + String.valueOf(value).length());
            sb.append(s);
            sb.append("=");
            sb.append(value);
            zzul.add(sb.toString());
            return this;
        }
        
        @Override
        public final String toString() {
            final StringBuilder sb = new StringBuilder(100);
            sb.append(this.zzum.getClass().getSimpleName());
            sb.append('{');
            for (int size = this.zzul.size(), i = 0; i < size; ++i) {
                sb.append(this.zzul.get(i));
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append('}');
            return sb.toString();
        }
    }
}
