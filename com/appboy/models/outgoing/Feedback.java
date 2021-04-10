package com.appboy.models.outgoing;

import com.appboy.models.*;
import bo.app.*;
import com.appboy.support.*;
import org.json.*;

public final class Feedback implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private final String b;
    private final String c;
    private final boolean d;
    private final ci e;
    private final String f;
    
    static {
        a = AppboyLogger.getAppboyLogTag(Feedback.class);
    }
    
    public Feedback(final String b, final String c, final boolean d, final ci e, final String f) {
        if (!StringUtils.isNullOrBlank(b)) {
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            return;
        }
        throw new IllegalArgumentException("Message cannot be null or blank");
    }
    
    @Override
    public JSONObject forJsonPut() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", (Object)StringUtils.checkNotNullOrEmpty(this.b));
            jsonObject.put("reply_to", (Object)this.c);
            jsonObject.put("is_bug", this.d);
            if (this.e != null) {
                jsonObject.put("device", (Object)this.e.a());
            }
            if (!StringUtils.isNullOrEmpty(this.f)) {
                jsonObject.put("user_id", (Object)this.f);
                return jsonObject;
            }
        }
        catch (JSONException ex) {
            AppboyLogger.e(Feedback.a, "Caught exception creating feedback Json.", (Throwable)ex);
        }
        return jsonObject;
    }
    
    public String getMessage() {
        return this.b;
    }
    
    public String getReplyToEmail() {
        return this.c;
    }
    
    public boolean isReportingABug() {
        return this.d;
    }
}
