package bo.app;

import org.json.*;
import com.appboy.support.*;

public final class ep extends ez
{
    private static final String b;
    private String c;
    
    static {
        b = AppboyLogger.getAppboyLogTag(ep.class);
    }
    
    public ep(final JSONObject jsonObject) {
        super(jsonObject);
        this.c = jsonObject.getJSONObject("data").getString("event_name");
    }
    
    @Override
    public JSONObject a() {
        final JSONObject a = super.a();
        try {
            a.put("type", (Object)"custom_event_property");
            final JSONObject jsonObject = a.getJSONObject("data");
            jsonObject.put("event_name", (Object)this.c);
            a.put("data", (Object)jsonObject);
            return a;
        }
        catch (JSONException ex) {
            AppboyLogger.e(ep.b, "Caught exception creating CustomEventWithPropertiesTriggerCondition Json.", (Throwable)ex);
            return a;
        }
    }
    
    @Override
    public boolean a(final fk fk) {
        if (fk instanceof fj) {
            final fj fj = (fj)fk;
            if (!StringUtils.isNullOrBlank(fj.a()) && fj.a().equals(this.c)) {
                return super.a(fk);
            }
        }
        return false;
    }
}
