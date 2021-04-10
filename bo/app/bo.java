package bo.app;

import android.content.*;
import com.appboy.configuration.*;
import com.appboy.support.*;
import android.util.*;
import android.telephony.*;
import android.content.res.*;
import android.os.*;
import java.util.*;
import android.view.*;
import android.app.*;
import java.lang.reflect.*;
import android.content.pm.*;

public class bo implements bs
{
    private static final String b;
    final SharedPreferences a;
    private final Context c;
    private final bt d;
    private final di e;
    private String f;
    
    static {
        b = AppboyLogger.getAppboyLogTag(bo.class);
    }
    
    public bo(final Context c, final AppboyConfigurationProvider appboyConfigurationProvider, final String s, final bt d, final di e) {
        if (c != null) {
            this.c = c;
            this.d = d;
            this.e = e;
            final StringBuilder sb = new StringBuilder();
            sb.append("com.appboy.storage.device_ad_info");
            sb.append(StringUtils.getCacheFileSuffix(c, s, appboyConfigurationProvider.getAppboyApiKey().toString()));
            this.a = c.getSharedPreferences(sb.toString(), 0);
            return;
        }
        throw null;
    }
    
    static String a(final DisplayMetrics displayMetrics) {
        final StringBuilder sb = new StringBuilder();
        sb.append(displayMetrics.widthPixels);
        sb.append("x");
        sb.append(displayMetrics.heightPixels);
        return sb.toString();
    }
    
    static String a(final Locale locale) {
        return locale.toString();
    }
    
    private String f() {
        return String.valueOf(Build$VERSION.SDK_INT);
    }
    
    private String g() {
        try {
            final TelephonyManager telephonyManager = (TelephonyManager)this.c.getSystemService("phone");
            final int phoneType = telephonyManager.getPhoneType();
            if (phoneType != 0) {
                if (phoneType != 1 && phoneType != 2) {
                    AppboyLogger.w(bo.b, "Unknown phone type");
                    return null;
                }
                return telephonyManager.getNetworkOperatorName();
            }
        }
        catch (SecurityException ex) {
            AppboyLogger.e(bo.b, "Caught security exception while reading the phone carrier name.", ex);
            return null;
        }
        catch (Resources$NotFoundException ex2) {
            AppboyLogger.e(bo.b, "Caught resources not found exception while reading the phone carrier name.", (Throwable)ex2);
        }
        return null;
    }
    
    private String h() {
        return Build.MODEL;
    }
    
    private Locale i() {
        return Locale.getDefault();
    }
    
    private TimeZone j() {
        return TimeZone.getDefault();
    }
    
    private DisplayMetrics k() {
        final WindowManager windowManager = (WindowManager)this.c.getSystemService("window");
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
    
    @Override
    public ci a() {
        return new ci(this.f(), this.g(), this.h(), a(this.i()), this.j().getID(), a(this.k()), this.d());
    }
    
    @Override
    public ci b() {
        this.e.a(this.a());
        return this.e.b();
    }
    
    @Override
    public String c() {
        final String a = this.d.a();
        if (a == null) {
            AppboyLogger.e(bo.b, "Error reading deviceId, received a null value.");
        }
        return a;
    }
    
    boolean d() {
        if (Build$VERSION.SDK_INT >= 24) {
            final NotificationManager notificationManager = (NotificationManager)this.c.getSystemService("notification");
            return notificationManager == null || notificationManager.areNotificationsEnabled();
        }
        if (Build$VERSION.SDK_INT >= 19) {
            try {
                final Method a = ef.a("android.support.v4.app.NotificationManagerCompat", "from", Context.class);
                if (a == null) {
                    return true;
                }
                final Object a2 = ef.a(null, a, this.c);
                if (a2 == null) {
                    return true;
                }
                final Method a3 = ef.a(a2.getClass(), "areNotificationsEnabled", (Class<?>[])new Class[0]);
                if (a3 == null) {
                    return true;
                }
                final Object a4 = ef.a(a2, a3, new Object[0]);
                return a4 == null || !(a4 instanceof Boolean) || (boolean)a4;
            }
            catch (Exception ex) {
                AppboyLogger.e(bo.b, "Failed to read notifications enabled state from NotificationManagerCompat.", ex);
            }
        }
        return true;
    }
    
    @Override
    public String e() {
        final String f = this.f;
        if (f != null) {
            return f;
        }
        final String packageName = this.c.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = this.c.getPackageManager().getPackageInfo(packageName, 0);
        }
        catch (PackageManager$NameNotFoundException ex) {
            final String b = bo.b;
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to inspect package [");
            sb.append(packageName);
            sb.append("]");
            AppboyLogger.e(b, sb.toString(), (Throwable)ex);
            packageInfo = null;
        }
        PackageInfo packageArchiveInfo = packageInfo;
        if (packageInfo == null) {
            packageArchiveInfo = this.c.getPackageManager().getPackageArchiveInfo(this.c.getApplicationInfo().sourceDir, 0);
        }
        if (packageArchiveInfo != null) {
            return this.f = packageArchiveInfo.versionName;
        }
        AppboyLogger.d(bo.b, "App version could not be read. Returning null");
        return null;
    }
}
