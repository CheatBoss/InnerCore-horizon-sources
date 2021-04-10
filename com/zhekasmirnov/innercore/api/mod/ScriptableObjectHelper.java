package com.zhekasmirnov.innercore.api.mod;

import java.util.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.json.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.utils.*;

public class ScriptableObjectHelper
{
    private static ScriptableObject defaultScope;
    
    static {
        ScriptableObjectHelper.defaultScope = Context.enter().initStandardObjects();
    }
    
    public static NativeArray createArray(final List<?> list) {
        return (NativeArray)Context.enter().newArray((Scriptable)ScriptableObjectHelper.defaultScope, list.toArray());
    }
    
    public static NativeArray createArray(final Object[] array) {
        return (NativeArray)Context.enter().newArray((Scriptable)ScriptableObjectHelper.defaultScope, array);
    }
    
    public static ScriptableObject createEmpty() {
        return (ScriptableObject)Context.enter().newObject((Scriptable)ScriptableObjectHelper.defaultScope);
    }
    
    public static NativeArray createEmptyArray() {
        return (NativeArray)Context.enter().newArray((Scriptable)ScriptableObjectHelper.defaultScope, 0);
    }
    
    public static boolean getBooleanProperty(final ScriptableObject scriptableObject, final String s, final boolean b) {
        final Object property = getProperty(scriptableObject, s, b);
        if (property instanceof Boolean) {
            return (boolean)property;
        }
        return b;
    }
    
    public static ScriptableObject getDefaultScope() {
        return ScriptableObjectHelper.defaultScope;
    }
    
    public static float getFloatProperty(final ScriptableObject scriptableObject, final String s, final float n) {
        final Object property = getProperty(scriptableObject, s, n);
        if (property instanceof Double) {
            return (float)(double)property;
        }
        if (property instanceof Integer) {
            return (float)(int)property;
        }
        if (property instanceof Float) {
            return (float)property;
        }
        return n;
    }
    
    public static int getIntProperty(final ScriptableObject scriptableObject, final String s, final int n) {
        final Object property = getProperty(scriptableObject, s, n);
        if (property instanceof Double) {
            return (int)(double)property;
        }
        if (property instanceof Integer) {
            return (int)property;
        }
        if (property instanceof Float) {
            return (int)(float)property;
        }
        return n;
    }
    
    public static Object getJavaProperty(final ScriptableObject scriptableObject, final String s, final Class clazz, final Object o) {
        try {
            return Context.jsToJava(getProperty(scriptableObject, s, o), clazz);
        }
        catch (Exception ex) {
            return o;
        }
    }
    
    public static int getLongProperty(final ScriptableObject scriptableObject, final String s, final int n) {
        final Object property = getProperty(scriptableObject, s, n);
        if (property instanceof Double) {
            return (int)(double)property;
        }
        if (property instanceof Integer) {
            return (int)property;
        }
        if (property instanceof Float) {
            return (int)(float)property;
        }
        return n;
    }
    
    public static NativeArray getNativeArrayProperty(final ScriptableObject scriptableObject, final String s, final NativeArray nativeArray) {
        final Object property = getProperty(scriptableObject, s, nativeArray);
        if (property instanceof NativeArray) {
            return (NativeArray)property;
        }
        return nativeArray;
    }
    
    public static Object getPropByPath(final ScriptableObject scriptableObject, final String s, final Object o) {
        final int index = s.indexOf(".");
        if (index == -1) {
            if (scriptableObject.has(s, (Scriptable)scriptableObject)) {
                return scriptableObject.get((Object)s);
            }
            return o;
        }
        else {
            final String substring = s.substring(0, index);
            if (!scriptableObject.has(substring, (Scriptable)scriptableObject)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(substring);
                sb.append(" not found");
                Logger.debug("INNERCORE", sb.toString());
                return o;
            }
            final Object value = scriptableObject.get((Object)substring);
            if (value instanceof ScriptableObject) {
                return getPropByPath((ScriptableObject)value, s.substring(index + 1), o);
            }
            return o;
        }
    }
    
    public static Object getProperty(final ScriptableObject scriptableObject, final String s, final Object o) {
        if (scriptableObject.has(s, (Scriptable)scriptableObject)) {
            return scriptableObject.get((Object)s);
        }
        return o;
    }
    
    public static ScriptableObject getScriptableObjectProperty(final ScriptableObject scriptableObject, final String s, final ScriptableObject scriptableObject2) {
        final Object property = getProperty(scriptableObject, s, scriptableObject2);
        if (property instanceof ScriptableObject) {
            return (ScriptableObject)property;
        }
        return scriptableObject2;
    }
    
    public static String getStringProperty(final ScriptableObject scriptableObject, final String s, final CharSequence charSequence) {
        final Object property = getProperty(scriptableObject, s, charSequence);
        if (property instanceof CharSequence) {
            return property.toString();
        }
        if (charSequence != null) {
            return charSequence.toString();
        }
        return null;
    }
    
    public static JSONObject toJSON(final ScriptableObject scriptableObject) {
        try {
            return toJSON(scriptableObject, new JSONObject());
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private static JSONObject toJSON(final ScriptableObject scriptableObject, final JSONObject jsonObject) throws JSONException {
        if (scriptableObject == null) {
            return null;
        }
        final Object[] allIds = scriptableObject.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            final Object value = scriptableObject.get(o);
            if (((ScriptableObject)value).getClass().isPrimitive()) {
                jsonObject.put(o.toString(), value);
            }
            else if (value instanceof CharSequence) {
                jsonObject.put(o.toString(), (Object)value.toString());
            }
            else if (value instanceof ScriptableObject) {
                jsonObject.put(o.toString(), (Object)toJSON((ScriptableObject)value, new JSONObject()));
            }
        }
        return jsonObject;
    }
    
    public ScriptableObject createFromString(final String s) {
        try {
            return (ScriptableObject)Compiler.assureContextForCurrentThread().evaluateString((Scriptable)new ScriptableObject() {
                public String getClassName() {
                    return "Empty Scope";
                }
            }, s, "Scriptable From String", 0, (Object)null);
        }
        catch (Exception ex) {
            UIUtils.processError(ex);
            return null;
        }
    }
}
