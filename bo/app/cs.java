package bo.app;

import com.appboy.support.*;
import android.net.*;
import java.util.*;
import org.json.*;

public final class cs extends cp
{
    private static final String b;
    private final ck c;
    
    static {
        b = AppboyLogger.getAppboyLogTag(cs.class);
    }
    
    public cs(final String s) {
        this(s, new ck.a().c());
    }
    
    public cs(final String s, final ck c) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("data");
        super(Uri.parse(sb.toString()), null);
        this.a(this.c = c);
    }
    
    @Override
    public void a(final ad ad, final cm cm) {
    }
    
    @Override
    public void a(final Map<String, String> map) {
        super.a(map);
        if (this.c.b()) {
            return;
        }
        map.put("X-Braze-DataRequest", "true");
        if (this.c.e()) {
            map.put("X-Braze-FeedRequest", "true");
        }
        if (this.c.d()) {
            map.put("X-Braze-TriggersRequest", "true");
        }
    }
    
    @Override
    public JSONObject g() {
        final JSONObject g = super.g();
        if (g == null) {
            return null;
        }
        try {
            if (!this.c.b()) {
                g.put("respond_with", (Object)this.c.a());
            }
            return g;
        }
        catch (JSONException ex) {
            AppboyLogger.w(cs.b, "Experienced JSONException while retrieving parameters. Returning null.", (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public boolean h() {
        return this.c.b() && super.h();
    }
    
    @Override
    public y i() {
        return y.b;
    }
}
