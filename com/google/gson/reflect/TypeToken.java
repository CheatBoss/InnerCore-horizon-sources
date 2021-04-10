package com.google.gson.reflect;

import com.google.gson.internal.*;
import java.lang.reflect.*;

public class TypeToken<T>
{
    final int hashCode;
    final Class<? super T> rawType;
    final Type type;
    
    protected TypeToken() {
        final Type superclassTypeParameter = getSuperclassTypeParameter(this.getClass());
        this.type = superclassTypeParameter;
        this.rawType = (Class<? super T>)$Gson$Types.getRawType(superclassTypeParameter);
        this.hashCode = this.type.hashCode();
    }
    
    TypeToken(Type canonicalize) {
        canonicalize = $Gson$Types.canonicalize($Gson$Preconditions.checkNotNull(canonicalize));
        this.type = canonicalize;
        this.rawType = (Class<? super T>)$Gson$Types.getRawType(canonicalize);
        this.hashCode = this.type.hashCode();
    }
    
    public static <T> TypeToken<T> get(final Class<T> clazz) {
        return new TypeToken<T>(clazz);
    }
    
    public static TypeToken<?> get(final Type type) {
        return new TypeToken<Object>(type);
    }
    
    static Type getSuperclassTypeParameter(final Class<?> clazz) {
        final Type genericSuperclass = clazz.getGenericSuperclass();
        if (!(genericSuperclass instanceof Class)) {
            return $Gson$Types.canonicalize(((ParameterizedType)genericSuperclass).getActualTypeArguments()[0]);
        }
        throw new RuntimeException("Missing type parameter.");
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o instanceof TypeToken && $Gson$Types.equals(this.type, ((TypeToken)o).type);
    }
    
    public final Class<? super T> getRawType() {
        return this.rawType;
    }
    
    public final Type getType() {
        return this.type;
    }
    
    @Override
    public final int hashCode() {
        return this.hashCode;
    }
    
    @Override
    public final String toString() {
        return $Gson$Types.typeToString(this.type);
    }
}
