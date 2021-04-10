package bo.app;

import com.appboy.support.*;
import android.net.*;
import java.util.*;
import org.json.*;
import com.appboy.models.*;

public final class cv extends cp
{
    private static final String b;
    private final ca c;
    
    static {
        b = AppboyLogger.getAppboyLogTag(cv.class);
    }
    
    public cv(final String s, final ca c) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("geofence/report");
        super(Uri.parse(sb.toString()), null);
        this.c = c;
    }
    
    @Override
    public void a(final ad ad, final cm cm) {
        AppboyLogger.d(cv.b, "GeofenceReportRequest executed successfully.");
    }
    
    @Override
    public JSONObject g() {
        final JSONObject g = super.g();
        if (g == null) {
            return null;
        }
        try {
            if (this.c != null) {
                g.put("geofence_event", ((IPutIntoJson<Object>)this.c).forJsonPut());
            }
            return g;
        }
        catch (JSONException ex) {
            AppboyLogger.w(cv.b, "Experienced JSONException while creating geofence report request. Returning null.", (Throwable)ex);
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
