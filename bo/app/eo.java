package bo.app;

import org.json.*;
import com.appboy.support.*;

public final class eo implements er
{
    private String a;
    
    public eo(final JSONObject jsonObject) {
        this.a = jsonObject.getJSONObject("data").getString("event_name");
    }
    
    public JSONObject a() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", (Object)"custom_event");
            final JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("event_name", (Object)this.a);
            jsonObject.put("data", (Object)jsonObject2);
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    public boolean a(final fk fk) {
        if (fk instanceof fj) {
            final fj fj = (fj)fk;
            if (!StringUtils.isNullOrBlank(fj.a()) && fj.a().equals(this.a)) {
                return true;
            }
        }
        return false;
    }
}
