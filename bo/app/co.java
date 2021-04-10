package bo.app;

import com.appboy.support.*;
import java.util.*;
import org.json.*;

public class co
{
    private static final String a;
    private long b;
    private Set<String> c;
    private Set<String> d;
    private Set<String> e;
    private boolean f;
    private boolean g;
    private long h;
    private float i;
    private int j;
    private int k;
    private int l;
    private boolean m;
    private boolean n;
    private long o;
    private boolean p;
    
    static {
        a = AppboyLogger.getAppboyLogTag(co.class);
    }
    
    public co() {
        this.f = false;
        this.g = false;
        this.h = -1L;
        this.i = -1.0f;
        this.j = -1;
        this.k = -1;
        this.l = -1;
        this.m = false;
        this.n = false;
        this.o = -1L;
        this.p = false;
    }
    
    public co(JSONObject optJSONObject) {
        this.f = false;
        this.g = false;
        this.h = -1L;
        this.i = -1.0f;
        this.j = -1;
        this.k = -1;
        this.l = -1;
        this.m = false;
        this.n = false;
        this.o = -1L;
        this.p = false;
        this.c = this.a(optJSONObject, "events_blacklist");
        this.d = this.a(optJSONObject, "attributes_blacklist");
        this.e = this.a(optJSONObject, "purchases_blacklist");
        this.b = optJSONObject.optLong("time", 0L);
        this.o = optJSONObject.optLong("messaging_session_timeout", -1L);
        final JSONObject optJSONObject2 = optJSONObject.optJSONObject("location");
        if (optJSONObject2 != null) {
            try {
                this.g = optJSONObject2.getBoolean("enabled");
                this.f = true;
            }
            catch (JSONException ex) {
                AppboyLogger.e(co.a, "Required location collection fields were null. Using defaults.", (Throwable)ex);
                this.f = false;
            }
            final long optLong = optJSONObject2.optLong("time", -1L);
            if (optLong >= 0L) {
                this.h = optLong * 1000L;
            }
            this.i = (float)optJSONObject2.optDouble("distance", -1.0);
        }
        final JSONObject optJSONObject3 = optJSONObject.optJSONObject("geofences");
        if (optJSONObject3 != null) {
            try {
                this.j = optJSONObject3.getInt("min_time_since_last_request");
                this.k = optJSONObject3.getInt("min_time_since_last_report");
                this.n = optJSONObject3.getBoolean("enabled");
                this.m = true;
                this.l = optJSONObject3.optInt("max_num_to_register", 20);
            }
            catch (JSONException ex2) {
                AppboyLogger.e(co.a, "Required geofence fields were null. Using defaults.", (Throwable)ex2);
                this.j = -1;
                this.k = -1;
                this.l = -1;
                this.n = false;
                this.m = false;
            }
        }
        optJSONObject = optJSONObject.optJSONObject("test_user");
        if (optJSONObject != null) {
            try {
                this.p = optJSONObject.getBoolean("device_logging_enabled");
            }
            catch (JSONException ex3) {
                AppboyLogger.e(co.a, "Required test user fields were null. Using defaults", (Throwable)ex3);
                this.p = false;
            }
        }
    }
    
    private Set<String> a(final JSONObject jsonObject, final String s) {
        if (jsonObject.optJSONArray(s) != null) {
            final HashSet<String> set = new HashSet<String>();
            final JSONArray optJSONArray = jsonObject.optJSONArray(s);
            for (int i = 0; i < optJSONArray.length(); ++i) {
                set.add(optJSONArray.getString(i));
            }
            return set;
        }
        return null;
    }
    
    public long a() {
        return this.b;
    }
    
    public void a(final float i) {
        this.i = i;
    }
    
    public void a(final int j) {
        this.j = j;
    }
    
    public void a(final long b) {
        this.b = b;
    }
    
    public void a(final Set<String> c) {
        this.c = c;
    }
    
    public void a(final boolean g) {
        this.g = g;
    }
    
    public Set<String> b() {
        return this.c;
    }
    
    public void b(final int k) {
        this.k = k;
    }
    
    public void b(final long h) {
        this.h = h;
    }
    
    public void b(final Set<String> d) {
        this.d = d;
    }
    
    public void b(final boolean f) {
        this.f = f;
    }
    
    public Set<String> c() {
        return this.d;
    }
    
    public void c(final int l) {
        this.l = l;
    }
    
    public void c(final long o) {
        this.o = o;
    }
    
    public void c(final Set<String> e) {
        this.e = e;
    }
    
    public void c(final boolean n) {
        this.n = n;
    }
    
    public Set<String> d() {
        return this.e;
    }
    
    public void d(final boolean m) {
        this.m = m;
    }
    
    public long e() {
        return this.o;
    }
    
    public void e(final boolean p) {
        this.p = p;
    }
    
    public boolean f() {
        return this.g;
    }
    
    public boolean g() {
        return this.f;
    }
    
    public long h() {
        return this.h;
    }
    
    public float i() {
        return this.i;
    }
    
    public int j() {
        return this.j;
    }
    
    public int k() {
        return this.k;
    }
    
    public int l() {
        return this.l;
    }
    
    public boolean m() {
        return this.n;
    }
    
    public boolean n() {
        return this.m;
    }
    
    public boolean o() {
        return this.p;
    }
}
