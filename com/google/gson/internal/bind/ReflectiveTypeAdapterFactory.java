package com.google.gson.internal.bind;

import com.google.gson.reflect.*;
import com.google.gson.*;
import java.lang.reflect.*;
import com.google.gson.stream.*;
import java.io.*;
import com.google.gson.annotations.*;
import com.google.gson.internal.*;
import java.util.*;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final FieldNamingStrategy fieldNamingPolicy;
    
    public ReflectiveTypeAdapterFactory(final ConstructorConstructor constructorConstructor, final FieldNamingStrategy fieldNamingPolicy, final Excluder excluder) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
    }
    
    private BoundField createBoundField(final Gson gson, final Field field, final String s, final TypeToken<?> typeToken, final boolean b, final boolean b2) {
        return (BoundField)new BoundField(s, b, b2) {
            final TypeAdapter<?> typeAdapter = ReflectiveTypeAdapterFactory.this.getFieldAdapter(gson, field, typeToken);
            final /* synthetic */ boolean val$isPrimitive = Primitives.isPrimitive(typeToken.getRawType());
            
            @Override
            void write(final JsonWriter jsonWriter, Object value) throws IOException, IllegalAccessException {
                value = field.get(value);
                new TypeAdapterRuntimeTypeWrapper<Object>(gson, this.typeAdapter, typeToken.getType()).write(jsonWriter, value);
            }
            
            public boolean writeField(final Object o) throws IOException, IllegalAccessException {
                final boolean serialized = this.serialized;
                boolean b = false;
                if (!serialized) {
                    return false;
                }
                if (field.get(o) != o) {
                    b = true;
                }
                return b;
            }
        };
    }
    
    static boolean excludeField(final Field field, final boolean b, final Excluder excluder) {
        return !excluder.excludeClass(field.getType(), b) && !excluder.excludeField(field, b);
    }
    
    private Map<String, BoundField> getBoundFields(final Gson gson, final TypeToken<?> typeToken, Class<?> rawType) {
        final LinkedHashMap<String, BoundField> linkedHashMap = new LinkedHashMap<String, BoundField>();
        if (rawType.isInterface()) {
            return linkedHashMap;
        }
        final Type type = typeToken.getType();
        for (TypeToken<?> value = typeToken; rawType != Object.class; rawType = value.getRawType()) {
            final Field[] declaredFields = rawType.getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                boolean excludeField = this.excludeField(field, true);
                final boolean excludeField2 = this.excludeField(field, false);
                if (excludeField || excludeField2) {
                    field.setAccessible(true);
                    final Type resolve = $Gson$Types.resolve(value.getType(), rawType, field.getGenericType());
                    final List<String> fieldNames = this.getFieldNames(field);
                    BoundField boundField = null;
                    for (int j = 0; j < fieldNames.size(); ++j) {
                        final String s = fieldNames.get(j);
                        if (j != 0) {
                            excludeField = false;
                        }
                        final BoundField boundField2 = linkedHashMap.put(s, this.createBoundField(gson, field, s, TypeToken.get(resolve), excludeField, excludeField2));
                        if (boundField == null) {
                            boundField = boundField2;
                        }
                    }
                    if (boundField != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(type);
                        sb.append(" declares multiple JSON fields named ");
                        sb.append(boundField.name);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
            }
            value = TypeToken.get($Gson$Types.resolve(value.getType(), rawType, rawType.getGenericSuperclass()));
        }
        return linkedHashMap;
    }
    
    private TypeAdapter<?> getFieldAdapter(final Gson gson, final Field field, final TypeToken<?> typeToken) {
        final JsonAdapter jsonAdapter = field.getAnnotation(JsonAdapter.class);
        if (jsonAdapter != null) {
            final TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
            if (typeAdapter != null) {
                return typeAdapter;
            }
        }
        return gson.getAdapter(typeToken);
    }
    
    static List<String> getFieldName(final FieldNamingStrategy fieldNamingStrategy, final Field field) {
        final SerializedName serializedName = field.getAnnotation(SerializedName.class);
        final LinkedList<String> list = new LinkedList<String>();
        if (serializedName == null) {
            list.add(fieldNamingStrategy.translateName(field));
            return list;
        }
        list.add(serializedName.value());
        final String[] alternate = serializedName.alternate();
        for (int length = alternate.length, i = 0; i < length; ++i) {
            list.add(alternate[i]);
        }
        return list;
    }
    
    private List<String> getFieldNames(final Field field) {
        return getFieldName(this.fieldNamingPolicy, field);
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Class<? super T> rawType = typeToken.getRawType();
        if (!Object.class.isAssignableFrom(rawType)) {
            return null;
        }
        return new Adapter<T>((ObjectConstructor)this.constructorConstructor.get(typeToken), (Map)this.getBoundFields(gson, typeToken, rawType));
    }
    
    public boolean excludeField(final Field field, final boolean b) {
        return excludeField(field, b, this.excluder);
    }
    
    public static final class Adapter<T> extends TypeAdapter<T>
    {
        private final Map<String, BoundField> boundFields;
        private final ObjectConstructor<T> constructor;
        
        private Adapter(final ObjectConstructor<T> constructor, final Map<String, BoundField> boundFields) {
            this.constructor = constructor;
            this.boundFields = boundFields;
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final T t) throws IOException {
            if (t == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            try {
                for (final BoundField boundField : this.boundFields.values()) {
                    if (boundField.writeField(t)) {
                        jsonWriter.name(boundField.name);
                        boundField.write(jsonWriter, t);
                    }
                }
                jsonWriter.endObject();
            }
            catch (IllegalAccessException ex) {
                throw new AssertionError((Object)ex);
            }
        }
    }
    
    abstract static class BoundField
    {
        final boolean deserialized;
        final String name;
        final boolean serialized;
        
        protected BoundField(final String name, final boolean serialized, final boolean deserialized) {
            this.name = name;
            this.serialized = serialized;
            this.deserialized = deserialized;
        }
        
        abstract void write(final JsonWriter p0, final Object p1) throws IOException, IllegalAccessException;
        
        abstract boolean writeField(final Object p0) throws IOException, IllegalAccessException;
    }
}
