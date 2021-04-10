package com.google.gson.internal.bind;

import com.google.gson.internal.*;
import com.google.gson.reflect.*;
import com.google.gson.annotations.*;
import com.google.gson.*;

public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    
    public JsonAdapterAnnotationTypeAdapterFactory(final ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }
    
    static TypeAdapter<?> getTypeAdapter(final ConstructorConstructor constructorConstructor, final Gson gson, final TypeToken<?> typeToken, final JsonAdapter jsonAdapter) {
        final Class<?> value = jsonAdapter.value();
        if (TypeAdapter.class.isAssignableFrom(value)) {
            return constructorConstructor.get((TypeToken<TypeAdapter<?>>)TypeToken.get(value)).construct();
        }
        if (TypeAdapterFactory.class.isAssignableFrom(value)) {
            return constructorConstructor.get((TypeToken<TypeAdapterFactory>)TypeToken.get(value)).construct().create(gson, typeToken);
        }
        throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final JsonAdapter jsonAdapter = typeToken.getRawType().getAnnotation(JsonAdapter.class);
        if (jsonAdapter == null) {
            return null;
        }
        return (TypeAdapter<T>)getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
    }
}
