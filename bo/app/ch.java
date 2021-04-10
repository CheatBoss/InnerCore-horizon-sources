package bo.app;

import com.appboy.support.*;
import org.json.*;

public final class ch implements cb
{
    private static final String a;
    private final double b;
    private final double c;
    private final Double d;
    private final Double e;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ch.class);
    }
    
    public ch(final double b, final double c, final Double d, final Double e) {
        if (ValidationUtils.isValidLocation(b, c)) {
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            return;
        }
        throw new IllegalArgumentException("Unable to create AppboyLocation. Latitude and longitude values are bounded by ±90 and ±180 respectively");
    }
    
    @Override
    public double a() {
        return this.b;
    }
    
    @Override
    public double b() {
        return this.c;
    }
    
    @Override
    public Double c() {
        return this.d;
    }
    
    @Override
    public Double d() {
        return this.e;
    }
    
    public boolean e() {
        return this.d != null;
    }
    
    public boolean f() {
        return this.e != null;
    }
    
    public JSONObject g() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", this.b);
            jsonObject.put("longitude", this.c);
            if (this.e()) {
                jsonObject.put("altitude", (Object)this.d);
            }
            if (this.f()) {
                jsonObject.put("ll_accuracy", (Object)this.e);
                return jsonObject;
            }
        }
        catch (JSONException ex) {
            AppboyLogger.e(ch.a, "Caught exception creating location Json.", (Throwable)ex);
        }
        return jsonObject;
    }
}
