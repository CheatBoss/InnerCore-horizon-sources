package bo.app;

import org.json.*;

public class ff implements fd
{
    private final int a;
    
    public ff(final JSONObject jsonObject) {
        this.a = jsonObject.optInt("re_eligibility", -1);
    }
    
    @Override
    public boolean a() {
        return this.a == 0;
    }
    
    @Override
    public boolean b() {
        return this.a == -1;
    }
    
    @Override
    public Integer c() {
        final int a = this.a;
        if (a > 0) {
            return a;
        }
        return null;
    }
    
    public JSONObject d() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("re_eligibility", this.a);
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
}
