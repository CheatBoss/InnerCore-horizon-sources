package com.google.gson.internal;

import java.io.*;
import java.lang.reflect.*;

private static final class GenericArrayTypeImpl implements Serializable, GenericArrayType
{
    private final Type componentType;
    
    public GenericArrayTypeImpl(final Type type) {
        this.componentType = $Gson$Types.canonicalize(type);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof GenericArrayType && $Gson$Types.equals(this, (Type)o);
    }
    
    @Override
    public Type getGenericComponentType() {
        return this.componentType;
    }
    
    @Override
    public int hashCode() {
        return this.componentType.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append($Gson$Types.typeToString(this.componentType));
        sb.append("[]");
        return sb.toString();
    }
}
