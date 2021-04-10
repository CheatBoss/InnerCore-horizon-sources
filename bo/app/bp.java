package bo.app;

import android.content.*;
import com.appboy.configuration.*;
import com.appboy.support.*;
import java.lang.reflect.*;
import com.appboy.*;

public class bp
{
    private static final String a;
    private static final String[] b;
    private final Context c;
    
    static {
        a = AppboyLogger.getAppboyLogTag(bp.class);
        b = new String[] { "com.google.firebase.iid.FirebaseInstanceId" };
    }
    
    public bp(final Context c) {
        this.c = c;
    }
    
    public static boolean a(final Context context, final AppboyConfigurationProvider appboyConfigurationProvider) {
        if (StringUtils.isNullOrEmpty(appboyConfigurationProvider.getFirebaseCloudMessagingSenderIdKey())) {
            AppboyLogger.w(bp.a, "Firebase Cloud Messaging requires a non-null and non-empty sender ID.");
            return false;
        }
        if (!dy.b(context)) {
            AppboyLogger.w(bp.a, "Firebase Cloud Messaging requires the Google Play Store to be installed.");
            return false;
        }
        try {
            final ClassLoader classLoader = bp.class.getClassLoader();
            final String[] b = bp.b;
            for (int length = b.length, i = 0; i < length; ++i) {
                final String s = b[i];
                if (Class.forName(s, false, classLoader) == null) {
                    final String a = bp.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Automatic registration for Firebase Cloud Messaging requires the following class to be present: ");
                    sb.append(s);
                    AppboyLogger.w(a, sb.toString());
                    return false;
                }
            }
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.e(bp.a, "Caught error while checking for required classes for Firebase Cloud Messaging.", ex);
            return false;
        }
    }
    
    private static String b(String s) {
        try {
            final Method a = ef.a("com.google.firebase.iid.FirebaseInstanceId", "getInstance", (Class<?>[])new Class[0]);
            if (a == null) {
                AppboyLogger.d(bp.a, "Firebase Cloud Messaging 'getInstance' method could not obtained. Not registering for Firebase Cloud Messaging.");
                return null;
            }
            final Object a2 = ef.a(null, a, new Object[0]);
            if (a2 == null) {
                AppboyLogger.d(bp.a, "Firebase Cloud Messaging 'InstanceId' object could not invoked. Not registering for Firebase Cloud Messaging.");
                return null;
            }
            final Method a3 = ef.a(a2.getClass(), "getToken", String.class, String.class);
            if (a3 == null) {
                AppboyLogger.d(bp.a, "Firebase Cloud Messaging 'FirebaseInstanceId.getInstance().getToken()' method could not obtained. Not registering for Firebase Cloud Messaging.");
                return null;
            }
            final Object a4 = ef.a(a2, a3, s, "FCM");
            if (a4 != null && a4 instanceof String) {
                s = (String)a4;
                return s;
            }
            return null;
        }
        catch (Exception ex) {
            AppboyLogger.e(bp.a, "Failed to register for Firebase Cloud Messaging", ex);
            return null;
        }
    }
    
    public void a(String b) {
        b = b(b);
        if (!StringUtils.isNullOrEmpty(b)) {
            Appboy.getInstance(this.c).registerAppboyPushMessages(b);
            return;
        }
        AppboyLogger.w(bp.a, "Obtained an empty or null Firebase Cloud Messaging registration token. Not registering token.");
    }
}
