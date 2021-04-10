package com.cedarsoftware.util.io;

import java.util.concurrent.atomic.*;
import java.math.*;
import java.sql.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class JsonWriter implements Closeable, Flushable
{
    private static Map<Class, JsonClassWriterBase> BASE_WRITERS;
    public static final String CLASSLOADER = "CLASSLOADER";
    public static final String CUSTOM_WRITER_MAP = "CUSTOM_WRITERS";
    public static final String DATE_FORMAT = "DATE_FORMAT";
    public static final String ENUM_PUBLIC_ONLY = "ENUM_PUBLIC_ONLY";
    private static final String FIELD_BLACK_LIST = "FIELD_BLACK_LIST";
    public static final String FIELD_NAME_BLACK_LIST = "FIELD_NAME_BLACK_LIST";
    public static final String FIELD_SPECIFIERS = "FIELD_SPECIFIERS";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String NEW_LINE;
    public static final String NOT_CUSTOM_WRITER_MAP = "NOT_CUSTOM_WRITERS";
    public static final String PRETTY_PRINT = "PRETTY_PRINT";
    public static final String SHORT_META_KEYS = "SHORT_META_KEYS";
    public static final String SKIP_NULL_FIELDS = "SKIP_NULL";
    public static final String TYPE = "TYPE";
    public static final String TYPE_NAME_MAP = "TYPE_NAME_MAP";
    public static final String WRITE_LONGS_AS_STRINGS = "WLAS";
    private static final Long ZERO;
    private static final Object[] byteStrings;
    private static final NullClass nullWriter;
    private boolean alwaysShowType;
    final Map<String, Object> args;
    private int depth;
    private long identity;
    private boolean isEnumPublicOnly;
    private boolean isPrettyPrint;
    private boolean neverShowType;
    private final Set<Class> notCustom;
    private final Map<Object, Long> objVisited;
    private final Map<Object, Long> objsReferenced;
    private final Writer out;
    private boolean shortMetaKeys;
    private boolean skipNullFields;
    private Map<String, String> typeNameMap;
    private boolean writeLongsAsStrings;
    private final Map<Class, JsonClassWriterBase> writerCache;
    private final Map<Class, JsonClassWriterBase> writers;
    
    static {
        byteStrings = new Object[256];
        NEW_LINE = System.getProperty("line.separator");
        ZERO = 0L;
        nullWriter = new NullClass();
        for (int i = -128; i <= 127; i = (short)(i + 1)) {
            JsonWriter.byteStrings[i + 128] = Integer.toString(i).toCharArray();
        }
        final HashMap<Class<String>, Writers.JsonStringWriter> base_WRITERS = new HashMap<Class<String>, Writers.JsonStringWriter>();
        base_WRITERS.put(String.class, new Writers.JsonStringWriter());
        base_WRITERS.put((Class<String>)Date.class, (Writers.JsonStringWriter)new Writers.DateWriter());
        base_WRITERS.put((Class<String>)AtomicBoolean.class, (Writers.JsonStringWriter)new Writers.AtomicBooleanWriter());
        base_WRITERS.put((Class<String>)AtomicInteger.class, (Writers.JsonStringWriter)new Writers.AtomicIntegerWriter());
        base_WRITERS.put((Class<String>)AtomicLong.class, (Writers.JsonStringWriter)new Writers.AtomicLongWriter());
        base_WRITERS.put((Class<String>)BigInteger.class, (Writers.JsonStringWriter)new Writers.BigIntegerWriter());
        base_WRITERS.put((Class<String>)BigDecimal.class, (Writers.JsonStringWriter)new Writers.BigDecimalWriter());
        base_WRITERS.put((Class<String>)java.sql.Date.class, (Writers.JsonStringWriter)new Writers.DateWriter());
        base_WRITERS.put((Class<String>)Timestamp.class, (Writers.JsonStringWriter)new Writers.TimestampWriter());
        base_WRITERS.put((Class<String>)Calendar.class, (Writers.JsonStringWriter)new Writers.CalendarWriter());
        base_WRITERS.put((Class<String>)TimeZone.class, (Writers.JsonStringWriter)new Writers.TimeZoneWriter());
        base_WRITERS.put((Class<String>)Locale.class, (Writers.JsonStringWriter)new Writers.LocaleWriter());
        base_WRITERS.put((Class<String>)Class.class, (Writers.JsonStringWriter)new Writers.ClassWriter());
        base_WRITERS.put((Class<String>)StringBuilder.class, (Writers.JsonStringWriter)new Writers.StringBuilderWriter());
        base_WRITERS.put((Class<String>)StringBuffer.class, (Writers.JsonStringWriter)new Writers.StringBufferWriter());
        JsonWriter.BASE_WRITERS = (Map<Class, JsonClassWriterBase>)base_WRITERS;
    }
    
    public JsonWriter(final OutputStream outputStream) {
        this(outputStream, null);
    }
    
    public JsonWriter(final OutputStream outputStream, Map<String, Object> hashMap) {
        this.writers = new HashMap<Class, JsonClassWriterBase>(JsonWriter.BASE_WRITERS);
        this.writerCache = new HashMap<Class, JsonClassWriterBase>();
        this.notCustom = new HashSet<Class>();
        this.objVisited = new IdentityHashMap<Object, Long>();
        this.objsReferenced = new IdentityHashMap<Object, Long>();
        this.typeNameMap = null;
        boolean neverShowType = false;
        this.shortMetaKeys = false;
        this.neverShowType = false;
        this.alwaysShowType = false;
        this.isPrettyPrint = false;
        this.isEnumPublicOnly = false;
        this.writeLongsAsStrings = false;
        this.skipNullFields = false;
        this.identity = 1L;
        this.depth = 0;
        this.args = new HashMap<String, Object>();
        if (hashMap == null) {
            hashMap = new HashMap<String, Object>();
        }
        this.args.putAll(hashMap);
        this.args.put("JSON_WRITER", this);
        this.typeNameMap = this.args.get("TYPE_NAME_MAP");
        this.shortMetaKeys = isTrue(this.args.get("SHORT_META_KEYS"));
        this.alwaysShowType = isTrue(this.args.get("TYPE"));
        if (Boolean.FALSE.equals(this.args.get("TYPE")) || "false".equals(this.args.get("TYPE"))) {
            neverShowType = true;
        }
        this.neverShowType = neverShowType;
        this.isPrettyPrint = isTrue(this.args.get("PRETTY_PRINT"));
        this.isEnumPublicOnly = isTrue(this.args.get("ENUM_PUBLIC_ONLY"));
        this.writeLongsAsStrings = isTrue(this.args.get("WLAS"));
        this.writeLongsAsStrings = isTrue(this.args.get("WLAS"));
        this.skipNullFields = isTrue(this.args.get("SKIP_NULL"));
        if (!this.args.containsKey("CLASSLOADER")) {
            this.args.put("CLASSLOADER", JsonWriter.class.getClassLoader());
        }
        final Map<Class, V> map = this.args.get("CUSTOM_WRITERS");
        if (map != null) {
            for (final Map.Entry<Class, V> entry : map.entrySet()) {
                this.addWriter(entry.getKey(), (JsonClassWriterBase)entry.getValue());
            }
        }
        final Collection<Class> collection = this.args.get("NOT_CUSTOM_WRITERS");
        if (collection != null) {
            final Iterator<Class> iterator2 = collection.iterator();
            while (iterator2.hasNext()) {
                this.addNotCustomWriter(iterator2.next());
            }
        }
        if (hashMap.containsKey("FIELD_SPECIFIERS")) {
            final Map<Class, V> map2 = this.args.get("FIELD_SPECIFIERS");
            final HashMap<Class, ArrayList<Field>> hashMap2 = new HashMap<Class, ArrayList<Field>>();
            for (final Map.Entry<Class, V> entry2 : map2.entrySet()) {
                final Class clazz = entry2.getKey();
                final List list = (List)entry2.getValue();
                final ArrayList list2 = new ArrayList<Field>(list.size());
                final Map<String, Field> deepDeclaredFields = MetaUtils.getDeepDeclaredFields(clazz);
                for (final String s : list) {
                    final Field field = deepDeclaredFields.get(s);
                    if (field == null) {
                        final StringBuilder sb = new StringBuilder("Unable to locate field: ");
                        sb.append(s);
                        sb.append(" on class: ");
                        sb.append(clazz.getName());
                        sb.append(". Make sure the fields in the FIELD_SPECIFIERS map existing on the associated class.");
                        throw new JsonIoException(sb.toString());
                    }
                    list2.add(field);
                }
                hashMap2.put(clazz, (ArrayList<Field>)list2);
            }
            this.args.put("FIELD_SPECIFIERS", hashMap2);
        }
        else {
            this.args.put("FIELD_SPECIFIERS", new HashMap());
        }
        if (hashMap.containsKey("FIELD_NAME_BLACK_LIST")) {
            final Map<Class, V> map3 = this.args.get("FIELD_NAME_BLACK_LIST");
            final HashMap<Class, ArrayList<Field>> hashMap3 = new HashMap<Class, ArrayList<Field>>();
            for (final Map.Entry<Class, V> entry3 : map3.entrySet()) {
                final Class clazz2 = entry3.getKey();
                final List list3 = (List)entry3.getValue();
                final ArrayList list4 = new ArrayList<Field>(list3.size());
                final Map<String, Field> deepDeclaredFields2 = MetaUtils.getDeepDeclaredFields(clazz2);
                for (final String s2 : list3) {
                    final Field field2 = deepDeclaredFields2.get(s2);
                    if (field2 == null) {
                        final StringBuilder sb2 = new StringBuilder("Unable to locate field: ");
                        sb2.append(s2);
                        sb2.append(" on class: ");
                        sb2.append(clazz2.getName());
                        sb2.append(". Make sure the fields in the FIELD_NAME_BLACK_LIST map existing on the associated class.");
                        throw new JsonIoException(sb2.toString());
                    }
                    list4.add(field2);
                }
                hashMap3.put(clazz2, (ArrayList<Field>)list4);
            }
            this.args.put("FIELD_BLACK_LIST", hashMap3);
        }
        else {
            this.args.put("FIELD_BLACK_LIST", new HashMap());
        }
        try {
            this.out = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            return;
        }
        catch (UnsupportedEncodingException ex) {}
        final UnsupportedEncodingException ex;
        throw new JsonIoException("UTF-8 not supported on your JVM.  Unable to convert object to JSON.", ex);
    }
    
    public static void addWriterPermanent(final Class clazz, final JsonClassWriterBase jsonClassWriterBase) {
        JsonWriter.BASE_WRITERS.put(clazz, jsonClassWriterBase);
    }
    
    private void beginCollection(final boolean b, final boolean b2) throws IOException {
        if (!b && !b2) {
            this.out.write(91);
        }
        else {
            this.out.write(44);
            this.newLine();
            final Writer out = this.out;
            String s;
            if (this.shortMetaKeys) {
                s = "\"@e\":[";
            }
            else {
                s = "\"@items\":[";
            }
            out.write(s);
        }
        this.tabIn();
    }
    
    private static boolean doesValueTypeMatchFieldType(final Class clazz, final String s, final Object o) {
        final boolean b = false;
        if (clazz != null) {
            final Field field = MetaUtils.getDeepDeclaredFields(clazz).get(s);
            boolean b2 = b;
            if (field != null) {
                b2 = b;
                if (o.getClass() == field.getType()) {
                    b2 = true;
                }
            }
            return b2;
        }
        return false;
    }
    
    private String doubleToString(final double n) {
        if (!Double.isNaN(n) && !Double.isInfinite(n)) {
            return Double.toString(n);
        }
        return "null";
    }
    
    public static boolean ensureJsonPrimitiveKeys(final Map map) {
        final Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            if (!(iterator.next() instanceof String)) {
                return false;
            }
        }
        return true;
    }
    
    private String floatToString(final float n) {
        if (!Float.isNaN(n) && !Float.isInfinite(n)) {
            return Float.toString(n);
        }
        return "null";
    }
    
    private JsonClassWriterBase forceGetCustomWriter(final Class clazz) {
        Object nullWriter = JsonWriter.nullWriter;
        int n = Integer.MAX_VALUE;
        for (final Map.Entry<Class, JsonClassWriterBase> entry : this.writers.entrySet()) {
            final Class clazz2 = entry.getKey();
            if (clazz2 == clazz) {
                return entry.getValue();
            }
            final int distance = MetaUtils.getDistance(clazz2, clazz);
            if (distance >= n) {
                continue;
            }
            n = distance;
            nullWriter = entry.getValue();
        }
        return (JsonClassWriterBase)nullWriter;
    }
    
    public static String formatJson(final String s) {
        return formatJson(s, null, null);
    }
    
    public static String formatJson(final String s, final Map map, final Map map2) {
        final HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        if (map != null) {
            hashMap.putAll(map);
        }
        hashMap.put("USE_MAPS", true);
        final Object jsonToJava = JsonReader.jsonToJava(s, (Map<String, Object>)hashMap);
        hashMap.clear();
        if (map2 != null) {
            hashMap.putAll(map2);
        }
        hashMap.put("PRETTY_PRINT", true);
        return objectToJson(jsonToJava, (Map<String, Object>)hashMap);
    }
    
    private JsonClassWriterBase getCustomWriter(final Class clazz) {
        JsonClassWriterBase forceGetCustomWriter;
        if ((forceGetCustomWriter = this.writerCache.get(clazz)) == null) {
            forceGetCustomWriter = this.forceGetCustomWriter(clazz);
            this.writerCache.put(clazz, forceGetCustomWriter);
        }
        if (forceGetCustomWriter == JsonWriter.nullWriter) {
            return null;
        }
        return forceGetCustomWriter;
    }
    
    private static List<Field> getFieldsUsingSpecifier(final Class clazz, final Map<Class, List<Field>> map) {
        final Iterator<Map.Entry<Class, List<Field>>> iterator = map.entrySet().iterator();
        int n = Integer.MAX_VALUE;
        List<Field> list = null;
        while (iterator.hasNext()) {
            final Map.Entry<Class, List<Field>> entry = iterator.next();
            final Class clazz2 = entry.getKey();
            if (clazz2 == clazz) {
                return entry.getValue();
            }
            final int distance = MetaUtils.getDistance(clazz2, clazz);
            if (distance >= n) {
                continue;
            }
            n = distance;
            list = entry.getValue();
        }
        return list;
    }
    
    private String getId(final Object o) {
        if (o instanceof JsonObject) {
            final long id = ((JsonObject)o).id;
            if (id != -1L) {
                return String.valueOf(id);
            }
        }
        final Long n = this.objsReferenced.get(o);
        if (n == null) {
            return null;
        }
        return Long.toString(n);
    }
    
    static boolean isTrue(final Object o) {
        if (o instanceof Boolean) {
            return Boolean.TRUE.equals(o);
        }
        if (o instanceof String) {
            return "true".equalsIgnoreCase((String)o);
        }
        return o instanceof Number && ((Number)o).intValue() != 0;
    }
    
    public static String objectToJson(final Object o) {
        return objectToJson(o, null);
    }
    
    public static String objectToJson(final Object o, final Map<String, Object> map) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final JsonWriter jsonWriter = new JsonWriter(byteArrayOutputStream, map);
            jsonWriter.write(o);
            jsonWriter.close();
            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        }
        catch (Exception ex) {
            throw new JsonIoException("Unable to convert object to JSON", ex);
        }
    }
    
    private void tab(final Writer writer, int i) throws IOException {
        if (!this.isPrettyPrint) {
            return;
        }
        writer.write(JsonWriter.NEW_LINE);
        this.depth += i;
        for (i = 0; i < this.depth; ++i) {
            writer.write("  ");
        }
    }
    
    private void writeArray(final Object o, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        final Class<?> class1 = o.getClass();
        final int length = Array.getLength(o);
        final boolean containsKey = this.objsReferenced.containsKey(o);
        final boolean b2 = b && !class1.equals(Object[].class);
        final Writer out = this.out;
        if (b2 || containsKey) {
            out.write(123);
            this.tabIn();
        }
        if (containsKey) {
            this.writeId(this.getId(o));
            out.write(44);
            this.newLine();
        }
        if (b2) {
            this.writeType(o, out);
            out.write(44);
            this.newLine();
        }
        if (length != 0) {
            if (!b2 && !containsKey) {
                out.write(91);
            }
            else {
                String s;
                if (this.shortMetaKeys) {
                    s = "\"@i\":[";
                }
                else {
                    s = "\"@items\":[";
                }
                out.write(s);
            }
            this.tabIn();
            final int n = length - 1;
            if (byte[].class == class1) {
                this.writeByteArray((byte[])o, n);
            }
            else if (char[].class == class1) {
                writeJsonUtf8String(new String((char[])o), out);
            }
            else if (short[].class == class1) {
                this.writeShortArray((short[])o, n);
            }
            else if (int[].class == class1) {
                this.writeIntArray((int[])o, n);
            }
            else if (long[].class == class1) {
                this.writeLongArray((long[])o, n);
            }
            else if (float[].class == class1) {
                this.writeFloatArray((float[])o, n);
            }
            else if (double[].class == class1) {
                this.writeDoubleArray((double[])o, n);
            }
            else if (boolean[].class == class1) {
                this.writeBooleanArray((boolean[])o, n);
            }
            else {
                final Class<?> componentType = o.getClass().getComponentType();
                final boolean primitive = MetaUtils.isPrimitive(componentType);
                for (int i = 0; i < length; ++i) {
                    final Object value = Array.get(o, i);
                    if (value == null) {
                        out.write("null");
                    }
                    else if (!this.writeArrayElementIfMatching(componentType, value, false, out)) {
                        if (!primitive && !(value instanceof Boolean) && !(value instanceof Long) && !(value instanceof Double)) {
                            if (this.neverShowType && MetaUtils.isPrimitive(value.getClass())) {
                                this.writePrimitive(value, false);
                            }
                            else {
                                this.writeImpl(value, value.getClass() != componentType || this.alwaysShowType);
                            }
                        }
                        else {
                            this.writePrimitive(value, value.getClass() != componentType);
                        }
                    }
                    if (i != n) {
                        out.write(44);
                        this.newLine();
                    }
                }
            }
            this.tabOut();
            out.write(93);
            if (b2 || containsKey) {
                this.tabOut();
                out.write(125);
            }
            return;
        }
        if (!b2 && !containsKey) {
            out.write("[]");
            return;
        }
        String s2;
        if (this.shortMetaKeys) {
            s2 = "\"@e\":[]";
        }
        else {
            s2 = "\"@items\":[]";
        }
        out.write(s2);
        this.tabOut();
        out.write(125);
    }
    
    private void writeBooleanArray(final boolean[] array, final int n) throws IOException {
        final Writer out = this.out;
        for (int i = 0; i < n; ++i) {
            String s;
            if (array[i]) {
                s = "true,";
            }
            else {
                s = "false,";
            }
            out.write(s);
        }
        out.write(Boolean.toString(array[n]));
    }
    
    private void writeByteArray(final byte[] array, final int n) throws IOException {
        final Writer out = this.out;
        final Object[] byteStrings = JsonWriter.byteStrings;
        for (int i = 0; i < n; ++i) {
            out.write((char[])byteStrings[array[i] + 128]);
            out.write(44);
        }
        out.write((char[])byteStrings[array[n] + 128]);
    }
    
    private void writeCollection(final Collection collection, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        final Writer out = this.out;
        final boolean containsKey = this.objsReferenced.containsKey(collection);
        final boolean empty = collection.isEmpty();
        if (!containsKey && !b) {
            if (empty) {
                out.write(91);
            }
        }
        else {
            out.write(123);
            this.tabIn();
        }
        this.writeIdAndTypeIfNeeded(collection, b, containsKey);
        if (!empty) {
            this.beginCollection(b, containsKey);
            this.writeElements(out, collection.iterator());
            this.tabOut();
            out.write(93);
            if (b || containsKey) {
                this.tabOut();
                out.write("}");
            }
            return;
        }
        if (!containsKey && !b) {
            out.write(93);
            return;
        }
        this.tabOut();
        out.write(125);
    }
    
    private void writeCollectionElement(final Object o) throws IOException {
        if (o == null) {
            this.out.write("null");
            return;
        }
        if (o instanceof Boolean || o instanceof Double) {
            this.writePrimitive(o, false);
            return;
        }
        if (o instanceof Long) {
            this.writePrimitive(o, this.writeLongsAsStrings);
            return;
        }
        if (o instanceof String) {
            writeJsonUtf8String((String)o, this.out);
            return;
        }
        if (this.neverShowType && MetaUtils.isPrimitive(o.getClass())) {
            this.writePrimitive(o, false);
            return;
        }
        this.writeImpl(o, true);
    }
    
    private void writeDoubleArray(final double[] array, final int n) throws IOException {
        final Writer out = this.out;
        for (int i = 0; i < n; ++i) {
            out.write(this.doubleToString(array[i]));
            out.write(44);
        }
        out.write(this.doubleToString(array[n]));
    }
    
    private void writeElements(final Writer writer, final Iterator iterator) throws IOException {
        while (iterator.hasNext()) {
            this.writeCollectionElement(iterator.next());
            if (iterator.hasNext()) {
                writer.write(44);
                this.newLine();
            }
        }
    }
    
    private boolean writeField(Object value, final boolean b, final String s, final Field field, final boolean b2) throws IOException {
        if (!b2 && (field.getModifiers() & 0x80) != 0x0) {
            return b;
        }
        final int modifiers = field.getModifiers();
        if (Enum.class.isAssignableFrom(field.getDeclaringClass()) && !"name".equals(field.getName())) {
            if (!Modifier.isPublic(modifiers) && this.isEnumPublicOnly) {
                return b;
            }
            if ("ordinal".equals(field.getName()) || "internal".equals(field.getName())) {
                return b;
            }
        }
        try {
            value = field.get(value);
        }
        catch (Exception ex) {
            value = null;
        }
        if (this.skipNullFields && value == null) {
            return b;
        }
        if (!b) {
            this.out.write(44);
            this.newLine();
        }
        writeJsonUtf8String(s, this.out);
        this.out.write(58);
        if (value == null) {
            this.out.write("null");
            return false;
        }
        final Class<?> type = field.getType();
        final boolean b3 = value.getClass() != type;
        if (!MetaUtils.isPrimitive(type) && (!this.neverShowType || !MetaUtils.isPrimitive(value.getClass()))) {
            this.writeImpl(value, b3 || this.alwaysShowType, true, true);
            return false;
        }
        this.writePrimitive(value, false);
        return false;
    }
    
    private void writeFloatArray(final float[] array, final int n) throws IOException {
        final Writer out = this.out;
        for (int i = 0; i < n; ++i) {
            out.write(this.floatToString(array[i]));
            out.write(44);
        }
        out.write(this.floatToString(array[n]));
    }
    
    private void writeId(String s) throws IOException {
        final Writer out = this.out;
        String s2;
        if (this.shortMetaKeys) {
            s2 = "\"@i\":";
        }
        else {
            s2 = "\"@id\":";
        }
        out.write(s2);
        final Writer out2 = this.out;
        if (s == null) {
            s = "0";
        }
        out2.write(s);
    }
    
    private void writeIdAndTypeIfNeeded(final Object o, boolean b, final boolean b2) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        if (b2) {
            this.writeId(this.getId(o));
        }
        if (b) {
            if (b2) {
                this.out.write(44);
                this.newLine();
            }
            this.writeType(o, this.out);
        }
    }
    
    private void writeIntArray(final int[] array, final int n) throws IOException {
        final Writer out = this.out;
        for (int i = 0; i < n; ++i) {
            out.write(Integer.toString(array[i]));
            out.write(44);
        }
        out.write(Integer.toString(array[n]));
    }
    
    private void writeJsonObjectArray(final JsonObject jsonObject, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        final int length = jsonObject.getLength();
        final String type = jsonObject.type;
        Class<Object[]> classForName;
        if (type != null && !Object[].class.getName().equals(type)) {
            classForName = (Class<Object[]>)MetaUtils.classForName(type, this.getClassLoader());
        }
        else {
            classForName = Object[].class;
        }
        final Writer out = this.out;
        final boolean b2 = Object[].class == classForName;
        final Class<?> componentType = classForName.getComponentType();
        final boolean b3 = this.objsReferenced.containsKey(jsonObject) && jsonObject.hasId();
        final boolean b4 = b && !b2;
        if (b4 || b3) {
            out.write(123);
            this.tabIn();
        }
        if (b3) {
            this.writeId(Long.toString(jsonObject.id));
            out.write(44);
            this.newLine();
        }
        if (b4) {
            String s;
            if (this.shortMetaKeys) {
                s = "\"@t\":\"";
            }
            else {
                s = "\"@type\":\"";
            }
            out.write(s);
            out.write(this.getSubstituteTypeName(classForName.getName()));
            out.write("\",");
            this.newLine();
        }
        if (length != 0) {
            if (!b4 && !b3) {
                out.write(91);
            }
            else {
                String s2;
                if (this.shortMetaKeys) {
                    s2 = "\"@e\":[";
                }
                else {
                    s2 = "\"@items\":[";
                }
                out.write(s2);
            }
            this.tabIn();
            final Object[] array = jsonObject.get("@items");
            for (int i = 0; i < length; ++i) {
                final Object o = array[i];
                if (o == null) {
                    out.write("null");
                }
                else if (Character.class != componentType && Character.TYPE != componentType) {
                    if (!(o instanceof Boolean) && !(o instanceof Long) && !(o instanceof Double)) {
                        if (this.neverShowType && MetaUtils.isPrimitive(((String)o).getClass())) {
                            this.writePrimitive(o, false);
                        }
                        else if (o instanceof String) {
                            writeJsonUtf8String((String)o, out);
                        }
                        else if (!this.writeArrayElementIfMatching(componentType, o, false, out)) {
                            this.writeImpl(o, ((String)o).getClass() != componentType || this.alwaysShowType);
                        }
                    }
                    else {
                        this.writePrimitive(o, ((String)o).getClass() != componentType);
                    }
                }
                else {
                    writeJsonUtf8String((String)o, out);
                }
                if (i != length - 1) {
                    out.write(44);
                    this.newLine();
                }
            }
            this.tabOut();
            out.write(93);
            if (b4 || b3) {
                this.tabOut();
                out.write(125);
            }
            return;
        }
        if (!b4 && !b3) {
            out.write("[]");
            return;
        }
        String s3;
        if (this.shortMetaKeys) {
            s3 = "\"@e\":[]";
        }
        else {
            s3 = "\"@items\":[]";
        }
        out.write(s3);
        this.tabOut();
        out.write("}");
    }
    
    private void writeJsonObjectCollection(final JsonObject jsonObject, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        final Class classForName = MetaUtils.classForName(jsonObject.type, this.getClassLoader());
        final boolean b2 = this.objsReferenced.containsKey(jsonObject) && jsonObject.hasId();
        final Writer out = this.out;
        final int length = jsonObject.getLength();
        if (b2 || b || length == 0) {
            out.write(123);
            this.tabIn();
        }
        if (b2) {
            this.writeId(String.valueOf(jsonObject.id));
        }
        if (b) {
            if (b2) {
                out.write(44);
                this.newLine();
            }
            String s;
            if (this.shortMetaKeys) {
                s = "\"@t\":\"";
            }
            else {
                s = "\"@type\":\"";
            }
            out.write(s);
            out.write(this.getSubstituteTypeName(classForName.getName()));
            out.write(34);
        }
        if (length == 0) {
            this.tabOut();
            out.write(125);
            return;
        }
        this.beginCollection(b, b2);
        final Object[] array = jsonObject.get("@items");
        for (int length2 = array.length, i = 0; i < length2; ++i) {
            this.writeCollectionElement(array[i]);
            if (i != length2 - 1) {
                out.write(44);
                this.newLine();
            }
        }
        this.tabOut();
        out.write("]");
        if (b || b2) {
            this.tabOut();
            out.write(125);
        }
    }
    
    private void writeJsonObjectMap(final JsonObject jsonObject, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        final boolean b2 = this.objsReferenced.containsKey(jsonObject) && jsonObject.hasId();
        final Writer out = this.out;
        out.write(123);
        this.tabIn();
        if (b2) {
            this.writeId(String.valueOf(jsonObject.getId()));
        }
        boolean b3;
        if (b3 = b) {
            if (b2) {
                out.write(44);
                this.newLine();
            }
            final String type = jsonObject.getType();
            if (type != null) {
                final Class classForName = MetaUtils.classForName(type, this.getClassLoader());
                String s;
                if (this.shortMetaKeys) {
                    s = "\"@t\":\"";
                }
                else {
                    s = "\"@type\":\"";
                }
                out.write(s);
                out.write(this.getSubstituteTypeName(classForName.getName()));
                out.write(34);
                b3 = b;
            }
            else {
                b3 = false;
            }
        }
        if (jsonObject.isEmpty()) {
            this.tabOut();
            out.write(125);
            return;
        }
        if (b3) {
            out.write(44);
            this.newLine();
        }
        String s2;
        if (this.shortMetaKeys) {
            s2 = "\"@k\":[";
        }
        else {
            s2 = "\"@keys\":[";
        }
        out.write(s2);
        this.tabIn();
        this.writeElements(out, jsonObject.keySet().iterator());
        this.tabOut();
        out.write("],");
        this.newLine();
        String s3;
        if (this.shortMetaKeys) {
            s3 = "\"@e\":[";
        }
        else {
            s3 = "\"@items\":[";
        }
        out.write(s3);
        this.tabIn();
        this.writeElements(out, jsonObject.values().iterator());
        this.tabOut();
        out.write(93);
        this.tabOut();
        out.write(125);
    }
    
    private boolean writeJsonObjectMapWithStringKeys(final JsonObject jsonObject, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        final boolean ensureJsonPrimitiveKeys = ensureJsonPrimitiveKeys(jsonObject);
        final boolean b2 = false;
        if (!ensureJsonPrimitiveKeys) {
            return false;
        }
        boolean b3 = b2;
        if (this.objsReferenced.containsKey(jsonObject)) {
            b3 = b2;
            if (jsonObject.hasId()) {
                b3 = true;
            }
        }
        final Writer out = this.out;
        out.write(123);
        this.tabIn();
        if (b3) {
            this.writeId(String.valueOf(jsonObject.getId()));
        }
        boolean b4;
        if (b4 = b) {
            if (b3) {
                out.write(44);
                this.newLine();
            }
            final String type = jsonObject.getType();
            if (type != null) {
                final Class classForName = MetaUtils.classForName(type, this.getClassLoader());
                String s;
                if (this.shortMetaKeys) {
                    s = "\"@t\":\"";
                }
                else {
                    s = "\"@type\":\"";
                }
                out.write(s);
                out.write(this.getSubstituteTypeName(classForName.getName()));
                out.write(34);
                b4 = b;
            }
            else {
                b4 = false;
            }
        }
        if (jsonObject.isEmpty()) {
            this.tabOut();
            out.write(125);
            return true;
        }
        if (b4) {
            out.write(44);
            this.newLine();
        }
        return this.writeMapBody(jsonObject.entrySet().iterator());
    }
    
    private void writeJsonObjectObject(final JsonObject jsonObject, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        final Writer out = this.out;
        final boolean b2 = this.objsReferenced.containsKey(jsonObject) && jsonObject.hasId();
        final boolean b3 = b && jsonObject.type != null;
        Class classForName = null;
        out.write(123);
        this.tabIn();
        if (b2) {
            this.writeId(String.valueOf(jsonObject.id));
        }
        if (b3) {
            if (b2) {
                out.write(44);
                this.newLine();
            }
            String s;
            if (this.shortMetaKeys) {
                s = "\"@t\":\"";
            }
            else {
                s = "\"@type\":\"";
            }
            out.write(s);
            out.write(this.getSubstituteTypeName(jsonObject.type));
            out.write(34);
            try {
                classForName = MetaUtils.classForName(jsonObject.type, this.getClassLoader());
            }
            catch (Exception ex) {
                classForName = null;
            }
        }
        if (jsonObject.isEmpty()) {
            this.tabOut();
            out.write(125);
            return;
        }
        if (b3 || b2) {
            out.write(44);
            this.newLine();
        }
        final Iterator<Map.Entry<String, V>> iterator = jsonObject.entrySet().iterator();
        int n = 1;
        while (iterator.hasNext()) {
            final Map.Entry<String, V> entry = iterator.next();
            if (this.skipNullFields && entry.getValue() == null) {
                continue;
            }
            if (n == 0) {
                out.write(44);
                this.newLine();
            }
            n = 0;
            final String s2 = entry.getKey();
            out.write(34);
            out.write(s2);
            out.write("\":");
            final V value = entry.getValue();
            if (value == null) {
                out.write("null");
            }
            else if (this.neverShowType && MetaUtils.isPrimitive(((String)value).getClass())) {
                this.writePrimitive(value, false);
            }
            else if (!(value instanceof BigDecimal) && !(value instanceof BigInteger)) {
                if (!(value instanceof Number) && !(value instanceof Boolean)) {
                    if (value instanceof String) {
                        writeJsonUtf8String((String)value, out);
                    }
                    else if (value instanceof Character) {
                        writeJsonUtf8String(String.valueOf(value), out);
                    }
                    else {
                        this.writeImpl(value, doesValueTypeMatchFieldType(classForName, s2, value) ^ true);
                    }
                }
                else {
                    out.write(value.toString());
                }
            }
            else {
                this.writeImpl(value, doesValueTypeMatchFieldType(classForName, s2, value) ^ true);
            }
        }
        this.tabOut();
        out.write(125);
    }
    
    public static void writeJsonUtf8String(final String s, final Writer writer) throws IOException {
        writer.write(34);
        for (int length = s.length(), i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 < ' ') {
                switch (char1) {
                    default: {
                        writer.write(String.format("\\u%04X", (int)char1));
                        break;
                    }
                    case 13: {
                        writer.write("\\r");
                        break;
                    }
                    case 12: {
                        writer.write("\\f");
                        break;
                    }
                    case 10: {
                        writer.write("\\n");
                        break;
                    }
                    case 9: {
                        writer.write("\\t");
                        break;
                    }
                    case 8: {
                        writer.write("\\b");
                        break;
                    }
                }
            }
            else if (char1 != '\\' && char1 != '\"') {
                writer.write(char1);
            }
            else {
                writer.write(92);
                writer.write(char1);
            }
        }
        writer.write(34);
    }
    
    private void writeLongArray(final long[] array, final int n) throws IOException {
        final Writer out = this.out;
        if (this.writeLongsAsStrings) {
            for (int i = 0; i < n; ++i) {
                out.write(34);
                out.write(Long.toString(array[i]));
                out.write(34);
                out.write(44);
            }
            out.write(34);
            out.write(Long.toString(array[n]));
            out.write(34);
            return;
        }
        for (int j = 0; j < n; ++j) {
            out.write(Long.toString(array[j]));
            out.write(44);
        }
        out.write(Long.toString(array[n]));
    }
    
    private void writeMap(final Map map, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        final Writer out = this.out;
        final boolean containsKey = this.objsReferenced.containsKey(map);
        out.write(123);
        this.tabIn();
        if (containsKey) {
            this.writeId(this.getId(map));
        }
        if (b) {
            if (containsKey) {
                out.write(44);
                this.newLine();
            }
            this.writeType(map, out);
        }
        if (map.isEmpty()) {
            this.tabOut();
            out.write(125);
            return;
        }
        if (b || containsKey) {
            out.write(44);
            this.newLine();
        }
        String s;
        if (this.shortMetaKeys) {
            s = "\"@k\":[";
        }
        else {
            s = "\"@keys\":[";
        }
        out.write(s);
        this.tabIn();
        this.writeElements(out, map.keySet().iterator());
        this.tabOut();
        out.write("],");
        this.newLine();
        String s2;
        if (this.shortMetaKeys) {
            s2 = "\"@e\":[";
        }
        else {
            s2 = "\"@items\":[";
        }
        out.write(s2);
        this.tabIn();
        this.writeElements(out, map.values().iterator());
        this.tabOut();
        out.write(93);
        this.tabOut();
        out.write(125);
    }
    
    private boolean writeMapBody(final Iterator iterator) throws IOException {
        final Writer out = this.out;
        while (iterator.hasNext()) {
            final Map.Entry<String, V> entry = iterator.next();
            writeJsonUtf8String(entry.getKey(), out);
            out.write(":");
            this.writeCollectionElement(entry.getValue());
            if (iterator.hasNext()) {
                out.write(44);
                this.newLine();
            }
        }
        this.tabOut();
        out.write(125);
        return true;
    }
    
    private boolean writeMapWithStringKeys(final Map map, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        if (!ensureJsonPrimitiveKeys(map)) {
            return false;
        }
        final boolean containsKey = this.objsReferenced.containsKey(map);
        this.out.write(123);
        this.tabIn();
        this.writeIdAndTypeIfNeeded(map, b, containsKey);
        if (map.isEmpty()) {
            this.tabOut();
            this.out.write(125);
            return true;
        }
        if (b || containsKey) {
            this.out.write(44);
            this.newLine();
        }
        return this.writeMapBody(map.entrySet().iterator());
    }
    
    private boolean writeOptionalReference(final Object o) throws IOException {
        if (o == null) {
            return false;
        }
        if (MetaUtils.isLogicalPrimitive(o.getClass())) {
            return false;
        }
        final Writer out = this.out;
        if (!this.objVisited.containsKey(o)) {
            this.objVisited.put(o, null);
            return false;
        }
        final String id = this.getId(o);
        if (id == null) {
            return false;
        }
        String s;
        if (this.shortMetaKeys) {
            s = "{\"@r\":";
        }
        else {
            s = "{\"@ref\":";
        }
        out.write(s);
        out.write(id);
        out.write(125);
        return true;
    }
    
    private void writePrimitive(final Object o, boolean b) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        if (o instanceof Character) {
            writeJsonUtf8String(String.valueOf(o), this.out);
            return;
        }
        if (o instanceof Long && this.writeLongsAsStrings) {
            if (b) {
                final Writer out = this.out;
                String s;
                if (this.shortMetaKeys) {
                    s = "{\"@t\":\"";
                }
                else {
                    s = "{\"@type\":\"";
                }
                out.write(s);
                this.out.write(this.getSubstituteTypeName("long"));
                this.out.write("\",\"value\":\"");
                this.out.write(o.toString());
                this.out.write("\"}");
                return;
            }
            this.out.write(34);
            this.out.write(o.toString());
            this.out.write(34);
        }
        else {
            if (o instanceof Double && (Double.isNaN((double)o) || Double.isInfinite((double)o))) {
                this.out.write("null");
                return;
            }
            if (o instanceof Float && (Float.isNaN((float)o) || Float.isInfinite((float)o))) {
                this.out.write("null");
                return;
            }
            this.out.write(o.toString());
        }
    }
    
    private void writeShortArray(final short[] array, final int n) throws IOException {
        final Writer out = this.out;
        for (int i = 0; i < n; ++i) {
            out.write(Integer.toString(array[i]));
            out.write(44);
        }
        out.write(Integer.toString(array[n]));
    }
    
    private void writeType(final Object o, final Writer writer) throws IOException {
        if (this.neverShowType) {
            return;
        }
        String s;
        if (this.shortMetaKeys) {
            s = "\"@t\":\"";
        }
        else {
            s = "\"@type\":\"";
        }
        writer.write(s);
        final Class<?> class1 = o.getClass();
        final String substituteTypeNameIfExists = this.getSubstituteTypeNameIfExists(class1.getName());
        if (substituteTypeNameIfExists != null) {
            writer.write(substituteTypeNameIfExists);
            writer.write(34);
            return;
        }
        final String name = class1.getName();
        if (name.equals("java.lang.Boolean")) {
            writer.write("boolean");
        }
        else if (name.equals("java.lang.Byte")) {
            writer.write("byte");
        }
        else if (name.equals("java.lang.Character")) {
            writer.write("char");
        }
        else if (name.equals("java.lang.Class")) {
            writer.write("class");
        }
        else if (name.equals("java.lang.Double")) {
            writer.write("double");
        }
        else if (name.equals("java.lang.Float")) {
            writer.write("float");
        }
        else if (name.equals("java.lang.Integer")) {
            writer.write("int");
        }
        else if (name.equals("java.lang.Long")) {
            writer.write("long");
        }
        else if (name.equals("java.lang.Short")) {
            writer.write("short");
        }
        else if (name.equals("java.lang.String")) {
            writer.write("string");
        }
        else if (name.equals("java.util.Date")) {
            writer.write("date");
        }
        else {
            writer.write(class1.getName());
        }
        writer.write(34);
    }
    
    public void addNotCustomWriter(final Class clazz) {
        this.notCustom.add(clazz);
    }
    
    public void addWriter(final Class clazz, final JsonClassWriterBase jsonClassWriterBase) {
        this.writers.put(clazz, jsonClassWriterBase);
    }
    
    @Override
    public void close() {
        try {
            this.out.close();
        }
        catch (Exception ex) {}
        this.writerCache.clear();
        this.writers.clear();
    }
    
    @Override
    public void flush() {
        try {
            if (this.out != null) {
                this.out.flush();
            }
        }
        catch (Exception ex) {}
    }
    
    ClassLoader getClassLoader() {
        return this.args.get("CLASSLOADER");
    }
    
    public Map getObjectsReferenced() {
        return this.objsReferenced;
    }
    
    public Map getObjectsVisited() {
        return this.objVisited;
    }
    
    protected String getSubstituteTypeName(final String s) {
        if (this.typeNameMap == null) {
            return s;
        }
        final String s2 = this.typeNameMap.get(s);
        if (s2 == null) {
            return s;
        }
        return s2;
    }
    
    protected String getSubstituteTypeNameIfExists(final String s) {
        if (this.typeNameMap == null) {
            return null;
        }
        return this.typeNameMap.get(s);
    }
    
    public void newLine() throws IOException {
        this.tab(this.out, 0);
    }
    
    public void tabIn() throws IOException {
        this.tab(this.out, 1);
    }
    
    public void tabOut() throws IOException {
        this.tab(this.out, -1);
    }
    
    protected void traceFields(final Deque<Object> deque, final Object o, Map<Class, List<Field>> iterator) {
        Collection<Field> collection;
        final List<Field> list = (List<Field>)(collection = getFieldsUsingSpecifier(o.getClass(), (Map<Class, List<Field>>)iterator));
        if (list == null) {
            collection = MetaUtils.getDeepDeclaredFields(o.getClass()).values();
        }
        iterator = collection.iterator();
        while (iterator.hasNext()) {
            final Field field = iterator.next();
            if ((field.getModifiers() & 0x80) != 0x0) {
                if (list == null) {
                    continue;
                }
                if (!list.contains(field)) {
                    continue;
                }
            }
            try {
                final Object value = field.get(o);
                if (value == null || MetaUtils.isLogicalPrimitive(value.getClass())) {
                    continue;
                }
                deque.addFirst(value);
            }
            catch (Exception ex) {}
        }
    }
    
    protected void traceReferences(final Object o) {
        if (o == null) {
            return;
        }
        final Map<Class, List<Field>> map = this.args.get("FIELD_SPECIFIERS");
        final ArrayDeque<Map<Map<Map<Map, Object>, Object>, Object>> arrayDeque = new ArrayDeque<Map<Map<Map<Map, Object>, Object>, Object>>();
        arrayDeque.addFirst((Map<Map<Map<Map, Object>, Object>, Object>)o);
        final Map<Object, Long> objVisited = this.objVisited;
        final Map<Object, Long> objsReferenced = this.objsReferenced;
        while (!arrayDeque.isEmpty()) {
            final Collection<Map> removeFirst = arrayDeque.removeFirst();
            if (!MetaUtils.isLogicalPrimitive(removeFirst.getClass())) {
                final Long n = objVisited.get(removeFirst);
                if (n != null) {
                    if (n == JsonWriter.ZERO) {
                        final Long value = this.identity++;
                        objVisited.put(removeFirst, value);
                        objsReferenced.put(removeFirst, value);
                        continue;
                    }
                    continue;
                }
                else {
                    objVisited.put(removeFirst, JsonWriter.ZERO);
                }
            }
            final Class<? extends Collection> class1 = removeFirst.getClass();
            if (class1.isArray()) {
                if (MetaUtils.isLogicalPrimitive(class1.getComponentType())) {
                    continue;
                }
                for (int length = Array.getLength(removeFirst), i = 0; i < length; ++i) {
                    final Object value2 = Array.get(removeFirst, i);
                    if (value2 != null) {
                        arrayDeque.addFirst((Map<K, Object>)value2);
                    }
                }
            }
            else if (Map.class.isAssignableFrom(class1)) {
                for (final Map.Entry<K, Object> entry : ((Map<K, Object>)removeFirst).entrySet()) {
                    if (entry.getValue() != null) {
                        arrayDeque.addFirst((Map<K, Object>)entry.getValue());
                    }
                    if (entry.getKey() != null) {
                        arrayDeque.addFirst((Map<K, Object>)entry.getKey());
                    }
                }
            }
            else if (Collection.class.isAssignableFrom(class1)) {
                for (final Map<K, Object> next : removeFirst) {
                    if (next != null) {
                        arrayDeque.addFirst(next);
                    }
                }
            }
            else {
                if (MetaUtils.isLogicalPrimitive(removeFirst.getClass())) {
                    continue;
                }
                this.traceFields((Deque<Object>)arrayDeque, removeFirst, map);
            }
        }
    }
    
    public void write(final Object o) {
        this.traceReferences(o);
        this.objVisited.clear();
        try {
            this.writeImpl(o, true);
            this.flush();
            this.objVisited.clear();
            this.objsReferenced.clear();
        }
        catch (Exception ex) {
            throw new JsonIoException("Error writing object to JSON:", ex);
        }
    }
    
    public boolean writeArrayElementIfMatching(final Class clazz, final Object o, final boolean b, final Writer writer) {
        if (o.getClass().isAssignableFrom(clazz)) {
            if (!this.notCustom.contains(o.getClass())) {
                try {
                    return this.writeCustom(clazz, o, b, writer);
                }
                catch (IOException ex) {
                    throw new JsonIoException("Unable to write custom formatted object as array element:", ex);
                }
            }
        }
        return false;
    }
    
    protected boolean writeCustom(final Class clazz, final Object o, boolean b, final Writer writer) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        final JsonClassWriterBase customWriter = this.getCustomWriter(clazz);
        final boolean b2 = false;
        final boolean b3 = false;
        if (customWriter == null) {
            return false;
        }
        if (this.writeOptionalReference(o)) {
            return true;
        }
        final boolean containsKey = this.objsReferenced.containsKey(o);
        if (customWriter instanceof JsonClassWriter) {
            final JsonClassWriter jsonClassWriter = (JsonClassWriter)customWriter;
            if (jsonClassWriter.hasPrimitiveForm() && ((!containsKey && !b) || customWriter instanceof Writers.JsonStringWriter)) {
                if (jsonClassWriter instanceof Writers.DateWriter) {
                    ((Writers.DateWriter)jsonClassWriter).writePrimitiveForm(o, writer, this.args);
                    return true;
                }
                jsonClassWriter.writePrimitiveForm(o, writer);
                return true;
            }
        }
        writer.write(123);
        this.tabIn();
        if (containsKey) {
            this.writeId(this.getId(o));
            if (b) {
                writer.write(44);
                this.newLine();
            }
        }
        if (b) {
            this.writeType(o, writer);
        }
        if (containsKey || b) {
            writer.write(44);
            this.newLine();
        }
        if (customWriter instanceof JsonClassWriterEx) {
            ((JsonClassWriterEx)customWriter).write(o, b || containsKey || b3, writer, this.args);
        }
        else {
            ((JsonClassWriter)customWriter).write(o, b || containsKey || b2, writer);
        }
        this.tabOut();
        writer.write(125);
        return true;
    }
    
    public boolean writeIfMatching(final Object o, boolean b, final Writer writer) {
        if (this.neverShowType) {
            b = false;
        }
        final Class<?> class1 = o.getClass();
        if (this.notCustom.contains(class1)) {
            return false;
        }
        try {
            return this.writeCustom(class1, o, b, writer);
        }
        catch (IOException ex) {
            throw new JsonIoException("Unable to write custom formatted object:", ex);
        }
    }
    
    public void writeImpl(final Object o, final boolean b) throws IOException {
        this.writeImpl(o, b, true, true);
    }
    
    public void writeImpl(final Object o, boolean b, final boolean b2, final boolean b3) throws IOException {
        if (this.neverShowType) {
            b = false;
        }
        if (o == null) {
            this.out.write("null");
            return;
        }
        if (b3 && this.writeIfMatching(o, b, this.out)) {
            return;
        }
        if (b2 && this.writeOptionalReference(o)) {
            return;
        }
        if (o.getClass().isArray()) {
            this.writeArray(o, b);
            return;
        }
        if (o instanceof Collection) {
            this.writeCollection((Collection)o, b);
            return;
        }
        if (o instanceof JsonObject) {
            final JsonObject jsonObject = (JsonObject)o;
            if (jsonObject.isArray()) {
                this.writeJsonObjectArray(jsonObject, b);
                return;
            }
            if (jsonObject.isCollection()) {
                this.writeJsonObjectCollection(jsonObject, b);
                return;
            }
            if (!jsonObject.isMap()) {
                this.writeJsonObjectObject(jsonObject, b);
                return;
            }
            if (!this.writeJsonObjectMapWithStringKeys(jsonObject, b)) {
                this.writeJsonObjectMap(jsonObject, b);
            }
        }
        else if (o instanceof Map) {
            if (!this.writeMapWithStringKeys((Map)o, b)) {
                this.writeMap((Map)o, b);
            }
        }
        else {
            this.writeObject(o, b, false);
        }
    }
    
    public void writeObject(final Object o, final boolean b, final boolean b2) throws IOException {
        final boolean b3 = !this.neverShowType && b;
        final boolean containsKey = this.objsReferenced.containsKey(o);
        if (!b2) {
            this.out.write(123);
            this.tabIn();
            if (containsKey) {
                this.writeId(this.getId(o));
            }
            if (containsKey && b3) {
                this.out.write(44);
                this.newLine();
            }
            if (b3) {
                this.writeType(o, this.out);
            }
        }
        boolean b5;
        final boolean b4 = b5 = !b3;
        if (containsKey) {
            b5 = b4;
            if (!b3) {
                b5 = false;
            }
        }
        final Map<Class, List<Field>> map = this.args.get("FIELD_SPECIFIERS");
        final List<Field> fieldsUsingSpecifier = getFieldsUsingSpecifier(o.getClass(), this.args.get("FIELD_BLACK_LIST"));
        final List<Field> fieldsUsingSpecifier2 = getFieldsUsingSpecifier(o.getClass(), map);
        if (fieldsUsingSpecifier2 != null) {
            for (final Field field : fieldsUsingSpecifier2) {
                if (fieldsUsingSpecifier == null || !fieldsUsingSpecifier.contains(field)) {
                    b5 = this.writeField(o, b5, field.getName(), field, true);
                }
            }
        }
        else {
            for (final Map.Entry<String, Field> entry : MetaUtils.getDeepDeclaredFields(o.getClass()).entrySet()) {
                final String s = entry.getKey();
                final Field field2 = entry.getValue();
                if (fieldsUsingSpecifier == null || !fieldsUsingSpecifier.contains(field2)) {
                    b5 = this.writeField(o, b5, s, field2, false);
                }
            }
        }
        if (!b2) {
            this.tabOut();
            this.out.write(125);
        }
    }
    
    public interface JsonClassWriter extends JsonClassWriterBase
    {
        boolean hasPrimitiveForm();
        
        void write(final Object p0, final boolean p1, final Writer p2) throws IOException;
        
        void writePrimitiveForm(final Object p0, final Writer p1) throws IOException;
    }
    
    public interface JsonClassWriterBase
    {
    }
    
    public interface JsonClassWriterEx extends JsonClassWriterBase
    {
        public static final String JSON_WRITER = "JSON_WRITER";
        
        void write(final Object p0, final boolean p1, final Writer p2, final Map<String, Object> p3) throws IOException;
        
        public static class Support
        {
            public static JsonWriter getWriter(final Map<String, Object> map) {
                return map.get("JSON_WRITER");
            }
        }
    }
    
    static final class NullClass implements JsonClassWriterBase
    {
    }
}
