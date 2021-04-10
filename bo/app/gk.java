package bo.app;

import org.json.*;

public class gk extends gj
{
    gg a;
    
    public gk(final gg a) {
        this.a = a;
    }
    
    @Override
    public void a(final String s, final Object o, final Object o2, final gh gh) {
        if (o instanceof Number && o2 instanceof Number) {
            if (((Number)o).doubleValue() != ((Number)o2).doubleValue()) {
                gh.a(s, o, o2);
            }
        }
        else if (o.getClass().isAssignableFrom(o2.getClass())) {
            if (o instanceof JSONArray) {
                this.e(s, (JSONArray)o, (JSONArray)o2, gh);
                return;
            }
            if (o instanceof JSONObject) {
                this.c(s, (JSONObject)o, (JSONObject)o2, gh);
                return;
            }
            if (!o.equals(o2)) {
                gh.a(s, o, o2);
            }
        }
        else {
            gh.a(s, o, o2);
        }
    }
    
    @Override
    public void c(final String s, final JSONObject jsonObject, final JSONObject jsonObject2, final gh gh) {
        this.b(s, jsonObject, jsonObject2, gh);
        if (!this.a.a()) {
            this.a(s, jsonObject, jsonObject2, gh);
        }
    }
    
    @Override
    public void e(final String s, final JSONArray jsonArray, final JSONArray jsonArray2, final gh gh) {
        if (jsonArray.length() != jsonArray2.length()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("[]: Expected ");
            sb.append(jsonArray.length());
            sb.append(" values but got ");
            sb.append(jsonArray2.length());
            gh.a(sb.toString());
            return;
        }
        if (jsonArray.length() == 0) {
            return;
        }
        if (this.a.b()) {
            this.c(s, jsonArray, jsonArray2, gh);
            return;
        }
        if (gm.c(jsonArray)) {
            this.b(s, jsonArray, jsonArray2, gh);
            return;
        }
        if (gm.d(jsonArray)) {
            this.a(s, jsonArray, jsonArray2, gh);
            return;
        }
        this.d(s, jsonArray, jsonArray2, gh);
    }
}
