package com.android.dex;

import com.android.dex.util.*;
import java.io.*;

public final class TypeList implements Comparable<TypeList>
{
    public static final TypeList EMPTY;
    private final Dex dex;
    private final short[] types;
    
    static {
        EMPTY = new TypeList(null, Dex.EMPTY_SHORT_ARRAY);
    }
    
    public TypeList(final Dex dex, final short[] types) {
        this.dex = dex;
        this.types = types;
    }
    
    @Override
    public int compareTo(final TypeList list) {
        for (int n = 0; n < this.types.length && n < list.types.length; ++n) {
            if (this.types[n] != list.types[n]) {
                return Unsigned.compare(this.types[n], list.types[n]);
            }
        }
        return Unsigned.compare(this.types.length, list.types.length);
    }
    
    public short[] getTypes() {
        return this.types;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < this.types.length; ++i) {
            Serializable value;
            if (this.dex != null) {
                value = this.dex.typeNames().get(this.types[i]);
            }
            else {
                value = this.types[i];
            }
            sb.append(value);
        }
        sb.append(")");
        return sb.toString();
    }
}
