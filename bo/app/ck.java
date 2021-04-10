package bo.app;

import com.appboy.models.*;
import com.appboy.support.*;
import org.json.*;

public class ck implements cc, IPutIntoJson<JSONObject>
{
    private final String a;
    private final Boolean b;
    private final Boolean c;
    private final cj d;
    
    private ck(final String a, final Boolean b, final Boolean c, final cj d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    public JSONObject a() {
        final JSONObject jsonObject = new JSONObject();
        try {
            if (!StringUtils.isNullOrEmpty(this.a)) {
                jsonObject.put("user_id", (Object)this.a);
            }
            if (this.b != null) {
                jsonObject.put("feed", (Object)this.b);
            }
            if (this.c != null) {
                jsonObject.put("triggers", (Object)this.c);
            }
            if (this.d != null) {
                jsonObject.put("config", (Object)this.d.a());
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    public boolean b() {
        final JSONObject a = this.a();
        return a.length() == 0 || (a.length() == 1 && a.has("user_id"));
    }
    
    public boolean c() {
        return this.d != null;
    }
    
    public boolean d() {
        return this.c != null;
    }
    
    public boolean e() {
        return this.b != null;
    }
    
    public boolean f() {
        return StringUtils.isNullOrEmpty(this.a) ^ true;
    }
    
    public static class a
    {
        private String a;
        private Boolean b;
        private Boolean c;
        private cj d;
        
        public a a() {
            this.b = true;
            return this;
        }
        
        public a a(final cj d) {
            this.d = d;
            return this;
        }
        
        public a a(final String a) {
            this.a = a;
            return this;
        }
        
        public a b() {
            this.c = true;
            return this;
        }
        
        public ck c() {
            return new ck(this.a, this.b, this.c, this.d, null);
        }
    }
}
