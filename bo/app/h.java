package bo.app;

import com.appboy.support.*;
import java.util.*;
import org.json.*;
import java.net.*;

final class h implements g
{
    private static final String a;
    private final g b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(h.class);
    }
    
    public h(final g b) {
        this.b = b;
    }
    
    private String a(final Map<String, String> map) {
        final ArrayList<String> list = new ArrayList<String>();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(entry.getKey());
            sb.append(" / ");
            sb.append(entry.getValue());
            sb.append(")");
            list.add(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        final Iterator<Object> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            sb2.append(iterator2.next());
            sb2.append(", ");
        }
        if (sb2.length() == 0) {
            return "";
        }
        return sb2.substring(0, sb2.length() - 2);
    }
    
    private void a(final JSONObject jsonObject) {
        Label_0015: {
            if (jsonObject == null) {
                final String a = "none";
                break Label_0015;
            }
            try {
                final String a = ec.a(jsonObject);
                final String a2 = h.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Result [");
                sb.append(a);
                sb.append("]");
                AppboyLogger.d(a2, sb.toString());
            }
            catch (Exception ex) {
                AppboyLogger.d(h.a, "Exception while logging result: ", ex);
            }
        }
    }
    
    private void b(final URI uri, final Map<String, String> map) {
        try {
            final String a = h.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Making request to [");
            sb.append(uri.toString());
            sb.append("], with headers: [");
            sb.append(this.a(map));
            sb.append("]");
            AppboyLogger.d(a, sb.toString());
        }
        catch (Exception ex) {
            AppboyLogger.d(h.a, "Exception while logging request: ", ex);
        }
    }
    
    private void b(final URI uri, final Map<String, String> map, final JSONObject jsonObject) {
        try {
            final String a = h.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Making request to [");
            sb.append(uri.toString());
            sb.append("], with headers: [");
            sb.append(this.a(map));
            sb.append("] and JSON parameters: [");
            sb.append(ec.a(jsonObject));
            sb.append("]");
            AppboyLogger.d(a, sb.toString());
        }
        catch (Exception ex) {
            AppboyLogger.d(h.a, "Exception while logging request: ", ex);
        }
    }
    
    @Override
    public JSONObject a(final URI uri, final Map<String, String> map) {
        this.b(uri, map);
        final JSONObject a = this.b.a(uri, map);
        this.a(a);
        return a;
    }
    
    @Override
    public JSONObject a(final URI uri, final Map<String, String> map, final JSONObject jsonObject) {
        this.b(uri, map, jsonObject);
        final JSONObject a = this.b.a(uri, map, jsonObject);
        this.a(a);
        return a;
    }
}
