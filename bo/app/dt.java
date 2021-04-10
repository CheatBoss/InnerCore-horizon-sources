package bo.app;

import android.content.*;
import org.json.*;
import com.appboy.models.outgoing.*;
import com.appboy.enums.*;
import com.appboy.support.*;
import java.util.*;

public class dt extends de<cl>
{
    private static final String c;
    final SharedPreferences a;
    final SharedPreferences b;
    private final bv d;
    private final dr e;
    private final String f;
    private final dq g;
    
    static {
        c = AppboyLogger.getAppboyLogTag(dt.class);
    }
    
    public dt(final Context context, final bv bv, final dr dr, final dq dq) {
        this(context, null, null, bv, dr, dq);
    }
    
    public dt(final Context context, final String f, String cacheFileSuffix, final bv d, final dr e, final dq g) {
        cacheFileSuffix = StringUtils.getCacheFileSuffix(context, f, cacheFileSuffix);
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.storage.user_cache.v3");
        sb.append(cacheFileSuffix);
        this.a = context.getSharedPreferences(sb.toString(), 0);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("com.appboy.storage.user_cache.push_token_store");
        sb2.append(cacheFileSuffix);
        this.b = context.getSharedPreferences(sb2.toString(), 0);
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
    }
    
    private boolean b(final JSONObject jsonObject) {
        if (this.g.a()) {
            AppboyLogger.w(dt.c, "SDK is disabled. Not writing to user cache.");
            return false;
        }
        final SharedPreferences$Editor edit = this.a.edit();
        edit.putString("user_cache_attributes_object", jsonObject.toString());
        edit.apply();
        return true;
    }
    
    private boolean c(final String s, final Object o) {
        final JSONObject f = this.f();
        Label_0021: {
            if (o != null) {
                break Label_0021;
            }
            while (true) {
                try {
                    f.put(s, JSONObject.NULL);
                    return this.b(f);
                    final String c = dt.c;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to write to user object json from prefs with key: [");
                    sb.append(s);
                    sb.append("] value: [");
                    sb.append(o);
                    sb.append("] ");
                    AppboyLogger.w(c, sb.toString());
                    return false;
                    f.put(s, o);
                    return this.b(f);
                }
                catch (JSONException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    private JSONObject g() {
        final JSONObject f = this.f();
        JSONObject jsonObject = null;
        Label_0037: {
            if (f.has("custom")) {
                try {
                    jsonObject = f.getJSONObject("custom");
                    break Label_0037;
                }
                catch (JSONException ex) {
                    AppboyLogger.e(dt.c, "Could not create custom attributes json object from preferences.", (Throwable)ex);
                }
            }
            jsonObject = null;
        }
        JSONObject jsonObject2 = jsonObject;
        if (jsonObject == null) {
            jsonObject2 = new JSONObject();
        }
        return jsonObject2;
    }
    
    @Override
    void a(cl a, final boolean b) {
        while (true) {
            Label_0197: {
                if (a == null || a.a() == null) {
                    break Label_0197;
                }
                final JSONObject a2 = a.a();
                if (b) {
                    if (a2.has("push_token")) {
                        final SharedPreferences$Editor edit = this.b.edit();
                        edit.putString("push_token", a2.optString("push_token"));
                        edit.apply();
                    }
                    return;
                }
                final JSONObject f = this.f();
                a = (cl)ec.a(a2, f);
                ((JSONObject)a).remove("push_token");
                final JSONObject optJSONObject = f.optJSONObject("custom");
                final JSONObject optJSONObject2 = a2.optJSONObject("custom");
                Label_0125: {
                    if (optJSONObject == null || optJSONObject2 == null) {
                        break Label_0125;
                    }
                    try {
                        ((JSONObject)a).put("custom", (Object)ec.a(optJSONObject2, optJSONObject));
                        while (true) {
                            final SharedPreferences$Editor edit2 = this.a.edit();
                            edit2.putString("user_cache_attributes_object", ((JSONObject)a).toString());
                            edit2.apply();
                            return;
                            Block_8: {
                                break Block_8;
                                final Throwable t;
                                AppboyLogger.w(dt.c, "Failed to add merged custom attributes back to user object.", t);
                                continue;
                            }
                            ((JSONObject)a).put("custom", (Object)optJSONObject);
                            continue;
                            AppboyLogger.d(dt.c, "Tried to confirm with a null outbound user. Doing nothing.");
                            return;
                            Label_0142: {
                                ((JSONObject)a).put("custom", (Object)optJSONObject2);
                            }
                            continue;
                        }
                    }
                    // iftrue(Label_0142:, optJSONObject == null)
                    // iftrue(Label_0167:, optJSONObject2 == null)
                    catch (JSONException ex) {}
                }
            }
            final JSONException ex;
            final Throwable t = (Throwable)ex;
            continue;
        }
    }
    
    public void a(final Gender gender) {
        // monitorenter(this)
        Label_0017: {
            if (gender != null) {
                break Label_0017;
            }
            while (true) {
                try {
                    this.c("gender", null);
                    while (true) {
                        return;
                        throw;
                        this.c("gender", gender.forJsonPut());
                        continue;
                    }
                }
                // monitorexit(this)
                // monitorexit(this)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public void a(final NotificationSubscriptionType notificationSubscriptionType) {
        // monitorenter(this)
        Label_0017: {
            if (notificationSubscriptionType != null) {
                break Label_0017;
            }
            while (true) {
                try {
                    this.c("email_subscribe", null);
                    // monitorexit(this)
                    while (true) {
                        return;
                        this.c("email_subscribe", notificationSubscriptionType.forJsonPut());
                        continue;
                    }
                    // monitorexit(this)
                    throw;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public void a(final AttributionData attributionData) {
        // monitorenter(this)
        while (true) {
            Label_0033: {
                if (attributionData == null) {
                    break Label_0033;
                }
                while (true) {
                    try {
                        final JSONObject forJsonPut = attributionData.forJsonPut();
                        this.a("ab_install_attribution", forJsonPut);
                        // monitorexit(this)
                        return;
                        // monitorexit(this)
                        throw;
                    }
                    finally {
                        continue;
                    }
                    break;
                }
            }
            final JSONObject forJsonPut = null;
            continue;
        }
    }
    
    public void a(final FacebookUser facebookUser) {
        // monitorenter(this)
        while (true) {
            Label_0033: {
                if (facebookUser == null) {
                    break Label_0033;
                }
                while (true) {
                    try {
                        final JSONObject forJsonPut = facebookUser.forJsonPut();
                        this.a("facebook", forJsonPut);
                        // monitorexit(this)
                        return;
                        // monitorexit(this)
                        throw;
                    }
                    finally {
                        continue;
                    }
                    break;
                }
            }
            final JSONObject forJsonPut = null;
            continue;
        }
    }
    
    public void a(final TwitterUser twitterUser) {
        // monitorenter(this)
        while (true) {
            Label_0033: {
                if (twitterUser == null) {
                    break Label_0033;
                }
                while (true) {
                    try {
                        final JSONObject forJsonPut = twitterUser.forJsonPut();
                        this.a("twitter", forJsonPut);
                        // monitorexit(this)
                        return;
                        // monitorexit(this)
                        throw;
                    }
                    finally {
                        continue;
                    }
                    break;
                }
            }
            final JSONObject forJsonPut = null;
            continue;
        }
    }
    
    public void a(final String s) {
        synchronized (this) {
            this.c("user_id", s);
        }
    }
    
    void a(final JSONObject jsonObject) {
        final String a = this.d.a();
        if (a == null) {
            AppboyLogger.d(dt.c, "Cannot add null push token to attributes object.");
            return;
        }
        final String string = this.b.getString("push_token", (String)null);
        if (string == null || !a.equals(string)) {
            jsonObject.put("push_token", (Object)a);
        }
    }
    
    public boolean a(final int n, final Month month, final int n2) {
        // monitorenter(this)
        Label_0023: {
            if (month == null) {
                Label_0051: {
                    try {
                        AppboyLogger.w(dt.c, "Month cannot be null.");
                        // monitorexit(this)
                        return false;
                    }
                    finally {
                        break Label_0051;
                    }
                    break Label_0023;
                }
            }
            // monitorexit(this)
        }
        // monitorexit(this)
        return this.c("dob", du.a(du.a(n, month.getValue(), n2), u.a));
    }
    
    public boolean a(final String s, final long n) {
        synchronized (this) {
            return this.a(s, du.a(n));
        }
    }
    
    public boolean a(String ensureAppboyFieldLength, final Object o) {
        synchronized (this) {
            if (!CustomAttributeValidationUtils.isValidCustomAttributeKey(ensureAppboyFieldLength, this.e.m())) {
                AppboyLogger.w(dt.c, "Custom attribute key cannot be null.");
                return false;
            }
            ensureAppboyFieldLength = ValidationUtils.ensureAppboyFieldLength(ensureAppboyFieldLength);
            if (o instanceof Boolean || o instanceof Integer || o instanceof Float || o instanceof Long || o instanceof Double) {
                return this.b(ensureAppboyFieldLength, o);
            }
            if (o instanceof String) {
                return this.b(ensureAppboyFieldLength, ValidationUtils.ensureAppboyFieldLength((String)o));
            }
            if (o instanceof Date) {
                return this.b(ensureAppboyFieldLength, du.a((Date)o, u.b));
            }
            if (o instanceof String[]) {
                return this.b(ensureAppboyFieldLength, ec.a((String[])o));
            }
            final String c = dt.c;
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not add unsupported custom attribute type with key: ");
            sb.append(ensureAppboyFieldLength);
            sb.append(" and value: ");
            sb.append(o);
            AppboyLogger.w(c, sb.toString());
            return false;
        }
    }
    
    boolean a(final String s, final JSONObject jsonObject) {
        final JSONObject f = this.f();
        Label_0025: {
            if (jsonObject != null) {
                break Label_0025;
            }
            while (true) {
                try {
                    Object o = JSONObject.NULL;
                    // iftrue(Label_0045:, optJSONObject == null)
                    while (true) {
                        f.put(s, o);
                        return this.b(f);
                        final JSONObject optJSONObject = f.optJSONObject(s);
                        Block_3: {
                            break Block_3;
                            Label_0045: {
                                f.put(s, (Object)jsonObject);
                            }
                            return this.b(f);
                        }
                        o = ec.a(optJSONObject, jsonObject);
                        continue;
                    }
                    final String c = dt.c;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to write to user object json from prefs with key: [");
                    sb.append(s);
                    sb.append("] value: [");
                    sb.append(jsonObject);
                    sb.append("] ");
                    AppboyLogger.w(c, sb.toString());
                    return false;
                }
                catch (JSONException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public void b(final NotificationSubscriptionType notificationSubscriptionType) {
        // monitorenter(this)
        Label_0018: {
            if (notificationSubscriptionType != null) {
                break Label_0018;
            }
            while (true) {
                try {
                    this.c("push_subscribe", null);
                    while (true) {
                        return;
                        throw;
                        this.c("push_subscribe", notificationSubscriptionType.forJsonPut());
                        continue;
                    }
                }
                // monitorexit(this)
                // monitorexit(this)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public void b(final String s) {
        synchronized (this) {
            this.c("first_name", s);
        }
    }
    
    boolean b(final String s, final Object o) {
        final JSONObject g = this.g();
        Label_0021: {
            if (o != null) {
                break Label_0021;
            }
            while (true) {
                try {
                    g.put(s, JSONObject.NULL);
                    return this.c("custom", g);
                    final String c = dt.c;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Could not write to custom attributes json object with key: [");
                    sb.append(s);
                    sb.append("] value: [");
                    sb.append(o);
                    sb.append("] ");
                    final JSONException ex;
                    AppboyLogger.w(c, sb.toString(), (Throwable)ex);
                    return false;
                    g.put(s, o);
                    return this.c("custom", g);
                }
                catch (JSONException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public void c(final String s) {
        synchronized (this) {
            this.c("last_name", s);
        }
    }
    
    public void d() {
        synchronized (this) {
            AppboyLogger.v(dt.c, "Push token cache cleared.");
            this.b.edit().clear().apply();
        }
    }
    
    public boolean d(String c) {
        // monitorenter(this)
        String trim = c;
        Label_0016: {
            if (c == null) {
                break Label_0016;
            }
            while (true) {
                try {
                    trim = c.trim();
                    if (trim != null && !ValidationUtils.isValidEmailAddress(trim)) {
                        c = dt.c;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Email address is not valid: ");
                        sb.append(trim);
                        AppboyLogger.w(c, sb.toString());
                        // monitorexit(this)
                        return false;
                    }
                    // monitorexit(this)
                    return this.c("email", trim);
                    // monitorexit(this)
                    throw;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public cl e() {
        if (!StringUtils.isNullOrEmpty(this.f)) {
            this.a(this.f);
        }
        final JSONObject f = this.f();
        try {
            this.a(f);
        }
        catch (JSONException ex) {
            AppboyLogger.e(dt.c, "Couldn't add push token to outbound json", (Throwable)ex);
        }
        this.a.edit().clear().apply();
        return new cl(f);
    }
    
    public void e(final String s) {
        synchronized (this) {
            this.c("country", s);
        }
    }
    
    JSONObject f() {
        final String string = this.a.getString("user_cache_attributes_object", (String)null);
        if (string == null) {
            return new JSONObject();
        }
        try {
            return new JSONObject(string);
        }
        catch (JSONException ex) {
            final String c = dt.c;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to load user object json from prefs with json string: ");
            sb.append(string);
            AppboyLogger.e(c, sb.toString(), (Throwable)ex);
            return new JSONObject();
        }
    }
    
    public void f(final String s) {
        synchronized (this) {
            this.c("home_city", s);
        }
    }
    
    public void g(final String s) {
        synchronized (this) {
            this.c("language", s);
        }
    }
    
    public boolean h(String c) {
        // monitorenter(this)
        String trim = c;
        Label_0016: {
            if (c == null) {
                break Label_0016;
            }
            while (true) {
                try {
                    trim = c.trim();
                    if (trim != null && !ValidationUtils.isValidPhoneNumber(trim)) {
                        c = dt.c;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Phone number contains invalid characters (allowed are digits, spaces, or any of the following +.-()): ");
                        sb.append(trim);
                        AppboyLogger.w(c, sb.toString());
                        // monitorexit(this)
                        return false;
                    }
                    // monitorexit(this)
                    return this.c("phone", trim);
                    // monitorexit(this)
                    throw;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public void i(final String s) {
        synchronized (this) {
            this.c("image_url", s);
        }
    }
    
    public boolean j(final String s) {
        synchronized (this) {
            if (!CustomAttributeValidationUtils.isValidCustomAttributeKey(s, this.e.m())) {
                AppboyLogger.w(dt.c, "Custom attribute key cannot be null.");
                return false;
            }
            return this.b(ValidationUtils.ensureAppboyFieldLength(s), JSONObject.NULL);
        }
    }
}
