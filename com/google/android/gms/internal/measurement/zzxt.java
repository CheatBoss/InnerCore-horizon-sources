package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzxt implements Comparable<zzxt>, Entry<Object, Object>
{
    private value;
    private final /* synthetic */ zzxm zzcch;
    private final zzcck;
    
    zzxt(final zzxm zzcch, final Comparable zzcck, final Object value) {
        this.zzcch = zzcch;
        this.zzcck = zzcck;
        this.value = value;
    }
    
    zzxt(final zzxm zzxm, final Entry<Object, Object> entry) {
        this(zzxm, entry.getKey(), entry.getValue());
    }
    
    private static boolean equals(final Object o, final Object o2) {
        if (o == null) {
            return o2 == null;
        }
        return o.equals(o2);
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Entry)) {
            return false;
        }
        final Entry entry = (Entry)o;
        return equals(this.zzcck, entry.getKey()) && equals(this.value, entry.getValue());
    }
    
    @Override
    public final Object getValue() {
        return this.value;
    }
    
    @Override
    public final int hashCode() {
        final Comparable zzcck = this.zzcck;
        int hashCode = 0;
        int hashCode2;
        if (zzcck == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = zzcck.hashCode();
        }
        final Object value = this.value;
        if (value != null) {
            hashCode = value.hashCode();
        }
        return hashCode2 ^ hashCode;
    }
    
    @Override
    public final Object setValue(final Object value) {
        this.zzcch.zzxz();
        final Object value2 = this.value;
        this.value = value;
        return value2;
    }
    
    @Override
    public final String toString() {
        final String value = String.valueOf(this.zzcck);
        final String value2 = String.valueOf(this.value);
        final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 1 + String.valueOf(value2).length());
        sb.append(value);
        sb.append("=");
        sb.append(value2);
        return sb.toString();
    }
}
