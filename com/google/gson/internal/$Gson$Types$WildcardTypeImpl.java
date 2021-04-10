package com.google.gson.internal;

import java.io.*;
import java.lang.reflect.*;

private static final class WildcardTypeImpl implements Serializable, WildcardType
{
    private final Type lowerBound;
    private final Type upperBound;
    
    public WildcardTypeImpl(final Type[] array, final Type[] array2) {
        final int length = array2.length;
        final boolean b = true;
        $Gson$Preconditions.checkArgument(length <= 1);
        $Gson$Preconditions.checkArgument(array.length == 1);
        Type canonicalize;
        if (array2.length == 1) {
            $Gson$Preconditions.checkNotNull(array2[0]);
            $Gson$Types.access$000(array2[0]);
            $Gson$Preconditions.checkArgument(array[0] == Object.class && b);
            this.lowerBound = $Gson$Types.canonicalize(array2[0]);
            canonicalize = Object.class;
        }
        else {
            $Gson$Preconditions.checkNotNull(array[0]);
            $Gson$Types.access$000(array[0]);
            this.lowerBound = null;
            canonicalize = $Gson$Types.canonicalize(array[0]);
        }
        this.upperBound = canonicalize;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof WildcardType && $Gson$Types.equals(this, (Type)o);
    }
    
    @Override
    public Type[] getLowerBounds() {
        final Type lowerBound = this.lowerBound;
        if (lowerBound != null) {
            return new Type[] { lowerBound };
        }
        return $Gson$Types.EMPTY_TYPE_ARRAY;
    }
    
    @Override
    public Type[] getUpperBounds() {
        return new Type[] { this.upperBound };
    }
    
    @Override
    public int hashCode() {
        final Type lowerBound = this.lowerBound;
        int n;
        if (lowerBound != null) {
            n = lowerBound.hashCode() + 31;
        }
        else {
            n = 1;
        }
        return n ^ this.upperBound.hashCode() + 31;
    }
    
    @Override
    public String toString() {
        StringBuilder sb;
        Type type;
        if (this.lowerBound != null) {
            sb = new StringBuilder();
            sb.append("? super ");
            type = this.lowerBound;
        }
        else {
            if (this.upperBound == Object.class) {
                return "?";
            }
            sb = new StringBuilder();
            sb.append("? extends ");
            type = this.upperBound;
        }
        sb.append($Gson$Types.typeToString(type));
        return sb.toString();
    }
}
