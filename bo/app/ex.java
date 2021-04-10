package bo.app;

import org.json.*;
import com.appboy.support.*;

public final class ex implements er
{
    private String a;
    
    ex() {
    }
    
    public ex(JSONObject optJSONObject) {
        optJSONObject = optJSONObject.optJSONObject("data");
        if (optJSONObject != null && !optJSONObject.isNull("campaign_id")) {
            this.a = optJSONObject.optString("campaign_id", (String)null);
        }
    }
    
    public JSONObject a() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", (Object)"push_click");
            if (this.a != null) {
                final JSONObject jsonObject2 = new JSONObject();
                jsonObject2.putOpt("campaign_id", (Object)this.a);
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
        if (fk instanceof fp) {
            if (StringUtils.isNullOrBlank(this.a)) {
                return true;
            }
            final fp fp = (fp)fk;
            if (!StringUtils.isNullOrBlank(fp.a()) && fp.a().equals(this.a)) {
                return true;
            }
        }
        return false;
    }
}
