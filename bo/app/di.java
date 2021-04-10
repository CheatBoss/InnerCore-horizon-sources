package bo.app;

import com.appboy.support.*;
import android.content.*;
import org.json.*;
import java.util.*;

public class di extends de<ci>
{
    private static final String b;
    final SharedPreferences a;
    private ci c;
    
    static {
        b = AppboyLogger.getAppboyLogTag(di.class);
    }
    
    public di(final Context context) {
        this(context, null, null);
    }
    
    public di(final Context context, final String s, final String s2) {
        this.c = null;
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.storage.device_cache.v3");
        sb.append(StringUtils.getCacheFileSuffix(context, s, s2));
        this.a = context.getSharedPreferences(sb.toString(), 0);
    }
    
    public void a(final ci c) {
        this.c = c;
    }
    
    @Override
    void a(final ci ci, final boolean b) {
        if (b && ci != null) {
            try {
                final JSONObject jsonObject = new JSONObject(this.a.getString("cached_device", "{}"));
                final JSONObject a = ci.a();
                final SharedPreferences$Editor edit = this.a.edit();
                edit.putString("cached_device", ec.a(jsonObject, a).toString());
                edit.apply();
            }
            catch (JSONException ex) {
                AppboyLogger.d(di.b, "Caught exception confirming and unlocking device cache.", (Throwable)ex);
            }
        }
    }
    
    ci d() {
        final JSONObject a = this.c.a();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(this.a.getString("cached_device", "{}"));
        }
        catch (JSONException ex) {
            AppboyLogger.e(di.b, "Caught exception confirming and unlocking Json objects.", (Throwable)ex);
        }
        final JSONObject jsonObject2 = new JSONObject();
        final Iterator keys = a.keys();
    Label_0204_Outer:
        while (true) {
            while (true) {
                Label_0221: {
                    if (!keys.hasNext()) {
                        break Label_0221;
                    }
                    final String s = keys.next();
                    final Object opt = a.opt(s);
                    final Object opt2 = jsonObject.opt(s);
                    Label_0192: {
                        if (!(opt instanceof JSONObject) && !(opt instanceof JSONArray)) {
                            if (!opt.equals(opt2)) {
                                try {
                                    jsonObject2.put(s, opt);
                                    continue Label_0204_Outer;
                                }
                                catch (JSONException ex2) {
                                    AppboyLogger.e(di.b, "Caught json exception creating dirty outbound device. Returning the whole device.", (Throwable)ex2);
                                }
                                break;
                            }
                            continue Label_0204_Outer;
                        }
                        else if (opt2 == null) {
                            break Label_0192;
                        }
                        try {
                            if (gf.a(String.valueOf(opt), String.valueOf(opt2), gg.c).b()) {
                                jsonObject2.put(s, opt);
                                continue Label_0204_Outer;
                            }
                            continue Label_0204_Outer;
                            String s2 = di.b;
                            String s3 = "Caught json exception creating dirty outbound device on a jsonObject value. Returning the whole device.";
                            final Throwable t;
                            AppboyLogger.d(s2, s3, t);
                            break;
                            try {
                                return ci.a(jsonObject2);
                            }
                            catch (JSONException t) {
                                s2 = di.b;
                                s3 = "Caught json exception creating device from json. Returning the whole device.";
                            }
                        }
                        catch (JSONException ex3) {}
                    }
                }
                final JSONException ex3;
                final Throwable t = (Throwable)ex3;
                continue;
            }
        }
        return this.c;
    }
}
