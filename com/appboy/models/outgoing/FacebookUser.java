package com.appboy.models.outgoing;

import com.appboy.models.*;
import com.appboy.enums.*;
import java.util.*;
import com.appboy.support.*;
import org.json.*;

public class FacebookUser implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private final String f;
    private final String g;
    private final Gender h;
    private final Integer i;
    private final Collection<String> j;
    private final String k;
    
    static {
        a = AppboyLogger.getAppboyLogTag(FacebookUser.class);
    }
    
    public FacebookUser(final String b, final String c, final String d, final String e, final String f, final String g, final Gender h, final Integer i, final Collection<String> j, final String k) {
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;
        this.k = k;
    }
    
    private JSONArray a() {
        final JSONArray jsonArray = new JSONArray();
        for (final String s : this.j) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", (Object)s);
            jsonArray.put((Object)jsonObject);
        }
        return jsonArray;
    }
    
    @Override
    public JSONObject forJsonPut() {
        final JSONObject jsonObject = new JSONObject();
        try {
            if (!StringUtils.isNullOrBlank(this.b)) {
                jsonObject.put("id", (Object)this.b);
            }
            if (!StringUtils.isNullOrBlank(this.c)) {
                jsonObject.put("first_name", (Object)this.c);
            }
            if (!StringUtils.isNullOrBlank(this.d)) {
                jsonObject.put("last_name", (Object)this.d);
            }
            if (!StringUtils.isNullOrBlank(this.e)) {
                jsonObject.put("email", (Object)this.e);
            }
            if (!StringUtils.isNullOrBlank(this.f)) {
                jsonObject.put("bio", (Object)this.f);
            }
            if (!StringUtils.isNullOrBlank(this.k)) {
                jsonObject.put("birthday", (Object)this.k);
            }
            if (!StringUtils.isNullOrBlank(this.g)) {
                final JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("name", (Object)this.g);
                jsonObject.put("location", (Object)jsonObject2);
            }
            if (this.h != null) {
                jsonObject.put("gender", (Object)this.h.forJsonPut());
            }
            jsonObject.put("num_friends", (Object)this.i);
            if (this.j != null && !this.j.isEmpty()) {
                jsonObject.put("likes", (Object)this.a());
                return jsonObject;
            }
        }
        catch (JSONException ex) {
            AppboyLogger.e(FacebookUser.a, "Caught exception creating facebook user Json.", (Throwable)ex);
        }
        return jsonObject;
    }
}
