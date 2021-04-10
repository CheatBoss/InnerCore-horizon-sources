package com.appsflyer;

import android.os.*;
import java.lang.reflect.*;
import java.util.*;
import org.json.*;

public class AFHelper
{
    public static JSONObject convertToJsonObject(final Map<String, ?> map) {
        if (Build$VERSION.SDK_INT >= 19) {
            return new JSONObject((Map)map);
        }
        return \u03b9(map);
    }
    
    private static Object \u0131(final Object o) {
        if (o == null) {
            return JSONObject.NULL;
        }
        Object null = o;
        if (!(o instanceof JSONArray)) {
            if (o instanceof JSONObject) {
                return o;
            }
            if (o.equals(JSONObject.NULL)) {
                return o;
            }
            try {
                if (o instanceof Collection) {
                    final JSONArray jsonArray = new JSONArray();
                    final Iterator<Object> iterator = (Iterator<Object>)((Collection)o).iterator();
                    while (iterator.hasNext()) {
                        jsonArray.put(\u0131(iterator.next()));
                    }
                    return jsonArray;
                }
                if (o.getClass().isArray()) {
                    final int length = Array.getLength(o);
                    final JSONArray jsonArray2 = new JSONArray();
                    for (int i = 0; i < length; ++i) {
                        jsonArray2.put(\u0131(Array.get(o, i)));
                    }
                    return jsonArray2;
                }
                if (o instanceof Map) {
                    return \u03b9((Map<String, ?>)o);
                }
                Object string = o;
                if (!(o instanceof Boolean)) {
                    string = o;
                    if (!(o instanceof Byte)) {
                        string = o;
                        if (!(o instanceof Character)) {
                            string = o;
                            if (!(o instanceof Double)) {
                                string = o;
                                if (!(o instanceof Float)) {
                                    string = o;
                                    if (!(o instanceof Integer)) {
                                        string = o;
                                        if (!(o instanceof Long)) {
                                            string = o;
                                            if (!(o instanceof Short)) {
                                                if (o instanceof String) {
                                                    return o;
                                                }
                                                string = o.toString();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return string;
            }
            catch (Exception ex) {
                null = JSONObject.NULL;
            }
            return null;
        }
        return null;
    }
    
    private static JSONObject \u03b9(Map<String, ?> iterator) {
        final JSONObject jsonObject = new JSONObject();
        iterator = ((Map<Object, Object>)iterator).entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Object, Object> entry = iterator.next();
            final Object \u0131 = \u0131(entry.getValue());
            try {
                jsonObject.put((String)entry.getKey(), \u0131);
            }
            catch (JSONException ex) {}
        }
        return jsonObject;
    }
}
