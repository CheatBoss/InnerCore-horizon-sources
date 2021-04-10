package bo.app;

import com.appboy.models.*;
import com.appboy.support.*;
import org.json.*;

public class cd implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private final ce b;
    private final double c;
    private volatile Double d;
    private volatile boolean e;
    
    static {
        a = AppboyLogger.getAppboyLogTag(cd.class);
    }
    
    public cd(final ce ce, final double n) {
        this(ce, n, null, false);
    }
    
    public cd(final ce b, final double c, final Double d, final boolean e) {
        this.e = false;
        this.b = b;
        this.c = c;
        this.e = e;
        this.d = d;
    }
    
    public cd(final JSONObject jsonObject) {
        this.e = false;
        this.b = ce.a(jsonObject.getString("session_id"));
        this.c = jsonObject.getDouble("start_time");
        this.e = jsonObject.getBoolean("is_sealed");
        if (jsonObject.has("end_time")) {
            this.d = jsonObject.getDouble("end_time");
        }
    }
    
    public ce a() {
        return this.b;
    }
    
    public void a(final Double d) {
        this.d = d;
    }
    
    public double b() {
        return this.c;
    }
    
    public Double c() {
        return this.d;
    }
    
    public boolean d() {
        return this.e;
    }
    
    public void e() {
        this.e = true;
        this.a(du.b());
    }
    
    public long f() {
        if (this.d == null) {
            return -1L;
        }
        final long n = (long)(this.d - this.c);
        if (n < 0L) {
            final String a = cd.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("End time '");
            sb.append(this.d);
            sb.append("' for session is less than the start time '");
            sb.append(this.c);
            sb.append("' for this session.");
            AppboyLogger.w(a, sb.toString());
        }
        return n;
    }
    
    public JSONObject g() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("session_id", (Object)this.b);
            jsonObject.put("start_time", this.c);
            jsonObject.put("is_sealed", this.e);
            if (this.d != null) {
                jsonObject.put("end_time", (Object)this.d);
                return jsonObject;
            }
        }
        catch (JSONException ex) {
            AppboyLogger.e(cd.a, "Caught exception creating Session Json.", (Throwable)ex);
        }
        return jsonObject;
    }
}
