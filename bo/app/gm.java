package bo.app;

import org.json.*;
import java.util.*;

public final class gm
{
    private static Integer a;
    
    static {
        gm.a = new Integer(1);
    }
    
    public static String a(final String s, final String s2) {
        if ("".equals(s)) {
            return s2;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(".");
        sb.append(s2);
        return sb.toString();
    }
    
    public static String a(final String s, final String s2, final Object o) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("[");
        sb.append(s2);
        sb.append("=");
        sb.append(o);
        sb.append("]");
        return sb.toString();
    }
    
    public static String a(final JSONArray jsonArray) {
        for (final String s : a((JSONObject)jsonArray.get(0))) {
            if (a(s, jsonArray)) {
                return s;
            }
        }
        return null;
    }
    
    public static <T> Map<T, Integer> a(final Collection<T> collection) {
        final HashMap<T, Integer> hashMap = (HashMap<T, Integer>)new HashMap<Object, Integer>();
        for (final T next : collection) {
            final Integer n = hashMap.get(next);
            if (n == null) {
                hashMap.put(next, gm.a);
            }
            else {
                hashMap.put(next, new Integer(n + 1));
            }
        }
        return hashMap;
    }
    
    public static Map<Object, JSONObject> a(final JSONArray jsonArray, final String s) {
        final HashMap<Object, JSONObject> hashMap = new HashMap<Object, JSONObject>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            final JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            hashMap.put(jsonObject.get(s), jsonObject);
        }
        return hashMap;
    }
    
    public static Set<String> a(final JSONObject jsonObject) {
        final TreeSet<String> set = new TreeSet<String>();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            set.add(keys.next());
        }
        return set;
    }
    
    public static boolean a(final Object o) {
        return !(o instanceof JSONObject) && !(o instanceof JSONArray);
    }
    
    public static boolean a(final String s, final JSONArray jsonArray) {
        final HashSet<Object> set = new HashSet<Object>();
        int i = 0;
        while (i < jsonArray.length()) {
            final Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                final JSONObject jsonObject = (JSONObject)value;
                if (jsonObject.has(s)) {
                    final Object value2 = jsonObject.get(s);
                    if (a(value2) && !set.contains(value2)) {
                        set.add(value2);
                        ++i;
                        continue;
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    public static List<Object> b(final JSONArray jsonArray) {
        final ArrayList<Object> list = new ArrayList<Object>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); ++i) {
            list.add(jsonArray.get(i));
        }
        return list;
    }
    
    public static boolean c(final JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); ++i) {
            if (!a(jsonArray.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean d(final JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); ++i) {
            if (!(jsonArray.get(i) instanceof JSONObject)) {
                return false;
            }
        }
        return true;
    }
}
