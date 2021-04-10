package com.android.dx.rop.annotation;

import com.android.dx.rop.cst.*;

public final class NameValuePair implements Comparable<NameValuePair>
{
    private final CstString name;
    private final Constant value;
    
    public NameValuePair(final CstString name, final Constant value) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        if (value == null) {
            throw new NullPointerException("value == null");
        }
        this.name = name;
        this.value = value;
    }
    
    @Override
    public int compareTo(final NameValuePair nameValuePair) {
        final int compareTo = this.name.compareTo((Constant)nameValuePair.name);
        if (compareTo != 0) {
            return compareTo;
        }
        return this.value.compareTo(nameValuePair.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof NameValuePair;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final NameValuePair nameValuePair = (NameValuePair)o;
        boolean b3 = b2;
        if (this.name.equals(nameValuePair.name)) {
            b3 = b2;
            if (this.value.equals(nameValuePair.value)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public CstString getName() {
        return this.name;
    }
    
    public Constant getValue() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() * 31 + this.value.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name.toHuman());
        sb.append(":");
        sb.append(this.value);
        return sb.toString();
    }
}
