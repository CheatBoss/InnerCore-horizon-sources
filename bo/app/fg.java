package bo.app;

import org.json.*;

public class fg implements fe
{
    private final long a;
    private final long b;
    private final int c;
    private final int d;
    private final int e;
    private final fd f;
    private final int g;
    
    public fg(final JSONObject jsonObject) {
        this.a = jsonObject.optLong("start_time", -1L);
        this.b = jsonObject.optLong("end_time", -1L);
        this.c = jsonObject.optInt("priority", 0);
        this.g = jsonObject.optInt("min_seconds_since_last_trigger", -1);
        this.d = jsonObject.optInt("delay", 0);
        this.e = jsonObject.optInt("timeout", -1);
        this.f = new ff(jsonObject);
    }
    
    @Override
    public long a() {
        return this.a;
    }
    
    @Override
    public long b() {
        return this.b;
    }
    
    @Override
    public int c() {
        return this.c;
    }
    
    @Override
    public int d() {
        return this.d;
    }
    
    @Override
    public int e() {
        return this.e;
    }
    
    @Override
    public fd f() {
        return this.f;
    }
    
    @Override
    public int g() {
        return this.g;
    }
    
    public JSONObject h() {
        try {
            final JSONObject jsonObject = this.f.forJsonPut();
            jsonObject.put("start_time", this.a);
            jsonObject.put("end_time", this.b);
            jsonObject.put("priority", this.c);
            jsonObject.put("min_seconds_since_last_trigger", this.g);
            jsonObject.put("timeout", this.e);
            jsonObject.put("delay", this.d);
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
}
