package bo.app;

import org.json.*;

public final class gf
{
    public static gh a(final gc gc, final gc gc2) {
        final gh gh = new gh();
        if (!gc.a().equals(gc2.a())) {
            gh.a("");
        }
        return gh;
    }
    
    public static gh a(final String s, final String s2, final gg gg) {
        return a(s, s2, a(gg));
    }
    
    public static gh a(final String s, final String s2, final gl gl) {
        final Object a = gi.a(s);
        final Object a2 = gi.a(s2);
        final boolean b = a instanceof JSONObject;
        if (b && a2 instanceof JSONObject) {
            return a((JSONObject)a, (JSONObject)a2, gl);
        }
        if (a instanceof JSONArray && a2 instanceof JSONArray) {
            return a((JSONArray)a, (JSONArray)a2, gl);
        }
        if (a instanceof gc && a2 instanceof gc) {
            return a((gc)a, (gc)a2);
        }
        if (b) {
            return new gh().a("", a, a2);
        }
        return new gh().a("", a, a2);
    }
    
    public static gh a(final JSONArray jsonArray, final JSONArray jsonArray2, final gl gl) {
        return gl.a(jsonArray, jsonArray2);
    }
    
    public static gh a(final JSONObject jsonObject, final JSONObject jsonObject2, final gg gg) {
        return a(jsonObject, jsonObject2, a(gg));
    }
    
    public static gh a(final JSONObject jsonObject, final JSONObject jsonObject2, final gl gl) {
        return gl.a(jsonObject, jsonObject2);
    }
    
    private static gl a(final gg gg) {
        return new gk(gg);
    }
}
