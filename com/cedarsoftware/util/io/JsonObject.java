package com.cedarsoftware.util.io;

import java.lang.reflect.*;
import java.util.*;

public class JsonObject<K, V> extends LinkedHashMap<K, V>
{
    static Set<String> primitiveWrappers;
    static Set<String> primitives;
    int col;
    long id;
    boolean isMap;
    int line;
    Object target;
    String type;
    
    static {
        JsonObject.primitives = new HashSet<String>();
        JsonObject.primitiveWrappers = new HashSet<String>();
        JsonObject.primitives.add("boolean");
        JsonObject.primitives.add("byte");
        JsonObject.primitives.add("char");
        JsonObject.primitives.add("double");
        JsonObject.primitives.add("float");
        JsonObject.primitives.add("int");
        JsonObject.primitives.add("long");
        JsonObject.primitives.add("short");
        JsonObject.primitiveWrappers.add("java.lang.Boolean");
        JsonObject.primitiveWrappers.add("java.lang.Byte");
        JsonObject.primitiveWrappers.add("java.lang.Character");
        JsonObject.primitiveWrappers.add("java.lang.Double");
        JsonObject.primitiveWrappers.add("java.lang.Float");
        JsonObject.primitiveWrappers.add("java.lang.Integer");
        JsonObject.primitiveWrappers.add("java.lang.Long");
        JsonObject.primitiveWrappers.add("java.lang.Short");
    }
    
    public JsonObject() {
        this.isMap = false;
        this.id = -1L;
    }
    
    public static boolean isPrimitiveWrapper(final Class clazz) {
        return JsonObject.primitiveWrappers.contains(clazz.getName());
    }
    
    @Override
    public void clear() {
        super.clear();
        this.type = null;
    }
    
    void clearArray() {
        this.remove("@items");
    }
    
    public Object[] getArray() {
        return this.get("@items");
    }
    
    public int getCol() {
        return this.col;
    }
    
    public Class getComponentType() {
        return this.target.getClass().getComponentType();
    }
    
    public long getId() {
        return this.id;
    }
    
    public int getLength() {
        if (this.isArray()) {
            if (this.target != null) {
                return Array.getLength(this.target);
            }
            final Object[] array = this.get("@items");
            if (array == null) {
                return 0;
            }
            return array.length;
        }
        else {
            if (!this.isCollection() && !this.isMap()) {
                final StringBuilder sb = new StringBuilder("getLength() called on a non-collection, line ");
                sb.append(this.line);
                sb.append(", col ");
                sb.append(this.col);
                throw new JsonIoException(sb.toString());
            }
            final Object[] array2 = this.get("@items");
            if (array2 == null) {
                return 0;
            }
            return array2.length;
        }
    }
    
    public int getLine() {
        return this.line;
    }
    
    public Object getPrimitiveValue() {
        if ("boolean".equals(this.type) || "double".equals(this.type) || "long".equals(this.type)) {
            return this.get("value");
        }
        if ("byte".equals(this.type)) {
            return this.get("value").byteValue();
        }
        if ("char".equals(this.type)) {
            return this.get("value").charAt(0);
        }
        if ("float".equals(this.type)) {
            return this.get("value").floatValue();
        }
        if ("int".equals(this.type)) {
            return this.get("value").intValue();
        }
        if ("short".equals(this.type)) {
            return this.get("value").shortValue();
        }
        final StringBuilder sb = new StringBuilder("Invalid primitive type, line ");
        sb.append(this.line);
        sb.append(", col ");
        sb.append(this.col);
        throw new JsonIoException(sb.toString());
    }
    
    public Long getReferenceId() {
        return this.get("@ref");
    }
    
    public Object getTarget() {
        return this.target;
    }
    
    public Class getTargetClass() {
        return this.target.getClass();
    }
    
    public String getType() {
        return this.type;
    }
    
    public boolean hasId() {
        return this.id != -1L;
    }
    
    public boolean isArray() {
        if (this.target != null) {
            return this.target.getClass().isArray();
        }
        if (this.type != null) {
            return this.type.contains("[");
        }
        return this.containsKey("@items") && !this.containsKey("@keys");
    }
    
    public boolean isCollection() {
        return this.target instanceof Collection || (this.containsKey("@items") && !this.containsKey("@keys") && (this.type != null && !this.type.contains("[")));
    }
    
    public boolean isMap() {
        return this.isMap || this.target instanceof Map;
    }
    
    public boolean isPrimitive() {
        return JsonObject.primitiveWrappers.contains(this.type);
    }
    
    public boolean isReference() {
        return this.containsKey("@ref");
    }
    
    void moveBytesToMate() {
        final byte[] array = (byte[])this.target;
        final Object[] array2 = this.getArray();
        for (int length = array2.length, i = 0; i < length; ++i) {
            array[i] = ((Number)array2[i]).byteValue();
        }
    }
    
    void moveCharsToMate() {
        final Object[] array = this.getArray();
        if (array == null) {
            this.target = null;
            return;
        }
        if (array.length == 0) {
            this.target = new char[0];
            return;
        }
        if (array.length == 1) {
            this.target = ((String)array[0]).toCharArray();
            return;
        }
        final StringBuilder sb = new StringBuilder("char[] should only have one String in the [], found ");
        sb.append(array.length);
        sb.append(", line ");
        sb.append(this.line);
        sb.append(", col ");
        sb.append(this.col);
        throw new JsonIoException(sb.toString());
    }
    
    @Override
    public V put(final K k, final V v) {
        if (k == null) {
            return super.put(null, v);
        }
        if (k.equals("@type")) {
            final String type = this.type;
            this.type = (String)v;
            return (V)type;
        }
        if (k.equals("@id")) {
            final long id = this.id;
            this.id = (long)v;
            return (V)Long.valueOf(id);
        }
        if (("@items".equals(k) && this.containsKey("@keys")) || ("@keys".equals(k) && this.containsKey("@items"))) {
            this.isMap = true;
        }
        return super.put(k, v);
    }
    
    public void setTarget(final Object target) {
        this.target = target;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public int size() {
        if (this.containsKey("@items")) {
            final Object value = this.get("@items");
            if (value instanceof Object[]) {
                return ((Object[])value).length;
            }
            if (value == null) {
                return 0;
            }
            final StringBuilder sb = new StringBuilder("JsonObject with @items, but no array [] associated to it, line ");
            sb.append(this.line);
            sb.append(", col ");
            sb.append(this.col);
            throw new JsonIoException(sb.toString());
        }
        else {
            if (this.containsKey("@ref")) {
                return 0;
            }
            return super.size();
        }
    }
}
