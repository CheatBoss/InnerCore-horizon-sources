package bo.app;

import com.appboy.support.*;
import java.util.concurrent.*;
import com.appboy.models.*;
import java.util.*;
import android.content.*;

public class bj
{
    private static final String h;
    final SharedPreferences a;
    final SharedPreferences b;
    Map<String, Long> c;
    long d;
    long e;
    int f;
    int g;
    
    static {
        h = AppboyLogger.getAppboyLogTag(bj.class);
    }
    
    bj(final Context context, final String s, final dr dr) {
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.managers.geofences.eligibility.global.");
        sb.append(s);
        this.a = context.getSharedPreferences(sb.toString(), 0);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("com.appboy.managers.geofences.eligibility.individual.");
        sb2.append(s);
        final SharedPreferences sharedPreferences = context.getSharedPreferences(sb2.toString(), 0);
        this.b = sharedPreferences;
        this.c = this.a(sharedPreferences);
        this.d = this.a.getLong("last_request_global", 0L);
        this.e = this.a.getLong("last_report_global", 0L);
        this.f = dr.e();
        this.g = dr.f();
    }
    
    String a(final String s) {
        try {
            return s.split("_", 2)[1];
        }
        catch (Exception ex) {
            final String h = bj.h;
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception trying to parse re-eligibility id: ");
            sb.append(s);
            AppboyLogger.i(h, sb.toString(), ex);
            return null;
        }
    }
    
    String a(final String s, final x x) {
        final StringBuilder sb = new StringBuilder();
        sb.append(x.toString().toLowerCase(Locale.US));
        sb.append("_");
        sb.append(s);
        return sb.toString();
    }
    
    Map<String, Long> a(final SharedPreferences sharedPreferences) {
        final ConcurrentHashMap<String, Long> concurrentHashMap = new ConcurrentHashMap<String, Long>();
        final Map all = sharedPreferences.getAll();
        if (all != null) {
            if (all.size() == 0) {
                return concurrentHashMap;
            }
            final Set<String> keySet = all.keySet();
            if (keySet.size() == 0) {
                return concurrentHashMap;
            }
            for (final String s : keySet) {
                final long long1 = sharedPreferences.getLong(s, 0L);
                final String h = bj.h;
                final StringBuilder sb = new StringBuilder();
                sb.append("Retrieving geofence id ");
                sb.append(this.a(s));
                sb.append(" eligibility information from local storage.");
                AppboyLogger.d(h, sb.toString());
                concurrentHashMap.put(s, long1);
            }
        }
        return concurrentHashMap;
    }
    
    void a(final co co) {
        final int j = co.j();
        if (j >= 0) {
            this.f = j;
            final String h = bj.h;
            final StringBuilder sb = new StringBuilder();
            sb.append("Min time since last geofence request reset via server configuration: ");
            sb.append(j);
            sb.append("s.");
            AppboyLogger.i(h, sb.toString());
        }
        final int k = co.k();
        if (k >= 0) {
            this.g = k;
            final String h2 = bj.h;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Min time since last geofence report reset via server configuration: ");
            sb2.append(k);
            sb2.append("s.");
            AppboyLogger.i(h2, sb2.toString());
        }
    }
    
    void a(final List<AppboyGeofence> list) {
        final HashSet<String> set = new HashSet<String>();
        final Iterator<AppboyGeofence> iterator = list.iterator();
        while (iterator.hasNext()) {
            set.add(iterator.next().getId());
        }
        final HashSet<String> set2 = new HashSet<String>(this.c.keySet());
        final SharedPreferences$Editor edit = this.b.edit();
        for (final String s : set2) {
            if (!set.contains(this.a(s))) {
                final String h = bj.h;
                final StringBuilder sb = new StringBuilder();
                sb.append("Deleting outdated re-eligibility id ");
                sb.append(s);
                sb.append(" from re-eligibility list.");
                AppboyLogger.d(h, sb.toString());
                this.c.remove(s);
                edit.remove(s);
            }
            else {
                final String h2 = bj.h;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Retaining re-eligibility id ");
                sb2.append(s);
                sb2.append(" in re-eligibility list.");
                AppboyLogger.d(h2, sb2.toString());
            }
        }
        edit.apply();
    }
    
    boolean a(final long e, final AppboyGeofence appboyGeofence, final x x) {
        if (appboyGeofence == null) {
            AppboyLogger.w(bj.h, "Geofence passed into getReportEligible() was null.");
            return false;
        }
        final String id = appboyGeofence.getId();
        final String a = this.a(id, x);
        int n;
        if (x.equals(x.a)) {
            n = appboyGeofence.getCooldownEnterSeconds();
        }
        else {
            n = appboyGeofence.getCooldownExitSeconds();
        }
        final long n2 = e - this.e;
        if (this.g > n2) {
            final String h = bj.h;
            final StringBuilder sb = new StringBuilder();
            sb.append("Geofence report suppressed since only ");
            sb.append(n2);
            sb.append(" seconds have passed since the last time geofences were reported globally (minimum interval: ");
            sb.append(this.g);
            sb.append("). id:");
            sb.append(id);
            AppboyLogger.d(h, sb.toString());
            return false;
        }
        if (this.c.containsKey(a)) {
            final long n3 = e - this.c.get(a);
            if (n > n3) {
                final String h2 = bj.h;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Geofence report suppressed since only ");
                sb2.append(n3);
                sb2.append(" seconds have passed since the last time this geofence/transition combination was reported (minimum interval: ");
                sb2.append(n);
                sb2.append("). id:");
                sb2.append(id);
                sb2.append(" transition:");
                sb2.append(x);
                AppboyLogger.d(h2, sb2.toString());
                return false;
            }
            final String h3 = bj.h;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(n3);
            sb3.append(" seconds have passed since the last time this geofence/transition combination was reported (minimum interval: ");
            sb3.append(n);
            sb3.append("). id:");
            sb3.append(id);
            sb3.append(" transition:");
            sb3.append(x);
            AppboyLogger.d(h3, sb3.toString());
        }
        else {
            final String h4 = bj.h;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Geofence report eligible since this geofence/transition combination has never reported. id:");
            sb4.append(id);
            sb4.append(" ");
            sb4.append(x);
            AppboyLogger.d(h4, sb4.toString());
        }
        final String h5 = bj.h;
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Geofence report eligible since ");
        sb5.append(n2);
        sb5.append(" seconds have passed since the last time geofences were reported globally (minimum interval: ");
        sb5.append(this.g);
        sb5.append("). id:");
        sb5.append(id);
        AppboyLogger.d(h5, sb5.toString());
        this.c.put(a, e);
        final SharedPreferences$Editor edit = this.b.edit();
        edit.putLong(a, e);
        edit.apply();
        this.e = e;
        final SharedPreferences$Editor edit2 = this.a.edit();
        edit2.putLong("last_report_global", e);
        edit2.apply();
        return true;
    }
    
    protected boolean a(final boolean b, final long d) {
        final long n = d - this.d;
        if (!b && this.f > n) {
            final String h = bj.h;
            final StringBuilder sb = new StringBuilder();
            sb.append("Geofence request suppressed since only ");
            sb.append(n);
            sb.append(" seconds have passed since the last time geofences were requested (minimum interval: ");
            sb.append(this.f);
            sb.append(").");
            AppboyLogger.d(h, sb.toString());
            return false;
        }
        String s;
        StringBuilder sb2;
        if (b) {
            s = bj.h;
            sb2 = new StringBuilder();
            sb2.append("Geofence request eligible. Ignoring rate limit for this geofence request. Elapsed time since last request:");
            sb2.append(n);
        }
        else {
            s = bj.h;
            sb2 = new StringBuilder();
            sb2.append("Geofence request eligible since ");
            sb2.append(n);
            sb2.append(" seconds have passed since the last time geofences were requested (minimum interval: ");
            sb2.append(this.f);
            sb2.append(").");
        }
        AppboyLogger.d(s, sb2.toString());
        this.d = d;
        final SharedPreferences$Editor edit = this.a.edit();
        edit.putLong("last_request_global", this.d);
        edit.apply();
        return true;
    }
}
