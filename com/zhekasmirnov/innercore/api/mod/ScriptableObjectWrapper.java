package com.zhekasmirnov.innercore.api.mod;

import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import java.util.*;
import org.mozilla.javascript.*;

public class ScriptableObjectWrapper
{
    private Scriptable scriptable;
    private NativeArray scriptableArray;
    private ScriptableObject scriptableObject;
    
    public ScriptableObjectWrapper() {
        this((Scriptable)ScriptableObjectHelper.createEmpty());
    }
    
    public ScriptableObjectWrapper(final String s) {
        try {
            if (s.length() > 0) {
                if (s.startsWith("[")) {
                    this.parseJson(new JSONArray(s));
                }
                else {
                    this.parseJson(new JSONObject(s));
                }
            }
        }
        catch (JSONException ex) {
            ICLog.e("ERROR", "failed to parse json string for ScriptableObjectWrapper", (Throwable)ex);
        }
    }
    
    public ScriptableObjectWrapper(final JSONArray jsonArray) {
        this.parseJson(jsonArray);
    }
    
    public ScriptableObjectWrapper(final JSONObject jsonObject) {
        this.parseJson(jsonObject);
    }
    
    public ScriptableObjectWrapper(final Scriptable wrappedObject) {
        this.setWrappedObject(wrappedObject);
    }
    
    private void parseJson(final JSONArray jsonArray) {
        this.setWrappedObject((Scriptable)ScriptableObjectHelper.createArray(new Object[jsonArray.length()]));
        for (int i = 0; i < jsonArray.length(); ++i) {
            final Object opt = jsonArray.opt(i);
            if (opt instanceof JSONObject) {
                this.put(i, new ScriptableObjectWrapper((JSONObject)opt));
            }
            else if (opt instanceof JSONArray) {
                this.put(i, new ScriptableObjectWrapper((JSONArray)opt));
            }
            else {
                this.put(i, opt);
            }
        }
    }
    
    private void parseJson(final JSONObject jsonObject) {
        this.setWrappedObject((Scriptable)ScriptableObjectHelper.createEmpty());
        final JSONArray names = jsonObject.names();
        if (names != null) {
            for (int i = 0; i < names.length(); ++i) {
                final String optString = names.optString(i);
                if (optString != null) {
                    final Object opt = jsonObject.opt(optString);
                    if (opt instanceof JSONObject) {
                        this.put(optString, new ScriptableObjectWrapper((JSONObject)opt));
                    }
                    else if (opt instanceof JSONArray) {
                        this.put(optString, new ScriptableObjectWrapper((JSONArray)opt));
                    }
                    else {
                        this.put(optString, opt);
                    }
                }
            }
        }
    }
    
    private void setWrappedObject(final Scriptable scriptable) {
        this.scriptable = scriptable;
        final boolean b = scriptable instanceof ScriptableObject;
        final NativeArray nativeArray = null;
        ScriptableObject scriptableObject;
        if (b) {
            scriptableObject = (ScriptableObject)scriptable;
        }
        else {
            scriptableObject = null;
        }
        this.scriptableObject = scriptableObject;
        NativeArray scriptableArray = nativeArray;
        if (scriptable instanceof NativeArray) {
            scriptableArray = (NativeArray)scriptable;
        }
        this.scriptableArray = scriptableArray;
    }
    
    public Object[] asArray() {
        final ArrayList<Object> list = new ArrayList<Object>();
        final Object[] ids = this.scriptable.getIds();
        for (int length = ids.length, i = 0; i < length; ++i) {
            final Object o = ids[i];
            if (o instanceof String) {
                list.add(this.scriptable.get((String)o, this.scriptable));
            }
            else {
                list.add(this.scriptable.get((int)o, this.scriptable));
            }
        }
        return list.toArray();
    }
    
    public Object get(final Object o) {
        return this.get(o, null);
    }
    
    public Object get(final Object o, final Object o2) {
        if (o instanceof Integer) {
            if (this.scriptable.has((int)o, this.scriptable)) {
                return this.scriptable.get((int)o, this.scriptable);
            }
            return o2;
        }
        else {
            if (this.scriptable.has((String)o, this.scriptable)) {
                return this.scriptable.get((String)o, this.scriptable);
            }
            return o2;
        }
    }
    
    public boolean getBoolean(final Object o) {
        return this.getBoolean(o, false);
    }
    
    public boolean getBoolean(final Object o, final boolean b) {
        if (o instanceof Integer) {
            if (this.scriptable.has((int)o, this.scriptable)) {
                return Context.toBoolean(this.scriptable.get((int)o, this.scriptable));
            }
            return b;
        }
        else {
            if (this.scriptable.has((String)o, this.scriptable)) {
                return Context.toBoolean(this.scriptable.get((String)o, this.scriptable));
            }
            return b;
        }
    }
    
    public float[] getColorTemplate(final Object o, final float n) {
        final float[] floatArray = this.getFloatArray(o, 4, n);
        final ScriptableObjectWrapper scriptableWrapper = this.getScriptableWrapper(o);
        if (scriptableWrapper != null) {
            floatArray[0] = scriptableWrapper.getFloat("r", floatArray[0]);
            floatArray[1] = scriptableWrapper.getFloat("g", floatArray[1]);
            floatArray[2] = scriptableWrapper.getFloat("b", floatArray[2]);
            floatArray[3] = scriptableWrapper.getFloat("a", floatArray[3]);
        }
        return floatArray;
    }
    
    public double getDouble(final Object o) {
        return this.getDouble(o, 0.0);
    }
    
    public double getDouble(Object value, final double n) {
        value = this.get(value, n);
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        return n;
    }
    
    public float getFloat(final Object o) {
        return this.getFloat(o, 0.0f);
    }
    
    public float getFloat(final Object o, final float n) {
        return (float)this.getDouble(o, n);
    }
    
    public float[] getFloatArray(final Object o, int i, final float n) {
        float float1 = n;
        if (this.getFloat(o, n) != n) {
            float1 = this.getFloat(o);
        }
        final Scriptable scriptable = this.getScriptable(o);
        final int n2 = 0;
        final int n3 = 0;
        if (scriptable != null && scriptable instanceof NativeArray) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            float[] array;
            Object value;
            for (array = new float[(int)Math.max(nativeArray.getLength(), i)], i = n3; i < array.length; ++i) {
                if (i < nativeArray.getLength()) {
                    value = nativeArray.get(i);
                    if (value instanceof Number) {
                        array[i] = ((Number)value).floatValue();
                        continue;
                    }
                }
                array[i] = float1;
            }
            return array;
        }
        final float[] array2 = new float[i];
        for (int j = n2; j < i; ++j) {
            array2[j] = float1;
        }
        return array2;
    }
    
    public int getInt(final Object o) {
        return this.getInt(o, 0);
    }
    
    public int getInt(final Object o, final int n) {
        return (int)this.getDouble(o, n);
    }
    
    public long getLong(final Object o) {
        return this.getLong(o, 0L);
    }
    
    public long getLong(Object value, final long n) {
        value = this.get(value, n);
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        return n;
    }
    
    public float[] getMinMaxTemplate(final Object o, final float n) {
        final float[] floatArray = this.getFloatArray(o, 2, n);
        final ScriptableObjectWrapper scriptableWrapper = this.getScriptableWrapper(o);
        if (scriptableWrapper != null) {
            floatArray[0] = scriptableWrapper.getFloat("min", floatArray[0]);
            floatArray[1] = scriptableWrapper.getFloat("max", floatArray[1]);
        }
        if (floatArray[1] < floatArray[0]) {
            floatArray[1] = floatArray[0];
        }
        return floatArray;
    }
    
    public Scriptable getScriptable(Object value) {
        value = this.get(value, null);
        if (value instanceof Scriptable) {
            return (Scriptable)value;
        }
        return null;
    }
    
    public ScriptableObjectWrapper getScriptableWrapper(final Object o) {
        final Scriptable scriptable = this.getScriptable(o);
        if (scriptable != null) {
            return new ScriptableObjectWrapper(scriptable);
        }
        return null;
    }
    
    public String getString(final Object o) {
        return this.getString(o, null);
    }
    
    public String getString(final Object o, final String s) {
        if (o instanceof Integer) {
            if (this.scriptable.has((int)o, this.scriptable)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.scriptable.get((int)o, this.scriptable));
                sb.append("");
                return sb.toString();
            }
            return s;
        }
        else {
            if (this.scriptable.has((String)o, this.scriptable)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.scriptable.get((String)o, this.scriptable));
                sb2.append("");
                return sb2.toString();
            }
            return s;
        }
    }
    
    public float[] getUVTemplate(final Object o) {
        final float[] floatArray = this.getFloatArray(o, 4, 0.0f);
        final ScriptableObjectWrapper scriptableWrapper = this.getScriptableWrapper(o);
        if (scriptableWrapper != null) {
            floatArray[0] = scriptableWrapper.getFloat("u1", floatArray[0]);
            floatArray[1] = scriptableWrapper.getFloat("v1", floatArray[1]);
            floatArray[2] = scriptableWrapper.getFloat("u2", floatArray[2]);
            floatArray[3] = scriptableWrapper.getFloat("v2", floatArray[3]);
        }
        if (floatArray[0] == 0.0f && floatArray[1] == 0.0f && floatArray[2] == 0.0f && floatArray[3] == 0.0f) {
            floatArray[3] = (floatArray[2] = 1.0f);
        }
        return floatArray;
    }
    
    public float[] getVec3Template(final Object o, final float n) {
        final float[] floatArray = this.getFloatArray(o, 3, n);
        final ScriptableObjectWrapper scriptableWrapper = this.getScriptableWrapper(o);
        if (scriptableWrapper != null) {
            floatArray[0] = scriptableWrapper.getFloat("x", floatArray[0]);
            floatArray[1] = scriptableWrapper.getFloat("y", floatArray[1]);
            floatArray[2] = scriptableWrapper.getFloat("z", floatArray[2]);
        }
        return floatArray;
    }
    
    public Scriptable getWrapped() {
        return this.scriptable;
    }
    
    public Scriptable getWrappedArray() {
        return (Scriptable)this.scriptableArray;
    }
    
    public ScriptableObject getWrappedObject() {
        return this.scriptableObject;
    }
    
    public boolean has(final Object o) {
        if (o instanceof Integer) {
            return this.scriptable.has((int)o, this.scriptable);
        }
        return this.scriptable.has((String)o, this.scriptable);
    }
    
    public void insert(int n, Object o) {
        if (this.scriptableArray == null) {
            throw new UnsupportedOperationException("insert works only on arrays!");
        }
        if (this.has(n)) {
            while (this.has(n)) {
                final Object value = this.scriptableArray.get(n);
                this.put(n, o);
                o = value;
                ++n;
            }
            this.put(n, o);
            return;
        }
        this.put(n, o);
    }
    
    public boolean isArray() {
        return this.scriptableArray != null;
    }
    
    public boolean isObject() {
        return this.scriptableObject != null;
    }
    
    public void put(final Object o, final Object o2) {
        Object wrapped = o2;
        if (o2 instanceof ScriptableObjectWrapper) {
            wrapped = ((ScriptableObjectWrapper)o2).getWrapped();
        }
        this.putRaw(o, wrapped);
    }
    
    public void putRaw(final Object o, final Object o2) {
        if (o instanceof Integer) {
            this.scriptable.put((int)o, this.scriptable, o2);
            return;
        }
        this.scriptable.put((String)o, this.scriptable, o2);
    }
    
    public boolean remove(final Object o) {
        if (this.has(o)) {
            if (o instanceof Integer) {
                this.scriptable.delete((int)o);
            }
            else {
                this.scriptable.delete((String)o);
            }
            return true;
        }
        return false;
    }
}
