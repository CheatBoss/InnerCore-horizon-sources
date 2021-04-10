package bo.app;

import java.util.concurrent.atomic.*;
import com.appboy.support.*;
import org.json.*;
import java.util.*;
import android.os.*;
import android.content.*;

public class dr
{
    private static final String a;
    private final SharedPreferences b;
    private final Object c;
    private AtomicBoolean d;
    private co e;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dr.class);
    }
    
    public dr(final Context context, String string) {
        this.c = new Object();
        this.d = new AtomicBoolean(false);
        if (string == null) {
            AppboyLogger.e(dr.a, "ServerConfigStorageProvider received null api key.");
            string = "";
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append(".");
            sb.append(string);
            string = sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("com.appboy.storage.serverconfigstorageprovider");
        sb2.append(string);
        this.b = context.getSharedPreferences(sb2.toString(), 0);
        new a().execute((Object[])new Void[0]);
    }
    
    private Set<String> a(String string) {
        try {
            string = this.b.getString(string, "");
            if (StringUtils.isNullOrBlank(string)) {
                return null;
            }
            final JSONArray jsonArray = new JSONArray(string);
            final HashSet<String> set = new HashSet<String>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                set.add(jsonArray.getString(i));
            }
            return set;
        }
        catch (Exception ex) {
            AppboyLogger.w(dr.a, "Experienced exception retrieving blacklisted strings from local storage. Returning null.", ex);
            return null;
        }
    }
    
    public void a(final co e) {
        Object o = this.c;
        synchronized (o) {
            this.e = e;
            // monitorexit(o)
            try {
                o = this.b.edit();
                if (e.b() != null) {
                    ((SharedPreferences$Editor)o).putString("blacklisted_events", new JSONArray((Collection)e.b()).toString());
                }
                if (e.c() != null) {
                    ((SharedPreferences$Editor)o).putString("blacklisted_attributes", new JSONArray((Collection)e.c()).toString());
                }
                if (e.d() != null) {
                    ((SharedPreferences$Editor)o).putString("blacklisted_purchases", new JSONArray((Collection)e.d()).toString());
                }
                ((SharedPreferences$Editor)o).putLong("config_time", e.a());
                ((SharedPreferences$Editor)o).putBoolean("location_enabled", e.f());
                ((SharedPreferences$Editor)o).putBoolean("location_enabled_set", e.g());
                ((SharedPreferences$Editor)o).putLong("location_time", e.h());
                ((SharedPreferences$Editor)o).putFloat("location_distance", e.i());
                ((SharedPreferences$Editor)o).putInt("geofences_min_time_since_last_request", e.j());
                ((SharedPreferences$Editor)o).putInt("geofences_min_time_since_last_report", e.k());
                ((SharedPreferences$Editor)o).putInt("geofences_max_num_to_register", e.l());
                ((SharedPreferences$Editor)o).putBoolean("geofences_enabled", e.m());
                ((SharedPreferences$Editor)o).putBoolean("geofences_enabled_set", e.n());
                ((SharedPreferences$Editor)o).putLong("messaging_session_timeout", e.e());
                ((SharedPreferences$Editor)o).putBoolean("test_user_device_logging_enabled", e.o());
                ((SharedPreferences$Editor)o).apply();
            }
            catch (Exception ex) {
                AppboyLogger.w(dr.a, "Could not persist server config to shared preferences.", ex);
            }
        }
    }
    
    public void a(final boolean b) {
        this.d.set(b);
    }
    
    public boolean a() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.n();
            }
            return this.b.getBoolean("geofences_enabled_set", false);
        }
    }
    
    public boolean b() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.m();
            }
            return this.b.getBoolean("geofences_enabled", false);
        }
    }
    
    public boolean c() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.f();
            }
            return this.b.getBoolean("location_enabled_set", false);
        }
    }
    
    public boolean d() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.f();
            }
            return this.b.getBoolean("location_enabled", false);
        }
    }
    
    public int e() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.j();
            }
            return this.b.getInt("geofences_min_time_since_last_request", -1);
        }
    }
    
    public int f() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.k();
            }
            return this.b.getInt("geofences_min_time_since_last_report", -1);
        }
    }
    
    public int g() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.l();
            }
            return this.b.getInt("geofences_max_num_to_register", -1);
        }
    }
    
    public long h() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.h();
            }
            return this.b.getLong("location_time", -1L);
        }
    }
    
    public long i() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.e();
            }
            return this.b.getLong("messaging_session_timeout", -1L);
        }
    }
    
    public float j() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.i();
            }
            return this.b.getFloat("location_distance", -1.0f);
        }
    }
    
    public long k() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.a();
            }
            return this.b.getLong("config_time", 0L);
        }
    }
    
    public Set<String> l() {
        synchronized (this.c) {
            Set<String> set;
            if (this.e != null) {
                set = this.e.b();
            }
            else {
                set = this.a("blacklisted_events");
            }
            if (set != null) {
                return set;
            }
            return new HashSet<String>();
        }
    }
    
    public Set<String> m() {
        synchronized (this.c) {
            Set<String> set;
            if (this.e != null) {
                set = this.e.c();
            }
            else {
                set = this.a("blacklisted_attributes");
            }
            if (set != null) {
                return set;
            }
            return new HashSet<String>();
        }
    }
    
    public Set<String> n() {
        synchronized (this.c) {
            Set<String> set;
            if (this.e != null) {
                set = this.e.d();
            }
            else {
                set = this.a("blacklisted_purchases");
            }
            if (set != null) {
                return set;
            }
            return new HashSet<String>();
        }
    }
    
    public boolean o() {
        synchronized (this.c) {
            if (this.e != null) {
                return this.e.o();
            }
            return this.b.getBoolean("test_user_device_logging_enabled", false);
        }
    }
    
    public boolean p() {
        return this.d.get();
    }
    
    class a extends AsyncTask<Void, Void, Void>
    {
        private a() {
        }
        
        protected Void a(final Void... array) {
            final co co = new co();
            co.b(dr.this.m());
            co.a(dr.this.l());
            co.c(dr.this.n());
            co.a(dr.this.k());
            co.c(dr.this.i());
            co.a(dr.this.d());
            co.b(dr.this.c());
            co.b(dr.this.h());
            co.a(dr.this.j());
            co.a(dr.this.e());
            co.b(dr.this.f());
            co.c(dr.this.g());
            co.c(dr.this.b());
            co.d(dr.this.a());
            co.e(dr.this.o());
            synchronized (dr.this.c) {
                dr.this.e = co;
                return null;
            }
        }
    }
}
