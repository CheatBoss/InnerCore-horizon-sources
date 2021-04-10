package bo.app;

import android.net.*;
import java.util.*;
import com.appboy.support.*;
import com.appboy.models.response.*;
import org.json.*;
import com.appboy.enums.inappmessage.*;
import com.appboy.models.*;

public class dc extends cp
{
    private static final String b;
    private final String c;
    private final long d;
    private final String e;
    private final fk f;
    private final ek g;
    private final ck h;
    private final br i;
    
    static {
        b = AppboyLogger.getAppboyLogTag(dc.class);
    }
    
    public dc(final String s, final em g, final fk f, final br i, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("template");
        super(Uri.parse(sb.toString()), null);
        this.c = g.g();
        this.d = g.f();
        this.e = g.h();
        this.f = f;
        this.h = new ck.a().a(s2).c();
        this.i = i;
        this.g = g;
    }
    
    @Override
    public void a(final ad ad, final cm cm) {
        if (cm != null && cm.b()) {
            if (!StringUtils.isNullOrBlank(this.e)) {
                cm.i().setLocalAssetPathForPrefetch(this.e);
            }
        }
        else {
            this.m();
        }
    }
    
    @Override
    public void a(final ad ad, final ResponseError responseError) {
        super.a(ad, responseError);
        this.m();
    }
    
    @Override
    public JSONObject g() {
        final JSONObject g = super.g();
        if (g == null) {
            return null;
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("trigger_id", (Object)this.c);
            jsonObject.put("trigger_event_type", (Object)this.f.b());
            if (this.f.e() != null) {
                jsonObject.put("data", ((IPutIntoJson<Object>)this.f.e()).forJsonPut());
            }
            g.put("template", (Object)jsonObject);
            if (this.h.f()) {
                g.put("respond_with", (Object)this.h.a());
            }
            return g;
        }
        catch (JSONException ex) {
            AppboyLogger.w(dc.b, "Experienced JSONException while retrieving parameters. Returning null.", (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public boolean h() {
        return false;
    }
    
    @Override
    public y i() {
        return y.b;
    }
    
    public long k() {
        return this.d;
    }
    
    public ek l() {
        return this.g;
    }
    
    void m() {
        AppboyLogger.i(dc.b, "Template request failed. Attempting to log in-app message template request failure.");
        if (StringUtils.isNullOrBlank(this.c)) {
            AppboyLogger.d(dc.b, "Trigger ID not found. Not logging in-app message template request failure.");
            return;
        }
        if (this.i == null) {
            AppboyLogger.e(dc.b, "Cannot log an in-app message template request failure because the IAppboyManager is null.");
            return;
        }
        try {
            this.i.a(cg.a(null, null, this.c, InAppMessageFailureType.TEMPLATE_REQUEST));
        }
        catch (JSONException ex) {
            this.i.a((Throwable)ex);
        }
    }
}
