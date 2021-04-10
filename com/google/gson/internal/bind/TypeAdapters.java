package com.google.gson.internal.bind;

import java.util.concurrent.atomic.*;
import java.math.*;
import java.net.*;
import com.google.gson.stream.*;
import java.io.*;
import com.google.gson.reflect.*;
import java.sql.*;
import com.google.gson.*;
import java.util.*;
import com.google.gson.annotations.*;

public final class TypeAdapters
{
    public static final TypeAdapter<AtomicBoolean> ATOMIC_BOOLEAN;
    public static final TypeAdapterFactory ATOMIC_BOOLEAN_FACTORY;
    public static final TypeAdapter<AtomicInteger> ATOMIC_INTEGER;
    public static final TypeAdapter<AtomicIntegerArray> ATOMIC_INTEGER_ARRAY;
    public static final TypeAdapterFactory ATOMIC_INTEGER_ARRAY_FACTORY;
    public static final TypeAdapterFactory ATOMIC_INTEGER_FACTORY;
    public static final TypeAdapter<BigDecimal> BIG_DECIMAL;
    public static final TypeAdapter<BigInteger> BIG_INTEGER;
    public static final TypeAdapter<BitSet> BIT_SET;
    public static final TypeAdapterFactory BIT_SET_FACTORY;
    public static final TypeAdapter<Boolean> BOOLEAN;
    public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING;
    public static final TypeAdapterFactory BOOLEAN_FACTORY;
    public static final TypeAdapter<Number> BYTE;
    public static final TypeAdapterFactory BYTE_FACTORY;
    public static final TypeAdapter<Calendar> CALENDAR;
    public static final TypeAdapterFactory CALENDAR_FACTORY;
    public static final TypeAdapter<Character> CHARACTER;
    public static final TypeAdapterFactory CHARACTER_FACTORY;
    public static final TypeAdapter<Class> CLASS;
    public static final TypeAdapterFactory CLASS_FACTORY;
    public static final TypeAdapter<Currency> CURRENCY;
    public static final TypeAdapterFactory CURRENCY_FACTORY;
    public static final TypeAdapter<Number> DOUBLE;
    public static final TypeAdapterFactory ENUM_FACTORY;
    public static final TypeAdapter<Number> FLOAT;
    public static final TypeAdapter<InetAddress> INET_ADDRESS;
    public static final TypeAdapterFactory INET_ADDRESS_FACTORY;
    public static final TypeAdapter<Number> INTEGER;
    public static final TypeAdapterFactory INTEGER_FACTORY;
    public static final TypeAdapter<JsonElement> JSON_ELEMENT;
    public static final TypeAdapterFactory JSON_ELEMENT_FACTORY;
    public static final TypeAdapter<Locale> LOCALE;
    public static final TypeAdapterFactory LOCALE_FACTORY;
    public static final TypeAdapter<Number> LONG;
    public static final TypeAdapter<Number> NUMBER;
    public static final TypeAdapterFactory NUMBER_FACTORY;
    public static final TypeAdapter<Number> SHORT;
    public static final TypeAdapterFactory SHORT_FACTORY;
    public static final TypeAdapter<String> STRING;
    public static final TypeAdapter<StringBuffer> STRING_BUFFER;
    public static final TypeAdapterFactory STRING_BUFFER_FACTORY;
    public static final TypeAdapter<StringBuilder> STRING_BUILDER;
    public static final TypeAdapterFactory STRING_BUILDER_FACTORY;
    public static final TypeAdapterFactory STRING_FACTORY;
    public static final TypeAdapterFactory TIMESTAMP_FACTORY;
    public static final TypeAdapter<URI> URI;
    public static final TypeAdapterFactory URI_FACTORY;
    public static final TypeAdapter<URL> URL;
    public static final TypeAdapterFactory URL_FACTORY;
    public static final TypeAdapter<UUID> UUID;
    public static final TypeAdapterFactory UUID_FACTORY;
    
    static {
        CLASS_FACTORY = newFactory(Class.class, CLASS = new TypeAdapter<Class>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Class clazz) throws IOException {
                if (clazz == null) {
                    jsonWriter.nullValue();
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Attempted to serialize java.lang.Class: ");
                sb.append(clazz.getName());
                sb.append(". Forgot to register a type adapter?");
                throw new UnsupportedOperationException(sb.toString());
            }
        });
        BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET = new TypeAdapter<BitSet>() {
            @Override
            public void write(final JsonWriter jsonWriter, final BitSet set) throws IOException {
                throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
            }
        });
        BOOLEAN = new TypeAdapter<Boolean>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Boolean b) throws IOException {
                if (b == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.value(b);
            }
        };
        BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Boolean b) throws IOException {
                String string;
                if (b == null) {
                    string = "null";
                }
                else {
                    string = b.toString();
                }
                jsonWriter.value(string);
            }
        };
        BOOLEAN_FACTORY = newFactory(Boolean.TYPE, Boolean.class, TypeAdapters.BOOLEAN);
        BYTE = new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        BYTE_FACTORY = newFactory(Byte.TYPE, Byte.class, TypeAdapters.BYTE);
        SHORT = new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        SHORT_FACTORY = newFactory(Short.TYPE, Short.class, TypeAdapters.SHORT);
        INTEGER = new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        INTEGER_FACTORY = newFactory(Integer.TYPE, Integer.class, TypeAdapters.INTEGER);
        ATOMIC_INTEGER_FACTORY = newFactory(AtomicInteger.class, ATOMIC_INTEGER = new TypeAdapter<AtomicInteger>() {
            @Override
            public void write(final JsonWriter jsonWriter, final AtomicInteger atomicInteger) throws IOException {
                jsonWriter.value(atomicInteger.get());
            }
        }.nullSafe());
        ATOMIC_BOOLEAN_FACTORY = newFactory(AtomicBoolean.class, ATOMIC_BOOLEAN = new TypeAdapter<AtomicBoolean>() {
            @Override
            public void write(final JsonWriter jsonWriter, final AtomicBoolean atomicBoolean) throws IOException {
                jsonWriter.value(atomicBoolean.get());
            }
        }.nullSafe());
        ATOMIC_INTEGER_ARRAY_FACTORY = newFactory(AtomicIntegerArray.class, ATOMIC_INTEGER_ARRAY = new TypeAdapter<AtomicIntegerArray>() {
            @Override
            public void write(final JsonWriter jsonWriter, final AtomicIntegerArray atomicIntegerArray) throws IOException {
                jsonWriter.beginArray();
                for (int length = atomicIntegerArray.length(), i = 0; i < length; ++i) {
                    jsonWriter.value(atomicIntegerArray.get(i));
                }
                jsonWriter.endArray();
            }
        }.nullSafe());
        LONG = new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        FLOAT = new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        DOUBLE = new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        };
        NUMBER_FACTORY = newFactory(Number.class, NUMBER = new TypeAdapter<Number>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                jsonWriter.value(n);
            }
        });
        CHARACTER = new TypeAdapter<Character>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Character c) throws IOException {
                String value;
                if (c == null) {
                    value = null;
                }
                else {
                    value = String.valueOf(c);
                }
                jsonWriter.value(value);
            }
        };
        CHARACTER_FACTORY = newFactory(Character.TYPE, Character.class, TypeAdapters.CHARACTER);
        STRING = new TypeAdapter<String>() {
            @Override
            public void write(final JsonWriter jsonWriter, final String s) throws IOException {
                jsonWriter.value(s);
            }
        };
        BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
            @Override
            public void write(final JsonWriter jsonWriter, final BigDecimal bigDecimal) throws IOException {
                jsonWriter.value(bigDecimal);
            }
        };
        BIG_INTEGER = new TypeAdapter<BigInteger>() {
            @Override
            public void write(final JsonWriter jsonWriter, final BigInteger bigInteger) throws IOException {
                jsonWriter.value(bigInteger);
            }
        };
        STRING_FACTORY = newFactory(String.class, TypeAdapters.STRING);
        STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER = new TypeAdapter<StringBuilder>() {
            @Override
            public void write(final JsonWriter jsonWriter, final StringBuilder sb) throws IOException {
                String string;
                if (sb == null) {
                    string = null;
                }
                else {
                    string = sb.toString();
                }
                jsonWriter.value(string);
            }
        });
        STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER = new TypeAdapter<StringBuffer>() {
            @Override
            public void write(final JsonWriter jsonWriter, final StringBuffer sb) throws IOException {
                String string;
                if (sb == null) {
                    string = null;
                }
                else {
                    string = sb.toString();
                }
                jsonWriter.value(string);
            }
        });
        URL_FACTORY = newFactory(URL.class, URL = new TypeAdapter<URL>() {
            @Override
            public void write(final JsonWriter jsonWriter, final URL url) throws IOException {
                String externalForm;
                if (url == null) {
                    externalForm = null;
                }
                else {
                    externalForm = url.toExternalForm();
                }
                jsonWriter.value(externalForm);
            }
        });
        URI_FACTORY = newFactory(URI.class, URI = new TypeAdapter<URI>() {
            @Override
            public void write(final JsonWriter jsonWriter, final URI uri) throws IOException {
                String asciiString;
                if (uri == null) {
                    asciiString = null;
                }
                else {
                    asciiString = uri.toASCIIString();
                }
                jsonWriter.value(asciiString);
            }
        });
        INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS = new TypeAdapter<InetAddress>() {
            @Override
            public void write(final JsonWriter jsonWriter, final InetAddress inetAddress) throws IOException {
                String hostAddress;
                if (inetAddress == null) {
                    hostAddress = null;
                }
                else {
                    hostAddress = inetAddress.getHostAddress();
                }
                jsonWriter.value(hostAddress);
            }
        });
        UUID_FACTORY = newFactory(UUID.class, UUID = new TypeAdapter<UUID>() {
            @Override
            public void write(final JsonWriter jsonWriter, final UUID uuid) throws IOException {
                String string;
                if (uuid == null) {
                    string = null;
                }
                else {
                    string = uuid.toString();
                }
                jsonWriter.value(string);
            }
        });
        CURRENCY_FACTORY = newFactory(Currency.class, CURRENCY = new TypeAdapter<Currency>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Currency currency) throws IOException {
                jsonWriter.value(currency.getCurrencyCode());
            }
        }.nullSafe());
        TIMESTAMP_FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.getRawType() != Timestamp.class) {
                    return null;
                }
                return (TypeAdapter<T>)new TypeAdapter<Timestamp>() {
                    final /* synthetic */ TypeAdapter val$dateTypeAdapter = gson.getAdapter(Date.class);
                    
                    @Override
                    public void write(final JsonWriter jsonWriter, final Timestamp timestamp) throws IOException {
                        this.val$dateTypeAdapter.write(jsonWriter, timestamp);
                    }
                };
            }
        };
        CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, CALENDAR = new TypeAdapter<Calendar>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Calendar calendar) throws IOException {
                if (calendar == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.beginObject();
                jsonWriter.name("year");
                jsonWriter.value(calendar.get(1));
                jsonWriter.name("month");
                jsonWriter.value(calendar.get(2));
                jsonWriter.name("dayOfMonth");
                jsonWriter.value(calendar.get(5));
                jsonWriter.name("hourOfDay");
                jsonWriter.value(calendar.get(11));
                jsonWriter.name("minute");
                jsonWriter.value(calendar.get(12));
                jsonWriter.name("second");
                jsonWriter.value(calendar.get(13));
                jsonWriter.endObject();
            }
        });
        LOCALE_FACTORY = newFactory(Locale.class, LOCALE = new TypeAdapter<Locale>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Locale locale) throws IOException {
                String string;
                if (locale == null) {
                    string = null;
                }
                else {
                    string = locale.toString();
                }
                jsonWriter.value(string);
            }
        });
        JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT = new TypeAdapter<JsonElement>() {
            @Override
            public void write(final JsonWriter jsonWriter, final JsonElement jsonElement) throws IOException {
                if (jsonElement == null || jsonElement.isJsonNull()) {
                    jsonWriter.nullValue();
                    return;
                }
                if (jsonElement.isJsonPrimitive()) {
                    final JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
                    if (asJsonPrimitive.isNumber()) {
                        jsonWriter.value(asJsonPrimitive.getAsNumber());
                        return;
                    }
                    if (asJsonPrimitive.isBoolean()) {
                        jsonWriter.value(asJsonPrimitive.getAsBoolean());
                        return;
                    }
                    jsonWriter.value(asJsonPrimitive.getAsString());
                }
                else {
                    if (jsonElement.isJsonArray()) {
                        jsonWriter.beginArray();
                        final Iterator<JsonElement> iterator = jsonElement.getAsJsonArray().iterator();
                        while (iterator.hasNext()) {
                            this.write(jsonWriter, iterator.next());
                        }
                        jsonWriter.endArray();
                        return;
                    }
                    if (jsonElement.isJsonObject()) {
                        jsonWriter.beginObject();
                        for (final Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                            jsonWriter.name(entry.getKey());
                            this.write(jsonWriter, entry.getValue());
                        }
                        jsonWriter.endObject();
                        return;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Couldn't write ");
                    sb.append(jsonElement.getClass());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
        });
        ENUM_FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                final Class<? super T> rawType = typeToken.getRawType();
                if (Enum.class.isAssignableFrom(rawType) && rawType != Enum.class) {
                    Class<? super T> superclass = rawType;
                    if (!rawType.isEnum()) {
                        superclass = rawType.getSuperclass();
                    }
                    return new EnumTypeAdapter<T>((Class<T>)superclass);
                }
                return null;
            }
        };
    }
    
    public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> typeToken, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.equals(typeToken)) {
                    return (TypeAdapter<T>)typeAdapter;
                }
                return null;
            }
        };
    }
    
    public static <TT> TypeAdapterFactory newFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.getRawType() == clazz) {
                    return (TypeAdapter<T>)typeAdapter;
                }
                return null;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Factory[type=");
                sb.append(clazz.getName());
                sb.append(",adapter=");
                sb.append(typeAdapter);
                sb.append("]");
                return sb.toString();
            }
        };
    }
    
    public static <TT> TypeAdapterFactory newFactory(final Class<TT> clazz, final Class<TT> clazz2, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                final Class<? super T> rawType = typeToken.getRawType();
                if (rawType != clazz && rawType != clazz2) {
                    return null;
                }
                return (TypeAdapter<T>)typeAdapter;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Factory[type=");
                sb.append(clazz2.getName());
                sb.append("+");
                sb.append(clazz.getName());
                sb.append(",adapter=");
                sb.append(typeAdapter);
                sb.append("]");
                return sb.toString();
            }
        };
    }
    
    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> clazz, final Class<? extends TT> clazz2, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                final Class<? super T> rawType = typeToken.getRawType();
                if (rawType != clazz && rawType != clazz2) {
                    return null;
                }
                return (TypeAdapter<T>)typeAdapter;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Factory[type=");
                sb.append(clazz.getName());
                sb.append("+");
                sb.append(clazz2.getName());
                sb.append(",adapter=");
                sb.append(typeAdapter);
                sb.append("]");
                return sb.toString();
            }
        };
    }
    
    public static <T1> TypeAdapterFactory newTypeHierarchyFactory(final Class<T1> clazz, final TypeAdapter<T1> typeAdapter) {
        return new TypeAdapterFactory() {
            @Override
            public <T2> TypeAdapter<T2> create(final Gson gson, final TypeToken<T2> typeToken) {
                final Class<? super T2> rawType = typeToken.getRawType();
                if (!clazz.isAssignableFrom(rawType)) {
                    return null;
                }
                return (TypeAdapter<T2>)new TypeAdapter<T1>() {
                    @Override
                    public void write(final JsonWriter jsonWriter, final T1 t1) throws IOException {
                        typeAdapter.write(jsonWriter, t1);
                    }
                };
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Factory[typeHierarchy=");
                sb.append(clazz.getName());
                sb.append(",adapter=");
                sb.append(typeAdapter);
                sb.append("]");
                return sb.toString();
            }
        };
    }
    
    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T>
    {
        private final Map<T, String> constantToName;
        private final Map<String, T> nameToConstant;
        
        public EnumTypeAdapter(final Class<T> clazz) {
            this.nameToConstant = new HashMap<String, T>();
            this.constantToName = new HashMap<T, String>();
            try {
                final T[] array = clazz.getEnumConstants();
                for (int length = array.length, i = 0; i < length; ++i) {
                    final Enum<T> enum1 = array[i];
                    String name = enum1.name();
                    final SerializedName serializedName = clazz.getField(name).getAnnotation(SerializedName.class);
                    if (serializedName != null) {
                        final String value = serializedName.value();
                        final String[] alternate = serializedName.alternate();
                        final int length2 = alternate.length;
                        int n = 0;
                        while (true) {
                            name = value;
                            if (n >= length2) {
                                break;
                            }
                            this.nameToConstant.put(alternate[n], (T)enum1);
                            ++n;
                        }
                    }
                    this.nameToConstant.put(name, (T)enum1);
                    this.constantToName.put((T)enum1, name);
                }
            }
            catch (NoSuchFieldException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Missing field in ");
                sb.append(clazz.getName());
                final AssertionError assertionError = new AssertionError((Object)sb.toString());
                assertionError.initCause(ex);
                throw assertionError;
            }
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final T t) throws IOException {
            String s;
            if (t == null) {
                s = null;
            }
            else {
                s = this.constantToName.get(t);
            }
            jsonWriter.value(s);
        }
    }
}
