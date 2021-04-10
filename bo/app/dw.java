package bo.app;

import android.app.*;
import com.appboy.receivers.*;
import android.content.*;
import com.appboy.models.*;
import com.appboy.support.*;
import org.json.*;
import java.util.*;

public final class dw
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dw.class);
    }
    
    public static PendingIntent a(final Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent("com.appboy.action.receiver.APPBOY_GEOFENCE_UPDATE").setClass(context, (Class)AppboyActionReceiver.class), 134217728);
    }
    
    public static List<AppboyGeofence> a(final SharedPreferences sharedPreferences) {
        final ArrayList<AppboyGeofence> list = new ArrayList<AppboyGeofence>();
        final Map all = sharedPreferences.getAll();
        if (all == null || all.size() == 0) {
            AppboyLogger.d(dw.a, "Did not find stored geofences.");
            return list;
        }
        final Set<String> keySet = all.keySet();
        if (keySet != null && keySet.size() != 0) {
            for (final String s : keySet) {
                final String string = sharedPreferences.getString(s, (String)null);
                String s2;
                StringBuilder sb2;
                String s3;
                try {
                    if (StringUtils.isNullOrBlank(string)) {
                        final String a = dw.a;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Received null or blank serialized  geofence string for geofence id ");
                        sb.append(s);
                        sb.append(" from shared preferences. Not parsing.");
                        AppboyLogger.w(a, sb.toString());
                        continue;
                    }
                    list.add(new AppboyGeofence(new JSONObject(string)));
                    continue;
                }
                catch (Exception ex) {
                    s2 = dw.a;
                    sb2 = new StringBuilder();
                    s3 = "Encountered unexpected exception while parsing stored geofence: ";
                }
                catch (JSONException ex) {
                    s2 = dw.a;
                    sb2 = new StringBuilder();
                    s3 = "Encountered Json exception while parsing stored geofence: ";
                }
                sb2.append(s3);
                sb2.append(string);
                final Exception ex;
                AppboyLogger.e(s2, sb2.toString(), ex);
            }
            return list;
        }
        AppboyLogger.w(dw.a, "Failed to find stored geofence keys.");
        return list;
    }
    
    public static boolean a(final dr dr) {
        String s;
        String s2;
        if (dr.a()) {
            if (dr.b()) {
                AppboyLogger.i(dw.a, "Geofences enabled in server configuration.");
                return true;
            }
            s = dw.a;
            s2 = "Geofences explicitly disabled via server configuration.";
        }
        else {
            s = dw.a;
            s2 = "Geofences implicitly disabled via server configuration.";
        }
        AppboyLogger.i(s, s2);
        return false;
    }
    
    public static int b(final dr dr) {
        if (dr.g() > 0) {
            return dr.g();
        }
        return 20;
    }
    
    public static PendingIntent b(final Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent("com.appboy.action.receiver.APPBOY_GEOFENCE_LOCATION_UPDATE").setClass(context, (Class)AppboyActionReceiver.class), 134217728);
    }
}
