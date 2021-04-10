package bo.app;

import com.appboy.models.*;
import android.app.*;
import com.appboy.configuration.*;
import com.google.android.gms.location.*;
import android.content.*;
import com.appboy.support.*;
import java.util.*;

public class bi
{
    private static final String j;
    final br a;
    final SharedPreferences b;
    final List<AppboyGeofence> c;
    final PendingIntent d;
    final PendingIntent e;
    bj f;
    cb g;
    boolean h;
    int i;
    private final Context k;
    private final AppboyConfigurationProvider l;
    private final dr m;
    private final Object n;
    
    static {
        j = AppboyLogger.getAppboyLogTag(bi.class);
    }
    
    public bi(final Context context, final String s, final br a, final AppboyConfigurationProvider l, final dr m) {
        this.n = new Object();
        final boolean b = false;
        this.h = false;
        this.k = context.getApplicationContext();
        this.a = a;
        this.b = context.getSharedPreferences(b(s), 0);
        this.l = l;
        this.m = m;
        boolean h = b;
        if (dw.a(m)) {
            h = b;
            if (this.a(context)) {
                h = true;
            }
        }
        this.h = h;
        this.i = dw.b(this.m);
        this.c = dw.a(this.b);
        this.d = dw.a(context);
        this.e = dw.b(context);
        this.f = new bj(context, s, m);
        this.a(true);
    }
    
    static String b(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.managers.geofences.storage.");
        sb.append(s);
        return sb.toString();
    }
    
    AppboyGeofence a(final String s) {
        synchronized (this.n) {
            for (final AppboyGeofence appboyGeofence : this.c) {
                if (appboyGeofence.getId().equals(s)) {
                    return appboyGeofence;
                }
            }
            return null;
        }
    }
    
    public void a() {
        AppboyLogger.d(bi.j, "Request to set up geofences received.");
        this.h = (dw.a(this.m) && this.a(this.k));
        this.a(false);
        this.b(true);
    }
    
    protected void a(final PendingIntent pendingIntent) {
        AppboyLogger.d(bi.j, "Tearing down geofences.");
        if (pendingIntent != null) {
            AppboyLogger.d(bi.j, "Unregistering any Braze geofences from Google Play Services.");
            LocationServices.getGeofencingClient(this.k).removeGeofences(pendingIntent);
        }
        synchronized (this.n) {
            AppboyLogger.d(bi.j, "Deleting locally stored geofences.");
            final SharedPreferences$Editor edit = this.b.edit();
            edit.clear();
            this.c.clear();
            edit.apply();
        }
    }
    
    public void a(final cb cb) {
        if (!this.h) {
            AppboyLogger.d(bi.j, "Appboy geofences not enabled. Not requesting geofences.");
            return;
        }
        if (cb != null) {
            final ch g = new ch(cb.a(), cb.b(), cb.c(), cb.d());
            this.g = g;
            this.a.a(g);
        }
    }
    
    public void a(final co co) {
        if (co == null) {
            AppboyLogger.w(bi.j, "Could not configure geofence manager from server config. Server config was null.");
            return;
        }
        final boolean m = co.m();
        final String j = bi.j;
        final StringBuilder sb = new StringBuilder();
        sb.append("Geofences enabled server config value ");
        sb.append(m);
        sb.append(" received.");
        AppboyLogger.d(j, sb.toString());
        final boolean h = m && this.a(this.k);
        if (h != this.h) {
            this.h = h;
            final String i = bi.j;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Geofences enabled status newly set to ");
            sb2.append(this.h);
            sb2.append(" during server config update.");
            AppboyLogger.i(i, sb2.toString());
            if (this.h) {
                this.a(false);
                this.b(true);
            }
            else {
                this.a(this.d);
            }
        }
        else {
            final String k = bi.j;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Geofences enabled status ");
            sb3.append(this.h);
            sb3.append(" unchanged during server config update.");
            AppboyLogger.d(k, sb3.toString());
        }
        final int l = co.l();
        if (l >= 0) {
            this.i = l;
            final String j2 = bi.j;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Max number to register newly set to ");
            sb4.append(this.i);
            sb4.append(" via server config.");
            AppboyLogger.i(j2, sb4.toString());
        }
        this.f.a(co);
    }
    
    public void a(final List<AppboyGeofence> list) {
        if (list == null) {
            AppboyLogger.w(bi.j, "Appboy geofence list was null. Not adding new geofences to local storage.");
            return;
        }
        if (!this.h) {
            AppboyLogger.w(bi.j, "Appboy geofences not enabled. Not adding new geofences to local storage.");
            return;
        }
        if (this.g != null) {
            for (final AppboyGeofence appboyGeofence : list) {
                appboyGeofence.setDistanceFromGeofenceRefresh(ed.a(this.g.a(), this.g.b(), appboyGeofence.getLatitude(), appboyGeofence.getLongitude()));
            }
            Collections.sort((List<Comparable>)list);
        }
        synchronized (this.n) {
            final String j = bi.j;
            final StringBuilder sb = new StringBuilder();
            sb.append("Received new geofence list of size: ");
            sb.append(list.size());
            AppboyLogger.d(j, sb.toString());
            final SharedPreferences$Editor edit = this.b.edit();
            edit.clear();
            this.c.clear();
            int n = 0;
            for (final AppboyGeofence appboyGeofence2 : list) {
                if (n == this.i) {
                    final String i = bi.j;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Reached maximum number of new geofences: ");
                    sb2.append(this.i);
                    AppboyLogger.d(i, sb2.toString());
                    break;
                }
                this.c.add(appboyGeofence2);
                final String k = bi.j;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Adding new geofence to local storage: ");
                sb3.append(appboyGeofence2.toString());
                AppboyLogger.d(k, sb3.toString());
                edit.putString(appboyGeofence2.getId(), appboyGeofence2.forJsonPut().toString());
                ++n;
            }
            edit.apply();
            final String l = bi.j;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Added ");
            sb4.append(this.c.size());
            sb4.append(" new geofences to local storage.");
            AppboyLogger.d(l, sb4.toString());
            // monitorexit(this.n)
            this.f.a(list);
            this.a(true);
        }
    }
    
    protected void a(final List<AppboyGeofence> list, final PendingIntent pendingIntent) {
        dx.a(this.k, list, pendingIntent);
    }
    
    protected void a(final boolean b) {
        if (!this.h) {
            AppboyLogger.d(bi.j, "Appboy geofences not enabled. Geofences not set up.");
            return;
        }
        AppboyLogger.d(bi.j, "Location permissions and Google Play Services available. Location collection and Geofencing enabled via config.");
        if (b) {
            synchronized (this.n) {
                this.a(this.c, this.d);
            }
        }
    }
    
    protected boolean a(final Context context) {
        if (!bk.a(this.l)) {
            AppboyLogger.d(bi.j, "Location collection not available. Geofences not enabled.");
            return false;
        }
        if (!PermissionUtils.hasPermission(context, "android.permission.ACCESS_FINE_LOCATION")) {
            AppboyLogger.i(bi.j, "Fine grained location permissions not found. Geofences not enabled.");
            return false;
        }
        if (!dy.a(context)) {
            AppboyLogger.d(bi.j, "Google Play Services not available. Geofences not enabled.");
            return false;
        }
        final ClassLoader classLoader = bi.class.getClassLoader();
        try {
            if (Class.forName("com.google.android.gms.location.LocationServices", false, classLoader) != null) {
                return true;
            }
            throw new RuntimeException("com.google.android.gms.location.LocationServices not found.");
        }
        catch (Exception ex) {
            AppboyLogger.d(bi.j, "Google Play Services Location API not found. Geofences not enabled.");
            return false;
        }
    }
    
    boolean a(final String s, final x x) {
        synchronized (this.n) {
            final AppboyGeofence a = this.a(s);
            if (a != null) {
                if (x.equals(x.a)) {
                    return a.getAnalyticsEnabledEnter();
                }
                if (x.equals(x.b)) {
                    return a.getAnalyticsEnabledExit();
                }
            }
            return false;
        }
    }
    
    public void b() {
        if (!this.h) {
            AppboyLogger.d(bi.j, "Appboy geofences not enabled. Not un-registering geofences.");
            return;
        }
        AppboyLogger.d(bi.j, "Tearing down all geofences.");
        this.a(this.d);
    }
    
    protected void b(final PendingIntent pendingIntent) {
        dx.a(this.k, pendingIntent);
    }
    
    public void b(final String s, final x x) {
        if (!this.h) {
            AppboyLogger.w(bi.j, "Appboy geofences not enabled. Not posting geofence report.");
            return;
        }
        try {
            final cg d = cg.d(s, x.toString().toLowerCase(Locale.US));
            if (this.a(s, x)) {
                this.a.a(d);
            }
            if (this.f.a(du.a(), this.a(s), x)) {
                this.a.b(d);
            }
        }
        catch (Exception ex) {
            AppboyLogger.w(bi.j, "Failed to record geofence transition.", ex);
        }
    }
    
    public void b(final boolean b) {
        if (!this.h) {
            AppboyLogger.d(bi.j, "Appboy geofences not enabled. Not requesting geofences.");
            return;
        }
        if (this.f.a(b, du.a())) {
            this.b(this.e);
        }
    }
}
