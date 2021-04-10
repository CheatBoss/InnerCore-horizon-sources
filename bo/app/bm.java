package bo.app;

import com.appboy.support.*;
import android.content.*;

public class bm
{
    private static final String d;
    final SharedPreferences a;
    final ad b;
    boolean c;
    private final dr e;
    
    static {
        d = AppboyLogger.getAppboyLogTag(bm.class);
    }
    
    public bm(final Context context, final ad b, final dr e) {
        this.c = false;
        this.b = b;
        this.e = e;
        this.a = context.getSharedPreferences("com.appboy.storage.sessions.messaging_session", 0);
    }
    
    void a() {
        if (this.c()) {
            AppboyLogger.d(bm.d, "Publishing new messaging session event.");
            this.b.a(al.a, al.class);
            this.c = true;
            return;
        }
        AppboyLogger.d(bm.d, "Messaging session not started.");
    }
    
    void b() {
        final long a = du.a();
        final String d = bm.d;
        final StringBuilder sb = new StringBuilder();
        sb.append("Messaging session stopped. Adding new messaging session timestamp: ");
        sb.append(a);
        AppboyLogger.d(d, sb.toString());
        this.a.edit().putLong("messaging_session_timestamp", a).apply();
        this.c = false;
    }
    
    boolean c() {
        final long i = this.e.i();
        if (i != -1L && !this.c) {
            final long long1 = this.a.getLong("messaging_session_timestamp", -1L);
            final long a = du.a();
            final String d = bm.d;
            final StringBuilder sb = new StringBuilder();
            sb.append("Messaging session timeout: ");
            sb.append(i);
            sb.append(", current diff: ");
            sb.append(a - long1);
            AppboyLogger.d(d, sb.toString());
            if (long1 + i < a) {
                return true;
            }
        }
        return false;
    }
}
