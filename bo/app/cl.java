package bo.app;

import com.appboy.models.*;
import org.json.*;

public class cl implements cc, IPutIntoJson<JSONArray>
{
    private final JSONObject a;
    private final JSONArray b;
    
    public cl(final JSONObject a) {
        this.a = a;
        (this.b = new JSONArray()).put((Object)this.a);
    }
    
    public JSONObject a() {
        return this.a;
    }
    
    @Override
    public boolean b() {
        final JSONObject a = this.a;
        boolean b = true;
        if (a != null) {
            if (a.length() == 0) {
                return true;
            }
            if (this.a.length() == 1 && this.a.has("user_id")) {
                return true;
            }
            b = false;
        }
        return b;
    }
    
    public JSONArray c() {
        return this.b;
    }
}
