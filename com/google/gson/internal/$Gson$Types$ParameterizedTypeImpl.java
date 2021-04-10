package com.google.gson.internal;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

private static final class ParameterizedTypeImpl implements Serializable, ParameterizedType
{
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;
    
    public ParameterizedTypeImpl(Type canonicalize, final Type type, final Type... array) {
        final boolean b = type instanceof Class;
        final int n = 0;
        if (b) {
            final Class clazz = (Class)type;
            final boolean static1 = Modifier.isStatic(clazz.getModifiers());
            final boolean b2 = true;
            final boolean b3 = static1 || clazz.getEnclosingClass() == null;
            boolean b4 = b2;
            if (canonicalize == null) {
                b4 = (b3 && b2);
            }
            $Gson$Preconditions.checkArgument(b4);
        }
        if (canonicalize == null) {
            canonicalize = null;
        }
        else {
            canonicalize = $Gson$Types.canonicalize(canonicalize);
        }
        this.ownerType = canonicalize;
        this.rawType = $Gson$Types.canonicalize(type);
        this.typeArguments = array.clone();
        int n2 = n;
        while (true) {
            final Type[] typeArguments = this.typeArguments;
            if (n2 >= typeArguments.length) {
                break;
            }
            $Gson$Preconditions.checkNotNull(typeArguments[n2]);
            $Gson$Types.access$000(this.typeArguments[n2]);
            final Type[] typeArguments2 = this.typeArguments;
            typeArguments2[n2] = $Gson$Types.canonicalize(typeArguments2[n2]);
            ++n2;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof ParameterizedType && $Gson$Types.equals(this, (Type)o);
    }
    
    @Override
    public Type[] getActualTypeArguments() {
        return this.typeArguments.clone();
    }
    
    @Override
    public Type getOwnerType() {
        return this.ownerType;
    }
    
    @Override
    public Type getRawType() {
        return this.rawType;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ $Gson$Types.access$100(this.ownerType);
    }
    
    @Override
    public String toString() {
        final int length = this.typeArguments.length;
        int i = 1;
        final StringBuilder sb = new StringBuilder((length + 1) * 30);
        sb.append($Gson$Types.typeToString(this.rawType));
        if (this.typeArguments.length == 0) {
            return sb.toString();
        }
        sb.append("<");
        sb.append($Gson$Types.typeToString(this.typeArguments[0]));
        while (i < this.typeArguments.length) {
            sb.append(", ");
            sb.append($Gson$Types.typeToString(this.typeArguments[i]));
            ++i;
        }
        sb.append(">");
        return sb.toString();
    }
}
