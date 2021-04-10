package bo.app;

import org.json.*;
import com.appboy.support.*;

public final class ew extends ez
{
    private static final String b;
    private String c;
    
    static {
        b = AppboyLogger.getAppboyLogTag(ew.class);
    }
    
    public ew(final JSONObject jsonObject) {
        super(jsonObject);
        this.c = jsonObject.getJSONObject("data").getString("product_id");
    }
    
    @Override
    public JSONObject a() {
        final JSONObject a = super.a();
        try {
            a.put("type", (Object)"purchase_property");
            final JSONObject jsonObject = a.getJSONObject("data");
            jsonObject.put("product_id", (Object)this.c);
            a.put("data", (Object)jsonObject);
            return a;
        }
        catch (JSONException ex) {
            AppboyLogger.e(ew.b, "Caught exception creating Json.", (Throwable)ex);
            return a;
        }
    }
    
    @Override
    public boolean a(final fk fk) {
        if (fk instanceof fo) {
            if (StringUtils.isNullOrBlank(this.c)) {
                return false;
            }
            final fo fo = (fo)fk;
            if (!StringUtils.isNullOrBlank(fo.a()) && fo.a().equals(this.c)) {
                return super.a(fk);
            }
        }
        return false;
    }
}
