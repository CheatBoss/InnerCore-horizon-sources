package com.appboy.models.outgoing;

import com.appboy.models.*;
import com.appboy.support.*;
import org.json.*;

public class AttributionData implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AttributionData.class);
    }
    
    public AttributionData(final String b, final String c, final String d, final String e) {
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    @Override
    public JSONObject forJsonPut() {
        final JSONObject jsonObject = new JSONObject();
        try {
            if (!StringUtils.isNullOrBlank(this.b)) {
                jsonObject.put("source", (Object)this.b);
            }
            if (!StringUtils.isNullOrBlank(this.c)) {
                jsonObject.put("campaign", (Object)this.c);
            }
            if (!StringUtils.isNullOrBlank(this.d)) {
                jsonObject.put("adgroup", (Object)this.d);
            }
            if (!StringUtils.isNullOrBlank(this.e)) {
                jsonObject.put("ad", (Object)this.e);
                return jsonObject;
            }
        }
        catch (JSONException ex) {
            AppboyLogger.e(AttributionData.a, "Caught exception creating AttributionData Json.", (Throwable)ex);
        }
        return jsonObject;
    }
}
