package com.google.gson.internal.bind;

import com.google.gson.*;
import java.lang.reflect.*;
import com.google.gson.stream.*;
import com.google.gson.reflect.*;
import java.io.*;

final class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T>
{
    private final Gson context;
    private final TypeAdapter<T> delegate;
    private final Type type;
    
    TypeAdapterRuntimeTypeWrapper(final Gson context, final TypeAdapter<T> delegate, final Type type) {
        this.context = context;
        this.delegate = delegate;
        this.type = type;
    }
    
    private Type getRuntimeTypeIfMoreSpecific(final Type type, final Object o) {
        Type class1 = type;
        if (o != null) {
            if (type != Object.class && !(type instanceof TypeVariable)) {
                class1 = type;
                if (!(type instanceof Class)) {
                    return class1;
                }
            }
            class1 = o.getClass();
        }
        return class1;
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final T t) throws IOException {
        TypeAdapter<?> typeAdapter = this.delegate;
        final Type runtimeTypeIfMoreSpecific = this.getRuntimeTypeIfMoreSpecific(this.type, t);
        if (runtimeTypeIfMoreSpecific != this.type) {
            typeAdapter = this.context.getAdapter(TypeToken.get(runtimeTypeIfMoreSpecific));
            if (typeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter) {
                final TypeAdapter<T> delegate = this.delegate;
                if (!(delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                    typeAdapter = delegate;
                }
            }
        }
        typeAdapter.write(jsonWriter, t);
    }
}
