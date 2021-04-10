package bo.app;

import com.appboy.models.*;
import com.appboy.support.*;
import org.json.*;

public class cj implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private final long b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(cj.class);
    }
    
    public cj(final long b) {
        this.b = b;
    }
    
    public JSONObject a() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("config_time", this.b);
            return jsonObject;
        }
        catch (JSONException ex) {
            AppboyLogger.d(cj.a, "Caught exception creating config params json.", (Throwable)ex);
            return null;
        }
    }
}
