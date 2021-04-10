package bo.app;

import org.json.*;
import java.util.*;

public abstract class gj implements gl
{
    @Override
    public final gh a(final JSONArray jsonArray, final JSONArray jsonArray2) {
        final gh gh = new gh();
        this.e("", jsonArray, jsonArray2, gh);
        return gh;
    }
    
    @Override
    public final gh a(final JSONObject jsonObject, final JSONObject jsonObject2) {
        final gh gh = new gh();
        this.c("", jsonObject, jsonObject2, gh);
        return gh;
    }
    
    protected void a(final String s, final JSONArray jsonArray, final JSONArray jsonArray2, final gh gh) {
        final String a = gm.a(jsonArray);
        if (a != null && gm.a(a, jsonArray2)) {
            final Map<Object, JSONObject> a2 = gm.a(jsonArray, a);
            final Map<Object, JSONObject> a3 = gm.a(jsonArray2, a);
            for (final Object next : a2.keySet()) {
                if (!a3.containsKey(next)) {
                    gh.a(gm.a(s, a, next), a2.get(next));
                }
                else {
                    this.a(gm.a(s, a, next), a2.get(next), a3.get(next), gh);
                }
            }
            for (final Object next2 : a3.keySet()) {
                if (!a2.containsKey(next2)) {
                    gh.b(gm.a(s, a, next2), a3.get(next2));
                }
            }
            return;
        }
        this.d(s, jsonArray, jsonArray2, gh);
    }
    
    protected void a(final String s, final JSONObject jsonObject, final JSONObject jsonObject2, final gh gh) {
        for (final String s2 : gm.a(jsonObject2)) {
            if (!jsonObject.has(s2)) {
                gh.b(s, s2);
            }
        }
    }
    
    protected void b(final String s, final JSONArray jsonArray, final JSONArray jsonArray2, final gh gh) {
        final Map<Object, Integer> a = gm.a(gm.b(jsonArray));
        final Map<K, Integer> a2 = gm.a((Collection<K>)gm.b(jsonArray2));
        for (final Object next : a.keySet()) {
            if (!a2.containsKey(next)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("[]");
                gh.a(sb.toString(), next);
            }
            else {
                if (a2.get(next).equals(a.get(next))) {
                    continue;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append("[]: Expected ");
                sb2.append(a.get(next));
                sb2.append(" occurrence(s) of ");
                sb2.append(next);
                sb2.append(" but got ");
                sb2.append(a2.get(next));
                sb2.append(" occurrence(s)");
                gh.a(sb2.toString());
            }
        }
        for (final K next2 : a2.keySet()) {
            if (!a.containsKey(next2)) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(s);
                sb3.append("[]");
                gh.b(sb3.toString(), next2);
            }
        }
    }
    
    protected void b(final String s, final JSONObject jsonObject, final JSONObject jsonObject2, final gh gh) {
        for (final String s2 : gm.a(jsonObject)) {
            final Object value = jsonObject.get(s2);
            if (jsonObject2.has(s2)) {
                this.a(gm.a(s, s2), value, jsonObject2.get(s2), gh);
            }
            else {
                gh.a(s, s2);
            }
        }
    }
    
    protected void c(final String s, final JSONArray jsonArray, final JSONArray jsonArray2, final gh gh) {
        for (int i = 0; i < jsonArray.length(); ++i) {
            final Object value = jsonArray.get(i);
            final Object value2 = jsonArray2.get(i);
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("[");
            sb.append(i);
            sb.append("]");
            this.a(sb.toString(), value, value2, gh);
        }
    }
    
    protected void d(final String s, final JSONArray jsonArray, final JSONArray jsonArray2, final gh gh) {
        final HashSet<Integer> set = new HashSet<Integer>();
        int i = 0;
    Label_0189_Outer:
        while (i < jsonArray.length()) {
            final Object value = jsonArray.get(i);
            while (true) {
                for (int j = 0; j < jsonArray2.length(); ++j) {
                    final Object value2 = jsonArray2.get(j);
                    if (!set.contains(j)) {
                        if (((JSONArray)value2).getClass().equals(((JSONObject)value).getClass())) {
                            if (value instanceof JSONObject) {
                                if (!this.a((JSONObject)value, (JSONObject)value2).a()) {
                                    continue Label_0189_Outer;
                                }
                            }
                            else if (value instanceof JSONArray) {
                                if (!this.a((JSONArray)value, (JSONArray)value2).a()) {
                                    continue Label_0189_Outer;
                                }
                            }
                            else if (!value.equals(value2)) {
                                continue Label_0189_Outer;
                            }
                            set.add(j);
                            final boolean b = true;
                            if (!b) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append(s);
                                sb.append("[");
                                sb.append(i);
                                sb.append("] Could not find match for element ");
                                sb.append(value);
                                gh.a(sb.toString());
                                return;
                            }
                            ++i;
                            continue Label_0189_Outer;
                        }
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
}
