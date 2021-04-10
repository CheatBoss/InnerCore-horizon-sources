package bo.app;

import com.appboy.enums.*;
import com.appboy.support.*;
import android.net.*;
import com.appboy.*;
import com.appboy.models.response.*;
import java.util.*;
import com.appboy.models.*;
import org.json.*;

public abstract class cp extends da implements cc, cw
{
    private static final String b;
    private Long c;
    private String d;
    private String e;
    private String f;
    private ci g;
    private String h;
    private SdkFlavor i;
    private cl j;
    private ck k;
    private by l;
    
    static {
        b = AppboyLogger.getAppboyLogTag(cp.class);
    }
    
    protected cp(final Uri uri, final Map<String, String> map) {
        super(uri, map);
    }
    
    @Override
    public Uri a() {
        return Appboy.getAppboyApiEndpoint(this.a);
    }
    
    @Override
    public void a(final long n) {
        this.c = n;
    }
    
    @Override
    public void a(final ad ad) {
        if (this.j != null) {
            ad.a(new am(this.j), am.class);
        }
        if (this.g != null) {
            ad.a(new ai(this.g), ai.class);
        }
    }
    
    @Override
    public void a(final ad ad, final ResponseError responseError) {
        final String b = cp.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("Error occurred while executing Braze request: ");
        sb.append(responseError.getMessage());
        AppboyLogger.e(b, sb.toString());
    }
    
    @Override
    public void a(final by l) {
        this.l = l;
    }
    
    @Override
    public void a(final ci g) {
        this.g = g;
    }
    
    public void a(final ck k) {
        this.k = k;
    }
    
    @Override
    public void a(final cl j) {
        this.j = j;
    }
    
    @Override
    public void a(final SdkFlavor i) {
        this.i = i;
    }
    
    @Override
    public void a(final String d) {
        this.d = d;
    }
    
    @Override
    public void a(final Map<String, String> map) {
        map.put("X-Braze-Api-Key", this.e);
    }
    
    @Override
    public void b(final String e) {
        this.e = e;
    }
    
    @Override
    public boolean b() {
        final ArrayList<ci> list = new ArrayList<ci>();
        list.add(this.g);
        list.add(this.j);
        list.add(this.l);
        for (final cc cc : list) {
            if (cc != null && !cc.b()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ci c() {
        return this.g;
    }
    
    @Override
    public void c(final String f) {
        this.f = f;
    }
    
    @Override
    public cl d() {
        return this.j;
    }
    
    @Override
    public void d(final String h) {
        this.h = h;
    }
    
    @Override
    public ck e() {
        return this.k;
    }
    
    @Override
    public by f() {
        return this.l;
    }
    
    @Override
    public JSONObject g() {
        final JSONObject jsonObject = new JSONObject();
        try {
            if (this.h != null) {
                jsonObject.put("app_version", (Object)this.h);
            }
            if (this.d != null) {
                jsonObject.put("device_id", (Object)this.d);
            }
            if (this.c != null) {
                jsonObject.put("time", (Object)this.c);
            }
            if (this.e != null) {
                jsonObject.put("api_key", (Object)this.e);
            }
            if (this.f != null) {
                jsonObject.put("sdk_version", (Object)this.f);
            }
            if (this.g != null && !this.g.b()) {
                jsonObject.put("device", (Object)this.g.a());
            }
            if (this.j != null && !this.j.b()) {
                jsonObject.put("attributes", (Object)this.j.c());
            }
            if (this.l != null && !this.l.b()) {
                jsonObject.put("events", (Object)ec.a((Collection<? extends IPutIntoJson<Object>>)this.l.a()));
            }
            if (this.i != null) {
                jsonObject.put("sdk_flavor", (Object)this.i.forJsonPut());
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            AppboyLogger.w(cp.b, "Experienced JSONException while retrieving parameters. Returning null.", (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public boolean h() {
        return this.b();
    }
}
