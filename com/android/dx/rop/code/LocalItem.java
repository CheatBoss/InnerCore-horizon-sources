package com.android.dx.rop.code;

import com.android.dx.rop.cst.*;

public class LocalItem implements Comparable<LocalItem>
{
    private final CstString name;
    private final CstString signature;
    
    private LocalItem(final CstString name, final CstString signature) {
        this.name = name;
        this.signature = signature;
    }
    
    private static int compareHandlesNulls(final CstString cstString, final CstString cstString2) {
        if (cstString == cstString2) {
            return 0;
        }
        if (cstString == null) {
            return -1;
        }
        if (cstString2 == null) {
            return 1;
        }
        return cstString.compareTo((Constant)cstString2);
    }
    
    public static LocalItem make(final CstString cstString, final CstString cstString2) {
        if (cstString == null && cstString2 == null) {
            return null;
        }
        return new LocalItem(cstString, cstString2);
    }
    
    @Override
    public int compareTo(final LocalItem localItem) {
        final int compareHandlesNulls = compareHandlesNulls(this.name, localItem.name);
        if (compareHandlesNulls != 0) {
            return compareHandlesNulls;
        }
        return compareHandlesNulls(this.signature, localItem.signature);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof LocalItem;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        if (this.compareTo((LocalItem)o) == 0) {
            b2 = true;
        }
        return b2;
    }
    
    public CstString getName() {
        return this.name;
    }
    
    public CstString getSignature() {
        return this.signature;
    }
    
    @Override
    public int hashCode() {
        final CstString name = this.name;
        int hashCode = 0;
        int hashCode2;
        if (name == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.name.hashCode();
        }
        if (this.signature != null) {
            hashCode = this.signature.hashCode();
        }
        return hashCode2 * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        if (this.name != null && this.signature == null) {
            return this.name.toQuoted();
        }
        if (this.name == null && this.signature == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        String quoted;
        if (this.name == null) {
            quoted = "";
        }
        else {
            quoted = this.name.toQuoted();
        }
        sb.append(quoted);
        sb.append("|");
        String quoted2;
        if (this.signature == null) {
            quoted2 = "";
        }
        else {
            quoted2 = this.signature.toQuoted();
        }
        sb.append(quoted2);
        return sb.toString();
    }
}
