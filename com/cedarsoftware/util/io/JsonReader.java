package com.cedarsoftware.util.io;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.math.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class JsonReader implements Closeable
{
    private static Map<Class, JsonClassReaderBase> BASE_READERS;
    public static final String CLASSLOADER = "CLASSLOADER";
    public static final String CUSTOM_READER_MAP = "CUSTOM_READERS";
    public static final String FAIL_ON_UNKNOWN_TYPE = "FAIL_ON_UNKNOWN_TYPE";
    public static final String JSON_READER = "JSON_READER";
    public static final String MISSING_FIELD_HANDLER = "MISSING_FIELD_HANDLER";
    public static final String NOT_CUSTOM_READER_MAP = "NOT_CUSTOM_READERS";
    public static final String OBJECT_RESOLVER = "OBJECT_RESOLVER";
    public static final String TYPE_NAME_MAP = "TYPE_NAME_MAP";
    static final String TYPE_NAME_MAP_REVERSE = "TYPE_NAME_MAP_REVERSE";
    public static final String UNKNOWN_OBJECT = "UNKNOWN_OBJECT";
    public static final String USE_MAPS = "USE_MAPS";
    private static final Map<String, Factory> factory;
    private final Map<String, Object> args;
    private final FastPushbackReader input;
    protected MissingFieldHandler missingFieldHandler;
    protected final Set<Class> notCustom;
    private final Map<Long, JsonObject> objsRead;
    protected final Map<Class, JsonClassReaderBase> readers;
    
    static {
        factory = new ConcurrentHashMap<String, Factory>();
        final CollectionFactory collectionFactory = new CollectionFactory();
        assignInstantiator(Collection.class, (Factory)collectionFactory);
        assignInstantiator(List.class, (Factory)collectionFactory);
        assignInstantiator(Set.class, (Factory)collectionFactory);
        assignInstantiator(SortedSet.class, (Factory)collectionFactory);
        final MapFactory mapFactory = new MapFactory();
        assignInstantiator(Map.class, (Factory)mapFactory);
        assignInstantiator(SortedMap.class, (Factory)mapFactory);
        final HashMap<Class<String>, Readers.SqlDateReader> base_READERS = (HashMap<Class<String>, Readers.SqlDateReader>)new HashMap<Class<StringBuffer>, Readers.StringBufferReader>();
        base_READERS.put((Class<StringBuffer>)String.class, (Readers.StringBufferReader)new Readers.StringReader());
        base_READERS.put((Class<StringBuffer>)Date.class, (Readers.StringBufferReader)new Readers.DateReader());
        base_READERS.put((Class<StringBuffer>)AtomicBoolean.class, (Readers.StringBufferReader)new Readers.AtomicBooleanReader());
        base_READERS.put((Class<StringBuffer>)AtomicInteger.class, (Readers.StringBufferReader)new Readers.AtomicIntegerReader());
        base_READERS.put((Class<StringBuffer>)AtomicLong.class, (Readers.StringBufferReader)new Readers.AtomicLongReader());
        base_READERS.put((Class<StringBuffer>)BigInteger.class, (Readers.StringBufferReader)new Readers.BigIntegerReader());
        base_READERS.put((Class<StringBuffer>)BigDecimal.class, (Readers.StringBufferReader)new Readers.BigDecimalReader());
        base_READERS.put((Class<StringBuffer>)java.sql.Date.class, (Readers.StringBufferReader)new Readers.SqlDateReader());
        base_READERS.put((Class<StringBuffer>)Timestamp.class, (Readers.StringBufferReader)new Readers.TimestampReader());
        base_READERS.put((Class<StringBuffer>)Calendar.class, (Readers.StringBufferReader)new Readers.CalendarReader());
        base_READERS.put((Class<StringBuffer>)TimeZone.class, (Readers.StringBufferReader)new Readers.TimeZoneReader());
        base_READERS.put((Class<StringBuffer>)Locale.class, (Readers.StringBufferReader)new Readers.LocaleReader());
        base_READERS.put((Class<StringBuffer>)Class.class, (Readers.StringBufferReader)new Readers.ClassReader());
        base_READERS.put((Class<StringBuffer>)StringBuilder.class, (Readers.StringBufferReader)new Readers.StringBuilderReader());
        base_READERS.put(StringBuffer.class, new Readers.StringBufferReader());
        JsonReader.BASE_READERS = (Map<Class, JsonClassReaderBase>)base_READERS;
    }
    
    public JsonReader() {
        this.readers = new HashMap<Class, JsonClassReaderBase>(JsonReader.BASE_READERS);
        this.notCustom = new HashSet<Class>();
        this.objsRead = new HashMap<Long, JsonObject>();
        this.args = new HashMap<String, Object>();
        this.input = null;
        this.getArgs().put("USE_MAPS", false);
        this.getArgs().put("CLASSLOADER", JsonReader.class.getClassLoader());
    }
    
    public JsonReader(final InputStream inputStream) {
        this(inputStream, false);
    }
    
    public JsonReader(final InputStream inputStream, final Map<String, Object> map) {
        this.readers = new HashMap<Class, JsonClassReaderBase>(JsonReader.BASE_READERS);
        this.notCustom = new HashSet<Class>();
        this.objsRead = new HashMap<Long, JsonObject>();
        this.args = new HashMap<String, Object>();
        this.initializeFromArgs(map);
        try {
            this.input = new FastPushbackBufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            throw new JsonIoException("Your JVM does not support UTF-8.  Get a better JVM.", ex);
        }
    }
    
    public JsonReader(final InputStream inputStream, final boolean b) {
        this(inputStream, makeArgMap(new HashMap<String, Object>(), b));
    }
    
    public JsonReader(final String s, final Map<String, Object> map) {
        this.readers = new HashMap<Class, JsonClassReaderBase>(JsonReader.BASE_READERS);
        this.notCustom = new HashSet<Class>();
        this.objsRead = new HashMap<Long, JsonObject>();
        this.args = new HashMap<String, Object>();
        this.initializeFromArgs(map);
        try {
            this.input = new FastPushbackBufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes("UTF-8")), "UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            throw new JsonIoException("Could not convert JSON to Maps because your JVM does not support UTF-8", ex);
        }
    }
    
    public JsonReader(final Map<String, Object> map) {
        this(new ByteArrayInputStream(new byte[0]), map);
    }
    
    public JsonReader(final byte[] array, final Map<String, Object> map) {
        this.readers = new HashMap<Class, JsonClassReaderBase>(JsonReader.BASE_READERS);
        this.notCustom = new HashSet<Class>();
        this.objsRead = new HashMap<Long, JsonObject>();
        this.args = new HashMap<String, Object>();
        this.initializeFromArgs(map);
        try {
            this.input = new FastPushbackBufferedReader(new InputStreamReader(new ByteArrayInputStream(array), "UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            throw new JsonIoException("Could not convert JSON to Maps because your JVM does not support UTF-8", ex);
        }
    }
    
    public static void addReaderPermanent(final Class clazz, final JsonClassReaderBase jsonClassReaderBase) {
        JsonReader.BASE_READERS.put(clazz, jsonClassReaderBase);
    }
    
    private static Map adjustOutputMap(final Object o) {
        if (o instanceof Map) {
            return (Map)o;
        }
        if (o != null && o.getClass().isArray()) {
            final JsonObject<String, Object> jsonObject = new JsonObject<String, Object>();
            jsonObject.put("@items", o);
            return jsonObject;
        }
        final JsonObject<String, Object[]> jsonObject2 = new JsonObject<String, Object[]>();
        jsonObject2.put("@items", new Object[] { o });
        return jsonObject2;
    }
    
    public static void assignInstantiator(final Class clazz, final Factory factory) {
        assignInstantiator(clazz.getName(), factory);
    }
    
    public static void assignInstantiator(final String s, final Factory factory) {
        JsonReader.factory.put(s, factory);
    }
    
    private String getErrorMessage(final String s) {
        if (this.input != null) {
            final StringBuilder sb = new StringBuilder(String.valueOf(s));
            sb.append("\nLast read: ");
            sb.append(this.input.getLastSnippet());
            sb.append("\nline: ");
            sb.append(this.input.getLine());
            sb.append(", col: ");
            sb.append(this.input.getCol());
            return sb.toString();
        }
        return s;
    }
    
    private void initializeFromArgs(final Map<String, Object> map) {
        Map<? extends String, ? extends MissingFieldHandler> map2 = (Map<? extends String, ? extends MissingFieldHandler>)map;
        if (map == null) {
            map2 = (Map<? extends String, ? extends MissingFieldHandler>)new HashMap<String, HashMap<K, String>>();
        }
        final Map<String, Object> args = this.getArgs();
        args.putAll(map2);
        args.put("JSON_READER", this);
        if (!args.containsKey("CLASSLOADER")) {
            args.put("CLASSLOADER", JsonReader.class.getClassLoader());
        }
        final HashMap<K, String> hashMap = args.get("TYPE_NAME_MAP");
        if (hashMap != null) {
            final HashMap<Object, String> hashMap2 = new HashMap<Object, String>();
            for (final Map.Entry<K, String> entry : hashMap.entrySet()) {
                hashMap2.put(entry.getValue(), (String)entry.getKey());
            }
            args.put("TYPE_NAME_MAP_REVERSE", hashMap2);
        }
        this.setMissingFieldHandler((MissingFieldHandler)args.get("MISSING_FIELD_HANDLER"));
        final HashMap<K, String> hashMap3 = args.get("CUSTOM_READERS");
        if (hashMap3 != null) {
            for (final Map.Entry<K, String> entry2 : hashMap3.entrySet()) {
                this.addReader((Class)entry2.getKey(), (JsonClassReaderBase)entry2.getValue());
            }
        }
        final Iterable<Class> iterable = args.get("NOT_CUSTOM_READERS");
        if (iterable != null) {
            final Iterator<Class> iterator3 = iterable.iterator();
            while (iterator3.hasNext()) {
                this.addNotCustomReader(iterator3.next());
            }
        }
    }
    
    public static Object jsonToJava(final InputStream inputStream, final Map<String, Object> map) {
        Map<String, Object> map2 = map;
        if (map == null) {
            map2 = new HashMap<String, Object>();
            map2.put("USE_MAPS", false);
        }
        if (!map2.containsKey("USE_MAPS")) {
            map2.put("USE_MAPS", false);
        }
        final JsonReader jsonReader = new JsonReader(inputStream, map2);
        final Object object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }
    
    public static Object jsonToJava(final String s) {
        return jsonToJava(s, null);
    }
    
    public static Object jsonToJava(final String s, final Map<String, Object> map) {
        Map<String, Object> map2 = map;
        if (map == null) {
            map2 = new HashMap<String, Object>();
            map2.put("USE_MAPS", false);
        }
        if (!map2.containsKey("USE_MAPS")) {
            map2.put("USE_MAPS", false);
        }
        final JsonReader jsonReader = new JsonReader(s, map2);
        final Object object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }
    
    public static Map jsonToMaps(final InputStream inputStream, final Map<String, Object> map) {
        Map<String, Object> map2 = map;
        if (map == null) {
            map2 = new HashMap<String, Object>();
        }
        map2.put("USE_MAPS", true);
        final JsonReader jsonReader = new JsonReader(inputStream, map2);
        final Object object = jsonReader.readObject();
        jsonReader.close();
        return adjustOutputMap(object);
    }
    
    public static Map jsonToMaps(final String s) {
        return jsonToMaps(s, null);
    }
    
    public static Map jsonToMaps(final String s, final Map<String, Object> map) {
        Map<String, Object> map2 = map;
        Label_0017: {
            if (map != null) {
                break Label_0017;
            }
            try {
                map2 = new HashMap<String, Object>();
                map2.put("USE_MAPS", true);
                final JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(s.getBytes("UTF-8")), map2);
                final Object object = jsonReader.readObject();
                jsonReader.close();
                return adjustOutputMap(object);
            }
            catch (UnsupportedEncodingException ex) {
                throw new JsonIoException("Could not convert JSON to Maps because your JVM does not support UTF-8", ex);
            }
        }
    }
    
    static Map makeArgMap(final Map<String, Object> map, final boolean b) {
        map.put("USE_MAPS", b);
        return map;
    }
    
    public static Object newInstance(final Class clazz) {
        if (JsonReader.factory.containsKey(clazz.getName())) {
            return ((ClassFactory)JsonReader.factory.get(clazz.getName())).newInstance(clazz);
        }
        return MetaUtils.newInstance(clazz);
    }
    
    public static Object newInstance(final Class clazz, final JsonObject jsonObject) {
        if (!JsonReader.factory.containsKey(clazz.getName())) {
            return MetaUtils.newInstance(clazz);
        }
        final Factory factory = JsonReader.factory.get(clazz.getName());
        if (factory instanceof ClassFactoryEx) {
            final HashMap<String, JsonObject> hashMap = new HashMap<String, JsonObject>();
            hashMap.put("jsonObj", jsonObject);
            return ((ClassFactoryEx)factory).newInstance(clazz, hashMap);
        }
        if (factory instanceof ClassFactory) {
            return ((ClassFactory)factory).newInstance(clazz);
        }
        final StringBuilder sb = new StringBuilder("Unknown instantiator (Factory) class.  Must subclass ClassFactoryEx or ClassFactory, found: ");
        sb.append(((ClassFactory)factory).getClass().getName());
        throw new JsonIoException(sb.toString());
    }
    
    public void addNotCustomReader(final Class clazz) {
        this.notCustom.add(clazz);
    }
    
    public void addReader(final Class clazz, final JsonClassReaderBase jsonClassReaderBase) {
        this.readers.put(clazz, jsonClassReaderBase);
    }
    
    @Override
    public void close() {
        try {
            if (this.input != null) {
                this.input.close();
            }
        }
        catch (Exception ex) {
            throw new JsonIoException("Unable to close input", ex);
        }
    }
    
    protected Object convertParsedMapsToJava(final JsonObject ex) {
        try {
            Resolver resolver;
            if (this.useMaps()) {
                resolver = new MapResolver(this);
            }
            else {
                resolver = new ObjectResolver(this, this.args.get("CLASSLOADER"));
            }
            resolver.createJavaObjectInstance(Object.class, (JsonObject)ex);
            final Object convertMapsToObjects = resolver.convertMapsToObjects((JsonObject<String, Object>)ex);
            resolver.cleanup();
            this.readers.clear();
            return convertMapsToObjects;
        }
        catch (Exception ex) {
            try {
                this.close();
            }
            catch (Exception ex2) {}
            if (ex instanceof JsonIoException) {
                throw (JsonIoException)ex;
            }
            throw new JsonIoException(this.getErrorMessage(ex.getMessage()), ex);
        }
    }
    
    public Map<String, Object> getArgs() {
        return this.args;
    }
    
    ClassLoader getClassLoader() {
        return this.args.get("CLASSLOADER");
    }
    
    MissingFieldHandler getMissingFieldHandler() {
        return this.missingFieldHandler;
    }
    
    public Map<Long, JsonObject> getObjectsRead() {
        return this.objsRead;
    }
    
    public Object getRefTarget(JsonObject jsonObject) {
        if (!jsonObject.isReference()) {
            return jsonObject;
        }
        jsonObject = this.objsRead.get(jsonObject.getReferenceId());
        if (jsonObject == null) {
            throw new IllegalStateException("The JSON input had an @ref to an object that does not exist.");
        }
        return this.getRefTarget(jsonObject);
    }
    
    public Object jsonObjectsToJava(final JsonObject jsonObject) {
        this.getArgs().put("USE_MAPS", false);
        return this.convertParsedMapsToJava(jsonObject);
    }
    
    public Object readObject() {
        final JsonParser jsonParser = new JsonParser(this.input, this.objsRead, this.getArgs());
        final JsonObject<String, String> jsonObject = new JsonObject<String, String>();
        try {
            final Object value = jsonParser.readValue(jsonObject);
            if (value == "~!o~") {
                return new JsonObject();
            }
            Object o;
            if (value instanceof Object[]) {
                jsonObject.setType(Object[].class.getName());
                jsonObject.setTarget(value);
                jsonObject.put("@items", (String)value);
                o = this.convertParsedMapsToJava(jsonObject);
            }
            else if (value instanceof JsonObject) {
                o = this.convertParsedMapsToJava((JsonObject)value);
            }
            else {
                o = value;
            }
            if (this.useMaps()) {
                return value;
            }
            return o;
        }
        catch (Exception ex) {
            throw new JsonIoException("error parsing JSON value", ex);
        }
        catch (JsonIoException ex2) {
            throw ex2;
        }
    }
    
    public void setMissingFieldHandler(final MissingFieldHandler missingFieldHandler) {
        this.missingFieldHandler = missingFieldHandler;
    }
    
    protected boolean useMaps() {
        return Boolean.TRUE.equals(this.getArgs().get("USE_MAPS"));
    }
    
    public interface ClassFactory extends Factory
    {
        Object newInstance(final Class p0);
    }
    
    public interface ClassFactoryEx extends Factory
    {
        Object newInstance(final Class p0, final Map p1);
    }
    
    public static class CollectionFactory implements ClassFactory
    {
        @Override
        public Object newInstance(final Class clazz) {
            if (List.class.isAssignableFrom(clazz)) {
                return new ArrayList();
            }
            if (SortedSet.class.isAssignableFrom(clazz)) {
                return new TreeSet();
            }
            if (Set.class.isAssignableFrom(clazz)) {
                return new LinkedHashSet();
            }
            if (Collection.class.isAssignableFrom(clazz)) {
                return new ArrayList();
            }
            final StringBuilder sb = new StringBuilder("CollectionFactory handed Class for which it was not expecting: ");
            sb.append(clazz.getName());
            throw new JsonIoException(sb.toString());
        }
    }
    
    public interface Factory
    {
    }
    
    public interface JsonClassReader extends JsonClassReaderBase
    {
        Object read(final Object p0, final Deque<JsonObject<String, Object>> p1);
    }
    
    public interface JsonClassReaderBase
    {
    }
    
    public interface JsonClassReaderEx extends JsonClassReaderBase
    {
        Object read(final Object p0, final Deque<JsonObject<String, Object>> p1, final Map<String, Object> p2);
        
        public static class Support
        {
            public static JsonReader getReader(final Map<String, Object> map) {
                return map.get("JSON_READER");
            }
        }
    }
    
    public static class MapFactory implements ClassFactory
    {
        @Override
        public Object newInstance(final Class clazz) {
            if (SortedMap.class.isAssignableFrom(clazz)) {
                return new TreeMap();
            }
            if (Map.class.isAssignableFrom(clazz)) {
                return new LinkedHashMap();
            }
            final StringBuilder sb = new StringBuilder("MapFactory handed Class for which it was not expecting: ");
            sb.append(clazz.getName());
            throw new JsonIoException(sb.toString());
        }
    }
    
    public interface MissingFieldHandler
    {
        void fieldMissing(final Object p0, final String p1, final Object p2);
    }
}
