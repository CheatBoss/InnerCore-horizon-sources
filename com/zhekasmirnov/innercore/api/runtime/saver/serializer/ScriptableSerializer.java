package com.zhekasmirnov.innercore.api.runtime.saver.serializer;

import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.runtime.saver.*;
import java.util.*;
import org.mozilla.javascript.*;
import org.json.*;

public class ScriptableSerializer
{
    private static Object avoidInvalidJsonValues(final Object o) {
        if (o instanceof Undefined) {
            return null;
        }
        if (o instanceof Number) {
            final Number n = (Number)o;
            final double doubleValue = n.doubleValue();
            final float floatValue = n.floatValue();
            if (Double.isNaN(doubleValue) || Float.isNaN(floatValue) || Double.isInfinite(doubleValue) || Float.isInfinite(floatValue)) {
                return 0.0f;
            }
        }
        return o;
    }
    
    public static String jsonToString(final Object o) {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(o);
        return sb.toString();
    }
    
    public static Object scriptableFromJson(final Object o) {
        if (o instanceof JSONObject) {
            final JSONObject jsonObject = (JSONObject)o;
            final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s = keys.next();
                empty.put(s, (Scriptable)empty, scriptableFromJson(jsonObject.opt(s)));
            }
            return ObjectSaverRegistry.readObject(empty);
        }
        if (o instanceof JSONArray) {
            final JSONArray jsonArray = (JSONArray)o;
            final ArrayList<Object> list = new ArrayList<Object>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                list.add(scriptableFromJson(jsonArray.opt(i)));
            }
            return ScriptableObjectHelper.createArray(list);
        }
        return o;
    }
    
    public static Object scriptableToJson(final Object o, final SerializationErrorHandler serializationErrorHandler) {
        return scriptableToJson0(o, serializationErrorHandler, new HashSet<ScriptableObject>());
    }
    
    private static Object scriptableToJson0(Object saveOrSkipObject, final SerializationErrorHandler serializationErrorHandler, final Set<ScriptableObject> set) {
        saveOrSkipObject = ObjectSaverRegistry.saveOrSkipObject(saveOrSkipObject);
        if (!(saveOrSkipObject instanceof ScriptableObject)) {
            return avoidInvalidJsonValues(saveOrSkipObject);
        }
        saveOrSkipObject = saveOrSkipObject;
        if (set.contains(saveOrSkipObject)) {
            return null;
        }
        set.add((ScriptableObject)saveOrSkipObject);
        final boolean b = saveOrSkipObject instanceof NativeArray;
        final int n = 0;
        int i = 0;
        if (b) {
            final JSONArray jsonArray = new JSONArray();
            for (Object[] array = ((NativeArray)saveOrSkipObject).toArray(); i < array.length; ++i) {
                jsonArray.put(scriptableToJson0(array[i], serializationErrorHandler, set));
            }
            set.remove(saveOrSkipObject);
            return jsonArray;
        }
        final JSONObject jsonObject = new JSONObject();
        final Object[] ids = ((ScriptableObject)saveOrSkipObject).getIds();
        for (int length = ids.length, j = n; j < length; ++j) {
            final Object o = ids[j];
            final Object value = ((ScriptableObject)saveOrSkipObject).get(o);
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append(o);
                sb.append("");
                jsonObject.put(sb.toString(), scriptableToJson0(value, serializationErrorHandler, set));
            }
            catch (JSONException ex) {
                if (serializationErrorHandler != null) {
                    serializationErrorHandler.handle((Exception)ex);
                }
            }
        }
        set.remove(saveOrSkipObject);
        return jsonObject;
    }
    
    public static Object stringToJson(final String s) throws JSONException {
        if (s == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"a\": ");
        sb.append(s);
        sb.append("}");
        return new JSONObject(sb.toString()).get("a");
    }
    
    public interface SerializationErrorHandler
    {
        void handle(final Exception p0);
    }
}
