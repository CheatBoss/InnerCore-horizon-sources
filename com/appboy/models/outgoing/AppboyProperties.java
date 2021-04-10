package com.appboy.models.outgoing;

import com.appboy.models.*;
import org.json.*;
import com.appboy.support.*;
import java.util.*;
import bo.app.*;

public final class AppboyProperties implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private JSONObject b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyProperties.class);
    }
    
    public AppboyProperties() {
        this.b = new JSONObject();
    }
    
    public AppboyProperties(final JSONObject b) {
        this.b = new JSONObject();
        this.b = b;
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator keys = b.keys();
        while (keys.hasNext()) {
            list.add(keys.next());
        }
        for (final String s : list) {
            if (a(s)) {
                try {
                    JSONObject jsonObject;
                    if (b.get(s) instanceof String) {
                        if (b(b.getString(s))) {
                            continue;
                        }
                        jsonObject = this.b;
                    }
                    else {
                        if (b.get(s) != JSONObject.NULL) {
                            continue;
                        }
                        jsonObject = this.b;
                    }
                    jsonObject.remove(s);
                    continue;
                }
                catch (JSONException ex) {
                    final String a = AppboyProperties.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Caught json exception validating property with key name: ");
                    sb.append(s);
                    AppboyLogger.e(a, sb.toString(), (Throwable)ex);
                    continue;
                }
                break;
            }
            this.b.remove(s);
        }
    }
    
    static boolean a(String s) {
        String s2;
        if (StringUtils.isNullOrBlank(s)) {
            s2 = AppboyProperties.a;
            s = "The AppboyProperties key cannot be null or contain only whitespaces. Not adding property.";
        }
        else {
            if (!s.startsWith("$")) {
                return true;
            }
            s2 = AppboyProperties.a;
            s = "The leading character in the key string may not be '$'. Not adding property.";
        }
        AppboyLogger.w(s2, s);
        return false;
    }
    
    static boolean b(final String s) {
        if (StringUtils.isNullOrBlank(s)) {
            AppboyLogger.w(AppboyProperties.a, "The AppboyProperties value cannot be null or contain only whitespaces. Not adding property.");
            return false;
        }
        return true;
    }
    
    public AppboyProperties addProperty(final String s, final double n) {
        if (!a(s)) {
            return this;
        }
        try {
            this.b.put(ValidationUtils.ensureAppboyFieldLength(s), n);
            return this;
        }
        catch (JSONException ex) {
            AppboyLogger.e(AppboyProperties.a, "Caught json exception trying to add property.", (Throwable)ex);
            return this;
        }
    }
    
    public AppboyProperties addProperty(final String s, final int n) {
        if (!a(s)) {
            return this;
        }
        try {
            this.b.put(ValidationUtils.ensureAppboyFieldLength(s), n);
            return this;
        }
        catch (JSONException ex) {
            AppboyLogger.e(AppboyProperties.a, "Caught json exception trying to add property.", (Throwable)ex);
            return this;
        }
    }
    
    public AppboyProperties addProperty(final String s, final String s2) {
        if (a(s)) {
            if (!b(s2)) {
                return this;
            }
            try {
                this.b.put(ValidationUtils.ensureAppboyFieldLength(s), (Object)ValidationUtils.ensureAppboyFieldLength(s2));
                return this;
            }
            catch (JSONException ex) {
                AppboyLogger.e(AppboyProperties.a, "Caught json exception trying to add property.", (Throwable)ex);
            }
        }
        return this;
    }
    
    public AppboyProperties addProperty(final String s, final Date date) {
        if (!a(s)) {
            return this;
        }
        if (date == null) {
            return this;
        }
        try {
            this.b.put(ValidationUtils.ensureAppboyFieldLength(s), (Object)du.a(date, u.b));
            return this;
        }
        catch (JSONException ex) {
            AppboyLogger.e(AppboyProperties.a, "Caught json exception trying to add property.", (Throwable)ex);
            return this;
        }
    }
    
    public AppboyProperties addProperty(final String s, final boolean b) {
        if (!a(s)) {
            return this;
        }
        try {
            this.b.put(ValidationUtils.ensureAppboyFieldLength(s), b);
            return this;
        }
        catch (JSONException ex) {
            AppboyLogger.e(AppboyProperties.a, "Caught json exception trying to add property.", (Throwable)ex);
            return this;
        }
    }
    
    @Override
    public JSONObject forJsonPut() {
        return this.b;
    }
    
    public int size() {
        return this.b.length();
    }
}
