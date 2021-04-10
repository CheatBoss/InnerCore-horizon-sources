package bo.app;

import android.net.*;
import com.appboy.support.*;
import java.util.*;
import org.json.*;

public class dd extends cp
{
    private static final String b;
    private final long c;
    private final List<String> d;
    private final String e;
    
    static {
        b = AppboyLogger.getAppboyLogTag(dd.class);
    }
    
    public dd(final String s, final List<String> d, final long c, final String e) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("data");
        super(Uri.parse(sb.toString()), null);
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    @Override
    public void a(final ad ad, final cm cm) {
    }
    
    @Override
    public JSONObject g() {
        final JSONObject g = super.g();
        if (g == null) {
            return null;
        }
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("time", this.c);
            if (!StringUtils.isNullOrBlank(this.e)) {
                jsonObject.put("user_id", (Object)this.e);
            }
            if (!this.d.isEmpty()) {
                jsonObject.put("device_logs", (Object)new JSONArray((Collection)this.d));
            }
            final JSONArray jsonArray = new JSONArray();
            jsonArray.put((Object)jsonObject);
            g.put("test_user_data", (Object)jsonArray);
            return g;
        }
        catch (JSONException ex) {
            AppboyLogger.e(dd.b, "Experienced JSONException while retrieving parameters. Returning null.", (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public boolean h() {
        return this.d.isEmpty() && super.h();
    }
    
    @Override
    public y i() {
        return y.b;
    }
}
