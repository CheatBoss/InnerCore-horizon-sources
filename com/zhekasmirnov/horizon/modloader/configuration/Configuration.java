package com.zhekasmirnov.horizon.modloader.configuration;

import com.zhekasmirnov.horizon.util.*;
import java.util.*;
import org.json.*;

public abstract class Configuration
{
    public abstract void refresh();
    
    public abstract <T> T get(final String p0, final Class<T> p1);
    
    public abstract Object get(final String p0);
    
    public abstract boolean set(final String p0, final Object p1);
    
    public abstract Object delete(final String p0);
    
    public abstract boolean isContainer(final String p0);
    
    public abstract Configuration getChild(final String p0);
    
    public abstract boolean isReadOnly();
    
    public abstract void save();
    
    public abstract void load();
    
    public int getInt(final String key) {
        final Object value = this.get(key);
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        return 0;
    }
    
    public float getFloat(final String key) {
        final Object value = this.get(key);
        if (value instanceof Number) {
            return ((Number)value).floatValue();
        }
        return 0.0f;
    }
    
    public double getDouble(final String key) {
        final Object value = this.get(key);
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        return 0.0;
    }
    
    public long getLong(final String key) {
        final Object value = this.get(key);
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        return 0L;
    }
    
    public String getString(final String key) {
        final Object value = this.get(key);
        return (value == null) ? null : value.toString();
    }
    
    public boolean getBoolean(final String key) {
        final Object value = this.get(key);
        return value instanceof Boolean && (boolean)value;
    }
    
    public <T> List<T> getArray(final String key) {
        final JSONArray array = this.get(key, JSONArray.class);
        if (array != null) {
            final List<T> result = new ArrayList<T>();
            for (final Object object : new JsonIterator<Object>(array)) {
                try {
                    result.add((T)object);
                }
                catch (ClassCastException ex) {}
            }
            return result;
        }
        return null;
    }
    
    private static boolean checkSameType(final Object o1, final Object o2) {
        return o1 != null && o2 != null && ((o1 instanceof Number && o2 instanceof Number) || (o1 instanceof CharSequence && o2 instanceof CharSequence) || o1.getClass() == o2.getClass());
    }
    
    private void checkAndRestore(final String prefix, final JSONObject json) {
        for (String key : new JsonIterator<String>(json)) {
            final Object value = json.opt(key);
            key = prefix + key;
            if (value instanceof JSONObject) {
                if (!this.isContainer(key)) {
                    this.delete(key);
                }
                this.checkAndRestore(key + ".", (JSONObject)value);
            }
            else {
                if (this.isContainer(key)) {
                    this.delete(key);
                }
                if (checkSameType(this.get(key), value)) {
                    continue;
                }
                this.set(key, value);
            }
        }
    }
    
    public void checkAndRestore(final JSONObject json) {
        if (!this.isReadOnly()) {
            this.checkAndRestore("", json);
            this.save();
        }
    }
    
    public void checkAndRestore(final String json) {
        try {
            this.checkAndRestore(new JSONObject(json));
        }
        catch (JSONException e) {
            throw new RuntimeException((Throwable)e);
        }
    }
}
