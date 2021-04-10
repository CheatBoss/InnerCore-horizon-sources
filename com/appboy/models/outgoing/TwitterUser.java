package com.appboy.models.outgoing;

import com.appboy.models.*;
import com.appboy.support.*;
import org.json.*;

public class TwitterUser implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private final Integer b;
    private final String c;
    private final String d;
    private final String e;
    private final Integer f;
    private final Integer g;
    private final Integer h;
    private final String i;
    
    static {
        a = AppboyLogger.getAppboyLogTag(TwitterUser.class);
    }
    
    public TwitterUser(final Integer b, final String c, final String d, final String e, final Integer f, final Integer g, final Integer h, final String i) {
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
    }
    
    @Override
    public JSONObject forJsonPut() {
        final JSONObject jsonObject = new JSONObject();
        try {
            if (!StringUtils.isNullOrBlank(this.c)) {
                jsonObject.put("screen_name", (Object)this.c);
            }
            if (!StringUtils.isNullOrBlank(this.d)) {
                jsonObject.put("name", (Object)this.d);
            }
            if (!StringUtils.isNullOrBlank(this.e)) {
                jsonObject.put("description", (Object)this.e);
            }
            if (!StringUtils.isNullOrBlank(this.i)) {
                jsonObject.put("profile_image_url", (Object)this.i);
            }
            jsonObject.put("id", (Object)this.b);
            jsonObject.put("followers_count", (Object)this.f);
            jsonObject.put("friends_count", (Object)this.g);
            jsonObject.put("statuses_count", (Object)this.h);
            return jsonObject;
        }
        catch (JSONException ex) {
            AppboyLogger.e(TwitterUser.a, "Caught exception creating twitter user Json.", (Throwable)ex);
            return jsonObject;
        }
    }
}
