package bo.app;

import org.json.*;

public class cn
{
    private final long a;
    private final long b;
    private final boolean c;
    private final JSONArray d;
    
    public cn(final String s) {
        this.c = false;
        this.a = -1L;
        this.b = -1L;
        this.d = new JSONArray().put((Object)new JSONObject(s));
    }
    
    public cn(final JSONObject jsonObject) {
        this.a = jsonObject.optLong("last_card_updated_at", -1L);
        this.b = jsonObject.optLong("last_full_sync_at", -1L);
        this.c = jsonObject.optBoolean("full_sync", false);
        this.d = jsonObject.optJSONArray("cards");
    }
    
    public long a() {
        return this.b;
    }
    
    public long b() {
        return this.a;
    }
    
    public boolean c() {
        return this.c;
    }
    
    public JSONArray d() {
        return this.d;
    }
}
