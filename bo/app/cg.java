package bo.app;

import java.util.*;
import com.appboy.models.outgoing.*;
import com.appboy.support.*;
import com.appboy.enums.inappmessage.*;
import com.appboy.models.*;
import java.math.*;
import java.io.*;
import org.json.*;

public final class cg implements ca
{
    private static final String a;
    private final w b;
    private final JSONObject c;
    private final double d;
    private final String e;
    private String f;
    private ce g;
    
    static {
        a = AppboyLogger.getAppboyLogTag(cg.class);
    }
    
    private cg(final w w, final JSONObject jsonObject) {
        this(w, jsonObject, du.b());
    }
    
    private cg(final w w, final JSONObject jsonObject, final double n) {
        this(w, jsonObject, n, UUID.randomUUID().toString());
    }
    
    private cg(final w b, final JSONObject c, final double d, final String e) {
        this.f = null;
        this.g = null;
        if (c == null) {
            throw new NullPointerException("Event data cannot be null");
        }
        if (b.a() != null) {
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            return;
        }
        throw new NullPointerException("Event type cannot be null");
    }
    
    private cg(final w b, final JSONObject c, final double d, final String e, final String f, final String s) {
        this.f = null;
        this.g = null;
        if (c == null) {
            throw new NullPointerException("Event data cannot be null");
        }
        if (b.a() != null) {
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            if (s != null) {
                this.g = ce.a(s);
            }
            return;
        }
        throw new NullPointerException("Event type cannot be null");
    }
    
    public static cg a(final long n) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("d", n);
        return new cg(w.A, jsonObject);
    }
    
    public static cg a(final av av, final ce ce) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("nop", true);
        final String b = b(av, ce);
        final String a = a(av);
        final StringBuilder sb = new StringBuilder(b);
        sb.append("\n");
        sb.append(a);
        jsonObject.put("e", (Object)sb.toString());
        return new cg(w.h, jsonObject);
    }
    
    public static cg a(final cb cb) {
        return new cg(w.a, cb.forJsonPut());
    }
    
    public static cg a(final String s, final double n, final double n2) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", (Object)s);
        jsonObject.put("latitude", n);
        jsonObject.put("longitude", n2);
        return new cg(w.D, jsonObject);
    }
    
    public static cg a(final String s, final int n) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", (Object)s);
        jsonObject.put("value", n);
        return new cg(w.p, jsonObject);
    }
    
    public static cg a(final String s, final AppboyProperties appboyProperties) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("n", (Object)StringUtils.checkNotNullOrEmpty(s));
        if (appboyProperties != null && appboyProperties.size() > 0) {
            jsonObject.put("p", (Object)appboyProperties.forJsonPut());
        }
        return new cg(w.b, jsonObject);
    }
    
    public static cg a(final String s, final String s2) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", (Object)s);
        jsonObject.put("l", (Object)s2);
        return new cg(w.y, jsonObject);
    }
    
    public static cg a(final String s, final String s2, final double n, final String s3, final String s4, final String s5) {
        final w a = w.a(s);
        if (a != null) {
            return new cg(a, new JSONObject(s2), n, s3, s4, s5);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot parse eventType ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static cg a(final String s, final String s2, final String s3) {
        return new cg(w.u, d(s, s2, s3));
    }
    
    public static cg a(final String s, final String s2, final String s3, final InAppMessageFailureType inAppMessageFailureType) {
        return new cg(w.x, b(s, s2, s3, inAppMessageFailureType));
    }
    
    public static cg a(final String s, final String s2, final String s3, final MessageButton messageButton) {
        return new cg(w.w, a(s, s2, s3, a(messageButton), null));
    }
    
    public static cg a(final String s, final String s2, final String s3, final String s4) {
        return new cg(w.w, a(s, s2, s3, s4, null));
    }
    
    public static cg a(final String s, final String s2, BigDecimal a, final int n, final AppboyProperties appboyProperties) {
        a = ee.a(a);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("pid", (Object)s);
        jsonObject.put("c", (Object)s2);
        jsonObject.put("p", a.doubleValue());
        jsonObject.put("q", n);
        if (appboyProperties != null && appboyProperties.size() > 0) {
            jsonObject.put("pr", (Object)appboyProperties.forJsonPut());
        }
        return new cg(w.c, jsonObject);
    }
    
    public static cg a(final String s, final String[] array) {
        JSONArray jsonArray;
        if (array == null) {
            jsonArray = null;
        }
        else {
            jsonArray = new JSONArray();
        }
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                jsonArray.put((Object)array[i]);
            }
        }
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", (Object)s);
        if (array == null) {
            jsonObject.put("value", JSONObject.NULL);
        }
        else {
            jsonObject.put("value", (Object)jsonArray);
        }
        return new cg(w.s, jsonObject);
    }
    
    public static cg a(final Throwable t, final ce ce) {
        final String b = b(t, ce);
        final String a = a(t);
        final StringBuilder sb = new StringBuilder(b);
        sb.append("\n");
        sb.append(a);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("e", (Object)sb.toString());
        return new cg(w.h, jsonObject);
    }
    
    public static String a(final MessageButton messageButton) {
        if (messageButton != null) {
            return String.valueOf(messageButton.getId());
        }
        return null;
    }
    
    static String a(final Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        String s2;
        final String s = s2 = stringWriter.toString();
        if (s.length() > 5000) {
            s2 = s.substring(0, 5000);
        }
        return s2;
    }
    
    static JSONObject a(String forJsonPut, final String s, final String s2, final String s3, final InAppMessageFailureType inAppMessageFailureType) {
        final JSONObject jsonObject = new JSONObject();
        if (!StringUtils.isNullOrEmpty(forJsonPut)) {
            final JSONArray jsonArray = new JSONArray();
            jsonArray.put((Object)forJsonPut);
            jsonObject.put("campaign_ids", (Object)jsonArray);
        }
        if (!StringUtils.isNullOrEmpty(s)) {
            final JSONArray jsonArray2 = new JSONArray();
            jsonArray2.put((Object)s);
            jsonObject.put("card_ids", (Object)jsonArray2);
        }
        if (!StringUtils.isNullOrEmpty(s2)) {
            final JSONArray jsonArray3 = new JSONArray();
            jsonArray3.put((Object)s2);
            jsonObject.put("trigger_ids", (Object)jsonArray3);
        }
        if (!StringUtils.isNullOrEmpty(s3)) {
            jsonObject.put("bid", (Object)s3);
        }
        if (inAppMessageFailureType != null) {
            forJsonPut = inAppMessageFailureType.forJsonPut();
            if (!StringUtils.isNullOrEmpty(forJsonPut)) {
                jsonObject.put("error_code", (Object)forJsonPut);
            }
        }
        return jsonObject;
    }
    
    public static cg b(final String s) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("cid", (Object)s);
        return new cg(w.e, jsonObject);
    }
    
    public static cg b(final String s, final String s2) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("cid", (Object)s);
        jsonObject.put("a", (Object)s2);
        return new cg(w.d, jsonObject);
    }
    
    public static cg b(final String s, final String s2, final String s3) {
        return new cg(w.t, d(s, s2, s3));
    }
    
    static String b(final Throwable t, final ce ce) {
        final StringBuilder sb = new StringBuilder();
        String s2;
        final String s = s2 = t.toString();
        if (s.length() > 5000) {
            s2 = s.substring(0, 5000);
        }
        sb.append("exception_class: ");
        sb.append(s2);
        sb.append(",");
        sb.append("session_id: ");
        String string;
        if (ce != null) {
            string = ce.toString();
        }
        else {
            string = null;
        }
        sb.append(string);
        return sb.toString();
    }
    
    static JSONObject b(final String s, final String s2, final String s3, final InAppMessageFailureType inAppMessageFailureType) {
        return a(s, s2, s3, null, inAppMessageFailureType);
    }
    
    public static cg c(final String s) {
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put((Object)s);
        jsonObject.put("ids", (Object)jsonArray);
        return new cg(w.i, jsonObject);
    }
    
    public static cg c(final String s, final String s2) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("cid", (Object)s);
        jsonObject.put("a", (Object)s2);
        return new cg(w.f, jsonObject);
    }
    
    public static cg c(final String s, final String s2, final String s3) {
        return new cg(w.v, d(s, s2, s3));
    }
    
    public static cg d(final String s) {
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put((Object)s);
        jsonObject.put("ids", (Object)jsonArray);
        return new cg(w.j, jsonObject);
    }
    
    public static cg d(final String s, final String s2) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("geo_id", (Object)s);
        jsonObject.put("event_type", (Object)s2);
        return new cg(w.k, jsonObject);
    }
    
    static JSONObject d(final String s, final String s2, final String s3) {
        return a(s, s2, s3, null, null);
    }
    
    public static cg e(final String s) {
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put((Object)s);
        jsonObject.put("ids", (Object)jsonArray);
        return new cg(w.m, jsonObject);
    }
    
    public static cg e(final String s, final String s2) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", (Object)s);
        jsonObject.put("value", (Object)s2);
        return new cg(w.q, jsonObject);
    }
    
    public static cg f(final String s) {
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put((Object)s);
        jsonObject.put("ids", (Object)jsonArray);
        return new cg(w.n, jsonObject);
    }
    
    public static cg f(final String s, final String s2) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", (Object)s);
        jsonObject.put("value", (Object)s2);
        return new cg(w.r, jsonObject);
    }
    
    public static cg g() {
        return i("content_cards_displayed");
    }
    
    public static cg g(final String s) {
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put((Object)s);
        jsonObject.put("ids", (Object)jsonArray);
        return new cg(w.l, jsonObject);
    }
    
    public static cg h() {
        return i("feed_displayed");
    }
    
    public static cg h(final String s) {
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put((Object)s);
        jsonObject.put("ids", (Object)jsonArray);
        return new cg(w.o, jsonObject);
    }
    
    public static cg i() {
        return i("feedback_displayed");
    }
    
    public static cg i(final String s) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("n", (Object)s);
        return new cg(w.g, jsonObject);
    }
    
    public static cg j() {
        return new cg(w.z, new JSONObject());
    }
    
    public static cg j(final String s) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("cid", (Object)s);
        return new cg(w.C, jsonObject);
    }
    
    public static cg k(final String s) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", (Object)s);
        return new cg(w.E, jsonObject);
    }
    
    @Override
    public double a() {
        return this.d;
    }
    
    @Override
    public void a(final ce g) {
        if (this.g == null) {
            this.g = g;
            return;
        }
        final String a = cg.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Session id can only be set once. Doing nothing. Given session id: ");
        sb.append(g);
        AppboyLogger.d(a, sb.toString());
    }
    
    @Override
    public void a(final String f) {
        if (this.f == null) {
            this.f = f;
            return;
        }
        final String a = cg.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("User id can only be set once. Doing nothing. Given user id: ");
        sb.append(f);
        AppboyLogger.d(a, sb.toString());
    }
    
    @Override
    public w b() {
        return this.b;
    }
    
    @Override
    public JSONObject c() {
        return this.c;
    }
    
    @Override
    public String d() {
        return this.e;
    }
    
    @Override
    public String e() {
        return this.f;
    }
    
    @Override
    public ce f() {
        return this.g;
    }
    
    public JSONObject k() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", (Object)this.b.a());
            jsonObject.put("data", (Object)this.c);
            jsonObject.put("time", this.d);
            if (!StringUtils.isNullOrEmpty(this.f)) {
                jsonObject.put("user_id", (Object)this.f);
            }
            if (this.g != null) {
                jsonObject.put("session_id", (Object)this.g.b());
                return jsonObject;
            }
        }
        catch (JSONException ex) {
            AppboyLogger.e(cg.a, "Caught exception creating Braze event Json.", (Throwable)ex);
        }
        return jsonObject;
    }
    
    @Override
    public String toString() {
        final JSONObject k = this.k();
        if (k != null && k.length() > 0) {
            return k.toString();
        }
        return "";
    }
}
