package com.cedarsoftware.util.io;

import java.util.*;
import java.lang.reflect.*;

abstract class Resolver
{
    private static final Map<String, Class> coercedTypes;
    private static final NullClass nullReader;
    private final boolean failOnUnknownType;
    protected final Collection<Missingfields> missingFields;
    private final Collection<Object[]> prettyMaps;
    protected final JsonReader reader;
    final Map<Class, JsonReader.JsonClassReaderBase> readerCache;
    private final Object unknownClass;
    final Collection<UnresolvedReference> unresolvedRefs;
    private final boolean useMaps;
    
    static {
        nullReader = new NullClass(null);
        (coercedTypes = new LinkedHashMap<String, Class>()).put("java.util.Arrays$ArrayList", ArrayList.class);
        Resolver.coercedTypes.put("java.util.LinkedHashMap$LinkedKeySet", LinkedHashSet.class);
        Resolver.coercedTypes.put("java.util.LinkedHashMap$LinkedValues", ArrayList.class);
        Resolver.coercedTypes.put("java.util.HashMap$KeySet", HashSet.class);
        Resolver.coercedTypes.put("java.util.HashMap$Values", ArrayList.class);
        Resolver.coercedTypes.put("java.util.TreeMap$KeySet", TreeSet.class);
        Resolver.coercedTypes.put("java.util.TreeMap$Values", ArrayList.class);
        Resolver.coercedTypes.put("java.util.concurrent.ConcurrentHashMap$KeySet", LinkedHashSet.class);
        Resolver.coercedTypes.put("java.util.concurrent.ConcurrentHashMap$KeySetView", LinkedHashSet.class);
        Resolver.coercedTypes.put("java.util.concurrent.ConcurrentHashMap$Values", ArrayList.class);
        Resolver.coercedTypes.put("java.util.concurrent.ConcurrentHashMap$ValuesView", ArrayList.class);
        Resolver.coercedTypes.put("java.util.concurrent.ConcurrentSkipListMap$KeySet", LinkedHashSet.class);
        Resolver.coercedTypes.put("java.util.concurrent.ConcurrentSkipListMap$Values", ArrayList.class);
        Resolver.coercedTypes.put("java.util.IdentityHashMap$KeySet", LinkedHashSet.class);
        Resolver.coercedTypes.put("java.util.IdentityHashMap$Values", ArrayList.class);
    }
    
    protected Resolver(final JsonReader reader) {
        this.unresolvedRefs = new ArrayList<UnresolvedReference>();
        this.readerCache = new HashMap<Class, JsonReader.JsonClassReaderBase>();
        this.prettyMaps = new ArrayList<Object[]>();
        this.missingFields = new ArrayList<Missingfields>();
        this.reader = reader;
        final Map<String, Object> args = reader.getArgs();
        args.put("OBJECT_RESOLVER", this);
        this.useMaps = Boolean.TRUE.equals(args.get("USE_MAPS"));
        Object value;
        if (args.containsKey("UNKNOWN_OBJECT")) {
            value = args.get("UNKNOWN_OBJECT");
        }
        else {
            value = null;
        }
        this.unknownClass = value;
        this.failOnUnknownType = Boolean.TRUE.equals(args.get("FAIL_ON_UNKNOWN_TYPE"));
    }
    
    private static Object[] buildCollection(final Deque<JsonObject<String, Object>> deque, Object[] target, final int n) {
        final JsonObject<String, Object> jsonObject = new JsonObject<String, Object>();
        jsonObject.put("@items", target);
        target = new Object[n];
        jsonObject.target = target;
        deque.addFirst(jsonObject);
        return target;
    }
    
    protected static void convertMapToKeysItems(final JsonObject<String, Object> jsonObject) {
        if (!jsonObject.containsKey("@keys") && !jsonObject.isReference()) {
            final Object[] array = new Object[jsonObject.size()];
            final Object[] array2 = new Object[jsonObject.size()];
            int n = 0;
            for (final Map.Entry<Object, Object> entry : jsonObject.entrySet()) {
                array[n] = entry.getKey();
                array2[n] = entry.getValue();
                ++n;
            }
            final String type = jsonObject.getType();
            jsonObject.clear();
            jsonObject.setType(type);
            jsonObject.put("@keys", array);
            jsonObject.put("@items", array2);
        }
    }
    
    private JsonReader.JsonClassReaderBase forceGetCustomReader(final Class clazz) {
        JsonReader.JsonClassReaderBase nullReader = Resolver.nullReader;
        int n = Integer.MAX_VALUE;
        for (final Map.Entry<Class, JsonReader.JsonClassReaderBase> entry : this.getReaders().entrySet()) {
            final Class clazz2 = entry.getKey();
            if (clazz2 == clazz) {
                return entry.getValue();
            }
            final int distance = MetaUtils.getDistance(clazz2, clazz);
            if (distance >= n) {
                continue;
            }
            n = distance;
            nullReader = entry.getValue();
        }
        return nullReader;
    }
    
    private Object getEnum(final Class clazz, final JsonObject jsonObject) {
        try {
            return Enum.valueOf((Class<Enum>)clazz, jsonObject.get("name"));
        }
        catch (Exception ex) {
            return Enum.valueOf((Class<Object>)clazz, jsonObject.get("java.lang.Enum.name"));
        }
    }
    
    private Object getEnumSet(final Class clazz, final JsonObject<String, Object> jsonObject) {
        final Object[] array = jsonObject.getArray();
        if (array != null && array.length != 0) {
            int i = 0;
            final Class classForName = MetaUtils.classForName(((JsonObject)array[0]).getType(), this.reader.getClassLoader());
            AbstractCollection<Enum> of = null;
            while (i < array.length) {
                final Enum enum1 = (Enum)this.getEnum(classForName, (JsonObject)array[i]);
                if (of == null) {
                    of = EnumSet.of(enum1);
                }
                else {
                    of.add(enum1);
                }
                ++i;
            }
            return of;
        }
        return newInstance(clazz, jsonObject);
    }
    
    private void handleMissingFields() {
        final JsonReader.MissingFieldHandler missingFieldHandler = this.reader.getMissingFieldHandler();
        if (missingFieldHandler != null) {
            for (final Missingfields missingfields : this.missingFields) {
                missingFieldHandler.fieldMissing(missingfields.target, missingfields.fieldName, missingfields.value);
            }
        }
    }
    
    public static Object newInstance(final Class clazz, final JsonObject jsonObject) {
        return JsonReader.newInstance(clazz, jsonObject);
    }
    
    protected void cleanup() {
        this.patchUnresolvedReferences();
        this.rehashMaps();
        this.reader.getObjectsRead().clear();
        this.unresolvedRefs.clear();
        this.prettyMaps.clear();
        this.readerCache.clear();
        this.handleMissingFields();
    }
    
    protected Object coerceCertainTypes(final String s) {
        final Class clazz = Resolver.coercedTypes.get(s);
        if (clazz == null) {
            return null;
        }
        return MetaUtils.newInstance(clazz);
    }
    
    protected Object convertMapsToObjects(final JsonObject<String, Object> jsonObject) {
        final ArrayDeque arrayDeque = new ArrayDeque<JsonObject<String, Object>>();
        arrayDeque.addFirst(jsonObject);
        while (!arrayDeque.isEmpty()) {
            final JsonObject<String, Object> jsonObject2 = arrayDeque.removeFirst();
            if (jsonObject2.isArray()) {
                this.traverseArray(arrayDeque, jsonObject2);
            }
            else if (jsonObject2.isCollection()) {
                this.traverseCollection(arrayDeque, jsonObject2);
            }
            else if (jsonObject2.isMap()) {
                this.traverseMap(arrayDeque, jsonObject2);
            }
            else {
                final Object ifMatching = this.readIfMatching(jsonObject2, null, arrayDeque);
                if (ifMatching != null) {
                    jsonObject2.target = ifMatching;
                }
                else {
                    this.traverseFields(arrayDeque, jsonObject2);
                }
            }
        }
        return jsonObject.target;
    }
    
    protected Object createJavaObjectInstance(Class o, final JsonObject jsonObject) {
        final boolean useMaps = this.useMaps;
        String s2;
        final String s = s2 = jsonObject.type;
        if ("java.lang.Object".equals(s)) {
            final Object value = jsonObject.get("value");
            s2 = s;
            if (jsonObject.keySet().size() == 1) {
                s2 = s;
                if (value != null) {
                    s2 = value.getClass().getName();
                }
            }
        }
        final int n = 0;
        int length = 0;
        if (s2 != null) {
            try {
                final Class classForName = MetaUtils.classForName(s2, this.reader.getClassLoader(), this.failOnUnknownType);
                if (classForName.isArray()) {
                    o = jsonObject.getArray();
                    if (o != null) {
                        length = o.length;
                    }
                    if (classForName == char[].class) {
                        jsonObject.moveCharsToMate();
                        final Object o2 = jsonObject.target;
                        return jsonObject.target = o2;
                    }
                    final Object o2 = Array.newInstance(classForName.getComponentType(), length);
                    return jsonObject.target = o2;
                }
                else {
                    if (MetaUtils.isPrimitive(classForName)) {
                        final Object o2 = MetaUtils.newPrimitiveWrapper(classForName, jsonObject.get("value"));
                        return jsonObject.target = o2;
                    }
                    if (classForName == Class.class) {
                        final Object o2 = MetaUtils.classForName(jsonObject.get("value"), this.reader.getClassLoader());
                        return jsonObject.target = o2;
                    }
                    if (classForName.isEnum()) {
                        final Object o2 = this.getEnum(classForName, jsonObject);
                        return jsonObject.target = o2;
                    }
                    if (Enum.class.isAssignableFrom(classForName)) {
                        final Object o2 = this.getEnum(classForName.getSuperclass(), jsonObject);
                        return jsonObject.target = o2;
                    }
                    if (EnumSet.class.isAssignableFrom(classForName)) {
                        final Object o2 = this.getEnumSet(classForName, jsonObject);
                        return jsonObject.target = o2;
                    }
                    o = this.coerceCertainTypes(classForName.getName());
                    Object o2;
                    if ((o2 = o) == null) {
                        o2 = newInstance(classForName, jsonObject);
                        return jsonObject.target = o2;
                    }
                    return jsonObject.target = o2;
                }
            }
            catch (Exception ex) {
                if (useMaps) {
                    jsonObject.type = null;
                    jsonObject.target = null;
                    return jsonObject;
                }
                String name;
                if (o == null) {
                    name = "null";
                }
                else {
                    name = ((Class)o).getName();
                }
                final StringBuilder sb = new StringBuilder("Unable to create class: ");
                sb.append(name);
                throw new JsonIoException(sb.toString(), ex);
            }
        }
        final Object[] array = jsonObject.getArray();
        Object o2;
        if (!((Class)o).isArray() && (array == null || o != Object.class || jsonObject.containsKey("@keys"))) {
            if (((Class)o).isEnum()) {
                o2 = this.getEnum((Class)o, jsonObject);
            }
            else if (Enum.class.isAssignableFrom((Class<?>)o)) {
                o2 = this.getEnum(((Class<Object>)o).getSuperclass(), jsonObject);
            }
            else if (EnumSet.class.isAssignableFrom((Class<?>)o)) {
                o2 = this.getEnumSet((Class)o, jsonObject);
            }
            else if ((o2 = this.coerceCertainTypes(((Class)o).getName())) == null) {
                if (o == Object.class && !useMaps) {
                    if (this.unknownClass == null) {
                        o2 = new JsonObject();
                        ((JsonObject)o2).type = Map.class.getName();
                    }
                    else {
                        if (!(this.unknownClass instanceof String)) {
                            final StringBuilder sb2 = new StringBuilder("Unable to determine object type at column: ");
                            sb2.append(jsonObject.col);
                            sb2.append(", line: ");
                            sb2.append(jsonObject.line);
                            sb2.append(", content: ");
                            sb2.append(jsonObject);
                            throw new JsonIoException(sb2.toString());
                        }
                        o2 = newInstance(MetaUtils.classForName(((String)this.unknownClass).trim(), this.reader.getClassLoader()), jsonObject);
                    }
                }
                else {
                    o2 = newInstance((Class)o, jsonObject);
                }
            }
        }
        else {
            int length2;
            if (array == null) {
                length2 = n;
            }
            else {
                length2 = array.length;
            }
            if (((Class)o).isArray()) {
                o = ((Class)o).getComponentType();
            }
            else {
                o = Object.class;
            }
            o2 = Array.newInstance((Class<?>)o, length2);
        }
        return jsonObject.target = o2;
    }
    
    protected JsonReader.JsonClassReaderBase getCustomReader(final Class clazz) {
        JsonReader.JsonClassReaderBase forceGetCustomReader;
        if ((forceGetCustomReader = this.readerCache.get(clazz)) == null) {
            forceGetCustomReader = this.forceGetCustomReader(clazz);
            this.readerCache.put(clazz, forceGetCustomReader);
        }
        if (forceGetCustomReader == Resolver.nullReader) {
            return null;
        }
        return forceGetCustomReader;
    }
    
    protected JsonReader getReader() {
        return this.reader;
    }
    
    protected Map<Class, JsonReader.JsonClassReaderBase> getReaders() {
        return this.reader.readers;
    }
    
    protected JsonObject getReferencedObj(final Long n) {
        final JsonObject jsonObject = this.reader.getObjectsRead().get(n);
        if (jsonObject == null) {
            final StringBuilder sb = new StringBuilder("Forward reference @ref: ");
            sb.append(n);
            sb.append(", but no object defined (@id) with that value");
            throw new JsonIoException(sb.toString());
        }
        return jsonObject;
    }
    
    protected boolean notCustom(final Class clazz) {
        return this.reader.notCustom.contains(clazz);
    }
    
    protected void patchUnresolvedReferences() {
        final Iterator<UnresolvedReference> iterator = this.unresolvedRefs.iterator();
        while (iterator.hasNext()) {
            final UnresolvedReference unresolvedReference = iterator.next();
            final Object target = unresolvedReference.referencingObj.target;
            final JsonObject jsonObject = this.reader.getObjectsRead().get(unresolvedReference.refId);
            if (unresolvedReference.index >= 0) {
                if (target instanceof List) {
                    ((List<Object>)target).set(unresolvedReference.index, jsonObject.target);
                }
                else if (target instanceof Collection) {
                    ((List<Object>)target).add(jsonObject.target);
                }
                else {
                    Array.set(target, unresolvedReference.index, jsonObject.target);
                }
            }
            else {
                final Field field = MetaUtils.getField(((List<Object>)target).getClass(), unresolvedReference.field);
                if (field != null) {
                    try {
                        field.set(target, jsonObject.target);
                    }
                    catch (Exception ex) {
                        final StringBuilder sb = new StringBuilder("Error setting field while resolving references '");
                        sb.append(field.getName());
                        sb.append("', @ref = ");
                        sb.append(unresolvedReference.refId);
                        throw new JsonIoException(sb.toString(), ex);
                    }
                }
            }
            iterator.remove();
        }
    }
    
    protected abstract Object readIfMatching(final Object p0, final Class p1, final Deque<JsonObject<String, Object>> p2);
    
    protected void rehashMaps() {
        final boolean useMaps = this.useMaps;
        for (final Object[] array : this.prettyMaps) {
            final JsonObject jsonObject = (JsonObject)array[0];
            Map<Object, Object> map;
            Object[] array4;
            Object[] array5;
            if (useMaps) {
                map = (Map<Object, Object>)jsonObject;
                final Object[] array2 = jsonObject.remove("@keys");
                final Object[] array3 = jsonObject.remove("@items");
                array4 = array2;
                array5 = array3;
            }
            else {
                map = (Map<Object, Object>)jsonObject.target;
                final Object[] array6 = (Object[])array[1];
                array5 = (Object[])array[2];
                jsonObject.clear();
                array4 = array6;
            }
            int n = 0;
            while (array4 != null) {
                if (n >= array4.length) {
                    break;
                }
                map.put(array4[n], array5[n]);
                ++n;
            }
        }
    }
    
    protected abstract void traverseArray(final Deque<JsonObject<String, Object>> p0, final JsonObject<String, Object> p1);
    
    protected abstract void traverseCollection(final Deque<JsonObject<String, Object>> p0, final JsonObject<String, Object> p1);
    
    public abstract void traverseFields(final Deque<JsonObject<String, Object>> p0, final JsonObject<String, Object> p1);
    
    protected void traverseMap(final Deque<JsonObject<String, Object>> deque, final JsonObject<String, Object> jsonObject) {
        convertMapToKeysItems(jsonObject);
        final Object[] array = jsonObject.get("@keys");
        final Object[] array2 = jsonObject.getArray();
        if (array != null && array2 != null) {
            final int length = array.length;
            if (length != array2.length) {
                throw new JsonIoException("Map written with @keys and @items entries of different sizes");
            }
            this.prettyMaps.add(new Object[] { jsonObject, buildCollection(deque, array, length), buildCollection(deque, array2, length) });
        }
        else if (array != array2) {
            throw new JsonIoException("Map written where one of @keys or @items is empty");
        }
    }
    
    protected static class Missingfields
    {
        private String fieldName;
        private Object target;
        private Object value;
        
        public Missingfields(final Object target, final String fieldName, final Object value) {
            this.target = target;
            this.fieldName = fieldName;
            this.value = value;
        }
    }
    
    private static final class NullClass implements JsonClassReaderBase
    {
    }
    
    static final class UnresolvedReference
    {
        private String field;
        private int index;
        private final long refId;
        private final JsonObject referencingObj;
        
        UnresolvedReference(final JsonObject referencingObj, final int index, final long refId) {
            this.index = -1;
            this.referencingObj = referencingObj;
            this.index = index;
            this.refId = refId;
        }
        
        UnresolvedReference(final JsonObject referencingObj, final String field, final long refId) {
            this.index = -1;
            this.referencingObj = referencingObj;
            this.field = field;
            this.refId = refId;
        }
    }
}
