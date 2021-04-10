package com.google.gson;

import com.google.gson.reflect.*;
import com.google.gson.internal.*;
import java.lang.reflect.*;
import java.util.concurrent.atomic.*;
import java.math.*;
import com.google.gson.internal.bind.*;
import com.google.gson.stream.*;
import java.io.*;
import java.util.*;

public final class Gson
{
    private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls;
    private final ConstructorConstructor constructorConstructor;
    final JsonDeserializationContext deserializationContext;
    private final List<TypeAdapterFactory> factories;
    private final boolean generateNonExecutableJson;
    private final boolean htmlSafe;
    private final boolean prettyPrinting;
    final JsonSerializationContext serializationContext;
    private final boolean serializeNulls;
    private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache;
    
    public Gson() {
        this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, Collections.emptyMap(), false, false, false, true, false, false, LongSerializationPolicy.DEFAULT, Collections.emptyList());
    }
    
    Gson(final Excluder excluder, final FieldNamingStrategy fieldNamingStrategy, final Map<Type, InstanceCreator<?>> map, final boolean serializeNulls, final boolean b, final boolean generateNonExecutableJson, final boolean htmlSafe, final boolean prettyPrinting, final boolean b2, final LongSerializationPolicy longSerializationPolicy, final List<TypeAdapterFactory> list) {
        this.calls = new ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>>();
        this.typeTokenCache = Collections.synchronizedMap(new HashMap<TypeToken<?>, TypeAdapter<?>>());
        this.deserializationContext = new JsonDeserializationContext() {};
        this.serializationContext = new JsonSerializationContext() {};
        this.constructorConstructor = new ConstructorConstructor(map);
        this.serializeNulls = serializeNulls;
        this.generateNonExecutableJson = generateNonExecutableJson;
        this.htmlSafe = htmlSafe;
        this.prettyPrinting = prettyPrinting;
        final ArrayList<TypeAdapterFactory> list2 = new ArrayList<TypeAdapterFactory>();
        list2.add(TypeAdapters.JSON_ELEMENT_FACTORY);
        list2.add(ObjectTypeAdapter.FACTORY);
        list2.add(excluder);
        list2.addAll((Collection<?>)list);
        list2.add(TypeAdapters.STRING_FACTORY);
        list2.add(TypeAdapters.INTEGER_FACTORY);
        list2.add(TypeAdapters.BOOLEAN_FACTORY);
        list2.add(TypeAdapters.BYTE_FACTORY);
        list2.add(TypeAdapters.SHORT_FACTORY);
        final TypeAdapter<Number> longAdapter = longAdapter(longSerializationPolicy);
        list2.add(TypeAdapters.newFactory(Long.TYPE, Long.class, longAdapter));
        list2.add(TypeAdapters.newFactory(Double.TYPE, Double.class, this.doubleAdapter(b2)));
        list2.add(TypeAdapters.newFactory(Float.TYPE, Float.class, this.floatAdapter(b2)));
        list2.add(TypeAdapters.NUMBER_FACTORY);
        list2.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        list2.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        list2.add(TypeAdapters.newFactory(AtomicLong.class, atomicLongAdapter(longAdapter)));
        list2.add(TypeAdapters.newFactory(AtomicLongArray.class, atomicLongArrayAdapter(longAdapter)));
        list2.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        list2.add(TypeAdapters.CHARACTER_FACTORY);
        list2.add(TypeAdapters.STRING_BUILDER_FACTORY);
        list2.add(TypeAdapters.STRING_BUFFER_FACTORY);
        list2.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        list2.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        list2.add(TypeAdapters.URL_FACTORY);
        list2.add(TypeAdapters.URI_FACTORY);
        list2.add(TypeAdapters.UUID_FACTORY);
        list2.add(TypeAdapters.CURRENCY_FACTORY);
        list2.add(TypeAdapters.LOCALE_FACTORY);
        list2.add(TypeAdapters.INET_ADDRESS_FACTORY);
        list2.add(TypeAdapters.BIT_SET_FACTORY);
        list2.add(DateTypeAdapter.FACTORY);
        list2.add(TypeAdapters.CALENDAR_FACTORY);
        list2.add(TimeTypeAdapter.FACTORY);
        list2.add(SqlDateTypeAdapter.FACTORY);
        list2.add(TypeAdapters.TIMESTAMP_FACTORY);
        list2.add(ArrayTypeAdapter.FACTORY);
        list2.add(TypeAdapters.CLASS_FACTORY);
        list2.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
        list2.add(new MapTypeAdapterFactory(this.constructorConstructor, b));
        list2.add(new JsonAdapterAnnotationTypeAdapterFactory(this.constructorConstructor));
        list2.add(TypeAdapters.ENUM_FACTORY);
        list2.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingStrategy, excluder));
        this.factories = (List<TypeAdapterFactory>)Collections.unmodifiableList((List<?>)list2);
    }
    
    private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLong>() {
            @Override
            public void write(final JsonWriter jsonWriter, final AtomicLong atomicLong) throws IOException {
                typeAdapter.write(jsonWriter, atomicLong.get());
            }
        }.nullSafe();
    }
    
    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLongArray>() {
            @Override
            public void write(final JsonWriter jsonWriter, final AtomicLongArray atomicLongArray) throws IOException {
                jsonWriter.beginArray();
                for (int length = atomicLongArray.length(), i = 0; i < length; ++i) {
                    typeAdapter.write(jsonWriter, atomicLongArray.get(i));
                }
                jsonWriter.endArray();
            }
        }.nullSafe();
    }
    
    private void checkValidFloatingPoint(final double n) {
        if (!Double.isNaN(n) && !Double.isInfinite(n)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(" is not a valid double value as per JSON specification. To override this");
        sb.append(" behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private TypeAdapter<Number> doubleAdapter(final boolean b) {
        if (b) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                if (n == null) {
                    jsonWriter.nullValue();
                    return;
                }
                Gson.this.checkValidFloatingPoint(n.doubleValue());
                jsonWriter.value(n);
            }
        };
    }
    
    private TypeAdapter<Number> floatAdapter(final boolean b) {
        if (b) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                if (n == null) {
                    jsonWriter.nullValue();
                    return;
                }
                Gson.this.checkValidFloatingPoint(n.floatValue());
                jsonWriter.value(n);
            }
        };
    }
    
    private static TypeAdapter<Number> longAdapter(final LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                if (n == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.value(n.toString());
            }
        };
    }
    
    public <T> TypeAdapter<T> getAdapter(final TypeToken<T> typeToken) {
        final TypeAdapter<?> typeAdapter = this.typeTokenCache.get(typeToken);
        if (typeAdapter != null) {
            return (TypeAdapter<T>)typeAdapter;
        }
        final Map<TypeToken<?>, FutureTypeAdapter<?>> map = this.calls.get();
        boolean b = false;
        Map<TypeToken<T>, FutureTypeAdapter<?>> map2;
        if ((map2 = (Map<TypeToken<T>, FutureTypeAdapter<?>>)map) == null) {
            map2 = (Map<TypeToken<T>, FutureTypeAdapter<?>>)new HashMap<TypeToken<T>, FutureTypeAdapter<Object>>();
            this.calls.set((Map<TypeToken<?>, FutureTypeAdapter<?>>)map2);
            b = true;
        }
        final FutureTypeAdapter<?> futureTypeAdapter = map2.get(typeToken);
        if (futureTypeAdapter != null) {
            return (TypeAdapter<T>)futureTypeAdapter;
        }
        try {
            final FutureTypeAdapter<Object> futureTypeAdapter2 = new FutureTypeAdapter<Object>();
            map2.put(typeToken, futureTypeAdapter2);
            final Iterator<TypeAdapterFactory> iterator = this.factories.iterator();
            while (iterator.hasNext()) {
                final TypeAdapter<?> create = iterator.next().create(this, (TypeToken<?>)typeToken);
                if (create != null) {
                    futureTypeAdapter2.setDelegate(create);
                    this.typeTokenCache.put(typeToken, create);
                    return (TypeAdapter<T>)create;
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("GSON cannot handle ");
            sb.append(typeToken);
            throw new IllegalArgumentException(sb.toString());
        }
        finally {
            map2.remove(typeToken);
            if (b) {
                this.calls.remove();
            }
        }
    }
    
    public <T> TypeAdapter<T> getAdapter(final Class<T> clazz) {
        return this.getAdapter((TypeToken<T>)TypeToken.get((Class<T>)clazz));
    }
    
    public <T> TypeAdapter<T> getDelegateAdapter(final TypeAdapterFactory typeAdapterFactory, final TypeToken<T> typeToken) {
        int n = (this.factories.contains(typeAdapterFactory) ^ true) ? 1 : 0;
        for (final TypeAdapterFactory typeAdapterFactory2 : this.factories) {
            if (n == 0) {
                if (typeAdapterFactory2 != typeAdapterFactory) {
                    continue;
                }
                n = 1;
            }
            else {
                final TypeAdapter<T> create = typeAdapterFactory2.create(this, typeToken);
                if (create != null) {
                    return create;
                }
                continue;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("GSON cannot serialize ");
        sb.append(typeToken);
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{serializeNulls:");
        sb.append(this.serializeNulls);
        sb.append("factories:");
        sb.append(this.factories);
        sb.append(",instanceCreators:");
        sb.append(this.constructorConstructor);
        sb.append("}");
        return sb.toString();
    }
    
    static class FutureTypeAdapter<T> extends TypeAdapter<T>
    {
        private TypeAdapter<T> delegate;
        
        public void setDelegate(final TypeAdapter<T> delegate) {
            if (this.delegate == null) {
                this.delegate = delegate;
                return;
            }
            throw new AssertionError();
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final T t) throws IOException {
            final TypeAdapter<T> delegate = this.delegate;
            if (delegate != null) {
                delegate.write(jsonWriter, t);
                return;
            }
            throw new IllegalStateException();
        }
    }
}
