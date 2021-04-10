package bo.app;

import java.util.*;
import com.appboy.models.*;
import com.appboy.models.response.*;
import com.appboy.support.*;
import org.json.*;

public class cm
{
    private static final String a;
    private final JSONArray b;
    private final cn c;
    private final IInAppMessage d;
    private final List<ek> e;
    private final co f;
    private final List<AppboyGeofence> g;
    private final ResponseError h;
    
    static {
        a = AppboyLogger.getAppboyLogTag(cm.class);
    }
    
    public cm(final JSONObject jsonObject, final cw cw, final br br) {
        final String optString = jsonObject.optString("error", (String)null);
        if (optString != null) {
            this.h = new ResponseError(optString);
        }
        else {
            this.h = null;
        }
        final JSONArray optJSONArray = jsonObject.optJSONArray("feed");
        if (optJSONArray != null) {
            this.b = optJSONArray;
        }
        else {
            this.b = null;
        }
        if (this.h == null && cw instanceof cr) {
            cn c = null;
            Label_0165: {
                String s;
                StringBuilder sb;
                String s2;
                try {
                    c = new cn(jsonObject);
                    break Label_0165;
                }
                catch (Exception ex) {
                    s = cm.a;
                    sb = new StringBuilder();
                    s2 = "Encountered Exception processing Content Cards response: ";
                }
                catch (JSONException ex) {
                    s = cm.a;
                    sb = new StringBuilder();
                    s2 = "Encountered JSONException processing Content Cards response: ";
                }
                sb.append(s2);
                sb.append(jsonObject.toString());
                final Exception ex;
                AppboyLogger.w(s, sb.toString(), ex);
                c = null;
            }
            this.c = c;
        }
        else {
            this.c = null;
        }
        this.e = gb.a(jsonObject.optJSONArray("triggers"), br);
        final JSONObject optJSONObject = jsonObject.optJSONObject("config");
        co f = null;
        Label_0292: {
            if (optJSONObject != null) {
                String s3;
                StringBuilder sb2;
                String s4;
                try {
                    f = new co(optJSONObject);
                    break Label_0292;
                }
                catch (Exception ex2) {
                    s3 = cm.a;
                    sb2 = new StringBuilder();
                    s4 = "Encountered Exception processing server config: ";
                }
                catch (JSONException ex2) {
                    s3 = cm.a;
                    sb2 = new StringBuilder();
                    s4 = "Encountered JSONException processing server config: ";
                }
                sb2.append(s4);
                sb2.append(optJSONObject.toString());
                final Exception ex2;
                AppboyLogger.w(s3, sb2.toString(), ex2);
            }
            f = null;
        }
        this.f = f;
        this.d = gb.a(jsonObject.optJSONObject("templated_message"), br);
        this.g = dv.a(jsonObject.optJSONArray("geofences"));
    }
    
    public boolean a() {
        return this.b != null;
    }
    
    public boolean b() {
        return this.d != null;
    }
    
    public boolean c() {
        return this.f != null;
    }
    
    public boolean d() {
        return this.e != null;
    }
    
    public boolean e() {
        return this.h != null;
    }
    
    public boolean f() {
        return this.g != null;
    }
    
    public boolean g() {
        return this.c != null;
    }
    
    public JSONArray h() {
        return this.b;
    }
    
    public IInAppMessage i() {
        return this.d;
    }
    
    public co j() {
        return this.f;
    }
    
    public List<ek> k() {
        return this.e;
    }
    
    public List<AppboyGeofence> l() {
        return this.g;
    }
    
    public cn m() {
        return this.c;
    }
    
    public ResponseError n() {
        return this.h;
    }
}
