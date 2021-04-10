package bo.app;

import java.util.concurrent.*;
import com.appboy.configuration.*;
import com.appboy.support.*;
import android.util.*;
import android.os.*;
import android.content.*;
import org.json.*;
import java.util.*;

public class fy implements fu
{
    private static final String a;
    private final Context b;
    private final br c;
    private final ad d;
    private final long e;
    private final SharedPreferences f;
    private final ft g;
    private final fw h;
    private Map<String, ek> i;
    private volatile long j;
    private final Object k;
    
    static {
        a = AppboyLogger.getAppboyLogTag(fy.class);
    }
    
    public fy(final Context context, final br c, final ThreadPoolExecutor threadPoolExecutor, final ad d, final AppboyConfigurationProvider appboyConfigurationProvider, final String s, final String s2) {
        this.j = 0L;
        this.k = new Object();
        this.b = context.getApplicationContext();
        this.c = c;
        this.d = d;
        this.e = appboyConfigurationProvider.getTriggerActionMinimumTimeIntervalInSeconds();
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.storage.triggers.actions");
        sb.append(StringUtils.getCacheFileSuffix(context, s, s2));
        this.f = context.getSharedPreferences(sb.toString(), 0);
        this.g = new fx(context, threadPoolExecutor, s2);
        this.h = new fz(context, s, s2);
        this.i = this.b();
    }
    
    static boolean a(final fk fk, final ek ek, long n, final long n2) {
        if (fk instanceof fq) {
            AppboyLogger.d(fy.a, "Ignoring minimum time interval between triggered actions because the trigger event is a test.");
            return true;
        }
        final long n3 = du.a() + ek.c().d();
        final int g = ek.c().g();
        if (g != -1) {
            final String a = fy.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Using override minimum display interval: ");
            sb.append(g);
            Log.d(a, sb.toString());
            n += g;
        }
        else {
            n += n2;
        }
        if (n3 >= n) {
            final String a2 = fy.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Minimum time interval requirement met for matched trigger. Action display time: ");
            sb2.append(n3);
            sb2.append(" . Next viable display time: ");
            sb2.append(n);
            AppboyLogger.i(a2, sb2.toString());
            return true;
        }
        final String a3 = fy.a;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Minimum time interval requirement and triggered action override time interval requirement of ");
        sb3.append(n2);
        sb3.append(" not met for matched trigger. Returning null. Next viable display time: ");
        sb3.append(n);
        sb3.append(". Action display time: ");
        sb3.append(n3);
        AppboyLogger.i(a3, sb3.toString());
        return false;
    }
    
    public fw a() {
        return this.h;
    }
    
    @Override
    public void a(final long j) {
        this.j = j;
    }
    
    @Override
    public void a(final fk fk) {
        final String a = fy.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("New incoming <");
        sb.append(fk.b());
        sb.append(">. Searching for matching triggers.");
        AppboyLogger.d(a, sb.toString());
        final ek b = this.b(fk);
        if (b != null) {
            this.a(fk, b);
        }
    }
    
    void a(final fk fk, final ek ek) {
        ek.a(this.g.a(ek));
        final fe c = ek.c();
        long n;
        if (c.e() != -1) {
            n = fk.d() + c.e();
        }
        else {
            n = -1L;
        }
        final Handler handler = new Handler(Looper.getMainLooper());
        final int d = c.d();
        final String a = fy.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Performing triggered action after a delay of ");
        sb.append(d);
        sb.append(" seconds.");
        Log.d(a, sb.toString());
        handler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                ek.a(fy.this.b, fy.this.d, fk, n);
            }
        }, (long)(d * 1000));
    }
    
    @Override
    public void a(final List<ek> list) {
        final fq fq = new fq();
        if (list == null) {
            AppboyLogger.w(fy.a, "Received a null list of triggers in registerTriggeredActions(). Doing nothing.");
            return;
        }
        synchronized (this.k) {
            this.i.clear();
            final SharedPreferences$Editor edit = this.f.edit();
            edit.clear();
            final String a = fy.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Registering ");
            sb.append(list.size());
            sb.append(" new triggered actions.");
            AppboyLogger.d(a, sb.toString());
            final Iterator<ek> iterator = list.iterator();
            boolean b = false;
            while (iterator.hasNext()) {
                final ek ek = iterator.next();
                final String a2 = fy.a;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Registering triggered action id ");
                sb2.append(ek.b());
                AppboyLogger.d(a2, sb2.toString());
                this.i.put(ek.b(), ek);
                edit.putString(ek.b(), ek.forJsonPut().toString());
                if (ek.a(fq)) {
                    b = true;
                }
            }
            edit.apply();
            // monitorexit(this.k)
            this.h.a(list);
            this.g.a(list);
            if (b) {
                AppboyLogger.i(fy.a, "Test triggered actions found, triggering test event.");
                this.a(fq);
                return;
            }
            AppboyLogger.d(fy.a, "No test triggered actions found.");
        }
    }
    
    ek b(final fk fk) {
        synchronized (this.k) {
            final Iterator<ek> iterator = this.i.values().iterator();
            ek ek = null;
            int n = Integer.MIN_VALUE;
            while (iterator.hasNext()) {
                final ek ek2 = iterator.next();
                if (ek2.a(fk) && this.h.a(ek2) && a(fk, ek2, this.j, this.e)) {
                    final String a = fy.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Found potential triggered action for incoming trigger event. Action id ");
                    sb.append(ek2.b());
                    sb.append(".");
                    AppboyLogger.d(a, sb.toString());
                    final int c = ek2.c().c();
                    if (c <= n) {
                        continue;
                    }
                    ek = ek2;
                    n = c;
                }
            }
            if (ek == null) {
                final String a2 = fy.a;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to match triggered action for incoming <");
                sb2.append(fk.b());
                sb2.append(">.");
                AppboyLogger.d(a2, sb2.toString());
                return null;
            }
            final String a3 = fy.a;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Found best triggered action for incoming trigger event ");
            String a4;
            if (fk.e() != null) {
                a4 = ec.a(fk.e().forJsonPut());
            }
            else {
                a4 = "";
            }
            sb3.append(a4);
            sb3.append(".\nMatched Action id: ");
            sb3.append(ek.b());
            sb3.append(".");
            AppboyLogger.d(a3, sb3.toString());
            return ek;
        }
    }
    
    Map<String, ek> b() {
        final HashMap<String, ek> hashMap = new HashMap<String, ek>();
        final Map all = this.f.getAll();
        if (all != null) {
            if (all.size() == 0) {
                return hashMap;
            }
            final Set<String> keySet = all.keySet();
            if (keySet != null) {
                if (keySet.size() == 0) {
                    return hashMap;
                }
                try {
                    for (final String s : keySet) {
                        final String string = this.f.getString(s, (String)null);
                        if (StringUtils.isNullOrBlank(string)) {
                            final String a = fy.a;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Received null or blank serialized triggered action string for action id ");
                            sb.append(s);
                            sb.append(" from shared preferences. Not parsing.");
                            AppboyLogger.w(a, sb.toString());
                        }
                        else {
                            final ek b = gb.b(new JSONObject(string), this.c);
                            if (b == null) {
                                continue;
                            }
                            hashMap.put(b.b(), b);
                            final String a2 = fy.a;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Retrieving templated triggered action id ");
                            sb2.append(b.b());
                            sb2.append(" from local storage.");
                            AppboyLogger.d(a2, sb2.toString());
                        }
                    }
                }
                catch (Exception ex) {
                    AppboyLogger.e(fy.a, "Encountered unexpected exception while parsing stored triggered actions.", ex);
                    return hashMap;
                }
                catch (JSONException ex2) {
                    AppboyLogger.e(fy.a, "Encountered Json exception while parsing stored triggered actions.", (Throwable)ex2);
                }
            }
        }
        return hashMap;
    }
}
