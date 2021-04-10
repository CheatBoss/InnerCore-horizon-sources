package com.google.gson;

import com.google.gson.reflect.*;
import com.google.gson.stream.*;
import java.io.*;
import com.google.gson.internal.*;

final class TreeTypeAdapter<T> extends TypeAdapter<T>
{
    private TypeAdapter<T> delegate;
    private final JsonDeserializer<T> deserializer;
    private final Gson gson;
    private final JsonSerializer<T> serializer;
    private final TypeAdapterFactory skipPast;
    private final TypeToken<T> typeToken;
    
    private TreeTypeAdapter(final JsonSerializer<T> serializer, final JsonDeserializer<T> deserializer, final Gson gson, final TypeToken<T> typeToken, final TypeAdapterFactory skipPast) {
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.gson = gson;
        this.typeToken = typeToken;
        this.skipPast = skipPast;
    }
    
    private TypeAdapter<T> delegate() {
        final TypeAdapter<T> delegate = this.delegate;
        if (delegate != null) {
            return delegate;
        }
        return this.delegate = this.gson.getDelegateAdapter(this.skipPast, this.typeToken);
    }
    
    public static TypeAdapterFactory newFactory(final TypeToken<?> typeToken, final Object o) {
        return new SingleTypeFactory(o, (TypeToken)typeToken, false, (Class)null);
    }
    
    public static TypeAdapterFactory newFactoryWithMatchRawType(final TypeToken<?> typeToken, final Object o) {
        return new SingleTypeFactory(o, (TypeToken)typeToken, typeToken.getType() == typeToken.getRawType(), (Class)null);
    }
    
    public static TypeAdapterFactory newTypeHierarchyFactory(final Class<?> clazz, final Object o) {
        return new SingleTypeFactory(o, (TypeToken)null, false, (Class)clazz);
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final T t) throws IOException {
        final JsonSerializer<T> serializer = this.serializer;
        if (serializer == null) {
            this.delegate().write(jsonWriter, t);
            return;
        }
        if (t == null) {
            jsonWriter.nullValue();
            return;
        }
        Streams.write(serializer.serialize(t, this.typeToken.getType(), this.gson.serializationContext), jsonWriter);
    }
    
    private static class SingleTypeFactory implements TypeAdapterFactory
    {
        private final JsonDeserializer<?> deserializer;
        private final TypeToken<?> exactType;
        private final Class<?> hierarchyType;
        private final boolean matchRawType;
        private final JsonSerializer<?> serializer;
        
        private SingleTypeFactory(final Object o, final TypeToken<?> exactType, final boolean matchRawType, final Class<?> hierarchyType) {
            final boolean b = o instanceof JsonSerializer;
            final JsonDeserializer<?> jsonDeserializer = null;
            JsonSerializer<?> serializer;
            if (b) {
                serializer = (JsonSerializer<?>)o;
            }
            else {
                serializer = null;
            }
            this.serializer = serializer;
            JsonDeserializer<?> deserializer = jsonDeserializer;
            if (o instanceof JsonDeserializer) {
                deserializer = (JsonDeserializer<?>)o;
            }
            this.deserializer = deserializer;
            $Gson$Preconditions.checkArgument(this.serializer != null || deserializer != null);
            this.exactType = exactType;
            this.matchRawType = matchRawType;
            this.hierarchyType = hierarchyType;
        }
        
        @Override
        public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
            final TypeToken<?> exactType = this.exactType;
            boolean assignable;
            if (exactType != null) {
                assignable = (exactType.equals(typeToken) || (this.matchRawType && this.exactType.getType() == typeToken.getRawType()));
            }
            else {
                assignable = this.hierarchyType.isAssignableFrom(typeToken.getRawType());
            }
            if (assignable) {
                return new TreeTypeAdapter<T>(this.serializer, this.deserializer, gson, typeToken, this, null);
            }
            return null;
        }
    }
}
