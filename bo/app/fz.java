package bo.app;

import com.appboy.support.*;
import java.util.concurrent.*;
import android.content.*;
import java.util.*;

public class fz implements fw
{
    private static final String a;
    private final SharedPreferences b;
    private Map<String, Long> c;
    
    static {
        a = AppboyLogger.getAppboyLogTag(fz.class);
    }
    
    public fz(final Context context, final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.storage.triggers.re_eligibility");
        sb.append(StringUtils.getCacheFileSuffix(context, s, s2));
        this.b = context.getSharedPreferences(sb.toString(), 0);
        this.c = this.a();
    }
    
    private Map<String, Long> a() {
        final ConcurrentHashMap<String, Long> concurrentHashMap = new ConcurrentHashMap<String, Long>();
        final Map all = this.b.getAll();
        if (all != null) {
            if (all.size() == 0) {
                return concurrentHashMap;
            }
            final Set<String> keySet = all.keySet();
            if (keySet != null) {
                if (keySet.size() == 0) {
                    return concurrentHashMap;
                }
                try {
                    for (final String s : keySet) {
                        final long long1 = this.b.getLong(s, 0L);
                        final String a = fz.a;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Retrieving triggered action id ");
                        sb.append(s);
                        sb.append(" eligibility information from local storage.");
                        AppboyLogger.d(a, sb.toString());
                        concurrentHashMap.put(s, long1);
                    }
                }
                catch (Exception ex) {
                    AppboyLogger.e(fz.a, "Encountered unexpected exception while parsing stored re-eligibility information.", ex);
                }
            }
        }
        return concurrentHashMap;
    }
    
    @Override
    public void a(final ek ek, final long n) {
        final String a = fz.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Updating re-eligibility for action Id ");
        sb.append(ek.b());
        sb.append(" to time ");
        sb.append(n);
        sb.append(".");
        AppboyLogger.d(a, sb.toString());
        this.c.put(ek.b(), n);
        final SharedPreferences$Editor edit = this.b.edit();
        edit.putLong(ek.b(), n);
        edit.apply();
    }
    
    @Override
    public void a(final List<ek> list) {
        final HashSet<String> set = new HashSet<String>();
        final Iterator<ek> iterator = list.iterator();
        while (iterator.hasNext()) {
            set.add(iterator.next().b());
        }
        final HashSet<String> set2 = new HashSet<String>(this.c.keySet());
        final SharedPreferences$Editor edit = this.b.edit();
        for (final String s : set2) {
            if (!set.contains(s)) {
                final String a = fz.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Deleting outdated triggered action id ");
                sb.append(s);
                sb.append(" from re-eligibility list.");
                AppboyLogger.d(a, sb.toString());
                this.c.remove(s);
                edit.remove(s);
            }
            else {
                final String a2 = fz.a;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Retaining triggered action ");
                sb2.append(s);
                sb2.append(" in re-eligibility list.");
                AppboyLogger.d(a2, sb2.toString());
            }
        }
        edit.apply();
    }
    
    @Override
    public boolean a(final ek ek) {
        final fd f = ek.c().f();
        if (f.a()) {
            final String a = fz.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Triggered action id ");
            sb.append(ek.b());
            sb.append(" always eligible via configuration. Returning true for eligibility status");
            AppboyLogger.d(a, sb.toString());
            return true;
        }
        if (!this.c.containsKey(ek.b())) {
            final String a2 = fz.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Triggered action id ");
            sb2.append(ek.b());
            sb2.append(" always eligible via never having been triggered. Returning true for eligibility status");
            AppboyLogger.d(a2, sb2.toString());
            return true;
        }
        if (f.b()) {
            final String a3 = fz.a;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Triggered action id ");
            sb3.append(ek.b());
            sb3.append(" no longer eligible due to having been triggered in the past and is only eligible once.");
            AppboyLogger.d(a3, sb3.toString());
            return false;
        }
        final long longValue = this.c.get(ek.b());
        if (du.a() + ek.c().d() >= f.c() + longValue) {
            final String a4 = fz.a;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Trigger action is re-eligible for display since ");
            sb4.append(du.a() - longValue);
            sb4.append(" seconds have passed since the last time it was triggered (minimum interval: ");
            sb4.append(f.c());
            sb4.append(").");
            AppboyLogger.d(a4, sb4.toString());
            return true;
        }
        final String a5 = fz.a;
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Trigger action is not re-eligible for display since only ");
        sb5.append(du.a() - longValue);
        sb5.append(" seconds have passed since the last time it was triggered (minimum interval: ");
        sb5.append(f.c());
        sb5.append(").");
        AppboyLogger.d(a5, sb5.toString());
        return false;
    }
}
