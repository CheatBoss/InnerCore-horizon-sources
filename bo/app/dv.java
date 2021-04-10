package bo.app;

import com.appboy.support.*;
import com.appboy.models.*;
import java.util.*;
import org.json.*;

public final class dv
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dv.class);
    }
    
    public static List<AppboyGeofence> a(final JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        final ArrayList<AppboyGeofence> list = new ArrayList<AppboyGeofence>();
        int n = 0;
        JSONObject optJSONObject;
        String a;
        StringBuilder sb;
        Throwable t;
        final JSONException ex;
        Label_0151_Outer:Label_0109_Outer:
        while (true) {
            if (n >= jsonArray.length()) {
                return list;
            }
            optJSONObject = jsonArray.optJSONObject(n);
            while (true) {
                Label_0046: {
                    if (optJSONObject != null) {
                        break Label_0046;
                    }
                    try {
                        AppboyLogger.w(dv.a, "Received null or blank geofence Json. Not parsing.");
                        while (true) {
                            ++n;
                            continue Label_0151_Outer;
                            a = dv.a;
                            sb = new StringBuilder();
                            sb.append("Failed to deserialize geofence Json due to JSONException: ");
                            sb.append(optJSONObject);
                            AppboyLogger.w(a, sb.toString(), t);
                            continue Label_0109_Outer;
                            list.add(new AppboyGeofence(optJSONObject));
                            continue Label_0109_Outer;
                        }
                    }
                    catch (Exception ex2) {}
                    catch (JSONException ex) {}
                }
                t = (Throwable)ex;
                continue;
            }
        }
    }
}
