package bo.app;

import com.appboy.support.*;
import android.content.*;
import com.google.android.gms.common.*;
import android.content.pm.*;

public final class dy
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dy.class);
    }
    
    public static boolean a(final Context context) {
        try {
            Class.forName("com.google.android.gms.common.GoogleApiAvailability");
            final int googlePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
            if (googlePlayServicesAvailable == 0) {
                AppboyLogger.d(dy.a, "Google Play Services is available.");
                return true;
            }
            final String a = dy.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Google Play Services is unavailable. Connection result: ");
            sb.append(googlePlayServicesAvailable);
            AppboyLogger.i(a, sb.toString());
            return false;
        }
        catch (Exception ex) {
            AppboyLogger.i(dy.a, "Google Play Services Availability API not found. Google Play Services not enabled.", ex);
            return false;
        }
    }
    
    public static boolean b(final Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.google.android.gsf", 0);
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.e(dy.a, "Unexpected exception while checking for com.google.android.gsf");
            return false;
        }
        catch (PackageManager$NameNotFoundException ex2) {
            return false;
        }
    }
}
