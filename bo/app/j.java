package bo.app;

import com.appboy.support.*;
import java.net.*;
import java.util.*;
import org.json.*;

final class j implements g
{
    private static final String a;
    private final g b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(j.class);
    }
    
    public j(final g b) {
        this.b = b;
    }
    
    @Override
    public JSONObject a(final URI uri, final Map<String, String> map) {
        final long currentTimeMillis = System.currentTimeMillis();
        try {
            return this.b.a(uri, map);
        }
        finally {
            final long currentTimeMillis2 = System.currentTimeMillis();
            final String a = j.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Request Executed in [");
            sb.append(currentTimeMillis2 - currentTimeMillis);
            sb.append("ms] [");
            sb.append(y.a.toString());
            sb.append(":");
            sb.append(uri.toString());
            sb.append("]");
            AppboyLogger.d(a, sb.toString());
        }
    }
    
    @Override
    public JSONObject a(final URI uri, final Map<String, String> map, final JSONObject jsonObject) {
        final long currentTimeMillis = System.currentTimeMillis();
        try {
            return this.b.a(uri, map, jsonObject);
        }
        finally {
            final long currentTimeMillis2 = System.currentTimeMillis();
            final String a = j.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Request Executed in [");
            sb.append(currentTimeMillis2 - currentTimeMillis);
            sb.append("ms] [");
            sb.append(y.b.toString());
            sb.append(":");
            sb.append(uri.toString());
            sb.append("]");
            AppboyLogger.d(a, sb.toString());
        }
    }
}
