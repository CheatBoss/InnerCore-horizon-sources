package bo.app;

import android.net.*;
import java.util.*;
import com.appboy.support.*;
import org.json.*;

public final class cr extends cp
{
    private static final String b;
    private final long c;
    private final long d;
    private final String e;
    
    static {
        b = AppboyLogger.getAppboyLogTag(cr.class);
    }
    
    public cr(final String s, final long c, final long d, final String e) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("content_cards/sync");
        super(Uri.parse(sb.toString()), null);
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    @Override
    public void a(final ad ad, final cm cm) {
        AppboyLogger.d(cr.b, "ContentCardsSyncRequest executed successfully.");
    }
    
    @Override
    public void a(final Map<String, String> map) {
        super.a(map);
        map.put("X-Braze-DataRequest", "true");
        map.put("X-Braze-ContentCardsRequest", "true");
    }
    
    @Override
    public JSONObject g() {
        final JSONObject g = super.g();
        if (g == null) {
            return null;
        }
        try {
            g.put("last_full_sync_at", this.d);
            g.put("last_card_updated_at", this.c);
            if (!StringUtils.isNullOrBlank(this.e)) {
                g.put("user_id", (Object)this.e);
            }
            return g;
        }
        catch (JSONException ex) {
            AppboyLogger.w(cr.b, "Experienced JSONException while creating Content Cards request. Returning null.", (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public boolean h() {
        return false;
    }
    
    @Override
    public y i() {
        return y.b;
    }
}
