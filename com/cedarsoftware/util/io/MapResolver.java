package com.cedarsoftware.util.io;

import java.math.*;
import java.util.*;
import java.lang.reflect.*;

public class MapResolver extends Resolver
{
    protected MapResolver(final JsonReader jsonReader) {
        super(jsonReader);
    }
    
    @Override
    protected Object readIfMatching(final Object o, final Class clazz, final Deque<JsonObject<String, Object>> deque) {
        return null;
    }
    
    @Override
    protected void traverseArray(final Deque<JsonObject<String, Object>> deque, final JsonObject<String, Object> jsonObject) {
        this.traverseCollection(deque, jsonObject);
    }
    
    @Override
    protected void traverseCollection(final Deque<JsonObject<String, Object>> deque, final JsonObject<String, Object> jsonObject) {
        final Object[] array = jsonObject.getArray();
        if (array == null) {
            return;
        }
        if (array.length == 0) {
            return;
        }
        int n = 0;
        final ArrayList list = new ArrayList<Object>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            final Object o = array[i];
            if (o == "~!o~") {
                list.add(new JsonObject<String, Object>());
            }
            else {
                list.add((JsonObject<String, Object>)o);
                if (o instanceof Object[]) {
                    final JsonObject<String, JsonObject<String, Object>> jsonObject2 = new JsonObject<String, JsonObject<String, Object>>();
                    jsonObject2.put("@items", (JsonObject<String, Object>)o);
                    deque.addFirst((JsonObject<String, Object>)jsonObject2);
                }
                else if (o instanceof JsonObject) {
                    final JsonObject<String, Object> jsonObject3 = (JsonObject<String, Object>)o;
                    final Long referenceId = jsonObject3.getReferenceId();
                    if (referenceId != null) {
                        list.set(n, (JsonObject<String, Object>)this.getReferencedObj(referenceId));
                    }
                    else {
                        deque.addFirst(jsonObject3);
                    }
                }
                ++n;
            }
        }
        jsonObject.target = null;
        for (int j = 0; j < array.length; ++j) {
            array[j] = list.get(j);
        }
    }
    
    @Override
    public void traverseFields(final Deque<JsonObject<String, Object>> deque, final JsonObject<String, Object> jsonObject) {
        final Object target = jsonObject.target;
        for (final Map.Entry<Object, Object> entry : jsonObject.entrySet()) {
            final String s = entry.getKey();
            Field field;
            if (target != null) {
                field = MetaUtils.getField(target.getClass(), s);
            }
            else {
                field = null;
            }
            final String value = entry.getValue();
            if (value == null) {
                jsonObject.put(s, null);
            }
            else if (value == "~!o~") {
                jsonObject.put(s, new JsonObject());
            }
            else if (((JsonObject<String, Object>)value).getClass().isArray()) {
                final JsonObject<String, Object> jsonObject2 = new JsonObject<String, Object>();
                jsonObject2.put("@items", value);
                deque.addFirst(jsonObject2);
                jsonObject.put(s, value);
            }
            else if (value instanceof JsonObject) {
                final JsonObject<String, Object> jsonObject3 = (JsonObject<String, Object>)value;
                if (field != null && JsonObject.isPrimitiveWrapper(field.getType())) {
                    jsonObject3.put("value", MetaUtils.newPrimitiveWrapper(field.getType(), jsonObject3.get("value")));
                }
                else {
                    final Long referenceId = jsonObject3.getReferenceId();
                    if (referenceId != null) {
                        jsonObject.put(s, this.getReferencedObj(referenceId));
                    }
                    else {
                        deque.addFirst(jsonObject3);
                    }
                }
            }
            else {
                if (field == null) {
                    continue;
                }
                final Class<?> type = field.getType();
                if (MetaUtils.isPrimitive(type)) {
                    jsonObject.put(s, MetaUtils.newPrimitiveWrapper(type, value));
                }
                else if (BigDecimal.class == type) {
                    jsonObject.put(s, Readers.bigDecimalFrom(value));
                }
                else if (BigInteger.class == type) {
                    jsonObject.put(s, Readers.bigIntegerFrom(value));
                }
                else {
                    if (!(value instanceof String) || type == String.class || type == StringBuilder.class || type == StringBuffer.class || !"".equals(value.trim())) {
                        continue;
                    }
                    jsonObject.put(s, null);
                }
            }
        }
        jsonObject.target = null;
    }
}
