package bo.app;

import org.json.*;

public final class ey implements er
{
    public JSONObject a() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", (Object)"test");
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    public boolean a(final fk fk) {
        return fk instanceof fq;
    }
}
