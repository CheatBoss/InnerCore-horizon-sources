package bo.app;

import org.json.*;

public class gi
{
    public static Object a(final String s) {
        if (s.trim().startsWith("{")) {
            return new JSONObject(s);
        }
        if (s.trim().startsWith("[")) {
            return new JSONArray(s);
        }
        if (!s.trim().startsWith("\"") && !s.trim().matches("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unparsable JSON string: ");
            sb.append(s);
            throw new JSONException(sb.toString());
        }
        return new gc() {
            @Override
            public String a() {
                return s;
            }
        };
    }
}
