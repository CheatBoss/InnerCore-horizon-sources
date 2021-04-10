package bo.app;

import com.appboy.support.*;
import android.net.*;
import java.util.*;
import org.json.*;
import com.appboy.models.*;

public final class cu extends cp
{
    private static final String b;
    private final ca c;
    
    static {
        b = AppboyLogger.getAppboyLogTag(cu.class);
    }
    
    public cu(final String s, final cb cb) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("geofence/request");
        super(Uri.parse(sb.toString()), null);
        this.c = cg.a(cb);
    }
    
    @Override
    public void a(final ad ad, final cm cm) {
        AppboyLogger.d(cu.b, "GeofenceRefreshRequest executed successfully.");
    }
    
    @Override
    public JSONObject g() {
        final JSONObject g = super.g();
        if (g == null) {
            return null;
        }
        try {
            if (this.c != null) {
                g.put("location_event", ((IPutIntoJson<Object>)this.c).forJsonPut());
            }
            return g;
        }
        catch (JSONException ex) {
            AppboyLogger.w(cu.b, "Experienced JSONException while creating geofence refresh request. Returning null.", (Throwable)ex);
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
}
