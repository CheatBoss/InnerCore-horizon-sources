package bo.app;

import com.appboy.support.*;
import java.util.*;
import com.appboy.models.*;
import org.json.*;

public final class ec extends JSONObject
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ec.class);
    }
    
    public static <TargetEnum extends Enum<TargetEnum>> TargetEnum a(final JSONObject jsonObject, final String s, final Class<TargetEnum> clazz) {
        return Enum.valueOf(clazz, jsonObject.getString(s).toUpperCase(Locale.US));
    }
    
    public static <TargetEnum extends Enum<TargetEnum>> TargetEnum a(final JSONObject jsonObject, final String s, final Class<TargetEnum> clazz, final TargetEnum targetEnum) {
        try {
            return a(jsonObject, s, clazz);
        }
        catch (Exception ex) {
            return targetEnum;
        }
    }
    
    public static String a(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return "";
        }
        try {
            return jsonObject.toString(2);
        }
        catch (JSONException ex) {
            AppboyLogger.e(ec.a, "Caught JSONException while generating pretty printed json. Returning standard toString().", (Throwable)ex);
            return jsonObject.toString();
        }
    }
    
    public static String a(final JSONObject jsonObject, final String s) {
        if (jsonObject.has(s) && !jsonObject.isNull(s)) {
            return jsonObject.optString(s, (String)null);
        }
        return null;
    }
    
    public static Map<String, String> a(final JSONObject jsonObject, final Map<String, String> map) {
        if (jsonObject != null) {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s = keys.next();
                hashMap.put(s, jsonObject.getString(s));
            }
            return hashMap;
        }
        if (map != null) {
            return map;
        }
        AppboyLogger.d(ec.a, "Cannot convert JSONObject to Map because JSONObject is null and no default was provided.");
        throw new JSONException("Cannot convert JSONObject to Map because JSONObject is null and no default was provided.");
    }
    
    public static <T> JSONArray a(final Collection<? extends IPutIntoJson<T>> collection) {
        final JSONArray jsonArray = new JSONArray();
        final Iterator<? extends IPutIntoJson<T>> iterator = collection.iterator();
        while (iterator.hasNext()) {
            jsonArray.put(((IPutIntoJson<Object>)iterator.next()).forJsonPut());
        }
        return jsonArray;
    }
    
    public static <T> JSONArray a(final T[] array) {
        final JSONArray jsonArray = new JSONArray();
        for (int length = array.length, i = 0; i < length; ++i) {
            jsonArray.put((Object)array[i]);
        }
        return jsonArray;
    }
    
    public static JSONObject a(final JSONObject jsonObject, final JSONObject jsonObject2) {
        try {
            final JSONObject jsonObject3 = new JSONObject();
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s = keys.next();
                jsonObject3.put(s, jsonObject.get(s));
            }
            final Iterator keys2 = jsonObject2.keys();
            while (keys2.hasNext()) {
                final String s2 = keys2.next();
                jsonObject3.put(s2, jsonObject2.get(s2));
            }
            return jsonObject3;
        }
        catch (JSONException ex) {
            AppboyLogger.e(ec.a, "Caught exception merging Json objects.", (Throwable)ex);
            return null;
        }
    }
}
