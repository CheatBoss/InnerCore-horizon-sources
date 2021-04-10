package bo.app;

import org.json.*;
import com.appboy.support.*;

public final class ev implements er
{
    String a;
    
    ev() {
    }
    
    public ev(JSONObject optJSONObject) {
        optJSONObject = optJSONObject.optJSONObject("data");
        if (optJSONObject != null && !optJSONObject.isNull("product_id")) {
            this.a = optJSONObject.optString("product_id", (String)null);
        }
    }
    
    public JSONObject a() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", (Object)"purchase");
            if (this.a != null) {
                final JSONObject jsonObject2 = new JSONObject();
                jsonObject2.putOpt("product_id", (Object)this.a);
                jsonObject.putOpt("data", (Object)jsonObject2);
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    public boolean a(final fk fk) {
        if (fk instanceof fo) {
            if (StringUtils.isNullOrBlank(this.a)) {
                return true;
            }
            final fo fo = (fo)fk;
            if (!StringUtils.isNullOrBlank(fo.a()) && fo.a().equals(this.a)) {
                return true;
            }
        }
        return false;
    }
}
