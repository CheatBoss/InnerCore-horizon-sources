package com.appboy.models;

import com.appboy.support.*;
import java.util.*;
import org.json.*;

public class GcmMessage implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private final String b;
    private final String c;
    private final Map<String, String> d;
    private final String e;
    private final String f;
    private final Integer g;
    
    static {
        a = AppboyLogger.getAppboyLogTag(GcmMessage.class);
    }
    
    public GcmMessage(final String b, final String c, final Map<String, String> d, final String e, final String f, final Integer g) {
        this.b = b;
        this.c = c;
        if (d != null) {
            this.d = d;
        }
        else {
            this.d = new HashMap<String, String>();
        }
        this.e = e;
        this.f = f;
        this.g = g;
    }
    
    @Override
    public JSONObject forJsonPut() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", (Object)this.b);
            jsonObject.put("content", (Object)this.c);
            jsonObject.put("extras", (Object)new JSONObject((Map)this.d));
            if (this.e != null) {
                jsonObject.put("collapse_key", (Object)this.e);
            }
            if (this.f != null) {
                jsonObject.put("campaign_id", (Object)this.f);
            }
            if (this.g != null) {
                jsonObject.put("notification_id", (Object)this.g);
                return jsonObject;
            }
        }
        catch (JSONException ex) {
            AppboyLogger.e(GcmMessage.a, "Caught exception creating gcm message Json.", (Throwable)ex);
        }
        return jsonObject;
    }
    
    public String getCampaignId() {
        return this.f;
    }
    
    public String getContent() {
        return this.c;
    }
    
    public Map<String, String> getExtras() {
        return this.d;
    }
    
    public Integer getNotificationId() {
        return this.g;
    }
    
    public String getTitle() {
        return this.b;
    }
}
