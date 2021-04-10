package bo.app;

import org.json.*;

public final class et implements er
{
    public JSONObject a() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", (Object)"open");
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    public boolean a(final fk fk) {
        return fk instanceof fn;
    }
}
