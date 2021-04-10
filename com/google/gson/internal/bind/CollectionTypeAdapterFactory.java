package com.google.gson.internal.bind;

import com.google.gson.reflect.*;
import com.google.gson.*;
import com.google.gson.internal.*;
import java.lang.reflect.*;
import com.google.gson.stream.*;
import java.io.*;
import java.util.*;

public final class CollectionTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    
    public CollectionTypeAdapterFactory(final ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class<? super T> rawType = typeToken.getRawType();
        if (!Collection.class.isAssignableFrom(rawType)) {
            return null;
        }
        final Type collectionElementType = $Gson$Types.getCollectionElementType(type, rawType);
        return (TypeAdapter<T>)new Adapter(gson, collectionElementType, (TypeAdapter<Object>)gson.getAdapter(TypeToken.get(collectionElementType)), (ObjectConstructor<? extends Collection<Object>>)this.constructorConstructor.get((TypeToken<? extends Collection<E>>)typeToken));
    }
    
    private static final class Adapter<E> extends TypeAdapter<Collection<E>>
    {
        private final ObjectConstructor<? extends Collection<E>> constructor;
        private final TypeAdapter<E> elementTypeAdapter;
        
        public Adapter(final Gson gson, final Type type, final TypeAdapter<E> typeAdapter, final ObjectConstructor<? extends Collection<E>> constructor) {
            this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(gson, typeAdapter, type);
            this.constructor = constructor;
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final Collection<E> collection) throws IOException {
            if (collection == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginArray();
            final Iterator<E> iterator = collection.iterator();
            while (iterator.hasNext()) {
                this.elementTypeAdapter.write(jsonWriter, iterator.next());
            }
            jsonWriter.endArray();
        }
    }
}
